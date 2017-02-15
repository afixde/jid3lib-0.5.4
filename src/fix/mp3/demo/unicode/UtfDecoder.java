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
public class UtfDecoder {

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
	private synchronized static Map<String, String> getLatRuEntities(String charSet) {
		// if (charSet != null && charSet.startsWith("Cp8")) {
		// return getRuLatEntitiesCp866();
		// }
		return getLatRuEntitiesUtf();
	}

	private synchronized static Map<String, String> getLatRuEntitiesUtf() {
		if (ruLatEntitiesUtf == null) {
			ruLatEntitiesUtf = new Hashtable<String, String>();
			// АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
			ruLatEntitiesUtf.put("A", "А");
			ruLatEntitiesUtf.put("B", "Б");
			ruLatEntitiesUtf.put("V", "В");
			ruLatEntitiesUtf.put("G", "Г");
			ruLatEntitiesUtf.put("DJ", "DJ");
			ruLatEntitiesUtf.put("D", "Д");
			ruLatEntitiesUtf.put("E", "Е");
			// ruLatEntitiesCp1251.put("Oe", "Ё");
			ruLatEntitiesUtf.put("Ö", "Ё");
			ruLatEntitiesUtf.put("\"", "Ё");
			ruLatEntitiesUtf.put("Zh", "Ж");
			ruLatEntitiesUtf.put("ZH", "Ж");
			ruLatEntitiesUtf.put("Z", "З");
			ruLatEntitiesUtf.put("I", "И");
			ruLatEntitiesUtf.put("J", "Й");
			ruLatEntitiesUtf.put("JPG", "JPG");
			ruLatEntitiesUtf.put("K", "К");
			ruLatEntitiesUtf.put("L", "Л");
			ruLatEntitiesUtf.put("MR", "Мr");
			ruLatEntitiesUtf.put("Mr", "Мr");
			ruLatEntitiesUtf.put("Mc", "Мc");
			ruLatEntitiesUtf.put("MC", "МC");
			ruLatEntitiesUtf.put("M", "М");
			ruLatEntitiesUtf.put("N", "Н");
			ruLatEntitiesUtf.put("O", "О");
			ruLatEntitiesUtf.put("P", "П");
			ruLatEntitiesUtf.put("R", "Р");
			ruLatEntitiesUtf.put("S", "С");
			ruLatEntitiesUtf.put("T", "Т");
			ruLatEntitiesUtf.put("U", "У");
			ruLatEntitiesUtf.put("F", "Ф");
			ruLatEntitiesUtf.put("X", "Х");
			ruLatEntitiesUtf.put("H", "Х");
			ruLatEntitiesUtf.put("Kh", "Х");
			ruLatEntitiesUtf.put("KH", "Х");
			ruLatEntitiesUtf.put("C", "Ц");
			ruLatEntitiesUtf.put("Ch", "Ч");
			ruLatEntitiesUtf.put("CH", "Ч");
			ruLatEntitiesUtf.put("SH", "Ш");
			ruLatEntitiesUtf.put("Sh", "Ш");
			ruLatEntitiesUtf.put("Sch", "Щ");
			ruLatEntitiesUtf.put("SCH", "Щ");
			ruLatEntitiesUtf.put("''", "Ъ");
			ruLatEntitiesUtf.put("Y", "Ы");
			ruLatEntitiesUtf.put("'", "Ь");
			ruLatEntitiesUtf.put("MP3", "MP3");
			// ruLatEntitiesCp1251.put("J", "Ь");
			ruLatEntitiesUtf.put("Ae", "Э");
			ruLatEntitiesUtf.put("AE", "Э");
			ruLatEntitiesUtf.put("Yu", "Ю");
			ruLatEntitiesUtf.put("YU", "Ю");
			ruLatEntitiesUtf.put("Ju", "Ю");
			ruLatEntitiesUtf.put("JU", "Ю");
			ruLatEntitiesUtf.put("Ya", "Я");
			ruLatEntitiesUtf.put("YA", "Я");
			ruLatEntitiesUtf.put("Ja", "Я");
			ruLatEntitiesUtf.put("JA", "Я");
			// абвгдеёжзиклмнопрстуфхцчшщъыьэюя
			ruLatEntitiesUtf.put("a", "а");
			ruLatEntitiesUtf.put("b", "б");
			ruLatEntitiesUtf.put("v", "в");
			ruLatEntitiesUtf.put("g", "г");
			ruLatEntitiesUtf.put("Dj", "Dj");
			ruLatEntitiesUtf.put("DJ", "Dj");
			ruLatEntitiesUtf.put("dj", "dj");
			ruLatEntitiesUtf.put("d", "д");
			ruLatEntitiesUtf.put("e", "е");
			ruLatEntitiesUtf.put("yo", "ё");
			ruLatEntitiesUtf.put("jo", "ё");
			ruLatEntitiesUtf.put("eye", "ее");
			ruLatEntitiesUtf.put("eje", "ее");
			ruLatEntitiesUtf.put("eyo", "её");
			ruLatEntitiesUtf.put("ejo", "её");
			ruLatEntitiesUtf.put("eyu", "ею");
			ruLatEntitiesUtf.put("eju", "ею");
			ruLatEntitiesUtf.put("eya", "ея");
			ruLatEntitiesUtf.put("eya", "ея");
			ruLatEntitiesUtf.put("ey", "ей");

			ruLatEntitiesUtf.put("aye", "ае");
			ruLatEntitiesUtf.put("aje", "ае");
			ruLatEntitiesUtf.put("ayo", "аё");
			ruLatEntitiesUtf.put("ajo", "аё");
			ruLatEntitiesUtf.put("ayu", "аю");
			ruLatEntitiesUtf.put("aju", "аю");
			ruLatEntitiesUtf.put("aya", "ая");
			ruLatEntitiesUtf.put("aya", "ая");
			ruLatEntitiesUtf.put("ay", "ай");

			ruLatEntitiesUtf.put("iye", "ие");
			ruLatEntitiesUtf.put("ije", "ие");
			ruLatEntitiesUtf.put("iyo", "иё");
			ruLatEntitiesUtf.put("ijo", "иё");
			ruLatEntitiesUtf.put("iyu", "ию");
			ruLatEntitiesUtf.put("iju", "ию");
			ruLatEntitiesUtf.put("iya", "ия");
			ruLatEntitiesUtf.put("iya", "ия");
			ruLatEntitiesUtf.put("iy", "ий");

			ruLatEntitiesUtf.put("oye", "ое");
			ruLatEntitiesUtf.put("oje", "ое");
			ruLatEntitiesUtf.put("oyo", "оё");
			ruLatEntitiesUtf.put("ojo", "оё");
			ruLatEntitiesUtf.put("oyu", "ою");
			ruLatEntitiesUtf.put("oju", "ою");
			ruLatEntitiesUtf.put("oya", "оя");
			ruLatEntitiesUtf.put("oya", "оя");
			ruLatEntitiesUtf.put("oy", "ой");

			ruLatEntitiesUtf.put("uye", "уе");
			ruLatEntitiesUtf.put("uje", "уе");
			ruLatEntitiesUtf.put("uyo", "уё");
			ruLatEntitiesUtf.put("ujo", "уё");
			ruLatEntitiesUtf.put("uyu", "ую");
			ruLatEntitiesUtf.put("uju", "ую");
			ruLatEntitiesUtf.put("uya", "уя");
			ruLatEntitiesUtf.put("uya", "уя");
			ruLatEntitiesUtf.put("uy", "уй");

			// ruLatEntitiesCp1251.put("oe", "ё");
			ruLatEntitiesUtf.put("ö", "ё");
			ruLatEntitiesUtf.put("yo", "ё");
			ruLatEntitiesUtf.put("jo", "ё");
			ruLatEntitiesUtf.put("io", "ё");
			ruLatEntitiesUtf.put("dio", "дио");
			ruLatEntitiesUtf.put("zh", "ж");
			ruLatEntitiesUtf.put("z", "з");
			ruLatEntitiesUtf.put("i", "и");
			ruLatEntitiesUtf.put("j", "й");
			ruLatEntitiesUtf.put("jec", "ек");
			ruLatEntitiesUtf.put("yec", "ек");
			ruLatEntitiesUtf.put("jpg", "jpg");
			ruLatEntitiesUtf.put("k", "к");
			ruLatEntitiesUtf.put("l", "л");
			ruLatEntitiesUtf.put("m", "м");
			ruLatEntitiesUtf.put("n", "н");
			ruLatEntitiesUtf.put("o", "о");
			ruLatEntitiesUtf.put("p", "п");
			ruLatEntitiesUtf.put("r", "р");
			ruLatEntitiesUtf.put("s", "с");
			ruLatEntitiesUtf.put("t", "т");
			ruLatEntitiesUtf.put("u", "у");
			ruLatEntitiesUtf.put("f", "ф");
			ruLatEntitiesUtf.put("x", "х");
			ruLatEntitiesUtf.put("h", "х");
			ruLatEntitiesUtf.put("kh", "х");
			ruLatEntitiesUtf.put("c", "ц");
			ruLatEntitiesUtf.put("ch", "ч");
			ruLatEntitiesUtf.put("sh", "ш");
			ruLatEntitiesUtf.put("sch", "щ");
			ruLatEntitiesUtf.put("shh", "щ");
			ruLatEntitiesUtf.put("''", "ъ");
			ruLatEntitiesUtf.put("y", "ы");
			ruLatEntitiesUtf.put("'", "ь");
			ruLatEntitiesUtf.put("ae", "э");
			ruLatEntitiesUtf.put("ü", "ю");
			ruLatEntitiesUtf.put("iu", "ю");
			ruLatEntitiesUtf.put("yu", "ю");
			ruLatEntitiesUtf.put("ju", "ю");
			ruLatEntitiesUtf.put("ya", "я");
			ruLatEntitiesUtf.put("ja", "я");
			ruLatEntitiesUtf.put("mp3", "mp3");

		}
		return ruLatEntitiesUtf;
	}

