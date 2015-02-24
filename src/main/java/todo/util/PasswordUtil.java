package todo.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * Source: http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java 
 *
 */
public class PasswordUtil
{
    // The higher the number of iterations the more 
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 20 * 1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;


    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static String getSaltedHash( String password )
    {
        try {
            byte[] salt;
            salt = SecureRandom.getInstance( "SHA1PRNG" ).generateSeed( saltLen );
            return Base64.encodeBase64String( salt ) + "$" + hash( password, salt );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }


    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check( String password, String stored )
    {
        String[] saltAndPass = stored.split( "\\$" );
        if ( saltAndPass.length != 2 ) {
            throw new IllegalStateException( "The stored password have the form 'salt$hash'" );
        }
        String hashOfInput = hash( password, Base64.decodeBase64( saltAndPass[0] ) );
        return hashOfInput.equals( saltAndPass[1] );
    }


    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash( String password, byte[] salt )
    {

        try {
            if ( password == null || password.length() == 0 )
                throw new IllegalArgumentException( "Empty passwords are not supported." );
            SecretKeyFactory f = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );
            SecretKey key;
            key = f.generateSecret( new PBEKeySpec( password.toCharArray(), salt, iterations, desiredKeyLen ) );
            return Base64.encodeBase64String( key.getEncoded() );
        } catch ( InvalidKeySpecException | NoSuchAlgorithmException e ) {
            e.printStackTrace();
            // This should never happen
            throw new IllegalStateException();
        }

    }
}
