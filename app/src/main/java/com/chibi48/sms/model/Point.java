package com.chibi48.sms.model;

import java.math.BigInteger;

/**
 *
 * @author Hp
 */
public class Point implements java.io.Serializable {
    public BigInteger x,y;
    public boolean isInfinite;

    //dengan menggunakan persamaan garis y^2 = (x^3 - ax + b)mod p
    private static BigInteger a = new BigInteger("-1");
    private static BigInteger b = new BigInteger("188");
    private static BigInteger p = new BigInteger("751");

    public Point(BigInteger _x, BigInteger _y){
        this.x = _x;
        this.y = _y;
        this.isInfinite = false;
    }

    public Point(BigInteger _x){
        this.x = _x;
        this.y = Koblitz.encode2(_x);
        this.isInfinite = false;
    }


    private BigInteger countGradient(Point A){
        return this.y.subtract(A.y).multiply( this.x.subtract(A.x).mod(this.p).modInverse(this.p)).mod(this.p);
    }

    public void add(Point A){
        if(A.x.compareTo(this.x) == 0 && A.y.compareTo(this.y) == 0){
            doubles();
        }else if(this.isInfinite && A.isInfinite){
            this.x = BigInteger.ZERO;
            this.y = BigInteger.ZERO;
        }else if(this.isInfinite && !A.isInfinite){
            //titik A dicopy ke titik ini
            this.x = A.x;this.y = A.y; this.isInfinite = A.isInfinite;
        }else if(!this.isInfinite && A.isInfinite){
            //tidak terjadi apa-apa
            //apabila x ada pada nilai sama dan y berbeda, maka tidak akan ada titik potong
        }else if(A.x.compareTo(this.x) == 0 && A.y.compareTo(this.y) != 0){
            this.x = BigInteger.ZERO; this.y = BigInteger.ZERO; this.isInfinite = true;
        }else{

            BigInteger gradient = countGradient(A);
            BigInteger oldX = this.x;
            BigInteger oldY = this.y;
            BigInteger newX = gradient.pow(2).subtract(oldX).subtract(A.x).mod(this.p);
            BigInteger newY = gradient.multiply(oldX.subtract(newX)).subtract(oldY).add(this.p).mod(this.p);

            this.x = newX;
            this.y = newY;

        }
    }

    public void subtract(Point A){
        Point dummy = new Point(A.x , A.y.multiply(new BigInteger("-1")).mod(this.p));
        add(dummy);
    }

    public void doubles(){
        BigInteger gradient = this.x.multiply(this.x).multiply(new BigInteger("3")).add(this.a).multiply(new BigInteger("2").multiply(this.y).modInverse(this.p)).mod(this.p);
        BigInteger oldX = this.x;
        this.x = gradient.multiply(gradient).subtract(new BigInteger("2").multiply(this.x)).mod(this.p);
        this.y = gradient.multiply(oldX.subtract(this.x)).subtract(this.y).mod(this.p);
    }

    public void times(BigInteger A){
        if(A.compareTo(BigInteger.ZERO)==0){
            this.x = BigInteger.ZERO;
            this.y = BigInteger.ZERO;
            this.isInfinite = false;
        }else if(A.compareTo(BigInteger.ONE)==0){
            //do nothing
        }else if(A.mod(new BigInteger("2")).equals(BigInteger.ZERO)){
            this.doubles();
            times(A.divide(new BigInteger("2")));
        }else{
            add(this);
        }
    }

    @Override
    public String toString(){
        return "["+x+" , "+y+"]";
    }

}