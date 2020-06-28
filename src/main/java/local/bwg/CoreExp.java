package local.bwg;

import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import local.bwg.support.WinAPISupport;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import local.bwg.telegram.AppTelegramInline;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import static com.github.theholywaffle.teamspeak3.api.ChannelProperty.CHANNEL_NAME;

/**
 * Core 2.0
 */

public class CoreExp {
    private static volatile int clientId;

    private static ArrayList<User> userdatabase = new ArrayList<>();

    private static ArrayList<User> lastuserdatabase = new ArrayList<>();

    private static SaveSupport saveSupport;

    private static AppTelegramInline appTelegramInline = new AppTelegramInline();

    CoreExp(String address, final String port, final String login, final String password) {

        appTelegramInline.run();

        saveSupport = new FileReaderWriterExp();

        final TS3Config cfg = new TS3Config();
        cfg.setHost(address);
        cfg.setDebugLevel(Level.ALL);
        cfg.setReconnectStrategy(ReconnectStrategy.constantBackoff());
        cfg.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                stuffThatNeedsToRunEveryTimeTheQueryConnects(ts3Query.getApi(), login, password, port);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                // Nothing
            }
        });

        final TS3Query query = new TS3Query(cfg);
        query.connect();

        stuffThatOnlyEverNeedsToBeRunOnce(query.getApi());
        //doSomethingThatTakesAReallyLongTime(query.getAsyncApi());

        //query.exit();
    }

    /**
     * TEST
     */
    /*public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            addLastUser(new User("name", i));
            System.out.print(printLastUsers());
            System.out.println("-------------");
        }
    }*/
    private static String printLastUsers() {
        StringBuilder lusers = new StringBuilder();
        for(User u : lastuserdatabase) {
            lusers.append("\n").append(u.getTime()).append(": ").append(u.getuName());
        }
        return lusers.toString();
    }
    private static void addLastUser(User user) {
        if (lastuserdatabase.size() > 15) {
            lastuserdatabase.remove(lastuserdatabase.size()-1);
        }
        lastuserdatabase.add(0, user);
    }

    private static void stuffThatNeedsToRunEveryTimeTheQueryConnects(final TS3Api api, String login, final String password, String port) {
        api.selectVirtualServerByPort(Integer.valueOf(port));
        api.login(login, password);

        api.setNickname("UnicBot");
        api.moveClient(clientId, 31);
        api.registerAllEvents();

        clientId = api.whoAmI().getId();

        saveLog(api.whoAmI().getNickname() + " join server");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Date date = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm");
                    String formatTime = formatForDateNow.format(date);

                    Map<ChannelProperty, String> options_c1 = new HashMap<>();
                    options_c1.put(CHANNEL_NAME, "[spacer.time]   TIME : " + formatTime + "  UTC+9");
                    api.editChannel(22, options_c1);

                    for (User u : userdatabase) {
                        if (u.getWakeUp().equals(formatTime)) {
                            api.pokeClient(u.isuID(), "Wake Up!");
                        }
                    }

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                /*Map<ChannelProperty, String> options_c2 = new HashMap<>();
                options_c2.put(CHANNEL_NAME, "[spacer.t2]█     █ █    █ █     █ █     █");
                api.editChannel(25, options_c2);

                Map<ChannelProperty, String> options_c3 = new HashMap<>();
                options_c3.put(CHANNEL_NAME, "[spacer.t3]█     █ █    █ █     █ █     █");
                api.editChannel(26, options_c3);

                Map<ChannelProperty, String> options_c4 = new HashMap<>();
                options_c4.put(CHANNEL_NAME, "[spacer.t4]▀▄▄▄▀ ▀▄▄▄▀ ▀▄▄▄▀ ▀▄▄▄▀");
                api.editChannel(27, options_c4);*/
            }
        }).start();
    }

    private static void buckGroundTasks(final TS3Api api) {
        for (Client c : api.getClients()) {

            //User user = new User(c.getNickname(), c.getId(), c.getUniqueIdentifier());

            User user = saveSupport.load(c.getUniqueIdentifier());
            if (user == null) {
                user = new User(c.getNickname(), c.getId(), c.getUniqueIdentifier());
            }

            user.updateTime();
            user.setuName(c.getNickname());
            user.setuID(c.getId());

            /*if (Objects.equals(user.getuName(), "Broker Day 27'")) {
                user.setLoginnotifyStatus(true);
                user.setPrivilegeLevel(27);
            }*/
            userdatabase.add(user);
        }
    }

    private static void stuffThatOnlyEverNeedsToBeRunOnce(final TS3Api api) {

        buckGroundTasks(api);

        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                api.sendPrivateMessage(e.getClientId(),
                        " welcome to the party! " +
                                "\n Commands: " +
                                "\n !use loginnotify" +
                                "\n !mytime" +
                                "\n !wakeup");
                ///api.sendPrivateMessage(e.getClientId(), "You total time: "+ );
                //api.sendPrivateMessage(e.getClientId(), " !use loginnotify");

                //log
                saveLog(e.getClientNickname() + " joined server");

                //telegram
                appTelegramInline.sendMessage(e.getClientNickname() + " joined server");

                try {
                    //load user data from file
                    User user = saveSupport.load(e.getUniqueClientIdentifier());
                    if (user == null) {
                        user = new User(e.getClientNickname(), e.getClientId(), e.getUniqueClientIdentifier());
                    }

                    user.updateTime();
                    user.setuName(e.getClientNickname());
                    user.setuID(e.getClientId());

                    api.sendPrivateMessage(e.getClientId(), "You total time: " + user.getTotalTimeString());
                    /*if (Objects.equals(user.getuName(), "Broker Day 27")) {
                        user.setLoginnotifyStatus(true);
                        user.setPrivilegeLevel(27);
                    }*/
                    //user.setLoginnotifyStatus(false);

                    for (User u : userdatabase) {
                        if(u.getuPrivilegeLevel() == 27) {
                            api.sendPrivateMessage(u.isuID(), "ui: " + e.getUniqueClientIdentifier());
                        }
                        if(u.isLoginnotifyStatus()) {
                            api.sendPrivateMessage(u.isuID(),user.getuName() + " join server.");
                        }
                        if(u.isuID() == e.getClientId()) {
                            user = null;
                            break;
                        }
                    }
                    if (user != null) {
                        userdatabase.add(user);
                    }
                } catch (Exception ignore) {
                    System.out.println("Error: uID");
                }
            }
            @Override
            public void onClientLeave(ClientLeaveEvent e) {
                try {
                    User user = null;
                    for (User u : userdatabase) {
                        if(e.getClientId() == u.isuID()){
                            user = u;
                            break;
                        }
                    }


                    if(user != null) {
                        saveLog(user.getuName() + " left server");
                        addLastUser(user);
                    }


                    for (User u : userdatabase) {
                        if(u.isLoginnotifyStatus()) {
                            if(user != null)
                                api.sendPrivateMessage(u.isuID(),user.getuName() + " left server.");
                        }
                    }

                    if(user != null) {
                        // update user properties
                        user.updateTotalTime();

                        userdatabase.remove(user);
                        saveSupport.save(user);
                    }
                } catch (Exception ignore) {
                    System.out.println("Error: uID not found");
                }
            }
            @Override
            public void onClientMoved(ClientMovedEvent e) {

            }
            @Override
            public void onTextMessage(TextMessageEvent e) {
                // Only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CLIENT && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();
                    if (message.startsWith("!last")) {
                        api.sendPrivateMessage(e.getInvokerId(), printLastUsers());
                    } else if (message.startsWith("!use loginnotify") || message.startsWith("!u l")) {
                        for (User u : userdatabase) {
                            if (e.getInvokerId() == u.isuID()) {
                                if (u.isLoginnotifyStatus()) {
                                    u.setLoginnotifyStatus(false);
                                    api.sendPrivateMessage(e.getInvokerId(), "Login notification is off for u.");
                                } else {
                                    u.setLoginnotifyStatus(true);
                                    api.sendPrivateMessage(e.getInvokerId(), "Login notification is on for u.");
                                }
                                break;
                            }
                        }
                    } else if (message.startsWith("!wakeup")){
                        for (User u : userdatabase) {
                            if (e.getInvokerId() == u.isuID()) {
                                try {
                                    u.setWakeUp(message.substring(8));
                                    api.sendPrivateMessage(e.getInvokerId(), "wakeup set " + message.substring(8));
                                } catch (Exception ignore) {
                                    api.sendPrivateMessage(e.getInvokerId(), "use: !wakeup [time]");
                                    break;
                                }
                                break;
                            }
                        }
                    } else if (message.startsWith("!cmd")) {
                        WinAPISupport winApi = new WinAPISupport();
                        winApi.exCMD(message.substring(4));
                    } else if (message.startsWith("!shutdown 0")) {
                        closeBot(e.getInvokerUniqueId());
                    } else if (message.startsWith("!use")) {
                        //api.sendPrivateMessage(e.getInvokerId(), ": !use notify " );
                        api.sendPrivateMessage(e.getInvokerId(), ": !use loginnotify "  );

                    } else if (message.startsWith("!mytime")) {
                        //api.sendPrivateMessage(e.getInvokerId(), ": !use notify " );
                        for (User u : userdatabase) {
                            if (e.getInvokerId() == u.isuID()) {
                                api.sendPrivateMessage(e.getInvokerId(), "Total time : " + u.getTotalTimeString());
                            }
                        }
                    }

                    else if (message.startsWith("!get")) {
                        try {
                            String[] cmd = message.split(" ");
                            switch (cmd[1]) {
                                case "all": {
                                    for (String filename : saveSupport.getAllFilesName()) {
                                        User user = saveSupport.load(filename);
                                        if (user == null) continue;
                                        String lastLoginDate = saveSupport.getFileLastModified(filename);
                                        api.sendPrivateMessage(e.getInvokerId(),
                                                "\n Name: " + user.getuName()
                                                        + "\n Identifier: " + user.getuUnicID()
                                                        + "\n TotalTime: " + user.getTotalTimeStringNoCalc()
                                                        + "\n LastLogin: " + lastLoginDate
                                        );
                                    }
                                    break;
                                }
                                case "users": {
                                    for (User u : userdatabase) {
                                        api.sendPrivateMessage(e.getInvokerId(),
                                                "\n Name: " + u.getuName()
                                                        + "\n Identifier: " + u.getuUnicID()
                                        );
                                    }
                                    break;
                                }
                                case "user": {
                                    User user = saveSupport.load(cmd[2]);
                                    String lastlogindate = saveSupport.getFileLastModified(cmd[2]);
                                    api.sendPrivateMessage(e.getInvokerId(),
                                            "\n Name: " + user.getuName()
                                                    + "\n Identifier: " + user.getuUnicID()
                                                    + "\n TotalTime: " + user.getTotalTimeStringNoCalc()
                                                    + "\n LastLogin: " + lastlogindate
                                    );
                                    break;
                                }
                            }
                        } catch (Exception ignore){
                            api.sendPrivateMessage(e.getInvokerId(),"incorrect syntax");
                        }
                    }
                }
            }
        });
    }

    private static void saveLog(String data){
        saveSupport.saveLog("unicbotlog", data);
    }

    private static void closeBot(String uID) {
        for (User u : userdatabase) {
            u.updateTotalTime();
            saveSupport.save(u);
        }
        saveLog(uID + ": shutdown 0");
        System.exit(0);
    }
}
