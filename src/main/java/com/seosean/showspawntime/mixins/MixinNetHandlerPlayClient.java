package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.modules.features.powerups.PowerupDetect;
import com.seosean.showspawntime.modules.features.powerups.Powerup;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method = "handleEntityMetadata", at = @At(value = "RETURN"))
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn, CallbackInfo callbackInfo)
    {
        PowerupDetect powerUpDetect = ShowSpawnTime.getInstance().getPowerupDetect();
        int gameTick = GameUtils.getCurrentZombiesTimeTick();
        if (Minecraft.getMinecraft().theWorld.getEntityByID(packetIn.getEntityId()) instanceof EntityArmorStand) {
            for (DataWatcher.WatchableObject watchableObject : packetIn.func_149376_c()) {
                if (watchableObject.getObjectType() == 4 && watchableObject.getDataValueId() == 2) {
                    if (watchableObject.getObject() instanceof String) {
                        String armorStandName = StringUtils.trim((String) watchableObject.getObject());
                        int round = ShowSpawnTime.getSpawnTimes().currentRound;

                        if (round == 0) {
                            return;
                        }
                        Powerup.PowerupType powerupType = Powerup.PowerupType.getPowerupType(armorStandName);

                        if (powerupType.equals(Powerup.PowerupType.NULL)) {
                            return;
                        }

                        Powerup.deserialize(powerupType, (EntityArmorStand) Minecraft.getMinecraft().theWorld.getEntityByID(packetIn.getEntityId()));

                        List<Integer> roundList2 = new ArrayList<>();
                        List<Integer> roundList3 = new ArrayList<>();
                        if (powerupType.equals(Powerup.PowerupType.MAX_AMMO)) {
                            roundList2.addAll(Arrays.asList(PowerupDetect.r2MaxRoundsAA));
                            roundList3.addAll(Arrays.asList(PowerupDetect.r3MaxRoundsAA));
                            if (roundList2.contains(round)) {
                                powerUpDetect.setMaxRound(2);
                            } else if (roundList3.contains(round) && gameTick <= 500) {
                                powerUpDetect.setMaxRound(2);
                            } else if (roundList3.contains(round)) {
                                powerUpDetect.setMaxRound(3);
                            } else if (roundList3.contains(round - 1) && gameTick <= 500) {
                                powerUpDetect.setMaxRound(3);
                            }
                        }
                        else if (powerupType.equals(Powerup.PowerupType.INSTA_KILL)) {
                            roundList2.addAll(Arrays.asList(PowerupDetect.r2InsRoundsAA));
                            roundList3.addAll(Arrays.asList(PowerupDetect.r3InsRoundsAA));
                            if (roundList2.contains(round)) {
                                powerUpDetect.setInstaRound(2);
                            } else if (roundList3.contains(round) && gameTick <= 500) {
                                powerUpDetect.setInstaRound(2);
                            } else if (roundList3.contains(round)) {
                                powerUpDetect.setInstaRound(3);
                            } else if (roundList3.contains(round - 1) && gameTick <= 500) {
                                powerUpDetect.setInstaRound(3);
                            }
                        } else if (powerupType.equals(Powerup.PowerupType.SHOPPING_SPREE)) {
                            roundList2.addAll(Arrays.asList(PowerupDetect.r5SSRoundsAA));
                            roundList3.addAll(Arrays.asList(PowerupDetect.r6SSRoundsAA));
                            List<Integer> roundList4 = new ArrayList<>(Arrays.asList(PowerupDetect.r7SSRoundsAA));
                            if (roundList2.contains(round)) {
                                powerUpDetect.setSSRound(5);
                            } else if (roundList3.contains(round) && gameTick <= 500) {
                                powerUpDetect.setSSRound(5);
                            } else if (roundList3.contains(round)) {
                                powerUpDetect.setSSRound(6);
                            } else if (roundList4.contains(round) && gameTick <= 500) {
                                powerUpDetect.setSSRound(6);
                            } else if (roundList4.contains(round)) {
                                powerUpDetect.setSSRound(7);
                            } else if (roundList4.contains(round - 1) && gameTick <= 500) {
                                powerUpDetect.setSSRound(7);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }
}
