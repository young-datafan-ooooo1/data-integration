import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author gavin
 * @since 2020/8/25 10:44 上午
 */
public class Demo {

    public static void main(String[] args) {
        final HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(String.valueOf(i), "xx");
        }

        String key = "a";
        int h;
        // 如果key为null，则hash值为0，否则调用key的hashCode()方法
        // 并让高16位与整个hash异或，这样做是为了使计算出的hash更分散
        System.out.println( (key.hashCode() >>> 16));
        System.out.println(16 & 12321312);

        final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();



    }
}
