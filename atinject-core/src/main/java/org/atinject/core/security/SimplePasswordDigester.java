package org.atinject.core.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimplePasswordDigester implements PasswordDigester {
    
    @Override
    public String digest(String input){
        
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
            byte[] digestedBytes = sha1.digest(inputBytes);
            return new String(encodeHex(digestedBytes));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    // taken from apache commons codec
    // see : org.apache.commons.codec.binary.Hex
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }
}
