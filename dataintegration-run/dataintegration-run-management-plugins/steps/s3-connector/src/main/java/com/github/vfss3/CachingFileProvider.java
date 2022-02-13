/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.vfss3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileProvider;
import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.commons.vfs2.provider.VfsComponent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.apache.commons.vfs2.FileName.ROOT_PATH;

/**
 * A {@link FileProvider} that handles physical files, such as the files in a local fs, or on an FTP server. An
 * originating file system cannot be layered on top of another file system.
 */
abstract class CachingFileProvider extends AbstractFileProvider {
    private final ReadWriteLock globalLock = new ReentrantReadWriteLock();
    private final Lock readLock = globalLock.readLock();
    private final Lock writeLock = globalLock.writeLock();

    private final Map<FileSystemKey, FileSystem> fileSystems = new TreeMap<>();
    private final Log log = LogFactory.getLog(getClass());

    public CachingFileProvider() {
        super();
    }

    /**
     * Creates a {@link FileSystem}. If the returned FileSystem implements {@link VfsComponent}, it will be initialised.
     *
     * @param rootName          The name of the root file of the file system to create.
     * @param fileSystemOptions The FileSystem options.
     * @return The FileSystem.
     * @throws FileSystemException if an error occurs.
     */
    protected abstract FileSystem doCreateFileSystem(
            FileName rootName, FileSystemOptions fileSystemOptions
    ) throws FileSystemException;

    /**
     * Locates a file object, by absolute URI.
     *
     * @param baseFile          The base file object.
     * @param uri               The URI of the file to locate
     * @param fileSystemOptions The FileSystem options.
     * @return The located FileObject
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject findFile(
            FileObject baseFile, String uri, FileSystemOptions fileSystemOptions
    ) throws FileSystemException {
        // Parse the URI
        final FileName name;

        try {
            name = parseUri(baseFile != null ? baseFile.getName() : null, uri);
        } catch (final FileSystemException exc) {
            throw new FileSystemException("vfs.provider/invalid-absolute-uri.error", uri, exc);
        }

        // Locate the file
        return findFile(name, fileSystemOptions);
    }

    /**
     * Locates a file from its parsed URI.
     *
     * @param name              The file name.
     * @param fileSystemOptions FileSystem options.
     * @return A FileObject associated with the file.
     * @throws FileSystemException if an error occurs.
     */
    protected FileObject findFile(FileName name, FileSystemOptions fileSystemOptions) throws FileSystemException {
        // Check in the cache for the file system
        final FileName rootName = getContext().getFileSystemManager().resolveName(name, ROOT_PATH);

        FileSystem fs = findFileSystem(rootName, fileSystemOptions);

        if (fs == null) {
            writeLock.lock();

            try {
                // Double check
                fs = findFileSystem(rootName, fileSystemOptions);

                if (fs == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Create new file system for [key=" + rootName + ",options=" + fileSystemOptions + "]");
                    }

                    // Need to create the file system, and cache it
                    fs = doCreateFileSystem(rootName, fileSystemOptions);

                    addFileSystem(rootName, fs);
                }
            } finally {
                writeLock.unlock();
            }
        }

        return fs.resolveFile(name);
    }

    @Override
    protected FileSystem findFileSystem(Comparable<?> rootName, FileSystemOptions fileSystemProps) {
        final FileSystemKey treeKey = new FileSystemKey(rootName, fileSystemProps);

        readLock.lock();

        try {
            return fileSystems.get(treeKey);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    protected void addFileSystem(Comparable<?> key, FileSystem fs) throws FileSystemException {
        // Add to the container and initialize
        addComponent(fs);

        if (log.isDebugEnabled()) {
            log.debug("Add new file system [key=" + key + ",options=" + fs.getFileSystemOptions() + "]");
        }

        final FileSystemKey treeKey = new FileSystemKey(key, fs.getFileSystemOptions());

        fileSystems.put(treeKey, fs);
    }

    @Override
    public void freeUnusedResources() {
        // Nothing to do with resources
    }

    @Override
    public void closeFileSystem(FileSystem fileSystem) {
        writeLock.lock();

        try {
            List<FileSystemKey> keys = new LinkedList<>();

            for (Map.Entry<FileSystemKey, FileSystem> entry : fileSystems.entrySet()) {
                if (entry.getValue().equals(fileSystem)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Remove file system " + entry.getKey());
                    }

                    keys.add(entry.getKey());
                }
            }

            keys.forEach(fileSystems::remove);
        } finally {
            writeLock.unlock();
        }

        removeComponent(fileSystem);

        if (fileSystem instanceof VfsComponent) {
            VfsComponent component = (VfsComponent) fileSystem;

            component.close();
        }
    }
}

