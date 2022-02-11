package com.youngdatafan.di.run.management.steps.ssh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "SSh 变量")
public class SSHVO {
    @ApiModelProperty(value = "地址名称")
    private String realservername;
    @ApiModelProperty(value = "密码")
    private String realserverpassword;
    private int realserverport;
    private String realUsername;
    private String realPassword;
    private String realproxyhost;
    private String realproxyusername;
    private String realproxypassword;
    private int realproxyport;
    private String realkeyFilename;
    private String realkeyPass;
    private Boolean useproxy;
    private Integer timeout;
    private Boolean publicpublickey;
}
