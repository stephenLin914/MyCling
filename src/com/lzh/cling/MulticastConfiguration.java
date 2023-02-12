package com.lzh.cling;

import java.net.InetAddress;

public interface MulticastConfiguration {
    InetAddress getGroup();

    int getPort();

    int getMaxDatagramBytes();
}
