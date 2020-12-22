package local.bwg.support;

import local.bwg.User;
import local.bwg.telegram.TelegramUser;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TelegramUserSaver implements SaveSupport {
    Logger logger = Logger.getLogger(this.getClass().getName());

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
            e.printStackTrace();
        }
        return false;
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