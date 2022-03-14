import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;


/**
 * @author gavin
 * @since 2020/2/24 11:13 上午
 */
public class EncryptConfigTest {

    public static void main(String[] args) {
        String password = "fdasjfldjskfkf787fda9r321";
        String data = "Prime@2020";

        PooledPBEStringEncryptor encrypt = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        config.setProviderClassName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.salt.NoOpIVGenerator");
        config.setStringOutputType("base64");
        encrypt.setConfig(config);

        System.out.println(encrypt.encrypt(data));

    }

}
