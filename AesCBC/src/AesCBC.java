

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;


public class AesCBC {

    private static String sPwd= null;

    private static String sivParameter= null;// store the 16 byte iv in hex format
    private static AesCBC instance = null;
    private static String sHMAC = null;



    //private static 
    private AesCBC(){

    }
    public static AesCBC getInstance(){
        if (instance==null)
            instance= new AesCBC();
        return instance;
    }

    /**
     * AES algorithm
     * CBC mode
     * @param sSrc the plaintext
     * @param encodingFormat the format of encoding. in this case, use"utf-8"
     * @param sKey Ase Key which form the user password
     * @param ivParameter a random IV used in AES CBC mode it can strength algorithm
     * @return cipher text
     * @throws Exception
     */
    public byte[] encrypt(String sSrc, String encodingFormat, String sKey, byte[] ivParameter) throws Exception {
        //provide the information about AES algorithm and CBC mode and PKCS5padding schema used to pad cleartext to be multiples of 8-byte blocks.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //read the key
        byte[] raw = sKey.getBytes();
        //constructs a secret key from the given byte array
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //Creates an IvParameterSpec object using the bytes in iv as the IV
        IvParameterSpec iv = new IvParameterSpec(ivParameter);
        //iInitializes this cipher with a key and a source of randomness
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        //Finishes a multiple-part encryption operation, depending on how this cipher was initialized.
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return encrypted;
    }

    /**
     *
     * @param sSrc Cipher text need to be decrypted
     * @param encodingFormat "utf-8"
     * @param sKey Ase Key which form the user password
     * @param ivParameter a random IV used in AES CBC mode it can strength algorithm
     * @return plaintext
     * @throws Exception
     */
    public String decrypt(byte[] sSrc, String encodingFormat, String sKey, byte[] ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            //constructs a secret key from the given byte array
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //provide the information about AES algorithm and CBC mode and PKCS5padding schema used to pad cleartext to be multiples of 8-byte block
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //read the IV
            IvParameterSpec iv = new IvParameterSpec(ivParameter);
            //initialize the cipher
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //decrypt the encrypt text
            byte[] original = cipher.doFinal(sSrc);
            //change the format of data which has been decrypted
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }



    //byte to hex
    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
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
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = br.readLine())!=null){
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    private static byte[] readByteFile(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static byte[] generateIV() {
        int DEFAULT_IVSIZE = 16;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[DEFAULT_IVSIZE]; // DEFAULT_IVSIZE =16
        random.nextBytes(bytes);
        return bytes;
    }




       public static void main(String[] args) throws Exception {



               System.out.println("use 'e - filename' to encrypt file");
               System.out.println("use 'd - filename' to decrypt file");


               Scanner sc = new Scanner(System.in);
               while(true)
               {
                   String scmd = sc.nextLine();

                   if (scmd.equals("e - msg"))
                   {
                       System.out.println("please set the password");
                       sPwd = sc.nextLine();

                       File fMsg = new File(("src/msg"));


                       //if the file dosent exit, complain
                       if (!fMsg.exists())
                       {
                           System.out.println("Exit: no file named 'msg'");
                           System.exit(1);
                       }

                       String cSrc = readFile(fMsg);


                       //SHA256 encrypt
                       byte[] enPwd = (SHA256.Encrypt(sPwd));
                       String senpwd = byte2hex(enPwd);

                       //generate the  SAEkey and MACkey
                       String saeskey = new String();
                       String smackey = new String();

                       saeskey = senpwd.substring(0,32);
                       smackey = senpwd.substring(32, 64);
                       //generate the IV
                       byte[] IV = generateIV();
                       sivParameter = byte2hex(IV);

                       // AES encryption
                       byte[] enByte = AesCBC.getInstance().encrypt(cSrc,"utf-8",saeskey,IV);
                       String enString = byte2hex(enByte);

                       //HMACSHA1
                       String enAes = sivParameter+ enString;
                       sHMAC = byte2hex(HMACSHA1.HmacSHA1Encrypt(enAes,smackey));

                       //The encrypted file will be the concatenation of the following data: 16-byte IV ||ciphertext || 20-byte HMAC
                       File fenMsg = new File("src/msg.8102");

                       fenMsg.createNewFile();
                       FileOutputStream fos = new FileOutputStream("src/msg.8102");
                       fos.write(hex2byte( sivParameter + enString + sHMAC));

                       fos.close();
                       fMsg.delete();

                   }
                   else if (scmd.equals("d - msg.8102"))
                   {
                       System.out.println("please input the password");
                       String sPwdTemp = sc.nextLine();

                       byte[] enPwd = (SHA256.Encrypt(sPwdTemp));
                       String senpwd = byte2hex(enPwd);

                       //generate the  SAEkey and MACkey
                       String saeskey = new String();
                       String smackey = new String();

                       saeskey = senpwd.substring(0,32);
                       smackey = senpwd.substring(32, 64);

                       //get the enString will be the concatenation of the following data: 16-byte IV ||ciphertext || 20-byte HMAC
                       byte[] enByteString = readByteFile(("src/msg.8102"));

                       //divide the text into 3 parts
                       byte[] ByteIV  = new byte[16];
                       byte[] Byteciphertext = new byte[enByteString.length - 36];
                       byte[] ByteHmac = new byte[20];

                       System.arraycopy(enByteString, 0, ByteIV, 0, 16);
                       System.arraycopy(enByteString, 16, Byteciphertext, 0, enByteString.length - 36);
                       System.arraycopy(enByteString, enByteString.length - 20, ByteHmac, 0, 20);


                       if (!byte2hex(ByteHmac).equals(byte2hex(HMACSHA1.HmacSHA1Encrypt(byte2hex(ByteIV) + byte2hex(Byteciphertext) ,smackey))))
                       {
                           System.out.println("wrong password or possibly corrupted file");
                           break;
                       }


                       //ASE decryption
                       String DeString = AesCBC.getInstance().decrypt(Byteciphertext,"utf-8",saeskey, ByteIV);
                       System.out.println(DeString);

                       File fenMsg = new File("src/msg.8102");
                       File fOrMsg = new File("src/msg");
                       fenMsg.createNewFile();

                       FileOutputStream fos = new FileOutputStream("src/msg");
                       fos.write(DeString.getBytes());

                       fos.close();
                       fenMsg.delete();

                   }
                   else if (scmd.equals("exit"))
                   {
                       break;
                   }
                   else {
                       System.out.println("Exit: wrong command");
                   }

               }


    }
}