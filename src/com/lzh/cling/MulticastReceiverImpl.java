package com.lzh.cling;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.logging.Logger;

public class MulticastReceiverImpl implements MulticastReceiver<MultiCastConfigurationImpl> {
    private static Logger log = Logger.getLogger(MulticastReceiverImpl.class.getName());

    private final MultiCastConfigurationImpl configuration;
    private MulticastSocket socket;
    private InetSocketAddress socketAddress;
    private NetworkInterface localInterface;

    public MulticastReceiverImpl(MultiCastConfigurationImpl configuration) {
        this.configuration = configuration;
    }

    @Override
    public void init(NetworkInterface networkInterface) {
        try {
            localInterface = networkInterface;
            socket = new MulticastSocket(configuration.getPort());
            socket.setReuseAddress(true);
            socket.setReceiveBufferSize(32768); // Keep a backlog of incoming datagrams if we are not fast enough

            socketAddress = new InetSocketAddress(configuration.getGroup(), configuration.getPort());
            socket.joinGroup(socketAddress, localInterface);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.leaveGroup(socketAddress, localInterface);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            socket.close();
        }
    }

    @Override
    public MultiCastConfigurationImpl getConfiguration() {
        return configuration;
    }

    @Override
    public void run() {
        while (true) {
            byte[] buf = new byte[configuration.getMaxDatagramBytes()];
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(datagramPacket);

                datagramPacket.getData();
                System.out.println(new String(datagramPacket.getData(), "UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
