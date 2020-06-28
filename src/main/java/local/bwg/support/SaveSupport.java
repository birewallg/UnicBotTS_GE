package local.bwg.support;

import local.bwg.User;

import java.util.ArrayList;

public interface SaveSupport {
    boolean saveLog(String line, String data);
    boolean save(User obj);
    User load(String line);
    ArrayList<String> getAllFilesName();
    String getFileLastModified(String filename);
}
