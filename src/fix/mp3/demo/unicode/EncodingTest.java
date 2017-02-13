package fix.mp3.demo.unicode;

/**
 * detect chars that are translated by an encoding
 * by Jon Skeet
 * pass name of an encoding you want to test on the command line
 * e.g. 8859_1 ISO8859_4 Cp437
 */
public class EncodingTest
{
   public static void main( String [] args ) throws Exception
   {
//      String encoding = args[0];
      String encoding = "Cp866";
      byte[] b = new byte[256];
      for ( int i=0; i<256; i++ )
      {
         b[i] = (byte)i;
      }
      String x = new String ( b , encoding );
      for ( int i=0; i<x.length(); i++ )
      {
         if ( x.charAt( i ) != i )
         {
            System.out.println( i + " -> " + (int)x.charAt ( i ) );
         }
      }
   }
}