package com.dsj.lib.remotecontrol.tool.p2p.p2pcore;


import android.util.Log;

import com.dsj.lib.remotecontrol.tool.p2p.p2pconstant.P2PConstant;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.SigMessage;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.param.ParamIPMsg;
import com.dsj.lib.remotecontrol.tool.p2p.p2ptimer.OSTimer;
import com.dsj.lib.remotecontrol.tool.p2p.p2ptimer.Timeout;

import java.net.InetAddress;
import java.util.HashMap;


/**
 * Created by xxx on 2015/9/19.
 */
public class MelonManager {

    private static final String tag = MelonManager.class.getSimpleName();

    private P2PManager p2PManager;
    private MelonHandler p2PHandler;
    private MelonCommunicate sigCommunicate;

    private HashMap<String, P2PNeighbor> mNeighbors;

    public MelonManager(P2PManager manager, MelonHandler handler,
                        MelonCommunicate communicate) {
        p2PHandler = handler;
        p2PManager = manager;
        sigCommunicate = communicate;

        mNeighbors = new HashMap<String, P2PNeighbor>();
    }

    public void sendBroadcast() {
        Timeout timeout = new Timeout() {
            @Override
            public void onTimeOut() {
                Log.d(tag, "broadcast 广播 msg");

                sigCommunicate.BroadcastMSG(P2PConstant.CommandNum.ON_LINE,
                        P2PConstant.Recipient.NEIGHBOR);
            }
        };
        //发送两个广播消息
        new OSTimer(p2PHandler, timeout, 200).start();
    }

    public void dispatchMSG(ParamIPMsg ipmsg, boolean isReceiver) {
        switch (ipmsg.peerMSG.commandNum) {
            case P2PConstant.CommandNum.ON_LINE: //收到上线广播
                Log.d(tag, "receive on_line and send on_line_ans message");

                boolean send = !isReceiver && ipmsg.isReceiver();//发送端才会触发添加设备，且只添加接收端 接收端不用，只管收消息
                addNeighbor(ipmsg.peerMSG, ipmsg.peerIAddr, send);

                //回复我上线
                if (isReceiver || ipmsg.peerMSG.isReceiver){
                    //接收端才回复上线，发送端不回复，不可被扫描
                    //收到接收端上线的消息，也回复
                    p2PHandler.send2Neighbor(ipmsg.peerIAddr,
                            P2PConstant.CommandNum.ON_LINE_ANS, null);
                }
                break;
            case P2PConstant.CommandNum.ON_LINE_ANS: //收到对方上线的回复
                Log.d(tag, "received on_line_ans message");
                addNeighbor(ipmsg.peerMSG, ipmsg.peerIAddr, true);
                break;
            case P2PConstant.CommandNum.OFF_LINE:
                delNeighbor(ipmsg.peerIAddr.getHostAddress());
                break;

        }
    }

    public void offLine() {
        Timeout timeOut = new Timeout() {
            @Override
            public void onTimeOut() {
                sigCommunicate.BroadcastMSG(P2PConstant.CommandNum.OFF_LINE,
                        P2PConstant.Recipient.NEIGHBOR);
            }
        };
        timeOut.onTimeOut();
        new OSTimer(p2PHandler, timeOut, 200);
    }

    public HashMap<String, P2PNeighbor> getNeighbors() {
        return mNeighbors;
    }

    /**
     * 局域网设备缓存增加
     *
     * @param sigMessage udp
     * @param address    ip
     * @param sendMsg    是否更新UI
     */
    private void addNeighbor(SigMessage sigMessage, InetAddress address, boolean sendMsg) {
        String ip = address.getHostAddress();
        P2PNeighbor neighbor = mNeighbors.get(ip);
        if (neighbor == null) {
            neighbor = new P2PNeighbor();
            neighbor.alias = sigMessage.senderAlias;
            neighbor.ip = ip;
            neighbor.inetAddress = address;
            mNeighbors.put(ip, neighbor);

            if (sendMsg) {
                p2PManager.getHandler().sendMessage(
                        p2PManager.getHandler().obtainMessage(P2PConstant.UI_MSG.ADD_NEIGHBOR,
                                neighbor));
            }
        }
    }

    private void delNeighbor(String ip) {
        P2PNeighbor neighbor = mNeighbors.get(ip);
        if (neighbor != null) {
            mNeighbors.remove(ip);
            p2PManager.getHandler().sendMessage(
                    p2PManager.getHandler().obtainMessage(P2PConstant.UI_MSG.REMOVE_NEIGHBOR,
                            neighbor));
        }
    }
}
