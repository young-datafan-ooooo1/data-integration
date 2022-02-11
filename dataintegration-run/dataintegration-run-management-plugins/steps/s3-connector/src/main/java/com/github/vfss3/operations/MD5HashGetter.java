package com.github.vfss3.operations;

import com.github.vfss3.S3FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public class MD5HashGetter implements IMD5HashGetter {
    private final S3FileObject file;

    /**
     * @param file
     */
    public MD5HashGetter(S3FileObject file) {
        this.file = file;
    }

    /* (non-Javadoc)
     * @see com.intridea.io.vfs.operations.IMD5HashGetter#getMD5Hash()
     */
    @Override
    public String getMD5Hash() throws FileSystemException {
        return file.getMD5Hash().orElse(null);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.vfs.operations.FileOperation#process()
     */
    @Override
    public void process() throws FileSystemException {

        // Do nothing
    }
}
