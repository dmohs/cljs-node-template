#!/bin/bash
IFS=$'\n\t'
set -euxo pipefail

BASE="$1"
SRC="$2"
DST="$3"

if [[ ! -e "$BASE" ]]; then
  >&2 echo "Base path does not exist: $BASE"
  exit 1
fi

SRC_PATH="${SRC//.//}"
SRC_PATH="${SRC_PATH//-/_}"
if [[ -e "$BASE/$SRC_PATH.clj" ]]; then
  EXT='clj'
elif [[ -e "$BASE/$SRC_PATH.cljs" ]]; then
  EXT='cljs'
else
  >&2 echo "Source path does not exist: ($SRC_PATH.clj $SRC_PATH.cljs)"
  exit 1
fi

DST_PATH="${DST//.//}"
DST_PATH="${DST_PATH//-/_}"

find "$BASE" -type f -exec sed -i '' "s/$SRC/$DST/g" '{}' ';'
mkdir -p "$(dirname $BASE/$DST_PATH)"
mv "$BASE/$SRC_PATH.$EXT" "$BASE/$DST_PATH.$EXT"
