package com.chibi48.sms.model;

import java.math.BigInteger;

/**
 *
 * @author Hp
 */
public class GeneratePublicKey {

    private BigInteger secret;
    private Point publicKey;

    public GeneratePublicKey(BigInteger _secret,Point _base){
        this.secret = _secret;
        this.publicKey = new Point(_base.x,_base.y);
        this.publicKey.times(secret);
    }

    public Point getPublicKey(){
        return this.publicKey;
    }
}
