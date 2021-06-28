package local.bwg.convertor;

import com.google.gson.Gson;
import local.bwg.model.InterfaceUser;
import local.bwg.model.TeamspeakUser;
import local.bwg.model.TelegramUser;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import local.bwg.support.TelegramUserSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class converter serializable file to json
 */
public class JsonConvertor {
    private static final Logger logger = Logger.getLogger(JsonConvertor.class.getName());


    public static void main(String[] args) {
        JsonConvertor jsonConvertor = new JsonConvertor();
        jsonConvertor.convert(new TeamspeakUser(), null, "udata-json/", new FileReaderWriterExp());
        jsonConvertor.convert(new TelegramUser("255397596"), null, "udata_tg-json/", new TelegramUserSaver());
    }

    public JsonConvertor() {
        logger.info("Json convertor initialization...");
    }

    /**
     * convert all files to json
     */
    public void convert(InterfaceUser user, String fromPath, String toPath, SaveSupport ss) {
        logger.info(
                "Create directory "
                + toPath + ": "
                + createDirectory(toPath)
        );

        ss.getAllFilesName(null).forEach(filename -> {
            if (!user.loadFromSirializeble(filename)) {
                logger.info("Convert error! File: " + filename);
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(user, user.getClass());
            jsonFileWriter(json, toPath, filename);
            logger.info("Convert Done! Object: " + json);
        });
    }

    /**
     * Save json file
     * @param json json object
     * @param path filepath
     * @param filename filename
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean jsonFileWriter(String json, String path, String filename) {
        try (FileWriter file = new FileWriter(path + filename)) {
            file.write(json);
            file.flush();
            return true;
        } catch (IOException exception) {
            logger.info(exception.getMessage());
        }
        return false;
    }

    /**
     * Create directory
     * @param path path
     * @return boolean
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean createDirectory(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists())
            return fileDir.mkdirs();
        return false;
    }
}
