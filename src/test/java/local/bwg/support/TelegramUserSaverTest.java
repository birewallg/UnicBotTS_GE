package local.bwg.support;

import local.bwg.telegram.TelegramUser;
import org.junit.jupiter.api.Test;

public class TelegramUserSaverTest {

    @Test
    public void saveInJson() {
        new TelegramUserSaver().loadFromJson("255397596");
    }

    @Test
    public void loadFromJson() {
        TelegramUser user = (TelegramUser) new TelegramUserSaver().loadFromJson("255397596");
        new TelegramUserSaver().saveInJson(user);
    }
}