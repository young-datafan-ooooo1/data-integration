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
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileContentInfoFactory;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileNotFolderException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileUtil;
import org.apache.commons.vfs2.NameScope;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.operations.DefaultFileOperations;
import org.apache.commons.vfs2.operations.FileOperations;
import org.apache.commons.vfs2.provider.DefaultURLStreamHandler;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.util.RandomAccessMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.vfs2.FileType.FILE;
import static org.apache.commons.vfs2.FileType.FOLDER;

/**
 * A partial file object implementation.
 *
 * @param <AFS> An AbstractFileSystem subclass
 */
abstract class AbstractFileObject<AFS extends AbstractFileSystem> implements FileObject {
    private final Log log = LogFactory.getLog(AbstractFileObject.class);

    /*
     * TODO - Chop this class up - move all the protected methods to several interfaces, so that structure and content
     * can be separately overridden.
     *
     * <p>
     * TODO - Check caps in methods like getChildren(), etc, and give better error messages (eg 'this file type does not
     * support listing children', vs 'this is not a folder')
     * </p>
     */

    private static final FileName[] EMPTY_FILE_ARRAY = {};

    private static final int INITIAL_LIST_SIZE = 5;

    /**
     * Traverses a file.
     */
    private static void traverse(final DefaultFileSelectorInfo fileInfo, final FileSelector selector,
            final boolean depthwise, final List<FileObject> selected) throws Exception {
        // Check the file itself
        final FileObject file = fileInfo.getFile();
        final int index = selected.size();

        // If the file is a folder, traverse it
        if (file.getType().hasChildren() && selector.traverseDescendents(fileInfo)) {
            final int curDepth = fileInfo.getDepth();
            fileInfo.setDepth(curDepth + 1);

            // Traverse the children
            final FileObject[] children = file.getChildren();
            for (final FileObject child : children) {
                fileInfo.setFile(child);
                traverse(fileInfo, selector, depthwise, selected);
            }

            fileInfo.setFile(file);
            fileInfo.setDepth(curDepth);
        }

        // Add the file if doing depthwise traversal
        if (selector.includeFile(fileInfo)) {
            if (depthwise) {
                // Add this file after its descendants
                selected.add(file);
            } else {
                // Add this file before its descendants
                selected.add(index, file);
            }
        }
    }

    private AbstractFileName fileName;

    private final AFS fileSystem;
    private FileContent content;
    // Cached info
    private boolean attached;

    private FileType type;
    private FileObject parent;

    // Changed to hold only the name of the children and let the object
    // go into the global files cache
    // private FileObject[] children;
    private FileName[] children;

    private List<Object> objects;

    /**
     * FileServices instance.
     */
    private FileOperations operations;

    /**
     *
     * @param name the file name - muse be an instance of {@link AbstractFileName}
     * @param fileSystem the file system
     * @throws ClassCastException if {@code name} is not an instance of {@link AbstractFileName}
     */
    protected AbstractFileObject(AbstractFileName name, AFS fileSystem) {
        this.fileName = name;
        this.fileSystem = fileSystem;

        fileSystem.fileObjectHanded(this);
    }

    /**
     * Attaches to the file.
     *
     * @throws FileSystemException if an error occurs.
     */
    protected final void attach() throws FileSystemException {
        if (isAttached()) {
            return;
        }

        try {
            // Attach and determine the file type
            doAttach();

            setAttached(true);
            // now the type could already be injected by doAttach (e.g from parent to child)

            // Locate the parent of this file
            if (parent == null) {
                final FileName name = fileName.getParent();

                if (name == null) {
                    setParent(null);
                } else {
                    setParent(fileSystem.resolveFile(name));
                }
            }

            /*
             * VFS-210: determine the type when really asked fore if (type == null) { setFileType(doGetType()); } if
             * (type == null) { setFileType(FileType.IMAGINARY); }
             */
        } catch (Exception exc) {
            throw new FileSystemException("vfs.provider/get-type.error", exc, fileName);
        }

        // fs.fileAttached(this);
    }

    /**
     * Queries the object if a simple rename to the file name of {@code newfile} is possible.
     *
     * @param newfile the new file name
     * @return true if rename is possible
     */
    @Override
    public boolean canRenameTo(final FileObject newfile) {
        return fileSystem == newfile.getFileSystem();
    }

    /**
     * Notifies the file that its children have changed.
     *
     * @param childName The name of the child.
     * @param newType The type of the child.
     * @throws Exception if an error occurs.
     */
    protected void childrenChanged(final FileName childName, final FileType newType) throws Exception {
        // TODO - this may be called when not attached

        if (children != null && childName != null && newType != null) {
            // TODO - figure out if children[] can be replaced by list
            final ArrayList<FileName> list = new ArrayList<>(Arrays.asList(children));
            if (newType.equals(FileType.IMAGINARY)) {
                list.remove(childName);
            } else {
                list.add(childName);
            }
            children = new FileName[list.size()];
            list.toArray(children);
        }

        // removeChildrenCache();
        onChildrenChanged(childName, newType);
    }

    /**
     * Closes this file, and its content.
     *
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void close() throws FileSystemException {
        FileSystemException exc = null;

        // Close the content
        if (content != null) {
            try {
                content.close();
                content = null;
            } catch (final FileSystemException e) {
                exc = e;
            }
        }

        // Detach from the file
        try {
            detach();
        } catch (final Exception e) {
            exc = new FileSystemException("vfs.provider/close.error", fileName, e);
        }

        if (exc != null) {
            throw exc;
        }
    }

    /**
     * Compares two FileObjects (ignores case).
     *
     * @param file the object to compare.
     * @return a negative integer, zero, or a positive integer when this object is less than, equal to, or greater than
     *         the given object.
     */
    @Override
    public int compareTo(final FileObject file) {
        if (file == null) {
            return 1;
        }
        return this.toString().compareToIgnoreCase(file.toString());
    }

