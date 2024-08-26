package cn.lsr.boot.demo.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账户信息表
 * </p>
 *
 * @author lishirui
 * @since 2024-07-30 05:30:46
 */
@TableName("t_accounts")
public class Accounts {

    /**
     * 账户ID，自动递增唯一标识
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Integer accountId;

    /**
     * 用户ID，关联用户信息表
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 账户类型，储蓄账户或支票账户
     */
    @TableField("account_type")
    private String accountType;

    /**
     * 账户余额，默认为0.00
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 账户货币类型，默认为美元
     */
    @TableField("currency")
    private String currency;

    /**
     * 账户创建时间，默认为当前时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 账户更新时间，每次更新时自动修改为当前时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * 账户状态，默认为活跃
     */
    @TableField("status")
    private String status;

    public Integer getAccountId() {
        return accountId;
    }

    public Accounts setAccountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }
    public Integer getUserId() {
        return userId;
    }

    public Accounts setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
    public String getAccountType() {
        return accountType;
    }

    public Accounts setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public Accounts setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
    public String getCurrency() {
        return currency;
    }

    public Accounts setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public Accounts setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Accounts setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public String getStatus() {
        return status;
    }

    public Accounts setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Accounts{" +
            "accountId=" + accountId +
            ", userId=" + userId +
            ", accountType=" + accountType +
            ", balance=" + balance +
            ", currency=" + currency +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", status=" + status +
        "}";
    }
}
