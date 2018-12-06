package com.effective.common.msgpack.entity;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by yanweiqi on 2018/12/4.
 */
@Message
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private int id;
    private String userName;
    private String passWord;

    public static UserInfo build(){
       return   new UserInfo();
    }

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassWord() {
        return passWord;
    }

    public UserInfo setPassWord(String passWord) {
        this.passWord = passWord;
        return this;
    }

    public byte[] encode(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.id);
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }

}
