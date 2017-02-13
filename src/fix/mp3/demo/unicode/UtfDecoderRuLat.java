package fix.mp3.demo.unicode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

// Author: Patrick Hargitt
// Upstream S.A.
/**
 * @author u771512
 *
 */
public class UtfDecoderRuLat {

	// FEFF because this is the Unicode char represented by the UTF-8 byte order
	// mark (EF BB BF).
	public static final String UTF8_BOM = "\uFEFF";

	private static Map<String, String> ruLatEntitiesUtf;
	private static Map<String, String> ruLatSpecials;

	// -------------------------- ENCODING METHODS --------------------------

	// private synchronized static Map<Integer, String> getRuLatEntities()
	// {
	// return getRuLatEntities(null);
	// }
	private synchronized static Map<String, String> getRuLatEntities(String charSet) {
		// if (charSet != null && charSet.startsWith("Cp8")) {
		// return getRuLatEntitiesCp866();
		// }
		return getRuLatEntitiesUtf();
	}

	private synchronized static Map<String, String> getRuLatEntitiesUtf() {
		if (ruLatEntitiesUtf == null) {
			ruLatEntitiesUtf = new Hashtable<String, String>();
			// АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
			ruLatEntitiesUtf.put("А", "A");
			ruLatEntitiesUtf.put("Б", "B");
			ruLatEntitiesUtf.put("В", "V");
			ruLatEntitiesUtf.put("Г", "G");
			ruLatEntitiesUtf.put("Д", "D");
			ruLatEntitiesUtf.put("Е", "E");
			ruLatEntitiesUtf.put("Ж", "Zh");
			ruLatEntitiesUtf.put("З", "Z");
			ruLatEntitiesUtf.put("И", "I");
			ruLatEntitiesUtf.put("Й", "J");
			ruLatEntitiesUtf.put("К", "K");
			ruLatEntitiesUtf.put("Л", "L");
			ruLatEntitiesUtf.put("М", "M");
			ruLatEntitiesUtf.put("Н", "N");
			ruLatEntitiesUtf.put("О", "O");
			ruLatEntitiesUtf.put("П", "P");

			ruLatEntitiesUtf.put("Р", "R");
			ruLatEntitiesUtf.put("С", "S");
			ruLatEntitiesUtf.put("Т", "T");
			ruLatEntitiesUtf.put("У", "U");
			ruLatEntitiesUtf.put("Ф", "F");
			ruLatEntitiesUtf.put("Х", "Kh");
			ruLatEntitiesUtf.put("Ц", "C");
			ruLatEntitiesUtf.put("Ч", "Ch");
			ruLatEntitiesUtf.put("Ш", "Sh");
			ruLatEntitiesUtf.put("Щ", "Sch");
			ruLatEntitiesUtf.put("Ъ", "");
			ruLatEntitiesUtf.put("Ы", "Y");
			ruLatEntitiesUtf.put("Ь", "\'");
			ruLatEntitiesUtf.put("Э", "Ae");
			ruLatEntitiesUtf.put("Ю", "Yu");
			ruLatEntitiesUtf.put("Я", "Ya");

			// абвгдеёжзиклмнопрстуфхцчшщъыьэюя
			ruLatEntitiesUtf.put("а", "a");
			ruLatEntitiesUtf.put("б", "b");
			ruLatEntitiesUtf.put("в", "v");
			ruLatEntitiesUtf.put("г", "g");
			ruLatEntitiesUtf.put("д", "d");
			ruLatEntitiesUtf.put("е", "e");
			ruLatEntitiesUtf.put("ж", "zh");
			ruLatEntitiesUtf.put("з", "z");
			ruLatEntitiesUtf.put("и", "i");
			ruLatEntitiesUtf.put("й", "j");
			ruLatEntitiesUtf.put("к", "k");
			ruLatEntitiesUtf.put("л", "l");
			ruLatEntitiesUtf.put("м", "m");
			ruLatEntitiesUtf.put("н", "n");
			ruLatEntitiesUtf.put("о", "o");
			ruLatEntitiesUtf.put("п", "p");

			ruLatEntitiesUtf.put("р", "r");
			ruLatEntitiesUtf.put("с", "s");
			ruLatEntitiesUtf.put("т", "t");
			ruLatEntitiesUtf.put("у", "u");
			ruLatEntitiesUtf.put("ф", "f");
			ruLatEntitiesUtf.put("х", "kh");
			ruLatEntitiesUtf.put("ц", "c");
			ruLatEntitiesUtf.put("ч", "ch");
			ruLatEntitiesUtf.put("ш", "sh");
			ruLatEntitiesUtf.put("щ", "sch");
			ruLatEntitiesUtf.put("ъ", "");
			ruLatEntitiesUtf.put("ы", "y");
			ruLatEntitiesUtf.put("ь", "\'");
			ruLatEntitiesUtf.put("э", "ae");
			ruLatEntitiesUtf.put("ю", "yu");
			ruLatEntitiesUtf.put("я", "ya");

			ruLatEntitiesUtf.put("ё", "yo");
			
			ruLatEntitiesUtf.put("DJ", "DJ");
			ruLatEntitiesUtf.put("JPG", "JPG");
			ruLatEntitiesUtf.put("MR", "Мr");
			ruLatEntitiesUtf.put("Mr", "Мr");
			ruLatEntitiesUtf.put("Mc", "Мc");
			ruLatEntitiesUtf.put("MC", "МC");
			ruLatEntitiesUtf.put("MP3", "MP3");
			ruLatEntitiesUtf.put("Dj", "Dj");
			ruLatEntitiesUtf.put("DJ", "Dj");
			ruLatEntitiesUtf.put("dj", "dj");
			ruLatEntitiesUtf.put("jpg", "jpg");
			ruLatEntitiesUtf.put("mp3", "mp3");

		}
		return ruLatEntitiesUtf;
	}

