package com.youngdatafan.dataintegration.core.util;

import java.io.Serializable;

/**
 * 键值对存储.
 *
 * @param <K> k
 * @param <V> v
 * @author renhua.zhang
 */
public class Pair<K, V> implements Serializable {

    private static final long serialVersionUID = -3245815422835418379L;

    private K k;

    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public Pair() {
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

}
