package com.effective.common.metic.array;

public class Logn {

    public static void main(String[] args) {
        getN(5);
    }


    private static int getN(int n) {
        int i = 1;
        while (i <= n) {
            System.out.printf("%d = %d * 2 \n", i * 2, i);
            i = i * 2;

        }
        return i;
    }
}
