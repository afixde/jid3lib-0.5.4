package fix.mp3.demo.unicode;

//package java.net;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

// Author: Patrick Hargitt
// Upstream S.A.
/**
 * @author u771512
 *
 */
@SuppressWarnings("unused")
public class AnsiDecoder {
	private static Map<String, String> entities;
	private static Map<Integer, String> ruLatEntitiesUtf8;
	private static Map<Integer, String> ruLatEntitiesCp1251;
	private static Map<Integer, String> ruLatEntitiesCp866;
	private static Map<Integer, String> ruLatEntitiesCp850;
	private static Map<String, String> encodingEntities;

	private static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	// -------------------------- STATIC METHODS --------------------------

	/**
	 * Fast convert a byte array to a hex string with possible leading zero.
	 *
	 * @param b
	 *            array of bytes you want to convert to hex.
	 * @param length
	 *            how many bytes you wanted convented to byte.
	 *
	 * @return hex equivalent of the bytes
	 */
	public static String toHexString(byte[] b, int length) {
		StringBuilder sb = new StringBuilder(length * 3);
		for (int i = 0; i < length; i++) {
			if (i != 0) {
				sb.append(' ');
			}
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Fast convert a String to a hex string with possible leading zero.
	 *
	 * @param b
	 *            array of chars you want to convert to hex.
	 * @param length
	 *            how many chars you wanted convented to byte.
	 *
	 * @return hex equivalent of the bytes
	 */
	public static String toHexString(String b, int length) {
		StringBuilder sb = new StringBuilder(length * 5);
		for (int i = 0; i < length; i++) {
			if (i != 0) {
				sb.append(' ');
			}
			// work high order to low
			final int c = b.charAt(i);
			sb.append(hexChar[(c & 0xf000) >>> 12]);
			sb.append(hexChar[(c & 0xf00) >>> 8]);
			sb.append(hexChar[(c & 0xf0) >>> 4]);
			sb.append(hexChar[c & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Fast convert a String to a int array with possible leading zero.
	 *
	 * @param b
	 *            array of chars you want to convert to hex.
	 * @param length
	 *            how many chars you wanted convented to byte.
	 *
	 * @return hex equivalent of the bytes
	 */
	public static int[] toHexIntArray(String b, int length) {
		int[] retval = new int[length];
		for (int i = 0; i < length; i++) {
			// String sb = "\\u";
			String sb = "";
			// work high order to low
			final int c = b.charAt(i);
			sb += hexChar[(c & 0xf000) >>> 12];
			sb += hexChar[(c & 0xf00) >>> 8];
			sb += hexChar[(c & 0xf0) >>> 4];
			sb += hexChar[c & 0x0f];
			retval[i] = Integer.parseInt(sb, 16);
		}
		return retval;
	}

	// -------------------------- ENCODING METHODS --------------------------

	private synchronized static Map<String, String> getEncodingEntities() {
		if (encodingEntities == null) {
			encodingEntities = new Hashtable<String, String>();
			String encodings[][] = { { "ASCII", "American Standard Code for Information Interchange " },
					{ "Cp1252", "Windows Latin-1 " }, { "Cp1251", "Windows Cyrillic " },
					{ "ISO-10646-UTF-1", "ISO-10646-UTF-1" },
					{ "UTF-16",
							"Sixteen-bit Unicode Transformation Format, byte order specified by a mandatory initial byte-order mark " },
					{ "ISO-8859-1", "ISO 8859-1, Latin alphabet No. 1 " },
					{ "UnicodeBig",
							"Sixteen-bit Unicode Transformation Format, big-endian byte order, with byte-order mark " },
					{ "UnicodeBigUnmarked", "Sixteen-bit Unicode Transformation Format, big-endian byte order " },
					{ "UnicodeLittle",
							"Sixteen-bit Unicode Transformation Format, little-endian byte order, with byte-order mark " },
					{ "UnicodeLittleUnmarked", "Sixteen-bit Unicode Transformation Format, little-endian byte order " },
					{ "UTF8", "Eight-bit unicode Transformation Format " }, { "Big5", "Big5, Traditional Chinese " },
					{ "UTF-8", "Eight-bit unicode Transformation Format " }, { "Big5", "Big5, Traditional Chinese " },
					{ "Big5_HKSCS", "Big5 with Hong Kong extensions, Traditional Chinese " },
					{ "Cp037", "USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia " },
					{ "Cp273", "IBM Austria, Germany " }, { "Cp277", "IBM Denmark, Norway " },
					{ "Cp278", "IBM Finland, Sweden " }, { "Cp280", "IBM Italy " },
					{ "Cp284", "IBM Catalan/Spain, Spanish Latin America " },
					{ "Cp285", "IBM United Kingdom, Ireland " }, { "Cp297", "IBM France " }, { "Cp420", "IBM Arabic " },
					{ "Cp424", "IBM Hebrew " },
					{ "Cp437", "MS-DOS United States, Australia, New Zealand, South Africa " },
					{ "Cp500", "EBCDIC 500V1 " }, { "Cp737", "PC Greek " }, { "Cp775", "PC Baltic " },
					{ "Cp838", "IBM Thailand extended SBCS " }, { "Cp850", "MS-DOS Latin-1 " },
					{ "Cp852", "MS-DOS Latin-2 " }, { "Cp855", "IBM Cyrillic " }, { "Cp856", "IBM Hebrew " },
					{ "Cp857", "IBM Turkish " }, { "Cp858", "Variant of Cp850 with Euro character " },
					{ "Cp860", "MS-DOS Portuguese " }, { "Cp861", "MS-DOS Icelandic " }, { "Cp862", "PC Hebrew " },
					{ "Cp863", "MS-DOS Canadian French " }, { "Cp864", "PC Arabic " }, { "Cp865", "MS-DOS Nordic " },
					{ "Cp866", "MS-DOS Russian " }, { "Cp868", "MS-DOS Pakistan " }, { "Cp869", "IBM Modern Greek " },
					{ "Cp870", "IBM Multilingual Latin-2 " }, { "Cp871", "IBM Iceland " }, { "Cp874", "IBM Thai " },
					{ "Cp875", "IBM Greek " }, { "Cp918", "IBM Pakistan (Urdu) " },
					{ "Cp921", "IBM Latvia, Lithuania (AIX, DOS) " }, { "Cp922", "IBM Estonia (AIX, DOS) " },
					{ "Cp930", "Japanese Katakana-Kanji mixed with 4370 UDC, superset of 5026 " },
					{ "Cp933", "Korean Mixed with 1880 UDC, superset of 5029 " },
					{ "Cp935", "Simplified Chinese Host mixed with 1880 UDC, superset of 5031 " },
					{ "Cp937", "Traditional Chinese Host miexed with 6204 UDC, superset of 5033 " },
					{ "Cp939", "Japanese Latin Kanji mixed with 4370 UDC, superset of 5035 " },
					{ "Cp942", "IBM OS/2 Japanese, superset of Cp932 " }, { "Cp942C", "Variant of Cp942 " },
					{ "Cp943", "IBM OS/2 Japanese, superset of Cp932 and Shift-JIS " },
					{ "Cp943C", "Variant of Cp943 " }, { "Cp948", "OS/2 Chinese (Taiwan) superset of 938 " },
					{ "Cp949", "PC Korean " }, { "Cp949C", "Variant of Cp949 " },
					{ "Cp950", "PC Chinese (Hong Kong, Taiwan) " }, { "Cp964", "AIX Chinese (Taiwan) " },
					{ "Cp970", "AIX Korean " }, { "Cp1006", "IBM AIX Pakistan (Urdu) " },
					{ "Cp1025", "IBM Multilingual Cyrillic: Bulgaria, Bosnia, Herzegovinia, Macedonia (FYR) " },
					{ "Cp1026", "IBM Latin-5, Turkey " }, { "Cp1046", "IBM Arabic - Windows " },
					{ "Cp1097", "IBM Iran (Farsi)/Persian " }, { "Cp1098", "IBM Iran (Farsi)/Persian (PC) " },
					{ "Cp1112", "IBM Latvia, Lithuania " }, { "Cp1122", "IBM Estonia " }, { "Cp1123", "IBM Ukraine " },
					{ "Cp1124", "IBM AIX Ukraine " }, { "Cp1140", "Variant of Cp037 with Euro character " },
					{ "Cp1141", "Variant of Cp273 with Euro character " },
					{ "Cp1142", "Variant of Cp277 with Euro character " },
					{ "Cp1143", "Variant of Cp278 with Euro character " },
					{ "Cp1144", "Variant of Cp280 with Euro character " },
					{ "Cp1145", "Variant of Cp284 with Euro character " },
					{ "Cp1146", "Variant of Cp285 with Euro character " },
					{ "Cp1147", "Variant of Cp297 with Euro character " },
					{ "Cp1148", "Variant of Cp500 with Euro character " },
					{ "Cp1149", "Variant of Cp871 with Euro character " }, { "Cp1250", "Windows Eastern European " },
					{ "Cp1253", "Windows Greek " }, { "Cp1254", "Windows Turkish " }, { "Cp1255", "Windows Hebrew " },
					{ "Cp1256", "Windows Arabic " }, { "Cp1257", "Windows Baltic " },
					{ "Cp1258", "Windows Vietnamese " },
					{ "Cp1381", "IBM OS/2, DOS People's Republic of China (PRC) " },
					{ "Cp1383", "IBM AIX People's Republic of China (PRC) " },
					{ "Cp33722", "IBM-eucJP - Japanese (superset of 5050) " },
					{ "EUC_CN", "GB2312, EUC encoding, Simplified Chinese " },
					{ "EUC_JP", "JIS X 0201, 0208, 0212, EUC encoding, Japanese " },
					{ "EUC_JP_LINUX", "JIS X 0201, 0208, EUC encoding, Japanese " },
					{ "EUC_KR", "KS C 5601, EUC encoding, Korean " },
					{ "EUC_TW", "CNS11643 (Plane 1-3), EUC encoding, Traditional Chinese " },
					{ "GBK", "GBK, Simplified Chinese " },
					// { "ISO2022CN","ISO 2022 CN, Chinese" },
					// { "ISO2022CN_CNS","CNS 11643 in ISO 2022 CN form,
					// Traditional Chinese" },
					// { "ISO2022CN_GB","GB 2312 in ISO 2022 CN form, Simplified
					// Chinese" },
					// { "ISO2022JP","JIS X 0201, 0208 in ISO 2022 form,
					// Japanese " },
					// { "ISO2022KR","ISO 2022 KR, Korean " },
					{ "ISO8859_2", "ISO 8859-2, Latin alphabet No. 2 " },
					{ "ISO8859_3", "ISO 8859-3, Latin alphabet No. 3 " },
					{ "ISO8859_4", "ISO 8859-4, Latin alphabet No. 4 " },
					{ "ISO8859_5", "ISO 8859-5, Latin/Cyrillic alphabet " },
					{ "ISO8859_6", "ISO 8859-6, Latin/Arabic alphabet " },
					{ "ISO8859_7", "ISO 8859-7, Latin/Greek alphabet " },
					{ "ISO8859_8", "ISO 8859-8, Latin/Hebrew alphabet " },
					{ "ISO8859_9", "ISO 8859-9, Latin alphabet No. 5 " },
					{ "ISO8859_13", "ISO 8859-13, Latin alphabet No. 7 " },
					{ "ISO8859_15_FDIS", "ISO 8859-15, Latin alphabet No. 9 " }, { "JIS0201", "JIS X 0201, Japanese " },
					{ "JIS0208", "JIS X 0208, Japanese " }, { "JIS0212", "JIS X 0212, Japanese " },
					{ "JISAutoDetect", "Detects and converts from Shift-JIS, EUC-JP, ISO 2022 JP" },
					{ "Johab", "Johab, Korean " }, { "KOI8_R", "KOI8-R, Russian " }, { "MS874", "Windows Thai " },
					{ "MS932", "Windows Japanese " }, { "MS936", "Windows Simplified Chinese " },
					{ "MS949", "Windows Korean " }, { "MS950", "Windows Traditional Chinese " },
					{ "MacArabic", "Macintosh Arabic " }, { "MacCentralEurope", "Macintosh Latin-2 " },
					{ "MacCroatian", "Macintosh Croatian " }, { "MacCyrillic", "Macintosh Cyrillic " },
					{ "MacDingbat", "Macintosh Dingbat " }, { "MacGreek", "Macintosh Greek " },
					{ "MacHebrew", "Macintosh Hebrew " }, { "MacIceland", "Macintosh Iceland " },
					{ "MacRoman", "Macintosh Roman " }, { "MacRomania", "Macintosh Romania " },
					{ "MacSymbol", "Macintosh Symbol " }, { "MacThai", "Macintosh Thai " },
					{ "MacTurkish", "Macintosh Turkish " }, { "MacUkraine", "Macintosh Ukraine " },
					{ "SJIS", "Shift-JIS, Japanese " }, { "TIS620", "TIS620, Thai " }, };
			for (int i = 0; i < encodings.length; i++) {
				encodingEntities.put(encodings[i][0], encodings[i][1]);
			}
		}
		return encodingEntities;
	}

	// private synchronized static Map<Integer, String> getRuLatEntities()
	// {
	// return getRuLatEntities(null);
	// }
	private synchronized static Map<Integer, String> getRuLatEntities(String charSet) {
		if (charSet != null && charSet.startsWith("Cp8")) {
			return getRuLatEntitiesCp866();
		}
		else if (charSet != null && charSet.matches("UTF.*8")) {
			return getRuLatEntitiesUtf8();
		}
		return getRuLatEntitiesCp1251();
	}

	private synchronized static Map<Integer, String> getRuLatEntitiesCp1251() {
		if (ruLatEntitiesCp1251 == null) {
			ruLatEntitiesCp1251 = new Hashtable<Integer, String>();
			ruLatEntitiesCp1251.put(0x80, "\'");
			ruLatEntitiesCp1251.put(0x0402, "\'");
			ruLatEntitiesCp1251.put(0x81, "G");
			ruLatEntitiesCp1251.put(0x0403, "G");
			ruLatEntitiesCp1251.put(0x82, ",");
			ruLatEntitiesCp1251.put(0x201a, ",");
			ruLatEntitiesCp1251.put(0x88, "€");
			ruLatEntitiesCp1251.put(0x20ac, "€");
			ruLatEntitiesCp1251.put(0x8a, "L");
			ruLatEntitiesCp1251.put(0x0409, "L");
			ruLatEntitiesCp1251.put(0x8c, "N");
			ruLatEntitiesCp1251.put(0x040a, "N");
			ruLatEntitiesCp1251.put(0x8d, "K");
			ruLatEntitiesCp1251.put(0x040c, "K");
			ruLatEntitiesCp1251.put(0x8e, "T");
			ruLatEntitiesCp1251.put(0x040b, "T");
			ruLatEntitiesCp1251.put(0x8f, "Z");
			ruLatEntitiesCp1251.put(0x040f, "Z");

			ruLatEntitiesCp1251.put(0x90, "\'");
			ruLatEntitiesCp1251.put(0x0452, "\'");
			ruLatEntitiesCp1251.put(0x9a, "l");
			ruLatEntitiesCp1251.put(0x0453, "l");
			ruLatEntitiesCp1251.put(0x9c, "n");
			ruLatEntitiesCp1251.put(0x045a, "n");
			ruLatEntitiesCp1251.put(0x9d, "k");
			ruLatEntitiesCp1251.put(0x045c, "k");
			ruLatEntitiesCp1251.put(0x9e, "t");
			ruLatEntitiesCp1251.put(0x045b, "t");
			ruLatEntitiesCp1251.put(0x9f, "z");
			ruLatEntitiesCp1251.put(0x045f, "z");

			ruLatEntitiesCp1251.put(0xb2, "I");
			ruLatEntitiesCp1251.put(0x0406, "I");
			ruLatEntitiesCp1251.put(0xb3, "i");
			ruLatEntitiesCp1251.put(0x0456, "i");
			ruLatEntitiesCp1251.put(0xb4, "G");
			ruLatEntitiesCp1251.put(0x0431, "G");
			ruLatEntitiesCp1251.put(0xb8, "oe");
			ruLatEntitiesCp1251.put(0x0451, "oe");
			ruLatEntitiesCp1251.put(0xb9, "#");
			ruLatEntitiesCp1251.put(0x00a3, "#");
			ruLatEntitiesCp1251.put(0xba, "e");
			ruLatEntitiesCp1251.put(0x0454, "e");
			ruLatEntitiesCp1251.put(0xbc, "j");
			ruLatEntitiesCp1251.put(0x0458, "j");
			ruLatEntitiesCp1251.put(0xbd, "S");
			ruLatEntitiesCp1251.put(0x0405, "S");
			ruLatEntitiesCp1251.put(0xbe, "s");
			ruLatEntitiesCp1251.put(0x0455, "s");
			ruLatEntitiesCp1251.put(0xbf, "i");
			ruLatEntitiesCp1251.put(0x0457, "i");

			ruLatEntitiesCp1251.put(0xc0, "A");
			ruLatEntitiesCp1251.put(0xc1, "B");
			ruLatEntitiesCp1251.put(0xc2, "V");
			ruLatEntitiesCp1251.put(0xc3, "G");
			ruLatEntitiesCp1251.put(0xc4, "D");
			ruLatEntitiesCp1251.put(0xc5, "E");
			ruLatEntitiesCp1251.put(0xc6, "Zh");
			ruLatEntitiesCp1251.put(0xc7, "Z");
			ruLatEntitiesCp1251.put(0xc8, "I");
			ruLatEntitiesCp1251.put(0xc9, "J");
			ruLatEntitiesCp1251.put(0xca, "K");
			ruLatEntitiesCp1251.put(0xcb, "L");
			ruLatEntitiesCp1251.put(0xcc, "M");
			ruLatEntitiesCp1251.put(0xcd, "N");
			ruLatEntitiesCp1251.put(0xce, "O");
			ruLatEntitiesCp1251.put(0xcf, "P");

			ruLatEntitiesCp1251.put(0xd0, "R");
			ruLatEntitiesCp1251.put(0xd1, "S");
			ruLatEntitiesCp1251.put(0xd2, "T");
			ruLatEntitiesCp1251.put(0xd3, "U");
			ruLatEntitiesCp1251.put(0xd4, "F");
			ruLatEntitiesCp1251.put(0xd5, "Kh");
			ruLatEntitiesCp1251.put(0xd6, "C");
			ruLatEntitiesCp1251.put(0xd7, "Ch");
			ruLatEntitiesCp1251.put(0xd8, "Sh");
			ruLatEntitiesCp1251.put(0xd9, "Sch");
			ruLatEntitiesCp1251.put(0xda, "'");
			ruLatEntitiesCp1251.put(0xdb, "Y");
			ruLatEntitiesCp1251.put(0xdc, "'");
			ruLatEntitiesCp1251.put(0xdd, "Ae");
			ruLatEntitiesCp1251.put(0xde, "Yu");
			ruLatEntitiesCp1251.put(0xdf, "Ya");

			ruLatEntitiesCp1251.put(0xe0, "a");
			ruLatEntitiesCp1251.put(0xe1, "b");
			ruLatEntitiesCp1251.put(0xe2, "v");
			ruLatEntitiesCp1251.put(0xe3, "g");
			ruLatEntitiesCp1251.put(0xe4, "d");
			ruLatEntitiesCp1251.put(0xe5, "e");
			ruLatEntitiesCp1251.put(0xe6, "zh");
			ruLatEntitiesCp1251.put(0xe7, "z");
			ruLatEntitiesCp1251.put(0xe8, "i");
			ruLatEntitiesCp1251.put(0xe9, "j");
			ruLatEntitiesCp1251.put(0xea, "k");
			ruLatEntitiesCp1251.put(0xeb, "l");
			ruLatEntitiesCp1251.put(0xec, "m");
			ruLatEntitiesCp1251.put(0xed, "n");
			ruLatEntitiesCp1251.put(0xee, "o");
			ruLatEntitiesCp1251.put(0xef, "p");

			ruLatEntitiesCp1251.put(0xf0, "r");
			ruLatEntitiesCp1251.put(0xf1, "s");
			ruLatEntitiesCp1251.put(0xf2, "t");
			ruLatEntitiesCp1251.put(0xf3, "u");
			ruLatEntitiesCp1251.put(0xf4, "f");
			ruLatEntitiesCp1251.put(0xf5, "kh");
			ruLatEntitiesCp1251.put(0xf6, "c");
			ruLatEntitiesCp1251.put(0xf7, "ch");
			ruLatEntitiesCp1251.put(0xf8, "sh");
			ruLatEntitiesCp1251.put(0xf9, "sch");
			ruLatEntitiesCp1251.put(0xfa, "'");
			ruLatEntitiesCp1251.put(0xfb, "y");
			ruLatEntitiesCp1251.put(0xfc, "'");
			ruLatEntitiesCp1251.put(0xfd, "ae");
			ruLatEntitiesCp1251.put(0xfe, "yu");
			ruLatEntitiesCp1251.put(0xff, "ya");

			ruLatEntitiesCp1251.put(0x0410, "A");
			ruLatEntitiesCp1251.put(0x0411, "B");
			ruLatEntitiesCp1251.put(0x0412, "V");
			ruLatEntitiesCp1251.put(0x0413, "G");
			ruLatEntitiesCp1251.put(0x0414, "D");
			ruLatEntitiesCp1251.put(0x0415, "E");
			ruLatEntitiesCp1251.put(0x0416, "Zh");
			ruLatEntitiesCp1251.put(0x0417, "Z");
			ruLatEntitiesCp1251.put(0x0418, "I");
			ruLatEntitiesCp1251.put(0x0419, "J");
			ruLatEntitiesCp1251.put(0x041a, "K");
			ruLatEntitiesCp1251.put(0x041b, "L");
			ruLatEntitiesCp1251.put(0x041c, "M");
			ruLatEntitiesCp1251.put(0x041d, "N");
			ruLatEntitiesCp1251.put(0x041e, "O");
			ruLatEntitiesCp1251.put(0x041f, "P");

			ruLatEntitiesCp1251.put(0x0420, "R");
			ruLatEntitiesCp1251.put(0x0421, "S");
			ruLatEntitiesCp1251.put(0x0422, "T");
			ruLatEntitiesCp1251.put(0x0423, "U");
			ruLatEntitiesCp1251.put(0x0424, "F");
			ruLatEntitiesCp1251.put(0x0425, "Kh");
			ruLatEntitiesCp1251.put(0x0426, "C");
			ruLatEntitiesCp1251.put(0x0427, "Ch");
			ruLatEntitiesCp1251.put(0x0428, "Sh");
			ruLatEntitiesCp1251.put(0x0429, "Sch");
			ruLatEntitiesCp1251.put(0x042a, "'");
			ruLatEntitiesCp1251.put(0x042b, "Y");
			ruLatEntitiesCp1251.put(0x042c, "'");
			ruLatEntitiesCp1251.put(0x042d, "Ae");
			ruLatEntitiesCp1251.put(0x042e, "Yu");
			ruLatEntitiesCp1251.put(0x042f, "Ya");

			ruLatEntitiesCp1251.put(0x0430, "a");
			ruLatEntitiesCp1251.put(0x0431, "b");
			ruLatEntitiesCp1251.put(0x0432, "v");
			ruLatEntitiesCp1251.put(0x0433, "g");
			ruLatEntitiesCp1251.put(0x0434, "d");
			ruLatEntitiesCp1251.put(0x0435, "e");
			ruLatEntitiesCp1251.put(0x0436, "zh");
			ruLatEntitiesCp1251.put(0x0437, "z");
			ruLatEntitiesCp1251.put(0x0438, "i");
			ruLatEntitiesCp1251.put(0x0439, "j");
			ruLatEntitiesCp1251.put(0x043a, "k");
			ruLatEntitiesCp1251.put(0x043b, "l");
			ruLatEntitiesCp1251.put(0x043c, "m");
			ruLatEntitiesCp1251.put(0x043d, "n");
			ruLatEntitiesCp1251.put(0x043e, "o");
			ruLatEntitiesCp1251.put(0x043f, "p");

			ruLatEntitiesCp1251.put(0x0440, "r");
			ruLatEntitiesCp1251.put(0x0441, "s");
			ruLatEntitiesCp1251.put(0x0442, "t");
			ruLatEntitiesCp1251.put(0x0443, "u");
			ruLatEntitiesCp1251.put(0x0444, "f");
			ruLatEntitiesCp1251.put(0x0445, "kh");
			ruLatEntitiesCp1251.put(0x0446, "c");
			ruLatEntitiesCp1251.put(0x0447, "ch");
			ruLatEntitiesCp1251.put(0x0448, "sh");
			ruLatEntitiesCp1251.put(0x0449, "sch");
			ruLatEntitiesCp1251.put(0x044a, "'");
			ruLatEntitiesCp1251.put(0x044b, "y");
			ruLatEntitiesCp1251.put(0x044c, "'");
			ruLatEntitiesCp1251.put(0x044d, "ae");
			ruLatEntitiesCp1251.put(0x044e, "yu");
			ruLatEntitiesCp1251.put(0x044f, "ya");

		}
		return ruLatEntitiesCp1251;
	}

	private synchronized static Map<Integer, String> getRuLatEntitiesCp866() {
		if (ruLatEntitiesCp866 == null) {
			ruLatEntitiesCp866 = new Hashtable<Integer, String>();
			ruLatEntitiesCp866.put(0x80, "A");
			ruLatEntitiesCp866.put(0x0410, "A");
			ruLatEntitiesCp866.put(0x81, "B");
			ruLatEntitiesCp866.put(0x0411, "B");
			ruLatEntitiesCp866.put(0x82, "V");
			ruLatEntitiesCp866.put(0x0412, "V");
			ruLatEntitiesCp866.put(0x83, "G");
			ruLatEntitiesCp866.put(0x0413, "G");
			ruLatEntitiesCp866.put(0x84, "D");
			ruLatEntitiesCp866.put(0x0414, "D");
			ruLatEntitiesCp866.put(0x85, "E");
			ruLatEntitiesCp866.put(0x0415, "E");
			ruLatEntitiesCp866.put(0x86, "Zh");
			ruLatEntitiesCp866.put(0x0416, "Zh");
			ruLatEntitiesCp866.put(0x87, "Z");
			ruLatEntitiesCp866.put(0x0417, "Z");
			ruLatEntitiesCp866.put(0x88, "I");
			ruLatEntitiesCp866.put(0x0418, "I");
			ruLatEntitiesCp866.put(0x89, "J");
			ruLatEntitiesCp866.put(0x0419, "J");
			ruLatEntitiesCp866.put(0x8A, "K");
			ruLatEntitiesCp866.put(0x041A, "K");
			ruLatEntitiesCp866.put(0x8B, "L");
			ruLatEntitiesCp866.put(0x041B, "L");
			ruLatEntitiesCp866.put(0x8C, "M");
			ruLatEntitiesCp866.put(0x041C, "M");
			ruLatEntitiesCp866.put(0x8D, "N");
			ruLatEntitiesCp866.put(0x041D, "N");
			ruLatEntitiesCp866.put(0x8E, "O");
			ruLatEntitiesCp866.put(0x041E, "O");
			ruLatEntitiesCp866.put(0x8F, "P");
			ruLatEntitiesCp866.put(0x041F, "P");

			ruLatEntitiesCp866.put(0x90, "R");
			ruLatEntitiesCp866.put(0x0420, "R");
			ruLatEntitiesCp866.put(0x91, "S");
			ruLatEntitiesCp866.put(0x0421, "S");
			ruLatEntitiesCp866.put(0x92, "T");
			ruLatEntitiesCp866.put(0x0422, "T");
			ruLatEntitiesCp866.put(0x93, "U");
			ruLatEntitiesCp866.put(0x0423, "U");
			ruLatEntitiesCp866.put(0x94, "F");
			ruLatEntitiesCp866.put(0x0424, "F");
			ruLatEntitiesCp866.put(0x95, "Kh");
			ruLatEntitiesCp866.put(0x0425, "Kh");
			ruLatEntitiesCp866.put(0x96, "C");
			ruLatEntitiesCp866.put(0x0426, "C");
			ruLatEntitiesCp866.put(0x97, "Ch");
			ruLatEntitiesCp866.put(0x0427, "Ch");
			ruLatEntitiesCp866.put(0x98, "Sh");
			ruLatEntitiesCp866.put(0x0428, "Sh");
			ruLatEntitiesCp866.put(0x99, "Sch");
			ruLatEntitiesCp866.put(0x0429, "Sch");
			ruLatEntitiesCp866.put(0x9A, "");
			ruLatEntitiesCp866.put(0x042A, "");
			ruLatEntitiesCp866.put(0x9B, "Y");
			ruLatEntitiesCp866.put(0x042B, "Y");
			ruLatEntitiesCp866.put(0x9C, "\'");
			ruLatEntitiesCp866.put(0x042C, "\'");
			ruLatEntitiesCp866.put(0x9D, "Ae");
			ruLatEntitiesCp866.put(0x042D, "Ae");
			ruLatEntitiesCp866.put(0x9E, "Yu");
			ruLatEntitiesCp866.put(0x042E, "Yu");
			ruLatEntitiesCp866.put(0x9F, "Ya");
			ruLatEntitiesCp866.put(0x042F, "Ya");

			ruLatEntitiesCp866.put(0xA0, "a");
			ruLatEntitiesCp866.put(0x0430, "a");
			ruLatEntitiesCp866.put(0xA1, "b");
			ruLatEntitiesCp866.put(0x0431, "b");
			ruLatEntitiesCp866.put(0xA2, "v");
			ruLatEntitiesCp866.put(0x0432, "v");
			ruLatEntitiesCp866.put(0xA3, "g");
			ruLatEntitiesCp866.put(0x0433, "g");
			ruLatEntitiesCp866.put(0xA4, "d");
			ruLatEntitiesCp866.put(0x0434, "d");
			ruLatEntitiesCp866.put(0xA5, "e");
			ruLatEntitiesCp866.put(0x0435, "e");
			ruLatEntitiesCp866.put(0xA6, "zh");
			ruLatEntitiesCp866.put(0x0436, "zh");
			ruLatEntitiesCp866.put(0xA7, "z");
			ruLatEntitiesCp866.put(0x0437, "z");
			ruLatEntitiesCp866.put(0xA8, "i");
			ruLatEntitiesCp866.put(0x0438, "i");
			ruLatEntitiesCp866.put(0xA9, "j");
			ruLatEntitiesCp866.put(0x0439, "j");
			ruLatEntitiesCp866.put(0xAA, "k");
			ruLatEntitiesCp866.put(0x043A, "k");
			ruLatEntitiesCp866.put(0xAB, "l");
			ruLatEntitiesCp866.put(0x043B, "l");
			ruLatEntitiesCp866.put(0xAC, "m");
			ruLatEntitiesCp866.put(0x043C, "m");
			ruLatEntitiesCp866.put(0xAD, "n");
			ruLatEntitiesCp866.put(0x043D, "n");
			ruLatEntitiesCp866.put(0xAE, "o");
			ruLatEntitiesCp866.put(0x043E, "o");
			ruLatEntitiesCp866.put(0xAF, "p");
			ruLatEntitiesCp866.put(0x043F, "p");

			ruLatEntitiesCp866.put(0xE0, "r");
			ruLatEntitiesCp866.put(0x0440, "r");
			ruLatEntitiesCp866.put(0xE1, "s");
			ruLatEntitiesCp866.put(0x0441, "s");
			ruLatEntitiesCp866.put(0xE2, "t");
			ruLatEntitiesCp866.put(0x0442, "t");
			ruLatEntitiesCp866.put(0xE3, "u");
			ruLatEntitiesCp866.put(0x0443, "u");
			ruLatEntitiesCp866.put(0xE4, "f");
			ruLatEntitiesCp866.put(0x0444, "f");
			ruLatEntitiesCp866.put(0xE5, "kh");
			ruLatEntitiesCp866.put(0x0445, "kh");
			ruLatEntitiesCp866.put(0xE6, "c");
			ruLatEntitiesCp866.put(0x0446, "c");
			ruLatEntitiesCp866.put(0xE7, "ch");
			ruLatEntitiesCp866.put(0x0447, "ch");
			ruLatEntitiesCp866.put(0xE8, "sh");
			ruLatEntitiesCp866.put(0x0448, "sh");
			ruLatEntitiesCp866.put(0xE9, "sch");
			ruLatEntitiesCp866.put(0x0449, "sch");
			ruLatEntitiesCp866.put(0xEA, "");
			ruLatEntitiesCp866.put(0x044A, "");
			ruLatEntitiesCp866.put(0xEB, "y");
			ruLatEntitiesCp866.put(0x044B, "y");
			ruLatEntitiesCp866.put(0xEC, "\'");
			ruLatEntitiesCp866.put(0x044C, "\'");
			ruLatEntitiesCp866.put(0xED, "ae");
			ruLatEntitiesCp866.put(0x044D, "ae");
			ruLatEntitiesCp866.put(0xEE, "yu");
			ruLatEntitiesCp866.put(0x044E, "yu");
			ruLatEntitiesCp866.put(0xEF, "ya");
			ruLatEntitiesCp866.put(0x044F, "ya");

			ruLatEntitiesCp866.put(0xF0, "Yo");
			ruLatEntitiesCp866.put(0x0401, "Yo");
			ruLatEntitiesCp866.put(0xF1, "yo");
			ruLatEntitiesCp866.put(0x0451, "yo");
			ruLatEntitiesCp866.put(0xF2, "Ae");
			ruLatEntitiesCp866.put(0x0404, "Ae");

		}
		return ruLatEntitiesCp866;
	}

	private synchronized static Map<Integer, String> getRuLatEntitiesUtf8() {
		if (ruLatEntitiesUtf8 == null) {
			ruLatEntitiesUtf8 = new Hashtable<Integer, String>();
			ruLatEntitiesUtf8.put(0xd090, "A");
			ruLatEntitiesUtf8.put(0xd091, "B");
			ruLatEntitiesUtf8.put(0xd092, "V");
			ruLatEntitiesUtf8.put(0xd093, "G");
			ruLatEntitiesUtf8.put(0xd094, "D");
			ruLatEntitiesUtf8.put(0xd095, "E");
			ruLatEntitiesUtf8.put(0xd096, "Zh");
			ruLatEntitiesUtf8.put(0xd097, "Z");
			ruLatEntitiesUtf8.put(0xd098, "I");
			ruLatEntitiesUtf8.put(0xd099, "J");
			ruLatEntitiesUtf8.put(0xd09a, "K");
			ruLatEntitiesUtf8.put(0xd09b, "L");
			ruLatEntitiesUtf8.put(0xd09c, "M");
			ruLatEntitiesUtf8.put(0xd09d, "N");
			ruLatEntitiesUtf8.put(0xd09e, "O");
			ruLatEntitiesUtf8.put(0xd09f, "P");

			ruLatEntitiesUtf8.put(0xd0a0, "R");
			ruLatEntitiesUtf8.put(0xd0a1, "S");
			ruLatEntitiesUtf8.put(0xd0a2, "T");
			ruLatEntitiesUtf8.put(0xd0a3, "U");
			ruLatEntitiesUtf8.put(0xd0a4, "F");
			ruLatEntitiesUtf8.put(0xd0a5, "Kh");
			ruLatEntitiesUtf8.put(0xd0a6, "C");
			ruLatEntitiesUtf8.put(0xd0a7, "Ch");
			ruLatEntitiesUtf8.put(0xd0a8, "Sh");
			ruLatEntitiesUtf8.put(0xd0a9, "Sch");
			ruLatEntitiesUtf8.put(0xd0aa, "");
			ruLatEntitiesUtf8.put(0xd0ab, "Y");
			ruLatEntitiesUtf8.put(0xd0ac, "\'");
			ruLatEntitiesUtf8.put(0xd0ad, "Ae");
			ruLatEntitiesUtf8.put(0xd0ae, "Yu");
			ruLatEntitiesUtf8.put(0xd0af, "Ya");

			ruLatEntitiesUtf8.put(0xd0b0, "a");
			ruLatEntitiesUtf8.put(0xd0b1, "b");
			ruLatEntitiesUtf8.put(0xd0b2, "v");
			ruLatEntitiesUtf8.put(0xd0b3, "g");
			ruLatEntitiesUtf8.put(0xd0b4, "d");
			ruLatEntitiesUtf8.put(0xd0b5, "e");
			ruLatEntitiesUtf8.put(0xd0b6, "zh");
			ruLatEntitiesUtf8.put(0xd0b7, "z");
			ruLatEntitiesUtf8.put(0xd0b8, "i");
			ruLatEntitiesUtf8.put(0xd0b9, "j");
			ruLatEntitiesUtf8.put(0xd0ba, "k");
			ruLatEntitiesUtf8.put(0xd0bb, "l");
			ruLatEntitiesUtf8.put(0xd0bc, "m");
			ruLatEntitiesUtf8.put(0x043D, "n");
			ruLatEntitiesUtf8.put(0xd0be, "o");
			ruLatEntitiesUtf8.put(0xd0bf, "p");

			ruLatEntitiesUtf8.put(0xd180, "r");
			ruLatEntitiesUtf8.put(0xd181, "s");
			ruLatEntitiesUtf8.put(0xd182, "t");
			ruLatEntitiesUtf8.put(0xd183, "u");
			ruLatEntitiesUtf8.put(0xd184, "f");
			ruLatEntitiesUtf8.put(0xd185, "kh");
			ruLatEntitiesUtf8.put(0xd186, "c");
			ruLatEntitiesUtf8.put(0xd187, "ch");
			ruLatEntitiesUtf8.put(0xd188, "sh");
			ruLatEntitiesUtf8.put(0xd189, "sch");
			ruLatEntitiesUtf8.put(0xd18a, "");
			ruLatEntitiesUtf8.put(0xd18b, "y");
			ruLatEntitiesUtf8.put(0xd18c, "\'");
			ruLatEntitiesUtf8.put(0x044D, "ae");
			ruLatEntitiesUtf8.put(0xd18e, "yu");
			ruLatEntitiesUtf8.put(0xd18f, "ya");

			ruLatEntitiesUtf8.put(0xd191, "yo");
//			ruLatEntitiesUtf8.put(0x0451, "Yo");
//			ruLatEntitiesUtf8.put(0xF2, "Ae");

		}
		return ruLatEntitiesUtf8;
	}

	private synchronized static Map<String, String> getEntities() {
		if (entities == null) {
			entities = new Hashtable<String, String>();
			// Quotation mark
			entities.put("quot", "\"");
			// Ampersand
			entities.put("amp", "\u0026");
			// Less than
			entities.put("lt", "\u003C");
			// Greater than
			entities.put("gt", "\u003E");
			// Nonbreaking space
			entities.put("nbsp", "\u00A0");
			// Inverted exclamation point
			entities.put("iexcl", "\u00A1");
			// Cent sign
			entities.put("cent", "\u00A2");
			// Pound sign
			entities.put("pound", "\u00A3");
			// General currency sign
			entities.put("curren", "\u00A4");
			// Yen sign
			entities.put("yen", "\u00A5");
			// Broken vertical bar
			entities.put("brvbar", "\u00A6");
			// Section sign
			entities.put("sect", "\u00A7");
			// Umlaut
			entities.put("uml", "\u00A8");
			// Copyright
			entities.put("copy", "\u00A9");
			// Feminine ordinal
			entities.put("ordf", "\u00AA");
			// Left angle quote
			entities.put("laquo", "\u00AB");
			// Not sign
			entities.put("not", "\u00AC");
			// Soft hyphen
			entities.put("shy", "\u00AD");
			// Registered trademark
			entities.put("reg", "\u00AE");
			// Macron accent
			entities.put("macr", "\u00AF");
			// Degree sign
			entities.put("deg", "\u00B0");
			// Plus or minus
			entities.put("plusmn", "\u00B1");
			// Superscript 2
			entities.put("sup2", "\u00B2");
			// Superscript 3
			entities.put("sup3", "\u00B3");
			// Acute accent
			entities.put("acute", "\u00B4");
			// Micro sign (Greek mu)
			entities.put("micro", "\u00B5");
			// Paragraph sign
			entities.put("para", "\u00B6");
			// Middle dot
			entities.put("middot", "\u00B7");
			// Cedilla
			entities.put("cedil", "\u00B8");
			// Superscript 1
			entities.put("sup1", "\u00B9");
			// Masculine ordinal
			entities.put("ordm", "\u00BA");
			// Right angle quote
			entities.put("raquo", "\u00BB");
			// Fraction one-fourth
			entities.put("frac14", "\u00BC");
			// Fraction one-half
			entities.put("frac12", "\u00BD");
			// Fraction three-fourths
			entities.put("frac34", "\u00BE");
			// Inverted question mark
			entities.put("iquest", "\u00BF");
			// Capital A, grave accent
			entities.put("Agrave", "\u00C0");
			// Capital A, acute accent
			entities.put("Aacute", "\u00C1");
			// Capital A, circumflex accent
			entities.put("Acirc", "\u00C2");
			// Capital A, tilde
			entities.put("Atilde", "\u00C3");
			// Capital A, umlaut
			entities.put("Auml", "\u00C4");
			// Capital A, ring
			entities.put("Aring", "\u00C5");
			// Capital AE ligature
			entities.put("AElig", "\u00C6");
			// Capital C, cedilla
			entities.put("Ccedil", "\u00C7");
			// Capital E, grave accent
			entities.put("Egrave", "\u00C8");
			// Capital E, acute accent
			entities.put("Eacute", "\u00C9");
			// Capital E, circumflex accent
			entities.put("Ecirc", "\u00CA");
			// Capital E, umlaut
			entities.put("Euml", "\u00CB");
			// Capital I, grave accent
			entities.put("Igrave", "\u00CC");
			// Capital I, acute accent
			entities.put("Iacute", "\u00CD");
			// Capital I, circumflex accent
			entities.put("Icirc", "\u00CE");
			// Capital I, umlaut
			entities.put("Iuml", "\u00CF");
			// Capital eth, Icelandic
			entities.put("ETH", "\u00D0");
			// Capital N, tilde
			entities.put("Ntilde", "\u00D1");
			// Capital O, grave accent
			entities.put("Ograve", "\u00D2");
			// Capital O, acute accent
			entities.put("Oacute", "\u00D3");
			// Capital O, circumflex accent
			entities.put("Ocirc", "\u00D4");
			// Capital O, tilde
			entities.put("Otilde", "\u00D5");
			// Capital O, umlaut
			entities.put("Ouml", "\u00D6");
			// Multiply sign
			entities.put("times", "\u00D7");
			// Capital O, slash
			entities.put("Oslash", "\u00D8");
			// Capital U, grave accent
			entities.put("Ugrave", "\u00D9");
			// Capital U, acute accent
			entities.put("Uacute", "\u00DA");
			// Capital U, circumflex accent
			entities.put("Ucirc", "\u00DB");
			// Capital U, umlaut
			entities.put("Uuml", "\u00DC");
			// Capital Y, acute accent
			entities.put("Yacute", "\u00DD");
			// Capital thorn, Icelandic
			entities.put("THORN", "\u00DE");
			// Small sz ligature, German
			entities.put("szlig", "\u00DF");
			// Small a, grave accent
			entities.put("agrave", "\u00E0");
			// Small a, acute accent
			entities.put("aacute", "\u00E1");
			// Small a, circumflex accent
			entities.put("acirc", "\u00E2");
			// Small a, tilde
			entities.put("atilde", "\u00E3");
			// Small a, umlaut
			entities.put("auml", "\u00E4");
			// Small a, ring
			entities.put("aring", "\u00E5");
			// Small ae ligature
			entities.put("aelig", "\u00E6");
			// Small c, cedilla
			entities.put("ccedil", "\u00E7");
			// Small e, grave accent
			entities.put("egrave", "\u00E8");
			// Small e, acute accent
			entities.put("eacute", "\u00E9");
			// Small e, circumflex accent
			entities.put("ecirc", "\u00EA");
			// Small e, umlaut
			entities.put("euml", "\u00EB");
			// Small i, grave accent
			entities.put("igrave", "\u00EC");
			// Small i, acute accent
			entities.put("iacute", "\u00ED");
			// Small i, circumflex accent
			entities.put("icirc", "\u00EE");
			// Small i, umlaut
			entities.put("iuml", "\u00EF");
			// Small eth, Icelandic
			entities.put("eth", "\u00F0");
			// Small n, tilde
			entities.put("ntilde", "\u00F1");
			// Small o, grave accent
			entities.put("ograve", "\u00F2");
			// Small o, acute accent
			entities.put("oacute", "\u00F3");
			// Small o, circumflex accent
			entities.put("ocirc", "\u00F4");
			// Small o, tilde
			entities.put("otilde", "\u00F5");
			// Small o, umlaut
			entities.put("ouml", "\u00F6");
			// Division sign
			entities.put("divide", "\u00F7");
			// Small o, slash
			entities.put("oslash", "\u00F8");
			// Small u, grave accent
			entities.put("ugrave", "\u00F9");
			// Small u, acute accent
			entities.put("uacute", "\u00FA");
			// Small u, circumflex accent
			entities.put("ucirc", "\u00FB");
			// Small u, umlaut
			entities.put("uuml", "\u00FC");
			// Small y, acute accent
			entities.put("yacute", "\u00FD");
			// Small thorn, Icelandic
			entities.put("thorn", "\u00FE");
			// Small y, umlaut
			entities.put("yuml", "\u00FF");
		}
		return entities;
	}

	public static String decodeRuLatEntities(byte[] bytes, String charsetIn) {
		if (bytes == null) {
			return null;
		}

		StringBuffer ostr = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			String tok = (String) getRuLatEntities(charsetIn).get(bytes[i]);
			if (tok != null)
				ostr.append(tok);
			else
				ostr.append('?');
		}
		return ostr.toString();
	}

	public static String decodeRuLatEntities(int[] ibytes, String istr, String charsetIn) {
		if (ibytes == null) {
			return null;
		}

		StringBuffer ostr = new StringBuffer();
		int idx = 0;
		for (int i = 0; i < ibytes.length; i++) {
			if (ibytes[i] > 0xf000)
				continue;
			String tok = (String) getRuLatEntities(charsetIn).get(ibytes[i]);
			if (tok != null)
				ostr.append(tok);
			else
				ostr.append(i < istr.length() ? istr.charAt(i) : '?');
			idx++;
		}
		return ostr.toString();
	}

	public static String decodeRuLatEntities(String str, String charsetIn) {
		if (str == null) {
			return null;
		}

		StringBuffer ostr = new StringBuffer();
		byte[] bytes = str.getBytes();
		for (int i = 0; i < str.length(); i++) {
			String tok = (String) getRuLatEntities(charsetIn).get((bytes[i] & 0xff));
			if (tok != null)
				ostr.append(tok);
			else
				ostr.append(str.charAt(i));
		}
		return ostr.toString();
	}

	public static String decodeRuLatEntities(String str, String charset, String charsetIn) throws IOException {
		if (str == null) {
			return null;
		}
		String encoding = (String) getEncodingEntities().get(charset);
		String encodingIn = (String) getEncodingEntities().get(charsetIn);
		if (encoding == null) {
			throw new IOException("CharSet: " + charset + " unknown!");
		}
		if (encodingIn == null) {
			throw new IOException("CharSet: " + charsetIn + " unknown!");
		}
		int[] outint = AnsiConvert.decodeBytesToHex(str, charset, encoding);

		return decodeRuLatEntities(outint, str, charsetIn);
	}

	public static String decodeRuLatEntitiesDeep(String str, String charset, String charsetIn) throws Exception {
		if (str == null) {
			return null;
		}
		String encoding = (String) getEncodingEntities().get(charset);
		String encodingIn = (String) getEncodingEntities().get(charsetIn);
		if (encoding == null) {
			throw new IOException("CharSet: " + charset + " unknown!");
		}
		if (encodingIn == null) {
			throw new IOException("CharSet: " + charsetIn + " unknown!");
		}
		int[] outint = AnsiConvert.decodeBytesToHex(str, charsetIn, encodingIn);

		String outstr = AnsiConvert.decodeIntsToString(outint, charset, encoding, outint.length);

		return decodeRuLatEntities(outstr, charset, charsetIn);
	}

	public static String decodeRuLatHexEntities(String str, String charsetIn) {
		if (str == null) {
			return null;
		}

		StringBuffer ostr = new StringBuffer();
		byte[] bytes = str.getBytes();
		for (int i = 0; i < str.length(); i++) {
			String tok = (String) getRuLatEntities(charsetIn).get((bytes[i] & 0xff));
			if (tok != null)
				ostr.append(tok);
			else
				ostr.append(str.charAt(i));
		}
		return ostr.toString();
	}

	public static String decodeEntities(String str) {
		StringBuffer ostr = new StringBuffer();
		int i1 = 0;
		int i2 = 0;

		while (i2 < str.length()) {
			i1 = str.indexOf("&", i2);
			if (i1 == -1) {
				ostr.append(str.substring(i2, str.length()));
				break;
			}
			ostr.append(str.substring(i2, i1));
			i2 = str.indexOf(";", i1);
			if (i2 == -1) {
				ostr.append(str.substring(i1, str.length()));
				break;
			}

			String tok = str.substring(i1 + 1, i2);
			if (tok.charAt(0) == '#') {
				tok = tok.substring(1);
				try {
					int radix = 10;
					if (tok.trim().charAt(0) == 'x') {
						radix = 16;
						tok = tok.substring(1, tok.length());
					}
					ostr.append((char) Integer.parseInt(tok, radix));
				} catch (NumberFormatException exp) {
					ostr.append('?');
				}
			} else {
				tok = (String) getEntities().get(tok);
				if (tok != null)
					ostr.append(tok);
				else
					ostr.append('?');
			}
			i2++;
		}
		return ostr.toString();
	}

	/**
	 *
	 * @param bytes
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	protected static String decodeBytesToHexString(byte[] bytes, String charset) throws IOException {

		String encoding = (String) getEncodingEntities().get(charset);
		if (encoding == null)
			throw new IOException("CharSet: " + charset + " unknown!");

		String ostr = new String();
		if (encoding.indexOf("Unicode") == -1) {
			for (int j = 0; j < bytes.length; j++) {
				String hex = (Integer.toHexString(bytes[j] & 0xFF)).toUpperCase();
				if (hex.length() == 1)
					hex = "0" + hex;
				ostr += "\\x" + hex;
			}
		} else {
			for (int j = 0; j < bytes.length; j += 2) {
				String hex1 = (Integer.toHexString(bytes[j] & 0xFF)).toUpperCase();
				if (hex1.length() == 1)
					hex1 = "0" + hex1;
				String hex2 = (Integer.toHexString(bytes[j + 1] & 0xFF)).toUpperCase();
				if (hex2.length() == 1)
					hex2 = "0" + hex2;
				if (encoding.indexOf("Little") == -1)
					ostr += "\\u" + hex1 + hex2;
				else
					ostr += "\\u" + hex2 + hex1;
			}
		}
		return ostr;
	}

	protected static String decodeBytesToHexString(String istr, String charset) throws IOException {
		byte[] bytes = istr.getBytes(charset);
		return decodeBytesToHexString(bytes, charset);
	}

	public static int[] decodeBytesToHex(String istr, String charset) throws IOException {

		String instr = decodeBytesToHexString(istr, charset);

		int[] outint = new int[instr.length() + 1];
		StringTokenizer st = new StringTokenizer(instr, "\\u/x");
		int count = 0;
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			outint[count] = Integer.parseInt(tok, 16);
			count++;
		}
		int[] oint = new int[count];
		for (int i = 0; i < count; i++) {
			oint[i] = outint[i];
		}

		return oint;
	}

	/**
	 *
	 * @param outint
	 * @param encoding
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public static String decodeIntsToString(int[] outint, String charset, int count) throws Exception {
		String encoding = (String) getEncodingEntities().get(charset);
		if (encoding == null)
			throw new IOException("CharSet: " + charset + " unknown!");

		String outstr = new String();
		if (encoding.indexOf("Unicode") == -1) {
			byte[] outbytes = new byte[count];
			for (int i = 0; i < count; i++)
				outbytes[i] = (byte) outint[i];
			outstr = new String(outbytes, charset);
		} else {
			char[] outchars = new char[count];

			if (encoding.indexOf("Little") == -1) {
				for (int i = 0; i < count; i++)
					outchars[i] = (char) outint[i];
			} else {
				for (int i = 0; i < count; i++) {
					int in = ((outint[i] >> 8) & 0xFF) | ((outint[i] & 0xFF) << 8);
					outchars[i] = (char) in;
				}
			}

			outstr = new String(outchars);
		}
		return outstr;
	}

	/**
	 * @param istr
	 * @param charset
	 * @param charsetIn
	 * @return
	 * @throws Exception
	 */
	public static String decodeCharSets(String istr, String charset, String charsetIn) throws Exception {
		int[] outint = decodeBytesToHex(istr, charsetIn);
		String outstr = decodeIntsToString(outint, charset, outint.length);
		return outstr;
	}

	public static void main(String[] args) throws Exception {
		String charSet = "Cp866";
		String charSetIn = "Cp850";
		String istr = "";
		// istr = "Íà Âå÷åðèíêå";
		// istr = "Àëüáîì áûë ñêà÷åí; ñ ñàéòà";
		// istr = "Þëèÿ Ñàâè÷åâà - Ìîñêâà-Âëàäèâîñòîê";
		istr = "002 Ä. Å«tÑ»á - çáaáº¿½áß8 G«í«¬.mp3";
		istr = "007 é. üaÑª¡Ñóá - ïeí«ó8 ß»áßÑG ¼¿a.mp3";
		istr = "003 DiskoGeka Avarin - LeGo vsegda.mp3";
		byte[] bytes = istr.getBytes();
		int i = 9;
		char chr = istr.charAt(i);
		String nth = "[" + i + "]";

		Charset cset = Charset.defaultCharset();

		System.out.println("Default charset is: [" + cset + "]");
		System.out.println(nth + ". byte  = " + new Byte(bytes[i]));
		System.out.println(nth + ". char  = '" + chr + "'");
		System.out.println(nth + ". digit = " + Character.codePointAt(istr.toCharArray(), i));
		System.out.println(nth + ". digit = " + Integer.toHexString(Character.codePointAt(istr.toCharArray(), i)));
		System.out.println(nth + ". ubyte = " + (bytes[i] & 0xff));

		// System.out.println(decodeRuLatEntities(istr, "UTF-16"));

		// System.out.println(decodeCharSets(istr, charSet, charSet));
		// System.out.println(decodeCharSets(istr, charSetIn, charSetIn));
		// System.out.println(decodeCharSets(istr, charSet, charSetIn));
		// System.out.println(decodeCharSets(istr, charSet, "Cp1252"));
		// System.out.println(decodeCharSets(istr, charSet, "UTF-16"));
		// System.out.println();

		System.out.println(decodeRuLatEntities(istr, charSet, charSet));
		System.out.println(decodeBytesToHexString(istr, charSet));
		System.out.println(decodeRuLatEntities(istr, charSetIn, charSetIn));
		System.out.println(decodeBytesToHexString(istr, charSetIn));

		System.out.println(decodeRuLatEntities(istr, charSet, charSetIn));

		int[] ival = decodeBytesToHex(istr, charSet);
		int[] oval = decodeBytesToHex(istr, charSetIn);
		String itxt = decodeRuLatEntities(istr, charSet, charSet);
		String otxt = decodeRuLatEntities(istr, charSetIn, charSetIn);

		for (i = 0; (i < ival.length && i < oval.length && i < itxt.length() && i < otxt.length()); i++) {
			System.out.println(i + ".\t0x"
			// + Integer.toHexString(ival[i])
					+ Integer.toHexString(Character.codePointAt(itxt.toCharArray(), i)) + " (0x"
					+ Integer.toHexString(ival[i]) + ")"
					// + " = '" + itxt.charAt(i) + "',\t0x" +
					// Integer.toHexString(oval[i])
					+ " = '" + itxt.charAt(i) + "',\t0x"
					+ Integer.toHexString(Character.codePointAt(otxt.toCharArray(), i)) + " (0x"
					+ Integer.toHexString(oval[i]) + ")" + " = '" + otxt.charAt(i) + "'");
		}

		// SortedMap<String, Charset>csets = Charset.availableCharsets();
		// Set<String> keyset = csets.keySet();
		// int cnt = 0;
		// for (String key : keyset)
		// {
		// System.out.println(++cnt + ". key [charset] = " + key + ", " +
		// csets.get(key).aliases());
		// }

		// for (String charSet : getEncodingEntities().keySet())
		// {
		// System.out.print("Char set [" + charSet + "]:\t");
		// try {
		// System.out.println(decodeRuLatEntities(istr, charSet));
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		//// e.printStackTrace();
		// System.out.println("ERROR");
		// }
		// }

		// String dstr = AnsiDecoder.decodeRuLatEntities(istr);

	}

}
