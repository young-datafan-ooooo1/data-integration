package com.youngdatafan.portal.model.management.datasource.dto;

import java.util.List;
import lombok.Data;

/**
 * AllUserAndGrantUser.
 */
@Data
public class AllUserAndGrantUser {

    private List<UserListDTO> allUser;

    private List<UserListDTO> grantUser;

    public AllUserAndGrantUser(List<UserListDTO> allUser, List<UserListDTO> grantUser) {
        this.allUser = allUser;
        this.grantUser = grantUser;
    }

}
