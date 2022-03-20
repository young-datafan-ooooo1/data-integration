package com.youngdatafan.portal.system.management.user.vo;

import com.youngdatafan.dataintegration.core.validation.FieldMatch;
import com.youngdatafan.portal.system.management.util.ValidatorConstans;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/11 1:44 下午
 */
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword"
                , message = "{user.password.fieldMatch}")})
public class UpdatePasswdVo {
    @ApiModelProperty("原密码")
    @NotBlank
    private String oldPassword;

    @ApiModelProperty("密码")
    @NotBlank
    @Pattern(regexp = ValidatorConstans.PWD,
            message = "{user.password.incorrect}")
    private String password;

    @ApiModelProperty("确认密码")
    @NotBlank
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
