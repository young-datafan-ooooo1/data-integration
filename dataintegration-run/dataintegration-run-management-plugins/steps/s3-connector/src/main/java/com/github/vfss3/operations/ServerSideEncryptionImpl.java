package com.github.vfss3.operations;

import com.github.vfss3.S3FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
class ServerSideEncryptionImpl implements ServerSideEncryption {
    private final S3FileObject file;

    ServerSideEncryptionImpl(S3FileObject file) {
        this.file = file;
    }

    @Override
    public boolean noEncryption() throws FileSystemException {
        return !file.getSSEAlgorithm().isPresent();
    }

    @Override
    public boolean encryptedWith(String algorithm) throws FileSystemException {
        return file.getSSEAlgorithm().filter(a -> a.equals(algorithm)).isPresent();
    }

    @Override
    public void process() throws FileSystemException {
    }
}
