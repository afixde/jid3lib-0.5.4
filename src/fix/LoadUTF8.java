package fix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LoadUTF8 {

	private static String _FILE = null;

	public static void main(String args[]) {

		_FILE = args[0];

		try {
			System.out.println(getUTF8());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// loadUTF8Data();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getUTF8() {
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(_FILE);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			// InputStreamReader isr = new InputStreamReader(fis, "Cp1252");
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void loadUTF8Data() {
		try {
			InputStream in = new FileInputStream(_FILE);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));

			String line = null;
			StringBuffer buffer = new StringBuffer();

			while ((line = reader.readLine()) != null) // Read line until EOF
			{
				buffer.append(line + "\n");
			}
			System.out.println("" + buffer.toString() + "");
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
