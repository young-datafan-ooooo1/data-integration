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
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.VfsComponent;
import org.apache.commons.vfs2.provider.VfsComponentContext;

/**
 * A partial {@link VfsComponent} implementation.
 */
abstract class AbstractVfsComponent implements VfsComponent {

    private VfsComponentContext context;
    private Log log;

    /**
     * Sets the Logger to use for the component.
     *
     * @param log The Log to use.
     */
    @Override
    public final void setLogger(final Log log) {
        this.log = log;
    }

    /**
     * Sets the context for this file system provider.
     *
     * @param context The VfsComponentContext.
     */
    @Override
    public final void setContext(final VfsComponentContext context) {
        this.context = context;
    }

    /**
     * Initializes the component. This implementation does nothing.
     *
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public void init() throws FileSystemException {
        // default is noop.
    }

    /**
     * Closes the provider. This implementation does nothing.
     */
    @Override
    public void close() {
        // default is noop.
    }

    /**
     * Returns the logger for this file system to use.
     *
     * @return logger for this file system
     */
    protected final Log getLogger() {
        return log;
    }

    /**
     * Returns the context for this provider.
     *
     * @return provider context
     */
    protected final VfsComponentContext getContext() {
        return context;
    }
}
