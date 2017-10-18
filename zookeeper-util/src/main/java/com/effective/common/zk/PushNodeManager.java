package com.effective.common.zk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PushNode Manager
 *
 * @author liuzhankun
 * @date 2013-7-18
 */
public class PushNodeManager {

    private static final Logger log = LoggerFactory.getLogger(PushNodeManager.class);

    /*
     * 节点信息
     * KEY：<ip>:<port>_<timestamp>
     * VALUE: PushNode
     */
    public static Map<String, PushNode> nodes = new ConcurrentHashMap<String, PushNode>();
    /*
     * 保存各个IDC的节点信息
     * KEY：<idcname>
     * VALUE：Set<ipPorts>
     */
    public static Map<String, List<String>> idcs = new ConcurrentHashMap<String, List<String>>();

    /**
     * 刷新节点信息
     */
    public static synchronized void refresh(Map<String, String> nodeInfos) {
        // 清空原有数据
        log.info("clear nodes, old nodes=" + nodes + ", idcs=" + idcs);
        nodes.clear();
        idcs.clear();

        if (nodeInfos == null || nodeInfos.size() == 0) {
            return;
        }

        for (String path : nodeInfos.keySet()) {
            String data = nodeInfos.get(path);
            PushNode pushNode = parseNode(data);

            if (pushNode == null) {
                log.info("pushNode is null, path=" + path + ", data=" + data);
                continue;
            }

            if (pushNode.getIpPort() == null) {
                continue;
            }
            nodes.put(path, pushNode);

            if (pushNode.getIdc() == null) {
                continue;
            }
            List<String> list = idcs.get(pushNode.getIdc());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(pushNode.getIpPort());
            idcs.put(pushNode.getIdc(), list);
        }

        log.info("new nodes=" + nodes + ", idcs=" + idcs);
    }

    /**
     * 判断节点是否活跃
     */
    public static boolean isActive(String nodeName) {
        PushNode pushNode = nodes.get(nodeName);
        if (pushNode == null) {
            return false;
        }
        return true;
    }

    /**
     * 解析数据信息
     */
    private static PushNode parseNode(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        PushNode node = new PushNode();
        try {
            JSONObject js = JSON.parseObject(text);
            if (js.containsKey("idc")) {
                node.setIdc(js.getString("idc"));
            }
            if (js.containsKey("ip_port")) {
                node.setIpPort(js.getString("ip_port"));
            }
            return node;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获得某个IDC可用的Push Server
     */
    public static String getIpPort(String idc) {

        List<String> list = null;
        if ("ctc".equals(idc)) {
            list = idcs.get(idc);
        } else {
            if (idc != null) {
                list = idcs.get(idc);
            }
            if (list == null || list.isEmpty()) {
                list = idcs.get("ctc");
            }
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        int idx = java.util.concurrent.ThreadLocalRandom.current().nextInt(list.size());
        return list.get(idx);
    }

    /**
     * 获得某个IDC可用的Push Server 列表
     */
    public static List<String> getIpPortList() {
        List<String> list = new ArrayList<String>();
        for (List<String> idcList : idcs.values()) {
            if (idcList == null || idcList.isEmpty()) {
                continue;
            }
            int size = idcList.size();
            if (size > 2) {
                int rand = java.util.concurrent.ThreadLocalRandom.current().nextInt(size);
                int rand2 = rand;
                while (rand == rand2) {
                    rand2 = java.util.concurrent.ThreadLocalRandom.current().nextInt(size);
                }
                list.add(idcList.get(rand));
                list.add(idcList.get(rand2));
            } else if (size == 2) {
                list.add(idcList.get(0));
                list.add(idcList.get(1));
            } else if (size == 1) {
                list.add(idcList.get(0));
            }
        }
        return list;
    }

    /**
     * 获得所有的Push Server列表
     * @return
     */
    public static List<String> getIpPortAll() {
        List<String> list = new ArrayList<>();
        for (List<String> idcList : idcs.values()) {
            if(CollectionUtils.isNotEmpty(idcList)) {
                list.addAll(idcList);
            }
        }
        return list;
    }

}
