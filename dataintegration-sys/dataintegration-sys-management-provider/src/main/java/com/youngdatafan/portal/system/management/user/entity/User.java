package com.youngdatafan.portal.system.management.user.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户对象.
 */
@ApiModel("用户对象")
@Data
public class User {
    private String username;

    private String password;

    private Integer age;
}
