package com.seosean.showspawntime.modules.features.powerups;

import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.LanguageUtils;
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
            if (LanguageUtils.equals(name, "zombies.game.instakill.upper")) {
                return INSTA_KILL;
            } else if (LanguageUtils.equals(name, "zombies.game.maxammo.upper")) {
                return MAX_AMMO;
            } else if (LanguageUtils.equals(name, "zombies.game.doublegold.upper")) {
                return DOUBLE_GOLD;
            } else if (LanguageUtils.equals(name, "zombies.game.bonusgold.upper")) {
                return BONUS_GOLD;
            } else if (LanguageUtils.equals(name, "zombies.game.carpenter.upper")) {
                return CARPENTER;
            } else if (LanguageUtils.equals(name, "zombies.game.shoppingspree.upper")) {
                return SHOPPING_SPREE;
            }
            return NULL;
        }
    }
}
