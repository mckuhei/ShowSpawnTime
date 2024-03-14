package com.seosean.showspawntime.features.dpscounter;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.powerups.Powerup;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class DPSCounter {

    public DPSCounter () {
        setDPS();
    }
    private static final List<WeaponInfo> weaponInfos = new ArrayList<>();

    private static final List<String> soundList = new ArrayList<>();
    static {
        weaponInfos.add(new WeaponInfo("Pistol", "mob.irongolem.hit", 2.0F, 10, 15, Item.getByNameOrId("minecraft:wooden_hoe"), 6, 6));
        weaponInfos.add(new WeaponInfo("Shotgun", "random.explode", 2.0F, 8, 12, Item.getByNameOrId("minecraft:iron_hoe"), 4.5, 4.5));
        weaponInfos.add(new WeaponInfo("Sniper", "fireworks.blast", 0.5F, 30, 45, Item.getByNameOrId("minecraft:wooden_shovel"), 30, 40));
        weaponInfos.add(new WeaponInfo("Rifle", "fireworks.largeBlast", 2.0F, 7, 10, Item.getByNameOrId("minecraft:stone_hoe"), 6, 8));
        weaponInfos.add(new WeaponInfo("ZombieZapper", "fire.ignite", 0.5F, 15, 20, Item.getByNameOrId("minecraft:diamond_pickaxe"), 12, 18));
        weaponInfos.add(new WeaponInfo("ElderGun", "ambient.weather.thunder", 2.0F, 20, 30, Item.getByNameOrId("minecraft:shears"), 15, 20));
        weaponInfos.add(new WeaponInfo("FlameThrower", "fire.fire", 2.0F, 4, 6, Item.getByNameOrId("minecraft:golden_hoe"), 2, 2));
        weaponInfos.add(new WeaponInfo("BlowDart", "random.bow", 0.5F, 20, 30, Item.getByNameOrId("minecraft:iron_shovel"), 10, 10));
        weaponInfos.add(new WeaponInfo("ZombieSoaker", "mob.slime.attack", 2.0F, 5, 10, Item.getByNameOrId("minecraft:diamond_hoe"), 5, 8));
        weaponInfos.add(new WeaponInfo("RainbowRifle", "fireworks.largeBlast", 2.0F, 5, 7, Item.getByNameOrId("minecraft:golden_shovel"), 5, 6, 6.5, 7));
        weaponInfos.add(new WeaponInfo("DoubleBarrelShotgun", "fireworks.largeBlast", 1.0F, 8, 12, Item.getByNameOrId("minecraft:flint_and_steel"), 7, 7, 8, 8));
        weaponInfos.add(new WeaponInfo("GoldDigger", "dig.stone", 2.0F, 10, 15, Item.getByNameOrId("minecraft:golden_pickaxe"), 6, 8, 10, 12, 15, 20));

        for (WeaponInfo weaponInfo : weaponInfos) {
            soundList.add(weaponInfo.getSound());
        }

    }

    public static int instaKillOn = 0;
    public static int doubleGoldOn = 0;
    public static void setPowerupOn(Powerup.PowerupType powerupType) {
        switch (powerupType) {
            case INSTA_KILL: instaKillOn = 10; break;
            case DOUBLE_GOLD: doubleGoldOn = 30; break;
        }
    }
    public static List<WeaponInfo> probableWeaponInfos = new ArrayList<>();

    public static WeaponInfo cacheReadyWeapon;
    public static WeaponInfo readyWeapon;

    public static void detectWeaponBehavior(String sound, float pitch) {
        double epsilon = 0.1;
        if (sound.equals("random.successful_hit") && (Math.abs(1.5F - pitch) < epsilon || Math.abs(2.0F - pitch) < epsilon)) {
            if (readyWeapon == null && cacheReadyWeapon != null) {
                readyWeapon = cacheReadyWeapon;
            }
            if (readyWeapon != null) {
                damage += readyWeapon.getDamage();
                cacheReadyWeapon = readyWeapon;
            }
            readyWeapon = null;
        } else if (soundList.contains(sound)) {
            List<WeaponInfo> probableWeaponInfos = new ArrayList<>();
            for (WeaponInfo weaponInfo : weaponInfos) {
                if (weaponInfo.getSound().equalsIgnoreCase(sound) && Math.abs(weaponInfo.getPitch() - pitch) < epsilon) {
                    probableWeaponInfos.add(weaponInfo);
                }
            }
            if (!probableWeaponInfos.isEmpty()) {
                DPSCounter.probableWeaponInfos = new ArrayList<>(probableWeaponInfos);
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!MainConfiguration.DPSCounterToggle) {
            return;
        }

        if (event.type != 1 && event.type != 0) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }

        String messsage = StringUtils.trim(event.message.getUnformattedText());
        if (messsage.contains(":")) {
            return;
        }

        if (!messsage.contains("+") || !LanguageUtils.contains(messsage, "zombies.game.gold")) {
            return;
        }

        boolean isCritical = LanguageUtils.contains(messsage, "zombies.game.criticalhit");

        int gold = doubleGoldOn > 0 ? StringUtils.getNumberInString(messsage) / 2 : StringUtils.getNumberInString(messsage);

        List<WeaponInfo> probableWeaponInfos = new ArrayList<>();

        for (WeaponInfo weaponInfo : weaponInfos) {
            if (isCritical ? weaponInfo.getCriticalGold() == gold : weaponInfo.getGold() == gold) {
                probableWeaponInfos.add(weaponInfo);
            }
        }
        l:
        for (WeaponInfo weaponInfo : probableWeaponInfos) {
            for (WeaponInfo weaponInfo1 : DPSCounter.probableWeaponInfos) {
                if (weaponInfo.equals(weaponInfo1)) {
                    readyWeapon = weaponInfo1;
                    break l;
                }
            }
        }

        DPSCounter.probableWeaponInfos = new ArrayList<>();
    }

    private static double damage;
    public static double DPS;
    private static void setDPS() {
        new DelayedTask() {
            @Override
            public void run() {
                if (instaKillOn > 0) {
                    instaKillOn --;
                }
                if (doubleGoldOn > 0) {
                    doubleGoldOn --;
                }
                DPS = damage;
                damage = 0;
            }
        }.runTaskTimer(0, 20);
    }
}
