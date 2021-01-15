package com.dsj.lib.remotecontrol.tool.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.dsj.lib.remotecontrol.tool.NetworkUtils;
import com.dsj.lib.remotecontrol.tool.p2p.p2pcore.P2PManager;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.lib.remotecontrol.tool.p2p.p2pinterface.Melon_Callback;
import com.dsj.lib.remotecontrol.tool.p2p.p2pinterface.ReceiveFile_Callback;
import com.dsj.lib.remotecontrol.tool.p2p.p2pinterface.SendFile_Callback;
import com.dsj.lib.remotecontrol.tool.sdk.AccessPointManager;

import java.net.UnknownHostException;
import java.util.List;

/**
 * created by wangyu on 2020/7/30 5:08 PM
 * description:
 */
public class KeyBoardSendUtil {

    private static KeyBoardSendUtil mUtil = null;

    public static KeyBoardSendUtil getInstance() {
        if (mUtil == null) {
            synchronized (KeyBoardSendUtil.class) {
                if (mUtil == null) {
                    mUtil = new KeyBoardSendUtil();
                }
            }
        }
        return mUtil;
    }

    private KeyBoardSendUtil() {
    }

    private P2PManager p2PManager;

    public void scanDevices(final KeyboardScanCallback callback, Context context, String deviceName) {
        if (context == null) {
            if (callback != null) {
                callback.scanError();
            }
            return;
        }
        String alias = TextUtils.isEmpty(deviceName) ? Build.DEVICE : deviceName;
        p2PManager = new P2PManager(context);
        P2PNeighbor melonInfo = new P2PNeighbor();
        melonInfo.alias = alias;
        String ip = null;
        try {
            ip = AccessPointManager.getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ip)) {
            ip = NetworkUtils.getLocalIp(context);
        }
        melonInfo.ip = ip;

        p2PManager.start(melonInfo, new Melon_Callback() {
            @Override
            public void Melon_Found(P2PNeighbor melon) {
                if (melon != null && callback != null) {
                    callback.scanDevice(melon);
                }
            }

            @Override
            public void Melon_Removed(P2PNeighbor melon) {
                if (melon != null && callback != null) {
                    callback.removeDevice(melon);
                }
            }
        });
    }

    public void sendMsg(Context context, String msg, P2PNeighbor neighbor, ReceiveFile_Callback callback) {
        if (TextUtils.isEmpty(msg)) {
            if (context != null) {
                Toast.makeText(context, "发送信息为空", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (p2PManager == null) {
            if (context != null) {
                Toast.makeText(context, "请先扫描设备", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (neighbor == null) {
            if (context != null) {
                Toast.makeText(context, "请选择目标设备", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        p2PManager.receiveFile(callback);
        p2PManager.sendStrMsg(neighbor, msg, null);
    }

    public void sendFile(Context context, P2PNeighbor neighbor, List<P2PFileInfo> selectedList, SendFile_Callback callback) {
        if (neighbor == null) {
            Toast.makeText(context, "请选择目标设备", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedList == null || selectedList.isEmpty()) {
            Toast.makeText(context, "请选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        if (p2PManager == null) {
            if (context != null) {
                Toast.makeText(context, "请先扫描设备", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        P2PNeighbor[] neighbors = new P2PNeighbor[]{neighbor};
        P2PFileInfo[] fileArray = new P2PFileInfo[selectedList.size()];
        for (int i = 0; i < selectedList.size(); i++) {
            fileArray[i] = selectedList.get(i);
        }
        p2PManager.sendFile(neighbors, fileArray, callback);
    }

    public void release(List<P2PNeighbor> list) {
        try {
            if (p2PManager != null) {
                if (!list.isEmpty()) {
                    for (P2PNeighbor neighbor : list) {
                        p2PManager.cancelSend(neighbor);
                    }
                }
                p2PManager.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUtil = null;
    }

}