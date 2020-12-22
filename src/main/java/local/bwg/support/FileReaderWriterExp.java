package local.bwg.support;

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
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User load(String path) {
        try {
            FileInputStream fi = new FileInputStream(
                    new File("udata\\"
                            + path.replaceAll("/", "")));
            ObjectInputStream oi = new ObjectInputStream(fi);

            User userObj = (User) oi.readObject();

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
        File folder = new File("udata\\");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> list = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                list.add(file.getName());
                //System.out.println(file.getName());
            }
        }
        return list;
    }

    @Override
    public String getFileLastModified(String filename) {
        File file = new File("udata\\" + filename);
        //System.out.println("Before Format : " + file.lastModified());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH|mm");
        return sdf.format(file.lastModified());
    }
}