package com.example.demoactivity.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * bus实例，用户传递信息
 */
public class BusHelper {

    /**
     * 配置调用线程，默认为ThreadEnforcer.MAIN 主线程
     * 此处配置的为任意线程
     */
    private static final Bus bus = new Bus(ThreadEnforcer.ANY);

    public static Bus getBus() {
        return bus;
    }
}
