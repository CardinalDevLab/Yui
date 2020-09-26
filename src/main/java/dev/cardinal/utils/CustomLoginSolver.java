package dev.cardinal.utils;

import kotlin.coroutines.Continuation;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.LoginSolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CustomLoginSolver {
    public static LoginSolver getLoginSolver() {
        final FileUtils fileUtils = new FileUtils();
        LoginSolver customLoginSolver = new LoginSolver() {
            @Nullable
            @Override
            public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
                final String[] return_data = new String[1];
                return_data[0] = "";
                try {
                    System.out.println("需要图片验证码登录, 验证码为 4 字母");
                    System.out.println("验证码已保存到/插件目录/temp/captcha_picture.png");
                    System.out.println("请在/插件目录/temp/captcha_answer.txt中输入，并等待");
                    final File answer = new File(fileUtils.getBaseFolder() + "/temp/captcha_answer.txt");
                    if (!answer.exists()) {
                        File dir = new File(answer.getParent());
                        dir.mkdir();
                        answer.createNewFile();
                    }
                    final File picture = new File(fileUtils.getBaseFolder() + "/temp/captcha_picture.png");
                    FileOutputStream fos = new FileOutputStream(picture);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                    TimerTask task = new FileWatcher(answer) {
                        @Override
                        protected void onChange(File file) {
                            try {
                                System.out.println("文件更改");
                                BufferedReader in = new BufferedReader(new FileReader(answer));
                                return_data[0] = in.readLine();
                                in.close();
                                picture.delete();
                                answer.delete();
                            } catch (IOException e) {
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, new Date(), 200);
                    while (return_data[0].length() == 0) {
                        System.out.println("文件未更改，等待10s");
                        TimeUnit.SECONDS.sleep(10);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                return return_data[0];
            }

            @Nullable
            @Override
            public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String url, @NotNull Continuation<? super String> continuation) {
                System.out.println("需要滑动验证码，请在任意浏览器中打开以下链接并完成验证码");
                System.out.println("完成后请更改/插件目录/temp/captcha_state.txt第一行为任意字符");
                try {
                    final String[] return_data = new String[1];
                    return_data[0] = "";
                    final File answer = new File(fileUtils.getBaseFolder() + "/temp/captcha_state.txt");
                    if (!answer.exists()) {
                        File dir = new File(answer.getParent());
                        dir.mkdir();
                        answer.createNewFile();
                    }
                    TimerTask task = new FileWatcher(answer) {
                        @Override
                        protected void onChange(File file) {
                            try {
                                System.out.println("文件更改");
                                BufferedReader in = new BufferedReader(new FileReader(answer));
                                return_data[0] = in.readLine();
                                in.close();
                                answer.delete();
                            } catch (IOException e) {
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, new Date(), 200);
                    while (return_data[0].length() == 0) {
                        System.out.println("文件未更改，等待10s");
                        TimeUnit.SECONDS.sleep(10);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                return "finished";
            }

            @Nullable
            @Override
            public Object onSolveUnsafeDeviceLoginVerify(@NotNull Bot bot, @NotNull String url, @NotNull Continuation<? super String> continuation) {
                System.out.println("需要进行账户安全认证");
                System.out.println("该账户有[设备锁]/[不常用登录地点]/[不常用设备登录]的问题");
                System.out.println("完成以下账号认证即可成功登录");
                System.out.println(url);
                System.out.println("完成后请更改/插件目录/temp/captcha_state.txt第一行为任意字符");
                try {
                    final String[] return_data = new String[1];
                    return_data[0] = "";
                    final File answer = new File(fileUtils.getBaseFolder() + "/temp/captcha_state.txt");
                    if (!answer.exists()) {
                        File dir = new File(answer.getParent());
                        dir.mkdir();
                        answer.createNewFile();
                    }
                    TimerTask task = new FileWatcher(answer) {
                        @Override
                        protected void onChange(File file) {
                            try {
                                System.out.println("文件更改");
                                BufferedReader in = new BufferedReader(new FileReader(answer));
                                return_data[0] = in.readLine();
                                in.close();
                                answer.delete();
                            } catch (IOException e) {
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, new Date(), 200);
                    while (return_data[0].length() == 0) {
                        System.out.println("文件未更改，等待10s");
                        TimeUnit.SECONDS.sleep(10);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                return "finished";
            }
        };
        return customLoginSolver;
    }
}
