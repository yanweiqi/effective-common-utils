package com.effective.common.metic.sort;


import java.util.Arrays;

/**
 * 归并排序算法
 * @author yanweiqi
 */
public class Merge extends BaseSort {


    public static void main(String[] args) {

        Merge merge = new Merge();
        merge.traversal(a);
        System.out.println("--------------------");

        int[] b= merge.sort(a);
        System.out.println("--------------------");
        merge.traversal(b);
    }

    /**
     * @param a 等待排序的数组
     * @return int[]  排序后的数组
     * @Description: 使用函数的递归（嵌套）调用，实现归并排序（从小到大）
     */
    @Override
    public int[] sort(int[] a) {

        if (a == null) {
            return new int[0];
        }

        // 如果分解到只剩一个数，返回该数
        if (a.length == 1) {
            return a;
        }

        // 将数组分解成左右两半
        int mid = a.length / 2;
        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);

        System.out.printf("mid = %d,left = %s,right= %s \n",mid,Arrays.toString(left),Arrays.toString(right));
        // 嵌套调用，对两半分别进行排序
        left = sort(left);
        right = sort(right);

        // 合并排序后的两半
        int[] merged = merge(left, right);
        return merged;
    }


    /**
     * @Description: 合并两个已经排序完毕的数组（从小到大）
     * @param a- 第一个数组，b- 第二个数组
     * @return int[]- 合并后的数组
     */

    private static int[] merge(int[] a, int[] b) {
        System.out.println("-------------------");
        System.out.printf("开始合并 left = %s,right = %s \n",Arrays.toString(a),Arrays.toString(b));
        if (a == null)  {
            a = new int[0];
        }
        if (b == null) {
            b = new int[0];
        }

        int[] c = new int[a.length + b.length];

        int i = 0, j = 0, k = 0;

        // 轮流从两个数组中取出较小的值，放入合并后的数组中
        while (j < a.length && k < b.length) {
            if (a[j] <= b[k]) {
                c[i] = a[j];
                j ++;
            } else {
                c[i] = b[k];
                k ++;
            }
            i ++;
        }

        // 将某个数组内剩余的数字放入合并后的数组中
        if (j < a.length) {
            for (int n = j; n < a.length; n++) {
                c[i] = a[n];
                i ++;
            }
        } else {
            for (int m = k; m < b.length; m++) {
                c[i] = b[m];
                i ++;
            }
        }
        System.out.printf("合并后 c = %s \n",Arrays.toString(c));
        System.out.println("-------------------");
        return c;
    }
}



