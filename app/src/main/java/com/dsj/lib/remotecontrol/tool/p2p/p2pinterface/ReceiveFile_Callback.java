package com.dsj.lib.remotecontrol.tool.p2p.p2pinterface;


import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.param.ParamStrEntity;

/**
 * 我要接受实现的接收回掉
 */
public interface ReceiveFile_Callback {
    public boolean QueryReceiving(P2PNeighbor src, P2PFileInfo files[]);

    public void ReceiverStr(ParamStrEntity entity);//接收消息

    public void ReceiptStr(ParamStrEntity entity);//接收消息回执

    public void BeforeReceiving(P2PNeighbor src, P2PFileInfo files[]);

    public void OnReceiving(P2PFileInfo files);

    public void AfterReceiving();

    public void AbortReceiving(int error, String alias);
}
