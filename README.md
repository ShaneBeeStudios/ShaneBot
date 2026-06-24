# ShaneBot
A private Discord moderation bot for the ShaneBee Support server.

## Features
- **Scam Spam Detection** — automatically detects users posting images across 3+ channels within 10 seconds (a common scam pattern). Offenders are timed out for 24 hours and an alert with the spam image is posted to the mod channel for admin review.
- **Mention Protection** — deletes messages that tag the server owner. Repeated offenders are timed out automatically.
- **Slash Commands** — moderation commands for muting, purging messages, slowmode, and more (admin only).

## Requirements
- Java 21+
- A Discord bot token with all Gateway Intents enabled

## Building
Run the `shadowJar` Gradle task in IntelliJ, or via the terminal:
```bash
./gradlew shadowJar
```
Output: `build/libs/ShaneBot-2.0.0.jar`

## Running
```bash
java -Xmx256m -jar ShaneBot-2.0.0.jar \
  --token=YOUR_BOT_TOKEN \
  --server=YOUR_SERVER_ID \
  --bot-c=YOUR_BOT_CHANNEL_ID \
  --admin-r=YOUR_ADMIN_ROLE_ID
```

| Argument | Description |
|----------|-------------|
| `--token` | Discord bot token |
| `--server` | Guild/server ID the bot is authorized to run on |
| `--bot-c` | Channel ID where the bot posts mod alerts (bans, mutes, scam detections) |
| `--admin-r` | Role ID for the admin role |

## Deployment
The bot runs on an Oracle Cloud Ubuntu server managed via PM2.

### First-time setup
Copy the example deploy config and fill in your server details:
```bash
cp deploy.env.example deploy.env
```
Edit `deploy.env` with your server IP and SSH key path. This file is gitignored and should never be committed.

### Deploying updates
1. Build the JAR (`shadowJar` Gradle task in IntelliJ)
2. Run the **Deploy ShaneBot** External Tool in IntelliJ (**Tools → External Tools → Deploy ShaneBot**)

This will rsync the JAR to the server and restart the PM2 process automatically.

### Useful server commands
```bash
pm2 logs shanebot --lines 50    # view recent logs
pm2 restart shanebot            # restart the bot
pm2 stop shanebot               # stop the bot
pm2 status                      # check if bot is running
```
