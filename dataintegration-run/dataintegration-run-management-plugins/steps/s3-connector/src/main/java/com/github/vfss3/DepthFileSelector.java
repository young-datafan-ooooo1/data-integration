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

import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

/**
 * A file selector that operates depth of the directory structure and will
 * select all files up to and including the depth given in the constructor.
 *
 * @author Matthias L. Jugel
 */
public class DepthFileSelector implements FileSelector {
    private final int maxDepth;
    private final int minDepth;

    /**
     * Create a file selector that will select ALL files.
     */
    public DepthFileSelector() {
        this(0, Integer.MAX_VALUE);
    }

    /**
     * Create a file selector that will select all files up to and including the
     * directory depth.
     *
     * @param depth
     *            the maximum depth
     */
    public DepthFileSelector(int depth) {
        this(0, depth);
    }

    /**
     * Create a file selector  that will select all files between min and max directory depth
     * @param min
     * @param max
     */
    public DepthFileSelector(int min, int max) {
        minDepth = min;
        maxDepth = max;
    }

    @Override
    public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
        int depth = fileSelectInfo.getDepth();
        return depth >= minDepth && depth <= maxDepth;
    }

    @Override
    public boolean traverseDescendents(FileSelectInfo fileSelectInfo)
            throws Exception {
        return fileSelectInfo.getDepth() < maxDepth;
    }
}
