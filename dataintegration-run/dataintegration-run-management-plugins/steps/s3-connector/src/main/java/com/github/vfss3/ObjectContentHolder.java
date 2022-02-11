package com.github.vfss3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.util.MonitorInputStream;
import org.apache.commons.vfs2.util.MonitorOutputStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.channels.Channels.newChannel;
import static java.nio.channels.FileChannel.open;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Objects.requireNonNull;

/**
 * Special container to keep input and output streams inside temporary file.
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
class ObjectContentHolder implements Closeable {
    private final Log log = LogFactory.getLog(ObjectContentHolder.class);

    private Path file;
    private boolean openForRead = false;
    private boolean openForWrite = false;
    private String md5;
    private long contentLength;

    public ObjectContentHolder() throws IOException {
        this.file = Files.createTempFile("vfs.", ".s3");

        file.toFile().deleteOnExit();

        if (log.isDebugEnabled()) {
            log.debug("Initialized cache file " + file);
        }
    }

    public void populateData(InputStream data, ObjectMetadataHolder metadata) throws IOException {
        populateData(
                data,
                metadata.getContentLength(),
                metadata.getMD5Hash().orElseThrow(() -> new FileSystemException("Empty MD5 for the object"))
        );
    }

    /**
     * Save data from the stream into temporary file.
     *
     * @param data
     * @param md5
     */
    public void populateData(InputStream data, long contentLength, String md5) throws IOException {
        if (file == null) {
            throw new FileSystemException("Content holder was closed");
        }

        this.openForWrite = true;
        this.md5 = requireNonNull(md5);
        this.contentLength = contentLength;

        try (FileChannel target = open(file, WRITE)) {
            if (contentLength == 0) {
                target.truncate(0);
            } else {
                try (ReadableByteChannel source = newChannel(data)) {
                    target.transferFrom(source, 0, contentLength);
                }
            }
        } finally {
            openForWrite = false;
        }
    }

    /**
     * Check content length and md5 for current data
     *
     * @return
     */
    public boolean sameData(ObjectMetadataHolder metadata) throws IOException{
        if (file == null) {
            throw new FileSystemException("Content holder was closed");
        }

        return (metadata.getContentLength() == contentLength) &&
                (md5 != null) &&
                (md5.equalsIgnoreCase(metadata.getMD5Hash().orElse(null)));
    }

    public InputStream getInputStream() throws FileSystemException {
        if (file == null) {
            throw new FileSystemException("Content holder was closed");
        }

        if (openForRead || openForWrite) {
            throw new FileSystemException("Close stream before using it for read");
        }

        try {
            return new UnlockOnCloseInputStream();
        } catch (IOException e) {
            log.error("Not able to get input stream for temporary file", e);

            throw new FileSystemException("Not able to get input stream for temporary file");
        }
    }

    public OutputStream getOutputStream(S3FileObject object) throws FileSystemException {
        if (file == null) {
            throw new FileSystemException("Content holder was closed");
        }

        if (openForRead || openForWrite) {
            throw new FileSystemException("Close stream before using it for write");
        }

        try {
            return (new UploadOnCloseOutputStream(requireNonNull(object)));
        } catch (IOException e) {
            log.error("Not able to get output stream for temporary file", e);

            throw new FileSystemException("Not able to get output stream for temporary file");
        }
    }

    @Override
    public void close() {
        if (file != null) {
            if (log.isDebugEnabled()) {
                log.debug("Close cache file " + file);
            }

            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                log.warn("Error deleting temp file: " + file, e);
            }

            file = null;
        }
    }

    /**
     * Get absolute path for cache file
     * @return
     * @throws FileSystemException
     */
    public String getFile() throws FileSystemException {
        if (file == null) {
            throw new FileSystemException("Content holder was closed");
        }

        return file.toFile().getAbsolutePath();
    }

    private class UploadOnCloseOutputStream extends MonitorOutputStream {
        private final S3FileObject object;

        UploadOnCloseOutputStream(S3FileObject object) throws IOException {
            super(Files.newOutputStream(file));

            this.object = object;

            openForWrite = true;
        }

        @Override
        protected void onClose() throws IOException {
            super.onClose();

            try {
                if (log.isDebugEnabled()) {
                    log.debug("Start to upload file " + file);
                }

                md5 = object.upload(file.toFile());
            } catch (Exception e) {
                throw new IOException(e);
            } finally {
                openForWrite = false;
            }
        }
    }

    private class UnlockOnCloseInputStream extends MonitorInputStream {
        public UnlockOnCloseInputStream() throws IOException {
            super(Files.newInputStream(file));

            openForRead = true;
        }

        @Override
        protected void onClose() throws IOException {
            super.onClose();

            openForRead = false;
        }
    }
}
