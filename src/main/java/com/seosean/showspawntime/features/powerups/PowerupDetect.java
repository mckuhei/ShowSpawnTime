package com.seosean.showspawntime.features.powerups;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.dpscounter.DPSCounter;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PowerupDetect {
    public PowerupDetect(){

    }


    public static Integer[] r2MaxRoundsDE = {2, 8, 12, 16, 21, 26};
    public static Integer[] r2MaxRoundsBB = {2, 5, 8, 12, 16, 21, 26};
    public static Integer[] r3MaxRoundsDEBB = {3, 6, 9, 13, 17, 22, 27};

    public static Integer[] r2MaxRoundsTL = {2, 8, 12, 16, 21, 26, 31, 36};
    public static Integer[] r3MaxRoundsTL = {3, 6, 9, 13, 17, 22, 27, 32, 37};

    public static Integer[] r2MaxRoundsAA = {2, 5, 8, 12, 16, 21, 26, 31, 36, 41, 46, 51, 61, 66, 71, 76, 81, 86, 91, 96};
    public static Integer[] r3MaxRoundsAA = {3, 6, 9, 13, 17, 22, 27, 32, 37, 42, 47, 52, 62, 67, 72, 77, 82, 87, 92, 97};

    public static Integer[] r2InsRoundsDE = {2, 8, 11, 14, 17, 23};
    public static Integer[] r2InsRoundsBB = {2, 5, 8, 11, 14, 17, 23};
    public static Integer[] r3InsRoundsDEBB = {3, 6, 9, 12, 18, 21, 24};

    public static Integer[] r2InsRoundsAA = {2, 5, 8, 11, 14, 17, 20, 23};
    public static Integer[] r3InsRoundsAA = {3, 6, 9, 12, 15, 18, 21};

    public static Integer[] r2InsRoundsTL = {2, 8, 11, 14, 17, 20, 23};
    public static Integer[] r3InsRoundsTL = {3, 6, 9, 12, 18, 21, 24};

    public static Integer[] r5SSRoundsAA = {5, 15, 45, 55, 65, 75, 85, 95, 105};
    public static Integer[] r6SSRoundsAA = {6, 16, 26, 36, 46, 66, 76, 86, 96};
    public static Integer[] r7SSRoundsAA = {7, 17, 27, 37, 47, 67, 77, 87, 97};
    public List<Integer> maxRounds = new ArrayList<>();
    public List<Integer> insRounds = new ArrayList<>();
    public List<Integer> ssRounds = new ArrayList<>();

    public void iniPowerupPatterns() {
        Powerup.expiredPowerups = new ArrayList<>();
        Powerup.powerups = new LinkedHashMap<>();
        Powerup.incPowerups = new ArrayList<>();
        insRounds = new ArrayList<>();
        maxRounds = new ArrayList<>();
        ssRounds = new ArrayList<>();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = StringUtils.trim(event.message.getUnformattedText());
        if (event.type != 1 && event.type != 0) {
            return;
        }

        if (message.contains(":")) {
            return;
        }

        if (!MainConfiguration.PowerupAlertToggle) {
            return;
        }

        if (PlayerUtils.isInZombies()){
            if(!LanguageUtils.contains(message, "zombies.game.activated") && !LanguageUtils.contains(message, "zombies.game.activated.2"))  {
                return;
            }
            int round = ShowSpawnTime.getSpawnTimes().currentRound;

            if (round == 0) {
                return;
            }

            int gameTick = ShowSpawnTime.getGameTickHandler().getGameTick();

            Powerup.PowerupType powerupType = Powerup.PowerupType.NULL;
            if (LanguageUtils.contains(message, "zombies.game.instakill.lower")) {
                if (insRounds.isEmpty()) {
                    if (round == 2){
                        this.setInstaRound(2);
                    } else if (round == 3 && gameTick <= 500) {
                        this.setInstaRound(2);
                    } else if (round == 3) {
                        this.setInstaRound(3);
                    } else if (round == 4) {
                        this.setInstaRound(3);
                    }
                }
                powerupType = Powerup.PowerupType.INSTA_KILL;
            } else if (LanguageUtils.contains(message, "zombies.game.maxammo.lower")) {
                if (maxRounds.isEmpty()) {
                    if (round == 2) {
                        this.setMaxRound(2);
                    } else if (round == 3 && gameTick <= 500) {
                        this.setMaxRound(2);
                    } else if (round == 3) {
                        this.setMaxRound(3);
                    } else if (round == 4) {
                        this.setMaxRound(3);
                    }
                }
                powerupType = Powerup.PowerupType.MAX_AMMO;
            } else if (LanguageUtils.contains(message, "zombies.game.shoppingspree.lower")) {
                if (ssRounds.isEmpty()) {
                    if (round == 5) {
                        this.setSSRound(5);
                    } else if (round == 6 && gameTick <= 500) {
                        this.setSSRound(5);
                    } else if (round == 6) {
                        this.setSSRound(6);
                    } else if (round == 7 && gameTick <= 500) {
                        this.setSSRound(6);
                    } else if (round == 7) {
                        this.setSSRound(7);
                    } else if (round == 8) {
                        this.setSSRound(7);
                    }
                }
                powerupType = Powerup.PowerupType.SHOPPING_SPREE;
            } else if (LanguageUtils.contains(message, "zombies.game.carpenter.lower")) {
                powerupType = Powerup.PowerupType.CARPENTER;
            } else if (LanguageUtils.contains(message, "zombies.game.bonusgold.lower")) {
                powerupType = Powerup.PowerupType.BONUS_GOLD;
            } else if (LanguageUtils.contains(message, "zombies.game.doublegold.lower")) {
                powerupType = Powerup.PowerupType.DOUBLE_GOLD;

            }

            DPSCounter.setPowerupOn(powerupType);

            Powerup.PowerupType finalPowerupType = powerupType;


            new DelayedTask() {
                boolean isClaimed = false;
                @Override
                public void run() {
                    for (Map.Entry<EntityArmorStand, Powerup> entry : new ArrayList<>(Powerup.powerups.entrySet())) {
                        if (entry.getValue().getPowerupType().equals(finalPowerupType)) {
                            EntityArmorStand entityArmorStand = entry.getKey();
                            if (entityArmorStand == null || entityArmorStand.isDead) {
                                entry.getValue().claim();
                                isClaimed = true;
                            }
                        }
                    }

                    if (!isClaimed) {
                        Powerup.incPowerups.removeIf(powerup -> powerup.getPowerupType().equals(finalPowerupType));
                    }
                }
            }.runTaskLater(5);
        }
    }

    public void detectArmorstand(String armorStandName, int entityID) {
        PowerupDetect powerUpDetect = ShowSpawnTime.getPowerupDetect();
        int gameTick = GameUtils.getCurrentZombiesTimeTick();

        int round = ShowSpawnTime.getSpawnTimes().currentRound;

        if (round == 0) {
            return;
        }

        Powerup.PowerupType powerupType = Powerup.PowerupType.getPowerupType(armorStandName);

        if (!powerupType.equals(Powerup.PowerupType.NULL)) {
            Powerup powerup = Powerup.deserialize(powerupType, (EntityArmorStand) Minecraft.getMinecraft().theWorld.getEntityByID(entityID));
            if (powerup == null) {
                return;
            }

            List<Integer> roundList2 = new ArrayList<>();
            List<Integer> roundList3 = new ArrayList<>();
            if (powerupType.equals(Powerup.PowerupType.MAX_AMMO)) {
                roundList2.addAll(Arrays.asList(PowerupDetect.r2MaxRoundsAA));
                roundList3.addAll(Arrays.asList(PowerupDetect.r3MaxRoundsAA));
                if (roundList2.contains(round)) {
                    powerUpDetect.setMaxRound(2);
                } else if (roundList3.contains(round) && gameTick <= 1000) {
                    powerUpDetect.setMaxRound(2);
                } else if (roundList3.contains(round)) {
                    powerUpDetect.setMaxRound(3);
                } else if (roundList3.contains(round - 1) && gameTick <= 1000) {
                    powerUpDetect.setMaxRound(3);
                }
            } else if (powerupType.equals(Powerup.PowerupType.INSTA_KILL)) {
                roundList2.addAll(Arrays.asList(PowerupDetect.r2InsRoundsAA));
                roundList3.addAll(Arrays.asList(PowerupDetect.r3InsRoundsAA));
                if (roundList2.contains(round)) {
                    powerUpDetect.setInstaRound(2);
                } else if (roundList3.contains(round) && gameTick <= 1000) {
                    powerUpDetect.setInstaRound(2);
                } else if (roundList3.contains(round)) {
                    powerUpDetect.setInstaRound(3);
                } else if (roundList3.contains(round - 1) && gameTick <= 1000) {
                    powerUpDetect.setInstaRound(3);
                }
            } else if (powerupType.equals(Powerup.PowerupType.SHOPPING_SPREE)) {
                roundList2.addAll(Arrays.asList(PowerupDetect.r5SSRoundsAA));
                roundList3.addAll(Arrays.asList(PowerupDetect.r6SSRoundsAA));
                List<Integer> roundList4 = new ArrayList<>(Arrays.asList(PowerupDetect.r7SSRoundsAA));
                if (roundList2.contains(round)) {
                    powerUpDetect.setSSRound(5);
                } else if (roundList3.contains(round) && gameTick <= 1000) {
                    powerUpDetect.setSSRound(5);
                } else if (roundList3.contains(round)) {
                    powerUpDetect.setSSRound(6);
                } else if (roundList4.contains(round) && gameTick <= 1000) {
                    powerUpDetect.setSSRound(6);
                } else if (roundList4.contains(round)) {
                    powerUpDetect.setSSRound(7);
                } else if (roundList4.contains(round - 1) && gameTick <= 1000) {
                    powerUpDetect.setSSRound(7);
                }
            }
            return;
        }
    }

    public void setInstaRound(int round) {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        switch (map) {
            case DEAD_END:
                insRounds = Arrays.asList(round == 2 ? r2InsRoundsDE : r3InsRoundsDEBB);
                break;
            case THE_LAB:
                insRounds = Arrays.asList(round == 2 ? r2InsRoundsTL : r3InsRoundsTL);
                break;
            case BAD_BLOOD:
                insRounds = Arrays.asList(round == 2 ? r2InsRoundsBB : r3InsRoundsDEBB);
                break;
            case ALIEN_ARCADIUM:
                insRounds = Arrays.asList(round == 2 ? r2InsRoundsAA : r3InsRoundsAA);
                break;
        }
    }

    public void setMaxRound(int round) {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        switch (map) {
            case DEAD_END:
                maxRounds = Arrays.asList(round == 2 ? r2MaxRoundsDE : r3MaxRoundsDEBB);
                break;
            case THE_LAB:
                maxRounds = Arrays.asList(round == 2 ? r2MaxRoundsTL : r3MaxRoundsTL);
                break;
            case BAD_BLOOD:
                maxRounds = Arrays.asList(round == 2 ? r2MaxRoundsBB : r3MaxRoundsDEBB);
                break;
            case ALIEN_ARCADIUM:
                maxRounds = Arrays.asList(round == 2 ? r2MaxRoundsAA : r3MaxRoundsAA);
                break;
        }
    }

    public void setSSRound(int round) {
        switch (round) {
            case 5:
                ssRounds = Arrays.asList(r5SSRoundsAA);
                break;
            case 6:
                ssRounds = Arrays.asList(r6SSRoundsAA);
                break;
            case 7:
                ssRounds = Arrays.asList(r7SSRoundsAA);
                break;
        }
    }

    public boolean isInsRound(int round) {
        return this.insRounds.contains(round);
    }

    public boolean isMaxRound(int round) {
        return this.maxRounds.contains(round);
    }

    public boolean isSSRound(int round) {
        return this.ssRounds.contains(round);
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!event.world.isRemote) {
            return;
        }

        if (!event.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            return;
        }

        this.iniPowerupPatterns();
    }
}