	private synchronized static Map<String, String> getLatSpecials() {
		if (ruLatSpecials == null) {
			ruLatSpecials = new Hashtable<String, String>();
			ruLatSpecials.put("Щаст", "Счаст");
			ruLatSpecials.put("щаст", "счаст");
			ruLatSpecials.put("ДЕЙАВУЕ", "DEJAVUE");
			ruLatSpecials.put("ДЕЯВУЕ", "DEJAVUE");
			ruLatSpecials.put("МАЫДАЫ", "MAYDAY");
			ruLatSpecials.put("Ф.А.Ц.Е", "F.A.C.E");
			ruLatSpecials.put("Данце", "Dance");
			ruLatSpecials.put("данце", "dance");
			ruLatSpecials.put("ДАНЦЕ", "DANCE");
			ruLatSpecials.put("Ума2рмах", "Ума2рман");
			ruLatSpecials.put("Ума2рмаХ", "Ума2рмаН");
			ruLatSpecials.put("Рефлех", "Reflex");
			ruLatSpecials.put("Ремих", "Remix");
			ruLatSpecials.put("ремих", "remix");
			ruLatSpecials.put("Т1оне", "T1one");
			ruLatSpecials.put("Алоена", "Алёна");
			ruLatSpecials.put("Вэнг", "Ваенг");
			ruLatSpecials.put("Шоди", "Сходи");
			ruLatSpecials.put("шоди", "сходи");
			ruLatSpecials.put("Киллах", "Killah");
			ruLatSpecials.put("Феат.", "Feat.");
			ruLatSpecials.put("феат.", "feat.");
			ruLatSpecials.put("Едит", "Edit");
			ruLatSpecials.put("Бест", "Best");
			ruLatSpecials.put("Геегун", "Geegun");
			ruLatSpecials.put("Парты", "Party");
			// ruLatSpecials.put("Проецт", "Project");
			ruLatSpecials.put("Проецт", "Проект");
			ruLatSpecials.put("ывэт", "ывает");
			ruLatSpecials.put("Цлуб", "Club");
			ruLatSpecials.put("Легостэв", "Легостаев");
			ruLatSpecials.put("Николэв", "Николаев");
			ruLatSpecials.put("Бахх Тее", "Bahh Tee");
			ruLatSpecials.put("летэшь", "летаешь");
			ruLatSpecials.put("игрэм", "играем");
			ruLatSpecials.put("поминэт", "поминает");
			ruLatSpecials.put("нэшь", "наешь");
			ruLatSpecials.put("5ивеста", "5ivesta");
			ruLatSpecials.put("5ИВЕСТА", "5IVESTA");
			ruLatSpecials.put("цз", "ц");
			ruLatSpecials.put("Юлиa", "Юлия");
			ruLatSpecials.put("Цустом", "Custom");
			ruLatSpecials.put("нэш", "наеш");
			ruLatSpecials.put("Цредо", "Credo");
			ruLatSpecials.put("Й-Поwер", "J-Power");
			ruLatSpecials.put("Олга", "Ольга");
			ruLatSpecials.put("Лубви", "Любви");
			ruLatSpecials.put("лубви", "любви");
			ruLatSpecials.put("Гроове", "Groove");
			ruLatSpecials.put("Ыля", "Юля");
			ruLatSpecials.put("Интернатёнал", "International");
			ruLatSpecials.put("Глюкьоза", "Глюк'оза");
			ruLatSpecials.put("зелоеные", "зеленые");
			ruLatSpecials.put("WИЛД", "ВИЛЬД");
			ruLatSpecials.put("Wилд", "Вильд");
			ruLatSpecials.put("Цоннецт", "Connect");
			ruLatSpecials.put("Сай", "Say");
			ruLatSpecials.put("бые", "bye");
			ruLatSpecials.put("Бые", "Bye");
			ruLatSpecials.put("Гоод", "Good");
			ruLatSpecials.put("Лухус", "Luxus");
			ruLatSpecials.put("Руссиан", "Russian");
			ruLatSpecials.put("Неw-Ыорк", "New-York");
			ruLatSpecials.put("Фуцк", "Fuck");
			ruLatSpecials.put("Ыоу", "You");
			ruLatSpecials.put("Зип", "Zip");
			ruLatSpecials.put("Меморы", "Memory");
			ruLatSpecials.put("Нэдине", "Наедине");
			ruLatSpecials.put("нэдине", "наедине");
			ruLatSpecials.put("МАРСЕЛ ", "МАРСЕЛЬ ");
			ruLatSpecials.put("Марсел ", "Марсель ");
			ruLatSpecials.put("Мелоды", "Melody");
			ruLatSpecials.put("СЕРИОГА", "СЕРЕГА");
			ruLatSpecials.put("Сериога", "Серёга");
			ruLatSpecials.put("Сероежа", "Серёжа");
			ruLatSpecials.put("МИТИА", "МИТЯ");
			ruLatSpecials.put("Митиа", "Митя");
			ruLatSpecials.put("Тебиа", "Тебя");
			ruLatSpecials.put("Ре-Флех", "Re-Flex");
			ruLatSpecials.put("Всио", "Все");
			ruLatSpecials.put("ЛИУДМИЛА", "ЛЮДМИЛА");
			ruLatSpecials.put("Лиудмила", "Людмила");
			ruLatSpecials.put("Елвира", "Ельвира");
			ruLatSpecials.put("Фамилы", "Family");
			ruLatSpecials.put("Твойих", "Твоих");
			ruLatSpecials.put("Чоет", "Чёт");
			ruLatSpecials.put("Бянка", "Бьянка");
			ruLatSpecials.put("Любов", "Любовь");
			ruLatSpecials.put("Любовьь", "Любовь");
			ruLatSpecials.put("Просциай", "Прощай");
			ruLatSpecials.put("Франкы", "Franky");
			ruLatSpecials.put("Радё", "Радио");
			ruLatSpecials.put("Студё", "Студио");
			ruLatSpecials.put("Радё", "Radio");
			ruLatSpecials.put("Версён", "Version");
			ruLatSpecials.put("версён", "version");
			ruLatSpecials.put("Дё", "Дио");
			ruLatSpecials.put("Гирл", "Girl");
			ruLatSpecials.put("Малыш", "Малышь");
			ruLatSpecials.put("Малышьь", "Малышь");
			ruLatSpecials.put("Лоунге", "Lounge");
			ruLatSpecials.put("Циты", "City");
			ruLatSpecials.put("Тхе", "The");
			ruLatSpecials.put("Тахи", "Taxi");
			ruLatSpecials.put("Лове", "Love");
			ruLatSpecials.put("Лады Д", "Lady D");
			// Лады Д Б
		}
		return ruLatSpecials;
	}

