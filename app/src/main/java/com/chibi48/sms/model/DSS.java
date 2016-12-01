package com.chibi48.sms.model;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Hp
 */
public class DSS {
    private long p;
    private long q;
    private long h;

    public DSS(){
        p = 59419;
        q = 3301;
        h = 100;
    }

    private BigInteger randomBigInteger(BigInteger n) {
        Random rnd = new Random();
        int maxNumBitLength = n.bitLength();
        BigInteger aRandomBigInt;
        do {
            aRandomBigInt = new BigInteger(maxNumBitLength, rnd);
            // compare random number lessthan ginven number
        } while (aRandomBigInt.compareTo(n) > 0);
        return aRandomBigInt;
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

    private BigInteger stringToBig(String st){
        byte[] z = new byte[st.length()];
        for (int i = 0; i<st.length(); i++)
            z[i] = (byte)st.charAt(i);
        return new BigInteger(z);
    }

    public long generateG(){
        long power = (p)/q;
        BigInteger Tes = BigInteger.valueOf(h);
        Tes = Tes.modPow(BigInteger.valueOf(power), BigInteger.valueOf(p));
        return Tes.longValue();
    }

    public long KeyGenerator(long privateX){
        BigInteger Tes = BigInteger.valueOf(generateG());

        //generate public Key
        Tes = Tes.modPow(BigInteger.valueOf(privateX), BigInteger.valueOf(p));
        return Tes.longValue();
    }

    public String generateSigning(long privateX, String message){
        BigInteger r = BigInteger.valueOf(generateG());
        BigInteger kRandom = randomBigInteger(BigInteger.valueOf(q-1));
        r = r.modPow(kRandom, BigInteger.valueOf(p)).mod(BigInteger.valueOf(q));

        SHA sha = new SHA();

        BigInteger s = stringToBig(sha.hash(message));
        BigInteger kk = kRandom.modInverse(BigInteger.valueOf(q));
        s = s.add(BigInteger.valueOf(privateX).multiply(r)).multiply(kk).mod(BigInteger.valueOf(q));

        String ret = message + "\n----" + getString(r) + "\n----" + getString(s);


        return ret;

    }

    public boolean verifySigning(String message, long publicX){
        String[] arrMessage = message.split("\n----");

        BigInteger r = stringToBig(arrMessage[1]);
        BigInteger w = stringToBig(arrMessage[2]);
        w = w.modInverse(BigInteger.valueOf(q));

        SHA sha = new SHA();

        BigInteger u1 = stringToBig(sha.hash(arrMessage[0])).multiply(w).mod(BigInteger.valueOf(q));
        BigInteger u2 = r.multiply(w).mod(BigInteger.valueOf(q));

        BigInteger g = BigInteger.valueOf(generateG());
        BigInteger y = BigInteger.valueOf(publicX);

        BigInteger v = g.pow(u1.intValue()).multiply(y.pow(u2.intValue()));
        v = v.mod(BigInteger.valueOf(p)).mod(BigInteger.valueOf(q));

        return (v.equals(r));
    }

}
