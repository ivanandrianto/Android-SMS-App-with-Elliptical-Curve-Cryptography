package com.chibi48.sms.model;

import java.math.BigInteger;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Hp
 */
public class SHA {
    private String A = "gE#";
    private String B = "ïÍ«‰";
    private String C = "˜ºÜþ";
    private String D = "2Tv";
    private String E = "ÃÒáð";

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

    public SHA(){

    }
    //TASK 1
    private ArrayList<String> padString(String message){
        DSS dss = new DSS();
        ArrayList<String> ret = new ArrayList<String>();
        char pad = (char) 0x00;
        char firstpad = (char) 0x80;
        boolean first = true;

        BigInteger originalLength = BigInteger.valueOf(message.length());
        String padLength = getString(originalLength);

        while(padLength.length()<8){

            padLength = pad+padLength;
        }

        while((message.length()%56)!=0){
            if(first){
                message+=firstpad;
                first = false;
            }else{
                message+=pad;
            }
        }
        for(int i = 0;i<message.length();i+=56){
            ret.add(message.substring(i,i+56)+padLength);
        }
        return ret;
    }

    private String kT(int t){
        String ret = "";
        if(t<20){
            ret = "Z‚y™";
        }else if(t<40){
            ret = "nÙë¡";
        }else if(t<60){
            ret = "¼Ü";
        }else if(t<80){
            ret = "ÊbÁÖ";
        }

        return ret;
    }

    private String specialFunction(int t, String b, String c, String d){
        String res = "";

        if(t<20){
            BigInteger partA = stringToBig(b).and(stringToBig(c));
            BigInteger partB = stringToBig(d).andNot(stringToBig(b));
            res = getString(partA.or(partB));
        }else if(t<40){
            BigInteger partA = stringToBig(b).xor(stringToBig(c)).xor(stringToBig(d));
            res = getString(partA);
        }else if(t<60){
            BigInteger partA = stringToBig(b).and(stringToBig(c));
            BigInteger partB = stringToBig(b).and(stringToBig(d));
            BigInteger partC = stringToBig(c).and(stringToBig(d));
            res = getString(partA.or(partB).or(partC));
        }else if(t<80){
            BigInteger partA = stringToBig(b).xor(stringToBig(c)).xor(stringToBig(d));
            res = getString(partA);
        }

        char pad = (char) 0x00;
        while(res.length()<64){
            res+=pad;
        }
        if(res.length()>64){
            res=res.substring(res.length()-64);
        }
        return res;
    }

    private String HSHA(String message){
        String ret = "";
        ArrayList<String> W = new ArrayList<String>();
        for(int i = 0;i<message.length();i+=4){
            W.add(message.substring(i,i+4));
        }

        for(int i = 16; i<80;i++){
            BigInteger wNumber = stringToBig(W.get(i-3)).xor(stringToBig(W.get(i-8))).xor(stringToBig(W.get(i-14))).xor(stringToBig(W.get(i-16)));
            wNumber = wNumber.shiftLeft(1);
            String res = getString(wNumber);
            char pad = (char) 0x00;
            while(res.length()<4){
                res+=pad;
            }
            if(res.length()>4)res = res.substring(res.length()-4);
            W.add(res);
        }

        String H0 = A;String H1 = B;String H2 = C;String H3 = D;String H4 = E;

        for (int i = 0; i<80;i++){
            String temp = getString(stringToBig(A).add(stringToBig(specialFunction(i,B,C,D))).add(stringToBig(E)).add(stringToBig(W.get(i))).add(stringToBig(kT(i))));
            E = D;
            D = C;
            C = B;
            B = A;
            A = temp;
        }

        H0 = getString(stringToBig(A).add(stringToBig(H0)));
        H1 = getString(stringToBig(B).add(stringToBig(H1)));
        H2 = getString(stringToBig(C).add(stringToBig(H2)));
        H3 = getString(stringToBig(D).add(stringToBig(H3)));
        H4 = getString(stringToBig(E).add(stringToBig(H4)));

        ret = H0.substring(H0.length()-4)
                + H1.substring(H1.length()-4)
                + H2.substring(H2.length()-4)
                + H3.substring(H3.length()-4)
                + H4.substring(H4.length()-4);
        return ret;
    }

    public String hash(String message){
        ArrayList<String> m = padString(message);
        ArrayList<String> result = new ArrayList<String>();

        for(int i =0;i<m.size();i++){
            result.add(HSHA(m.get(i)));
        }

        BigInteger res = stringToBig(result.get(0));
        for(int i =1;i<result.size();i++){
            res = res.xor(stringToBig(result.get(1)));
        }

        String hasil = DatatypeConverter.printHexBinary(getString(res).getBytes());
        return hasil.toLowerCase();
    }
}