package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.config.MainConfiguration;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.InputStream;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Shadow
    protected abstract InputStream getResourceInputStream(ResourceLocation location);

    @Redirect(method = "readGlyphSizes", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getResourceInputStream(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;"))
    private InputStream readGlyphSizes(FontRenderer instance, ResourceLocation location)
    {
        if (MainConfiguration.CriticalTextFix) {
            return getResourceInputStream(new ResourceLocation("showspawntime", "font/glyph_sizes.bin"));
        } else {
            return getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
        }
    }

}
