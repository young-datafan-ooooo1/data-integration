package com.youngdatafan.dataintegration.core.util.struct;

/**
 * 实现阻塞的Map， 用于等待并获取某个指定Key的对象到来 主要用于生产者消费者模式的数据同步.
 * 开发逻辑主要参考：http://songsong.iteye.com/blog/802881
 *
 * @author renhua.zhang
 * @since 2017-10-28 10:32
 */
public interface BlockingMap<K, V> {

    /**
     * 先注册，再调用put方法.
     *
     * @param key key
     * @throws InterruptedException lock error.
     */
    void registe(K key) throws InterruptedException;

    /**
     * 用于生产者插入返回数据，如果数据没有注册则销毁该数据.
     *
     * @param key key
     * @param v   values
     * @throws InterruptedException lock error.
     */
    void put(K key, V v) throws InterruptedException;

    /**
     * 用于消费者获取数据，若未获取到则一直等待（有风险，可能永远阻塞）.
     *
     * @param key key
     * @return value
     * @throws InterruptedException lock error.
     */
    V take(K key) throws InterruptedException;

    /**
     * 用于消费者获取数据，超时退出.
     *
     * @param key     key
     * @param timeout 超时时间为毫秒
     * @return value
     * @throws InterruptedException lock error.
     */
    V poll(K key, long timeout) throws InterruptedException;

    /**
     * 删除数据，用于对已经注册了，由于业务失败需要删除的需求.
     *
     * @param key key
     */
    void remove(K key);

    /**
     * 清空数据.
     */
    void clear();

    /**
     * 获取map 大小.
     *
     * @return size
     */
    int size();
}
