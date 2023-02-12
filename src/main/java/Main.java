package main.java;

import com.lzh.cling.MulticastReceiverImpl;
import main.java.com.lzh.cling.ClingExecutor;
import main.java.com.lzh.cling.MultiCastConfigurationImpl;
import main.java.com.lzh.cling.MulticastConfiguration;
import main.java.com.lzh.cling.MulticastReceiver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Logger;

public class Main {

    private static Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {

        NetworkInterface localInterface = null;
        Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface iface : Collections.list(interfaceEnumeration)) {
            if (com.lzh.cling.Utils.isUsableNetworkInterface(iface)) {
                log.info("Discovered usable network interface: " + iface.getDisplayName());
                localInterface = iface;
            }
        }

        MulticastReceiver multicastReceiver = new MulticastReceiverImpl(new MultiCastConfigurationImpl());
        multicastReceiver.init(localInterface);
        ClingExecutor executor = new ClingExecutor();
        executor.execute(multicastReceiver);


//        Main main = new Main();
//        List<InetAddress> addressList = main.listAllBroadcastAddresses();
//        for (InetAddress address : addressList) {
//            System.out.println(address.getHostName());
//        }
    }
    List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }
}