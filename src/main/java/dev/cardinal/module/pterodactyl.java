package dev.cardinal.module;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.entities.PteroAPI;
import dev.cardinal.data.ConfigData;
import dev.cardinal.data.UserData;
import net.mamoe.mirai.contact.User;

import java.util.HashMap;

public class pterodactyl {
    public void onMessage(String message, User user) {
        if (message.length() < 4 || !message.substring(0,2).equals("翼龙")){
            return;
        }
        String command = message.substring(0,4);
        String args = "";
        if (message.length() > 4) {
            args = message.substring(5);
        }
        PteroBuilder pteroBuilder = new PteroBuilder().setApplicationUrl(ConfigData.pterodactylApplicationurl);

        if (command.equals("翼龙绑定")) {
            String serverId = args.substring(0,8);
            String token = args.substring(9);
            PteroClient api = pteroBuilder.setToken(token).build().asClient();
            if (api.retrieveServerByIdentifier(serverId).execute().isServerOwner()) {
                HashMap tempHashMap = null;
                if (UserData.userData.get(user.getId()) != null) {
                    tempHashMap = UserData.userData.get(user.getId());
                } else {
                    tempHashMap = new HashMap();
                }
                tempHashMap.put(serverId,token);
                UserData.userData.put(user.getId(),tempHashMap);
                user.sendMessage("绑定成功");
            }
        }

        if (command.equals("翼龙状态")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    user.sendMessage("未设置默认服务器");
                } else {
                    user.sendMessage("正在查询...");
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    Utilization utilization = api.retrieveUtilization(api.retrieveServerByIdentifier(serverId).execute()).execute();
                    String tempMessageCache = "服务器: " + api.retrieveServerByIdentifier(serverId).execute().getName() + "\n当前状态: " + utilization.getState() + "\nCPU: " + utilization.getCurrentCPU() + "/" + utilization.getMaxCPU()
                            + "\n内存: " + utilization.getCurrentMemory() + "/" + utilization.getMaxMemory()
                            + "\n磁盘: " + utilization.getCurrentDisk() +  "/" + utilization.getMaxDisk();
                    user.sendMessage(tempMessageCache);
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0, 8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    Utilization utilization = api.retrieveUtilization(api.retrieveServerByIdentifier(serverId).execute()).execute();
                    String tempMessageCache = "服务器: " + api.retrieveServerByIdentifier(serverId).execute().getName() + "\n当前状态: " + utilization.getState() + "\nCPU: " + utilization.getCurrentCPU() + "/" + utilization.getMaxCPU()
                            + "\n内存: " + utilization.getCurrentMemory() + "/" + utilization.getMaxMemory()
                            + "\n磁盘: " + utilization.getCurrentDisk() +  "/" + utilization.getMaxDisk();
                    user.sendMessage(tempMessageCache);
                } else {
                    user.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙默认")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 8) {
                String serverId = args.substring(0, 8);
                tempHashMap.put("default", serverId);
                UserData.userData.put(user.getId(), tempHashMap);
                user.sendMessage("设置成功");
            } else {
                user.sendMessage("命令格式不正确");
            }
        }

        if (command.equals("翼龙启动")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    user.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.START).execute();
                    user.sendMessage("正在启动");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.START).execute();
                    user.sendMessage("正在启动");
                } else {
                    user.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙关机")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    user.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.STOP).execute();
                    user.sendMessage("正在关机");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.STOP).execute();
                    user.sendMessage("正在关机");
                } else {
                    user.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙强停")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    user.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.KILL).execute();
                    user.sendMessage("正在强停");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.KILL).execute();
                    user.sendMessage("正在强停");
                } else {
                    user.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙重启")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    user.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.RESTART).execute();
                    user.sendMessage("正在重启");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.RESTART).execute();
                    user.sendMessage("正在重启");
                } else {
                    user.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙执行")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.userData.get(user.getId()) == null) {
                user.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.userData.get(user.getId());
            }
        }
    }
}