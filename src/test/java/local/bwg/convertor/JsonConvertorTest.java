package local.bwg.convertor;

import local.bwg.model.TeamspeakUser;
import local.bwg.support.FileReaderWriterExp;
import local.bwg.support.TelegramUserSaver;
import local.bwg.model.TelegramUser;
import org.junit.jupiter.api.Test;

public class JsonConvertorTest {

    @Test
    public void convert() {
        JsonConvertor jsonConvertor = new JsonConvertor();
        jsonConvertor.convert(new TeamspeakUser(), null, "udata-json/", new FileReaderWriterExp());
        jsonConvertor.convert(new TelegramUser("255397596"), null, "udata_tg-json/", new TelegramUserSaver());
    }
}