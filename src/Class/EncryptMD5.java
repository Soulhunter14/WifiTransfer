package Class;

/**
 *      Instituto Tecnologico de Zacatepec
 * Descripcion:   Encriptacion de paswords en MD5
 * @author Gonzalo Silverio
 * Fecha: 03-12-2011
 * Archivo:  TestEncriptarMD5.java
 */
 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class EncryptMD5
{
    private static final char[] CONSTS_HEX = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
    public static String encriptaEnMD5(String stringAEncriptar)
    {
        try
        {
           MessageDigest msgd = MessageDigest.getInstance("MD5");
           byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
           StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
           for (int i = 0; i < bytes.length; i++)
           {
               int bajo = (int)(bytes[i] & 0x0f);
               int alto = (int)((bytes[i] & 0xf0) >> 4);
               strbCadenaMD5.append(CONSTS_HEX[alto]);
               strbCadenaMD5.append(CONSTS_HEX[bajo]);
           }
           return strbCadenaMD5.toString();
        } catch (NoSuchAlgorithmException e) {
           return null;
        }
    }

}