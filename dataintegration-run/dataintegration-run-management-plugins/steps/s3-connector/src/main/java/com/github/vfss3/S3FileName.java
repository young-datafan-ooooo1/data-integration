/*
 * Copyright 2007 Matthias L. Jugel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.vfss3;

import com.github.vfss3.operations.PlatformFeatures;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static org.apache.commons.vfs2.FileType.FILE;
import static org.apache.commons.vfs2.FileType.FOLDER;

public class S3FileName extends AbstractFileName {
    public static final String SCHEME = "s3";

    /**
     * Host and port for S3 url - endpoint for the client
     */
    private final String endpoint;

    /**
     * Bucket name as part of host name
     */
    private final String urlPrefix;

    /**
     * First segment of path as a bucket - can be null in case of
     */
    private final String pathPrefix;

    /**
     * Bucket name as separated field.
     */
    private final String bucket;

    /**
     * Region name for signing requests
     */
    private final String signingRegion;

    /**
     * Flag does cloud support SSE or not
     */
    private final PlatformFeatures platformFeatures;

    /**
     * Force access key and secret key for the url
     */
    private final String accessKey;
    private final String secretKey;

    public S3FileName(
            String endpoint,
            String urlPrefix, String pathPrefix,
            String bucket, String signingRegion,
            String path, FileType type,
            String accessKey, String secretKey,
            PlatformFeatures platformFeatures
    ) {
        super(SCHEME, path, type);

        this.endpoint = requireNonNull(endpoint);

        if ((bucket != null) && (bucket.contains("/") || bucket.contains(" ") || (bucket.trim().length() == 0))) {
            throw new IllegalArgumentException("Bucket name [" + bucket + "] has to be valid bucket name");
        }

        this.bucket = requireNonNull(bucket);
        this.signingRegion = requireNonNull(signingRegion);
        this.pathPrefix = pathPrefix;
        this.urlPrefix = urlPrefix;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.platformFeatures = requireNonNull(platformFeatures);
    }

    @Override
    public S3FileName createName(String absPath, FileType type) {
        return new S3FileName(
                endpoint, urlPrefix, pathPrefix, bucket, signingRegion,
                absPath, type, accessKey, secretKey, platformFeatures
        );
    }

    @Override
    protected void appendRootUri(StringBuilder buffer, boolean addPassword) {
        buffer.append(getScheme());
        buffer.append("://");

        if (urlPrefix != null) {
            buffer.append(urlPrefix).append('.');
        }

        buffer.append(endpoint);

        if (pathPrefix != null) {
            buffer.append('/').append(pathPrefix);
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public boolean hasPathPrefix() {
        return (pathPrefix != null);
    }

    String getPathPrefix() {
        return pathPrefix;
    }

    String getUrlPrefix() {
        return urlPrefix;
    }

    public String getBucket() {
        return bucket;
    }

    public String getSigningRegion() {
        return signingRegion;
    }

    public boolean hasCredentials() {
        return ((accessKey != null) && (secretKey != null));
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public PlatformFeatures getPlatformFeatures() {
        return platformFeatures;
    }

    /**
     * Returns S3 key from name or empty for a bucket.
     *
     * @return
     * @throws FileSystemException
     */
    public Optional<String> getS3Key() throws FileSystemException {
        if ((type != FILE) && (type != FOLDER)) {
            throw new FileSystemException("Not able to get key from imaginary file");
        }

        if (getPathDecoded().equals(ROOT_PATH)) {
            return empty();
        }

        StringBuilder path = new StringBuilder(getPathDecoded());

        if ((path.indexOf(SEPARATOR) == 0) && (path.length() > 1)) {
            path.deleteCharAt(0);
        }

        if (type == FOLDER) {
            path.append(SEPARATOR_CHAR);
        }

        return Optional.of(path.toString());
    }

    public String getS3KeyAs(FileType fileType) throws FileSystemException {
        StringBuilder path = new StringBuilder(getPathDecoded());

        if ((path.indexOf(SEPARATOR) == 0) && (path.length() > 1)) {
            path.deleteCharAt(0);
        }

        if (fileType == FOLDER) {
            path.append(SEPARATOR_CHAR);
        }

        return path.toString();
    }

    @Override
    public String toString() {
        return "S3FileName{" +
                "endpoint='" + endpoint + '\'' +
                ", urlPrefix='" + urlPrefix + '\'' +
                ", pathPrefix='" + pathPrefix + '\'' +
                ", bucket='" + bucket + '\'' +
                ", signingRegion='" + signingRegion + '\'' +
                ", platformFeatures=" + platformFeatures +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + ((secretKey != null) ? "***secret***" : "null") + '\'' +
                '}';
    }
}
