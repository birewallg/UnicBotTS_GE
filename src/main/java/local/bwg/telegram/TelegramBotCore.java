package local.bwg.telegram;

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
import java.util.stream.Stream;

public class TelegramBotCore extends TelegramLongPollingBot {
    private static Logger logger = Logger.getLogger(TelegramBotCore.class.getName());

    private static final String BOT_NAME = "TS3SUnicBotNotiferBot";
    private static final String BOT_TOKEN = "942837035:AAF3dCriN53KD5qtPRQwL6XWQJGqysiFcB0";

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
        //String normalMessage = convertToUTF8(message);
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
                sendMessage(id, convertToUTF8("дороу"));
                break;
            }
            case "/help": {
                sendMessage(id, convertToUTF8(
                        "ну давай"
                                + "\n /start"
                                + "\n /sub подписаться на уведомления"
                                + "\n /unsub отписаться"
                                + "\n /next"
                                + "\n /prev"
                                + "\n /track"
                                + "\n /station"
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
                    if (message.toLowerCase().substring(0, 4).equals("/all")) {
                        sendQuary("Observer: " + message.substring(5));
                    } else if (message.toLowerCase().startsWith(convertToUTF8("вопрос"))) {
                        String[] query = message.split(" ");
                        switch (query[1]) {
                            case "31": {
                                sendMessage(id, convertToUTF8(
                                        "Какое право дает главе гильдии открытие базы? "
                                                + "\n"
                                                + "Ответ — Право выбирать постройки для улучшения."));
                                break;
                            }
                            case "32": {
                                sendMessage(id, convertToUTF8(
                                        "Накапливая очки, можно повышать уровень базы гильдии. " +
                                                "А что влияет на количество этих очков? "
                                                + "\n"
                                                + "Ответ — Вклад участников гильдии."));
                                break;
                            }
                            case "33": {
                                sendMessage(id, convertToUTF8(
                                        "Для строительства зданий гильдии требуются материалы и время. " +
                                                "Если использовать больше материалов, можно сократить время. Это верно?"
                                                + "\n"
                                                + "Ответ — Да."));
                                break;
                            }
                            case "34": {
                                sendMessage(id, convertToUTF8(
                                        "Что получат люди, завершив ежедневное задание Зала единения? "
                                                + "\n"
                                                + "Ответ — Лунное железо."));
                                break;
                            }
                            case "35": {
                                sendMessage(id, convertToUTF8(
                                        "Представители какой расы обладают третьим глазом? "
                                                + "\n"
                                                + "Ответ — Древние."));
                                break;
                            }
                            case "36": {
                                sendMessage(id, convertToUTF8(
                                        "В тысячном году на западе Идеального мира появилось и возвысилось новое " +
                                                "государство — Страна заката или Страна сумерек. В каком году оно исчезло?"
                                                + "\n"
                                                + "Ответ — В одна тысяча сорок четвертом."));
                                break;
                            }
                            case "37": {
                                sendMessage(id, convertToUTF8(
                                        "Всего за три десятилетия Страна сумерек превратилась в мощную державу. " +
                                                "Это произошло благодаря загадочному магическому предмету, попавшему в " +
                                                "руки к ее правителю. Что это был за предмет?"
                                                + "\n"
                                                + "Ответ — Золотая маска."));
                                break;
                            }
                            case "38": {
                                sendMessage(id, convertToUTF8(
                                        "Царица Минла разделила Чистилище на три области. Что к ним не относится?"
                                                + "\n"
                                                + "Ответ — Обитель ужаса."));
                                break;
                            }
                            case "39": {
                                sendMessage(id, convertToUTF8(
                                        "Дерево технологий гильдии носит название двадцати восьми созвездий " +
                                                "китайского зодиака. Какой из следующих древних трактатов не упоминает " +
                                                "эти созвездия?Дерево технологий гильдии носит название двадцати восьми созвездий китайского зодиака. Какой из следующих древних трактатов не упоминает эти созвездия?"
                                                + "\n"
                                                + "Ответ — ‘Сон в красном тереме’."));
                                break;
                            }
                            case "40": {
                                sendMessage(id, convertToUTF8(
                                        "Что получает игрок за прохождение Зала закаленного духа?"
                                                + "\n"
                                                + "Ответ – Опыт."));
                                break;
                            }
                        }
                    } else
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
        sendMessage.enableMarkdown(true);
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