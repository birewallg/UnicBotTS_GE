package local.bwg.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class BotOptions {
    private static final Logger logger = Logger.getLogger(BotOptions.class.getName());

    private BotOptionsImpl options = new BotOptionsImpl();

    public static void main(String[] args) {
        BotOptions options = new BotOptions();
    }

    public BotOptions() {
        this.loadOptionsFromFile("udata\\options\\", "options");
    }

    public String getTelegramBotName() {
        return options.TELEGRAM_BOT_NAME;
    }

    public String getTeamspeakBotPort() {
        return options.TEAMSPEAK_BOT_PORT;
    }

    public String getTelegramBotToken() {
        return options.TELEGRAM_BOT_TOKEN;
    }

    public String getTeamspeakBotLogin() {
        return options.TEAMSPEAK_BOT_LOGIN;
    }

    public String getTeamspeakBotPassword() {
        return options.TEAMSPEAK_BOT_PASSWORD;
    }

    public String getTeamspeakBotIpAddress() {
        return options.TEAMSPEAK_BOT_IP_ADDRESS;
    }

    public boolean saveOptions(BotOptions options) {
        Gson gson = new Gson();
        String json = gson.toJson(options);
        return jsonFileWriter(json, "", "options");
    }
    public void loadOptionsFromFile(String path, String filename) {
        Gson gson = new Gson();
        String json = this.jsonFileReader(path, filename);
        this.options = gson.fromJson(json, BotOptionsImpl.class);
        //return jsonFileWriter(json, "", "options");
    }
    public String jsonFileReader(String path, String filename) {
        try(FileReader reader = new FileReader(path + filename)){
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while((c=reader.read()) != -1){
                stringBuilder.append((char)c);
            }
            logger.info("Read Done! Object: " + stringBuilder);
            return stringBuilder.toString();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    /**
     * Save json file
     * @param json json object
     * @param path filepath
     * @param filename filename
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean jsonFileWriter(String json, String path, String filename) {
        try (FileWriter file = new FileWriter(path + filename)) {
            file.write(json);
            file.flush();
            return true;
        } catch (IOException exception) {
            logger.info(exception.getMessage());
        }
        logger.info("Save Done! Object: " + json);
        return false;
    }

    private static class BotOptionsImpl {
        public String TELEGRAM_BOT_NAME = "";
        public String TELEGRAM_BOT_TOKEN = "";
        public String TEAMSPEAK_BOT_LOGIN = "";
        public String TEAMSPEAK_BOT_PASSWORD = "";
        public String TEAMSPEAK_BOT_IP_ADDRESS = "";
        public String TEAMSPEAK_BOT_PORT = "";
    }
}
