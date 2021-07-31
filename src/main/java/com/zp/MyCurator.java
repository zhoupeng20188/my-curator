package com.zp;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.AddWatchMode;

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
        client.start();
        client.create().forPath("/my/path", "fsda".getBytes());
        client.setData().forPath("/my/path");
        client.getData().forPath("/my/path");
        List<String> strings = client.getChildren().forPath("/my/path");
        Void aVoid = client.watchers().add().withMode(AddWatchMode.PERSISTENT).forPath("/my/path");
    }
}
