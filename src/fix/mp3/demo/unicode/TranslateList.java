package fix.mp3.demo.unicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TranslateList {
	private static Map<String, String> optionEntities = new Hashtable<String, String>();;
	private static final int MAX_BYTES_TO_SAMPLE = 1024 * 1024;

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
	@SuppressWarnings("unused")
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
	"Usage: FileConvert [-ch charset out] [-ci charset in] -f filelist dir/file\n"
			+ "                   [--fc filenamecase: u(pper)/l(ower)/c(capitalized)\n"
			+ "                   [-x execute new file] dir/file\n"
			+ "  set one or more specified tags (3v2.4) for specified mp3 file\n"
			+ "		and convert the files and the tags into latin char set\n"
			+ "  i.e. FileConvert -l \"filelist.txt\" mp3dir\n";

	public static void main(String[] args) throws Exception {
		String fromFileStr = ".";
		String listFileStr = ".";
		String charSetIn = "UTF-16";
		String charSet = "UTF-16";

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
		}

		/**
		 * Options CharSet
		 */
		if (getOption("CharSetBase") != null) {
			charSet = getOption("CharSetBase");
		}
		if (getOption("CharSetIn") != null) {
			charSetIn = getOption("CharSetIn");
		}
		if (getOption("CharSetOut") != null) {
			charSet = getOption("CharSetOut");
		}
		if (getOption("CharSet") != null) {
			charSet = getOption("CharSet");
		}

		// file list
		listFileStr = getOption("FileList");
		if (listFileStr == null) {
			System.out.println("ERROR: missing the file list!");
			System.out.println(usage);
			System.exit(1);
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(listFileStr));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				System.out.println("Gelesene Zeile: " + zeile);

				byte[] bytes = zeile.getBytes();
				String translatedBytes = new String(bytes, charSetIn);
				// System.out.println("translatedBytes = \n" + translatedBytes);

				int[] outint = AnsiDecoder.toHexIntArray(translatedBytes, translatedBytes.length());

				String outstr = AnsiDecoder.decodeRuLatEntities(outint, translatedBytes, charSet);
				// String dstr = AnsiDecoder.decodeRuLatEntities(outstr,
				// charSetIn, charSet);
				System.out.println("outstr = [" + outstr + "]");

				// int[] outint = AnsiDecoder.toHexIntArray(zeile,
				// zeile.length());
				//
				// String outstr = AnsiDecoder.decodeRuLatEntities(outint,
				// zeile, charSet);
				// // String dstr = AnsiDecoder.decodeRuLatEntities(outstr,
				// charSetIn, charSet);
				// System.out.println("1. outstr = \n" + outstr);
				//
				// outstr = AnsiDecoder.decodeRuLatEntitiesDeep(zeile, charSet,
				// charSetIn);
				// System.out.println("2. outstr = \n" + outstr);

			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.exit(0);

		// File filelist = new java.io.File(listFileStr);
		// if (!filelist.isFile())
		// {
		// System.out.println("ERROR: invalid list file: " + listFileStr + "!"
		// );
		// System.out.println(usage);
		// System.exit(1);
		// }

		List<String> translFiles = translateFileList(listFileStr, charSet, charSetIn);

		int count = 0;
		for (String file : translFiles) {
			count++;
			System.out.println(count + ". outstr = " + file);
		}

		System.exit(0);
	}

	public static void main0(String[] args) throws Exception {
		String fromFileStr = ".";
		String listFileStr = ".";
		String charSetIn = "UTF-16";
		String charSet = "UTF-16";

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
		}

		/**
		 * Options CharSet
		 */
		if (getOption("CharSetBase") != null) {
			charSet = getOption("CharSetBase");
		}
		if (getOption("CharSetIn") != null) {
			charSetIn = getOption("CharSetIn");
		}
		if (getOption("CharSetOut") != null) {
			charSet = getOption("CharSetOut");
		}
		if (getOption("CharSet") != null) {
			charSet = getOption("CharSet");
		}

		// file list
		listFileStr = getOption("FileList");
		if (listFileStr == null) {
			System.out.println("ERROR: missing the file list!");
			System.out.println(usage);
			System.exit(1);
		}
		File filelist = new java.io.File(listFileStr);
		if (!filelist.isFile()) {
			System.out.println("ERROR: invalid list file: " + listFileStr + "!");
			System.out.println(usage);
			System.exit(1);
		}

		ByteBuffer.allocateDirect(2560);
		FileInputStream from = null;
		try {
			// from = new FileInputStream(filelist);
			// ByteBuffer buf = ByteBuffer.allocateDirect(256); // BUFSIZE = 256
			// FileChannel ch = from.getChannel();
			// Charset csi = Charset.forName(charSetIn); // Or whatever encoding
			// you want
			// Charset cso = Charset.forName(charSet); // Or whatever encoding
			// you want
			//
			// /* read the file into a buffer, 256 bytes at a time */
			// while ( (ch.read( buf )) != -1 ) {
			// buffer.put(buf);
			// buf.rewind();
			// CharBuffer chbuf = csi.decode(buf);
			// inFiles += chbuf;
			// chbuf = cso.decode(buf);
			// outFiles += chbuf;
			// buf.clear();
			// }
			// decFiles = AnsiDecoder.decodeRuLatEntitiesDeep(inFiles, charSet,
			// charSetIn);

			int sampleSize = (int) Math.min(filelist.length(), MAX_BYTES_TO_SAMPLE);

			// R E A D
			byte[] bytes = new byte[sampleSize];
			// -1 means eof.
			// You don't necessarily get all you ask for in one read.
			// You get what's immediately available.
			from = new FileInputStream(filelist);
			int bytesRead = from.read(bytes, 0
					/* offset in ba */, sampleSize
			/* bytes to read */ );
			if (bytesRead != sampleSize) {
				throw new IOException("cannot read file");
			}

			String translatedBytes = new String(bytes, charSetIn);
			// System.out.println("translatedBytes = \n" + translatedBytes);

			Math.min(translatedBytes.length(), 256);

			int[] outint = AnsiDecoder.toHexIntArray(translatedBytes, translatedBytes.length());

			String outstr = AnsiDecoder.decodeRuLatEntities(outint, translatedBytes, charSet);
			// String dstr = AnsiDecoder.decodeRuLatEntities(outstr, charSetIn,
			// charSet);
			System.out.println("outstr = \n" + outstr);

		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
		}

		// System.out.println();
		// System.out.println("-------------------------");
		// System.out.println("charSetIn: " + charSetIn);
		// System.out.println("charSet: " + charSet);
		// System.out.println("inFiles : \n" + inFiles);
		// System.out.println("outFiles: \n" + outFiles);
		// System.out.println("decFiles: \n" + decFiles);
		//
		//
		// // Create the encoder and decoder for ISO-8859-1
		// Charset csi = Charset.forName(charSet);
		// Charset cso = Charset.forName(charSetIn);
		// CharsetEncoder encoder = csi.newEncoder();
		// CharsetDecoder decoder = cso.newDecoder();
		// try
		// {
		// // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
		// // The new ByteBuffer is ready to be read.
		// // This line is the key to removing "unmappable" characters.
		// encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		// encoder.onMalformedInput(CodingErrorAction.IGNORE);
		// ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(outFiles));
		// // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character
		// ByteBuffer and then to a string.
		// // The new ByteBuffer is ready to be read.
		// CharBuffer cbuf = decoder.decode(bbuf);
		// System.out.println("Translated 1: \n" + cbuf.toString());
		// cbuf = decoder.decode(buffer);
		// System.out.println("Translated 2: \n" + cbuf.toString());
		// }
		// catch (CharacterCodingException e)
		// {
		// System.out.println(e);
		// }
		// catch (Exception e)
		// {
		// System.out.println(e);
		// }
		//
		// int[] outint = AnsiConvert.decodeBytesToHex(inFiles, charSet,
		// charSetIn);
		// int count = outint.length;
		//
		//// String inpstr = decodeIntsToString(outint, encodings[idx-1][0],
		// encodings[idx-1][1], count);
		// String outstr = AnsiConvert.decodeIntsToString(outint, charSetIn,
		// charSetIn, count);
		// String dstr = AnsiDecoder.decodeRuLatEntities(outstr, charSetIn,
		// charSet);
		//
		// byte[] obytes = outstr.getBytes(charSetIn);
		// String ostr = AnsiConvert.decodeBytesToHexString(obytes, charSetIn);
		//
		// String outstr2 = AnsiDecoder.decodeCharSets(inFiles, charSetIn,
		// charSet);
		//
		// System.out.println("outstr : \n" + outstr2);
		// System.out.println("decstr : \n" + dstr);
		//

		System.exit(0);
	}

	public static void main1(String[] args) throws Exception {
		String fromFileStr = ".";
		String listFileStr = ".";
		String charSetIn = "UTF-16";
		String charSet = "UTF-16";
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
		}
		// boolean values:
		if ("true".equals(getOption("NewFile"))) {
		}
		if ("false".equals(getOption("WithInterpret"))) {
		}
		if ("false".equals(getOption("WithTrack"))) {
		}
		if ("false".equals(getOption("WithTitle"))) {
		}

		/**
		 * Options CharSet
		 */
		if (getOption("CharSetBase") != null) {
			charSet = getOption("CharSetBase");
			getOption("CharSetBase");
		}
		if (getOption("CharSetIn") != null) {
			charSetIn = getOption("CharSetIn");
		}
		if (getOption("CharSetOut") != null) {
			charSet = getOption("CharSetOut");
			getOption("CharSetOut");
		}
		if (getOption("CharSet") != null) {
			charSet = getOption("CharSet");
		}
		if (getOption("CharSetId") != null) {
			getOption("CharSetId");
		}

		// file list
		listFileStr = getOption("FileList");
		if (listFileStr == null) {
			System.out.println("ERROR: missing the file list!");
			System.out.println(usage);
			System.exit(1);
		}
		File filelist = new java.io.File(listFileStr);
		if (!filelist.isFile()) {
			System.out.println("ERROR: invalid list file: " + listFileStr + "!");
			System.out.println(usage);
			System.exit(1);
		}

		String inFiles = "";
		String outFiles = "";
		String decFiles = "";
		ByteBuffer buffer = ByteBuffer.allocateDirect(2560);
		FileInputStream from = null;
		try {
			from = new FileInputStream(filelist);
			ByteBuffer buf = ByteBuffer.allocateDirect(256); // BUFSIZE = 256
			FileChannel ch = from.getChannel();
			Charset csi = Charset.forName(charSetIn); // Or whatever encoding
														// you want
			Charset cso = Charset.forName(charSet); // Or whatever encoding you
													// want

			/* read the file into a buffer, 256 bytes at a time */
			while ((ch.read(buf)) != -1) {
				buffer.put(buf);
				buf.rewind();
				CharBuffer chbuf = csi.decode(buf);
				inFiles += chbuf;
				chbuf = cso.decode(buf);
				outFiles += chbuf;
				// System.out.println("chbuf = [" + chbuf + "]");
				// for ( int i = 0; i < chbuf.length(); i++ ) {
				// /* print each character */
				// System.out.print(chbuf.get(i));
				// }
				buf.clear();
			}
			decFiles = AnsiDecoder.decodeRuLatEntitiesDeep(outFiles, charSet, charSetIn);

		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
		}

		System.out.println();
		System.out.println("-------------------------");
		System.out.println("charSetIn: " + charSetIn);
		System.out.println("charSet:   " + charSet);
		System.out.println("inFiles : \n" + inFiles);
		System.out.println("outFiles: \n" + outFiles);
		System.out.println("decFiles: \n" + decFiles);

		// Create the encoder and decoder for ISO-8859-1
		Charset csi = Charset.forName(charSet);
		Charset cso = Charset.forName(charSetIn);
		CharsetEncoder encoder = csi.newEncoder();
		CharsetDecoder decoder = cso.newDecoder();
		try {
			// Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
			// The new ByteBuffer is ready to be read.
			// This line is the key to removing "unmappable" characters.
			encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
			encoder.onMalformedInput(CodingErrorAction.IGNORE);
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(outFiles));
			// Convert ISO-LATIN-1 bytes in a ByteBuffer to a character
			// ByteBuffer and then to a string.
			// The new ByteBuffer is ready to be read.
			CharBuffer cbuf = decoder.decode(bbuf);
			System.out.println("Translated 1: \n" + cbuf.toString());
			cbuf = decoder.decode(buffer);
			System.out.println("Translated 2: \n" + cbuf.toString());
		} catch (CharacterCodingException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}

		int[] outint = AnsiConvert.decodeBytesToHex(inFiles, charSet, charSetIn);
		int count = outint.length;

		// String inpstr = decodeIntsToString(outint, encodings[idx-1][0],
		// encodings[idx-1][1], count);
		String outstr = AnsiConvert.decodeIntsToString(outint, charSetIn, charSetIn, count);
		String dstr = AnsiDecoder.decodeRuLatEntities(outstr, charSetIn, charSet);

		byte[] obytes = outstr.getBytes(charSetIn);
		AnsiConvert.decodeBytesToHexString(obytes, charSetIn);

		String outstr2 = AnsiDecoder.decodeCharSets(inFiles, charSetIn, charSet);

		System.out.println("outstr : \n" + outstr2);
		System.out.println("decstr : \n" + dstr);

		try {
			int sampleSize = (int) Math.min(filelist.length(), MAX_BYTES_TO_SAMPLE);

			// O P E N
			FileInputStream fis = new FileInputStream(filelist);

			// R E A D
			byte[] b = new byte[sampleSize];
			// -1 means eof.
			// You don't necessarily get all you ask for in one read.
			// You get what's immediately available.
			int bytesRead = fis.read(b, 0
					/* offset in ba */, sampleSize
			/* bytes to read */ );
			if (bytesRead != sampleSize) {
				fis.close();
				throw new IOException("cannot read file");
			}
			byte[] rawByteSample = b;
			// C L O S E
			fis.close();

			String origString = new String(rawByteSample, charSetIn);
			System.out.println(" origString = \n" + origString);
			decFiles = AnsiDecoder.decodeRuLatEntitiesDeep(origString, charSet, charSetIn);
			System.out.println(" decFiles = \n" + decFiles);
		} catch (IOException e2) {
			System.out.println("Cannot read " + listFileStr);
		}
	}

	public static void main2(String[] args) {
		try {
			String fromFile = ".";
			if (args.length != 0) {
				fromFile = new String(args[0]);
				if (args.length == 2) {
					new String(args[1]);
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

	public static List<String> translateFileList(String fromFileName, String charSet, String charSetIn)
			throws IOException {
		File fromFile = new File(fromFileName);
		FileInputStream from = null;
		List<String> retval = null;

		try {
			if (!fromFile.exists())
				throw new IOException("parseFile: " + "no such source file: " + fromFileName);
			if (!fromFile.isFile())
				throw new IOException("parseFile: " + "can't copy directory: " + fromFileName);
			if (!fromFile.canRead())
				throw new IOException("parseFile: " + "source file is unreadable: " + fromFileName);

			// ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
			// from = new FileInputStream(fromFileName);

			int sampleSize = (int) Math.min(fromFile.length(), MAX_BYTES_TO_SAMPLE);

			// R E A D
			byte[] bytes = new byte[sampleSize];
			// -1 means eof.
			// You don't necessarily get all you ask for in one read.
			// You get what's immediately available.
			from = new FileInputStream(fromFileName);
			int bytesRead = from.read(bytes, 0
					/* offset in ba */, sampleSize
			/* bytes to read */ );
			if (bytesRead != sampleSize) {
				throw new IOException("cannot read file");
			}

			String translatedBytes = new String(bytes, charSetIn);
			// System.out.println("translatedBytes = \n" + translatedBytes);

			// int hexCharSampleSize =
			// Math.min( translatedBytes.length(), 256 );
			// System.out.println("HexString:" +
			// AnsiDecoder.toHexString(translatedBytes, hexCharSampleSize ));

			int[] outint = AnsiDecoder.toHexIntArray(translatedBytes, translatedBytes.length());

			String outstr = AnsiDecoder.decodeRuLatEntities(outint, translatedBytes, charSet);
			// String dstr = AnsiDecoder.decodeRuLatEntities(outstr, charSetIn,
			// charSet);
			// System.out.println("outstr = \n" + outstr);

			StringTokenizer st = new StringTokenizer(outstr, "\n");
			if (st.countTokens() <= 0)
				return null;

			retval = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				String tok = st.nextToken().replaceAll("[\n\r]", "").trim();
				if (tok.length() == 0)
					continue;
				retval.add(tok);
				// System.out.println(count + ". outstr = " + tok);
			}
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
		}

		return retval;
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

	protected static String[] parseFileName(String fileName) {
		return parseFileName(fileName, true, true, true, null);
	}

	protected static String[] parseFileName(String fileName, boolean withTrackno, boolean withInterpr,
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
		String[] retVal = new String[] { "", "", "" };
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
					if (ch == ' ' || ch == '.' || ch == '-' || ch == '_' || ch == '\t') {
						// nonsense
						continue;
					}
					interpr = 1;
					sInterpr += ch;
				} else {
					if (ch == '-' || ch == '«') {
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
			retVal[0] = sTrackNo.toUpperCase();
			retVal[1] = sInterpr.toUpperCase();
			retVal[2] = sSgTitle.toUpperCase();
		} else if ("l".equals(optFileNameCase)) {
			retVal[0] = sTrackNo.toLowerCase();
			retVal[1] = sInterpr.toLowerCase();
			retVal[2] = sSgTitle.toLowerCase();
		} else if ("c".equals(optFileNameCase)) {
			retVal[0] = sTrackNo;
			retVal[1] = getCapitalizedCase(sInterpr);
			retVal[2] = getCapitalizedCase(sSgTitle);
		} else {
			retVal[0] = sTrackNo;
			retVal[1] = sInterpr;
			retVal[2] = sSgTitle;
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
				if ("-ch".equals(arg)) {
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

}
