package com.seosean.showspawntime.modules.features.playerinvisibility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerInvisibility {

    public static boolean isPlayerInvisible(Entity entity){
        boolean flag = !entity.isInvisible();
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
        boolean flag2 = entity != Minecraft.getMinecraft().thePlayer && entity instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < 7.02F;
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