	public static String decodeLatRuEntities(String str, String charsetIn) {
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
		Map<String, String> specials = getLatSpecials();
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
			String xlat = (String) getLatRuEntities(charsetIn).get(tok);
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

	public static int processTagsForDirectory(String fromDirStr) throws FileNotFoundException, IOException {
		String charSet = StandardCharsets.UTF_8.name();
		String fileLat = fromDirStr + "/" + "tag_lat.txt";
		String fileRu = fromDirStr + "/" + "tag_ru.txt";

		List<String> fileListLat = getFileList(fromDirStr);
		if (fileListLat.isEmpty()) {
			return 0;
		}
		writeFile(fileLat, fileListLat, charSet);

		List<String> fileListRu = new ArrayList<>();

		for (String filLat : fileListLat) {
			fileListRu.add(decodeLatRuEntities(filLat, charSet));
		}
		writeFile(fileRu, fileListRu, charSet);
		return fileListLat.size();
	}

	public static void main(String[] args) throws IOException {

		// List<String> dirList =
		// getDirList("v:/Studio/Usb_003/01.Pop/Pop-06.1");
		List<String> dirList = getDirList("d:/Studio/Musik/_Stream/Audials/_process/ru");
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
		System.out.println(decodeLatRuEntities(istr, charSetIn));
	}

}
