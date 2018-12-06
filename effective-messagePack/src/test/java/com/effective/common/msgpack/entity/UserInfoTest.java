package com.effective.common.msgpack.entity;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.runner.RunWith;
import org.msgpack.MessagePack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by yanweiqi on 2018/12/5.
 */
@RunWith(JUnitPlatform.class)
@IncludeEngines("junit-jupiter")
@DisplayName("Testing using JUnit 5")
public class UserInfoTest {

    static ThreadLocal<Integer> loopCount = new ThreadLocal();
    static UserInfo userInfo = UserInfo.build()
            .setId(1000000)
            .setUserName("q1w2e3r4t5y6u7i8o9p0")
            .setPassWord("1000000");

    static {
        loopCount.set(1000000);
    }

    @Test
    public void testNioByteBufferString() throws UnsupportedEncodingException {
        byte[] value = userInfo.getUserName().getBytes();
        System.out.println("byte[]:" + Arrays.toString(value)+" size:"+value.length);
        String userName = new String(value, StandardCharsets.UTF_8);
        System.out.println("userName:" + userName);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(4);
        buffer.putInt(userInfo.getId());
        buffer.putInt(userInfo.getPassWord().length());
        buffer.put(userInfo.getPassWord().getBytes());
        buffer.flip();




        byte[] newByte = new byte[buffer.remaining()];
        System.out.println("newByte[]:" + Arrays.toString(newByte)+" size:"+newByte.length);
        buffer.get(newByte);
        String userName1 = new String(value, StandardCharsets.UTF_8);

        System.out.println("userName1:" + userName1);

        System.out.println("byteMax:"+Byte.MAX_VALUE);


        System.out.println("userInfoByte[]:"+Arrays.toString(buffer.array()) );
        byte[] userIdBytes = ByteBuffer.allocate(4).putInt(userInfo.getId()).array();
        System.out.println("userId:"+Arrays.toString(userIdBytes) );
    }

    public int convertirOctetEnEntier(byte[] b){
        int MASK = 0xFF;
        int result = 0;
        result = b[0] & MASK;
        result = result + ((b[1] & MASK) << 8);
        result = result + ((b[2] & MASK) << 16);
        result = result + ((b[3] & MASK) << 24);
        return result;
    }

    @Test
    public void testNioByteBufferInt() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.flip();
        System.out.println("userId:" + buffer.getInt());
    }

    /**
     * message pack 序列化的性能有很差，但是byte[]数组最小
     *
     * @return
     * @throws IOException
     */
    public long msgPackTest() throws IOException {
        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(userInfo);
        UserInfo userInfo1 = pack.read(bytes, UserInfo.class);
        return bytes.length;
    }

    public long serializableTest() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        return b.length;
    }

    public long nioByteBuffer() {
        return userInfo.encode().length;
    }

    @Test
    public void compareLength() throws IOException {
        System.out.println("The message pack length is:" + msgPackTest());
        System.out.println("The jdk serializable length is :" + serializableTest());
        System.out.println("The Nio byteBuffer length is:" + nioByteBuffer());
    }

    @Test
    public void comparePerformTest() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            long start = System.currentTimeMillis();

            for (int i = 0; i < loopCount.get(); i++) {
                try {
                    serializableTest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("JDK:" + (System.currentTimeMillis() - start));
        });

        Thread t2 = new Thread(() -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount.get(); i++) {
                try {
                    msgPackTest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("MessagePack:" + (System.currentTimeMillis() - start));
        });

        Thread t3 = new Thread(() -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount.get(); i++) {
                try {
                    nioByteBuffer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("ByteBuffer:" + (System.currentTimeMillis() - start));
        });


        t1.start();
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();

    }
}
