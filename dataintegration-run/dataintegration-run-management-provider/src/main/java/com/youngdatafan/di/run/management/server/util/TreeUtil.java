package com.youngdatafan.di.run.management.server.util;

import com.youngdatafan.di.run.management.server.dto.PluginInfoDTO;
import java.util.ArrayList;
import java.util.List;
import org.pentaho.di.core.util.StringUtil;

public class TreeUtil {

    protected TreeUtil() {

    }

    private final static String TOP_NODE_ID = "0";

    /**
     * 用于树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> PluginInfoDTO<T> build(List<PluginInfoDTO<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<PluginInfoDTO<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pid = node.getSecondPluginType();
            if (StringUtil.isEmpty(pid) || TOP_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (PluginInfoDTO<T> n : nodes) {
                String id = n.getPluginId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        PluginInfoDTO<T> root = new PluginInfoDTO<>();
        root.setPluginId("0");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        return root;
    }





}