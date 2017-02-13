package fix;

public class TestMp3 {
	public TestMp3() {
		;
	}
	protected static String RuDeConverter(String instr) {
		//
		StringBuffer outbuf = new StringBuffer();
		StringBuffer inbuf = new StringBuffer(instr);

		for (int i = 0; i < inbuf.length(); i++) {
			switch (inbuf.charAt(i)) {
				case '!':
					outbuf.append('M');
					break;
				case 'a':
					outbuf.append('a');
					break;
				case '?':
					outbuf.append('m');
					break;
				case '0':
				case '1':
				default:
					outbuf.append(inbuf.charAt(i));
					break;
			}
		}

		return outbuf.toString();
	}
	public void main() {
//		InputStreamReader isr = new InputStreamReader(fis, "Cp1252");


	}
}
