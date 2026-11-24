package dev.fishbowl.fontguard.mixin;

import com.mojang.blaze3d.font.GlyphProvider;
import dev.fishbowl.fontguard.FontGuard;
import net.minecraft.client.gui.font.AllMissingGlyphProvider;
import net.minecraft.client.gui.font.providers.TrueTypeGlyphProviderDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrueTypeGlyphProviderDefinition.class)
public class TrueTypeFontGuardMixin {

    @Inject(method = "load", at = @At("HEAD"), cancellable = true, require = 1, expect = 1)
    private void fontguard$blockServerFonts(ResourceManager manager, CallbackInfoReturnable<GlyphProvider> cir) {
        Identifier fontFile = ((TrueTypeGlyphProviderDefinition) (Object) this).location().withPrefix("font/");
        String packId = manager.getResource(fontFile).map(Resource::sourcePackId).orElse(null);
        if (FontGuard.shouldBlock(packId)) {
            FontGuard.onBlocked(packId, fontFile);
            cir.setReturnValue(new AllMissingGlyphProvider());
        }
    }
}
