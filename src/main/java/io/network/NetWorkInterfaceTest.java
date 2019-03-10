package io.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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


}
