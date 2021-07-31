package com.zp;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author zhoupeng
 * @Date 2021-07-31 11:23
 */
public class MyWathcer {
    public static void main(String[] args) throws Exception {
        String zookeeperConnectionString = "";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

        MyWathcer myWathcer = new MyWathcer();
        // 监听指定节点
        myWathcer.nodeCache(client);
        // 监听指定节点的子节点
        myWathcer.pathChildrenCache(client);
        // 监听指定节点及其子孙节点
        myWathcer.treeCache(client);
    }

    public void nodeCache(CuratorFramework client) throws Exception {

        //获取监听对象
        final NodeCache nodeCache = new NodeCache(client, "/app2");

        //添加监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {

            //监听回调函数，单个监听到节点发生增删改操作时会执行此方法
            @Override
            public void nodeChanged() throws Exception {
                String path = nodeCache.getPath();
                System.out.println(path + "节点收到了操作！");
                //获取当前节点更新后的数据
                byte[] data = nodeCache.getCurrentData().getData();
                System.out.println("更新后的数据为：" + new String(data));
            }
        });

        //开启监听，如果为true则开启监听器
        nodeCache.start(true);

        System.out.println("监听器已开启！");
        //让线程休眠30s(为了方便测试)
        Thread.sleep(1000 * 30);
    }

    public void pathChildrenCache(CuratorFramework client) throws Exception {
    /*
            获取监听对象
            参数1：客户端连接对象
            参数2：节点，监听的是指定节点的子节点
            参数3：是否开启缓存
        */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/app2", true);

        //添加监听
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

            /**
             * 监听回调函数
             * @param curatorFramework 客户端连接对象
             * @param pathChildrenCacheEvent 监听事件对象
             * @throws Exception
             */
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

                //监听子节点的数据修改事件

                //获取子节点的改变类型
                PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                //判断监听子节点的改变类型是否为数据修改(UPDATE)
                if (type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {

                    System.out.println(pathChildrenCacheEvent);
                    //从监听事件对象中获取修改后的数据
                    byte[] data = pathChildrenCacheEvent.getData().getData();
                    System.out.println(new String(data));
                }
            }
        });

        //开启监听器
        pathChildrenCache.start();

        System.out.println("监听器已开启！");

        //休眠线程，方便测试
        Thread.sleep(1000 * 120);

    }

    public void treeCache(CuratorFramework client) throws Exception {
        //指定监听对象
        TreeCache treeCache = new TreeCache(client, "/app2/op1");

        //添加监听器
        treeCache.getListenable().addListener(new TreeCacheListener() {

            /**
             * 监听回调函数
             * @param curatorFramework 客户端连接对象
             * @param treeCacheEvent    监听事件对象
             * @throws Exception
             */
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("子节点被改变！");
                System.out.println(treeCacheEvent);
            }
        });

        //开启监听
        treeCache.start();
        System.out.println("监听器已开启！");
        Thread.sleep(1000 * 120);

    }


}
