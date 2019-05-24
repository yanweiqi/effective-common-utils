package com.effective.common.metic.sort;

/**
 * 插入排序中心思想
 *
 * 每次循环取i+1的元素，与前面已经排好的元素进行比较，找到自己的位置插入
 * @author yanweiqi
 */
public class Insertion extends BaseSort {

    public static void main(String[] args) {


        Insertion insertion = new Insertion();
        insertion.traversal(a);
        System.out.println("--------------------");
        insertion.sort(a);
        insertion.traversal(a);
    }


    @Override
    public int[] sort(int[] a) {

        // 插入排序，a 表示数组
        if (a.length <= 1) {
            return a;
        }

        for (int i = 1; i < a.length; i++) {

            int j = i - 1;
            int v = a[i];

            //对已经排好的元素进行比较，找到自己的位置
            for (; j >= 0; j--) {
                if(a[j] < v){
                    a[j+1] = a[j];
                } else {
                    break;
                }
            }
            a[j+1] = v;
        }

        return a;
    }
}