    /**
     * Copies another file to this file.
     *
     * @param file The FileObject to copy.
     * @param selector The FileSelector.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void copyFrom(final FileObject file, final FileSelector selector) throws FileSystemException {
        if (!FileObjectUtils.exists(file)) {
            throw new FileSystemException("vfs.provider/copy-missing-file.error", file);
        }

        // Locate the files to copy across
        final ArrayList<FileObject> files = new ArrayList<>();
        file.findFiles(selector, false, files);

        // Copy everything across
        for (final FileObject srcFile : files) {
            // Determine the destination file
            final String relPath = file.getName().getRelativeName(srcFile.getName());
            final FileObject destFile = resolveFile(relPath, NameScope.DESCENDENT_OR_SELF);

            // Clean up the destination file, if necessary
            if (FileObjectUtils.exists(destFile) && destFile.getType() != srcFile.getType()) {
                // The destination file exists, and is not of the same type,
                // so delete it
                // TODO - add a pluggable policy for deleting and overwriting existing files
                destFile.deleteAll();
            }

            // Copy across
            try {
                if (srcFile.getType().hasContent()) {
                    FileUtil.copyContent(srcFile, destFile);
                } else if (srcFile.getType().hasChildren()) {
                    destFile.createFolder();
                }
            } catch (final IOException e) {
                throw new FileSystemException("vfs.provider/copy-file.error", e, srcFile, destFile);
            }
        }
    }

    /**
     * Creates this file, if it does not exist.
     *
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void createFile() throws FileSystemException {
        try {
            // VFS-210: We do not want to trunc any existing file, checking for its existence is
            // still required
            if (exists() && !isFile()) {
                throw new FileSystemException("vfs.provider/create-file.error", fileName);
            }

            if (!exists()) {
                getOutputStream().close();
                endOutput();
            }
        } catch (final RuntimeException re) {
            throw re;
        } catch (final Exception e) {
            throw new FileSystemException("vfs.provider/create-file.error", fileName, e);
        }
    }

    /**
     * Creates this folder, if it does not exist. Also creates any ancestor files which do not exist.
     *
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void createFolder() throws FileSystemException {
        try {
            // VFS-210: we create a folder only if it does not already exist. So this check should be safe.
            if (getType().hasChildren()) {
                // Already exists as correct type
                return;
            }

            /*
             * VFS-210: checking for writeable is not always possible as the security constraint might be more complex
             * if (!isWriteable()) { throw new FileSystemException("vfs.provider/create-folder-read-only.error", name);
             * }
             */

            // Traverse up the hierarchy and make sure everything is a folder
            final FileObject parent = getParent();

            if ((parent != null) && !parent.exists()) {
                parent.createFolder();
            }

            if (exists()) {
                throw new FileSystemException("vfs.provider/create-folder-mismatched-type.error", fileName);
            }

            // Create the folder
            doCreateFolder();

