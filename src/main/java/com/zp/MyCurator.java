package com.zp;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * @Author zhoupeng
 * @Date 2021-07-31 10:21
 */
public class MyCurator {
    public static void main(String[] args) throws Exception {
        String zookeeperConnectionString = "";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("event received");
            }
        });
        client.start();
        client.create().forPath("/my/path", "fsda".getBytes());
        client.setData().forPath("/my/path");
        client.getData().forPath("/my/path");
        List<String> strings = client.getChildren().forPath("/my/path");
        Void aVoid = client.watchers().add().withMode(AddWatchMode.PERSISTENT).forPath("/my/path");


        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/sdfsd");
    }
}
