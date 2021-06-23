package local.bwg.support;

import com.google.gson.Gson;
import local.bwg.User;
import local.bwg.telegram.TelegramUser;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TelegramUserSaver implements SaveSupport {
    private final Logger logger = Logger.getLogger(TelegramUserSaver.class.getName());

    @Override
    public boolean saveLog(String line, String data) {
        return false;
    }

    @Override
    public boolean save(Object obj) {
        try {
            FileOutputStream f = new FileOutputStream(new File("udata_tg\\",((TelegramUser) obj).getuID()));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(obj);

            o.close();
            f.close();
            return true;
        } catch (IOException e) {
            logger.warning("fileerr");
        }
        return false;
    }

    @Override
    public boolean saveInJson(Object obj) {
        try (FileWriter file = new FileWriter("udata_tg-json\\"+((TelegramUser) obj).getuID())) {
            Gson gson = new Gson();
            file.write(gson.toJson(obj, TelegramUser.class));
            file.flush();
            return true;
        } catch (IOException exception) {
            logger.warning("fileerr");
            return false;
        }
    }

    @Override
    public Object load(String line) {
        try {
            FileInputStream fi = new FileInputStream(
                    new File("udata_tg\\" + line));
            ObjectInputStream oi = new ObjectInputStream(fi);

            TelegramUser userObj = (TelegramUser) oi.readObject();

            oi.close();
            fi.close();
            return userObj;
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("fileerr");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public TelegramUser loadFromJson(String path) {
        /*try {
            FileInputStream fi = new FileInputStream(
                    new File("udata_tg\\" + line));
            ObjectInputStream oi = new ObjectInputStream(fi);

            TelegramUser userObj = (TelegramUser) oi.readObject();

            oi.close();
            fi.close();
            return userObj;
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("fileerr");
            e.printStackTrace();
        }
        return null;*/
        try(FileReader reader = new FileReader(
                "udata_tg-json\\" + path.replaceAll("/", ""))){
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while((c=reader.read()) != -1){
                stringBuilder.append((char)c);
            }
            logger.info("Read Done! Object: " + stringBuilder);

            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), TelegramUser.class);
        } catch(IOException ex) {
            logger.warning("fileerr");
            return null;
        }
    }

    @Override
    public ArrayList<String> getAllFilesName() {
        ArrayList<String> list = new ArrayList<>();
        try {
            File folder = new File("udata_tg\\");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    list.add(file.getName());
                    System.out.println(file.getName());
                }
            }
        } catch (NullPointerException ignore) {
            System.out.println("NullPointerException: Directory is not found!");
        }
        return list;
    }

    @Override
    public String getFileLastModified(String filename) {
        return null;
    }
}