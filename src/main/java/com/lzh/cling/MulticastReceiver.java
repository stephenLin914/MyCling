package main.java.com.lzh.cling;

import java.net.InetAddress;
import java.net.NetworkInterface;

public interface MulticastReceiver<C extends MulticastConfiguration> extends Runnable {

    void init(NetworkInterface localInterface);

    void stop();

    C getConfiguration();
}
