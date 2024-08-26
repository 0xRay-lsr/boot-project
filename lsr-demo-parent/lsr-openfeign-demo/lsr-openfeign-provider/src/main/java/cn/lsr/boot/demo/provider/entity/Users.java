package cn.lsr.boot.demo.provider.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@TableName("t_users")
public class Users {

    /**
     * 用户ID，自动递增唯一标识
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户的名字
     */
    @TableField("first_name")
    private String firstName;

    /**
     * 用户的姓氏
     */
    @TableField("last_name")
    private String lastName;

    /**
     * 用户名，唯一且不能为空
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码，使用加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 用户邮箱，唯一且不能为空
     */
    @TableField("email")
    private String email;

    /**
     * 用户电话号码，可为空
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 用户地址，可为空
     */
    @TableField("address")
    private String address;

    /**
     * 用户出生日期，可为空
     */
    @TableField("date_of_birth")
    private Date dateOfBirth;

    /**
     * 账户创建时间，默认为当前时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 账户更新时间，每次更新时自动修改为当前时间
     */
    @TableField("update_time")
    private Date updateTime;

    public Integer getUserId() {
        return userId;
    }

    public Users setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
    public String getFirstName() {
        return firstName;
    }

    public Users setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public String getLastName() {
        return lastName;
    }

    public Users setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    public String getUsername() {
        return username;
    }

    public Users setUsername(String username) {
        this.username = username;
        return this;
    }
    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public Users setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Users setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public String getAddress() {
        return address;
    }

    public Users setAddress(String address) {
        this.address = address;
        return this;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Users setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Users setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Users setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "Users{" +
            "userId=" + userId +
            ", firstName=" + firstName +
            ", lastName=" + lastName +
            ", username=" + username +
            ", password=" + password +
            ", email=" + email +
            ", phoneNumber=" + phoneNumber +
            ", address=" + address +
            ", dateOfBirth=" + dateOfBirth +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
