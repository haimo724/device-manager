package com.sicau.devicemanager.POJO.DTO;

import com.sicau.devicemanager.config.validation.group.CommonValidatedGroup;
import com.sicau.devicemanager.config.validation.group.DeviceValidatedGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 注册用户传输的数据
 * @author BeFondOfTaro
 * Created at 15:11 2018/5/14
 */
@Data
public class UserRegisterDTO {

    /**
     * 用户名
     */
    @NotNull(message = "用户id不能为空",groups = {DeviceValidatedGroup.modifyUser.class})
    @Pattern(regexp = "^\\d*$", message = "id只能是数字",groups = CommonValidatedGroup.LegalityGroup.class)
    private String userId;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空",groups = {DeviceValidatedGroup.addUser.class})
    @Pattern(regexp = "^[a-zA-Z\u4e00-\u9fa5_0-9]+$", groups = CommonValidatedGroup.LegalityGroup.class, message = "设username只能包含汉字、英文字母、数字、下划线")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空",groups = {DeviceValidatedGroup.addUser.class})
    private String password;

    /**
     * 真实姓名
     */
    @NotNull(message = "真实姓名不能为空",groups = {DeviceValidatedGroup.addUser.class})
    @Pattern(regexp = "^[a-zA-Z\u4e00-\u9fa5_0-9]+$", groups = CommonValidatedGroup.LegalityGroup.class, message = "realName只能为汉字")
    private String realName;

    /**
     * 邮箱
     */
    @Email(groups = CommonValidatedGroup.LegalityGroup.class)
    private String email;

    /**
     * 电话
     */
    @NotNull(message = "电话不能为空",groups = {DeviceValidatedGroup.addUser.class})
    @Pattern(regexp = "^\\d+$", message = "phone只能是数字",groups = CommonValidatedGroup.LegalityGroup.class)
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 角色id
     */
    @NotNull(message = "角色不能为空",groups = {DeviceValidatedGroup.addUser.class})
    @Pattern(regexp = "^\\d*$", message = "roleId只能是数字",groups = CommonValidatedGroup.LegalityGroup.class)
    private String roleId;
}
