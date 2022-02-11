package com.github.vfss3.operations;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;

/**
 * Interface for getting file Access Control List.
 *
 * @author Marat Komarov
 */
public interface IAclGetter extends FileOperation {

    /**
     * Returns true when file is readable
     * @param group
     * @return
     */
    boolean canRead(Acl.Group group);

    /**
     * Returns true when file is writeable
     * @param group
     * @return
     */
    boolean canWrite(Acl.Group group);

    /**
     * Returns file ACL
     * @return
     */
    Acl getAcl();

    /**
     * Executes getter operation.
     * Must be called before aby other operation methods
     */
    @Override
    void process() throws FileSystemException;
}
