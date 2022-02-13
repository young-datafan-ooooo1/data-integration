package com.github.vfss3.operations;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;

/**
 * Get md5 hash for file.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public interface IMD5HashGetter extends FileOperation {
    /**
     * Get MD5 hash for object.
     *
     * @return
     */
    String getMD5Hash() throws FileSystemException;
}
