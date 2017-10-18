package com.effective.common.zk;

import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperInstance zookeeperInstance = new ZookeeperInstance();

        String host = "localhost:2181";

        zookeeperInstance.connect(host);

        System.out.println("1、--------connect zookeeper ok-----------\n");
/*
        boolean isExists = zookeeperInstance.exists("/test");
        if (isExists) {
            zookeeperInstance.deleteNode("/test", -1);
            System.out.println("2、--------delete znode ok-----------\n");
        }
        System.out.println("3、--------exists znode ok-----------\n");


        byte[] data = {1, 2, 3, 4, 5};
        String result = zookeeperInstance.createNode("/test", data);
        System.out.println(result);
        System.out.println("4、--------create znode ok-----------\n");


        List<String> children = zookeeperInstance.getChildren("/");
        for (String child : children) {
            System.out.println(child);
        }
        System.out.println("5、--------get children znode ok-----------\n");

        byte[] nodeData = zookeeperInstance.getData("/test");
        System.out.println(Arrays.toString(nodeData));
        System.out.println("6、--------get znode data ok-----------\n");
*/
        PushNode bidderNode = new PushNode();
        bidderNode.setIdc("101");
        bidderNode.setIpPort("127.0.0.1");


        String json = JSONObject.toJSONString(bidderNode);

        byte[] data = json.getBytes();
        zookeeperInstance.setData("/test", data, 0);

        byte[] nodeData = zookeeperInstance.getData("/test");
        System.out.println(Arrays.toString(nodeData));
        System.out.println("8、--------get znode new data ok-----------\n");

        zookeeperInstance.closeConnect();
        System.out.println("9、--------close zookeeper ok-----------\n");
    }

}
