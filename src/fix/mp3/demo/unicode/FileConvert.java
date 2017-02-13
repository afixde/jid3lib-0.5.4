package fix.mp3.demo.unicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FileConvert {
	public static class FileTags {
		String TrackNo = "";
		String Artist = "";
		String Title = "";
		String Album = "";
		String Genre = "";
		String Year = "";

		public FileTags() {
		}

		public FileTags(String no, String intp, String tit) {
			TrackNo = no;
			Artist = intp;
			Title = tit;
		}

		public FileTags(String no, String intp, String tit, String alb, String gen, String year) {
			TrackNo = no;
			Artist = intp;
			Title = tit;
			Album = alb;
			Genre = gen;
			Year = year;
		}

		public String toString() {
			return "SongTitle:  \t" + Title + "\nLeadArtist: \t" + Artist + "\nAlbumTitle: \t" + Album
					+ "\nLeSongGenre:\t" + Genre + "\nYear:       \t" + Year + "\nTrackNo:    \t" + TrackNo;
		}
	}

	public static class ProfileCharSet {
		String key = "";
		String charSet = "";
		String charSetIn = "";
		String charSetId = "";

		public ProfileCharSet() {

		}

		public ProfileCharSet(String id, String cset) {
			key = id;
			charSet = cset;
			charSetIn = cset;
			charSetId = cset;
		}

		public ProfileCharSet(String id, String cset, String csetin) {
			key = id;
			charSet = cset;
			charSetIn = csetin;
			charSetId = cset;
		}

		public ProfileCharSet(String id, String cset, String csetin, String csetid) {
			key = id;
			charSet = cset;
			charSetIn = csetin;
			charSetId = csetid;
		}
	}

	private static final ProfileCharSet[] profCharSet = { new ProfileCharSet("win", "Cp1251", "Cp1252"),
			new ProfileCharSet("dos", "Cp866", "Cp850"), new ProfileCharSet("utf8", "UTF8", "UTF8"),
			new ProfileCharSet("utf16", "UTF-16", "UTF-16") };

	private static Map<String, String> optionEntities = new Hashtable<String, String>();;

	private synchronized static String getOption(String key) {
		return (String) optionEntities.get(key);
	}

	private synchronized static boolean putOption(String key, String entity) {
		if (getOption(key) != null && getOption("Force") == null) {
			return false;
		}
		optionEntities.put(key, entity);
		return true;
	}

	private synchronized static String getCapitalizedCase(String inString) {
		String outString = "";
		String[] vString = inString.split("\\s");
		for (String word : vString) {
			if (!"".equals(outString)) {
				outString += " ";
			}
			if (word.length() > 0) {
				outString += word.substring(0, 1).toUpperCase()
						+ (word.length() > 1 ? word.substring(1).toLowerCase() : "");
			}
		}
		return outString;
	}

	/**
	 * converts the input string to required string case:<br>
	 * - u: to upper<br>
	 * - l: to lower<br>
	 * - c: to capitalized<br>
	 *
	 * @param inString
	 * @param optCase
	 * @return
	 */
	private synchronized static String toStringCase(String inString, String optCase) {
		if ("u".equals(optCase)) {
			return inString.toUpperCase();
		}
		if ("l".equals(optCase)) {
			return inString.toLowerCase();
		}
		if ("c".equals(optCase)) {
			return getCapitalizedCase(inString);
		}
		return inString;
	}

	private static final String usage =
	// "Usage: FileConvert [-ch char set base] [-cs char set] [-ci char set for
	// tag-id] [-n new file] dir/file\n"
	"Usage: FileConvert [-t title] [-i interpret] [-y year] [-n trackno] [-a album] [-g genre]\n"
			+ "                   [-ch charset] [-co charset out] [-ci charset in] [-cid charset tag]\n"
			+ "                   [-c win/dos/utf8/utf16 prepared charset profiles (win = default)]\n"
			+ "                   [-p @ parse tags from file] \n"
			+ "                   [-f list file (using file list for tagging)]\n"
			+ "                   [--fc filenamecase: u(pper)/l(ower)/c(capitalized)\n"
			+ "                   [-x execute new file] dir/file\n"
			+ "  set one or more specified tags (3v2.4) for specified mp3 file\n"
			+ "		and convert the files and the tags into latin char set\n"
			+ "  i.e. FileConvert  -ci Cp850 -ch Cp866 file.mp3 - convert from DOS char set\n"
			+ "  i.e. FileConvert  -c dos file.mp3 - dito. convert from DOS char set\n"
			+ "  i.e. FileConvert -t \"Song name\" -i \"Interpret\" file.mp3\n"
			+ "  i.e. FileConvert -x file.mp3     \n"
			+ "                 - create new file, convert by default Cp1252 (Win) charset\n"
			+ "  i.e. FileConvert c:\\music -n @ -t @ -i @ -a @ -y 2008 -g Pop -x --fc c\n"
			+ "                 - create new file from directory c:\\music\n"
			+ "                 - track/title/interpret/album extract from file name: \n"
			+ "                         examples for default contents:\n"
			+ "                         001. Sergej Minaev - Gimn Diskoteki 80-kh\n"
			+ "                         001 - Sergej Minaev - Gimn Diskoteki 80-kh\n"
			+ "                         001_Sergej Minaev - Gimn Diskoteki 80-kh\n"
			+ "                         NNN IIIIIIIIIIIII   TTTTTTTTTTTTTTTTTTTT\n"
			+ "                         N - track No.\n" + "                         I - Interpret\n"
			+ "                         T - song Title\n" + "                 - file name in capitalized case, i.e:\n"
			+ "                         DIMA BILAN -> Dima Bilan\n"
			+ "  i.e. FileConvert c:\\music -p @ -y 2008 -g Pop -x --fc c\n"
			+ "                 - same as above. Option -p @ substitutes (-n @ -t @ -i @ -a @)\n";

	public static void main(String[] args) throws Exception {
		String fromFileStr = ".";
		// String charSetIn = "UTF-16";
		// String charSet = "UTF-16";
		// String charSetId = "UTF-16";
		String charSetIn = "Cp1251";
		String charSet = "Cp1252";
		String charSetId = "Cp1252";
		boolean newfile = false;
		boolean withint = true; // file name includes interpret
		boolean withtrk = true; // file name includes track no.
		boolean withtit = true; // file name includes song title

		buildOptions(args);

		if (optionEntities == null || optionEntities.size() < 0) {
			System.out.println("ERROR: incomplete specification!");
			System.out.println(usage);
			System.exit(1);
		} else if (getOption("File") == null) {
		} else {
			fromFileStr = getOption("File");
			if (fromFileStr.endsWith("\\"))
				fromFileStr.substring(0, fromFileStr.length() - 1);
			if (fromFileStr.endsWith("\""))
				fromFileStr.substring(0, fromFileStr.length() - 1);
		}
		// boolean values:
		if ("true".equals(getOption("NewFile")))
			newfile = true;
		if ("false".equals(getOption("WithInterpret")))
			withint = false;
		if ("false".equals(getOption("WithTrack")))
			withtrk = false;
		if ("false".equals(getOption("WithTitle")))
			withtit = false;

		/**
		 * Options CharSet
		 */
		if (getOption("ProfileCharSet") != null) {
			String key = getOption("ProfileCharSet");
			boolean found = false;
			for (ProfileCharSet pcs : profCharSet)
				if (pcs.key.equals(key)) {
					found = true;
					charSet = pcs.charSet;
					charSetIn = pcs.charSetIn;
					charSetId = pcs.charSetId;
					break;
				}
			if (!found) {
				System.out.println("ERROR: unknown charset profile:" + key + "!");
				System.out.println(usage);
				System.exit(1);
			}
		}
		if (getOption("CharSetBase") != null) {
			charSet = getOption("CharSetBase");
			charSetId = getOption("CharSetBase");
			charSetIn = getOption("CharSetBase");
		}
		if (getOption("CharSetIn") != null) {
			charSetIn = getOption("CharSetIn");
		}
		if (getOption("CharSetOut") != null) {
			charSet = getOption("CharSetOut");
			charSetId = getOption("CharSetOut");
		}
		if (getOption("CharSet") != null) {
			charSet = getOption("CharSet");
		}
		if (getOption("CharSetId") != null) {
			charSetId = getOption("CharSetId");
		}

		// file list option
		String listFileStr = getOption("FileList");

		File file = new java.io.File(fromFileStr);
		List<String> dir = null;
		String[] optdir = null;
		File[] fromFile = null;
		List<File> listFrom = new ArrayList<File>();
		if (file.isDirectory()) {
			optdir = file.list(); // Get list of names
			File[] files = file.listFiles();
			for (File fil : files) {
				if (!fil.isFile())
					continue;
				if (!fil.getName().toLowerCase().endsWith("mp3"))
					continue;
				listFrom.add(fil);
			}
			fromFile = new File[listFrom.size()];
			for (int i = 0; i < listFrom.size(); i++) {
				fromFile[i] = listFrom.get(i);
			}
		} else if (file.isFile()) {
			optdir = new String[] { fromFileStr };
			fromFile = new File[] { file };
		}
		if (optdir == null) {
			System.out.println("No files found in: " + fromFileStr); // Print
																		// the
																		// list
		} else {
			java.util.Arrays.sort(optdir); // Sort it (Data Structuring
											// chapter))
			System.out.println("System files in " + fromFileStr + ":"); // Print
																		// the
																		// list
			dir = new ArrayList<String>();
			for (int i = 0; i < optdir.length; i++) {
				if (optdir[i].matches(".*\\.[Mm][Pp]3$")) {
					dir.add(optdir[i]);
					System.out.println(" - " + optdir[i]); // Print the list
				}
			}
			List<String> xlatList = null;
			if (listFileStr != null) {
				xlatList = TranslateList.translateFileList(listFileStr, charSet, charSetIn);
				if (xlatList.size() != dir.size()) {
					System.out.println("ERROR: the size of the translation list "
							+ "does not match the number of files! (" + xlatList.size() + " <> " + dir.size() + ")");
					for (int i = 0; i < dir.size(); i++) {
						String xfil = xlatList.get(i);
						if (!xfil.matches(".*\\.[Mm][Pp]3$")) {
							xfil = xlatList.get(i).trim() + ".mp3";
							xlatList.set(i, xfil);
						}
						System.out.println(dir.get(i) + "\t -> " + xlatList.get(i));
					}
					System.exit(0);
				}
				System.out.println(" about to translate in following way:");
				for (int i = 0; i < dir.size(); i++) {
					String xfil = xlatList.get(i);
					if (!xfil.matches(".*\\.[Mm][Pp]3$")) {
						xfil = xlatList.get(i).trim() + ".mp3";
						xlatList.set(i, xfil);
					}
					System.out.println(dir.get(i) + "\t -> " + xlatList.get(i));
				}
				System.out.print(" Translate the list [y/n] = ");

				byte buffer[] = new byte[1]; // Zeichenpuffer
				String input = "";
				int read;
				read = System.in.read(buffer, 0, 1);
				// Umwandeln des Pufferinhaltes in einen String
				input = new String(buffer, 0, read);
				// Ausgabe der eingelesenen Zeichen
				// System.out.print(input);

				if (!input.matches("[yYjJ]")) {
					System.out.println("exit");
					System.exit(0);
				}
			}

			System.out.println(); // Print the list
			System.out.println("-----------------------------------------------------------"); // Print
																								// the
																								// list
			System.out.println("Translated files in " + fromFileStr + " (Code = [" + charSet + "]):"); // Print
																										// the
																										// list
			for (String fil : dir) {
				System.out.println(" - " + getPrintable(AnsiDecoder.decodeRuLatEntitiesDeep(fil, charSet, charSetIn))); // Print
																														// the
																														// list
			}
			System.out.println(); // Print the list
			System.out.println("-----------------------------------------------------------"); // Print
																								// the
																								// list
			System.out.println("MP3 Tags in " + fromFileStr + ":"); // Print the
																	// list
			for (int i = 0; i < dir.size(); i++) {
				String fil = dir.get(i);
				if (fil.matches(".*.[mM][pP]3$")) {
					String decFileName = "";

					if (xlatList != null) {
						decFileName = xlatList.get(i);
					} else {
						decFileName = AnsiDecoder.decodeRuLatEntitiesDeep(fil, charSet, charSetIn);
					}
					System.out.println(" - " + decFileName); // Print the list
					MP3 mp3f = new MP3(fromFile[i]);

					FileTags orgTags = new FileTags(getPrintable(mp3f.getTrack()), getPrintable(mp3f.getArtist()),
							getPrintable(mp3f.getTitle()), getPrintable(mp3f.getAlbum()), getPrintable(mp3f.getGenre()),
							getPrintable(mp3f.getYear()));

					FileTags filTags = new FileTags();
					filTags.Title = getPrintable(
							AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.Title, charSet, charSetId));
					filTags.Artist = getPrintable(
							AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.Artist, charSet, charSetId));
					filTags.Album = getPrintable(
							AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.Album, charSet, charSetId));
					filTags.Genre = getPrintable(
							AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.Genre, charSet, charSetId));
					filTags.Year = getPrintable(AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.Year, charSet, charSetId));
					filTags.TrackNo = getPrintable(
							AnsiDecoder.decodeRuLatEntitiesDeep(orgTags.TrackNo, charSet, charSetId));

					FileTags parsedFileName = parseFileName(decFileName, withtrk, withint, withtit,
							getOption("optFileNameCase"));

					/**
					 * Options
					 */
					// Parsed Tags handling in general
					// optParseTagsFromFile
					if ("@".equals(getOption("optParseTagsFromFile"))) {
						filTags.Title = parsedFileName.Title;
						filTags.Artist = parsedFileName.Artist;
						filTags.TrackNo = parsedFileName.TrackNo;
						if (file.isDirectory()) {
							filTags.Album = file.getName();
						}
					}
					// Title
					if ("@".equals(getOption("Title"))) {
						filTags.Title = parsedFileName.Title;
					} else if (getOption("Title") != null && dir.size() == 1) {
						filTags.Title = getOption("Title");
					} else if (filTags.Title == null || filTags.Title.length() == 0) {
						if (parsedFileName.Title.length() == 0) {
							filTags.Title = AnsiDecoder.decodeRuLatEntities(
									AnsiDecoder.decodeRuLatEntitiesDeep(
											decFileName.substring(0, decFileName.length() - 4), charSet, charSetIn),
									charSetIn);
						} else {
							filTags.Title = parsedFileName.Title;
						}
					}
					// Artists
					if ("@".equals(getOption("Artist"))) {
						filTags.Artist = parsedFileName.Artist;
					} else if (getOption("Artist") != null) {
						filTags.Artist = getOption("Artist");
					} else if (filTags.Artist == null || filTags.Artist.length() == 0) {
						if (getOption("Artist") != null) {
							filTags.Artist = getOption("Artist");
						} else {
							filTags.Artist = parsedFileName.Artist;
						}
					}

					// Trackno
					// if (getOption("Trackno") != null && (fTrack == null ||
					// fTrack.isEmpty() || fTrack.equals("0"))) {
					if (getOption("Trackno") != null) {
						String trk = getOption("Trackno");
						if (trk.equals("@") && decFileName.length() >= 1) {
							// strip two first chars from file name
							filTags.TrackNo = stripTrackNo(decFileName, 0);
						} else if (trk.equals("@@") && decFileName.length() >= 2) {
							// strip two first chars from file name
							filTags.TrackNo = stripTrackNo(decFileName, 2);
						} else if (trk.equals("@@@") && decFileName.length() >= 3) {
							// strip three first chars from file name
							filTags.TrackNo = stripTrackNo(decFileName, 3);
						} else if (trk.startsWith("##") || trk.startsWith("00")) {
							// build by current id base = two chars
							filTags.TrackNo = new DecimalFormat(trk).format(i + 1);
						} else {
							filTags.TrackNo = null;
						}
					}
					// Album
					if ("@".equals(getOption("Album"))) {
						if (file.isDirectory()) {
							filTags.Album = file.getName();
						} else if (file.isFile()) {
							filTags.Album = file.getParent();
						} else {
							filTags.Album = null;
						}
					} else if (getOption("Album") != null) {
						filTags.Album = getOption("Album");
					} else if (filTags.Album == null || filTags.Album.length() == 0) {
						if (getOption("Album") != null) {
							filTags.Album = getOption("Album");
						} else if (file.isDirectory()) {
							filTags.Album = file.getName();
						} else if (file.isFile()) {
							filTags.Album = file.getParent();
						} else {
							filTags.Album = null;
						}
					}
					// Year
					// if (getOption("Year") != null && (fYear == null ||
					// fYear.isEmpty() || fYear.equals("0"))) {
					if (getOption("Year") != null) {
						filTags.Year = getOption("Year");
					}
					// Genre
					// if (getOption("Genre") != null && (fGenre == null ||
					// fGenre.isEmpty() || fGenre.equals("0"))) {
					if (getOption("Genre") != null) {
						filTags.Genre = getOption("Genre");
					}

					System.out.println(
							"   - charSet/Id/In:    " + "[" + charSet + " / " + charSetId + " / " + charSetIn + "]");
					System.out.println("   - parsed file  Track: " + parsedFileName.TrackNo + ", Artist: "
							+ parsedFileName.Artist + ", Song: " + parsedFileName.Title);
					System.out.println("   - SongTitle:     " + filTags.Title + "\t[" + orgTags.Title + "]");
					System.out.println("   - LeadArtist:    " + filTags.Artist + "\t[" + orgTags.Artist + "]");
					System.out.println("   - AlbumTitle:    " + filTags.Album + "\t[" + orgTags.Album + "]");
					System.out.println("   - SongGenre:     " + filTags.Genre + "\t[" + orgTags.Genre + "]");
					System.out.println("   - Year:          " + filTags.Year + "\t[" + orgTags.Year + "]");
					System.out.println("   - TrackNo:       " + filTags.TrackNo + "\t[" + orgTags.TrackNo + "]");
					String toFileName = buildDestDir(fromFile[i].getParent()) + "\\"
							+ toStringCase(decFileName, getOption("optFileNameCase"));

					if (newfile) {
						// String toDirName = fromFileStr + "\\lat";
						String toDirName = buildDestDir(fromFileStr);
						File outFile = new java.io.File(toDirName);
						if (!outFile.isDirectory()) {
							if (!outFile.mkdirs()) {
								System.out.println(" ERROR: cannot create directory: " + toDirName);
								System.exit(0);
							}
						}

						String fromFileName = fromFileStr + "\\" + fil;
						// optFileNameCase = getOption("optFileNameCase");
						// if (optFileNameCase != null){
						// fromFileName = toStringCase(fromFileName,
						// optFileNameCase);
						// }
						System.out.println("   ->>> copy " + fromFileName + " -> " + toFileName);
						File toFile = copyFile(fromFileName, toFileName);
						if (toFile != null) {
							MP3 mp3 = new MP3(toFileName);
							// String title =
							// AnsiDecoder.decodeRuLatEntities(mp3.getTitle(),
							// charSetId);
							// if (fTitle == null) {
							// ;
							// }
							// else if (fTitle.indexOf("???") > -1) {
							// mp3.setTitle(AnsiDecoder.decodeRuLatEntities(AnsiDecoder.decodeRuLatEntities(dir[i],
							// charSet)));
							// }
							// else if (fTitle.startsWith("yayu")) {
							// fTitle.replaceFirst("yayu", "");
							// mp3.setTitle(fTitle);
							// }
							// else {
							// mp3.setTitle(fTitle);
							// }

							System.out.println("   ->>> <<< Writing Tags: " + filTags.Title + "/" + filTags.Artist + "/"
									+ filTags.Album + "/" + filTags.Genre + "/" + filTags.Year + "/" + filTags.TrackNo
									+ "//");

							if (filTags.Title != null)
								mp3.setTitle(filTags.Title);
							if (filTags.Artist != null)
								mp3.setArtist(filTags.Artist);
							if (filTags.Album != null)
								mp3.setAlbum(filTags.Album);
							if (filTags.Genre != null)
								mp3.setGenre(filTags.Genre);
							if (filTags.Year != null)
								mp3.setYear(filTags.Year);
							if (filTags.TrackNo != null)
								mp3.setTrackno(filTags.TrackNo);
						} else {
							// TODO: handle exception
							System.out.println(" ERROR: cannot access for update the mp3 tags for: " + toFileName);
						}
					}
				}
				System.out.println();
				// System.out.println(" - " +
				// AnsiDecoder.decodeRuLatEntities(dir[i], charSet)); // Print
				// the list
			}
		}
		System.exit(0);
	}

	public static void main2(String[] args) {
		try {
			String fromFile = ".";
			@SuppressWarnings("unused")
			String toFile = null;
			if (args.length != 0) {
				fromFile = new String(args[0]);
				if (args.length == 2) {
					toFile = new String(args[1]);
				}
				if (args.length > 2) {
					System.out.println(usage);
					System.exit(1);
				}
			}
			copy(fromFile, "toFile.txt");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public static File copyFile(String fromFileName, String toFileName) {

		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		try {
			if (!fromFile.exists())
				throw new IOException("FileCopy: " + "no such source file: " + fromFileName);
			if (!fromFile.isFile())
				throw new IOException("FileCopy: " + "can't copy directory: " + fromFileName);
			if (!fromFile.canRead())
				throw new IOException("FileCopy: " + "source file is unreadable: " + fromFileName);

			if (toFile.isDirectory()) {
				toFile = new File(toFile, fromFile.getName());
			}

			if (toFile.exists()) {
				if (!toFile.canWrite())
					throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFileName);
				System.out.print(
						"Overwrite existing file " + toFile.getAbsolutePath() + "/" + toFile.getName() + "? (Y/N): ");
				System.out.flush();
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String response = in.readLine();
				if (!response.equals("Y") && !response.equals("y"))
					throw new IOException("FileCopy: " + "existing file was not overwritten.");
			} else {
				String parent = toFile.getParent();
				if (parent == null)
					parent = System.getProperty("user.dir");
				File dir = new File(parent);
				if (!dir.exists())
					throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
				if (dir.isFile())
					throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
				if (!dir.canWrite())
					throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
			}

			FileInputStream from = null;
			FileOutputStream to = null;
			try {
				from = new FileInputStream(fromFile);
				to = new FileOutputStream(toFile);
				byte[] buffer = new byte[4096];
				int bytesRead;

				while ((bytesRead = from.read(buffer)) != -1)
					to.write(buffer, 0, bytesRead); // write
			} finally {
				if (from != null)
					try {
						from.close();
					} catch (IOException e) {
						;
					}
				if (to != null)
					try {
						to.close();
					} catch (IOException e) {
						;
					}
			}
		} catch (Exception e) {
			return null;
		}
		return toFile;
	}

	public static void copy(String fromFileName, String toFileName) throws IOException {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists())
			throw new IOException("FileCopy: " + "no such source file: " + fromFileName);
		if (!fromFile.isFile())
			throw new IOException("FileCopy: " + "can't copy directory: " + fromFileName);
		if (!fromFile.canRead())
			throw new IOException("FileCopy: " + "source file is unreadable: " + fromFileName);

		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());
		}

		if (toFile.exists()) {
			if (!toFile.canWrite())
				throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFileName);
			System.out.print(
					"Overwrite existing file " + toFile.getAbsolutePath() + "/" + toFile.getName() + "? (Y/N): ");
			System.out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String response = in.readLine();
			if (!response.equals("Y") && !response.equals("y"))
				throw new IOException("FileCopy: " + "existing file was not overwritten.");
		} else {
			String parent = toFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists())
				throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
			if (dir.isFile())
				throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
			if (!dir.canWrite())
				throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}

	public static void copy(File fromFile, String toFileName) throws IOException {

		File toFile = new File(toFileName);

		// if (toFile.isDirectory())
		// toFile = new File(toFile, fromFile.getName());
		toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			if (!toFile.canWrite())
				throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFileName);
			System.out.print(
					"Overwrite existing file " + toFile.getAbsolutePath() + "/" + toFile.getName() + "? (Y/N): ");
			System.out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String response = in.readLine();
			if (!response.equals("Y") && !response.equals("y"))
				throw new IOException("FileCopy: " + "existing file was not overwritten.");
		} else {
			String parent = toFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists())
				throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
			if (dir.isFile())
				throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
			if (!dir.canWrite())
				throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}

	protected static FileTags parseFileName(String fileName) {
		return parseFileName(fileName, true, true, true, null);
	}

	protected static FileTags parseFileName(String fileName, boolean withTrackno, boolean withInterpr,
			boolean withSongTitle, String optFileNameCase) {
		/**
		 * examples for default contents: 001. Sergej Minaev - Gimn Diskoteki
		 * 80-kh 001 - Sergej Minaev - Gimn Diskoteki 80-kh 001_Sergej Minaev -
		 * Gimn Diskoteki 80-kh NNN IIIIIIIIIIIII TTTTTTTTTTTTTTTTTTTT N - track
		 * No. I - Interpret T - song Title
		 *
		 */
		int trackno = 0; // 0 = started, 1 = running, 2 = end
		int interpr = 0; // 0 = started, 1 = running, 2 = end
		int sgtitle = 0; // 0 = started, 1 = running, 2 = end
		String sTrackNo = "";
		String sInterpr = "";
		String sSgTitle = "";
		FileTags retVal = new FileTags();
		if (fileName == null || fileName.length() == 0) {
			return retVal;
		}
		for (int i = 0; i < fileName.length(); i++) {
			char ch = fileName.charAt(i);
			// trackno?
			if (withTrackno && trackno < 2) {
				if (ch >= '0' && ch <= '9') {
					sTrackNo += ch;
					trackno = 1;
				} else {
					if (trackno == 1) {
						trackno = 2;
					}
				}
			}
			// interpret?
			else if (withInterpr && interpr < 2) {
				if (interpr == 0) {
					if (ch == ' ' || ch == '.' || ch == '-'
					// || ch == '_'
							|| ch == '\t') {
						// nonsense
						continue;
					}
					if (sInterpr.length() == 0 && ch == ' ')
						continue;
					interpr = 1;
					sInterpr += ch;
				} else {
					if ((ch == '-' && i > 0 && fileName.charAt(i - 1) == ' ') || ch == '«') {
						// end of interpret
						interpr = 2;
					} else {
						sInterpr += ch;
					}
				}
			}
			// interpret?
			else if (sgtitle < 2) {
				if (sgtitle == 0) {
					if (ch == ' ' || ch == '.' || ch == '-' || ch == '_' || ch == '»' || ch == '\t') {
						// nonsense
						continue;
					}
					if (sSgTitle.length() == 0 && ch == ' ')
						continue;
					sgtitle = 1;
					sSgTitle = fileName.substring(i);
					sSgTitle = sSgTitle.replaceFirst(".[mM][pP]3", "");
					break;
				}
			}
		}
		if (sgtitle == 0 && interpr == 1) {
			sSgTitle = sInterpr.replaceFirst(".[mM][pP]3", "");
			sInterpr = "";
			if (!withSongTitle) {
				sInterpr = sSgTitle;
				sSgTitle = "";
			}
		}
		if ("u".equals(optFileNameCase)) {
			retVal = new FileTags(sTrackNo.toUpperCase(), sInterpr.toUpperCase(), sSgTitle.toUpperCase());
		} else if ("l".equals(optFileNameCase)) {
			retVal = new FileTags(sTrackNo.toLowerCase(), sInterpr.toUpperCase(), sSgTitle.toUpperCase());
		} else if ("c".equals(optFileNameCase)) {
			retVal = new FileTags(sTrackNo, getCapitalizedCase(sInterpr), getCapitalizedCase(sSgTitle));
		} else {
			retVal = new FileTags(sTrackNo.trim(), sInterpr.trim(), sSgTitle.trim());
			// retVal[0] = sTrackNo;
			// retVal[1] = sInterpr;
			// retVal[2] = sSgTitle;
		}

		return retVal;
	}

	/**
	 *
	 * @param inpVal
	 * @return
	 */
	protected static String getPrintable(String inpVal) {
		String retVal = "";
		if (inpVal == null || inpVal.length() == 0) {
			return retVal;
		}

		for (int i = 0; i < inpVal.length(); i++) {
			char ch = inpVal.charAt(i);
			// is printable?
			if (Character.isIdentifierIgnorable(ch))
				ch = '?';
			retVal += ch;
		}

		return retVal;
	}

	/**
	 * @param args
	 */
	protected static void buildOptions(String[] args) {
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
				if ("-c".equals(arg)) {
					option = "ProfileCharSet";
				} else if ("-ch".equals(arg)) {
					option = "CharSetBase";
				} else if ("-ci".equals(arg)) {
					option = "CharSetIn";
				} else if ("-co".equals(arg)) {
					option = "CharSetOut";
				} else if ("-cs".equals(arg)) {
					option = "CharSet";
				} else if ("-cid".equals(arg)) {
					option = "CharSetId";
				} else if ("-x".equals(arg)) {
					option = "NewFile";
					if (!putOption(option, "true")) {
						System.out.println("ERROR: value for option: " + option + " already exists!");
						System.out.println(usage);
						System.exit(1);
					} else {
						// option complete
						option = null;
					}
				} else if ("-noi".equals(arg)) {
					option = "WithInterpret";
					if (!putOption(option, "false")) {
						System.out.println("ERROR: value for option: " + option + " already exists!");
						System.out.println(usage);
						System.exit(1);
					} else {
						// option complete
						option = null;
					}
				} else if ("-non".equals(arg)) {
					option = "WithTrack";
					if (!putOption(option, "false")) {
						System.out.println("ERROR: value for option: " + option + " already exists!");
						System.out.println(usage);
						System.exit(1);
					} else {
						// option complete
						option = null;
					}
				} else if ("-not".equals(arg)) {
					option = "WithTitle";
					if (!putOption(option, "false")) {
						System.out.println("ERROR: value for option: " + option + " already exists!");
						System.out.println(usage);
						System.exit(1);
					} else {
						// option complete
						option = null;
					}
				} else if ("-t".equals(arg)) {
					option = "Title";
				} else if ("-i".equals(arg)) {
					option = "Artist";
				} else if ("-n".equals(arg)) {
					option = "Trackno";
				} else if ("-y".equals(arg)) {
					option = "Year";
				} else if ("-a".equals(arg)) {
					option = "Album";
				} else if ("-g".equals(arg)) {
					option = "Genre";
				} else if ("-f".equals(arg)) {
					option = "FileList";
				} else if ("--fc".equals(arg)) {
					option = "optFileNameCase";
				} else if ("-p".equals(arg)) {
					option = "optParseTagsFromFile";
				} else if ("-m".equals(arg)) {
					option = "optParseFileFromTag";
				} else if ("-@".equals(arg)) {
					putOption("Force", "@");
					putOption("Title", "@");
					putOption("Artist", "@");
					putOption("Album", "@");
					putOption("Trackno", "@@");
					putOption("Genre", "Pop");
					option = null;
				} else {
					System.out.println("ERROR: unknown option: " + arg + "!");
					System.out.println(usage);
					System.exit(1);
				}
			} else if (option != null) {
				// value for new option
				if (!putOption(option, arg)) {
					System.out.println("ERROR: value for option: " + option + " already exists!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// option complete
					option = null;
				}
			} else {
				// neither option nor value for option, so it must be the File
				if (!putOption("File", arg)) {
					System.out.println("ERROR: multiple file specification!");
					System.out.println(usage);
					System.exit(1);
				} else {
					// file specification
					option = null;
				}
			}
		}
	}

	/**
	 * This routine builds a destination directory from the requested directory.
	 * <br>
	 * The rules is, put in the level of work directory the directories named
	 * "lat\current dir".<br>
	 * e.g.: <br>
	 * "D:\Music\Pop\Bravo 30" is converted to "D:\Music\Pop\lat\Bravo 30"
	 *
	 * @param orgDir
	 * @return
	 */
	protected static String buildDestDir(String orgDir) {
		String retval = "\\.\\_lat";

		if (orgDir == null)
			return retval;
		if (!orgDir.contains("\\"))
			return orgDir + "\\_lat";
		/**
		 * look for name of current directory
		 */
		int i = orgDir.length() - 1;
		for (; i >= 0; i--) {
			if (orgDir.charAt(i) == '\\')
				break;
		}
		if (i > 0) {
			retval = orgDir.substring(0, i) + "\\_lat" + orgDir.substring(i);
		}

		return retval;
	}

	/**
	 * This routine builds a destination directory from the requested directory.
	 * <br>
	 * The rules is, put in the level of work directory the directories named
	 * "lat\current dir".<br>
	 * e.g.: <br>
	 * "D:\Music\Pop\Bravo 30" is converted to "D:\Music\Pop\lat\Bravo 30"
	 *
	 * @param orgDir
	 * @return
	 */
	protected static String stripTrackNo(String fileName, int len) {
		String retval = "";

		for (int i = 0; i < fileName.length(); i++) {
			if (!(fileName.charAt(i) >= '0' && fileName.charAt(i) <= '9'))
				break;
			retval += fileName.charAt(i);
		}

		if (len <= 0 || len > retval.length())
			return retval;

		return retval.substring(retval.length() - len);
	}
}
