package com.github.vfss3.operations;

import org.apache.commons.vfs2.operations.FileOperation;

/**
 * Different S3 implementations have slightly different behaviour and we need to control it on code side
 *
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
public interface PlatformFeatures extends FileOperation {
    /**
     * Default permission for owner - everybody have `allowed`, but Yandex deny for everybody.
     *
     * @return
     */
    boolean defaultAllowForOwner();

    /**
     * Mail.ru implementation doesn't allow to deny access for an owner.
     *
     * @return
     */
    boolean allowDenyForOwner();

    /**
     * Does it support server-side encryption
     *
     * @return
     */
    boolean supportsServerSideEncryption();

    /**
     * Does it support special group for authorized requests - For AliCloud it is only Owner and Everyone
     * @return
     */
    boolean supportsAuthorizedGroup();

    /**
     * Does it support Acl for files.
     * @return
     */
    boolean supportsAcl();
}
