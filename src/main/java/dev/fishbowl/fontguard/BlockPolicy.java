package dev.fishbowl.fontguard;

import java.util.Set;

public final class BlockPolicy {

    private BlockPolicy() {
    }

    public static boolean shouldBlock(String packId, boolean enabled, Set<String> trustedServers, String serverAddress) {
        if (!enabled) {
            return false;
        }
        if (packId == null || !packId.startsWith("server/")) {
            return false;
        }
        if (serverAddress != null && trustedServers.contains(serverAddress)) {
            return false;
        }
        return true;
    }
}
