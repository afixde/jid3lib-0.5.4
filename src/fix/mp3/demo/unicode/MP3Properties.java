package fix.mp3.demo.unicode;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

public class MP3Properties {
    File m_file = null;
    MP3File m_mp3file = null;
    public MP3Properties() {
    }
    public MP3Properties(File file) {
        m_file = file;
        try {
            m_mp3file = new MP3File(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public long getLength() {
        // TODO Auto-generated method stub
        if (m_file == null) return 0L;
        return m_file.length();
    }
}
