package local.bwg.telegram;

import local.bwg.model.BotOptions;
import local.bwg.support.SaveSupport;
import local.bwg.support.TelegramUserSaver;
import local.bwg.support.VLCSupport;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TelegramBotCore extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(TelegramBotCore.class.getName());

    private static final String BOT_NAME = BotOptions.getTelegramBotName();
    private static final String BOT_TOKEN = BotOptions.getTelegramBotToken();

    private ArrayList<TelegramUser> observers = new ArrayList<>();

    SaveSupport save = new TelegramUserSaver();

    public void sendQuary(String msg) {
        observers.forEach(obs -> sendMessage(obs.getuID(), msg));
    }

    TelegramBotCore() {
        ArrayList<String> uIDlist = save.getAllFilesName();
        uIDlist.forEach(e -> observers.add((TelegramUser) save.load(e)));
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        logger.info(convertToUTF8(message));
        logger.info(message);
        logger.info(convertToASCII(message));
        System.out.println(convertToUTF8(message));
        messageSeparator(update.getMessage().getChatId().toString(), message);
    }

    public void messageSeparator(String id, String message) {
        switch (message) {
            case "/observers": {
                StringBuilder msg = new StringBuilder();
                observers.forEach(e -> {
                    msg.append(e.getuID()).append("\n");
                });
                sendMessage(id, convertToUTF8(msg.toString()));
                break;
            }
            case "/sub": {
                TelegramUser tu = new TelegramUser(id);
                if (!observers.contains(tu)) {
                    tu.setSubscribe(true);
                    save.save((Object) tu);
                    observers.add(tu);
                    sendMessage(id, convertToUTF8("вы подписаны"));
                } else
                    sendMessage(id, convertToUTF8("не донимай меня"));
                break;
            }
            case "/unsub": {
                TelegramUser tu = new TelegramUser(id);
                if (observers.contains(tu)) {
                    observers.remove(tu);
                    sendMessage(id, convertToUTF8("Подписка отменена"));
                } else
                    sendMessage(id, convertToUTF8("не донимай меня"));
                break;
            }
            case "/start": {
                sendMessage(id, convertToUTF8("дороу чел"));
                break;
            }
            case "/help": {
                sendMessage(id, convertToUTF8(
                        "ну давай"
                                + "\n /start"
                                + "\n /sub подписаться на уведомления от ts бота"
                                + "\n /unsub отписаться"
                                + "\n /next"
                                + "\n /prev"
                                + "\n /track"
                                + "\n /station"
                                + "\n /goto [id]"
                ));
                break;
            }
            case "/next": {
                if (VLCSupport.vlcNextTrack()) {
                    sendMessage(id, convertToUTF8("Station: " + VLCSupport.GetStationName()));
                } else {
                    sendMessage(id, convertToUTF8("Failed!"));
                }
                break;
            }
            case "/prev": {
                if (VLCSupport.vlcPrevTrack()) {
                    sendMessage(id, convertToUTF8("Station: " + VLCSupport.GetStationName()));
                } else {
                    sendMessage(id, convertToUTF8("Failed!"));
                }
                break;
            }
            case "/track": {
                sendMessage(id, convertToUTF8("Track: " + VLCSupport.GetTrackName()));
                break;
            }
            case "/station": {
                sendMessage(id, convertToUTF8("Station: " + VLCSupport.GetStationName()));
                break;
            }
            default: {
                try {
                    if (message.toLowerCase().startsWith(convertToUTF8("/goto"))) {
                        String station_id = message.split(" ")[1];
                        if (VLCSupport.GoTo(station_id)) {
                            sendMessage(id, convertToUTF8("Station: " + VLCSupport.GetStationName()));
                        } else {
                            sendMessage(id, convertToUTF8("Failed!"));
                        }
                    } else

                        if (message.toLowerCase().startsWith("/all")) {
                        sendQuary("Observer: " + message.substring(5));
                    } /*else if (message.toLowerCase().startsWith(convertToUTF8("вопрос"))) {
                        String[] query = message.split(" ");
                        sendMessage(id, convertToUTF8(new ClanHole().clanHoleQuestions(query[1])));
                    } else if (new ClanHole().clanHoleQuestions(message) != null) {
                        sendMessage(id, convertToUTF8(new ClanHole().clanHoleQuestions(message)));
                    }*/ else
                        sendMessage(id, convertToUTF8(
                                "не нинаю такого"
                                        + "\n Посмотри что умею:"
                                        + "\n /help"));
                } catch (Exception ignore) {
                    sendMessage(id, convertToUTF8(
                            "нинаю такого"
                                    + "\n Посмотри что умею:"
                                    + "\n /help"));
                }
            }
        }
    }

    /**
     * Sender
     * @param chatId чат куда отправлять
     * @param s сообщение
     */
    public synchronized void sendMessage(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private String convertToUTF8(String text) {
        return new String(text.getBytes(), StandardCharsets.UTF_8);
    }
    private String convertToASCII(String text) {
        return new String(text.getBytes(), StandardCharsets.US_ASCII);
    }

}