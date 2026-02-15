package com.eu.habbo.messages.rcon;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.bots.Bot;
import com.eu.habbo.habbohotel.rooms.Room;
import com.google.gson.Gson;

public class BotShout extends RCONMessage<BotShout.JSON> {
    public BotShout() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            Bot bot = room.getBot(json.bot_id);
            if (bot != null) {
                bot.shout(json.message);
                return;
            }
        }

        this.status = STATUS_ERROR;
        this.message = "Bot not found in any active room";
    }

    static class JSON {
        public int bot_id;
        public String message;
    }
}
