package com.seosean.showspawntime.events;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NoticeSoundEvent extends Event {

    private final SoundEvent sound;
    private final float pitch;
    public NoticeSoundEvent(SoundEvent sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    public SoundEvent getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }
}
