package fix.mp3.demo.unicode;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.gtranslate.Language;

public class TranslateByGoogle {

	public static void main(String[] args) {

		List<String> toTranslate = Arrays.asList(
				// Pass in list of strings to be translated
				"Pugachiova - Tianet, tjanet, tyanet", "АЛЛА ПУГАЧИОВА - Тианет Сердце Руки", "Мало така мало",
				"АЛёша - Ты мое все (remix)", "Тyанет");

		TranslationsListResponse response = translateList(toTranslate, Language.ENGLISH, Language.RUSSIAN);
		//		TranslationsListResponse response = translateList(toTranslate, Language.BULGARIAN, Language.RUSSIAN);
		for (TranslationsResource tr : response.getTranslations()) {
			System.out.println(tr.getTranslatedText());
		}
	}

	public static TranslationsListResponse translateList(List<String> toTranslate, String frLanguage, String toLanguage) {
		// See comments on
		// https://developers.google.com/resources/api-libraries/documentation/translate/v2/java/latest/
		// on options to set
		try {
			Translate t = null;

			t = new Translate.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), null)
					// Need to update this to your App-Name
					.setApplicationName("Stackoverflow-Example").build();
			Translate.Translations.List list = null;
			try {
				list = t.new Translations().list(toTranslate, toLanguage);
				if (frLanguage != null) {
					list.setSource(frLanguage);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Set your API-Key from https://console.developers.google.com/
			String apiKey = System.getenv("GOOGLE_API_KEY");
			list.setKey(apiKey);
			TranslationsListResponse response = null;
			response = list.execute();
			return response;
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}