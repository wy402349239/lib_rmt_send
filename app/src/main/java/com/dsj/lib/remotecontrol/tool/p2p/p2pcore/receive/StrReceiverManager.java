package com.dsj.lib.remotecontrol.tool.p2p.p2pcore.receive;

import com.dsj.lib.remotecontrol.tool.p2p.p2pconstant.P2PConstant;
import com.dsj.lib.remotecontrol.tool.p2p.p2pcore.MelonHandler;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.param.ParamIPMsg;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.param.ParamStrEntity;

/**
 * created by wangyu on 2020/7/29 6:04 PM
 * description:
 */
public class StrReceiverManager extends ReceiveManager{

    public StrReceiverManager(MelonHandler handler) {
        super(handler);
    }

    @Override
    protected void invoke(ParamIPMsg paramIPMsg) {
        String peerIP = paramIPMsg.peerIAddr.getHostAddress();
        P2PFileInfo[] files = new P2PFileInfo[0];
        P2PNeighbor neighbor;
        try {
            neighbor = p2PHandler.getNeighborManager().getNeighbors().get(peerIP);
        }catch (Exception e){
            neighbor = null;
        }
        String content = paramIPMsg.peerMSG.addition;
        ParamStrEntity entity = new ParamStrEntity(content, neighbor);
        receiver = new Receiver(this, neighbor, files);

        if (p2PHandler != null)
            p2PHandler.send2UI(P2PConstant.CommandNum.SEND_STR_REQ, entity);
    }

    @Override
    protected void invokeStr(ParamIPMsg paramIPMsg, int cmd) {
        super.invokeStr(paramIPMsg, cmd);
        String peerIP = paramIPMsg.peerIAddr.getHostAddress();
        P2PFileInfo[] files = new P2PFileInfo[0];
        P2PNeighbor neighbor;
        try {
            neighbor = p2PHandler.getNeighborManager().getNeighbors().get(peerIP);
        }catch (Exception e){
            neighbor = null;
        }
        String content = paramIPMsg.peerMSG.addition;
        ParamStrEntity entity = new ParamStrEntity(content, neighbor);
        receiver = new Receiver(this, neighbor, files);
        // cmd == P2PConstant.CommandNum.Receipt_STR_REQ || P2PConstant.CommandNum.SEND_STR_REQ
        if (p2PHandler != null)
            p2PHandler.send2UI(cmd, entity);
    }

}