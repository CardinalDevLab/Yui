package dev.cardinal.module;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import dev.cardinal.data.ConfigData;
import dev.cardinal.data.UserData;
import dev.cardinal.utils.MessageUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class pterodactyl {
    public void onMessage(MessageEvent event) {
        String message = MessageUtils.messageToString(event.getMessage());
        User sender = event.getSender();
        Contact contact = event.getSubject();
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
            if (event instanceof GroupMessageEvent) {
                if (event.getBot().getGroup(event.getSubject().getId()).get(event.getBot().getId()).getPermission().getLevel() < 1 || event.getBot().getGroup(event.getSubject().getId()).get(event.getSender().getId()).getPermission().getLevel() > 1) {
                    event.getSubject().sendMessage("敏感操作, 请私信进行! 机器人无权限, 无法撤回!");
                    return;
                }
                event.getBot().recallIn(event.getMessage(), 500);
                event.getSubject().sendMessage("敏感操作, 请私信进行!");
                return;
            }
            if (args.length() <= 8) {
                contact.sendMessage("命令格式不正确");
                return;
            }
            String serverId = args.substring(0,8);
            String token = args.substring(9);
            PteroClient api = pteroBuilder.setToken(token).build().asClient();
            if (api.retrieveServerByIdentifier(serverId).execute().isServerOwner()) {
                HashMap tempHashMap = null;
                if (UserData.pterodactylUserData.get(sender.getId()) != null) {
                    tempHashMap = UserData.pterodactylUserData.get(sender.getId());
                } else {
                    tempHashMap = new HashMap();
                }
                tempHashMap.put(serverId,token);
                UserData.pterodactylUserData.put(sender.getId(),tempHashMap);
                contact.sendMessage("绑定成功");
            }
        }

        if (command.equals("翼龙状态")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    contact.sendMessage("未设置默认服务器");
                } else {
                    contact.sendMessage("正在查询...");
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    Utilization utilization = api.retrieveUtilization(api.retrieveServerByIdentifier(serverId).execute()).execute();
                    String tempMessageCache = "服务器: " + api.retrieveServerByIdentifier(serverId).execute().getName() + "\n当前状态: " + utilization.getState() + "\nCPU: " + utilization.getCurrentCPU() + "/" + utilization.getMaxCPU()
                            + "\n内存: " + utilization.getCurrentMemory() + "/" + utilization.getMaxMemory()
                            + "\n磁盘: " + utilization.getCurrentDisk() +  "/" + utilization.getMaxDisk();
                    contact.sendMessage(tempMessageCache);
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
                    contact.sendMessage(tempMessageCache);
                } else {
                    contact.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙默认")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 8) {
                String serverId = args.substring(0, 8);
                tempHashMap.put("default", serverId);
                UserData.pterodactylUserData.put(sender.getId(), tempHashMap);
                contact.sendMessage("设置成功");
            } else {
                contact.sendMessage("命令格式不正确");
            }
        }

        if (command.equals("翼龙启动")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    contact.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.START).execute();
                    contact.sendMessage("正在启动");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.START).execute();
                    contact.sendMessage("正在启动");
                } else {
                    contact.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙关机")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    contact.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.STOP).execute();
                    contact.sendMessage("正在关机");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.STOP).execute();
                    contact.sendMessage("正在关机");
                } else {
                    contact.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙强停")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    contact.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.KILL).execute();
                    contact.sendMessage("正在强停");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.KILL).execute();
                    contact.sendMessage("正在强停");
                } else {
                    contact.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙重启")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            if (args.length() == 0) {
                if (tempHashMap.get("default") == null) {
                    contact.sendMessage("未设置默认服务器");
                } else {
                    String serverId = String.valueOf(tempHashMap.get("default"));
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.RESTART).execute();
                    contact.sendMessage("正在重启");
                }
            } else {
                if (args.length() == 8) {
                    String serverId = args.substring(0,8);
                    String token = String.valueOf(tempHashMap.get(serverId));
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    api.setPower(clientServer, PowerAction.RESTART).execute();
                    contact.sendMessage("正在重启");
                } else {
                    contact.sendMessage("命令格式不正确");
                }
            }
        }

        if (command.equals("翼龙列表")) {
            HashMap tempHashMap = new HashMap();
            String tempMessageCache = "服务器列表: \n标识符  服务器名\n";
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
                Iterator<Map.Entry<String,String>> iterator = tempHashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String,String> entry = iterator.next();
                    if (!entry.getKey().equals("default")) {
                        String serverId = entry.getKey();
                        String token = entry.getValue();
                        ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                        String serverName = clientServer.getName();
                        tempMessageCache += serverId + " : " + serverName + "\n";
                    }
                }
                contact.sendMessage(tempMessageCache);
            }
        }

        if (command.equals("翼龙执行")) {
            HashMap tempHashMap = new HashMap();
            if (UserData.pterodactylUserData.get(sender.getId()) == null) {
                contact.sendMessage("请先绑定");
                return;
            } else {
                tempHashMap = UserData.pterodactylUserData.get(sender.getId());
            }
            String serverId;
            String token;
            String tempCommand;
            if (args.length() > 8) {
                serverId = args.substring(0,8);
                if (tempHashMap.get(serverId) != null) {
                    token = String.valueOf(tempHashMap.get(serverId));
                    tempCommand = args.substring(9);
                    ClientServer clientServer = pteroBuilder.setToken(token).build().asClient().retrieveServerByIdentifier(serverId).execute();
                    PteroClient api = pteroBuilder.setToken(token).build().asClient();
                    api.sendCommand(clientServer, tempCommand).execute();
                    contact.sendMessage("正在执行" + tempCommand);
                } else {
                    if (tempHashMap.get("default") != null) {
                        contact.sendMessage("此服务器未绑定");
                    }
                }
            } else {
                contact.sendMessage("请指定服务器");
            }
        }

    }

}
