# FontGuard

A tiny client-side Fabric mod that stops a malicious Minecraft server from
attacking your computer through its resource pack.

## The problem it blocks

A server can push a resource pack to your client the moment you join. If that
pack contains a crafted TrueType (`.ttf`) font, Minecraft hands the raw font
bytes straight to a native font library (FreeType) to parse. A malformed font
can corrupt native memory during parsing, which on a vulnerable client leads to
a crash or remote code execution, all without you clicking anything. Java-side
protections do not help because the damage happens inside native code.

## What FontGuard does

Every resource in Minecraft knows which pack it came from. A pack a server sends
you is tagged with a pack id that starts with `server/`. FontGuard hooks the
exact spot where a TrueType font is about to be opened and, if that font is
coming from a server pack, it stops it before the native parser ever sees the
bytes. The font simply does not load, your game stays up, and you get a chat
warning naming the server.

Nothing else is touched:

- Vanilla fonts load normally.
- Fonts from other mods load normally.
- Fonts from resource packs you installed yourself load normally.
- Only TrueType fonts pushed by the server you are connected to are blocked.

Most legitimate servers use image-based (bitmap) glyphs for custom text, not
real `.ttf` files, so this almost never affects normal play. If you trust a
specific server and want its fonts, add it to the allow list (below).

## Install

1. Minecraft 26.3 with Fabric Loader and Fabric API.
2. Drop `fontguard-1.0.0.jar` into your `mods` folder next to Fabric API.
3. Launch. You will see `FontGuard active` in the log.

Share the jar with friends the same way, one file, no setup.

## Config

A file is created at `.minecraft/config/fontguard.json`:

```json
{
  "enabled": true,
  "showWarning": true,
  "trustedServers": []
}
```

- `enabled` - turn the protection on or off.
- `showWarning` - show a chat line when a font is blocked.
- `trustedServers` - server addresses whose TrueType fonts you want to allow,
  for example `["play.myfriendserver.net"]`.

## Test it yourself (optional)

`tools/make_testpack.sh` builds a harmless resource pack that contains an
ordinary system font as a `.ttf` provider and zips it. Point a local test
server's `resource-pack` at the zip, join with FontGuard installed, and you will
see the block warning in chat and a `Blocked TrueType font` line in the log. Put
the same zip in your own `resourcepacks` folder instead and it loads fine, which
shows FontGuard only blocks the server copy. This uses a normal font and no
exploit code.

## Versions

Built for Minecraft 26.3. The 26.2 and 1.21.11 ports are a small change away if
you want them.

## License

MIT.
