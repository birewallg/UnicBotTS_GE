package local.bwg.telegram;

import java.util.Objects;

public class TelegramUser {
    private String uID;

    TelegramUser(String id){
        this.uID = id;
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
