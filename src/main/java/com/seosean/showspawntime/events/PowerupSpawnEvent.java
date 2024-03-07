package com.seosean.showspawntime.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class PowerupSpawnEvent extends Event {

    private String name;
    public PowerupSpawnEvent(String name) {
        this.name = name;
    }

    public String getPowerupType() {
        return name;
    }

//    public enum PowerupType {
//        INSTA_KILL,
//        MAX_AMMO,
//        DOUBLE_GOLD,
//        CARPENTER,
//        SHOPPING_SPREE,
//        BONUS_GOLD
//
//        private final String[] names;
//        PowerupType(String[] names) {
//            this.names = names;
//        }
//
//    }
}
