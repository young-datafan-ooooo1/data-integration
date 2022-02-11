package com.github.vfss3.operations;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;

/**
 * Interface for setting file Access Control List.
 *
 * @author Marat Komarov
 */
public interface IAclSetter extends FileOperation {

    /**
     * Sets file Access Control List.
     * @param acl
     */
    void setAcl(Acl acl);

    /**
     * Executes setter operations.
     * Must be called after setAcl.
     */
    @Override
    void process() throws FileSystemException;

}
