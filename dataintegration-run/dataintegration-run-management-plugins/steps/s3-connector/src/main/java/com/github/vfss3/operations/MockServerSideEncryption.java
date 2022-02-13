package com.github.vfss3.operations;

/**
 * Mock implementation that valid for no SSE clouds.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
class MockServerSideEncryption implements ServerSideEncryption {
    @Override
    public boolean noEncryption() {
        return true;
    }

    @Override
    public boolean encryptedWith(String algorithm) {
        return true;
    }

    @Override
    public void process() {
    }
}
