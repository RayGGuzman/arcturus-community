package com.eu.habbo.messages.rcon;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.bots.Bot;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUserAction;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserActionComposer;
import com.google.gson.Gson;

public class BotAction extends RCONMessage<BotAction.JSON> {
    public BotAction() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            Bot bot = room.getBot(json.bot_id);
            if (bot != null) {
                int actionId = Math.max(0, Math.min(json.action_id, 7));
                RoomUserAction action = RoomUserAction.fromValue(actionId);
                room.sendComposer(new RoomUserActionComposer(bot.getRoomUnit(), action).compose());
                return;
            }
        }

        this.status = STATUS_ERROR;
        this.message = "Bot not found in any active room";
    }

    static class JSON {
        public int bot_id;
        public int action_id;
    }
}
