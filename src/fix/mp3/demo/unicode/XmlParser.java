package fix.mp3.demo.unicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class XmlParser {

	private static BufferedReader reader;

	public XmlParser() {
		;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Document doc = null;

		// reader = new BufferedReader(new InputStreamReader(System.in));
		// String name = getUserInput("What is your name?");

		File f = new File("xml_file.xml");

		try {
			// Das Dokument erstellen
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(f);
			XMLOutputter fmt = new XMLOutputter();

			// komplettes Dokument ausgeben
			fmt.output(doc, System.out);

			// Wurzelelement ausgeben
			Element element = doc.getRootElement();
			System.out.println("\nWurzelelement: " + element);

			// Wurzelelementnamen ausgeben
			System.out.println("Wurzelelementname: " + element.getName());

			// Eine Liste aller direkten Kindelemente eines Elementes erstellen
			List<?> alleKinder = (List<?>) element.getChildren();
			System.out.println("Erstes Kindelement: " + ((Element) alleKinder.get(0)).getName());

			// Eine Liste aller direkten Kindelemente eines benannten
			// Elementes erstellen
			List<?> benannteKinder = element.getChildren("OptionsList");

			Element ele = ((Element) benannteKinder.get(0));
			List<?> optionsKinder = ele.getChildren("Options");

			// Das erste Kindelement ausgeben
			System.out.println("options Kindelement: " + optionsKinder.get(0));

			Element opt = (Element) optionsKinder.get(0);
			System.out.println("opt: " + opt.getAttributes());

			List<Attribute> attrs = (List<Attribute>) opt.getAttributes();
			// for (int i = 0; i < attrs.size(); i++)
			for (Attribute attr : attrs) {
				System.out.println("attr.name = " + attr.getName() + ", attr.value = " + attr.getValue());
			}

			String id = opt.getAttributeValue("id");
			// Das erste Kindelement ausgeben
			System.out.println("id: " + id);
			//
			// // Wert eines bestimmten Elementes ausgeben
			// String kind = element.getAttributeValue("NumberOfAisle");
			// System.out.println("NumberOfAisle: " + kind);
			//
			// // Attribut ausgeben
			// Element kind2 = element.getChild("NumberOfXCoord");
			// System.out.println("Ortsname: " +
			// kind2.getAttributeValue("name"));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: missing file: " + f.getPath() + "\n");
			e.printStackTrace();
		}
	}

	// gets user input response to supplied prompt
	@SuppressWarnings("unused")
	private static String getUserInput(String prompt) {
		if (reader == null)
			reader = new BufferedReader(new InputStreamReader(System.in));
		String userinput = "";
		while (userinput.equals("")) {
			System.out.println(prompt);
			try {
				userinput = reader.readLine();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		return userinput;
	}

}
