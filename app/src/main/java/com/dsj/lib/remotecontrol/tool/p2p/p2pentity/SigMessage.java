package com.dsj.lib.remotecontrol.tool.p2p.p2pentity;


import com.dsj.lib.remotecontrol.tool.p2p.p2pconstant.P2PConstant;

import java.util.Date;


/**
 * Created by xxx on 2015/9/17.
 * 局域网用户之间的upd消息
 */
public class SigMessage {
    /**
     * 发送包的编号 时间即编号
     */
    public String packetNum;
    /**
     * 发送者的昵称
     */
    public String senderAlias;
    /**
     * 发送者的ip地址
     */
    public String senderIp;
    /**
     *
     */
    public int commandNum;
    /**
     *
     */
    public int recipient;
    /**
     * 内容
     */
    public String addition;

    /**
     * 是否接收端
     */
    public boolean isReceiver;

    private static final String receiverTag = "#Tag_Receiver_Str#";

    public SigMessage() {
        this.packetNum = getTime();
    }

    public SigMessage(String protocolString) {
        protocolString = protocolString.trim();
        String[] args = protocolString.split(":");

        packetNum = args[0];
        senderAlias = args[1];
        senderIp = args[2];
        commandNum = Integer.parseInt(args[3]);
        recipient = Integer.parseInt(args[4]);
        if (args.length > 5)
            addition = args[5];
        else
            addition = null;

        for (int i = 6; i < args.length; i++) {
            if (args[i].contains(receiverTag)){
                String receiverStr = args[i].replace(receiverTag, "");
                isReceiver = Boolean.parseBoolean(receiverStr);
            }else {
                addition += (":" + args[i]);
            }
        }
    }

    public String toProtocolString() {
        StringBuffer sb = new StringBuffer();
        sb.append(packetNum);
        sb.append(":");
        sb.append(senderAlias);
        sb.append(":");
        sb.append(senderIp);
        sb.append(":");
        sb.append(commandNum);
        sb.append(":");
        sb.append(recipient);
        sb.append(":");
        sb.append(addition);
        sb.append(":");
        sb.append(receiverTag);
        sb.append(String.valueOf(isReceiver));
        sb.append(P2PConstant.MSG_SEPARATOR);
        return sb.toString();
    }

    public String toProtocolStringReceiver(int cmd) {
        StringBuffer sb = new StringBuffer();
        sb.append(packetNum);
        sb.append(":");
        sb.append(senderAlias);
        sb.append(":");
        sb.append(senderIp);
        sb.append(":");
        sb.append(commandNum);
        sb.append(":");
        sb.append(recipient);
        sb.append(":");
        sb.append(addition);
        if (cmd == P2PConstant.CommandNum.ON_LINE){
            sb.append(":");//上线消息需要加上是否接收端
            sb.append(receiverTag);
            sb.append(String.valueOf(isReceiver));
        }
        sb.append(P2PConstant.MSG_SEPARATOR);

        return sb.toString();
    }

    private String getTime() {
        Date nowDate = new Date();
        return Long.toString(nowDate.getTime());
    }
}
