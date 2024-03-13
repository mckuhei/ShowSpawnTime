package com.seosean.showspawntime.features.playerinvisibility;

import com.seosean.showspawntime.config.MainConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerInvisibility {
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        if (player != Minecraft.getMinecraft().thePlayer && !player.isPlayerSleeping() && PlayerInvisibility.withinDistance(player) && player.getMaxHealth() < 100) {
            event.setCanceled(MainConfiguration.PlayerInvisible);
        }
    }

    private static boolean withinDistance(EntityPlayer other) {
        return getDistance(other) < 1.4;
    }

    private static double getDistance(EntityPlayer other) {
        return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(other);
    }

    public static boolean isPlayerInvisible(Entity entity){
        boolean flag = !entity.isInvisible();
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
        boolean flag2 = entity != Minecraft.getMinecraft().thePlayer && entity instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < 7.02F && !((EntityPlayer) entity).isPlayerSleeping() && ((EntityPlayer) entity).getMaxHealth() < 100;
        return (flag || flag1) && (flag1 || flag2);
    }

    private static float linearCrease(float number) {
        if (number >= 1.4 && number <= 4){
            return 0.0533F * number + 0.0258F;
        } else if(number > 4 && number <= 6){
            return 0.15F * number - 0.4126F;
        }else if (number > 6 && number <= 7.71){
            return 0.3F * number - 1.313F;
        }else{
            return 0;
        }
    }

    public static float getAlpha(Entity entity){
        return linearCrease(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity));
    }
}
