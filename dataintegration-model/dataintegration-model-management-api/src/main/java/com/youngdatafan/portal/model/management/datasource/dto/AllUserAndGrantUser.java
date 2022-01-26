package com.youngdatafan.portal.model.management.datasource.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/12/1 1:15 下午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class AllUserAndGrantUser {

    private List<UserListDTO> allUser;

    private List<UserListDTO> grantUser;

    public AllUserAndGrantUser(List<UserListDTO> allUser, List<UserListDTO> grantUser) {
        this.allUser = allUser;
        this.grantUser = grantUser;
    }

    public List<UserListDTO> getAllUser() {
        return allUser;
    }

    public void setAllUser(List<UserListDTO> allUser) {
        this.allUser = allUser;
    }

    public List<UserListDTO> getGrantUser() {
        return grantUser;
    }

    public void setGrantUser(List<UserListDTO> grantUser) {
        this.grantUser = grantUser;
    }
}
