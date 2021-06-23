package local.bwg.support;

import java.util.ArrayList;

public interface SaveSupport {
    boolean saveLog(String line, String data);
    boolean save(Object obj);
    boolean saveInJson(Object obj);
    Object load(String line);
    Object loadFromJson(String line);
    ArrayList<String> getAllFilesName();
    String getFileLastModified(String filename);
}