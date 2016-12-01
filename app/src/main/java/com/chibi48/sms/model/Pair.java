package com.chibi48.sms.model;

public class Pair {
    public Point A;
    public Point B;
    public Pair(Point _A,Point _B){
        this.A = new Point(_A.x,_A.y);
        this.B = new Point(_B.x,_B.y);
    }

    @Override
    public String toString(){
        return "<"+A+" , "+B+">";
    }
}