package com.github.vfss3.parser;

import com.amazonaws.regions.Regions;
import com.github.vfss3.S3FileName;
import com.github.vfss3.operations.PlatformFeatures;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileNameParser;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.provider.VfsComponentContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.vfs2.FileName.ROOT_PATH;
import static org.apache.commons.vfs2.FileType.FOLDER;
import static org.apache.commons.vfs2.FileType.IMAGINARY;

/**
 * @author Matthias L. Jugel
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public class S3FileNameParser extends AbstractFileNameParser {
    private static final String DEFAULT_SIGNING_REGION = "us-east-1";
    private static final String DEFAULT_ALIYUN_SIGNING_REGION = "cn-hangzhou";

    private final Log log = LogFactory.getLog(S3FileNameParser.class);

    private static final Pattern AWS_HOST_PATTERN = compile("((?<bucket>[a-z0-9\\-]+)\\.)?s3[-.]((?<region>[a-z0-9\\-]+)\\.)?amazonaws\\.com");
    private static final Pattern YANDEX_HOST_PATTERN = compile("((?<bucket>[a-z0-9\\-]+)\\.)?storage\\.yandexcloud\\.net");
    private static final Pattern MAIL_RU_HOST_PATTERN = compile("((?<bucket>[a-z0-9\\-]+)\\.)?[ih]b\\.bizmrg\\.com");
    private static final Pattern ALIYUN_HOST_PATTERN = compile("((?<bucket>[a-z0-9\\-]+)\\.)?oss(-(?<region>[a-z0-9\\-]+))?\\.aliyuncs\\.com");
    private static final Pattern ORACLE_HOST_PATTERN = compile(
            "(?<bucket>[a-z0-9\\-]+)\\.compat\\.objectstorage\\.(?<region>[a-z0-9\\-]+)\\.oraclecloud\\.com"
    );
    private static final Pattern DIGITAL_OCEAN_HOST_PATTERN = compile(
            "(?<bucket>[a-z0-9\\-]+)\\.(?<region>[a-z0-9\\-]+)\\.digitaloceanspaces\\.com"
    );
    private static final Pattern SBER_CLOUD_HOST_PATTERN = compile(
            "(?<bucket>[a-z0-9\\-]+)\\.obs\\.(?<region>[a-z0-9\\-]+)\\.hc\\.sbercloud\\.ru"
    );

    private static final Pattern PATH = compile("^/+(?<bucket>[^/]+)/*(?<key>/.*)?");

    public S3FileNameParser() {
    }

    /**
     * Parses URI and constructs S3 file name.
     */
    @Override
    public FileName parseUri(
            VfsComponentContext context, FileName base, String filename
    ) throws FileSystemException {
        if (log.isDebugEnabled()) {
            log.debug("Parse uri [base=" + base + ",filename=" + filename + "]");
        }

        URI uri;

        try {
            uri = new URI(filename.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            throw new FileSystemException(e);
        }

        if (!uri.getScheme().equalsIgnoreCase("s3")) {
            throw new FileSystemException("vfs.impl/unknown-scheme.error", uri.getScheme(), filename);
        }

        if ((uri.getHost() == null) || (uri.getHost().trim().length() == 0)) {
            throw new FileSystemException("Not able to find host in url [" + filename + "]");
        }

        String accessKey = null, secretKey = null, region = null;

        if ((uri.getUserInfo() != null) && (uri.getUserInfo().trim().length() != 0)) {
            String[] info = uri.getUserInfo().split(":");

            if ((info.length != 2) && (info.length != 3)) {
                throw new FileSystemException("Wrong user info inside url [" + filename + "]");
            } else {
                accessKey = info[0];
                secretKey = info[1];
                region = (info.length == 3) ? info[2] : null;
            }
        }

        if (base != null) {
            // We already have all configuration
            S3FileName file = buildS3FileName(base, filename);

            if (log.isDebugEnabled()) {
                log.debug("From [base=" + base + ",file=" + filename + "] got " + file);
            }

            return file;
        }

        Matcher hostNameMatcher;

        if ((hostNameMatcher = AWS_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            // Standard AWS endpoint
            region = (region == null) ? hostNameMatcher.group("region") : region;

            checkRegion(region);

            String bucket = hostNameMatcher.group("bucket");
            String host = uri.getHost();
            String key = uri.getPath();

            if ((bucket != null) && (bucket.trim().length() > 0)) {
                // Has bucket inside URL
                if ((region != null) && (region.trim().length() > 0)) {
                    host = "s3-" + region + ".amazonaws.com";
                } else {
                    host = "s3.amazonaws.com";
                }
            } else {
                final Matcher pathMatcher = PATH.matcher(uri.getPath());

                if (pathMatcher.matches()) {
                    String pathBucket = pathMatcher.group("bucket");

                    if ((pathBucket != null) && (pathBucket.trim().length() > 0)) {
                        bucket = pathMatcher.group("bucket");
                    }

                    key = pathMatcher.group("key");
                }
            }

            if ((bucket == null) || (bucket.trim().length() == 0)) {
                throw new FileSystemException("Not able to find bucket inside [" + filename + "]");
            }

            if (region == null) {
                region = DEFAULT_SIGNING_REGION;
            }

            S3FileName file = buildS3FileName(
                    host, null, bucket, bucket, region, key, accessKey, secretKey,
                    new PlatformFeaturesImpl(true, true, true, true, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = YANDEX_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            String bucket = hostNameMatcher.group("bucket");
            String key;

            if ((bucket != null) && (bucket.trim().length() > 0)) {
                key = uri.getPath();
            } else {
                final Matcher pathMatcher = PATH.matcher(uri.getPath());

                if (pathMatcher.matches()) {
                    bucket = pathMatcher.group("bucket");
                    key = pathMatcher.group("key");
                } else {
                    throw new FileSystemException("Not able to find bucket inside [" + filename + "]");
                }
            }

            S3FileName file = buildS3FileName(
                    "storage.yandexcloud.net", bucket, null, bucket, "ru-central1", key, accessKey, secretKey,
                    new PlatformFeaturesImpl(false, true, false, true, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = MAIL_RU_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            String bucket = hostNameMatcher.group("bucket");
            String key;

            if ((bucket != null) && (bucket.trim().length() > 0)) {
                key = uri.getPath();
            } else {
                final Matcher pathMatcher = PATH.matcher(uri.getPath());

                if (pathMatcher.matches()) {
                    bucket = pathMatcher.group("bucket");
                    key = pathMatcher.group("key");
                } else {
                    throw new FileSystemException("Not able to find bucket inside [" + filename + "]");
                }
            }

            S3FileName file = buildS3FileName(
                    "hb.bizmrg.com", null, bucket, bucket, "ru-msk", key, accessKey, secretKey,
                    new PlatformFeaturesImpl(true, false, false, true, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = ALIYUN_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            // Aliyun endpoint
            region = (region == null) ? hostNameMatcher.group("region") : region;
            String host = uri.getHost();

            // Aliyun OSS only supports virtual hosted style access (info from 1. August 2019)
            // https://www.alibabacloud.com/help/doc-detail/64919.htm

            String bucket = hostNameMatcher.group("bucket");

            if ((bucket == null) || (bucket.trim().length() == 0)) {
                throw new FileSystemException("Path-style URLs are not supported on Aliyun Object Storage Service  [" + filename + "]");
            } else {
                // strip the bucket name from the host uri as it will be prepended
                // again in the S3RequestEndpointResolver
                host = host.substring(bucket.length() + 1);
            }

            S3FileName file = buildS3FileName(
                    host,
                    null,
                    null,
                    bucket,
                    (region != null) ? region : DEFAULT_ALIYUN_SIGNING_REGION,
                    uri.getPath(),
                    accessKey,
                    secretKey,
                    new PlatformFeaturesImpl(true, false, true, false, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = ORACLE_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            // Oracle cloud endpoint
            region = (region == null) ? hostNameMatcher.group("region") : region;
            String host = uri.getHost();

            String namespace = hostNameMatcher.group("bucket");
            String bucket, key;

            if ((namespace == null) || (namespace.trim().length() == 0)) {
                throw new FileSystemException("Virtual host style URLs are not supported on Oracle Cloud Storage Service  [" + filename + "]");
            } else {
                final Matcher pathMatcher = PATH.matcher(uri.getPath());

                if (pathMatcher.matches()) {
                    bucket = pathMatcher.group("bucket");
                    key = pathMatcher.group("key");
                } else {
                    throw new FileSystemException("Not able to find bucket inside [" + filename + "]");
                }
            }

            S3FileName file = buildS3FileName(
                    host,
                    null,
                    bucket,
                    bucket,
                    region,
                    key,
                    accessKey,
                    secretKey,
                    new PlatformFeaturesImpl(false, false, false, false, false)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = DIGITAL_OCEAN_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            // Digital Ocean cloud endpoint
            region = (region == null) ? hostNameMatcher.group("region") : region;
            String host = uri.getHost();

            String bucket = hostNameMatcher.group("bucket");

            if ((bucket == null) || (bucket.trim().length() == 0)) {
                throw new FileSystemException("Path-style URLs are not supported on Digital Ocean Spaces [" + filename + "]");
            } else {
                // strip the bucket name from the host uri as it will be prepended
                // again in the S3RequestEndpointResolver
                host = host.substring(bucket.length() + 1);
            }

            S3FileName file = buildS3FileName(
                    host,
                    null,
                    null,
                    bucket,
                    region,
                    uri.getPath(),
                    accessKey,
                    secretKey,
                    new PlatformFeaturesImpl(true, true, false, true, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else if ((hostNameMatcher = SBER_CLOUD_HOST_PATTERN.matcher(uri.getHost())).matches()) {
            // SberCloud endpoint
            region = (region == null) ? hostNameMatcher.group("region") : region;
            String host = uri.getHost();

            String bucket = hostNameMatcher.group("bucket");

            if ((bucket == null) || (bucket.trim().length() == 0)) {
                throw new FileSystemException("Path-style URLs are not supported on SberCloud [" + filename + "]");
            } else {
                // strip the bucket name from the host uri as it will be prepended
                // again in the S3RequestEndpointResolver
                host = host.substring(bucket.length() + 1);
            }

            S3FileName file = buildS3FileName(
                    host,
                    null,
                    null,
                    bucket,
                    region,
                    uri.getPath(),
                    accessKey,
                    secretKey,
                    new PlatformFeaturesImpl(true, true, false, true, true)
            );

            if (log.isDebugEnabled()) {
                log.debug("From uri " + filename + " got " + file);
            }

            return file;
        } else {
            // Custom endpoint like localstack
            StringBuilder host = new StringBuilder(uri.getHost());

            if (uri.getPort() != (-1)) {
                host.append(':').append(uri.getPort());
            }

            final Matcher pathMatcher = PATH.matcher(uri.getPath());

            if (pathMatcher.matches()) {
                S3FileName file = buildS3FileName(
                        host.toString(),
                        null,
                        pathMatcher.group("bucket"),
                        pathMatcher.group("bucket"),
                        (region != null) ? region : DEFAULT_SIGNING_REGION,
                        pathMatcher.group("key"),
                        accessKey, secretKey,
                        new PlatformFeaturesImpl(true, true, false, true, true)
                );

                if (log.isDebugEnabled()) {
                    log.debug("From uri " + filename + " got " + file);
                }

                return file;
            } else {
                throw new FileSystemException("Not able to find bucket inside [" + filename + "]");
            }
        }
    }

    /**
     * Check region for correct name.
     *
     */
    private void checkRegion(String regionName) throws FileSystemException {
        if ((regionName != null) && (regionName.trim().length() > 0)) {
            try {
                requireNonNull(Regions.fromName(regionName));
            } catch (IllegalArgumentException e) {
                throw new FileSystemException("Not able to parse region [" + regionName + "]");
            }
        }
    }

    private S3FileName buildS3FileName(FileName base, String key) {
        S3FileName s3Base = (S3FileName) base;

        return s3Base.createName(key.substring(s3Base.getRootURI().length()), IMAGINARY);
    }

    private S3FileName buildS3FileName(
            String endpoint, String urlPrefix, String pathPrefix,
            String bucket, String signingRegion,
            String key,
            String accessKey, String secretKey,
            PlatformFeatures features
    ) throws FileSystemException {
        if ((key == null) || (key.trim().length() == 0)) {
            key = ROOT_PATH;
        }

        if (!key.equals(ROOT_PATH)) {
            StringBuilder sb = new StringBuilder(key);

            UriParser.fixSeparators(sb);
            UriParser.normalisePath(sb);

            key = sb.toString();
        }

        FileType type = (ROOT_PATH.equals(key)) ? FOLDER : IMAGINARY;

        return (new S3FileName(
                endpoint, urlPrefix, pathPrefix, bucket, signingRegion, key, type, accessKey, secretKey, features
        ));
    }
}
