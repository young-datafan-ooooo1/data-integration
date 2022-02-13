package com.github.vfss3.operations;

import com.github.vfss3.S3FileObject;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;
import org.apache.commons.vfs2.operations.FileOperationProvider;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

public class S3FileOperationsProvider implements FileOperationProvider {
    @Override
    public void collectOperations(Collection<Class<? extends FileOperation>> operationsList, FileObject file) {
        if (file instanceof S3FileObject) {
            operationsList.add(IAclGetter.class);
            operationsList.add(IAclSetter.class);
            operationsList.add(IPublicUrlsGetter.class);
            operationsList.add(IMD5HashGetter.class);
            operationsList.add(ServerSideEncryption.class);
            operationsList.add(PlatformFeatures.class);
        }
    }

    @Override
    public FileOperation getOperation(FileObject file, Class<? extends FileOperation> operationClass) throws FileSystemException {
        requireNonNull(file);
        requireNonNull(operationClass);

        if (file instanceof S3FileObject) {
            final S3FileObject s3file = (S3FileObject) file;

            if (operationClass.equals(IAclGetter.class)) {
                // getter
                return new AclGetter(s3file);
            } else if (operationClass.equals(IAclSetter.class)) {
                // setter
                return new AclSetter(s3file);
            } else if (operationClass.equals(IPublicUrlsGetter.class)) {
                // public urls
                return new PublicUrlsGetter(s3file);
            } else if (operationClass.equals(IMD5HashGetter.class)) {
                // get md5 hash
                return new MD5HashGetter(s3file);
            } else if (operationClass.equals(ServerSideEncryption.class)) {
                if (s3file.getName().getPlatformFeatures().supportsServerSideEncryption()) {
                    return (new ServerSideEncryptionImpl(s3file));
                } else {
                    return (new MockServerSideEncryption());
                }
            } else if (operationClass.equals(PlatformFeatures.class)) {
                return s3file.getName().getPlatformFeatures();
            }
        }

        throw new FileSystemException(
                "Operation " + operationClass.getName() + " is not provided for file " + file.getName()
        );
    }
}
