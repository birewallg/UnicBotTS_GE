package local.bwg.Convertor;

import local.bwg.model.AbstractUser;
import local.bwg.model.TeamspeakUser;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import org.json.JSONObject;

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
        //if (args.length == 2) jsonConvertor.convert(null, args[0], args[1]);
        jsonConvertor.convert(new TeamspeakUser(), null, "udata-json/");
        jsonConvertor.convert(new TeamspeakUser(), null, "udata_tg-json/");
    }

    public JsonConvertor() {
        logger.info("Json convertor initialization...");
    }

    /**
     * convert all files to json
     */
    private void convert(Object object, String fromPath, String toPath) {
        logger.info(
                "Create directory "
                + toPath + ": "
                + createDirectory(toPath)
        );

        SaveSupport saveSupport = new FileReaderWriterExp();
        for (String filename : saveSupport.getAllFilesName()) {
            AbstractUser user = (TeamspeakUser) saveSupport.load(filename);//убрать хуйня какая-то
            if (user == null) continue;
            JSONObject json = user.getJSONObject();
            jsonFileWriter(user.getJSONObject(), toPath, filename);
            logger.info("Convert object: " + json);
        }
    }

    /**
     * Save json file
     * @param json json object
     * @param path filepath
     * @param filename filename
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean jsonFileWriter(JSONObject json, String path, String filename) {
        try (FileWriter file = new FileWriter(path + filename)) {
            file.write(json.toString());
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
