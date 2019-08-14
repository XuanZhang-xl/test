package zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 使用curator操作zk
 * created by XUAN on 2019/8/14
 */
public class CuratorSample {

    public static void main(String[] args) throws Exception {
        CuratorOperator curatorOperator = new CuratorOperator();
        CuratorFramework operator = curatorOperator.curatorFramework;

        System.out.println("当前客户端状态: " + operator.getState().name());

        Thread.sleep(3000);
        operator.start();
        System.out.println("当前客户端状态: " + operator.getState().name());

        String nodePath = "/curator/text/a";
        String data = "data";
        //createNode(operator, nodePath, data);

        String newData = "newData";
        //updateNode(operator, nodePath, newData);

        // 删/curator及子节点
        String deleteNodePath = "/curator";
        // 删 /curator 下的/text及子节点
        //String deleteNodePath = "/curator/text";
        //deleteNode(operator, deleteNodePath);

        nodePath = "/curator";
        //childNode(operator, nodePath);

        //watcherOnce(operator, nodePath);


        //watcherForever(operator, nodePath);

        watcherForeverChild(operator, nodePath);

        Thread.sleep(3000);
        operator.close();
        System.out.println("当前客户端状态: " + operator.getState().name());
    }
    private static CountDownLatch countDownLatch = new CountDownLatch(4);

    /**
     * 监听子节点事件
     * @param operator
     * @param nodePath
     */
    private static void watcherForeverChild(CuratorFramework operator, String nodePath) throws Exception {
        // cacheData: 缓存 stat TODO: 哪个stat?
        PathChildrenCache childrenCache = new PathChildrenCache(operator, nodePath, true);
        // NORMAL:异步初始化, 同步数据时触发事件, 但是没有初始化事件
        // POST_INITIALIZED_EVENT:异步初始化, 有初始化事件
        // BUILD_INITIAL_CACHE: 同步初始化
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化ok");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    System.out.println("子节点" + event.getData().getPath() + "增加");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println("子节点" + event.getData().getPath() + "更新data: " + new String(event.getData().getData()));
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println("子节点" + event.getData().getPath() + "移除");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED)) {
                    // ???
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED)) {
                    // ???
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_LOST)) {
                    // ???
                }
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        // 如果异步初始化, 这里很有可能为空
        List<ChildData> childDataList = childrenCache.getCurrentData();
        System.out.println(nodePath + "的子节点数据列表:");
        for (ChildData childData : childDataList) {
            System.out.println(new String(childData.getData()));
        }
        Thread.sleep(1000000);
    }

    private static void watcherForever(CuratorFramework operator, String nodePath) throws Exception {
        NodeCache nodeCache = new NodeCache(operator, nodePath);

        // buildInitial: 初始化的时候获取node的值并缓存
        nodeCache.start(true);

        if (nodeCache.getCurrentData() != null) {
            System.out.println("节点初始化数据为: " + new String(nodeCache.getCurrentData().getData()));
        } else {
            System.out.println("节点初始化数据为空");
        }
        // 永久监听, 只支持数据变化, 以及本身的创建/删除
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() != null) {
                    String data = new String(nodeCache.getCurrentData().getData());
                    System.out.println("节点路径: " + nodeCache.getCurrentData().getPath() + "  数据" + data);
                } else {
                    System.out.println("节点" + nodeCache.getPath() + "为空");
                }
            }
        });
        Thread.sleep(1000000);
    }

    private static void watcherOnce(CuratorFramework operator, String nodePath) throws Exception {
        // usingWatcher只会出发一次的watcher
        byte[] bytes = operator.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("监听到了" + nodePath + "的" + watchedEvent.getType().name() + "事件");
            }
        }).forPath(nodePath);
    }

    private static void childNode(CuratorFramework operator, String nodePath) throws Exception {
        Stat stat = new Stat();
        byte[] bytes = operator.getData()
                // 可以获得stat
                .storingStatIn(stat).forPath(nodePath);
        System.out.println("获得节点"+ nodePath + "的data: " + new String(bytes));
        if (stat != null) {
            List<String> children = operator.getChildren().forPath(nodePath);
            for (String child : children) {
                System.out.println("child: " + child);
            }
        } else {
            System.out.println("节点"+ nodePath + "不存在");
        }
    }

    private static void deleteNode(CuratorFramework operator, String nodePath) throws Exception {
        Stat stat = operator.checkExists().forPath(nodePath);
        if (stat != null) {
            operator.delete()
                    // 如果删除失败, 会继续删除, 直到成功, 可不加
                    .guaranteed()
                    // 删除子节点, 如果删除的节点有子节点而没有这句话, 则报错
                    .deletingChildrenIfNeeded()
                    // 乐观锁, 实际应用中一定要加, 并处理异常
                    .withVersion(stat.getVersion())
                    .forPath(nodePath);
        } else {
            System.out.println("节点"+ nodePath + "不存在");
        }
    }

    private static void updateNode(CuratorFramework operator, String nodePath, String data) throws Exception {
        Stat stat = operator.checkExists().forPath(nodePath);
        if (stat != null) {
            Stat stat1 = operator.setData()
                    // 乐观锁, 实际应用中一定要加, 并处理异常
                    .withVersion(stat.getVersion())
                    // forPath(String path), 不设data的话, 竟然把ip设为data了????
                    .forPath(nodePath, data.getBytes());
            System.out.println("节点状态 :"+ stat1);
        } else {
            System.out.println("节点"+ nodePath + "不存在");
        }

    }

    private static void createNode(CuratorFramework operator, String nodePath, String data) throws Exception {
        Stat stat = operator.checkExists().forPath(nodePath);
        if (stat == null) {
            operator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(nodePath, data.getBytes());
        } else {
            System.out.println("节点"+ nodePath + "已存在");
        }
    }
}
