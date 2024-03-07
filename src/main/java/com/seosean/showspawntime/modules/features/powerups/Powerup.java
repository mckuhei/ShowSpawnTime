package com.seosean.showspawntime.modules.features.powerups;

import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Powerup {

    private PowerupType powerupType;
    private EntityArmorStand entityArmorStand;

    private int offsetTime;

    private boolean inc;

    public static Map<EntityArmorStand, Powerup> powerups = new HashMap<>();
    public static List<Powerup> incPowerups = new ArrayList<>();
    public static Powerup deserialize(PowerupType powerupType, EntityArmorStand entityArmorStand) {
        if (!powerups.containsKey(entityArmorStand)) {
            Powerup powerup = new Powerup(powerupType, entityArmorStand);
            incPowerups.removeIf(p -> p.getPowerupType().equals(powerupType));
            powerups.put(entityArmorStand, powerup);
            return powerup;
        }
        return null;
    }
    public static Powerup deserialize(PowerupType powerupType) {
        Powerup powerup = new Powerup(powerupType);
        incPowerups.add(powerup);
        return powerup;
    }

    public Powerup(PowerupType powerupType) {
        this(powerupType, null);
    }

    private DelayedTask delayedTask;
    public Powerup(PowerupType powerupType, EntityArmorStand entityArmorStand) {
        this.powerupType = powerupType;
        this.entityArmorStand = entityArmorStand;
        this.offsetTime = 1200;
        if (entityArmorStand != null) {
            delayedTask = new DelayedTask() {
                @Override
                public void run() {
                    if (offsetTime <= 0) {
                        for (Map.Entry<EntityArmorStand, Powerup> entry : new ArrayList<>(powerups.entrySet())) {
                            if (entry.getValue().equals(Powerup.this)) {
                                powerups.remove(entry.getKey());
                                break;
                            }
                        }
                        this.cancel();
                        return;
                    }
                    offsetTime--;
                }
            };
            delayedTask.runTaskTimer(0, 1);
        } else {
            this.inc = true;
        }
    }

    public void claim() {
        this.offsetTime = 0;
        for (Map.Entry<EntityArmorStand, Powerup> entry : new ArrayList<>(powerups.entrySet())) {
            if (entry.getValue().equals(Powerup.this)) {
                powerups.remove(entry.getKey());
                delayedTask.cancel();
                return;
            }
        }
    }

    public PowerupType getPowerupType() {
        return powerupType;
    }

    public int getOffsetTime(){
        return offsetTime;
    }
    public enum PowerupType {
        NULL(EnumChatFormatting.WHITE, ""),
        INSTA_KILL(EnumChatFormatting.RED, "INSTA KILL"),
        MAX_AMMO(EnumChatFormatting.BLUE, "MAKS AMMO"),
        DOUBLE_GOLD(EnumChatFormatting.GOLD, "DOUBO GOLD"),
        CARPENTER(EnumChatFormatting.DARK_BLUE, "CARPENTER"),
        BONUS_GOLD(EnumChatFormatting.YELLOW, "BONUS GOLD"),
        SHOPPING_SPREE(EnumChatFormatting.DARK_PURPLE, "SHOP SPREE");

        private final String displayName;
        private final EnumChatFormatting enumChatFormatting;
        PowerupType(EnumChatFormatting enumChatFormatting, String displayName) {
            this.enumChatFormatting = enumChatFormatting;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public EnumChatFormatting getEnumChatFormatting() {
            return enumChatFormatting;
        }
        public static PowerupType getPowerupType(String name) {
            switch (name) {
                case "INSTA KILL":
                case "瞬间击杀":
                case "一擊必殺": return INSTA_KILL;
                case "MAX AMMO":
                case "弹药满载":
                case "填滿彈藥": return MAX_AMMO;
                case "DOUBLE GOLD":
                case "双倍金钱":
                case "雙倍金幣": return DOUBLE_GOLD;
                case "SHOPPING SPREE":
                case "购物狂潮":
                case "購物狂潮": return SHOPPING_SPREE;
                case "CARPENTER":
                case "木匠": return CARPENTER;
                case "BONUS GOLD":
                case "金钱加成":
                case "額外金幣": return BONUS_GOLD;
            }
            return NULL;
        }
    }
}
