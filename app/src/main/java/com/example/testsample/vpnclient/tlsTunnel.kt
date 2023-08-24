package com.example.testsample.vpnclient

object tlsTunnel {
    external fun tunnelStart(uuid: String, make_config: String): Int
    external fun tunnelStop(): Int
    external fun setTunnelFd(fd:Int);
    external fun getVpnAddress():String;
    init {
        System.loadLibrary("tlsTunnelWrapper")
    }
}
