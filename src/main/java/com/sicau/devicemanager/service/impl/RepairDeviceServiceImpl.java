package com.sicau.devicemanager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sicau.devicemanager.POJO.DO.Device;
import com.sicau.devicemanager.POJO.DO.DeviceStatusRecord;
import com.sicau.devicemanager.POJO.DO.RepairOrder;
import com.sicau.devicemanager.POJO.DTO.QueryPage;
import com.sicau.devicemanager.POJO.DTO.RepairOrderDTO;
import com.sicau.devicemanager.constants.DeviceStatusEnum;
import com.sicau.devicemanager.dao.DeviceMapper;
import com.sicau.devicemanager.dao.DeviceStatusRecordMapper;
import com.sicau.devicemanager.dao.RepairOrderMapper;
import com.sicau.devicemanager.dao.UserMapper;
import com.sicau.devicemanager.service.RepairDeviceService;
import com.sicau.devicemanager.util.KeyUtil;
import com.sicau.devicemanager.util.web.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sicau.devicemanager.POJO.DO.Role;
import com.sicau.devicemanager.POJO.DTO.QueryPage;
import com.sicau.devicemanager.POJO.DTO.RepairOrderDTO;
import com.sicau.devicemanager.POJO.DTO.UserDTO;
import com.sicau.devicemanager.config.exception.ResourceException;
import com.sicau.devicemanager.constants.*;
import com.sicau.devicemanager.config.exception.CommonException;
import com.sicau.devicemanager.constants.DeviceStatusEnum;
import com.sicau.devicemanager.constants.OrderStatusEnum;
import com.sicau.devicemanager.constants.ResultEnum;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RepairDeviceServiceImpl implements RepairDeviceService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceStatusRecordMapper deviceStatusRecordMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public void submitRepairDeviceOrder(RepairOrder repairOrder) {

        //插入维修订单信息
        repairOrder.setApplyUserId(RequestUtil.getCurrentUserId());
        //状态变成 维修中
        repairOrder.setStatusCode(OrderStatusEnum.TO_BE_REPAIRED.getCode());
        repairOrderMapper.insertSelective(repairOrder);

        String deviceId = repairOrder.getDeviceId();
        //查询设备原始记录
        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        //修改设备状态
        deviceMapper.updateStatusIdById(deviceId, DeviceStatusEnum.FIXING.getCode());

        //插入设备状态修改记录
        deviceStatusRecordMapper.insert(new DeviceStatusRecord(
                //生成唯一的主键 格式: 时间+随机数
                KeyUtil.genUniqueKey(),

                deviceId,
                device.getStatusId(),
                DeviceStatusEnum.FIXING.getCode(),
                device.getLocationId(),
                device.getLocationId(),
                RequestUtil.getCurrentUserId()
        ));
    }

    /**
     * 删除维修订单(申请用户可删除自己的，管理员都可以删除
     *
     * @param id 订单id
     * @return
     * @author pettrgo
     */
    @Override
    public boolean deleteRepairDeviceOrder(Integer id) {
        String userId = RequestUtil.getCurrentUserId();
        //获取用户角色信息
        UserDTO userDTO = userMapper.getUserById(userId);
        RepairOrder repairOrder = repairOrderMapper.selectByPrimaryKey(id);
        if (userDTO == null) {
            throw new ResourceException(ResourceExceptionEnum.RESOURCE_NOT_FOUND, ResourceConstants.USER);
        }
        if (repairOrder == null) {
            throw new ResourceException(ResourceExceptionEnum.RESOURCE_NOT_FOUND, ResourceConstants.USER);
        }
        for (Role role : userDTO.getRoleList()) {
            switch (role.getName()) {
                case "管理员":
                    repairOrderMapper.deleteByPrimaryKey(id);
                    return true;
                default:
                    if (repairOrder.getApplyUserId().equals(userId)) {
                        repairOrderMapper.deleteByPrimaryKey(id);
                        return true;
                    }
            }
        }
        return false;
    }

    @Override
    public void modifyOrder(RepairOrder repairOrder) {
        if (repairOrder == null ||  StringUtils.isEmpty(repairOrder.getId())) {
            throw new CommonException(ResultEnum.ORDER_ID_NOT_PRESENT);
        }
        //订单还没开始处理才可以修改
        RepairOrder oldOrder = repairOrderMapper.selectByPrimaryKey(repairOrder.getId());
        if (oldOrder.getStatusCode()!=OrderStatusEnum.TO_BE_REPAIRED.getCode()){
            throw new CommonException(ResultEnum.ORDER_CANNOT_MODIFY);
        }
        repairOrderMapper.updateByPrimaryKeySelective(repairOrder);
    }

    @Override
    public List<RepairOrder> getOrdersByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            throw new CommonException(ResultEnum.DEVICE_ID_NOT_PRESENT);
        }
        return repairOrderMapper.getOrdersByDeviceId(deviceId);
    }

    @Override
    public void finishOrder(int orderId, OrderStatusEnum orderStatusEnum) {
        RepairOrder oldOrder = repairOrderMapper.selectByPrimaryKey(orderId);
        //订单不是维修中，将不能完结
        if (oldOrder.getStatusCode()!=OrderStatusEnum.IN_MAINTENANCE.getCode()){
            throw new CommonException(ResultEnum.ORDER_CANNOT_FINISHED);
        }
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setId(orderId);
        repairOrder.setStatusCode(orderStatusEnum.getCode());
        repairOrderMapper.updateByPrimaryKeySelective(repairOrder);
    }

    @Override
    public void finishOrder(int orderId, DeviceStatusEnum deviceStatusEnum) {
        RepairOrder oldOrder = repairOrderMapper.selectByPrimaryKey(orderId);
        //订单不是处理完成，将不能修改设备状态
        if (oldOrder.getStatusCode()==OrderStatusEnum.IN_MAINTENANCE.getCode()||oldOrder.getStatusCode()==OrderStatusEnum.TO_BE_REPAIRED.getCode()){
            throw new CommonException(ResultEnum.ORDERS_DEVICE_STATUS_CANNOT_CHANGE);
        }

        String deviceId = oldOrder.getDeviceId();
        Device oldDevice = deviceMapper.selectByPrimaryKey(deviceId);
        //设备状态只能从维修改为其他
        if (oldDevice.getStatusId() != DeviceStatusEnum.FIXING.getCode()) {
            throw new CommonException(ResultEnum.ORDERS_DEVICE_STATUS_CANNOT_CHANGE);
        }
        //能提交订单成功，设备不可能为空，所以直接改变
        deviceMapper.updateStatusIdById(deviceId, deviceStatusEnum.getCode());
        //插入设备状态修改记录
        deviceStatusRecordMapper.insert(new DeviceStatusRecord(
                //生成唯一的主键 格式: 时间+随机数
                KeyUtil.genUniqueKey(),
                deviceId,
                oldDevice.getStatusId(),
                deviceStatusEnum.getCode(),
                oldDevice.getLocationId(),
                oldDevice.getLocationId(),
                RequestUtil.getCurrentUserId()
        ));
    }

}