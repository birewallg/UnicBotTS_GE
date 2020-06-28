package local.bwg.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class AppTelegramInline {
    public static TelegramBotCore telegramBotCore;

    public static void main(String[] args) {
        AppTelegramInline appTelegramInline = new AppTelegramInline();
        appTelegramInline.run();
    }


    public void run() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(telegramBotCore = new TelegramBotCore());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage (String msg) {
        telegramBotCore.sendQuary(msg);
    }
}
