package dev.fishbowl.fontguard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class FontGuard {

    public static final String MOD_ID = "fontguard";
    public static final Logger LOGGER = LoggerFactory.getLogger("FontGuard");

    private static FontGuardConfig config = new FontGuardConfig();
    private static final Set<String> warnedPacks = Collections.synchronizedSet(new HashSet<>());

    private FontGuard() {
    }

    public static void init() {
        config = FontGuardConfig.load();
        LOGGER.info("FontGuard active - blocking TrueType fonts from server resource packs (enabled={})", config.enabled);
    }

    public static FontGuardConfig config() {
        return config;
    }

    public static boolean shouldBlock(String packId) {
        return BlockPolicy.shouldBlock(packId, config.enabled, new HashSet<>(config.trustedServers), currentServerAddress());
    }

    public static void onBlocked(String packId, Identifier fontFile) {
        LOGGER.warn("Blocked TrueType font {} coming from server pack {}", fontFile, packId);
        if (!config.showWarning || !warnedPacks.add(packId)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        String address = currentServerAddress();
        String where = address == null ? "this server" : address;
        mc.execute(() -> {
            if (mc.player != null) {
                mc.player.sendSystemMessage(Component.literal(
                        "[FontGuard] Blocked a TrueType font from " + where + "'s resource pack (known crash/RCE vector)."));
            }
        });
    }

    private static String currentServerAddress() {
        ServerData server = Minecraft.getInstance().getCurrentServer();
        return server == null ? null : server.ip;
    }
}
