import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA1 {
    //the name of algorithm
    private static final String MAC_NAME = "HmacSHA1";
    //the format of encoding
    private static final String ENCODING = "UTF-8";

    /**
     * @param encryptText 16-byte IV ||ciphertext
     * @param encryptKey  the key of the mac
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {

        byte[] data=encryptKey.getBytes(ENCODING);
        //Create a secret key which depend on the mac key data
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //Create a Mac object following the HmacSHA1 algorithm
        Mac mac = Mac.getInstance(MAC_NAME);
        //initialize the mac with secretKey
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        //encryption operation, depending on how this cipher was initialized
        return mac.doFinal(text);
    }

}