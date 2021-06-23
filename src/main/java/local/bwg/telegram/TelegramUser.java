package local.bwg.telegram;

import local.bwg.User;
import local.bwg.model.InterfaceUser;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.SaveSupport;
import local.bwg.support.TelegramUserSaver;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

public class TelegramUser implements Serializable, InterfaceUser {
    private static final long serialVersionUID = -3295590026052296594L;

    private static final Logger logger = Logger.getLogger(TelegramUser.class.getName());

    private String uID;
    private boolean subscribe = false;

    @Override
    public boolean loadFromSirializeble(String source) {
        try {
            SaveSupport saveSupport = new TelegramUserSaver();
            TelegramUser user = (TelegramUser) saveSupport.load(source);
            this.uID = user.uID;
            this.subscribe = user.subscribe;
            return true;
        } catch (Exception ignore) {
            logger.info("LoadFromSirializeble Error: " + source);
            return false;
        }
    }

    public TelegramUser(String id){
        this.uID = id;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public String getuID() {
        return uID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramUser that = (TelegramUser) o;
        return Objects.equals(uID, that.uID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uID);
    }
}
