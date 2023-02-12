package com.lzh.cling;

import java.net.NetworkInterface;

public interface MulticastReceiver<C extends MulticastConfiguration> extends Runnable {

    void init(NetworkInterface localInterface);

    void stop();

    C getConfiguration();
}
