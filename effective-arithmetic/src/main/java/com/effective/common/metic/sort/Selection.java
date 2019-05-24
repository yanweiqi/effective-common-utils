package com.effective.common.metic.sort;


/**
 * @author yanweiqi
 */
public class Selection extends BaseSort {


    public static void main(String[] args) {
        Selection selection = new Selection();
        selection.traversal(a);
        System.out.println("--------------------");

        selection.sort(a);

        System.out.println("--------------------");
        selection.traversal(a);
    }


    @Override
    public int[] sort(int[] a) {
        if (a.length <= 1) {
            return a;
        }

        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                System.out.printf("|a[%d] a[%d] |", i, j);
                if (a[i] < a[j]) {
                    int tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                }
            }
            System.out.println();
        }
        return a;
    }
}
