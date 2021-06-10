package local.bwg.model;

import local.bwg.User;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import org.json.JSONObject;

public class JsonConvertor {
    private static SaveSupport saveSupport;

    public static void main(String[] args) {
        convert();
    }

    private static void convert() {
        saveSupport = new FileReaderWriterExp();
        // convert all serialize data to json
        for (String filename : saveSupport.getAllFilesName()) {
            User user = (User) saveSupport.load(filename);
            if (user == null) continue;
            JSONObject json = new JSONObject();
            json.put("uName", user.getuName());
            json.put("uID", user.isuID());
            json.put("uUnicID", user.getuUnicID());
            json.put("uPrivilegeLevel", user.getuPrivilegeLevel());
            json.put("time", user.getTime());
            json.put("totalTime", user.getTotalTime());
            json.put("wakeUp", user.getWakeUp());
            json.put("wakeUp", user.isLoginnotifyStatus());

            System.out.println(json);
            /*String lastLoginDate = saveSupport.getFileLastModified(filename);
            api.sendPrivateMessage(e.getInvokerId(),
                    "\n Name: " + user.getuName()
                            + "\n Identifier: " + user.getuUnicID()
                            + "\n TotalTime: " + user.getTotalTimeStringNoCalc()
                            + "\n LastLogin: " + lastLoginDate
            );*/
        }

    }
}