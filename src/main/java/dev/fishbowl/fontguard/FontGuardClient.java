package dev.fishbowl.fontguard;

import net.fabricmc.api.ClientModInitializer;

public class FontGuardClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FontGuard.init();
    }
}
