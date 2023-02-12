package com.lzh.cling;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

public class Utils {
    private static Logger log = Logger.getLogger(Utils.class.getName());
    public static boolean isUsableNetworkInterface(NetworkInterface iface) throws Exception {
        if (!iface.isUp()) {
            log.finer("Skipping network interface (down): " + iface.getDisplayName());
            return false;
        }

        if (Collections.list(iface.getInetAddresses()).size() == 0) {
            log.finer("Skipping network interface without bound IP addresses: " + iface.getDisplayName());
            return false;
        }

        if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vmnet") ||
                (iface.getDisplayName() != null &&  iface.getDisplayName().toLowerCase(Locale.ROOT).contains("vmnet"))) {
            log.finer("Skipping network interface (VMWare): " + iface.getDisplayName());
            return false;
        }

        if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vnic")) {
            log.finer("Skipping network interface (Parallels): " + iface.getDisplayName());
            return false;
        }

        if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vboxnet")) {
            log.finer("Skipping network interface (Virtual Box): " + iface.getDisplayName());
            return false;
        }

        if (iface.getName().toLowerCase(Locale.ROOT).contains("virtual")) {
            log.finer("Skipping network interface (named '*virtual*'): " + iface.getDisplayName());
            return false;
        }

        if (iface.getName().toLowerCase(Locale.ROOT).startsWith("ppp")) {
            log.finer("Skipping network interface (PPP): " + iface.getDisplayName());
            return false;
        }

        if (iface.isLoopback()) {
            log.finer("Skipping network interface (ignoring loopback): " + iface.getDisplayName());
            return false;
        }

        if (!iface.supportsMulticast())
            log.warning("Network interface may not be multicast capable: "  + iface.getDisplayName());

        return true;
    }
}
