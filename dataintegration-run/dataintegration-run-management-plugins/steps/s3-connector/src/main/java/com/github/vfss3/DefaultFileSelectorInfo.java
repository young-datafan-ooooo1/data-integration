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
import org.apache.commons.vfs2.FileSelectInfo;

/**
 * A default {@link FileSelectInfo} implementation.
 */
final class DefaultFileSelectorInfo implements FileSelectInfo {

    private FileObject baseFolder;
    private FileObject file;
    private int depth;

    @Override
    public FileObject getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(final FileObject baseFolder) {
        this.baseFolder = baseFolder;
    }

    @Override
    public FileObject getFile() {
        return file;
    }

    public void setFile(final FileObject file) {
        this.file = file;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return super.toString() + " [baseFolder=" + baseFolder + ", file=" + file + ", depth=" + depth + "]";
    }
}
