package com.chibi48.sms.model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Hp
 */
public class ECC {

    private Point base;
    private Point publicKey;

    public ECC(){
        base = new Point(new BigInteger("12345678"));
        GeneratePublicKey gen = new GeneratePublicKey(secretKey,base);
        publicKey = gen.getPublicKey();
    }
    private final BigInteger k = new BigInteger("20");
    private final BigInteger secretKey = new BigInteger("10320885690046317857");

    private Pair encrypt(BigInteger m, Point _base, Point publicKey){

        Point pM = new Point(m);
        Point A = new Point(_base.x,_base.y);
        A.times(k);
        Point B = new Point(publicKey.x,publicKey.y);
        B.times(k);
        B.add(pM);
        return new Pair(A,B);
    }

    private String getString(BigInteger big){
        String ret = "";
        byte[] z = big.toByteArray();

        for (int i = 0; i < z.length; i++)
            ret+= (char)(z[i] & 0xFF);

        if (ret.length() == 1)
            ret = (char)256 + ret;

        return ret;
    }

    private BigInteger backToBig(String st){
        byte[] z = new byte[2];
        for (int i = 0; i<2; i++)
            z[i] = (byte)st.charAt(i);
        return new BigInteger(z);
    }

    private BigInteger decrypt(Pair cipher, Point _base){

        Point item1 = new Point(_base.x,_base.y);
        item1.times(k);
        item1.times(secretKey);

        Point item2 = new Point(cipher.B.x,cipher.B.y);
        item2.subtract(item1);
        return item2.x;

    }

    public String encryptString(String plainteks) throws UnsupportedEncodingException{
        String encryptedText = "";
        int count  = 0;

        for (int i = 0; i < plainteks.length(); i++) {
            String str = "" + plainteks.charAt(i);
            BigInteger m = new BigInteger(str.getBytes());
            Pair encryptedPair = encrypt(m,base,publicKey);
            if(count == 0){
                BigInteger X = encryptedPair.A.x;
                BigInteger Y = encryptedPair.A.y;
                encryptedText += getString(X);
                encryptedText += getString(Y);
                count++;
            }
            encryptedText += getString(encryptedPair.B.x);
            encryptedText += getString(encryptedPair.B.y);
        }
        return DatatypeConverter.printHexBinary(encryptedText.getBytes());
    }

    public String decryptString(String cipherteks){
        cipherteks = new String(DatatypeConverter.parseHexBinary(cipherteks));
        List<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < cipherteks.length()) {
            strings.add(cipherteks.substring(index, Math.min(index + 2,cipherteks.length())));
            index += 2;
        }
        String keyA = strings.get(0);
        String keyB = strings.get(1);
        String plainteks = "";

        for(int i = 2; i<strings.size(); i+=2){
            String cipherLeft = strings.get(i);
            String cipherRight = strings.get(i+1);
            Pair cipher = new Pair(new Point(backToBig(keyA),backToBig(keyB)),new Point(backToBig(cipherLeft),backToBig(cipherRight)));

            BigInteger plain = decrypt(cipher,base);
            plainteks += new String(plain.toByteArray());
        }
        return plainteks;
    }

}