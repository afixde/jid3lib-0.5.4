package fix.mp3.demo.unicode;

import com.gtranslate.Translator;

public class TranslateGt {

	public static void main(String[] args) {
		String text = "Sergej Minaev - Gimn Diskoteki";
		Translator translator = Translator.getInstance();
		// System.out.println(translator.detect(text));
		System.out.println(translator.translate(text, "de", "ru"));

	}

}
