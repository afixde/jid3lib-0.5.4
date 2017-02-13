//////////////////////////////////////////////////////////////////////
//
// Utility to convert from one encoding to another.
//
// Usage: java jiconv -f from_encoding -t to_encoding
//
// Where: from_encoding - encoding of input steam (stdin)
//        to_encoding   - encoding of output stream (stdout)
//
// For the list of available encoding, See...
// http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html
//
//
// Sample Usage :
// %java jiconv -f UTF8 -t EUC_JP  < input_file.txt > output_file.txt
//
/////////////////////////////////////////////////////////////////////////
package fix.mp3.demo.unicode;

import java.io.*;

public class jiconv {

    static String fromEncoding = null;
    static String toEncoding = null;

    private static void performConversion() {

        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, fromEncoding));
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(System.out, toEncoding));
            BufferedWriter utf8 = new BufferedWriter(
                new OutputStreamWriter(System.out, "UTF8"));

            String str ;
            while((str = in.readLine()) != null) {
                str = new String ("\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435");
                out.write(str, 0, str.length());
                out.newLine();
                out.flush();
                utf8.write(str, 0, str.length());
                utf8.newLine();
                utf8.flush();
                System.out.println("str = [" + str + "]");
//                System.out.println("in  = [" + in.toString() + "]");
//                System.out.println("out = [" + out.toString() + "]");
            }


        } catch (UnsupportedEncodingException ex) {

           System.err.println(ex);
           System.err.println("ERROR: The encoding specified is wrong or not supported by this system");

        } catch (IOException ex) {
           System.out.flush();
           System.err.println(ex);
        }

    }



    private static void printUsage() {
        System.err.println(
                "\nUtility to convert from one encoding to another.\n");
        System.err.println(
                "Usage: java jiconv -f from_encoding -t to_encoding\n");
        System.err.println(
                "Where: from_encoding - encoding of input steam (stdin)");
        System.err.println(
                "       to_encoding   - encoding of output stream (stdout)\n");
        System.err.println("For the list of available encoding, See... ");
        System.err.println("http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html\n\n");
        System.err.println("Sample Usage:");
        System.err.println(
                "%java jiconv -f UTF8 -t EUC_JP < input_file.txt > output_file.txt");
    }

    public static void main(String[] args) {

        int i = 0;
        String arg;

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if (arg.equals("-f")) {
                if (i < args.length) {
                    fromEncoding = args[i++];
                }
                else
                   break ;
            }
            else if (arg.equals("-t")) {
                if (i < args.length) {
                    toEncoding = args[i++];
                }
                else
                   break ;
            }
        }
        if (fromEncoding == null || toEncoding == null)
            printUsage();
        else
            performConversion();
    }
}
