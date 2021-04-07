package local.bwg.support;

import java.io.IOException;
import java.util.Objects;

@Deprecated
public class WinAPISupport {

    static private String[] radioList = new String[]
            {
                    "Epic Rock Radio",
                    "Radio Bloodstream",
                    "PONdENDS.COM | RADIO | REAL YAAD VYBEZ",
                    "Harlex Music",
                    "Epic Rock Radio"
            };
    public static boolean exRestartFix(String... params) {
        try {
            Runtime.getRuntime().exec("cmd /c \"C:\\App\\Radio Bloodstream.m3u\"");
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean exRestart(String... params) {
        try {
            //Runtime.getRuntime().exec("cmd /c C:\\App\\ts3c.lnk \"ts3server://10.0.0.63?nickname=Radio&channel=[spacer]RadioChannel\"");
            Runtime.getRuntime().exec("cmd /c \"shutdown /r /t 0 /f\"");
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    static private String getTrackNumber(String st){
        for (int i = 0; i < radioList.length - 1; i++){
            if (Objects.equals(st, radioList[i]))
                return String.valueOf(i);
        }
        return "0";
    }
    public static boolean exNext(String params) {
        String[] par = params.split(" ");

        String nextStation = "";
        String station;

        try {
            byte number = Byte.parseByte(par[par.length-1]);
            station = radioList[number - 1];
            station = getTrackNumber(station);
            System.out.println("cmd /c C:\\app\\" + station + ".m3u");
            Runtime.getRuntime().exec("cmd /c \"C:\\App\\" + station + ".m3u\"");
            return true;
        } catch (Exception e) {
            station = VLCSupport.GetStationName();
        }
        System.out.println(station);

        for (String radio : radioList){
            if (!Objects.equals(nextStation, "")) {
                nextStation = radio;
                break;
            }
            if(Objects.equals(radio, station))
                nextStation = radio;
        }
        if (Objects.equals(nextStation, "") || Objects.equals(nextStation, "unknown"))
            nextStation = radioList[0];
        try {
            nextStation = getTrackNumber(nextStation);
            System.out.println("cmd /c C:\\app\\" + nextStation + ".m3u");
            Runtime.getRuntime().exec("cmd /c \"C:\\App\\" + nextStation + ".m3u\"");
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean exCMD(String params) {
        try {
            Runtime.getRuntime().exec("cmd" + params);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean destroy(String... params) {
        return true;
    }
}