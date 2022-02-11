import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.pentaho.di.core.vfs.KettleVFS;

public class KettleVfsExample {


    public static void main(String[] args) throws Exception{
        StaticUserAuthenticator auth = new StaticUserAuthenticator("username", "prime", "prime@2020");

        FileSystemOptions opts = new FileSystemOptions();
        // 账号信息
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
        // 被动模式
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);

        FileObject fileObject = KettleVFS.getFileObject("ftp://192.168.124.166:20021/51149b4a-86eb-4e5d-85ea-3f1529ac5b49/8e17f09b46284153bf076e0ab6f576eb/8979d719f41f4edbb58f94222400c14e", opts);
        System.out.println(fileObject.exists());


    }
}
