import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author gavin
 * @since 2020/8/25 10:44 上午
 */
public class Demo1 {
    private final static List<String> data = new ArrayList<>();
    private final static Map<String, String> map = new HashMap<>();
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final static Lock readLock = readWriteLock.readLock();
    private final static Lock writeLock = readWriteLock.writeLock();

    public static void main(String[] args) throws InterruptedException {
//        线程进入读锁的前提条件：
//        没有其他线程的写锁，
//        没有写请求或者有写请求，但调用线程和持有锁的线程是同一个。
//        线程进入写锁的前提条件：
//        没有其他线程的读锁
//        没有其他线程的写锁


        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {
                    read();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        write();

        TimeUnit.SECONDS.sleep(10);
    }

    public static void write() throws InterruptedException {
        try {
            writeLock.lock();
            data.add("写数据");
            System.out.println(Thread.currentThread().getName() + ": 写数据");
            TimeUnit.SECONDS.sleep(3);
        } finally {
            writeLock.unlock();
        }
    }

    public static void read() throws InterruptedException {
        try {
            readLock.lock();
//            for (String datum : data) {
            System.out.println(Thread.currentThread().getName() + ":读数据");
//            }
            TimeUnit.SECONDS.sleep(3);
        } finally {
            readLock.unlock();
        }
    }

}
