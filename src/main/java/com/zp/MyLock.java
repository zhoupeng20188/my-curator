package com.zp;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhoupeng
 * @Date 2021-07-31 10:26
 */
public class MyLock {
    public static void main(String[] args) throws Exception {

        String zookeeperConnectionString = "";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/lock");
        if ( lock.acquire(1000, TimeUnit.MILLISECONDS) )
        {
            try
            {
                // do some work inside of the critical section here
            }
            finally
            {
                lock.release();
            }
        }
    }
}
