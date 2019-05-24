package com.effective.common.metic.sort;

public abstract class BaseSort implements Sort {

    protected static int[] a = new int[]{5, 6, 7, 8, 9, 0, 1, 2, 3, 4,12};


    @Override
    public abstract int[] sort(int[] a);

    @Override
    public void traversal(int[] a) {

        for (int i = 0; i < a.length; i++) {
            System.out.printf("%d ", a[i]);
        }
        System.out.println();
    }
}
