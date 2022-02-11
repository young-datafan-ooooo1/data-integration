package com.github.vfss3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemOptions;

import static com.github.vfss3.S3FileSystemOptions.PREFIX;
import static java.util.Objects.requireNonNull;

/**
 * The config builder for various AWS S3 configuration options.
 */
public class S3FileSystemConfigBuilder extends FileSystemConfigBuilder {
    private static final String SERVER_SIDE_ENCRYPTION   = "serverSideEncryption";
    private static final String CLIENT_CONFIGURATION     = "clientConfiguration";
    private static final String DISABLE_CHUNKED_ENCODING = "disableChunkedEncoding"; // Useful for localstack
    private static final String USE_HTTPS                = "useHttps";               // Useful for localstack
    private static final String CREATE_BUCKET            = "createBucket";
    private static final String CREDENTIALS_PROVIDER     = "credentialsProvider";

    private static final int DEFAULT_MAX_ERROR_RETRY = 8;

    private static final S3FileSystemConfigBuilder BUILDER = new S3FileSystemConfigBuilder();

    /**
     * Gets the singleton builder.
     *
     * @return the singleton builder.
     */
    public static S3FileSystemConfigBuilder getInstance(){
        return BUILDER;
    }

    private S3FileSystemConfigBuilder() {
        super(PREFIX + ".");
    }

    @Override
    protected Class<? extends FileSystem> getConfigClass() {
        return S3FileSystem.class;
    }

    void setOption(FileSystemOptions opts, String name, Object value) {
        setParam(opts, name, value);
    }

    Object getOption(FileSystemOptions opts, String name) {
        return getParam(opts, name);
    }

    boolean getBooleanOption(FileSystemOptions opts, String name, boolean defaultValue) {
        return getBoolean(opts, name, defaultValue);
    }

    String getStringOption(FileSystemOptions opts, String name, String defaultValue) {
        return getString(opts, name, defaultValue);
    }

    int getIntegerOption(FileSystemOptions opts, String name, int defaultValue) {
        return getInteger(opts, name, defaultValue);
    }

    public boolean getServerSideEncryption(FileSystemOptions opts) {
        return getBooleanOption(opts, SERVER_SIDE_ENCRYPTION, false);
    }

    public void setServerSideEncryption(FileSystemOptions opts, final boolean serverSideEncryption) {
        setOption(opts, SERVER_SIDE_ENCRYPTION, serverSideEncryption);
    }

    /**
     * @param clientConfiguration The AWS ClientConfiguration object to
     *                            use when creating the connection.
     */
    public void setClientConfiguration(FileSystemOptions opts, ClientConfiguration clientConfiguration) {
        setOption(opts, CLIENT_CONFIGURATION, requireNonNull(clientConfiguration));
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
    public ClientConfiguration getClientConfiguration(FileSystemOptions opts) {
        ClientConfiguration clientConfiguration = (ClientConfiguration) getOption(opts, CLIENT_CONFIGURATION);

        if (clientConfiguration == null) {
            clientConfiguration = new ClientConfiguration();

            clientConfiguration.setMaxErrorRetry(DEFAULT_MAX_ERROR_RETRY);
        }

        return clientConfiguration;
    }

    /**
     * Don't use chunked encoding for AWS calls - useful for localstack because it doesn't support it.
     *
     * @return true if use https for all communications
     */
    public boolean getDisableChunkedEncoding(FileSystemOptions opts) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        return builder.getBooleanOption(opts, DISABLE_CHUNKED_ENCODING, false);
    }

    /**
     * Don't use chunked encoding for AWS calls - useful for localstack because it doesn't support it.
     *
     * @param disableChunkedEncoding
     */
    public void setDisableChunkedEncoding(FileSystemOptions opts, boolean disableChunkedEncoding) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        builder.setOption(opts, DISABLE_CHUNKED_ENCODING, disableChunkedEncoding);
    }

    /**
     * Use https for endpoint calls. true by default
     *
     * @return true if use https for all communications
     */
    public boolean isUseHttps(FileSystemOptions opts) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        return builder.getBooleanOption(opts, USE_HTTPS, true);
    }

    /**
     * Use https for endpoint calls. true by default
     *
     * @param opts
     * @param useHttps
     */
    public void setUseHttps(FileSystemOptions opts, boolean useHttps) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        builder.setOption(opts, USE_HTTPS, useHttps);
    }

    /**
     * Should we do 'create bucket' call in case of missed bucket.
     *
     * @return
     */
    public boolean isCreateBucket(FileSystemOptions opts) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        return builder.getBooleanOption(opts, CREATE_BUCKET, false);
    }

    /**
     * Should we do 'create bucket' call in case of missed bucket.
     *
     * @param opts
     * @param createBucket
     */
    public void setCreateBucket(FileSystemOptions opts, boolean createBucket) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        builder.setOption(opts, CREATE_BUCKET, createBucket);
    }

    /**
     * Get credentials provider for a file system - DefaultAWSCredentialsProviderChain by default
     *
     * @return
     */
    public AWSCredentialsProvider getCredentialsProvider(FileSystemOptions opts) {
        S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();
        AWSCredentialsProvider provider = (AWSCredentialsProvider) builder.getOption(opts, CREDENTIALS_PROVIDER);

        return (provider != null) ? provider : (new DefaultAWSCredentialsProviderChain());
    }

    /**
     * Set credentials provider for a file system
     *
     * @param opts
     * @param provider
     */
    public void setCredentialsProvider(FileSystemOptions opts, AWSCredentialsProvider provider) {
        final S3FileSystemConfigBuilder builder = new S3FileSystemConfigBuilder();

        builder.setOption(opts, CREDENTIALS_PROVIDER, provider);
    }
}
