package com.dp.de.run.management.plugin.service.ssh;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.HTTPProxyData;
import com.youngdatafan.di.run.management.DiRunManagementApplication;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.util.Utils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, SSHTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SSHTest {


    public boolean SSHConnect(String realservername, String realserverpassword, int realserverport,
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

        // Authenticate

        boolean isAuthenticated;
        if (publicpublickey) {

            isAuthenticated = conn.authenticateWithPublicKey(realUsername, new File(realkeyFilename), realkeyPass);
        } else {
            isAuthenticated = conn.authenticateWithPassword(realUsername, realserverpassword);
        }



        return isAuthenticated;

    }

    @Test
    public void testSSHconnect() throws Exception {
        boolean b = SSHConnect("192.168.138.187", "Admin@1235", 22, "root",
                "Admin@123", "22", "root", "Admin@123",
                22, "", "", false, 20, false);
        System.out.println("_______________"+b);
    }


    @Test
    public void testSSHncryptPasswor() throws Exception {
        System.out.println("————————————————————————————————————————————");
        System.out.println(Encr.encryptPasswordIfNotUsingVariables("123"));
    }
}
