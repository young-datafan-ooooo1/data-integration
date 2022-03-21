package com.youngdatafan.dataintegration.core.util.encryption.sm;

import lombok.Getter;
import lombok.Setter;

/**
 * sm4 上下文对象.
 *
 * @author gavin
 */
@Getter
@Setter
public class Sm4Context {
    private int mode;

    private long[] sk;

    private boolean isPadding;

    public Sm4Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }

}
