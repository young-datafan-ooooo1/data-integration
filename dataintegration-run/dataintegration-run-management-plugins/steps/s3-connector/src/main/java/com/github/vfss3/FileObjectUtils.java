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

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DecoratedFileObject;

/**
 * Utility methods for {@link FileObject}.
 */
final class FileObjectUtils {
    /**
     * Null-safe call to {@link FileObject#exists()}.
     *
     * @param fileObject the file object to test, may be null.
     * @return false if {@code fileObject} is null, otherwise, see {@link FileObject#exists()}.
     * @throws FileSystemException On error determining if this file exists.
     * @since 2.4
     */
    public static boolean exists(FileObject fileObject) throws FileSystemException {
        return fileObject != null && fileObject.exists();
    }

    /**
     * Gets access to the base object even if decorated.
     *
     * @param fileObject The FileObject.
     * @return The decorated FileObject or null.
     * @throws FileSystemException if an error occurs.
     */
    @Deprecated
    public static AbstractFileObject getAbstractFileObject(final FileObject fileObject) throws FileSystemException {
        Object searchObject = fileObject;
        while (searchObject instanceof DecoratedFileObject) {
            searchObject = ((DecoratedFileObject) searchObject).getDecoratedFileObject();
        }
        if (searchObject instanceof AbstractFileObject) {
            return (AbstractFileObject) searchObject;
        }
        if (searchObject == null) {
            return null;
        }

        throw new FileSystemException("vfs.util/find-abstract-file-object.error",
                fileObject == null ? "null" : fileObject.getClass().getName());
    }

    /**
     *
     * Gets access to the base object even if decorated.
     *
     * @param fileObject The FileObject.
     * @return The decorated FileObject or null.
     * @throws FileSystemException if an error occurs.
     */
    public static FileObject unwrap(FileObject fileObject) throws FileSystemException {
        if (fileObject == null) {
            return null;
        }

        FileObject searchObject = fileObject;

        while (searchObject instanceof DecoratedFileObject) {
            searchObject = ((DecoratedFileObject) searchObject).getDecoratedFileObject();
        }
        if (searchObject != null) {
            return searchObject;
        }

        throw new FileSystemException("vfs.util/find-abstract-file-object.error", fileObject.getClass().getName());
    }

    private FileObjectUtils() {
    }
}
