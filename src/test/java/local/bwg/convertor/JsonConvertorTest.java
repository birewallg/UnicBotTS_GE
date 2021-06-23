package local.bwg.convertor;

import local.bwg.User;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.TelegramUserSaver;
import local.bwg.telegram.TelegramUser;
import org.junit.jupiter.api.Test;

public class JsonConvertorTest {

    @Test
    public void convert() {
        JsonConvertor jsonConvertor = new JsonConvertor();
        jsonConvertor.convert(new User(), null, "udata-json/", new FileReaderWriterExp());
        jsonConvertor.convert(new TelegramUser("255397596"), null, "udata_tg-json/", new TelegramUserSaver());
    }
}