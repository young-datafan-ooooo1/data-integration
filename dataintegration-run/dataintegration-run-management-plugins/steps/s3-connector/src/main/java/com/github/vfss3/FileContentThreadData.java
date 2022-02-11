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

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.RandomAccessContent;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Holds the data which needs to be local to the current thread
 */
class FileContentThreadData {

    // private int state = DefaultFileContent.STATE_CLOSED;

    private final ArrayList<InputStream> inputStreamList = new ArrayList<>();
    private final ArrayList<RandomAccessContent> randomAccessContentList = new ArrayList<>();
    private DefaultFileContent.FileContentOutputStream outputStream;

    FileContentThreadData() {
    }

    /*
     * int getState() { return state; }
     *
     * void setState(int state) { this.state = state; }
     */

    void addInstr(final InputStream inputStream) {
        this.inputStreamList.add(inputStream);
    }

    void setOutstr(final DefaultFileContent.FileContentOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    DefaultFileContent.FileContentOutputStream getOutstr() {
        return this.outputStream;
    }

    void addRastr(final RandomAccessContent randomAccessContent) {
        this.randomAccessContentList.add(randomAccessContent);
    }

    int getInstrsSize() {
        return this.inputStreamList.size();
    }

    public Object removeInstr(final int pos) {
        return this.inputStreamList.remove(pos);
    }

    public void removeInstr(final InputStream inputStream) {
        this.inputStreamList.remove(inputStream);
    }

    public Object removeRastr(final int pos) {
        return this.randomAccessContentList.remove(pos);
    }

    public void removeRastr(final RandomAccessContent randomAccessContent) {
        this.randomAccessContentList.remove(randomAccessContent);
    }

    public boolean hasStreams() {
        return inputStreamList.size() > 0 || outputStream != null || randomAccessContentList.size() > 0;
    }

    public void closeOutstr() throws FileSystemException {
        outputStream.close();
        outputStream = null;
    }

    int getRastrsSize() {
        return randomAccessContentList.size();
    }
}
