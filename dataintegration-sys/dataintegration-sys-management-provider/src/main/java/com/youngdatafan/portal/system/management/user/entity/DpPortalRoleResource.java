package com.youngdatafan.portal.system.management.user.entity;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/2/11 7:56 下午
*/
public class DpPortalRoleResource {
    /**
    * 角色编号(PK)
    */
    private String roleId;

    /**
    * 资源编号(PK)
    */
    private String resourceId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", resourceId=").append(resourceId);
        sb.append("]");
        return sb.toString();
    }
}