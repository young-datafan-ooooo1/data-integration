package com.github.vfss3.operations;

import com.github.vfss3.S3FileName;
import com.github.vfss3.S3FileObject;
import com.github.vfss3.S3FileSystemOptions;
import org.apache.commons.vfs2.FileSystemException;

/**
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
class PublicUrlsGetter implements IPublicUrlsGetter {
    private final S3FileObject file;

    public PublicUrlsGetter(S3FileObject file) {
        this.file = file;
    }

    @Override
    public String getHttpUrl() {
        final S3FileSystemOptions options = new S3FileSystemOptions(file.getFileSystem().getFileSystemOptions());

        return file.getName().getURI().replaceFirst("^" + S3FileName.SCHEME + "://", options.isUseHttps() ? "https://" : "http://");
    }

    @Override
    public String getSignedUrl(int expireInSeconds) throws FileSystemException {
        return file.getSignedUrl(expireInSeconds);
    }

    @Override
    public void process() throws FileSystemException {
        // Nothing to do
    }
}
