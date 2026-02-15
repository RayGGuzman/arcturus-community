package com.eu.habbo.messages.rcon;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.bots.Bot;
import com.eu.habbo.habbohotel.users.DanceType;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserDanceComposer;
import com.google.gson.Gson;

public class BotDance extends RCONMessage<BotDance.JSON> {
    public BotDance() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            Bot bot = room.getBot(json.bot_id);
            if (bot != null) {
                int danceId = Math.max(0, Math.min(json.dance_id, 4));
                DanceType danceType = DanceType.values()[danceId];
                bot.getRoomUnit().setDanceType(danceType);
                room.sendComposer(new RoomUserDanceComposer(bot.getRoomUnit()).compose());
                return;
            }
        }

        this.status = STATUS_ERROR;
        this.message = "Bot not found in any active room";
    }

    static class JSON {
        public int bot_id;
        public int dance_id;
    }
}
