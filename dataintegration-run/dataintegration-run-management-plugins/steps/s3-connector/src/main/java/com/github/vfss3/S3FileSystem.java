package com.github.vfss3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import java.util.Collection;

import static com.amazonaws.services.s3.internal.Constants.BUCKET_ACCESS_FORBIDDEN_STATUS_CODE;
import static com.amazonaws.services.s3.internal.Constants.BUCKET_REDIRECT_STATUS_CODE;
import static com.amazonaws.services.s3.internal.Constants.NO_SUCH_BUCKET_STATUS_CODE;

/**
 * An S3 file system.
 *
 * @author Marat Komarov
 * @author Matthias L. Jugel
 * @author Moritz Siuts
 */
public class S3FileSystem extends AbstractFileSystem {
    private final Log log = LogFactory.getLog(getClass());

    private AmazonS3 service;
    private TransferManager transferManager;
    private final Bucket bucket;

    S3FileSystem(
            S3FileName rootName, S3FileSystemOptions options, TransferManager transferManager
    ) throws FileSystemException {
        super(rootName, null, options.toFileSystemOptions());

        this.transferManager = transferManager;
        this.service = transferManager.getAmazonS3Client();

        if (log.isInfoEnabled()) {
            log.info("Init new S3 FileSystem [root=" + rootName + ",opts=" + options + "]");
        }

        try {
            if (options.isCreateBucket() && !doesBucketExist(rootName.getBucket())) {
                bucket = service.createBucket(rootName.getBucket());

                if (log.isInfoEnabled()) {
                    log.info("Created new bucket [" + bucket + "]");
                }
            } else {
                bucket = new Bucket(rootName.getBucket());
            }
        } catch (AmazonServiceException e) {
            String s3message = e.getMessage();

            if (s3message != null) {
                throw new FileSystemException(s3message, e);
            } else {
                throw new FileSystemException(e);
            }
        }
    }

    @Override
    protected void addCapabilities(Collection<Capability> caps) {
        caps.addAll(S3FileProvider.capabilities);
    }

    AmazonS3 getService() {
        return service;
    }

    TransferManager getTransferManager() {
        return transferManager;
    }

    @Override
    protected FileObject createFile(FileName fileName) throws Exception {
        S3FileObject s3FileObject = new S3FileObject((S3FileName) fileName, this);

        return s3FileObject;
    }

    @Override
    protected void doCloseCommunicationLink() {
        if (transferManager != null) {
            transferManager.shutdownNow(true);

            service = null;
            transferManager = null;
        }
    }

    /**
     * Implementation from AWS SDK but with exception on AccessForbidden status.
     *
     * @param bucketName
     * @return
     * @throws FileSystemException
     */
    private boolean doesBucketExist(String bucketName) throws FileSystemException {
        try {
            return service.doesBucketExistV2(bucketName);
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == BUCKET_ACCESS_FORBIDDEN_STATUS_CODE) {
                throw new FileSystemException("vfs.provider.s3/connection-forbidden.error", bucketName, e);
            }

            // A redirect error or a forbidden error means the bucket exists. So
            // returning true.
            if ((e.getStatusCode() == BUCKET_REDIRECT_STATUS_CODE)) {
                return true;
            }

            if (e.getStatusCode() == NO_SUCH_BUCKET_STATUS_CODE) {
                return false;
            }

            // Unknown exception
            throw new FileSystemException(e);
        }
    }
}
