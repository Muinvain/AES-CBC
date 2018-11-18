
import com.sun.tools.javac.util.Name;
import sun.tools.jconsole.Tab;

import java.io.*;
import java.lang.String;


public class Main {


    private static String[] Table1 = new String[]{
            "amelia",
            "olivia",
            "jessica",
            "emily",
            "lily",
            "ava",
            "isla",
            "sophie",
            "mia",
            "isabella"};
    private static String Table2[] = new String[]{
            "harry",
            "oliver",
            "jack",
            "charlie",
            "jacob",
            "thomas",
            "alfie",
            "riley",
            "william",
            "james"};


    private static String hashcodeTable[] = new String[]{
            "5E0176C9D2070A5A2A22BF74B4ABED303654690D58D64221CCBD022AF827ABC4",
            "B9DD960C1753459A78115D3CB845A57D924B6877E805B08BD01086CCDF34433C",
            "D813086F366C302BD387DF634BE054A5190F7CC13B578825A5A8D0E0BF444122",
            "FFFF0E6D65F3821008C1C33A5DD9E82F64E1D34FA71BA22EA441C0C60670C40E",
            "4ABB806D8A2496F726198403E6E175FFBFCE51054638DA442CD2BD45EFE0BAF9",
            "AB8A711F117BB9AD76B613A60C42506B27E1CF98E995C36E1321DC20B413E581",
            "20D97873B1EB48015400F9726EC75B0E6C96EEC8F6164596C5D1C126D9B594FA",
            "20ABFE97D83DC400404F15C4D1755CA5A7A3C0BD41D010FFBF69D85EF417B838"};


    //byte to hex
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        java.lang.String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    //hex to byte
    public static byte[] hex2byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String readFile(File file){
        //
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//
            String s = null;
            while((s = br.readLine()) != null){//
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String FindNamePwd(String[] table) {

        String stempPwd = new String();
        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < 8; j++) {
                if (byte2hex(SHA256.Encrypt(table[i])).equals(hashcodeTable[j])) {

                    stempPwd = stempPwd + (hashcodeTable[j] + " " + table[i] + "\n");
                }
            }
        }
        return stempPwd;

    }
    public static String FindTable4Pwd() {

        String stempPwd = new String();
        File fTable4 = new File("src/word_list_moby_all_moby_words.flat.txt");

        try{
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fTable4));//
            String s = null;
            while((s = br.readLine()) != null){//
                for (int j = 0 ; j < 8 ; j++)
                {
                    if (byte2hex(SHA256.Encrypt(s)).equals(hashcodeTable[j])) {

                        stempPwd = stempPwd + (hashcodeTable[j] + " " + s + "\n");
                    }
                }


            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stempPwd;
    }

    public static void main(String[] args) {


        try {



            File fres = new File("src/password");





            String cPwd = new String();
            cPwd = FindNamePwd(Table1) + FindNamePwd(Table2) + FindTable4Pwd();


            fres.createNewFile();
            FileOutputStream fos = new FileOutputStream("src/password");
            fos.write(cPwd.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}