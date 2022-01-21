package com.youngdatafan.portal.system.management.user.entity;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/2/10 6:14 下午
*/
public class DpPortalUserRole {
    /**
    * 用户编号(PK)
    */
    private String userId;

    /**
    * 角色编号(PK)
    */
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", roleId=").append(roleId);
        sb.append("]");
        return sb.toString();
    }
}