package com.github.vfss3;

import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazonaws.services.s3.Headers.ETAG;
import static com.amazonaws.services.s3.model.ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION;
import static com.amazonaws.util.DateUtils.cloneDate;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Build class for S3 ObjectMetadata
 *
 * @author <A href="mailto:alexey at abashev dot ru">Alexey Abashev</A>
 */
class ObjectMetadataHolder {
    private final ObjectMetadata metadata;
    private final boolean virtual;

    ObjectMetadataHolder() {
        this.metadata = new ObjectMetadata();
        this.virtual = true;
    }

    ObjectMetadataHolder(ObjectMetadata metadata) {
        this(metadata, false);
    }

    private ObjectMetadataHolder(ObjectMetadata metadata, boolean virtual) {
        this.metadata = requireNonNull(metadata);
        this.virtual = virtual;
    }

    ObjectMetadataHolder(S3ObjectSummary summary) {
        this();

        metadata.setContentLength(requireNonNull(summary).getSize());
        metadata.setLastModified(summary.getLastModified());
        metadata.setHeader(ETAG, summary.getETag());
    }

    ObjectMetadataHolder withZeroContentLength() {
        return this.withContentLength(0);
    }

    ObjectMetadataHolder withContentLength(long length) {
        ObjectMetadata newMeta = metadata.clone();

        newMeta.setContentLength(length);

        return new ObjectMetadataHolder(newMeta, virtual);
    }

    ObjectMetadataHolder withContentType(String type) {
        ObjectMetadata newMeta = metadata.clone();

        newMeta.setContentType(Mimetypes.getInstance().getMimetype(type));

        return new ObjectMetadataHolder(newMeta, virtual);
    }

    ObjectMetadataHolder withLastModifiedNow() {
        ObjectMetadata newMeta = metadata.clone();

        newMeta.setLastModified(new Date());

        return new ObjectMetadataHolder(newMeta, virtual);
    }

    ObjectMetadataHolder withServerSideEncryption(boolean useEncryption) {
        ObjectMetadata newMeta = metadata.clone();

        if (useEncryption) {
            newMeta.setSSEAlgorithm(AES_256_SERVER_SIDE_ENCRYPTION);
        }

        return new ObjectMetadataHolder(newMeta, virtual);
    }

    String getServerSideEncryption() {
        return metadata.getSSEAlgorithm();
    }

    public boolean isVirtual() {
        return virtual;
    }

    public Optional<String> getMD5Hash() {
        return ofNullable(metadata.getETag());
    }

    public long getContentLength() {
        return metadata.getContentLength();
    }

    public long getLastModified() {
        Date lastModified = metadata.getLastModified();

        return (lastModified != null) ? lastModified.getTime() : 0L;
    }

    /**
     * Send metadata with PUT request.
     *
     * @param request
     */
    public void sendWith(PutObjectRequest request) {
        request.setMetadata(deepMetadataClone());
    }

    public void sendWith(CopyObjectRequest request) {
        request.setNewObjectMetadata(deepMetadataClone());
    }

    /**
     * Make deep clone for ObjectMetadata and remove tracing headers (break mail.ru cloud integration)
     *
     * @return
     */
    private ObjectMetadata deepMetadataClone() {
        ObjectMetadata result = new ObjectMetadata();

        final Map<String, Object> headers = new HashMap<>(metadata.getRawMetadata());

        headers.remove("X-Host");
        headers.remove("X-Req-Id");

        headers.forEach(result::setHeader);

        result.setUserMetadata(metadata.getUserMetadata());

        result.setExpirationTime(cloneDate(metadata.getExpirationTime()));
        result.setHttpExpiresDate(cloneDate(metadata.getHttpExpiresDate()));
        result.setRestoreExpirationTime(cloneDate(metadata.getRestoreExpirationTime()));

        if (metadata.getOngoingRestore() != null) {
            result.setOngoingRestore(metadata.getOngoingRestore());
        }

        result.setExpirationTimeRuleId(metadata.getExpirationTimeRuleId());

        return result;
    }
}
