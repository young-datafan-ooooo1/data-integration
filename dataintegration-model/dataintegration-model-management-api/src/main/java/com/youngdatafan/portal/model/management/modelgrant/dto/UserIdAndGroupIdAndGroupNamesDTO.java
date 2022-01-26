package com.youngdatafan.portal.model.management.modelgrant.dto;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/25 10:12 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class UserIdAndGroupIdAndGroupNamesDTO {

    private String userId;

    private List<GroupIdAndGroupNamesDTO> list;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<GroupIdAndGroupNamesDTO> getList() {
        return list;
    }

    public void setList(List<GroupIdAndGroupNamesDTO> list) {
        this.list = list;
    }
}
