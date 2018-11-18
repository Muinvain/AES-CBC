import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA256
{
    private static final String encName = "SHA-256";

    public static byte[] Encrypt(String strSrc) {
        MessageDigest md = null;

        byte[] bt = strSrc.getBytes();
        try {
            //initialize the MessageDigest with "SHA-256"
            md = MessageDigest.getInstance(encName);
            md.update(bt);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return md.digest();
    }
}
