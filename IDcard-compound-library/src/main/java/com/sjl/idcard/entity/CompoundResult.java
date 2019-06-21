package com.sjl.idcard.entity;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename CompoundResult.java
 * @time 2019/6/21 17:10
 * @copyright(C) 2019 song
 */
public class CompoundResult {
    private int code;
    private String msg;
    private IdentityCard identityCard;

    private CompoundResult(){

    }

    private CompoundResult(int code, String msg, IdentityCard identityCard) {
        this.code = code;
        this.msg = msg;
        this.identityCard = identityCard;
    }

    private CompoundResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CompoundResult build(int code, String msg, IdentityCard identityCard){
        return new CompoundResult(code,msg,identityCard);
    }

    public static CompoundResult build(int code, String msg){
        return new CompoundResult(code,msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(IdentityCard identityCard) {
        this.identityCard = identityCard;
    }
}
