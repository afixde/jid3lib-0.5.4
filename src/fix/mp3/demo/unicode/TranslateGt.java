package fix.mp3.demo.unicode;

import com.gtranslate.Language;
import com.gtranslate.Translator;
import com.gtranslate.text.Text;
import com.gtranslate.text.TextTranslate;

public class TranslateGt {

	public static void main(String[] args) {
		String input = "Sergej Minaev - Gimn Diskoteki";
		Translator translator = Translator.getInstance();

		// System.out.println(translator.detect(text));

		System.out.println(translator.translate(input, Language.GERMAN, Language.RUSSIAN));

		String output = "";
		Text text = new Text(input, Language.RUSSIAN);
		TextTranslate textTranslate = new TextTranslate(text, output);
		translator.translate(textTranslate);
		System.out.println(textTranslate.toString());
	}

}
