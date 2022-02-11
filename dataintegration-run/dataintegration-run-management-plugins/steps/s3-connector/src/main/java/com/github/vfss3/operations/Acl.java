package com.github.vfss3.operations;

import java.util.*;

/**
 * ACL for S3.
 * Usage samples:
 * Allow everyone to read:
 * <code>
 * acl.allow(Acl.Group.GUEST, Acl.Permission.READ);
 * </code>
 * Allow all authorized users to read and write:
 * <code>
 * Acl.Permission[] rights = {Acl.Permission.READ, Acl.Permission.WRITE};
 * acl.allow(Acl.Group.AUTHORIZED, rights);
 * </code>
 * Deny all (owner still has access to overwrite ACL):
 * <code>
 * acl.denyAll();
 * </code>
 *
 * @author Marat Komarov
 * @author <A href="mailto:alex@intridea.com">Alex Kovalyov</A>
 */
public class Acl {
    public enum Permission {
        READ, WRITE
    }

    public enum Group {
        OWNER, AUTHORIZED, EVERYONE
    };

    /**
     * Number of available groups
     */
    private int groupsCount = Group.values().length;

    /**
     * Number of available rules
     */
    private int rulesCount = Permission.values().length;

    /**
     * Internal rights holder
     */
    private byte[][] rulesTable;

    /**
     * @see {@link #getRules()}
     */
    private Map<Group, Permission[]> rules;

    /**
     * Will be True if rules were changed since last {@link #getRules()} call.
     */
    private boolean changed = true;

    /**
     * Create new empty ACL.
     */
    public Acl() {
        this(null);
    }

    /**
     * Create ACL and load rules.
     * @param rules A set of predefined rules.
     */
    public Acl(Map<Group, Permission[]> rules) {
        rulesTable = new byte[groupsCount][rulesCount];
        if (rules != null) {
            setRules(rules);
        } else {
            denyAll();
        }
    }

    /**
     * Allow access for a group
     * @param group
     * @param permission
     */
    public void allow (Group group, Permission permission) {
        setRule(group, permission, (byte) 1);
    }

    /**
     * Set a list of permissions for a group.
     * @param group
     * @param permission_list
     */
    public void allow (Group group, Permission[] permission_list) {
        setRule(group, permission_list, (byte) 1);
    }

    /**
     * Allow all permissions for a group
     * @param group
     */
    public void allow (Group group) {
        setRule(group, (byte) 1);
    }

    /**
     * Allow specific permission for all
     */
    public void allow (Permission permission) {
        setRule(permission, (byte) 1);
    }

    /**
     * Allow access for all
     */
    public void allow (Permission[] permission_list) {
        setRule(permission_list, (byte) 1);
    }

    /**
     * Allow all to all
     */
    public void allowAll () {
        setRule((byte) 1);
    }

    /**
     * Deny right to group
     */
    public void deny (Group group, Permission permission) {
        setRule(group, permission, (byte) 0);
    }

    /**
     * Deny access for a group
     */
    public void deny (Group group, Permission[] permission_list) {
        setRule(group, permission_list, (byte) 0);
    }

    /**
     * Deny all to for a group
     * @param group
     */
    public void deny (Group group) {
        setRule(group, (byte) 0);
    }

    /**
     * Deny access for all
     */
    public void deny (Permission permission) {
        setRule(permission, (byte) 0);
    }

    /**
     * Deny access for all
     */
    public void deny (Permission[] permission) {
        setRule(permission, (byte) 0);
    }

    /**
     * Completely deny.
     */
    public void denyAll () {
        setRule((byte) 0);
    }

    /**
     * Returns true when a group has specific access.
     */
    public boolean isAllowed(Group group, Permission permission) {
        return rulesTable[group.ordinal()][permission.ordinal()] == 1;
    }

    /**
     * Returns true when specific access is denied for a group
     */
    public boolean isDenied(Group group, Permission permission) {
        return rulesTable[group.ordinal()][permission.ordinal()] == 0;
    }

    /**
     * Sets a list of allowed rules. Calls {@link #denyAll()} and then applies rules.
     *
     * @param rules Access rule to apply.
     */
    public void setRules(Map<Group, Permission[]> rules) {
        // Deny all by default
        denyAll();

        // Set allow rules
        for (Group group : rules.keySet()) {
            Permission[] permissions = rules.get(group);

            allow(group, permissions);
        }
    }

    /**
     * Returns a list of allowed rules.
     *
     * @return
     */
    public Map<Group, Permission[]> getRules() {
        if (changed) {
            Map<Group, Permission[]> result = new HashMap<Group, Permission[]>(groupsCount);
            Permission[] rightValues = Permission.values();

            for (Group group : Group.values()) {
                int groupIndex = group.ordinal();
                List<Permission> permissions = new ArrayList<Permission>(rulesCount);

                for (int i = 0; i < rulesCount; i++) {
                    if (rulesTable[groupIndex][i] == 1) {
                        permissions.add(rightValues[i]);
                    }
                }

                result.put(group, permissions.toArray(new Permission[permissions.size()]));
            }

            rules = result;
        }

        return rules;
    }

    /*
     * Helper methods
     *
     */

    private void setRule (Group group, Permission permission, byte allow) {
        rulesTable[group.ordinal()][permission.ordinal()] = allow;
        changed = true;
    }

    private void setRule (Group group, Permission[] permissions, byte allow) {
        int groupIndex = group.ordinal();
        for (Permission permission : permissions) {
            rulesTable[groupIndex][permission.ordinal()] = allow;
        }
        changed = true;
    }

    private void setRule (Group group, byte allow) {
        int groupIndex = group.ordinal();
        for (int i=0; i<rulesCount; i++) {
            rulesTable[groupIndex][i] = allow;
        }
        changed = true;
    }

    private void setRule (Permission permission, byte allow) {
        int i = permission.ordinal();
        for (int j=0; j<groupsCount; j++) {
            rulesTable[j][i] = allow;
        }
    }

    private void setRule (Permission[] permissions, byte allow) {
        for (Permission permission : permissions) {
            int i = permission.ordinal();
            for (int j=0; j<groupsCount; j++) {
                rulesTable[j][i] = allow;
            }
        }
    }

    private void setRule (byte allow) {
        for (int j=0; j<groupsCount; j++) {
            for (int i=0; i<rulesCount; i++) {
                rulesTable[j][i] = allow;
            }
        }
        changed = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Group, Permission[]> entry : getRules().entrySet()) {
            sb.append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue())).append(',');
        }

        return "Acl{rules={" + sb.toString() + "}}";
    }
}
