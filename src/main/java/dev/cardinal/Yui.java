package dev.cardinal;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.entities.PteroAPI;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dev.cardinal.data.ConfigData;
import dev.cardinal.data.UserData;
import dev.cardinal.listener.MessageListener;
import dev.cardinal.utils.FileUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Yui {
    public static Yaml yaml = new Yaml();
    public static Bot bot;
    public static void main(String[] args) throws Exception {
        final FileUtils fileUtils = new FileUtils();
        String configPath;
        String userDataPath;

        //File actions
        if (!new File(fileUtils.getBaseFolder()+"/config.yml").exists() || !new File(fileUtils.getBaseFolder()+ "/userdata.yml").exists()) {
            configPath = fileUtils.ExportResource("/config.yml");
            userDataPath = fileUtils.ExportResource("/userdata.yml");
        } else {
            configPath = fileUtils.getBaseFolder() + "/config.yml";
            userDataPath = fileUtils.getBaseFolder() + "/userdata.yml";
        }

        //Config Actions
        ConfigData.configMap = yaml.load(new FileInputStream(new File(configPath)));

        ConfigData.pterodactylConfig = (LinkedHashMap) ConfigData.configMap.get("pterodactyl");

        ConfigData.qqNumber = Long.parseLong(String.valueOf(ConfigData.configMap.get("qq_number")));
        ConfigData.qqPassword = String.valueOf(ConfigData.configMap.get("qq_password"));
        ConfigData.noGuiSupport = Boolean.parseBoolean(String.valueOf(ConfigData.configMap.get("noGuiSupport")));
        ConfigData.pterodactylEnabled = Boolean.parseBoolean(String.valueOf(ConfigData.pterodactylConfig.get("enabled")));
        ConfigData.pterodactylApplicationurl = String.valueOf(ConfigData.pterodactylConfig.get("applicationurl"));

        //UserData Actions
        UserData.userData = yaml.load(new FileInputStream(new File(userDataPath)));


//        Iterator<Map.Entry<Long, HashMap<String, String>>> entries = UserData.userData.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<Long, HashMap<String,String>> entry = entries.next();
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }

        fileUtils.scheduleYamlDump(configPath,userDataPath);

        if (ConfigData.noGuiSupport) {
            bot = BotFactoryJvm.newBot(ConfigData.qqNumber, ConfigData.qqPassword, new BotConfiguration() {
                {
                    fileBasedDeviceInfo(fileUtils.getBaseFolder()+"/deviceInfo.json");
                    setLoginSolver(ConfigData.customLoginSolver);
                }
            });
        } else {
            bot = BotFactoryJvm.newBot(ConfigData.qqNumber, ConfigData.qqPassword, new BotConfiguration() {
                {
                    fileBasedDeviceInfo(fileUtils.getBaseFolder() + "/deviceInfo.json");
                }
            });
        }
        bot.login();

        Events.registerEvents(bot,new MessageListener());

    }
}
