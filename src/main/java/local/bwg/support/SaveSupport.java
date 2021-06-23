package local.bwg.support;

import java.util.ArrayList;

public interface SaveSupport {
    boolean saveLog(String line, String data);
    boolean save(Object obj);
    boolean saveJson(Object obj);
    Object load(String line);
    Object loadJson(String line);
    ArrayList<String> getAllFilesName(String path);
    String getFileLastModified(String filename);
}