package local.bwg.model;

import local.bwg.User;
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
        if (args.length == 1) jsonConvertor.convert(args[0]);
        jsonConvertor.convert("udata-json/");
    }

    public JsonConvertor(){
        logger.info("Json convertor initialization...");
    }

    /**
     * convert all files to json
     */
    private void convert(String toPath) {
        logger.info(
                "Create directory "
                + toPath + ": "
                + createDirectory(toPath)
        );

        SaveSupport saveSupport = new FileReaderWriterExp();
        for (String filename : saveSupport.getAllFilesName()) {
            User user = (User) saveSupport.load(filename);
            if (user == null) continue;
            JSONObject json = user.getJSONObject();
            fileJsonSave(user.getJSONObject(), toPath, filename);
            logger.info("Convert object: " + json);
        }
    }

    /**
     * Save json file
     * @param json json object
     * @param path path
     */
    public void fileJsonSave(JSONObject json, String path, String filename) {
        try (FileWriter file = new FileWriter(path + filename)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException exception) {
            logger.info(exception.getMessage());
        }
    }

    /**
     * Create directory
     * @param path path
     * @return boolean
     */
    private boolean createDirectory(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists())
            return fileDir.mkdirs();
        return false;
    }
}
