package com.effective.common.base.jvm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * @author yanweiqi
 */
public class PrintGC {

    static List<Byte[]> list = new ArrayList();

    private static final int _1MB = 100 * 1024;

    public static void add(Byte[] b) {
        list.add(b);
    }


    public static void main(String args[]) {

        /**
         for (int i = 0; i < 100; i++) {
         add(new Byte[_1MB]);
         try {
         Thread.sleep(200);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         }*/

        int[] intArray = {4, 7, 2, 5, 3};

        BitSet bitSet = new BitSet(8);

        for (int i = 0; i < intArray.length; i++) {
            bitSet.set(intArray[i], true);
        }

        System.out.println(bitSet);


        System.out.println(Integer.MAX_VALUE + "，" + Integer.MIN_VALUE);


        String s0 = "0" + Integer.toBinaryString(2147483647);
        String s1 = "0" + Integer.toBinaryString(-2147483647);

        System.out.println(s0 + "," + s1);

        String[] ss = new String[4];

        int b0 = s0.length();
        int b1 = s1.length();

        System.out.println(b0 + "," + b1);
        int index = 0;
        for (int i = 0; i < 32; i += 8) {
            ss[index] = s0.substring(i, i + 8);
            index++;
        }
        for (int i = 0; i < ss.length; i++) {
            System.out.print(ss[i] + " ");
        }


        for (int i = 1; i < 8; i = i * 2) {
            System.out.println(i);
        }
    }
}
