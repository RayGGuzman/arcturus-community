package com.eu.habbo.messages.rcon;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.bots.Bot;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserEffectComposer;
import com.google.gson.Gson;

public class BotEffect extends RCONMessage<BotEffect.JSON> {
    public BotEffect() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            Bot bot = room.getBot(json.bot_id);
            if (bot != null) {
                int duration = json.duration > 0 ? json.duration : 30;
                int endTimestamp = Emulator.getIntUnixTimestamp() + duration;
                bot.getRoomUnit().setEffectId(json.effect_id, endTimestamp);
                room.sendComposer(new RoomUserEffectComposer(bot.getRoomUnit()).compose());
                return;
            }
        }

        this.status = STATUS_ERROR;
        this.message = "Bot not found in any active room";
    }

    static class JSON {
        public int bot_id;
        public int effect_id;
        public int duration;
    }
}
