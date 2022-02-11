package com.github.vfss3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import org.apache.commons.vfs2.FileSystemOptions;

/**
 * Wrapper around FileSystemOptions for storing and retrieving various options. It can't be immutable because it use
 * system properties as default values.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public class S3FileSystemOptions {
    /**
     * Configuration prefix
     */
    public static final String PREFIX = "s3";

    private final FileSystemOptions options;

    public S3FileSystemOptions() {
        this(null);
    }

    /**
     * Create new object with copy of existed properties.
     *
     * @param options
     */
    public S3FileSystemOptions(FileSystemOptions options) {
        this(options, true);
    }

    /**
     * Create new options object based on existed options. cloneOptions useful for old config builder.
     *
     * @param options
     * @param cloneOptions
     */
    public S3FileSystemOptions(FileSystemOptions options, boolean cloneOptions) {
        if (options != null) {
            this.options = cloneOptions ? (FileSystemOptions) options.clone() : options;
        } else {
            this.options = new FileSystemOptions();
        }
    }

    /**
     * use server-side encryption.
     *
     * @param serverSideEncryption true if server-side encryption should be used.
     */
    public void setServerSideEncryption(boolean serverSideEncryption) {
        S3FileSystemConfigBuilder.getInstance().setServerSideEncryption(options, serverSideEncryption);
    }

    /**
     * @return true if server-side encryption is being used.
     * @see #setServerSideEncryption(boolean)
     */
    public boolean getServerSideEncryption() {
        return S3FileSystemConfigBuilder.getInstance().getServerSideEncryption(options);
    }

    /**
     * @param clientConfiguration The AWS ClientConfiguration object to
     *                            use when creating the connection.
     */
    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        S3FileSystemConfigBuilder.getInstance().setClientConfiguration(options, clientConfiguration);
    }

    /**
     * @return The AWS ClientConfiguration object to use when creating the
     * connection.  If none has been set, a default ClientConfiguration is returend,
     * with the following differences:
     *   1. The maxErrorRetry is 8 instead of the AWS default (3).  This
     *      is generally a better setting to use when operating in a production
     *      environment and means approximately up to 2 minutes of retries for
     *      failed operations.
     */
    public ClientConfiguration getClientConfiguration() {
        return S3FileSystemConfigBuilder.getInstance().getClientConfiguration(options);
    }

    /**
     * Set option to disable chunked encoding
     *
     * @param disableChunkedEncoding
     */
    public void setDisableChunkedEncoding(boolean disableChunkedEncoding) {
        S3FileSystemConfigBuilder.getInstance().setDisableChunkedEncoding(options, disableChunkedEncoding);
    }

    /**
     * Get
     *
     * @return true if per-file locking should be used.
     */
    public boolean isDisableChunkedEncoding() {
        return S3FileSystemConfigBuilder.getInstance().getDisableChunkedEncoding(options);
    }

    /**
     * Use https for endpoint calls. true by default
     *
     * @return true if use https for all communications
     */
    public boolean isUseHttps() {
        return S3FileSystemConfigBuilder.getInstance().isUseHttps(options);
    }

    /**
     * Use https for endpoint calls. true by default
     */
    public void setUseHttps(boolean useHttps) {
        S3FileSystemConfigBuilder.getInstance().setUseHttps(options, useHttps);
    }

    /**
     * Create bucket in case of missed one.
     */
    public boolean isCreateBucket() {
        return S3FileSystemConfigBuilder.getInstance().isCreateBucket(options);
    }

    /**
     * Create bucket in case of missed one.
     */
    public void setCreateBucket(boolean createBucket) {
        S3FileSystemConfigBuilder.getInstance().setCreateBucket(options, createBucket);
    }

    /**
     * Get credentials provider for a file system - DefaultAWSCredentialsProviderChain by default
     *
     * @return
     */
    public AWSCredentialsProvider getCredentialsProvider() {
        return S3FileSystemConfigBuilder.getInstance().getCredentialsProvider(options);
    }

    /**
     * Set credentials provider for a file system
     *
     * @param provider
     */
    public void setCredentialsProvider(AWSCredentialsProvider provider) {
        S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(options, provider);
    }

    /**
     * Returns clone of options object for some legacy things.
     *
     * @return
     */
    public FileSystemOptions toFileSystemOptions() {
        return (FileSystemOptions) options.clone();
    }

    @Override
    public String toString() {
        return options.toString();
    }
}
