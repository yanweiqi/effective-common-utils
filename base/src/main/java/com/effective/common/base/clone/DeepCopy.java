package com.effective.common.base.clone;

import java.io.*;

/**
 * Created by yanweiqi on 2016/7/1.
 */
public class DeepCopy<T> {


	public static <T> T deepClone(T t) throws Exception{
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (T)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
