package com.eu.habbo.messages.rcon;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.bots.Bot;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomChatMessage;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserTalkComposer;
import com.google.gson.Gson;

public class TalkBot extends RCONMessage<TalkBot.JSON> {
    public TalkBot() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        // Search all active rooms for the bot
        for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            Bot bot = room.getBot(json.bot_id);
            if (bot != null) {
                if (json.bubble_id >= 0) {
                    // Custom bubble: construct chat message with specified bubble style
                    RoomChatMessage chatMessage = new RoomChatMessage(json.message, bot.getRoomUnit(), RoomChatMessageBubbles.getBubble(json.bubble_id));
                    room.botChat(new RoomUserTalkComposer(chatMessage).compose());
                } else {
                    // Default: use bot's permanent bubble via bot.talk()
                    bot.talk(json.message);
                }
                return;
            }
        }

        this.status = STATUS_ERROR;
        this.message = "Bot not found in any active room";
    }

    static class JSON {
        public int bot_id;
        public String message;
        public int bubble_id = -1;
    }
}
