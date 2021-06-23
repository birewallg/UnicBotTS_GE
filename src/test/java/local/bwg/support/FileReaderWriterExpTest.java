package local.bwg.support;

import local.bwg.User;
import org.junit.jupiter.api.Test;

public class FileReaderWriterExpTest {
    @Test
    public void loadFromJson() {
        new FileReaderWriterExp().loadFromJson("QYYAhV7Xy77zhOsV6g4iNGfNZIQ=");
    }

    @Test
    public void saveFromJson() {
        User user = new FileReaderWriterExp().loadFromJson("QYYAhV7Xy77zhOsV6g4iNGfNZIQ=");
        new FileReaderWriterExp().saveInJson(user);
    }
}