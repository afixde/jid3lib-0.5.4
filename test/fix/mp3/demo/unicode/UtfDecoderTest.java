package fix.mp3.demo.unicode;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class UtfDecoderTest extends TestCase {

	@Test
	public void testDirList() throws FileNotFoundException {
		String fromDirStr = "v:/Studio/Usb_003/02.Chanson";
		List<String> dirList = UtfDecoder.getDirList(fromDirStr);
		assertTrue(dirList.size() > 0);
	}

	@Test
	public void testFileList() throws FileNotFoundException {
		String fromDirStr = "d:/Studio/Musik/_Stream/Audials/Work/ru";
		List<String> fileList = UtfDecoder.getFileList(fromDirStr);
		for (String line : fileList) {
			System.out.println(line);
		}
	}
}
