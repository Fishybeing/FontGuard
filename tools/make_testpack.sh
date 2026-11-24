#!/usr/bin/env bash
set -euo pipefail

here="$(cd "$(dirname "$0")/.." && pwd)"
pack="$here/testpack"
font_src=""
for f in /c/Windows/Fonts/arial.ttf /c/Windows/Fonts/segoeui.ttf /c/Windows/Fonts/consola.ttf; do
    if [ -f "$f" ]; then font_src="$f"; break; fi
done
if [ -z "$font_src" ]; then
    echo "No system .ttf found to copy. Put any .ttf at $pack/assets/fontguard/font/test.ttf yourself."
    exit 1
fi

rm -rf "$pack"
mkdir -p "$pack/assets/fontguard/font"
cp "$font_src" "$pack/assets/fontguard/font/test.ttf"

cat > "$pack/pack.mcmeta" <<'EOF'
{
  "pack": {
    "pack_format": 97,
    "description": "FontGuard test pack"
  }
}
EOF

cat > "$pack/assets/fontguard/font/test.json" <<'EOF'
{
  "providers": [
    {
      "type": "ttf",
      "file": "fontguard:test.ttf",
      "size": 11.0,
      "oversample": 2.0
    }
  ]
}
EOF

zip_out="$here/testpack.zip"
rm -f "$zip_out"

jar_bin=""
for j in "$(command -v jar 2>/dev/null || true)" /c/Users/matt/.jdks/*/bin/jar; do
    if [ -x "$j" ]; then jar_bin="$j"; break; fi
done
if [ -z "$jar_bin" ]; then
    echo "No jar tool found to build the zip."
    exit 1
fi
"$jar_bin" -cMf "$zip_out" -C "$pack" .

sha1=$(sha1sum "$zip_out" | cut -d' ' -f1)

echo "Built $zip_out"
echo "sha1: $sha1"
echo
echo "To test as a server pack, in server.properties set:"
echo "  resource-pack=http://localhost:8000/testpack.zip"
echo "  resource-pack-sha1=$sha1"
echo "  require-resource-pack=true"
echo "Serve it from this folder with:  python -m http.server 8000"
echo "Join with FontGuard installed: the font load is blocked and you get a chat warning."
echo "Put testpack.zip in your resourcepacks folder instead and it loads normally."
