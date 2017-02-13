package fix.mp3.demo.unicode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.AbstractID3v2Frame;
import org.farng.mp3.id3.FrameBodyCOMM;
import org.farng.mp3.id3.FrameBodyTALB;
import org.farng.mp3.id3.FrameBodyTCON;
import org.farng.mp3.id3.FrameBodyTCOP;
import org.farng.mp3.id3.FrameBodyTDRC;
import org.farng.mp3.id3.FrameBodyTIT2;
import org.farng.mp3.id3.FrameBodyTPE1;
import org.farng.mp3.id3.FrameBodyTRCK;
import org.farng.mp3.id3.FrameBodyWXXX;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_4;
import org.farng.mp3.id3.ID3v2_4Frame;
import org.farng.mp3.object.AbstractMP3Object;

/*
 * Provides a simple to use wrapper to access MP3 metadata, primarily ID3v2 tags.
 */
public class MP3 {

	private MP3File mp3data = null;
	private MP3Properties mp3props = null;

	/**
	 * Constructor
	 * 
	 * @param filename
	 *            absolute fielpath to mp3 file
	 */
	public MP3(String filename) {

		try {

			mp3props = new MP3Properties();
			// mp3props = new MP3Properties(new File(filename));
			if (mp3props == null) {
				throw new RuntimeException("could not read mp3 properties for:" + filename);
			}
			mp3data = new MP3File(filename);
			if (mp3data == null) {
				throw new RuntimeException("could not read mp3 data for:" + filename);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MP3(File file) {

		try {

			mp3props = new MP3Properties();
			// mp3props = new MP3Properties(new File(filename));
			if (mp3props == null) {
				throw new RuntimeException("could not read mp3 properties for:" + file.getAbsolutePath());
			}
			mp3data = new MP3File(file, true);
			if (mp3data == null) {
				throw new RuntimeException("could not read mp3 data for: " + file.getAbsolutePath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getters...

	/**
	 * Returns the duration of the mp3 file in seconds.
	 */
	public long getDuration() {
		// System.out.println("DURATION:"+mp3props.getLength());
		return mp3props.getLength();
	}

	/**
	 * Returns the ID3v2 title, if none, then the ID3v1 title or if neither
	 * exist, returns null.
	 */
	public String getTitle() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getSongTitle();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getSongTitle();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 artist, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getArtist() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getLeadArtist();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getLeadArtist();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 album, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getAlbum() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getAlbumTitle();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getAlbumTitle();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 year, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getYear() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getYearReleased();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getYearReleased();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 artist, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getTrack() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getTrackNumberOnAlbum();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getTrackNumberOnAlbum();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 artist, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getAuthor() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getAuthorComposer();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getAuthorComposer();
		} else {
			return null;
		}
	}

	/**
	 * Returns the ID3v2 artist, if none, then the ID3v1 artist or if neither
	 * exist, returns null.
	 */
	public String getGenre() {
		if (mp3data.hasID3v2Tag()) {
			return mp3data.getID3v2Tag().getSongGenre();
		} else if (mp3data.hasID3v1Tag()) {
			return mp3data.getID3v1Tag().getSongGenre();
		} else {
			return null;
		}
	}

	// setters...
	/**
	 * Set title and save change immediately to mp3 file.
	 */
	public void setTitle(String title) {

		ID3v1 id3v1 = mp3data.getID3v1Tag();
		if (id3v1 != null) {
			id3v1.setTitle(title);
			mp3data.setID3v1Tag(id3v1);
		}

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TIT2");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTIT2((byte) 0, title.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTIT2) field.getBody()).setText(title.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set artist and save change immediately to mp3 file.
	 */
	public void setArtist(String artist) {

		ID3v1 id3v1 = mp3data.getID3v1Tag();
		if (id3v1 != null) {
			id3v1.setArtist(artist);
			mp3data.setID3v1Tag(id3v1);
		}

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TPE1");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTPE1((byte) 0, artist.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTPE1) field.getBody()).setText(artist.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set artist and save change immediately to mp3 file.
	 */
	public void setAlbum(String album) {

		ID3v1 id3v1 = mp3data.getID3v1Tag();
		if (id3v1 != null) {
			id3v1.setAlbum(album);
			mp3data.setID3v1Tag(id3v1);
		}

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TALB");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTALB((byte) 0, album.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTALB) field.getBody()).setText(album.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set artist and save change immediately to mp3 file.
	 */
	public void setYear(String year) {

		ID3v1 id3v1 = mp3data.getID3v1Tag();
		if (id3v1 != null) {
			id3v1.setYear(year);
			mp3data.setID3v1Tag(id3v1);
		}

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TDRC");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTDRC((byte) 0, year.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTDRC) field.getBody()).setText(year.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// setters...
	/**
	 * Set title and save change immediately to mp3 file.
	 */
	public void setTrackno(String number) {

		// ID3v1 id3v1 = mp3data.getID3v1Tag();
		// if (id3v1 != null) {
		// id3v1.setTrackNumberOnAlbum(number);
		// mp3data.setID3v1Tag(id3v1);
		// }
		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TRCK");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTRCK((byte) 0, number.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTRCK) field.getBody()).setText(number.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set artist and save change immediately to mp3 file.
	 */
	public void setComment(String comment) {

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("COMM");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyCOMM((byte) 0, "ENG", "", comment.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyCOMM) field.getBody()).setText(comment.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set artist and save change immediately to mp3 file. MUST be 4 digit year
	 * place space as pseicifed by ID3 2.4 spec:
	 *
	 * The 'Copyright message' frame, in which the string must begin with a year
	 * and a space character (making five characters)
	 *
	 */
	public void setCopyright(String copyright) {

		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TCOP");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTCOP((byte) 0, copyright.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTCOP) field.getBody()).setText(copyright.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set genre and save change immediately to mp3 file.
	 */
	// public void setGenre(String genre) {
	//
	// ID3v2_4 id3v24 = getIDv2Tag();
	//
	// AbstractID3v2Frame field = id3v24.getFrame("");
	// if (field == null) {
	// field = new ID3v2_4Frame(new FrameBody((byte) 0, title.trim()));
	// id3v24.setFrame(field);
	// } else {
	// ((FrameBodyTIT2) field.getBody()).setText(title.trim());
	// }
	//
	// try {
	// mp3data.setID3v2Tag(id3v24);
	// mp3data.save();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	/**
	 * Set URL and save change immediately to mp3 file.
	 */
	public void setUrl(String url) {

		ID3v2_4 id3v24 = getIDv2Tag();

		// AbstractID3v2Frame field = id3v24.getFrame("WXXX");
		// FrameBodyWXXX frame = new FrameBodyWXXX();
		// frame.setUrlLink(url.trim());
		// field = new ID3v2_4Frame(frame);
		// id3v24.setFrame(field);

		AbstractID3v2Frame field = id3v24.getFrame("WXXX");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyWXXX((byte) 0, "the url", url.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyWXXX) field.getBody()).setUrlLink(url.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Return a new v2.4 tag if none already exists in this mp3 file
	 * 
	 * @return
	 */
	private ID3v2_4 getIDv2Tag() {
		ID3v2_4 nuTag = new ID3v2_4();

		AbstractID3v2 v2tag = mp3data.getID3v2Tag();
		if (v2tag != null && (v2tag instanceof ID3v2_4)) {
			nuTag = (ID3v2_4) v2tag;
		} else {
			System.out.println("using new v2.4 tag");
		}
		return nuTag;
	}

	public void setGenre(String genre) {
		ID3v2_4 id3v24 = getIDv2Tag();

		AbstractID3v2Frame field = id3v24.getFrame("TCON");
		if (field == null) {
			field = new ID3v2_4Frame(new FrameBodyTCON((byte) 0, genre.trim()));
			id3v24.setFrame(field);
		} else {
			((FrameBodyTCON) field.getBody()).setText(genre.trim());
		}

		try {
			mp3data.setID3v2Tag(id3v24);
			mp3data.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// private static class Options {
	// String Name;
	// boolean enabled;
	// boolean checkarg;
	// }
	private static Map<String, String> optionEntities = new Hashtable<String, String>();;

	private synchronized static String getOption(String key) {
		return (String) optionEntities.get(key);
	}

	private synchronized static boolean putOption(String key, String entity) {
		if (getOption(key) != null) {
			return false;
		}
		optionEntities.put(key, entity);
		return true;
	}

	private static final String usage = "Usage: MP3 [-t title] [-i interpret] [-y year] [-n trackno] [-a album] [-g genre] file\n"
			+ "  set one or more specified tags (3v2.4) for specified mp3 file\n"
			+ "  i.e. MP3 -t \"Song name\" -i \"Interpret\" file.mp3";

	public static void main2(String[] args) throws IOException, TagException {
		// String fromFileStr = ".";
		// String charSet = "UTF-16";
		// String charSetId = "UTF-16";
		// String fileName = null;
		//
		// String Title = null;
		// String Interpret = null;
		// String Number = null;
		// String Year = null;
		// String Genre = null;
		// boolean title = false;
		// boolean interpret = false;
		// boolean number = false;
		// boolean year = false;
		// boolean genre = false;
		// boolean newfile = true;

		String option = null;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				// new option starting
				if (option != null) {
					System.out.println("ERROR: please complete the option: " + option + "!");
					System.out.println(usage);
					System.exit(1);
				}
				if ("-t".equals(arg)) {
					option = "Title";
				} else if ("-i".equals(arg)) {
					option = "Interpret";
				} else if ("-t".equals(arg)) {
					option = "Trackno";
				} else if ("-y".equals(arg)) {
					option = "Year";
				} else if ("-a".equals(arg)) {
					option = "Album";
				} else if ("-g".equals(arg)) {
					option = "Genre";
				} else {
					System.out.println("ERROR: unknon option: " + arg + "!");
					System.out.println(usage);
					System.exit(1);
				}
			} else if (option != null) {
				// value for new option
				if (!MP3.putOption(option, arg)) {
					System.out.println("ERROR: value for option: " + option + " already exists!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// option complete
					option = null;
				}
			} else {
				// neither option nor value for option, so it must be the File
				if (!MP3.putOption("File", arg)) {
					System.out.println("ERROR: multiple file specification!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// option complete
					option = null;
				}
			}
		}
		if (optionEntities == null || optionEntities.size() < 2) {
			System.out.println("ERROR: incomplete specification!");
			System.out.println(usage);
			System.exit(1);
		} else if (getOption("File") == null) {
			System.out.println("ERROR: missing file specification!");
			System.out.println(usage);
			System.exit(1);
		}
		String value = getOption("File");
		MP3 mp3f = new MP3(value);
		System.out.println("Settings for MP3 file = " + value + " ...");
		value = getOption("Title");
		if (value != null) {
			System.out.println("Setting Title = " + value);
			mp3f.setTitle(value);
		}
		value = getOption("Interpret");
		if (value != null) {
			System.out.println("Setting Interpret = " + value);
			mp3f.setArtist(value);
		}
		value = getOption("Trackno");
		if (value != null) {
			System.out.println("Setting Track No = " + value);
			mp3f.setTrackno(value);
		}
		value = getOption("Year");
		if (value != null) {
			System.out.println("Setting Year = " + value);
			mp3f.setYear(value);
		}
		value = getOption("Album");
		if (value != null) {
			System.out.println("Setting Album = " + value);
			mp3f.setAlbum(value);
		}
		value = getOption("Genre");
		if (value != null) {
			System.out.println("Setting Genre = " + value);
			mp3f.setGenre(value);
		}

		// System.out.println(" has ID3v2Tag");
		// System.out.println(" - SongTitle: " +
		// AnsiDecoder.decodeRuLatEntities(mp3f.getTitle(), charSetId));
		// System.out.println(" - LeadArtist: " +
		// AnsiDecoder.decodeRuLatEntities(mp3f.getArtist(), charSetId));
		// System.out.println(" - AlbumTitle: " +
		// AnsiDecoder.decodeRuLatEntities(f.getAlbumTitle(), charSetId));
		// System.out.println(" - SongGenre: " +
		// AnsiDecoder.decodeRuLatEntities(f.getSongGenre(), charSetId));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, TagException {
		int[] rating = { 0, 13, 23, 54, 64, 118, 128, 186, 196, 242, 252 };

		String option = null;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				// new option starting
				if (option != null) {
					System.out.println("ERROR: please complete the option: " + option + "!");
					System.out.println(usage);
					System.exit(1);
				}
				if ("-t".equals(arg)) {
					option = "Title";
				} else if ("-i".equals(arg)) {
					option = "Interpret";
				} else if ("-t".equals(arg)) {
					option = "Trackno";
				} else if ("-y".equals(arg)) {
					option = "Year";
				} else if ("-a".equals(arg)) {
					option = "Album";
				} else if ("-g".equals(arg)) {
					option = "Genre";
				} else {
					System.out.println("ERROR: unknon option: " + arg + "!");
					System.out.println(usage);
					System.exit(1);
				}
			} else if (option != null) {
				// value for new option
				if (!MP3.putOption(option, arg)) {
					System.out.println("ERROR: value for option: " + option + " already exists!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// option complete
					option = null;
				}
			} else {
				// neither option nor value for option, so it must be the File
				if (!MP3.putOption("File", arg)) {
					System.out.println("ERROR: multiple file specification!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// option complete
					option = null;
				}
			}
		}
		// if (optionEntities == null || optionEntities.size() < 2) {
		// System.out.println("ERROR: incomplete specification!" );
		// System.out.println(usage);
		// System.exit(1);
		// }
		if (getOption("File") == null) {
			System.out.println("ERROR: missing file specification!");
			System.out.println(usage);
			System.exit(1);
		}

		String fromFileStr = getOption("File");
		File file = new java.io.File(fromFileStr);
		List<String> dir = null;
		String[] optdir = null;
		@SuppressWarnings("unused")
		File[] fromFile = null;
		if (file.isDirectory()) {
			optdir = file.list(); // Get list of names
			fromFile = file.listFiles();
		} else if (file.isFile()) {
			optdir = new String[] { fromFileStr };
			fromFile = new File[] { file };
		}
		java.util.Arrays.sort(optdir); // Sort it (Data Structuring chapter))
		System.out.println("System files in " + fromFileStr + ":"); // Print the
																	// list
		dir = new ArrayList<String>();
		for (int i = 0; i < optdir.length; i++) {
			if (optdir[i].matches(".*\\.[Mm][Pp]3$")) {
				dir.add(optdir[i]);
				System.out.println(" - " + optdir[i]); // Print the list
			}
		}

		for (String fname : dir) {
			String value = file.getAbsolutePath() + "\\" + fname;
			MP3 mp3f = new MP3(value);
			System.out.println(" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Tags of MP3 file = " + value + ":");

			if (!mp3f.mp3data.hasID3v2Tag()) {
				System.out.println("missing ID3v2 tag!");
				System.exit(0);
			}
			Iterator<AbstractID3v2Frame> iter = mp3f.mp3data.getID3v2Tag().iterator();
			while (iter.hasNext()) {
				AbstractID3v2Frame frame = iter.next();
				String id = frame.getIdentifier().substring(0, 4);
				if (!"POPM".equals(id))
					continue;

				System.out.println(" frame.id: " + frame.getIdentifier().substring(0, 4));

				Iterator<AbstractMP3Object> it = (Iterator<AbstractMP3Object>) frame.getBody().iterator();
				while (it.hasNext()) {
					@SuppressWarnings("unused")
					AbstractMP3Object obj = it.next();
					// System.out.println(" obj.id = " + obj.getIdentifier() +
					// ", obj.val = " + obj.getValue());
				}
				Object rat = frame.getBody().getObject("Rating");
				Long val = 0L;
				long uns = 0;
				if (rat instanceof Long) {
					val = (Long) rat;
					uns = val;
					if (val < 0)
						// uns = (val * -1) + 128;
						uns = val + 256;
				}
				double rate = 0.0;
				for (int i = 0; i < rating.length; i++) {
					if (Math.abs(rating[i] - uns) < 5) {
						rate = 0.5 * i;
						break;
					}
				}
				// System.out.println(" rat = " + rat.toString());
				System.out.println(" val = " + val);
				System.out.println(" uns = " + uns);
				System.out.println(" rate = " + rate);

				// System.out.println(" frame.body: " + frame.getBody());
				// System.out.println(" frame.id: " +
				// AbstractID3v2Frame.isValidID3v2FrameIdentifier(id));
				// System.out.println(" frame: " + frame.toString());
			}
		}
	}

}
