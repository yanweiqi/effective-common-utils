package com.effective.common.concurrent.pool;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {

    /**
     * 1.add：add方法在添加元素的时候，若超出了度列的长度会直接抛出异常
     */
    @Test
    public void testAdd(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);
            queue.add("hello");
            queue.add("world");
            queue.add("yes");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 对于put方法，若向队尾添加元素的时候发现队列已经满了会发生阻塞一直等待空间，以加入元素。
     */
    @Test
    public void testPut(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);

            queue.put("hello");
            queue.put("world");
            queue.put("yes");

            System.out.println("yes");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * offer方法在添加元素时，如果发现队列已满无法添加的话，会直接返回false,不会抛异常。
     */
    @Test
    public void testOffer(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);

            boolean bol1=queue.offer("hello");
            boolean bol2=queue.offer("world");
            boolean bol3=queue.offer("yes");

            System.out.println(queue.toString());
            System.out.println(bol1);
            System.out.println(bol2);
            System.out.println(bol3);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 不阻塞，弹出null
     */
    @Test
    public void testPoll(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);
            queue.offer("hello");
            queue.offer("world");
            String a = queue.poll();
            System.out.println("第1次弹出 "+a);
            String b = queue.poll();
            System.out.println("第2次弹出 "+b);
            String c = queue.poll();
            System.out.println("第3次弹出 "+c);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 不阻塞，弹出null
     */
    @Test
    public void testPollTimeout(){
        try {
            BlockingQueue<String> queue=new LinkedBlockingQueue<>(2);
            queue.offer("hello");
            queue.offer("world");
            String a = queue.poll();
            System.out.println("第1次弹出 "+a);
            String b = queue.poll();
            System.out.println("第2次弹出 "+b);
            String c = queue.poll(1, TimeUnit.SECONDS);
            System.out.println("第3次弹出 "+c);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 不阻塞，弹出null
     */
    @Test
    public void testTake(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);
            queue.offer("hello");
            queue.offer("world");
            String a = queue.take();
            System.out.println("第1次弹出 "+a);
            String b = queue.take();
            System.out.println("第2次弹出 "+b);
            String c = queue.take();
            System.out.println("第3次弹出 "+c);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Test
    public void testPeek(){
        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);
            queue.offer("hello");

            String a = queue.peek();
            System.out.println("第1次弹出 "+a);
            queue.poll();
            String b = queue.peek();
            System.out.println("第2次弹出 "+b);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Test
    public void testDateTime(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当天的零点:  "+ LocalDateTime.of(now.toLocalDate(), LocalTime.MIN));
    }
}
