package com.youngdatafan.dataintegration.core.util.struct;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 支持多线程阻塞模式的Map对象。当获取某个Key对应的对象时，若对象不存在，则一直阻塞（可设置timeout致超时）
 * 主要应用于生产者和消费者模式下的数据获取的同步
 * 用于报文处理 ： registe + put 方法是为了防止数据过期不销毁：
 * 在数据写入的时候先注册，数据返回的时候判断是否已经注册。如果没有注册则不做处理
 * 如果数据在超时时间之后返回（我们put的时候不判断，数据就会永远的存在map中）.
 *
 * @author renhua.zhang
 * @since 2017-10-28 10:32
 */
public class HashBlockingMap<K, V> implements BlockingMap<K, V> {
    private final ReentrantLock lock = new ReentrantLock();

    private ConcurrentMap<K, Item<V>> map;

    public HashBlockingMap() {
        map = new ConcurrentHashMap<K, Item<V>>();
    }

    @Override
    public void registe(K key) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (!map.containsKey(key)) {
                map.put(key, new Item<>());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V v) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (map.containsKey(key)) {
                Item<V> item = map.get(key);
                item.put(v);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V take(K key) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (!map.containsKey(key)) {
                map.put(key, new Item<>());
            }
        } finally {
            lock.unlock();
        }

        Item<V> item = map.get(key);
        V x = item.take();
        map.remove(key);

        return x;
    }

    @Override
    public V poll(K key, long timeout) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (!map.containsKey(key)) {
                map.put(key, new Item<>());
            }
        } finally {
            lock.unlock();
        }

        Item<V> item = map.get(key);
        V x = item.poll(timeout);
        map.remove(key);

        return x;
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    private static class Item<E> {

        private final ReentrantLock lock = new ReentrantLock();

        private final Condition cond = lock.newCondition();

        private E obj;

        private void put(E o) throws InterruptedException {
            if (o == null) {
                throw new NullPointerException();
            }
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                obj = o;
                cond.signal();
            } finally {
                lock.unlock();
            }
        }

        E take() throws InterruptedException {
            E x;
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                try {
                    while (obj == null) {
                        cond.await();
                    }
                } catch (InterruptedException e) {
                    cond.signal();
                    throw e;
                }
                x = obj;
            } finally {
                lock.unlock();
            }
            return x;
        }

        private E poll(long timeout) throws InterruptedException {
            long timeoutNanos = TimeUnit.MILLISECONDS.toNanos(timeout);
            E x;
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                for (; ;) {
                    if (obj != null) {
                        x = obj;
                        break;
                    }
                    if (timeoutNanos <= 0) {
                        return null;
                    }
                    try {
                        timeoutNanos = cond.awaitNanos(timeoutNanos);
                    } catch (InterruptedException e) {
                        cond.signal();
                        throw e;
                    }
                }
            } finally {
                lock.unlock();
            }
            return x;
        }
    }
}
