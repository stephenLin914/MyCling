package com.lzh.cling;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MultiCastConfigurationImpl implements MulticastConfiguration {
    @Override
    public InetAddress getGroup() {
        try {
            return InetAddress.getByName(Constants.IPV4_UPDP_MULTICAST_GROUP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPort() {
        return Constants.UPDP_MULTICAST_PORT;
    }

    @Override
    public int getMaxDatagramBytes() {
        return 640;
    }
}
