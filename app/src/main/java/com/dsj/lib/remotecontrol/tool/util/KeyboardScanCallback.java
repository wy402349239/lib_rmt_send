package com.dsj.lib.remotecontrol.tool.util;

import com.dsj.lib.remotecontrol.tool.p2p.p2pentity.P2PNeighbor;

public interface KeyboardScanCallback {

    void scanDevice(P2PNeighbor neighbor);

    void scanError();

    void removeDevice(P2PNeighbor neighbor);
}
