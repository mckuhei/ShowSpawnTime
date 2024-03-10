package com.seosean.showspawntime.features.dpscounter;

import com.seosean.showspawntime.utils.LanguageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WeaponInfo {
    private String gunName;
    private String sound;
    private float pitch;
    private int gold;
    private int criticalGold;
    private Item item;
    private double damage;
    private double[] ultimatedDamage;
    public WeaponInfo(String gunName, String sound, float pitch, int gold, int criticalGold, Item item, double damage, double... ultimatedDamage) {
        this.gunName = gunName;
        this.sound = sound;
        this.pitch = pitch;
        this.gold = gold;
        this.criticalGold = criticalGold;
        this.item = item;
        this.damage = damage;
        this.ultimatedDamage = ultimatedDamage;
    }

    public String getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public int getCriticalGold() {
        return criticalGold;
    }

    public int getGold() {
        return gold;
    }

    public double getDamage() {
        int level = getUltimateLevel(this);
        if (level == 0) {
            return damage;
        } else {
            if (level - 1 < ultimatedDamage.length) {
                return ultimatedDamage[level - 1];
            }
        }
        return damage;
    }

    private static int getUltimateLevel(WeaponInfo weaponInfo) {
        for (ItemStack itemStack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
            if (itemStack != null) {
                if (itemStack.getItem().equals(weaponInfo.getItem()) && itemStack.getDisplayName().contains(" ")) {
                    String itemName = itemStack.getDisplayName();
                    if (LanguageUtils.contains(itemName, "zombies.game.ultimate")) {
                        if (itemName.contains("I") || itemName.contains("1")) {
                            return 1;
                        } else if (itemName.contains("II") || itemName.contains("2")) {
                            return 2;
                        } else if (itemName.contains("III") || itemName.contains("3")) {
                            return 3;
                        } else if (itemName.contains("IV") || itemName.contains("4")) {
                            return 4;
                        } else if (itemName.contains("V") || itemName.contains("5")) {
                            return 5;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public Item getItem() {
        return item;
    }

    public String getGunName() {
        return gunName;
    }

    public boolean equals(WeaponInfo weaponInfo) {
        return this.item.equals(weaponInfo.getItem());
    }
}
