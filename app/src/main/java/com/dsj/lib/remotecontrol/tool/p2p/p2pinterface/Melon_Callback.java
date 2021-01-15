package com.dsj.lib.remotecontrol.tool.p2p.p2pinterface;


import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;

/**
 * 局域网好友上线和掉线
 */
public interface Melon_Callback {
    /**
     * 局域网发现好友
     *
     * @param melon
     */
    public void Melon_Found(P2PNeighbor melon);

    /**
     * 局域网好友离开
     *
     * @param melon
     */
    public void Melon_Removed(P2PNeighbor melon);
}
