/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package com.youngdatafan.di.run.management.server.trans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.HasLogChannelInterface;
import org.pentaho.di.core.logging.KettleLogLayout;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogParentProvidedInterface;
import org.pentaho.di.core.logging.LoggingRegistry;

/**
 * 日志浏览器
 *
 * @author gavin
 * @since 2020-02-13 16:11:22
 */
public class LogBrowser {
    // for i18n purposes, needed by Translator2!!
    private static Class<?> PKG = LogBrowser.class;

    private LogParentProvidedInterface logProvider;
    private List<String> childIds = new ArrayList<>();
    private Date lastLogRegistryChange;
    private AtomicBoolean paused;

    private AtomicInteger lastLogId;
    private AtomicBoolean busy;
    private KettleLogLayout logLayout;

    public LogBrowser(LogParentProvidedInterface logProvider) {
        this.logProvider = logProvider;
        this.paused = new AtomicBoolean(false);

        // installLogSniffer
        lastLogId = new AtomicInteger(-1);
        busy = new AtomicBoolean(false);
        logLayout = new KettleLogLayout(true);
    }

    /**
     * 获取实时日志
     */
    public String getRealTimeLog() {
        StringBuilder logBuilder = new StringBuilder();

        HasLogChannelInterface provider = logProvider.getLogChannelProvider();

        if (provider != null && !busy.get() && !paused.get()) {
            busy.set(true);

            LogChannelInterface logChannel = provider.getLogChannel();
            String parentLogChannelId = logChannel.getLogChannelId();
            LoggingRegistry registry = LoggingRegistry.getInstance();
            Date registryModDate = registry.getLastModificationTime();

            if (childIds == null
                    || lastLogRegistryChange == null || registryModDate.compareTo(lastLogRegistryChange) > 0) {
                lastLogRegistryChange = registry.getLastModificationTime();
                childIds = LoggingRegistry.getInstance().getLogChannelChildren(parentLogChannelId);
            }

            // See if we need to log any lines...
            //
            int lastNr = KettleLogStore.getLastBufferLineNr();
            if (lastNr > lastLogId.get()) {
                List<KettleLoggingEvent> logLines =
                        KettleLogStore.getLogBufferFromTo(childIds, false, lastLogId.get(), lastNr);

                // 获取日志
                for (KettleLoggingEvent logLine : logLines) {
                    String line = logLayout.format(logLine).trim();
                    // append
                    logBuilder.append(line).append(Const.CR);
                }
            }

            lastLogId.set(lastNr);
            busy.set(false);
        }

        return logBuilder.toString();
    }

    public LogParentProvidedInterface getLogProvider() {
        return logProvider;
    }

    public boolean isPaused() {
        return paused.get();
    }

    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }

}
