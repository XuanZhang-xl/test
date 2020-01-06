package xl.test.javabasic.io.network;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * created by zhangxuan9 on 2019/1/25
 */
public class NetWorkInterfaceTest {

    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            System.out.println(networkInterface.getName());
            System.out.println(networkInterface.getDisplayName());
            System.out.println(networkInterface.getIndex());
            System.out.println(networkInterface.isUp());
            System.out.println(networkInterface.isLoopback());
            System.out.println(networkInterface.isPointToPoint());
            System.out.println();


            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                System.out.println(inetAddress.getCanonicalHostName());
                System.out.println(inetAddress.getHostAddress());
                System.out.println(inetAddress.getHostName());
                for (byte address : inetAddress.getAddress()) {
                    System.out.print(address + " ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }

    }

    @Test
    public void inetAddresses() throws IOException {
        InetAddress inetAddress = InetAddress.getByName("224.0.0.0");
        System.out.println("根据域名查ip:  " + inetAddress);
        // 通配地址, 匹配本地系统中的任何地址,  IPv4中通配地址是0.0.0.0
        System.out.println("是否是通配地址: " + inetAddress.isAnyLocalAddress());
        // 回送地址, 就是环回地址, 127.0.0.1
        System.out.println("是否是回送地址: " + inetAddress.isLoopbackAddress());
        // 本地连接地址 ???
        System.out.println("是否是本地连接地址: " + inetAddress.isLinkLocalAddress());
        // 本地网站地址 ???
        System.out.println("是否是本地网站地址: " + inetAddress.isSiteLocalAddress());
        // 组播地址
        System.out.println("是否是组播地址: " + inetAddress.isMulticastAddress());
        // 全球组播地址
        System.out.println("是否是全球组播地址: " + inetAddress.isMCGlobal());
        // 组织范围组播地址
        System.out.println("是否是组织范围组播地址: " + inetAddress.isMCOrgLocal());
        // 网站范围组播地址
        System.out.println("是否是网站范围组播地址: " + inetAddress.isMCSiteLocal());
        // 子网范围组播地址
        System.out.println("是否是子网范围组播地址: " + inetAddress.isMCLinkLocal());
        // 本地接口组播地址
        System.out.println("是否是本地接口组播地址: " + inetAddress.isMCNodeLocal());


        System.out.println("---------------------------");
        inetAddress = InetAddress.getByName("180.101.49.12");
        System.out.println("根据ip查域名:  " + inetAddress.getHostName());
        System.out.println("百度所有ip:");
        InetAddress[] baiduIps = InetAddress.getAllByName("www.baidu.com");
        for (InetAddress ip : baiduIps) {
            System.out.println(ip);
        }
        System.out.println("本机地址: " + InetAddress.getLocalHost());

        System.out.println("百度可达性: " + InetAddress.getByName("www.baidu.com").isReachable(1000));
        System.out.println("谷歌可达性: " + InetAddress.getByName("www.google.com").isReachable(10000));
    }

}
