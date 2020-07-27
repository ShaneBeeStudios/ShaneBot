package com.shanebeestudios.bot.task;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class MuteTimer extends TimerTask {

    private final BotHandler botHandler;

    public MuteTimer(BotHandler botHandler, int seconds) {
        this.botHandler = botHandler;
        Logger.info("Starting timer...");
        Timer timer = new Timer("MuteTimer");
        timer.schedule(this, 3000, seconds * 1000);
    }

    @Override
    public void run() {
        tick();
    }

    private void tick() {
        botHandler.getMuteData().checkMutes();
    }

}
