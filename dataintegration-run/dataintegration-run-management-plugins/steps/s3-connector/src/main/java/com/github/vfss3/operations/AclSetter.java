package com.github.vfss3.operations;

import com.github.vfss3.S3FileObject;
import org.apache.commons.vfs2.FileSystemException;

class AclSetter implements IAclSetter {

    private S3FileObject file;

    private Acl acl;

    public AclSetter(S3FileObject file) {
        this.file = file;
    }

    @Override
    public void setAcl(Acl acl) {
        this.acl = acl;
    }

    @Override
    public void process() throws FileSystemException {
        file.setAcl(acl);
    }
}
