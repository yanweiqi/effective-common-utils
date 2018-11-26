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

        boolean isExists = zookeeperInstance.exists("/test");
        if (isExists) {
            zookeeperInstance.deleteNode("/test", -1);
            System.out.println("2、--------delete znode ok-----------\n");
        }

        byte[] data = {1, 2, 3, 4, 5};
        String result = zookeeperInstance.createNode("/test", data);
        System.out.println(result);
        System.out.println("3、--------create znode ok-----------\n");

        Thread.sleep(1000*60*5);



    }

}