            // Update cached info
            handleCreate(FOLDER);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exc) {
            throw new FileSystemException("vfs.provider/create-folder.error", fileName, exc);
        }
    }

    /**
     * Deletes this file.
     * <p>
     * TODO - This will not fail if this is a non-empty folder.
     * </p>
     *
     * @return true if this object has been deleted
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean delete() throws FileSystemException {
        return delete(Selectors.SELECT_SELF) > 0;
    }

    /**
     * Deletes this file, and all children matching the {@code selector}.
     *
     * @param selector The FileSelector.
     * @return the number of deleted files.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public int delete(final FileSelector selector) throws FileSystemException {
        int nuofDeleted = 0;

        /*
         * VFS-210 if (getType() == FileType.IMAGINARY) { // File does not exist return nuofDeleted; }
         */

        // Locate all the files to delete
        final ArrayList<FileObject> files = new ArrayList<>();
        findFiles(selector, true, files);

        if (log.isDebugEnabled()) {
            log.debug("Found files to delete " + files.toString());
        }

        for (FileObject fileObject : files) {
            final AbstractFileObject file = FileObjectUtils.getAbstractFileObject(fileObject);

            if (file != null) {
                // file.attach();

                // VFS-210: It seems impossible to me that findFiles will return a list with hidden files/directories
                // in it, else it would not be hidden. Checking for the file-type seems ok in this case
                // If the file is a folder, make sure all its children have been deleted
                if (checkBeforeDelete(file)) {
                    // Skip - as the selector forced us not to delete all files
                    continue;
                }

                // Delete the file
                if (file.deleteSelf()) {
                    nuofDeleted++;
                }
            }
        }

        return nuofDeleted;
    }

    /**
     * Deletes this file and all children. Shorthand for {@code delete(Selectors.SELECT_ALL)}
     *
     * @return the number of deleted files.
     * @throws FileSystemException if an error occurs.
     * @see #delete(FileSelector)
     * @see Selectors#SELECT_ALL
     */
    @Override
    public int deleteAll() throws FileSystemException {
        return this.delete(Selectors.SELECT_ALL);
    }

    /**
     * Deletes this file, once all its children have been deleted
     *
     * @return true if this file has been deleted
     * @throws FileSystemException if an error occurs.
     */
    private boolean deleteSelf() throws FileSystemException {
        // Its possible to delete a read-only file if you have write-execute access to the directory

        /*
         * VFS-210 if (getType() == FileType.IMAGINARY) { // File does not exist return false; }
         */

        try {
            // Delete the file
            doDelete();

            // Update cached info
            handleDelete();
        } catch (final RuntimeException re) {
            throw re;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/delete.error", exc, fileName);
        }

        return true;
    }

    /**
     * Detaches this file, invalidating all cached info. This will force a call to {@link #doAttach} next time this file
     * is used.
     *
     * @throws Exception if an error occurs.
     */
    private void detach() throws Exception {
        if (isAttached()) {
            try {
                doDetach();
            } finally {
                setFileType(null);
                setAttached(false);
                setParent(null);

                // fs.fileDetached(this);

                removeChildrenCache();
                // children = null;
            }
        }
    }

    /**
     * Attaches this file object to its file resource.
     * <p>
     * This method is called before any of the doBlah() or onBlah() methods. Sub-classes can use this method to perform
     * lazy initialisation.
     * </p>
     * <p>
     * This implementation does nothing.
     * </p>
     *
     * @throws Exception if an error occurs.
     */
    protected void doAttach() throws Exception {
        // noop
    }

    /**
     * Create a FileContent implementation.
     *
     * @return The FileContent.
     * @throws FileSystemException if an error occurs.
     * @since 2.0
     */
    protected FileContent doCreateFileContent() throws FileSystemException {
        return new DefaultFileContent(this, getFileContentInfoFactory());
    }

    /**
     * Creates this file as a folder. Is only called when:
     * <ul>
     * <li>{@link #doGetType} returns {@link FileType#IMAGINARY}.</li>
     * <li>The parent folder exists and is writeable, or this file is the root of the file system.</li>
     * </ul>
     * This implementation throws an exception.
     *
     * @throws Exception if an error occurs.
     */
    protected void doCreateFolder() throws Exception {
        throw new FileSystemException("vfs.provider/create-folder-not-supported.error");
    }

    /**
     * Deletes the file. Is only called when:
     * <ul>
     * <li>{@link #doGetType} does not return {@link FileType#IMAGINARY}.</li>
     * <li>{@link #doIsWriteable} returns true.</li>
     * <li>This file has no children, if a folder.</li>
     * </ul>
     * This implementation throws an exception.
     *
     * @throws Exception if an error occurs.
     */
    protected void doDelete() throws Exception {
        throw new FileSystemException("vfs.provider/delete-not-supported.error");
    }

    /**
     * Detaches this file object from its file resource.
     * <p>
     * Called when this file is closed. Note that the file object may be reused later, so should be able to be
     * reattached.
     * </p>
     * <p>
     * This implementation does nothing.
     * </p>
     *
     * @throws Exception if an error occurs.
     */
    protected void doDetach() throws Exception {
        // noop
    }

    /**
     * Returns the attributes of this file. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns an empty map.
     * </p>
     *
     * @return The attributes of the file.
     * @throws Exception if an error occurs.
     */
    protected Map<String, Object> doGetAttributes() throws Exception {
        return Collections.emptyMap();
    }

    /**
     * Returns the certificates used to sign this file. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns null.
     * </p>
     *
     * @return The certificates used to sign the file.
     * @throws Exception if an error occurs.
     */
    protected Certificate[] doGetCertificates() throws Exception {
        return null;
    }

    /**
     * Returns the size of the file content (in bytes). Is only called if {@link #doGetType} returns
     * {@link FileType#FILE}.
     *
     * @return The size of the file in bytes.
     * @throws Exception if an error occurs.
     */
    protected abstract long doGetContentSize() throws Exception;

    /**
     * Creates an input stream to read the file content from. Is only called if {@link #doGetType} returns
     * {@link FileType#FILE}.
     * <p>
     * It is guaranteed that there are no open output streams for this file when this method is called.
     * </p>
     * <p>
     * The returned stream does not have to be buffered.
     * </p>
     *
     * @return An InputStream to read the file content.
     * @throws Exception if an error occurs.
     */
    protected abstract InputStream doGetInputStream() throws Exception;

    /**
     * Returns the last modified time of this file. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @return The last modification time.
     * @throws Exception if an error occurs.
     */
    protected long doGetLastModifiedTime() throws Exception {
        throw new FileSystemException("vfs.provider/get-last-modified-not-supported.error");
    }

    /**
     * Creates an output stream to write the file content to. Is only called if:
     * <ul>
     * <li>{@link #doIsWriteable} returns true.
     * <li>{@link #doGetType} returns {@link FileType#FILE}, or {@link #doGetType} returns {@link FileType#IMAGINARY},
     * and the file's parent exists and is a folder.
     * </ul>
     * It is guaranteed that there are no open stream (input or output) for this file when this method is called.
     * <p>
     * The returned stream does not have to be buffered.
     * </p>
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @param bAppend true if the file should be appended to, false if it should be overwritten.
     * @return An OutputStream to write to the file.
     * @throws Exception if an error occurs.
     */
    protected OutputStream doGetOutputStream(final boolean bAppend) throws Exception {
        throw new FileSystemException("vfs.provider/write-not-supported.error");
    }

    /**
     * Creates access to the file for random i/o. Is only called if {@link #doGetType} returns {@link FileType#FILE}.
     * <p>
     * It is guaranteed that there are no open output streams for this file when this method is called.
     * </p>
     *
     * @param mode The mode to access the file.
     * @return The RandomAccessContext.
     * @throws Exception if an error occurs.
     */
    protected RandomAccessContent doGetRandomAccessContent(final RandomAccessMode mode) throws Exception {
        throw new FileSystemException("vfs.provider/random-access-not-supported.error");
    }

    /**
     * Determines the type of this file. Must not return null. The return value of this method is cached, so the
     * implementation can be expensive.
     *
     * @return the type of the file.
     * @throws Exception if an error occurs.
     */
    protected abstract FileType doGetType() throws Exception;

    /**
     * Determines if this file is executable. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns false.
     * </p>
     *
     * @return true if the file is executable, false otherwise.
     * @throws Exception if an error occurs.
     */
    protected boolean doIsExecutable() throws Exception {
        return false;
    }

    /**
     * Determines if this file is hidden. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns false.
     * </p>
     *
     * @return true if the file is hidden, false otherwise.
     * @throws Exception if an error occurs.
     */
    protected boolean doIsHidden() throws Exception {
        return false;
    }

    /**
     * Determines if this file can be read. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns true.
     * </p>
     *
     * @return true if the file is readable, false otherwise.
     * @throws Exception if an error occurs.
     */
    protected boolean doIsReadable() throws Exception {
        return true;
    }

    /**
     * Checks if this fileObject is the same file as {@code destFile} just with a different name. E.g. for case
     * insensitive file systems like windows.
     *
     * @param destFile The file to compare to.
     * @return true if the FileObjects are the same.
     * @throws FileSystemException if an error occurs.
     */
    protected boolean doIsSameFile(final FileObject destFile) throws FileSystemException {
        return false;
    }

    /**
     * Determines if this file is a symbolic link. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns false.
     * </p>
     *
     * @return true if the file is readable, false otherwise.
     * @throws Exception if an error occurs.
     * @since 2.4
     */
    protected boolean doIsSymbolicLink() throws Exception {
        return false;
    }

    /**
     * Determines if this file can be written to. Is only called if {@link #doGetType} does not return
     * {@link FileType#IMAGINARY}.
     * <p>
     * This implementation always returns true.
     * </p>
     *
     * @return true if the file is writable.
     * @throws Exception if an error occurs.
     */
    protected boolean doIsWriteable() throws Exception {
        return true;
    }

    /**
     * Check do we have children for specified file or not - useful for delete method to not drop non-empty folders.
     * @param file check for specified file
     * @return has children or not
     * @throws FileSystemException
     */
    protected boolean checkBeforeDelete(FileObject file) throws FileSystemException {
        return file.getType().hasChildren() && (file.getChildren().length != 0);
    }

    /**
     * Lists the children of this file. Is only called if {@link #doGetType} returns {@link FileType#FOLDER}. The return
     * value of this method is cached, so the implementation can be expensive.
     *
     * @return a possible empty String array if the file is a directory or null or an exception if the file is not a
     *         directory or can't be read.
     * @throws Exception if an error occurs.
     */
    protected abstract String[] doListChildren() throws Exception;

    /**
     * Lists the children of this file.
     * <p>
     * Is only called if {@link #doGetType} returns {@link FileType#FOLDER}.
     * </p>
     * <p>
     * The return value of this method is cached, so the implementation can be expensive.
     * Other than {@code doListChildren} you could return FileObject's to e.g. reinitialize the type of the file.
     * </p>
     * <p>
     * (Introduced for Webdav: "permission denied on resource" during getType())
     * </p>
     *
     * @return The children of this FileObject.
     * @throws Exception if an error occurs.
     */
    protected FileObject[] doListChildrenResolved() throws Exception {
        return null;
    }

    /**
     * Removes an attribute of this file.
     * <p>
     * Is only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @param attrName The name of the attribute to remove.
     * @throws Exception if an error occurs.
     * @since 2.0
     */
    protected void doRemoveAttribute(final String attrName) throws Exception {
        throw new FileSystemException("vfs.provider/remove-attribute-not-supported.error");
    }

    /**
     * Renames the file.
     * <p>
     * Is only called when:
     * </p>
     * <ul>
     * <li>{@link #doIsWriteable} returns true.</li>
     * </ul>
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @param newFile A FileObject with the new file name.
     * @throws Exception if an error occurs.
     */
    protected void doRename(final FileObject newFile) throws Exception {
        throw new FileSystemException("vfs.provider/rename-not-supported.error");
    }

    /**
     * Sets an attribute of this file.
     * <p>
     * Is only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @param attrName The attribute name.
     * @param value The value to be associated with the attribute name.
     * @throws Exception if an error occurs.
     */
    protected void doSetAttribute(final String attrName, final Object value) throws Exception {
        throw new FileSystemException("vfs.provider/set-attribute-not-supported.error");
    }

    /**
     * Make the file executable.
     * <p>
     * Only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     * <p>
     * This implementation returns false.
     * </p>
     *
     * @param executable True to allow access, false to disallow.
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return true if the operation succeeded.
     * @throws Exception Any Exception thrown is wrapped in FileSystemException.
     * @see #setExecutable(boolean, boolean)
     * @since 2.1
     */
    protected boolean doSetExecutable(final boolean executable, final boolean ownerOnly) throws Exception {
        return false;
    }

    /**
     * Sets the last modified time of this file.
     * <p>
     * Is only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     * <p>
     * This implementation throws an exception.
     * </p>
     *
     * @param modtime The last modification time.
     * @return true if the time was set.
     * @throws Exception Any Exception thrown is wrapped in FileSystemException.
     */
    protected boolean doSetLastModifiedTime(final long modtime) throws Exception {
        throw new FileSystemException("vfs.provider/set-last-modified-not-supported.error");
    }

    /**
     * Make the file or folder readable.
     * <p>
     * Only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     * <p>
     * This implementation returns false.
     * </p>
     *
     * @param readable True to allow access, false to disallow
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return true if the operation succeeded
     * @throws Exception Any Exception thrown is wrapped in FileSystemException.
     * @see #setReadable(boolean, boolean)
     * @since 2.1
     */
    protected boolean doSetReadable(final boolean readable, final boolean ownerOnly) throws Exception {
        return false;
    }

    /**
     * Make the file or folder writeable.
     * <p>
     * Only called if {@link #doGetType} does not return {@link FileType#IMAGINARY}.
     * </p>
     *
     * @param writable True to allow access, false to disallow
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return true if the operation succeeded
     * @throws Exception Any Exception thrown is wrapped in FileSystemException.
     * @see #setWritable(boolean, boolean)
     * @since 2.1
     */
    protected boolean doSetWritable(final boolean writable, final boolean ownerOnly) throws Exception {
        return false;
    }

    /**
     * Called when the output stream for this file is closed.
     *
     * @throws Exception if an error occurs.
     */
    protected void endOutput() throws Exception {
        if (getType() == FileType.IMAGINARY) {
            // File was created
            handleCreate(FILE);
        } else {
            // File has changed
            onChange();
        }
    }

    /**
     * Determines if the file exists.
     *
     * @return true if the file exists, false otherwise,
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean exists() throws FileSystemException {
        return getType() != FileType.IMAGINARY;
    }

    private FileName[] extractNames(final FileObject[] objects) {
        if (objects == null) {
            return null;
        }

        final FileName[] names = new FileName[objects.length];
        for (int iterObjects = 0; iterObjects < objects.length; iterObjects++) {
            names[iterObjects] = objects[iterObjects].getName();
        }

        return names;
    }

    @Override
    protected void finalize() throws Throwable {
        fileSystem.fileObjectDestroyed(this);

        super.finalize();
    }

    /**
     * Finds the set of matching descendants of this file, in depthwise order.
     *
     * @param selector The FileSelector.
     * @return list of files or null if the base file (this object) do not exist
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject[] findFiles(final FileSelector selector) throws FileSystemException {
        final List<FileObject> list = this.listFiles(selector);
        return list == null ? null : list.toArray(new FileObject[0]);
    }

    /**
     * Traverses the descendants of this file, and builds a list of selected files.
     *
     * @param selector The FileSelector.
     * @param depthwise if true files are added after their descendants, before otherwise.
     * @param selected A List of the located FileObjects.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void findFiles(final FileSelector selector, final boolean depthwise, final List<FileObject> selected)
            throws FileSystemException {
        try {
            if (exists()) {
                // Traverse starting at this file
                final DefaultFileSelectorInfo info = new DefaultFileSelectorInfo();
                info.setBaseFolder(this);
                info.setDepth(0);
                info.setFile(this);
                traverse(info, selector, depthwise, selected);
            }
        } catch (final Exception e) {
            throw new FileSystemException("vfs.provider/find-files.error", fileName, e);
        }
    }

    /**
     * Returns the file system this file belongs to.
     *
     * @return The FileSystem this file is associated with.
     */
    protected AFS getAbstractFileSystem() {
        return fileSystem;
    }

    /**
     * Returns a child of this file.
     *
     * @param name The name of the child to locate.
     * @return The FileObject for the file or null if the child does not exist.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject getChild(final String name) throws FileSystemException {
        // TODO - use a hashtable when there are a large number of children
        final FileObject[] children = getChildren();
        for (final FileObject element : children) {
            final FileName child = element.getName();
            // TODO - use a comparator to compare names
            if (child.getBaseName().equals(name)) {
                return resolveFile(child);
            }
        }
        return null;
    }

    /**
     * Returns the children of the file.
     *
     * @return an array of FileObjects, one per child.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject[] getChildren() throws FileSystemException {
        // VFS-210
        if (!fileSystem.hasCapability(Capability.LIST_CHILDREN)) {
            throw new FileNotFolderException(fileName);
        }

        attach();

        /*
         * VFS-210 if (!getType().hasChildren()) { throw new
         * FileSystemException("vfs.provider/list-children-not-folder.error", name); }
         */

        // Use cached info, if present
        if (children != null) {
            return resolveFiles(children);
        }

        // allow the filesystem to return resolved children. e.g. prefill type for webdav
        FileObject[] childrenObjects;
        try {
            childrenObjects = doListChildrenResolved();
            children = extractNames(childrenObjects);
        } catch (final FileSystemException exc) {
            // VFS-210
            throw exc;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/list-children.error", exc, fileName);
        }

        if (childrenObjects != null) {
            return childrenObjects;
        }

        // List the children
        final String[] files;
        try {
            files = doListChildren();
        } catch (final FileSystemException exc) {
            // VFS-210
            throw exc;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/list-children.error", exc, fileName);
        }

        if (files == null) {
            // VFS-210
            // honor the new doListChildren contract
            // return null;
            throw new FileNotFolderException(fileName);
        } else if (files.length == 0) {
            // No children
            children = EMPTY_FILE_ARRAY;
        } else {
            // Create file objects for the children
            final FileName[] cache = new FileName[files.length];
            for (int i = 0; i < files.length; i++) {
                final String file = files[i];
                cache[i] = fileSystem.getFileSystemManager().resolveName(fileName, file, NameScope.CHILD);
            }
            // VFS-285: only assign the children file names after all of them have been
            // resolved successfully to prevent an inconsistent internal state
            children = cache;
        }

        return resolveFiles(children);
    }

    /**
     * Returns the file's content.
     *
     * @return the FileContent for this FileObject.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileContent getContent() throws FileSystemException {
        attach();

        if (content == null) {
            content = doCreateFileContent();
        }

        return content;
    }

    /**
     * Creates the FileContentInfo factory.
     *
     * @return The FileContentInfoFactory.
     */
    protected FileContentInfoFactory getFileContentInfoFactory() {
        return fileSystem.getFileSystemManager().getFileContentInfoFactory();
    }

    /**
     * @return FileOperations interface that provides access to the operations API.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileOperations getFileOperations() throws FileSystemException {
        if (operations == null) {
            operations = new DefaultFileOperations(this);
        }

        return operations;
    }

    /**
     * Returns the file system this file belongs to.
     *
     * @return The FileSystem this file is associated with.
     */
    @Override
    public FileSystem getFileSystem() {
        return fileSystem;
    }

    /**
     * Returns an input stream to use to read the content of the file.
     *
     * @return The InputStream to access this file's content.
     * @throws FileSystemException if an error occurs.
     */
    public InputStream getInputStream() throws FileSystemException {
        /*
         * VFS-210 if (!getType().hasContent()) { throw new FileSystemException("vfs.provider/read-not-file.error",
         * name); } if (!isReadable()) { throw new FileSystemException("vfs.provider/read-not-readable.error", name); }
         */

        // Get the raw input stream
        try {
            return doGetInputStream();
        } catch (final org.apache.commons.vfs2.FileNotFoundException exc) {
            throw new org.apache.commons.vfs2.FileNotFoundException(fileName, exc);
        } catch (final FileNotFoundException exc) {
            throw new org.apache.commons.vfs2.FileNotFoundException(fileName, exc);
        } catch (final FileSystemException exc) {
            throw exc;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/read.error", fileName, exc);
        }
    }

    /**
     * Returns the name of the file.
     *
     * @return The FileName, never {@code null}.
     */
    @Override
    public FileName getName() {
        return fileName;
    }

    /**
     * Prepares this file for writing. Makes sure it is either a file, or its parent folder exists. Returns an output
     * stream to use to write the content of the file to.
     *
     * @return An OutputStream where the new contents of the file can be written.
     * @throws FileSystemException if an error occurs.
     */
    public OutputStream getOutputStream() throws FileSystemException {
        return getOutputStream(false);
    }

    /**
     * Prepares this file for writing. Makes sure it is either a file, or its parent folder exists. Returns an output
     * stream to use to write the content of the file to.
     *
     * @param bAppend true when append to the file.
     *            Note: If the underlying file system does not support appending, a FileSystemException is thrown.
     * @return An OutputStream where the new contents of the file can be written.
     * @throws FileSystemException if an error occurs; for example:
     *             bAppend is true, and the underlying FileSystem does not support it
     */
    public OutputStream getOutputStream(final boolean bAppend) throws FileSystemException {
        /*
         * VFS-210 if (getType() != FileType.IMAGINARY && !getType().hasContent()) { throw new
         * FileSystemException("vfs.provider/write-not-file.error", name); } if (!isWriteable()) { throw new
         * FileSystemException("vfs.provider/write-read-only.error", name); }
         */

        if (bAppend && !fileSystem.hasCapability(Capability.APPEND_CONTENT)) {
            throw new FileSystemException("vfs.provider/write-append-not-supported.error", fileName);
        }

        if (getType() == FileType.IMAGINARY) {
            // Does not exist - make sure parent does
            final FileObject parent = getParent();

            if ((parent != null) && !parent.exists()) {
                parent.createFolder();
            }
        }

        // Get the raw output stream
        try {
            return doGetOutputStream(bAppend);
        } catch (final RuntimeException re) {
            throw re;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/write.error", exc, fileName);
        }
    }

    /**
     * Returns the parent of the file.
     *
     * @return the parent FileObject.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject getParent() throws FileSystemException {
        if (fileName.compareTo(fileSystem.getRootName()) == 0) // equals is not implemented :-/
        {
            if (fileSystem.getParentLayer() == null) {
                // Root file has no parent
                return null;
            }
            // Return the parent of the parent layer
            return fileSystem.getParentLayer().getParent();
        }

        attach();

        return parent;
    }

    protected void setParent(FileObject parent) {
        this.parent = parent;
    }

    /**
     * Returns the receiver as a URI String for public display, like, without a password.
     *
     * @return A URI String without a password, never {@code null}.
     */
    @Override
    public String getPublicURIString() {
        return fileName.getFriendlyURI();
    }

    /**
     * Returns an input/output stream to use to read and write the content of the file in and random manner.
     *
     * @param mode The RandomAccessMode.
     * @return The RandomAccessContent.
     * @throws FileSystemException if an error occurs.
     */
    public RandomAccessContent getRandomAccessContent(final RandomAccessMode mode) throws FileSystemException {
        /*
         * VFS-210 if (!getType().hasContent()) { throw new FileSystemException("vfs.provider/read-not-file.error",
         * name); }
         */

        if (mode.requestRead()) {
            if (!fileSystem.hasCapability(Capability.RANDOM_ACCESS_READ)) {
                throw new FileSystemException("vfs.provider/random-access-read-not-supported.error");
            }
            if (!isReadable()) {
                throw new FileSystemException("vfs.provider/read-not-readable.error", fileName);
            }
        }

        if (mode.requestWrite()) {
            if (!fileSystem.hasCapability(Capability.RANDOM_ACCESS_WRITE)) {
                throw new FileSystemException("vfs.provider/random-access-write-not-supported.error");
            }
            if (!isWriteable()) {
                throw new FileSystemException("vfs.provider/write-read-only.error", fileName);
            }
        }

        // Get the raw input stream
        try {
            return doGetRandomAccessContent(mode);
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/random-access.error", fileName, exc);
        }
    }

    /**
     * Returns the file's type.
     *
     * @return The FileType.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileType getType() throws FileSystemException {
        try {
            attach();

            // VFS-210: get the type only if requested for
            if (type == null) {
                setFileType(doGetType());
            }
            if (type == null) {
                setFileType(FileType.IMAGINARY);
            }
        } catch (final Exception e) {
            throw new FileSystemException("vfs.provider/get-type.error", e, fileName);
        }

        return type;
    }

    /**
     * Returns a URL representation of the file.
     *
     * @return The URL representation of the file.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public URL getURL() throws FileSystemException {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<URL>() {
                @Override
                public URL run() throws MalformedURLException, FileSystemException {
                    final StringBuilder buf = new StringBuilder();
                    final String scheme = UriParser.extractScheme(VFS.getManager().getSchemes(), fileName.getURI(), buf);
                    return new URL(scheme, "", -1, buf.toString(),
                            new DefaultURLStreamHandler(fileSystem.getContext(), fileSystem.getFileSystemOptions()));
                }
            });
        } catch (final PrivilegedActionException e) {
            throw new FileSystemException("vfs.provider/get-url.error", fileName, e.getException());
        }
    }

    /**
     * Called when this file is changed.
     * <p>
     * This will only happen if you monitor the file using {@link org.apache.commons.vfs2.FileMonitor}.
     * </p>
     *
     * @throws Exception if an error occurs.
     */
    protected void handleChanged() throws Exception {
        // Notify the file system
        fileSystem.fireFileChanged(this);
    }

    /**
     * Called when this file is created. Updates cached info and notifies the parent and file system.
     *
     * @param newType The type of the file.
     * @throws Exception if an error occurs.
     */
    final void handleCreate(final FileType newType) throws Exception {
        if (isAttached()) {
            // Fix up state
            injectType(newType);

            removeChildrenCache();

            // Notify subclass
            onChange();
        }

        // Notify parent that its child list may no longer be valid
        notifyParent(this.getName(), newType);

        // Notify the file system
        fileSystem.fireFileCreated(this);
    }

    /**
     * Called when this file is deleted. Updates cached info and notifies subclasses, parent and file system.
     *
     * @throws Exception if an error occurs.
     */
    final void handleDelete() throws Exception {
        if (isAttached()) {
            // Fix up state
            injectType(FileType.IMAGINARY);
            removeChildrenCache();

            // Notify subclass
            onChange();
        }

        // Notify parent that its child list may no longer be valid
        notifyParent(this.getName(), FileType.IMAGINARY);

        // Notify the file system
        fileSystem.fireFileDeleted(this);
    }

    /**
     * This method is meant to add an object where this object holds a strong reference then. E.g. a archive-file system
     * creates a list of all children and they shouldn't get garbage collected until the container is garbage collected
     *
     * @param strongRef The Object to add.
     */
    // TODO should this be a FileObject?
    public void holdObject(final Object strongRef) {
        if (objects == null) {
            objects = new ArrayList<>(INITIAL_LIST_SIZE);
        }
        objects.add(strongRef);
    }

    protected void injectType(final FileType fileType) {
        setFileType(fileType);
    }

    /**
     * Check if the internal state is "attached".
     *
     * @return true if this is the case
     */
    @Override
    public boolean isAttached() {
        return attached;
    }

    /**
     * Change state of 'attached'.
     * @param attached
     */
    protected void setAttached(boolean attached) {
        this.attached = attached;
    }

    /**
     * Check if the content stream is open.
     *
     * @return true if this is the case
     */
    @Override
    public boolean isContentOpen() {
        if (content == null) {
            return false;
        }

        return content.isOpen();
    }

    /**
     * Determines if this file is executable.
     *
     * @return {@code true} if this file is executable, {@code false} if not.
     * @throws FileSystemException On error determining if this file exists.
     */
    @Override
    public boolean isExecutable() throws FileSystemException {
        try {
            return exists() ? doIsExecutable() : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/check-is-executable.error", fileName, exc);
        }
    }

    /**
     * Checks if this file is a regular file by using its file type.
     *
     * @return true if this file is a regular file.
     * @throws FileSystemException if an error occurs.
     * @see #getType()
     * @see FileType#FILE
     */
    @Override
    public boolean isFile() throws FileSystemException {
        // Use equals instead of == to avoid any class loader worries.
        return FILE.equals(getType());
    }

    /**
     * Checks if this file is a folder by using its file type.
     *
     * @return true if this file is a regular file.
     * @throws FileSystemException if an error occurs.
     * @see #getType()
     * @see FileType#FOLDER
     */
    @Override
    public boolean isFolder() throws FileSystemException {
        // Use equals instead of == to avoid any class loader worries.
        return FOLDER.equals(getType());
    }

    /**
     * Determines if this file can be read.
     *
     * @return true if the file is a hidden file, false otherwise.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean isHidden() throws FileSystemException {
        try {
            return exists() ? doIsHidden() : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/check-is-hidden.error", fileName, exc);
        }
    }

    /**
     * Determines if this file can be read.
     *
     * @return true if the file can be read, false otherwise.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean isReadable() throws FileSystemException {
        try {
            return exists() ? doIsReadable() : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/check-is-readable.error", fileName, exc);
        }
    }

    /**
     * Checks if this fileObject is the same file as {@code destFile} just with a different name. E.g. for case
     * insensitive file systems like windows.
     *
     * @param destFile The file to compare to.
     * @return true if the FileObjects are the same.
     * @throws FileSystemException if an error occurs.
     */
    protected final boolean isSameFile(final FileObject destFile) throws FileSystemException {
        return doIsSameFile(destFile);
    }

    /**
     * Determines if this file can be read.
     *
     * @return true if the file can be read, false otherwise.
     * @throws FileSystemException if an error occurs.
     * @since 2.4
     */
    @Override
    public boolean isSymbolicLink() throws FileSystemException {
        try {
            return exists() ? doIsSymbolicLink() : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/check-is-symbolic-link.error", fileName, exc);
        }
    }

    /**
     * Determines if this file can be written to.
     *
     * @return true if the file can be written to, false otherwise.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean isWriteable() throws FileSystemException {
        try {
            if (exists()) {
                return doIsWriteable();
            }
            final FileObject parent = getParent();
            if (parent != null) {
                return parent.isWriteable();
            }
            return true;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/check-is-writeable.error", fileName, exc);
        }
    }

    /**
     * Returns an iterator over a set of all FileObject in this file object.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<FileObject> iterator() {
        try {
            return listFiles(Selectors.SELECT_ALL).iterator();
        } catch (final FileSystemException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Lists the set of matching descendants of this file, in depthwise order.
     *
     * @param selector The FileSelector.
     * @return list of files or null if the base file (this object) do not exist or the {@code selector} is null
     * @throws FileSystemException if an error occurs.
     */
    public List<FileObject> listFiles(final FileSelector selector) throws FileSystemException {
        if (!exists() || selector == null) {
            return null;
        }

        final ArrayList<FileObject> list = new ArrayList<>();
        this.findFiles(selector, true, list);
        return list;
    }

    /**
     * Moves (rename) the file to another one.
     *
     * @param destFile The target FileObject.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void moveTo(final FileObject destFile) throws FileSystemException {
        if (canRenameTo(destFile)) {
            if (!getParent().isWriteable()) {
                throw new FileSystemException("vfs.provider/rename-parent-read-only.error", getName(),
                        getParent().getName());
            }
        } else {
            if (!isWriteable()) {
                throw new FileSystemException("vfs.provider/rename-read-only.error", getName());
            }
        }

        if (destFile.exists() && !isSameFile(destFile)) {
            destFile.deleteAll();
            // throw new FileSystemException("vfs.provider/rename-dest-exists.error", destFile.getName());
        }

        if (canRenameTo(destFile)) {
            // issue rename on same filesystem
            try {
                // remember type to avoid attach
                final FileType srcType = getType();

                doRename(destFile);

                FileObjectUtils.getAbstractFileObject(destFile).handleCreate(srcType);
                destFile.close(); // now the destFile is no longer imaginary. force reattach.

                handleDelete(); // fire delete-events. This file-object (src) is like deleted.
            } catch (final RuntimeException re) {
                throw re;
            } catch (final Exception exc) {
                throw new FileSystemException("vfs.provider/rename.error", exc, getName(), destFile.getName());
            }
        } else {
            // different fs - do the copy/delete stuff

            destFile.copyFrom(this, Selectors.SELECT_SELF);

            if ((destFile.getType().hasContent()
                    && destFile.getFileSystem().hasCapability(Capability.SET_LAST_MODIFIED_FILE)
                    || destFile.getType().hasChildren()
                            && destFile.getFileSystem().hasCapability(Capability.SET_LAST_MODIFIED_FOLDER))
                    && fileSystem.hasCapability(Capability.GET_LAST_MODIFIED)) {
                destFile.getContent().setLastModifiedTime(this.getContent().getLastModifiedTime());
            }

            deleteSelf();
        }

    }

    /**
     * Clled after this file-object closed all its streams.
     */
    protected void notifyAllStreamsClosed() {
        // noop
    }

    /**
     * Notify the parent of a change to its children, when a child is created or deleted.
     *
     * @param childName The name of the child.
     * @param newType The type of the child.
     * @throws Exception if an error occurs.
     */
    private void notifyParent(final FileName childName, final FileType newType) throws Exception {
        final FileObject resolveParent = getParent();

        if (resolveParent != null) {
            FileObjectUtils.getAbstractFileObject(resolveParent).childrenChanged(childName, newType);
        }
    }

    /**
     * Called when the type or content of this file changes.
     * <p>
     * This implementation does nothing.
     * </p>
     *
     * @throws Exception if an error occurs.
     */
    protected void onChange() throws Exception {
        // noop
    }

    /**
     * Called when the children of this file change. Allows subclasses to refresh any cached information about the
     * children of this file.
     * <p>
     * This implementation does nothing.
     * </p>
     *
     * @param child The name of the child that changed.
     * @param newType The type of the file.
     * @throws Exception if an error occurs.
     */
    protected void onChildrenChanged(final FileName child, final FileType newType) throws Exception {
        // noop
    }

    /**
     * This will prepare the fileObject to get resynchronized with the underlying file system if required.
     *
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void refresh() throws FileSystemException {
        // Detach from the file
        try {
            detach();
        } catch (final Exception e) {
            throw new FileSystemException("vfs.provider/resync.error", fileName, e);
        }
    }

    private void removeChildrenCache() {
        children = null;
    }

    private FileObject resolveFile(final FileName child) throws FileSystemException {
        return fileSystem.resolveFile(child);
    }

    /**
     * Finds a file, relative to this file.
     *
     * @param path The path of the file to locate. Can either be a relative path, which is resolved relative to this
     *            file, or an absolute path, which is resolved relative to the file system that contains this file.
     * @return The FileObject.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject resolveFile(final String path) throws FileSystemException {
        final FileName otherName = fileSystem.getFileSystemManager().resolveName(fileName, path);
        return fileSystem.resolveFile(otherName);
    }

    /**
     * Returns a child by name.
     *
     * @param name The name of the child to locate.
     * @param scope the NameScope.
     * @return The FileObject for the file or null if the child does not exist.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public FileObject resolveFile(final String name, final NameScope scope) throws FileSystemException {
        // return fs.resolveFile(this.name.resolveName(name, scope));
        return fileSystem.resolveFile(fileSystem.getFileSystemManager().resolveName(this.fileName, name, scope));
    }

    private FileObject[] resolveFiles(final FileName[] children) throws FileSystemException {
        if (children == null) {
            return null;
        }

        final FileObject[] objects = new FileObject[children.length];
        for (int iterChildren = 0; iterChildren < children.length; iterChildren++) {
            objects[iterChildren] = resolveFile(children[iterChildren]);
        }

        return objects;
    }

    @Override
    public boolean setExecutable(final boolean readable, final boolean ownerOnly) throws FileSystemException {
        try {
            return exists() ? doSetExecutable(readable, ownerOnly) : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/set-executable.error", fileName, exc);
        }
    }

    private void setFileType(final FileType type) {
        if (type != null && type != FileType.IMAGINARY) {
            try {
                fileName.setType(type);
            } catch (final FileSystemException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        this.type = type;
    }

    @Override
    public boolean setReadable(final boolean readable, final boolean ownerOnly) throws FileSystemException {
        try {
            return exists() ? doSetReadable(readable, ownerOnly) : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/set-readable.error", fileName, exc);
        }
    }

    // --- OPERATIONS ---

    @Override
    public boolean setWritable(final boolean readable, final boolean ownerOnly) throws FileSystemException {
        try {
            return exists() ? doSetWritable(readable, ownerOnly) : false;
        } catch (final Exception exc) {
            throw new FileSystemException("vfs.provider/set-writeable.error", fileName, exc);
        }
    }

    /**
     * Returns the URI as a String.
     *
     * @return Returns the URI as a String.
     */
    @Override
    public String toString() {
        return fileName.getURI();
    }
}
