#!/bin/bash
# Swap DB files for test, then restore originals after test run

# Percorsi
ORIG_DB_DIR="data/dbs"
TEST_DB_DIR="data/test/dbs"
FILES=(bookset.db userset.db lendingset.db)

# Backup e swap
swap_in() {
  for f in "${FILES[@]}"; do
    if [ -f "$ORIG_DB_DIR/$f" ]; then
      mv "$ORIG_DB_DIR/$f" "$ORIG_DB_DIR/$f.bak"
    fi
    cp "$TEST_DB_DIR/$f" "$ORIG_DB_DIR/$f"
  done
}

# Ripristina
swap_out() {
  for f in "${FILES[@]}"; do
    if [ -f "$ORIG_DB_DIR/$f.bak" ]; then
      mv "$ORIG_DB_DIR/$f.bak" "$ORIG_DB_DIR/$f"
    else
      rm -f "$ORIG_DB_DIR/$f"
    fi
  done
}

case "$1" in
  in)
    swap_in
    ;;
  out)
    swap_out
    ;;
  *)
    echo "Usage: $0 in|out"
    exit 1
    ;;
esac
