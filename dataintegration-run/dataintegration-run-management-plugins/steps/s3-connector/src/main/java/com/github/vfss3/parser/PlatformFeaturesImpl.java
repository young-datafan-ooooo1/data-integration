package com.github.vfss3.parser;

import com.github.vfss3.operations.PlatformFeatures;

import java.util.Objects;

/**
 * @author <A href="mailto:alexey@abashev.ru">Alexey Abashev</A>
 */
class PlatformFeaturesImpl implements PlatformFeatures {
    private final boolean defaultAllowForOwner;
    private final boolean allowDenyForOwner;
    private final boolean supportsServerSideEncryption;
    private final boolean supportsAuthorizedGroup;
    private final boolean supportsAcl;

    public PlatformFeaturesImpl(
            boolean defaultAllowForOwner, boolean allowDenyForOwner, boolean supportsServerSideEncryption,
            boolean supportsAuthorizedGroup, boolean supportsAcl
    ) {
        this.defaultAllowForOwner = defaultAllowForOwner;
        this.allowDenyForOwner = allowDenyForOwner;
        this.supportsServerSideEncryption = supportsServerSideEncryption;
        this.supportsAuthorizedGroup = supportsAuthorizedGroup;
        this.supportsAcl = supportsAcl;
    }

    @Override
    public boolean defaultAllowForOwner() {
        return defaultAllowForOwner;
    }

    @Override
    public boolean allowDenyForOwner() {
        return allowDenyForOwner;
    }

    @Override
    public boolean supportsServerSideEncryption() {
        return supportsServerSideEncryption;
    }

    @Override
    public boolean supportsAuthorizedGroup() {
      return supportsAuthorizedGroup;
    }

    @Override
    public boolean supportsAcl() {
        return supportsAcl;
    }

    @Override
    public final void process() {
        // Nothing to do
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformFeaturesImpl that = (PlatformFeaturesImpl) o;
        return defaultAllowForOwner == that.defaultAllowForOwner &&
                allowDenyForOwner == that.allowDenyForOwner &&
                supportsServerSideEncryption == that.supportsServerSideEncryption;
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultAllowForOwner, allowDenyForOwner, supportsServerSideEncryption);
    }

    @Override
    public String toString() {
        return "PlatformFeaturesImpl{" +
            "defaultAllowForOwner=" + defaultAllowForOwner +
            ", allowDenyForOwner=" + allowDenyForOwner +
            ", supportsServerSideEncryption=" + supportsServerSideEncryption +
            ", supportsAuthorizedGroup=" + supportsAuthorizedGroup +
            '}';
    }
}
