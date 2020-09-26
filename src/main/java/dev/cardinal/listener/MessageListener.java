package dev.cardinal.listener;

import dev.cardinal.data.ConfigData;
import dev.cardinal.module.pterodactyl;
import dev.cardinal.utils.MessageUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;

public class MessageListener extends SimpleListenerHost {
    @EventHandler
    public ListeningStatus onMessgae(MessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());

        if (ConfigData.pterodactylEnabled) {
            pterodactyl pterodactyl = new pterodactyl();
            pterodactyl.onMessage(message,event.getSender());
        }

        return ListeningStatus.LISTENING;
    }
}
