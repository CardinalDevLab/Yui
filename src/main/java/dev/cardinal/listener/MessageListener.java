package dev.cardinal.listener;

import dev.cardinal.data.ConfigData;
import dev.cardinal.module.pterodactyl;
import dev.cardinal.utils.MessageUtils;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;

public class MessageListener extends SimpleListenerHost {
    @EventHandler
    public ListeningStatus onTempMessgae(TempMessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());

        if (ConfigData.pterodactylEnabled) {
            pterodactyl pterodactyl = new pterodactyl();
            pterodactyl.onDirectMessage(message,event.getSender());
        }
        return ListeningStatus.LISTENING;
    }

    //This should be the same as onTempMessage
    @EventHandler
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());

        if (ConfigData.pterodactylEnabled) {
            pterodactyl pterodactyl = new pterodactyl();
            pterodactyl.onDirectMessage(message,event.getSender());
        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler
    public ListeningStatus onGroupMessage(GroupMessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());

        if (ConfigData.pterodactylEnabled) {
            pterodactyl pterodactyl = new pterodactyl();
            pterodactyl.onGroupMessage(message,event.getGroup(),event.getSender());
        }
        return ListeningStatus.LISTENING;
    }
}
