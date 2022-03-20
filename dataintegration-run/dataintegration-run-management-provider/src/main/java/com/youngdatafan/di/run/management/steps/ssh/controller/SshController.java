package com.youngdatafan.di.run.management.steps.ssh.controller;


import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.ssh.api.SSHServiceApi;
import com.youngdatafan.di.run.management.steps.ssh.vo.SSHVO;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.HTTPProxyData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.pentaho.di.core.util.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/ssh")
public class SshController implements SSHServiceApi {
    @Override
    public Result testSSHConnect(SSHVO sshvo) {
        try {
            Boolean  aBoolean=  SSHConnect(sshvo.getRealservername(),sshvo.getRealserverpassword(),sshvo.getRealserverport(),
                      sshvo.getRealUsername(),sshvo.getRealPassword(),sshvo.getRealproxyhost(),sshvo.getRealproxyusername(),
                      sshvo.getRealproxypassword(),sshvo.getRealproxyport(),sshvo.getRealkeyFilename(),sshvo.getRealkeyPass(),
                      sshvo.getUseproxy(),sshvo.getTimeout(),sshvo.getPublicpublickey());
            if(!aBoolean){
                return Result.fail(StatusCode.CODE_10010.getCode(), "", "ssh连接失败");
            }
            return Result.success(aBoolean);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "ssh连接异常");
        }


    }

    @Override
    public Result upload(MultipartFile file)  {
        try {
            InputStream inputStream = file.getInputStream();
            String result = IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
            return Result.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "文件读取异常");

        }

    }


    public static boolean SSHConnect(String realservername, String realserverpassword, int realserverport,
                              String realUsername, String realPassword, String realproxyhost, String realproxyusername,
                              String realproxypassword, int realproxyport, String realkeyFilename, String realkeyPass,
                              Boolean useproxy, Integer timeout, boolean publicpublickey) throws Exception {
        /* Create a connection instance */
        Connection conn = new Connection(realservername, realserverport);
        /* We want to connect through a HTTP proxy */
        if (useproxy) {
            conn.setProxyData(new HTTPProxyData(realproxyhost, realproxyport));
            /* Now connect */
            // if the proxy requires basic authentication:
            if (!Utils.isEmpty(realproxyusername) || !Utils.isEmpty(realproxypassword)) {
                conn.setProxyData(new HTTPProxyData(realproxyhost, realproxyport, realproxyusername, realproxypassword));
            }
        }
        if (timeout > 0) {
            // Use timeout
            conn.connect(null, 0, timeout * 1000);
        } else {
            // Cache Host Key
            conn.connect();
        }
        boolean isAuthenticated;
        if (publicpublickey) {
            isAuthenticated = conn.authenticateWithPublicKey(realUsername, realkeyFilename.toCharArray(), realkeyPass);
        } else {
            isAuthenticated = conn.authenticateWithPassword(realUsername, realserverpassword);
        }
        return isAuthenticated;
    }
}
