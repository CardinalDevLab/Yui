package dev.cardinal.module;

import dev.cardinal.data.ConfigData;
import dev.cardinal.utils.MessageUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;

public class generalCommand {
    public void onMessage(final MessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());

        System.out.println(message);

        Contact contact = event.getSubject();
        if (message.length() >= 4 && message.substring(0,4).equals("命令列表")) {
            String messageCache = "";
            if (ConfigData.pterodactylEnabled) {
                messageCache += "1. 翼龙绑定: 绑定翼龙面板的服务器 \n"
                        + "翼龙绑定 服务器标识符 API密钥 \n"
                        + "2. 翼龙默认: 设置后可以省略服务器标识符执行多数命令 \n"
                        + "翼龙默认 服务器标识符 \n"
                        + "3. 翼龙状态: 查看服务器的状态(绑定了默认服务器后可以省略服务器标识符) \n"
                        + "翼龙状态 服务器标识符 \n"
                        + "4. 翼龙启动: 启动服务器(绑定了默认服务器后可以省略服务器标识符) \n"
                        + "翼龙启动 服务器标识符 \n"
                        + "5. 翼龙关机: 关闭服务器(绑定了默认服务器后可以省略服务器标识符) \n"
                        + "翼龙关机 服务器标识符 \n"
                        + "6. 翼龙强停: 强制停止服务器(绑定了默认服务器后可以省略服务器标识符) \n"
                        + "翼龙强停 服务器标识符 \n"
                        + "7. 翼龙重启: 重新启动服务器(绑定了默认服务器后可以省略服务器标识符) \n"
                        + "翼龙重启 服务器标识符 \n"
                        + "8. 翼龙列表: 展示绑定的所有服务器, 方便查询服务器标识符 \n"
                        + "翼龙列表 \n"
                        + "9. 翼龙执行: 为特定服务器执行指令 \n"
                        + "翼龙执行 服务器标识符 指令";
            }
            contact.sendMessage(messageCache);
            return;
        }

        if (message.length() >= 2 && message.substring(0,2).equals("关于")) {
            String messageCache = "";
            messageCache += "Yui 结衣 \n"
                    + "发送  命令列表  获取全部命令 \n"
                    + "模块化多功能机器人 \n"
                    + "致力于实现尽可能多的功能 \n"
                    + "基于AGPL 3协议开源 \n"
                    + "Github: https://github.com/CardinalDevLab/Yui \n"
                    + "欢迎PR";
            contact.sendMessage(messageCache);
            return;
        }

        if (event.getMessage().stream().anyMatch(it -> it instanceof At && ((At) it).getTarget() == event.getBot().getId())) {
            String messageCache = "";
            messageCache += "Yui 结衣 \n"
                    + "发送  命令列表  获取全部命令 \n"
                    + "模块化多功能机器人 \n"
                    + "致力于实现尽可能多的功能 \n"
                    + "基于AGPL 3协议开源 \n"
                    + "Github: https://github.com/CardinalDevLab/Yui \n"
                    + "欢迎PR";
            contact.sendMessage(messageCache);
            return;
        }
    }
}
