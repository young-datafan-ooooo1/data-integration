package com.github.vfss3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.github.vfss3.parser.S3FileNameParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.amazonaws.services.s3.transfer.TransferManagerBuilder.standard;

/**
 * An S3 file provider. Create an S3 file system out of an S3 file name. Also
 * defines the capabilities of the file system.
 *
 * @author Marat Komarov
 * @author Matthias L. Jugel
 * @author Moritz Siuts
 */
public class S3FileProvider extends CachingFileProvider {
    private final Log log = LogFactory.getLog(getClass());

    final static Collection<Capability> capabilities = Collections.unmodifiableCollection(Arrays.asList(
        Capability.CREATE,
        Capability.DELETE,
        Capability.GET_TYPE,
        Capability.GET_LAST_MODIFIED,
        Capability.LIST_CHILDREN,
        Capability.READ_CONTENT,
        Capability.URI,
        Capability.WRITE_CONTENT
    ));

    public S3FileProvider() {
        setFileNameParser(new S3FileNameParser());
    }

    /**
     * Create a file system with the S3 root provided.
     *
     * @param fileName the S3 file name that defines the root (bucket)
     * @param fileSystemOptions file system options
     * @return an S3 file system
     * @throws FileSystemException if the file system cannot be created
     */
    @Override
    protected FileSystem doCreateFileSystem(
            FileName fileName, FileSystemOptions fileSystemOptions
    ) throws FileSystemException {
        final S3FileName root = (S3FileName) fileName;
        final S3FileSystemOptions options = new S3FileSystemOptions(fileSystemOptions);

        final AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder.standard().
                withClientConfiguration(options.getClientConfiguration());

        if (root.hasCredentials()) {
            clientBuilder.withCredentials(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(root.getAccessKey(), root.getSecretKey()))
            );
        } else {
            clientBuilder.withCredentials(options.getCredentialsProvider());
        }

        if (options.isDisableChunkedEncoding()) {
            clientBuilder.disableChunkedEncoding();
        }

        if (root.hasPathPrefix()) {
            clientBuilder.enablePathStyleAccess();
        }

        if (options.getServerSideEncryption() && !root.getPlatformFeatures().supportsServerSideEncryption()) {
            log.warn("Try to use Server-Side Encryption with cloud that doesn't support it");
        }

        StringBuilder endpoint = new StringBuilder();

        if (options.isUseHttps()) {
            endpoint.append("https://");
        } else {
            endpoint.append("http://");
        }

        endpoint.append(root.getEndpoint());

        if (log.isDebugEnabled()) {
            log.debug("Endpoint configuration [endpoint=" + endpoint + ",region=" + root.getSigningRegion() + "]");
        }

        clientBuilder.withEndpointConfiguration(new EndpointConfiguration(endpoint.toString(), root.getSigningRegion()));

        TransferManager transferManager = standard().
                withS3Client(clientBuilder.build()).
                build();

        return (new S3FileSystem(root, options, transferManager));
    }

    /**
     * Get the capabilities of the file system provider.
     *
     * @return the file system capabilities
     */
    @Override
    public Collection<Capability> getCapabilities() {
        return capabilities;
    }

    /**
     * Return config builder.
     *
     * @return A config builder for S3FileSystem.
     * @see org.apache.commons.vfs2.provider.AbstractFileProvider#getConfigBuilder()
     */
    @Override
    public FileSystemConfigBuilder getConfigBuilder() {
        return S3FileSystemConfigBuilder.getInstance();
    }
}
