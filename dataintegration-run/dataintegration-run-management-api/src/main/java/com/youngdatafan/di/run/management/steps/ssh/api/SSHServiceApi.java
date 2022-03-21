package com.youngdatafan.di.run.management.steps.ssh.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.ssh.vo.SSHVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Api(tags = "SSH_API接口")
public interface SSHServiceApi {

    @ApiOperation(value = "测试链接", produces = "application/json")
    @PostMapping(value = "/testSSHConnect")
    Result testSSHConnect(@RequestBody SSHVO sshvo);

    @ApiOperation(value = "pem文件上传", produces = "application/json")
    @PostMapping("/upload")
    Result  upload(@RequestParam("file") MultipartFile file) ;
}
