package dev.cardinal.utils;

import net.mamoe.mirai.message.data.MessageChain;

public class MessageUtils {
    public static String messageToString(MessageChain chain) {
        return chain.contentToString();
    }
}
