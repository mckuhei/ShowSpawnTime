package com.seosean.showspawntime.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ZombiesTickEvent extends Event {
    private int tick;
    public ZombiesTickEvent(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }
}