	private synchronized static Map<String, String> getRuSpecials() {
		if (ruLatSpecials == null) {
			ruLatSpecials = new Hashtable<String, String>();
//			ruLatSpecials.put("Щаст", "Счаст");
//			ruLatSpecials.put("щаст", "счаст");
//			ruLatSpecials.put("ДЕЙАВУЕ", "DEJAVUE");
//			ruLatSpecials.put("ДЕЯВУЕ", "DEJAVUE");
//			ruLatSpecials.put("МАЫДАЫ", "MAYDAY");
//			ruLatSpecials.put("Ф.А.Ц.Е", "F.A.C.E");
//			ruLatSpecials.put("Данце", "Dance");
//			ruLatSpecials.put("данце", "dance");
//			ruLatSpecials.put("ДАНЦЕ", "DANCE");
//			ruLatSpecials.put("Ума2рмах", "Ума2рман");
//			ruLatSpecials.put("Ума2рмаХ", "Ума2рмаН");
//			ruLatSpecials.put("Рефлех", "Reflex");
//			ruLatSpecials.put("Ремих", "Remix");
//			ruLatSpecials.put("ремих", "remix");
//			ruLatSpecials.put("Т1оне", "T1one");
//			ruLatSpecials.put("Алоена", "Алёна");
//			ruLatSpecials.put("Вэнг", "Ваенг");
//			ruLatSpecials.put("Шоди", "Сходи");
//			ruLatSpecials.put("шоди", "сходи");
//			ruLatSpecials.put("Киллах", "Killah");
//			ruLatSpecials.put("Феат.", "Feat.");
//			ruLatSpecials.put("феат.", "feat.");
//			ruLatSpecials.put("Едит", "Edit");
//			ruLatSpecials.put("Бест", "Best");
//			ruLatSpecials.put("Геегун", "Geegun");
//			ruLatSpecials.put("Парты", "Party");
//			// ruLatSpecials.put("Проецт", "Project");
//			ruLatSpecials.put("Проецт", "Проект");
//			ruLatSpecials.put("ывэт", "ывает");
//			ruLatSpecials.put("Цлуб", "Club");
//			ruLatSpecials.put("Легостэв", "Легостаев");
//			ruLatSpecials.put("Николэв", "Николаев");
//			ruLatSpecials.put("Бахх Тее", "Bahh Tee");
//			ruLatSpecials.put("летэшь", "летаешь");
//			ruLatSpecials.put("игрэм", "играем");
//			ruLatSpecials.put("поминэт", "поминает");
//			ruLatSpecials.put("нэшь", "наешь");
//			ruLatSpecials.put("5ивеста", "5ivesta");
//			ruLatSpecials.put("5ИВЕСТА", "5IVESTA");
//			ruLatSpecials.put("цз", "ц");
//			ruLatSpecials.put("Юлиa", "Юлия");
//			ruLatSpecials.put("Цустом", "Custom");
//			ruLatSpecials.put("нэш", "наеш");
//			ruLatSpecials.put("Цредо", "Credo");
//			ruLatSpecials.put("Й-Поwер", "J-Power");
//			ruLatSpecials.put("Олга", "Ольга");
//			ruLatSpecials.put("Лубви", "Любви");
//			ruLatSpecials.put("лубви", "любви");
//			ruLatSpecials.put("Гроове", "Groove");
//			ruLatSpecials.put("Ыля", "Юля");
//			ruLatSpecials.put("Интернатёнал", "International");
//			ruLatSpecials.put("Глюкьоза", "Глюк'оза");
//			ruLatSpecials.put("зелоеные", "зеленые");
//			ruLatSpecials.put("WИЛД", "ВИЛЬД");
//			ruLatSpecials.put("Wилд", "Вильд");
//			ruLatSpecials.put("Цоннецт", "Connect");
//			ruLatSpecials.put("Сай", "Say");
//			ruLatSpecials.put("бые", "bye");
//			ruLatSpecials.put("Бые", "Bye");
//			ruLatSpecials.put("Гоод", "Good");
//			ruLatSpecials.put("Лухус", "Luxus");
//			ruLatSpecials.put("Руссиан", "Russian");
//			ruLatSpecials.put("Неw-Ыорк", "New-York");
//			ruLatSpecials.put("Фуцк", "Fuck");
//			ruLatSpecials.put("Ыоу", "You");
//			ruLatSpecials.put("Зип", "Zip");
//			ruLatSpecials.put("Меморы", "Memory");
//			ruLatSpecials.put("Нэдине", "Наедине");
//			ruLatSpecials.put("нэдине", "наедине");
//			ruLatSpecials.put("МАРСЕЛ ", "МАРСЕЛЬ ");
//			ruLatSpecials.put("Марсел ", "Марсель ");
//			ruLatSpecials.put("Мелоды", "Melody");
//			ruLatSpecials.put("СЕРИОГА", "СЕРЕГА");
//			ruLatSpecials.put("Сериога", "Серёга");
//			ruLatSpecials.put("Сероежа", "Серёжа");
//			ruLatSpecials.put("МИТИА", "МИТЯ");
//			ruLatSpecials.put("Митиа", "Митя");
//			ruLatSpecials.put("Тебиа", "Тебя");
//			ruLatSpecials.put("Ре-Флех", "Re-Flex");
//			ruLatSpecials.put("Всио", "Все");
//			ruLatSpecials.put("ЛИУДМИЛА", "ЛЮДМИЛА");
//			ruLatSpecials.put("Лиудмила", "Людмила");
//			ruLatSpecials.put("Елвира", "Ельвира");
//			ruLatSpecials.put("Фамилы", "Family");
//			ruLatSpecials.put("Твойих", "Твоих");
//			ruLatSpecials.put("Чоет", "Чёт");
//			ruLatSpecials.put("Бянка", "Бьянка");
//			ruLatSpecials.put("Любов", "Любовь");
//			ruLatSpecials.put("Любовьь", "Любовь");
//			ruLatSpecials.put("Просциай", "Прощай");
//			ruLatSpecials.put("Франкы", "Franky");
//			ruLatSpecials.put("Радё", "Радио");
//			ruLatSpecials.put("Студё", "Студио");
//			ruLatSpecials.put("Радё", "Radio");
//			ruLatSpecials.put("Версён", "Version");
//			ruLatSpecials.put("версён", "version");
//			ruLatSpecials.put("Дё", "Дио");
//			ruLatSpecials.put("Гирл", "Girl");
//			ruLatSpecials.put("Малыш", "Малышь");
//			ruLatSpecials.put("Малышьь", "Малышь");
//			ruLatSpecials.put("Лоунге", "Lounge");
//			ruLatSpecials.put("Циты", "City");
//			ruLatSpecials.put("Тхе", "The");
//			ruLatSpecials.put("Тахи", "Taxi");
//			ruLatSpecials.put("Лове", "Love");
//			ruLatSpecials.put("Лады Д", "Lady D");
			// Лады Д Б
		}
		return ruLatSpecials;
	}

