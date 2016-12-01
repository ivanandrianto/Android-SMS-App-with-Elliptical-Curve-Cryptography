package com.chibi48.sms.model;

import java.math.BigInteger;

/**
 *
 * @author ivan
 */
public class BigIntegerHelper {

    private static final BigInteger b100 = new BigInteger("100");
    private static final boolean[] isSquareResidue;
    static{
        isSquareResidue = new boolean[100];
        for(int i =0;i<100;i++){
            isSquareResidue[(i*i)%100]=true;
        }
    }

    public static boolean isSquare(final BigInteger r) {
        final int y = (int) r.mod(b100).longValue();
        boolean check = false;
        if (isSquareResidue[y]) {
            final BigInteger temp = sqrt(r);
            if (r.compareTo(temp.pow(2)) == 0) {
                check = true;
            }
        }
        return check;
    }

    public static BigInteger sqrt(final BigInteger val) {
        final BigInteger two = BigInteger.valueOf(2);
        BigInteger a = BigInteger.ONE.shiftLeft(val.bitLength() / 2);
        BigInteger b;
        do {
            b = val.divide(a);
            a = (a.add(b)).divide(two);
        } while (a.subtract(b).abs().compareTo(two) >= 0);
        return a;
    }

    public static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

}
