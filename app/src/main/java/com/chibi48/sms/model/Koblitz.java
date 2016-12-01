package com.chibi48.sms.model;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ivan
 */
public class Koblitz {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static BigInteger k = new BigInteger("20");
    private static BigInteger a = new BigInteger("-1");
    private static BigInteger b = new BigInteger("188");
    private static BigInteger p = new BigInteger("751");
    private static Set<BigInteger> set = new HashSet<BigInteger>();
    private static Set<String> setStr = new HashSet<String>();

    public Koblitz() {

    }

    public Koblitz(BigInteger a, BigInteger b, BigInteger p, BigInteger k, String text) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.k = k;
    }

    private static BigInteger getX (BigInteger m) {
        boolean found = false;
        BigInteger validX = new BigInteger("-1");
        int c = 1;

        while(!found) {
//            System.out.println("c: " + c);
            BigInteger c_bi = BigInteger.valueOf(new Integer(c).intValue());
            BigInteger x = m.multiply(k).add(c_bi);
//            System.out.println("x: " + x);
            BigInteger x3 = x.pow(3);
            BigInteger ax = a.multiply(x);
            BigInteger right = x3.add(ax).add(b);
            BigInteger rightMod = right.mod(p);
//            System.out.println("right: " + right);

            BigInteger power = (p.add((BigInteger.ONE))).divide(new BigInteger("4"));
//            System.out.println("power: " + power);
            //BigInteger rightPow = right.pow(power.intValue()); // power bisa jadi negatif -> ga bisa pake cara ini
            BigInteger rightPow =  BigIntegerHelper.pow(right, power); // Ga selesai2
            BigInteger y = rightPow.mod(p);
//            System.out.println("y: " + y);
            if (y.pow(2).mod(p).compareTo(rightMod) == 0) {
                validX = x;
//                System.out.print("vlidX: " + validX + " ");
                set.add(validX);
                found = true;
            }
            c++;
        }
//        System.out.println("found " + found);
        return validX;
    }


    public static String encode (String text) {
        String encoded = "";

        for (int i = 0; i < text.length(); i++) {
            System.out.print("i: " + i + " ");
            String str = text.substring(i, i + 1);
            BigInteger bi = new BigInteger(str.getBytes());
            String encodedStr = String.valueOf(getX(bi).toByteArray());
            System.out.println(encodedStr);
            setStr.add(encodedStr);
            encoded += encodedStr;
        }
        return encoded;
    }

    public static BigInteger encode2 (BigInteger bi) {
        return getX(bi);
    }

    public static char decode2 (BigInteger bi) {
        return (char)bi.subtract(BigInteger.ONE).divide(k).intValue();
    }

}

