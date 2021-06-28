package local.bwg.support;

import local.bwg.model.TeamspeakUser;
import org.junit.jupiter.api.Test;

public class FileReaderWriterExpTest {

    @Test
    public void saveFromJson() {
        TeamspeakUser user = new FileReaderWriterExp().loadJson("QYYAhV7Xy77zhOsV6g4iNGfNZIQ=");
        new FileReaderWriterExp().saveJson(user);
    }

    @Test
    public void loadFromJson() {
        new FileReaderWriterExp()
                .getAllFilesName("udata\\udata-json\\")
                .forEach(file -> {
            new FileReaderWriterExp().loadJson(file);
        });
    }

}