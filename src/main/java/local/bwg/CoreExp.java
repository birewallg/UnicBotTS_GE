package local.bwg;

import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import local.bwg.support.VLCSupport;
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

import java.nio.charset.StandardCharsets;
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

    private static String track = "unknown";
    private static boolean trackNotify = true;

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
    }

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
        //api.selectVirtualServerByPort(Integer.valueOf(port));
        api.selectVirtualServerById(1);
        api.login(login, password);

        clientId = api.whoAmI().getId();
        api.moveClient(clientId, 32);

        api.registerAllEvents();
        api.setNickname("UnicBot");

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
            }
        }).start();
        new Thread(() -> {
            String track = "known";
            String station = "known";
            while (true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String temp = VLCSupport.GetTrackName();
                if (!temp.equals(track)){
                    Map<ChannelProperty, String> options_c1 = new HashMap<>();
                    options_c1.put(CHANNEL_NAME, "Track: " + temp.replaceAll("[^a-zA-Z0-9æøåÆØÅ_ -]",""));
                    api.editChannel(31, options_c1);
                    track = temp;
                }
                temp = VLCSupport.GetStationName();
                if (!temp.equals(station)){
                    Map<ChannelProperty, String> options_c1 = new HashMap<>();
                    options_c1.put(CHANNEL_NAME, "Station: " + temp.replaceAll("[^a-zA-Z0-9æøåÆØÅ_ -]",""));
                    api.editChannel(33, options_c1);
                    station = temp;
                }
            }
        }).start();
    }

    private static void buckGroundTasks(final TS3Api api) {
        for (Client c : api.getClients()) {

            User user = (User) saveSupport.load(c.getUniqueIdentifier());
            if (user == null) {
                user = new User(c.getNickname(), c.getId(), c.getUniqueIdentifier());
            }

            user.updateTime();
            user.setuName(c.getNickname());
            user.setuID(c.getId());

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
                //log
                saveLog(e.getClientNickname() + " joined server");

                //telegram
                appTelegramInline.sendMessage(e.getClientNickname() + " joined server");

                try {
                    //load user data from file
                    User user = (User) saveSupport.load(e.getUniqueClientIdentifier());
                    if (user == null) {
                        user = new User(e.getClientNickname(), e.getClientId(), e.getUniqueClientIdentifier());
                    }

                    user.updateTime();
                    user.setuName(e.getClientNickname());
                    user.setuID(e.getClientId());

                    api.sendPrivateMessage(e.getClientId(), "You total time: " + user.getTotalTimeString());

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

                        //telegram
                        appTelegramInline.sendMessage(user.getuName() + " left server");
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
                if (e.getTargetChannelId() == 32) {
                    int movingClientId = e.getClientId();
                    String name = api.getClientInfo(movingClientId).getNickname();

                    String stationname = VLCSupport.GetStationName();
                    String trackname = VLCSupport.GetTrackName();
                    byte[] msg = ("\n Hi, " + name + "\n Station: " + stationname
                            + "\n Track: " + trackname).getBytes(StandardCharsets.US_ASCII);
                    api.sendChannelMessage(new String(msg, StandardCharsets.UTF_8));
                    api.sendChannelMessage(
                                    "\n Commands: " +
                                    "\n !track" +
                                    "\n !station" +
                                    "\n !next" +
                                    "\n !prev");
                    /*
                    if (Objects.equals(trackname, "unknown")) {
                        api.sendChannelMessage("Restart...");
                        if (!WinAPISupport.exRestartFix()) {
                            api.sendChannelMessage("Sorry the radio does not work");
                        }
                    }
                    */
                }
            }
            @Override
            public void onTextMessage(TextMessageEvent e) {
                // Only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL
                        && e.getInvokerId() != clientId
                        && api.getClientInfo(e.getInvokerId()).getChannelId() == 32) {
                    String message = e.getMessage().toLowerCase();
                    /** Music control
                     * vlc control */
                    if (message.equals("!track")) {
                        //api.sendPrivateMessage(e.getInvokerId(), VLCSupport.GetTrackName());
                        api.sendChannelMessage(VLCSupport.GetTrackName());
                    } else if (message.equals("!station")) {
                        //api.sendPrivateMessage(e.getInvokerId(), VLCSupport.GetTrackName());
                        api.sendChannelMessage(VLCSupport.GetStationName());
                    } else if(message.startsWith("!next")){
                        if (VLCSupport.vlcNextTrack()) {
                            api.sendChannelMessage("Station: " + VLCSupport.GetStationName());
                        } else {
                            api.sendChannelMessage("Failed!");
                        }
                    } else if(message.startsWith("!prev")){
                        if (VLCSupport.vlcPrevTrack()) {
                            api.sendChannelMessage("Station: " + VLCSupport.GetStationName());
                        } else {
                            api.sendChannelMessage("Failed!");
                        }
                    }
                }
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
                                        User user = (User) saveSupport.load(filename);
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
                                    User user = (User) saveSupport.load(cmd[2]);
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
