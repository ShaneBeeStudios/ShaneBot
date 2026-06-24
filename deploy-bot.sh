#!/bin/bash

# Load server config (this file is gitignored — see deploy.env.example)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ ! -f "$SCRIPT_DIR/deploy.env" ]; then
    echo "ERROR: deploy.env not found! Copy deploy.env.example to deploy.env and fill it in."
    exit 1
fi
source "$SCRIPT_DIR/deploy.env"

JAR_LOCAL="$SCRIPT_DIR/build/libs/ShaneBot-2.0.0.jar"
JAR_REMOTE="/home/ubuntu/ShaneBot-2.0.0.jar"

echo "==> Uploading ShaneBot JAR to server..."
rsync -avz -e "ssh -i $SSH_KEY" "$JAR_LOCAL" "$SERVER:$JAR_REMOTE"

if [ $? -ne 0 ]; then
    echo "ERROR: rsync failed!"
    exit 1
fi

echo "==> Restarting ShaneBot via PM2..."
ssh -i "$SSH_KEY" "$SERVER" "pm2 restart shanebot"

if [ $? -ne 0 ]; then
    echo "ERROR: PM2 restart failed!"
    exit 1
fi

echo "==> Done! ShaneBot deployed and restarted."
