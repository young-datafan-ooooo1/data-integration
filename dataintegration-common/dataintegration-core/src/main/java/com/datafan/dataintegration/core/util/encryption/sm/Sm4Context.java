package com.datafan.dataintegration.core.util.encryption.sm;

public class Sm4Context {
    private int mode;

    private long[] sk;

    private boolean isPadding;

    public Sm4Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long[] getSk() {
        return sk;
    }

    public void setSk(long[] sk) {
        this.sk = sk;
    }

    public boolean isPadding() {
        return isPadding;
    }

    public void setPadding(boolean padding) {
        isPadding = padding;
    }
}
