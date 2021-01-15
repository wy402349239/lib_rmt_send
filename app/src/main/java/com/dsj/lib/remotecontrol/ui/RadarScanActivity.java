package com.dsj.lib.remotecontrol.ui;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.dsj.lib.remotecontrol.R;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.param.ParamStrEntity;
import com.dsj.lib.remotecontrol.tool.p2p.p2pinterface.ReceiveFile_Callback;
import com.dsj.lib.remotecontrol.tool.util.KeyBoardSendUtil;
import com.dsj.lib.remotecontrol.tool.util.KeyboardScanCallback;

import java.util.ArrayList;
import java.util.List;

public class RadarScanActivity extends AppCompatActivity {

    private KeyBoardSendUtil util = null;

    private String alias;//当前设备别名
    private AppCompatTextView btn;
    private List<P2PNeighbor> neighbors = new ArrayList<>();//扫描到的设备
    private P2PNeighbor curNeighbor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radarscan);
        alias = Build.DEVICE;
        TextView radar_scan_name = findViewById(R.id.activity_radar_scan_name);
        radar_scan_name.setText(String.format("本机：%s", alias));

        btn = findViewById(R.id.sendtv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (neighbors == null || util == null){
                    return;
                }
                util.sendMsg(RadarScanActivity.this, btn.toString(), curNeighbor, new ReceiveFile_Callback(){

                    @Override
                    public boolean QueryReceiving(P2PNeighbor src, P2PFileInfo[] files) {
                        return false;
                    }

                    @Override
                    public void ReceiverStr(ParamStrEntity entity) {

                    }

                    @Override
                    public void ReceiptStr(ParamStrEntity entity) {
                        Toast.makeText(RadarScanActivity.this, entity.neighbor.alias + "已收到 : " + entity.getContent(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void BeforeReceiving(P2PNeighbor src, P2PFileInfo[] files) {

                    }

                    @Override
                    public void OnReceiving(P2PFileInfo files) {

                    }

                    @Override
                    public void AfterReceiving() {

                    }

                    @Override
                    public void AbortReceiving(int error, String alias) {

                    }
                });
            }
        });
        initP2P();
    }

    private void initP2P() {
        util = KeyBoardSendUtil.getInstance();
        util.scanDevices(new KeyboardScanCallback() {
            @Override
            public void scanDevice(P2PNeighbor neighbor) {
                if (!neighbors.contains(neighbor)){
                    neighbors.add(neighbor);
                    curNeighbor = neighbor;
                    btn.setText(neighbor.alias + "(" + neighbor.ip + ")");
                }
            }

            @Override
            public void scanError() {

            }

            @Override
            public void removeDevice(P2PNeighbor neighbor) {
                neighbors.remove(neighbor);
            }
        }, RadarScanActivity.this, alias);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (util != null){
            util.release(neighbors);
            util = null;
        }
    }

}
