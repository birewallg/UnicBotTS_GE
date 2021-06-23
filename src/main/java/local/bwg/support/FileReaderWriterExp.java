package local.bwg.support;

import com.google.gson.Gson;
import local.bwg.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class FileReaderWriterExp implements SaveSupport {
    Logger logger = Logger.getLogger(this.getClass().getName());
    @Override
    public boolean saveLog(String path, String data) {
        try(FileWriter writer = new FileWriter(path, true)) {
            Date date = new Date();
            SimpleDateFormat formatForDateNow
                    = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss' : '");
            writer.write(formatForDateNow.format(date) + data);
            writer.append("\n");
            writer.flush();
        } catch (IOException e){
            logger.warning("fileerr");
            return false;
        }
        return true;
    }

    @Override
    public boolean save(Object obj) {
        try {
            User userObj = (User) obj;
            FileOutputStream f = new FileOutputStream(new File("udata\\"+userObj.getuUnicID().replaceAll("/", "")));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(userObj);

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
        try (FileWriter file = new FileWriter("udata-json\\" + ((User) obj).getuUnicID().replaceAll("/", ""))) {
            Gson gson = new Gson();
            file.write(gson.toJson(obj, User.class));
            file.flush();
            return true;
        } catch (IOException exception) {
            logger.warning("fileerr");
            return false;
        }
    }

    @Override
    public User load(String path) {
        try {
            FileInputStream fi = new FileInputStream(
                    new File("udata\\"
                            + path.replaceAll("/", "")));
            ObjectInputStream oi = new ObjectInputStream(fi);
            Object obj = oi.readObject();
            User userObj = (User) obj;

            oi.close();
            fi.close();
            return userObj;
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("fileerr");
        }
        return null;
    }

    @Override
    public User loadFromJson(String path) {
        try(FileReader reader = new FileReader(
                "udata-json\\" + path.replaceAll("/", ""))){
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while((c=reader.read()) != -1){
                stringBuilder.append((char)c);
            }
            logger.info("Read Done! Object: " + stringBuilder);

            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), User.class);
        } catch(IOException ex) {
            logger.warning("fileerr");
            return null;
        }
    }

    @Override
    public ArrayList<String> getAllFilesName() {
        File folder = new File("udata\\");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> list = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                list.add(file.getName());
            }
        }
        return list;
    }

    @Override
    public String getFileLastModified(String filename) {
        File file = new File("udata\\" + filename);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH|mm");
        return sdf.format(file.lastModified());
    }
}