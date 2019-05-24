package com.effective.common.metic.sort;


/**
 * @author yanweiqi
 */
public class Bubble extends BaseSort {

    public static void main(String[] args) {
        Bubble bubble = new Bubble();
        bubble.traversal(a);
        System.out.println("--------------------");

        bubble.sort(a);

        System.out.println("--------------------");
        bubble.traversal(a);
    }

    @Override
    public int[] sort(int[] a) {

        int n = a.length;
        if (n <= 0) {
            return a;
        }

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n - i - 1; j++) {
                System.out.printf("|a[%d] a[%d] |", i, j);
                if (a[j] < a[j+1]) {
                    int tmp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tmp;
                }
            }
            System.out.println();
        }
        return a;
    }
}