	public static String decodeRuLatEntities(String str, String charsetIn) {
		if (str == null) {
			return null;
		}

		StringBuffer ostr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			// first try a long one (3)
			String xlat = xlatPart(str, charsetIn, i, 3);
			if (xlat != null) {
				ostr.append(xlat);
				i += 2;
				continue;
			}
			// next try a reduced one (2)
			xlat = xlatPart(str, charsetIn, i, 2);
			if (xlat != null) {
				ostr.append(xlat);
				i += 1;
				continue;
			}
			// last try a single one (1)
			xlat = xlatPart(str, charsetIn, i, 1);
			if (xlat != null)
				ostr.append(xlat);
			else
				ostr.append(str.charAt(i));
		}

		/**
		 * check specials
		 */
		String out = ostr.toString();
		Map<String, String> specials = getRuSpecials();
		specials.keySet();
		for (String special : specials.keySet()) {
			// System.out.println(special + " = " + out.contains(special));
			out = out.replaceAll(special, specials.get(special));
		}
		return out;
	}

	protected static String xlatPart(String in, String charsetIn, int offs, int length) {
		int len = offs + length;
		if (len <= in.length()) {
			String tok = in.substring(offs, len);
			String xlat = (String) getRuLatEntities(charsetIn).get(tok);
			if (xlat != null) {
				return xlat;
			}
		}
		return null;
	}

	public static void writeFile(String fileName, List<String> fileContent, String charSet) throws IOException {

		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, charSet);
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		osw.write(UTF8_BOM);
		for (String line : fileContent) {
			osw.write(line + "\n");
		}
		osw.close();
	}

	public static List<String> getDirList(String fromDirStr) throws FileNotFoundException {
		File file = new java.io.File(fromDirStr);
		List<String> dirList = new ArrayList<String>();
		if (!file.isDirectory()) {
			return dirList;
		}
		dirList.add(file.getAbsolutePath());
		File[] files = file.listFiles();
		for (File fil : files) {
			if (!fil.isDirectory())
				continue;
			// dirList.add(fil.getAbsolutePath());
			List<String> dirSublist = getDirList(fil.getAbsolutePath());
			if (!dirSublist.isEmpty()) {
				dirList.addAll(dirSublist);
			}
		}
		return dirList;
	}

	public static List<String> getFileList(String fromDirStr) throws FileNotFoundException {
		File file = new java.io.File(fromDirStr);
		List<String> fileList = new ArrayList<String>();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File fil : files) {
				if (!fil.isFile())
					continue;
				if (!fil.getName().toLowerCase().endsWith(".mp3"))
					continue;
				fileList.add(purgeFileName(fil.getName()));
			}
		} else if (file.isFile()) {
			fileList.add(purgeFileName(file.getName()));
		} else {
			throw new FileNotFoundException("ERROR: invalid file/directory: [" + fromDirStr + "]!");
		}
		Collections.sort(fileList);
		return fileList;
	}

	private static String purgeFileName(String name) {
		final String FILEXT = ".mp3";
		if (name == null || name.length() <= FILEXT.length()) {
			return name;
		}
		if (!(name.toLowerCase()).matches(".*" + FILEXT + "$")) {
			return name;
		}

		return name.substring(0, name.length() - FILEXT.length());
	}

	public static int processTagsForDirectory(String fromDirStr) throws Exception {
		String charSet = StandardCharsets.UTF_8.name();
		String fileLat = fromDirStr + "/" + "tag_lat.txt";
		String fileRu = fromDirStr + "/" + "tag_ru.txt";

		List<String> fileListRu = getFileList(fromDirStr);
		if (fileListRu.isEmpty()) {
			return 0;
		}
		writeFile(fileRu, fileListRu, charSet);

		String toDirName = attachTargetDir(fromDirStr, "_lat");
		List<String> fileListLat = new ArrayList<>();

		for (String filRu : fileListRu) {
			String filLat = decodeRuLatEntities(filRu, charSet);
			fileListLat.add(filLat);
			FileConvert.copy(fromDirStr + "/" + filRu + ".mp3", toDirName + "/" + filLat  + ".mp3");
		}
		writeFile(fileLat, fileListLat, charSet);
		return fileListLat.size();
	}

	private static String attachTargetDir(String fromDirStr, String locTargetDir) {
		String toDirName = fromDirStr + "/" + locTargetDir;
		File outFile = new java.io.File(toDirName );
		if (!outFile.isDirectory()) {
			if (!outFile.mkdirs()) {
				System.out.println(" ERROR: cannot create directory: " + toDirName);
				System.exit(0);
			}
		}
		return toDirName;
	}

	public static void main(String[] args) throws Exception {

		// List<String> dirList =
		// getDirList("v:/Studio/Usb_003/01.Pop/Pop-06.1");
		List<String> dirList = getDirList("d:/Studio/Musik/_Stream/Audials/Work/ru");
		for (String dir : dirList) {
			int count = processTagsForDirectory(dir);
			System.out.println(dir + (count == 0 ? "" : ": (" + count + " files)"));
		}
	}

	public static void main2(String[] args) throws Exception {
		// String charSet = "UTF8";
		String charSetIn = "UTF8";
		String istr = "";
		istr = "001.Uma2rmaH - Tancuj, Muza! .mp3\n";
		istr += "O bozhe kakoy muzhchina natali\n";

		System.out.println("Translate:\n" + istr);
		System.out.println(decodeRuLatEntities(istr, charSetIn));
	}

}
