package zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper配置
 * created by XUAN on 2019/8/12
 */
public class ZookeeperSample {

    //public static final String zkServer = "140.143.206.160:2181";
    public static final String zkServer = "140.143.206.160:2181,140.143.206.160:2182,140.143.206.160:2183";

    public static final Integer timeout = 5000;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        // 异步连接, 回调后才会真正连接成功, Watcher就是这个连接成功回调事件
        ZooKeeper zooKeeper = new ZooKeeper(zkServer, timeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.EventType type = watchedEvent.getType();
                if (Event.EventType.None == type) {
                    System.out.println("收到zookeeper [none] 事件");
                } else if (Event.EventType.NodeCreated == type) {
                    System.out.println("收到zookeeper [NodeCreated] 事件");
                } else if (Event.EventType.NodeDataChanged == type) {
                    System.out.println("收到zookeeper [NodeDataChanged] 事件");
                    countDownLatch.countDown();
                } else if (Event.EventType.NodeDeleted == type) {
                    System.out.println("收到zookeeper [NodeDeleted] 事件");
                } else if (Event.EventType.DataWatchRemoved == type) {
                    System.out.println("收到zookeeper [DataWatchRemoved] 事件");
                } else if (Event.EventType.NodeChildrenChanged == type) {
                    System.out.println("收到zookeeper [NodeChildrenChanged] 事件");
                } else if (Event.EventType.ChildWatchRemoved == type) {
                    System.out.println("收到zookeeper [ChildWatchRemoved] 事件");
                }
            }
        });
        // CONNECTING
        System.out.println("客户端连接zookeeper服务器, 连接状态" + zooKeeper.getState().name());
        Thread.sleep(2000 );
        // CONNECTED
        System.out.println("连接状态" + zooKeeper.getState().name());

        //createDataNodeSync(zooKeeper);
        //createDataNodeASync(zooKeeper);
        //createChangeDataSync(zooKeeper);
        //deleteDataNodeSync(zooKeeper);
        //getNodeDataASync(zooKeeper);
        //getChildNodeData(zooKeeper, "/async");
        //nodeAcl(zooKeeper, "/acl");
        nodeAclIp(zooKeeper, "/acl");

    }

    private static void nodeAclIp(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        path = path + "/ip";
        String data = "data";
        ArrayList<ACL> acls = new ArrayList<>();
        Id ip = new Id("ip", "10.45.71.150");
        acls.add(new ACL(ZooDefs.Perms.ALL, ip));

        Stat exists = zooKeeper.exists(path, false);
        if (exists == null) {
            zooKeeper.create(path, data.getBytes(), acls, CreateMode.PERSISTENT);
        }
        Stat stat = new Stat();
        byte[] result = zooKeeper.getData(path, true, stat);
        System.out.println(path + "的data: " + new String(result));

    }

    private static void nodeAcl(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        path = path + "/aa";
        String data = "data";
        ArrayList<ACL> acls = new ArrayList<>();
        Id xuan1 = new Id("digest", DigestAuthenticationProvider.generateDigest("xuan1:123456"));
        Id xuan2 = new Id("digest", DigestAuthenticationProvider.generateDigest("xuan2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, xuan1));
        acls.add(new ACL(ZooDefs.Perms.READ, xuan2));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, xuan2));

        // 如果没有权限, 则添加权限
        zooKeeper.addAuthInfo("digest", "xuan1:123456".getBytes());
        Stat exists = zooKeeper.exists(path, false);
        if (exists == null) {
            zooKeeper.create(path, data.getBytes(), acls, CreateMode.PERSISTENT);
        }

        // 获得权限
        // 会回填数据
        Stat stat = new Stat();
        List<ACL> aclList = zooKeeper.getACL(path, stat);
        for (ACL acl : aclList) {
            System.out.println(acl.getId().getScheme() + "   " + acl.getId().getId());
        }
    }

    /**
     * 获得子节点数据
     * @param zooKeeper
     */
    private static void getChildNodeData(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, true);
        if (children != null && children.size() > 0) {
            for (String child : children) {
                getChildNodeData(zooKeeper, path + "/" + child);
            }
        } else {
            // 当前节点没有子节点了
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(path, true, stat);
            System.out.println("子节点" + path + "的数据为" + new String(data));
        }
        if (countDownLatch.getCount() > 0) {
            countDownLatch.await();
        }
    }

    /**
     * 获得节点数据
     * @param zooKeeper
     */
    private static void getNodeDataASync(ZooKeeper zooKeeper) throws InterruptedException {
        String path = "/async";
        Stat stat = new Stat();

        //第二个参数为true, 表示注册了默认事件
        zooKeeper.getData(path, true, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
                System.out.println("获得节点数据:" + path);
                System.out.println(ctx.toString());
                // 如果节点不存在的话. 这里bytes就会为null
                if (bytes != null) {
                    System.out.println("data: " + new String(bytes));
                }
                System.out.println("rc: " + rc);
                if (stat != null) {
                    System.out.println("stat: " + stat.toString());
                }
            }
        }, "获得数据成功");
        // 修改节点数据可继续执行
        countDownLatch.await();
        //Thread.sleep(5000);
    }

    /**
     * 删除节点
     * @param zooKeeper
     */
    private static void deleteDataNodeSync(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String path = "/xuan";
        Stat exists = zooKeeper.exists(path, false);
        if (exists != null) {
            zooKeeper.delete(path, exists.getVersion());
            System.out.println("删除节点" + path + "成功");
        } else {
            System.out.println("节点" + path + "不存在");
        }
    }

    /**
     * 修改节点数据
     * @param zooKeeper
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static void createChangeDataSync(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String path = "/xuan";
        String data = "change";
        Stat exists = zooKeeper.exists(path, false);
        if (exists != null) {
            Stat stat = zooKeeper.setData(path, data.getBytes(), exists.getVersion());
            if (stat != null) {
                System.out.println("修改节点数据" + stat.toString());
            } else {
                System.out.println("修改data返回为null");
            }
        } else {
            System.out.println("节点" + path + "不存在");
        }


    }

    /**
     * 异步创建节点
     * @param zooKeeper
     */
    private static void createDataNodeASync(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String path = "/async";
        String data = "async";
        Stat exists = zooKeeper.exists(path, false);
        if (exists == null) {
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    // 创建后的回调
                    System.out.println("创建节点:" + path);
                    // 这里o就是ctx
                    System.out.println(ctx.toString());

                    System.out.println();
                    // 其他参数:
                    System.out.println("rc: " + rc);
                    System.out.println("name: " + name);
                }
            }, "创建成功");
            System.out.println("创建节点" + path);
            Thread.sleep(50000);
        } else {
            System.out.println("节点" + path + "已存在");
        }
    }

    /**
     * 同步创建节点
     * @param zooKeeper
     */
    private static void createDataNodeSync(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String path = "/xuan";
        String data = "xuan";
        Stat exists = zooKeeper.exists(path, false);
        if (exists == null) {
            String createResult = zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("创建节点" + path);
            System.out.println("创建节点返回  " + createResult);
        } else {
            System.out.println("节点" + path + "已存在");
        }
    }
}
