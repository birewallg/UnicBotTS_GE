package local.bwg.model;

public class BotOptions {
    private static final String TELEGRAM_BOT_NAME = "";
    private static final String TELEGRAM_BOT_TOKEN = "";

    private static final String TEAMSPEAK_BOT_LOGIN = "UnicBot";
    private static final String TEAMSPEAK_BOT_PASSWORD = "";
    private static final String TEAMSPEAK_BOT_IP_ADDRESS = "";
    private static final String TEAMSPEAK_BOT_PORT = "";

    public static String getTelegramBotName() {
        return TELEGRAM_BOT_NAME;
    }

    public static String getTeamspeakBotPort() {
        return TEAMSPEAK_BOT_PORT;
    }

    public static String getTelegramBotToken() {
        return TELEGRAM_BOT_TOKEN;
    }

    public static String getTeamspeakBotLogin() {
        return TEAMSPEAK_BOT_LOGIN;
    }

    public static String getTeamspeakBotPassword() {
        return TEAMSPEAK_BOT_PASSWORD;
    }

    public static String getTeamspeakBotIpAddress() {
        return TEAMSPEAK_BOT_IP_ADDRESS;
    }
}
