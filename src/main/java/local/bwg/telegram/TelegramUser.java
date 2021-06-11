package local.bwg.telegram;

import local.bwg.model.InterfaceUser;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class TelegramUser implements Serializable, InterfaceUser {
    private static final long serialVersionUID = -3295590026052296594L;

    private String uID;
    private boolean subscribe = false;


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

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public boolean loadFromSirializeble(String source) {
        return false;
    }
}
