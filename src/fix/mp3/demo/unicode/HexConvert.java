package fix.mp3.demo.unicode;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HexConvert extends Applet
                        implements ActionListener
{
    /**
	 *
	 */
	private static final long serialVersionUID = -3364479674994704859L;
	Choice ichoice, ochoice ;
    TextField itext, otext, ctext ;

    public void init()
    {
        setBackground(Color.white);
        GridBagLayout gridbag = new GridBagLayout() ;
        GridBagConstraints c  = new GridBagConstraints() ;

        setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = GridBagConstraints.NORTHWEST;

        c.gridwidth = GridBagConstraints.REMAINDER;
        ichoice = new Choice() ;
        ichoice.add("Pick Input Encoding: ");
        for(int i=0; i<encodings.length; i++)
        {
            ichoice.add(encodings[i][0] + ": " + encodings[i][1]);
        }
        gridbag.setConstraints(ichoice, c);
        add(ichoice);

        c.gridwidth = GridBagConstraints.RELATIVE;
        Label lbl = new Label("Input String (hex):");
        gridbag.setConstraints(lbl, c);
        add(lbl);

        c.gridwidth = GridBagConstraints.REMAINDER;
        itext = new TextField("",40);
        gridbag.setConstraints(itext, c);
        add(itext);

        ochoice = new Choice() ;
        ochoice.add("Pick Output Encoding: ");
        for(int i=0; i<encodings.length; i++)
        {
            ochoice.add(encodings[i][0] + ": " + encodings[i][1]);
        }
        gridbag.setConstraints(ochoice, c);
        add(ochoice);

        c.gridwidth = GridBagConstraints.RELATIVE;
        Button btn = new Button("Convert =>");
        btn.addActionListener(this);
        gridbag.setConstraints(btn, c);
        add(btn);

        c.gridwidth = GridBagConstraints.REMAINDER;
        otext = new TextField("",40);
        gridbag.setConstraints(otext, c);
        add(otext);

        c.gridwidth = GridBagConstraints.RELATIVE;
        Label lblc = new Label("Output string (ASCII):");
        gridbag.setConstraints(lblc, c);
        add(lblc);

        c.gridwidth = GridBagConstraints.REMAINDER;
        ctext = new TextField("",40);
        gridbag.setConstraints(ctext, c);
        add(ctext);

    }

    public void actionPerformed(ActionEvent e)
    {
        int idx, odx ;
        if ((idx=ichoice.getSelectedIndex()) == 0)
        {
           otext.setText("Pick Input Encoding!");
           return ;
        }
        if ((odx=ochoice.getSelectedIndex()) == 0)
        {
           otext.setText("Pick Output Encoding!");
           return ;
        }

        try
        {
           String instr = itext.getText();
           int[] outint = new int[instr.length()+1];
           StringTokenizer st = new StringTokenizer(instr, "\\u/x");
           int count = 0;
           while(st.hasMoreTokens())
           {
              String tok = st.nextToken();
              outint[count] = Integer.parseInt(tok, 16);
              count++ ;

           }

           String outstr = new String();
           if (encodings[idx-1][1].indexOf("Unicode") == -1)
           {
                byte[] outbytes = new byte[count];
                for(int i=0; i<count; i++)
                   outbytes[i] = (byte) outint[i] ;
                outstr = new String(outbytes, encodings[idx-1][0]);
           }
           else
           {
                char[] outchars = new char[count];

                if (encodings[idx-1][0].indexOf("Little") == -1)
                {
                   for(int i=0; i<count; i++)
                      outchars[i] = (char) outint[i];
                }
                else
                {
                   for(int i=0; i<count; i++)
                   {
                      int in = ((outint[i] >> 8) & 0xFF) |
                                                ((outint[i] & 0xFF) << 8) ;
                      outchars[i] = (char) in ;
                   }
                }

                outstr = new String(outchars);
           }

           String ostr = new String();
           byte[] obytes = outstr.getBytes(encodings[odx-1][0]);

           if (encodings[odx-1][1].indexOf("Unicode") == -1)
           {
              for(int j=0; j<obytes.length; j++)
              {
                  String hex = (Integer.toHexString(obytes[j]&0xFF))
                                                        .toUpperCase();
                  if (hex.length()==1) hex = "0" + hex ;
                  ostr += "\\x" +  hex ;
              }
           }
           else
           {
              for(int j=0; j<obytes.length; j+=2)
              {
                  String hex1 = (Integer.toHexString(obytes[j]&0xFF))
                                                        .toUpperCase();
                  if (hex1.length()==1) hex1 = "0" + hex1 ;
                  String hex2 = (Integer.toHexString(obytes[j+1]&0xFF))
                                                        .toUpperCase();
                  if (hex2.length()==1) hex2 = "0" + hex2 ;
                  if (encodings[odx-1][1].indexOf("Little") == -1)
                        ostr += "\\u" + hex1 + hex2 ;
                  else
                        ostr += "\\u" + hex2 + hex1 ;
              }
           }

           otext.setText(ostr);
           ctext.setText(outstr);
        }
        catch (Exception exp)
        {
           otext.setText("ERROR: "+exp);
        }
    }

    public static void main(String[] args)
    {
        Frame f  = new Frame();
        f.addWindowListener(new WindowAdapter() {
           @SuppressWarnings("unused")
		public void windowsClosing(WindowEvent e) {
                System.exit(0);
           };
        });
        HexConvert ut = new HexConvert() ;
        ut.setSize(600, 150);
        f.add(ut);
        f.pack();
        ut.init();
        f.setSize(600, 150+20);
//        f.show();
        f.setVisible(true);
    }

    public static String encodings[][] = {
        { "ASCII","American Standard Code for Information Interchange " },
        { "Cp1252","Windows Latin-1 " },
        { "ISO-8859-1","ISO 8859-1, Latin alphabet No. 1 " },
        { "UnicodeBig","Sixteen-bit Unicode Transformation Format, big-endian byte order, with byte-order mark " },
        { "UnicodeBigUnmarked","Sixteen-bit Unicode Transformation Format, big-endian byte order " },
        { "UnicodeLittle","Sixteen-bit Unicode Transformation Format, little-endian byte order, with byte-order mark " },
        { "UnicodeLittleUnmarked","Sixteen-bit Unicode Transformation Format, little-endian byte order " },
        { "UTF8","Eight-bit unicode Transformation Format " },
        { "UTF-16","Sixteen-bit Unicode Transformation Format, byte order specified by a mandatory initial byte-order mark " },
        { "Big5","Big5, Traditional Chinese " },
        { "Big5_HKSCS","Big5 with Hong Kong extensions, Traditional Chinese " },
        { "Cp037","USA, Canada (Bilingual, French), Netherlands, Portugal, Brazil, Australia " },
        { "Cp273","IBM Austria, Germany " },
        { "Cp277","IBM Denmark, Norway " },
        { "Cp278","IBM Finland, Sweden " },
        { "Cp280","IBM Italy " },
        { "Cp284","IBM Catalan/Spain, Spanish Latin America " },
        { "Cp285","IBM United Kingdom, Ireland " },
        { "Cp297","IBM France " },
        { "Cp420","IBM Arabic " },
        { "Cp424","IBM Hebrew " },
        { "Cp437","MS-DOS United States, Australia, New Zealand, South Africa " },
        { "Cp500","EBCDIC 500V1 " },
        { "Cp737","PC Greek " },
        { "Cp775","PC Baltic " },
        { "Cp838","IBM Thailand extended SBCS " },
        { "Cp850","MS-DOS Latin-1 " },
        { "Cp852","MS-DOS Latin-2 " },
        { "Cp855","IBM Cyrillic " },
        { "Cp856","IBM Hebrew " },
        { "Cp857","IBM Turkish " },
        { "Cp858","Variant of Cp850 with Euro character " },
        { "Cp860","MS-DOS Portuguese " },
        { "Cp861","MS-DOS Icelandic " },
        { "Cp862","PC Hebrew " },
        { "Cp863","MS-DOS Canadian French " },
        { "Cp864","PC Arabic " },
        { "Cp865","MS-DOS Nordic " },
        { "Cp866","MS-DOS Russian " },
        { "Cp868","MS-DOS Pakistan " },
        { "Cp869","IBM Modern Greek " },
        { "Cp870","IBM Multilingual Latin-2 " },
        { "Cp871","IBM Iceland " },
        { "Cp874","IBM Thai " },
        { "Cp875","IBM Greek " },
        { "Cp918","IBM Pakistan (Urdu) " },
        { "Cp921","IBM Latvia, Lithuania (AIX, DOS) " },
        { "Cp922","IBM Estonia (AIX, DOS) " },
        { "Cp930","Japanese Katakana-Kanji mixed with 4370 UDC, superset of 5026 " },
        { "Cp933","Korean Mixed with 1880 UDC, superset of 5029 " },
        { "Cp935","Simplified Chinese Host mixed with 1880 UDC, superset of 5031 " },
        { "Cp937","Traditional Chinese Host miexed with 6204 UDC, superset of 5033 " },
        { "Cp939","Japanese Latin Kanji mixed with 4370 UDC, superset of 5035 " },
        { "Cp942","IBM OS/2 Japanese, superset of Cp932 " },
        { "Cp942C","Variant of Cp942 " },
        { "Cp943","IBM OS/2 Japanese, superset of Cp932 and Shift-JIS " },
        { "Cp943C","Variant of Cp943 " },
        { "Cp948","OS/2 Chinese (Taiwan) superset of 938 " },
        { "Cp949","PC Korean " },
        { "Cp949C","Variant of Cp949 " },
        { "Cp950","PC Chinese (Hong Kong, Taiwan) " },
        { "Cp964","AIX Chinese (Taiwan) " },
        { "Cp970","AIX Korean " },
        { "Cp1006","IBM AIX Pakistan (Urdu) " },
        { "Cp1025","IBM Multilingual Cyrillic: Bulgaria, Bosnia, Herzegovinia, Macedonia (FYR) " },
        { "Cp1026","IBM Latin-5, Turkey " },
        { "Cp1046","IBM Arabic - Windows " },
        { "Cp1097","IBM Iran (Farsi)/Persian " },
        { "Cp1098","IBM Iran (Farsi)/Persian (PC) " },
        { "Cp1112","IBM Latvia, Lithuania " },
        { "Cp1122","IBM Estonia " },
        { "Cp1123","IBM Ukraine " },
        { "Cp1124","IBM AIX Ukraine " },
        { "Cp1140","Variant of Cp037 with Euro character " },
        { "Cp1141","Variant of Cp273 with Euro character " },
        { "Cp1142","Variant of Cp277 with Euro character " },
        { "Cp1143","Variant of Cp278 with Euro character " },
        { "Cp1144","Variant of Cp280 with Euro character " },
        { "Cp1145","Variant of Cp284 with Euro character " },
        { "Cp1146","Variant of Cp285 with Euro character " },
        { "Cp1147","Variant of Cp297 with Euro character " },
        { "Cp1148","Variant of Cp500 with Euro character " },
        { "Cp1149","Variant of Cp871 with Euro character " },
        { "Cp1250","Windows Eastern European " },
        { "Cp1251","Windows Cyrillic " },
        { "Cp1253","Windows Greek " },
        { "Cp1254","Windows Turkish " },
        { "Cp1255","Windows Hebrew " },
        { "Cp1256","Windows Arabic " },
        { "Cp1257","Windows Baltic " },
        { "Cp1258","Windows Vietnamese " },
        { "Cp1381","IBM OS/2, DOS People's Republic of China (PRC) " },
        { "Cp1383","IBM AIX People's Republic of China (PRC) " },
        { "Cp33722","IBM-eucJP - Japanese (superset of 5050) " },
        { "EUC_CN","GB2312, EUC encoding, Simplified Chinese " },
        { "EUC_JP","JIS X 0201, 0208, 0212, EUC encoding, Japanese " },
        { "EUC_JP_LINUX","JIS X 0201, 0208, EUC encoding, Japanese " },
        { "EUC_KR","KS C 5601, EUC encoding, Korean " },
        { "EUC_TW","CNS11643 (Plane 1-3), EUC encoding, Traditional Chinese " },
        { "GBK","GBK, Simplified Chinese " },
        { "ISO2022CN","ISO 2022 CN, Chinese" },
        { "ISO2022CN_CNS","CNS 11643 in ISO 2022 CN form, Traditional Chinese" },
        { "ISO2022CN_GB","GB 2312 in ISO 2022 CN form, Simplified Chinese" },
        { "ISO2022JP","JIS X 0201, 0208 in ISO 2022 form, Japanese " },
        { "ISO2022KR","ISO 2022 KR, Korean " },
        { "ISO8859_2","ISO 8859-2, Latin alphabet No. 2 " },
        { "ISO8859_3","ISO 8859-3, Latin alphabet No. 3 " },
        { "ISO8859_4","ISO 8859-4, Latin alphabet No. 4 " },
        { "ISO8859_5","ISO 8859-5, Latin/Cyrillic alphabet " },
        { "ISO8859_6","ISO 8859-6, Latin/Arabic alphabet " },
        { "ISO8859_7","ISO 8859-7, Latin/Greek alphabet " },
        { "ISO8859_8","ISO 8859-8, Latin/Hebrew alphabet " },
        { "ISO8859_9","ISO 8859-9, Latin alphabet No. 5 " },
        { "ISO8859_13","ISO 8859-13, Latin alphabet No. 7 " },
        { "ISO8859_15_FDIS","ISO 8859-15, Latin alphabet No. 9 " },
        { "JIS0201","JIS X 0201, Japanese " },
        { "JIS0208","JIS X 0208, Japanese " },
        { "JIS0212","JIS X 0212, Japanese " },
        { "JISAutoDetect","Detects and converts from Shift-JIS, EUC-JP, ISO 2022 JP" },
        { "Johab","Johab, Korean " },
        { "KOI8_R","KOI8-R, Russian " },
        { "MS874","Windows Thai " },
        { "MS932","Windows Japanese " },
        { "MS936","Windows Simplified Chinese " },
        { "MS949","Windows Korean " },
        { "MS950","Windows Traditional Chinese " },
        { "MacArabic","Macintosh Arabic " },
        { "MacCentralEurope","Macintosh Latin-2 " },
        { "MacCroatian","Macintosh Croatian " },
        { "MacCyrillic","Macintosh Cyrillic " },
        { "MacDingbat","Macintosh Dingbat " },
        { "MacGreek","Macintosh Greek " },
        { "MacHebrew","Macintosh Hebrew " },
        { "MacIceland","Macintosh Iceland " },
        { "MacRoman","Macintosh Roman " },
        { "MacRomania","Macintosh Romania " },
        { "MacSymbol","Macintosh Symbol " },
        { "MacThai","Macintosh Thai " },
        { "MacTurkish","Macintosh Turkish " },
        { "MacUkraine","Macintosh Ukraine " },
        { "SJIS","Shift-JIS, Japanese " },
        { "TIS620","TIS620, Thai " },
        };
}

