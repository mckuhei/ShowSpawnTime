package com.seosean.showspawntime.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class NoticeSoundEvent extends Event {

    private final String sound;
    private final float pitch;
    public NoticeSoundEvent(String sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    public String getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }
}
