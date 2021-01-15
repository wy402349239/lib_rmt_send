package com.dsj.lib.remotecontrol.tool.p2p.p2pentity;


import java.io.Serializable;
import java.net.InetAddress;

/**
 * 局域网的用户
 */
public class P2PNeighbor implements Serializable {
    public String alias;
    public String ip;
    public InetAddress inetAddress;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        P2PNeighbor s = (P2PNeighbor) obj;

        if ((s.ip == null))
            return false;

        return (this.ip.equals(s.ip));
    }
}
