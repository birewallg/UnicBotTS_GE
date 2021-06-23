package local.bwg.support;

import local.bwg.model.TelegramUser;
import org.junit.jupiter.api.Test;

public class TelegramUserSaverTest {

    @Test
    public void saveInJson() {
        TelegramUser user = (TelegramUser) new TelegramUserSaver().loadJson("255397596");
        new TelegramUserSaver().saveJson(user);
    }

    @Test
    public void loadFromJson() {
        new TelegramUserSaver()
                .getAllFilesName("udata_tg-json")
                .forEach(file -> {
            new TelegramUserSaver().loadJson(file);
        });
    }
}