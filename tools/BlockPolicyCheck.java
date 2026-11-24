import dev.fishbowl.fontguard.BlockPolicy;

import java.util.Set;

public class BlockPolicyCheck {

    static int failures = 0;

    public static void main(String[] args) {
        Set<String> noTrust = Set.of();
        Set<String> trusted = Set.of("play.friendserver.net");

        expect(true, BlockPolicy.shouldBlock("server/0000000A/abcd", true, noTrust, "play.evil.net"),
                "server pack font is blocked");
        expect(false, BlockPolicy.shouldBlock("vanilla", true, noTrust, "play.evil.net"),
                "vanilla font is allowed");
        expect(false, BlockPolicy.shouldBlock("file/MyPack.zip", true, noTrust, "play.evil.net"),
                "local installed pack font is allowed");
        expect(false, BlockPolicy.shouldBlock("fabric", true, noTrust, null),
                "mod font is allowed");
        expect(false, BlockPolicy.shouldBlock(null, true, noTrust, "play.evil.net"),
                "null pack id is allowed");
        expect(false, BlockPolicy.shouldBlock("server/0000000A/abcd", false, noTrust, "play.evil.net"),
                "disabled mod blocks nothing");
        expect(false, BlockPolicy.shouldBlock("server/0000000A/abcd", true, trusted, "play.friendserver.net"),
                "trusted server font is allowed");
        expect(true, BlockPolicy.shouldBlock("server/0000000A/abcd", true, trusted, "play.other.net"),
                "untrusted server still blocked when a different server is trusted");

        if (failures == 0) {
            System.out.println("ALL PASS");
        } else {
            System.out.println(failures + " FAILED");
            System.exit(1);
        }
    }

    static void expect(boolean want, boolean got, String name) {
        String status = want == got ? "ok  " : "FAIL";
        if (want != got) {
            failures++;
        }
        System.out.println(status + " - " + name);
    }
}
