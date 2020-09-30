package dev.cardinal.listener;

import dev.cardinal.data.ConfigData;
import dev.cardinal.module.generalCommand;
import dev.cardinal.module.pterodactyl;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;

public class MessageListener extends SimpleListenerHost {
    @EventHandler
    public ListeningStatus onMessage(MessageEvent event) {
        generalCommand generalCommand = new generalCommand();
        generalCommand.onMessage(event);
        if (ConfigData.pterodactylEnabled) {
            pterodactyl pterodactyl = new pterodactyl();
            pterodactyl.onMessage(event);
        }
        return ListeningStatus.LISTENING;
    }
}
