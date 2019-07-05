package lambda;

/**
 * created by XUAN on 2019/7/2
 */
public class Activity {

    private Long activityId;
    private String activityUrl;
    private Long appliedCount;
    private Long couponId;
    private String createUser;
    private Long personLimitCount;
    private String status;
    private Long totalCount;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public Long getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(Long appliedCount) {
        this.appliedCount = appliedCount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getPersonLimitCount() {
        return personLimitCount;
    }

    public void setPersonLimitCount(Long personLimitCount) {
        this.personLimitCount = personLimitCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
