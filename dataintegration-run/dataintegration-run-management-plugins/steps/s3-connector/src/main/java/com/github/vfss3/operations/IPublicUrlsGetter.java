package com.github.vfss3.operations;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;

/**
 * File operation for gettin' direct urls to S3 objects.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public interface IPublicUrlsGetter extends FileOperation {
    /**
     * Get direct http url to file.
     *
     * @return
     */
    String getHttpUrl();

    /**
     *
     * @param expireInSeconds
     * @return
     */
    String getSignedUrl(int expireInSeconds) throws FileSystemException;
}
