package com.sicau.devicemanager.POJO.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sicau.devicemanager.POJO.DO.Brand;
import com.sicau.devicemanager.POJO.DO.Category;
import com.sicau.devicemanager.POJO.DO.Device;
import com.sicau.devicemanager.POJO.DO.Location;
import com.sicau.devicemanager.POJO.RO.DeviceQueryKeyCondition;
import com.sicau.devicemanager.config.validation.group.DeviceValidatedGroup.AddDeviceGroup;
import com.sicau.devicemanager.config.validation.group.DeviceValidatedGroup.QueryDeviceGroup;
import com.sicau.devicemanager.config.validation.group.DeviceValidatedGroup.UpdateDeviceGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Yazhe
 * Created at 17:17 2018/8/7
 */
@JsonInclude(Include.NON_NULL)
public class DeviceDTO extends Device {

    /**
     * 品牌
     */
    private Brand brand;

    @NotNull(message = "brandId不能为空",groups = {AddDeviceGroup.class, UpdateDeviceGroup.class})
    private String brandId;

    /**
     * 分类id
     */
    @NotNull(message = "分类不能为空",groups = {AddDeviceGroup.class, UpdateDeviceGroup.class})
    @NotEmpty(message = "分类不能为空",groups = {AddDeviceGroup.class, UpdateDeviceGroup.class})
    private List<String> categoryIds;

    private String categoryId;

    private String locationStr;

    @Valid
    private Location location;

    private String categoryStr;

    @Valid
    private Category category;

    private String workNature;

    private String custodian;

    private String amountUnit;

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    @Valid
    @NotNull(groups = {QueryDeviceGroup.class}, message = "分页参数不能为空")
    private QueryPage queryPage;

    private String queryKey;

	/**
	 * 搜索选项
	 */
	private DeviceQueryKeyCondition deviceQueryKeyCondition;

    private List<Location> locationList;

    private List<String> locationIds;

    private String userId;

    /**
     * 使用部门
     */
    private String useDepartment;

    /**
     * 设备型号
     */
    private String deviceModel;

	public DeviceQueryKeyCondition getDeviceQueryKeyCondition() {
		return deviceQueryKeyCondition;
	}

	public void setDeviceQueryKeyCondition(DeviceQueryKeyCondition deviceQueryKeyCondition) {
		this.deviceQueryKeyCondition = deviceQueryKeyCondition;
	}

	public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getUseDepartment() {
        return useDepartment;
    }

    public void setUseDepartment(String useDepartment) {
        this.useDepartment = useDepartment;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<String> locationIds) {
        this.locationIds = locationIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public QueryPage getQueryPage() {
        return queryPage;
    }

    public void setQueryPage(QueryPage queryPage) {
        this.queryPage = queryPage;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    public String getWorkNature() {
        return workNature;
    }

    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }

    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public DeviceDTO() {
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
                "brand=" + brand +
                ", categoryIds=" + categoryIds +
                ", categoryId='" + categoryId + '\'' +
                ", locationStr='" + locationStr + '\'' +
                ", location=" + location +
                ", categoryStr='" + categoryStr + '\'' +
                ", category=" + category +
                ", workNature='" + workNature + '\'' +
                ", custodian='" + custodian + '\'' +
                ", amountUnit='" + amountUnit + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", queryPage=" + queryPage +
                ", queryKey='" + queryKey + '\'' +
                ", locationList=" + locationList +
                ", locationIds=" + locationIds +
                ", userId='" + userId + '\'' +
                "} " + super.toString();
    }
}
