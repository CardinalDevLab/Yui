package dev.cardinal.data;

import dev.cardinal.utils.CustomLoginSolver;
import net.mamoe.mirai.utils.LoginSolver;

import java.util.LinkedHashMap;

public class ConfigData {
    //Main config
    public static LinkedHashMap configMap;

    //Module config
    public static LinkedHashMap pterodactylConfig;

    //Specific config
    public static Long qqNumber;
    public static String qqPassword;
    public static Boolean noGuiSupport;
    //Pterodactyl config
    public static Boolean pterodactylEnabled;
    public static String pterodactylApplicationurl;


    public static LoginSolver customLoginSolver = CustomLoginSolver.getLoginSolver();
}
