import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  MessageDigest provides applications the functionality of a message digest algorithm, such as SHA-1 or SHA-256.
 *  encName provides the type of algorithm
 */
public class SHA256
{
    //encrypt the plaintext in sha-256
    private static final String encName = "SHA-256";

    public static byte[] Encrypt(String strSrc) {
        MessageDigest md = null;

        //convert the plaintext from string to byte, and put it into the byte[]
        byte[] bt = strSrc.getBytes();
        try {
            //initialize the MessageDigest in SHA-256 algorithm
            md = MessageDigest.getInstance(encName);
            //MessageDigest read the plaintext and doing  encryption.
            md.update(bt);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        //return the Digest in Byte type
        return md.digest();
    }
}
