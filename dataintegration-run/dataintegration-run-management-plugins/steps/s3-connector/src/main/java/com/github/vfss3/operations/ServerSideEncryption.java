package com.github.vfss3.operations;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;

/**
 * File operation to work with server-side encryption. Some cloud providers don't support it.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public interface ServerSideEncryption extends FileOperation {
    /**
     * No encryption for file.
     *
     * @return
     */
    boolean noEncryption() throws FileSystemException;

    /**
     * Check does file encrypted with algorithm or not.
     *
     * @param algorithm
     * @return
     */
    boolean encryptedWith(String algorithm) throws FileSystemException;
}
