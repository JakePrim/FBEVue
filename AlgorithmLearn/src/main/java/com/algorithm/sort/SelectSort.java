package com.algorithm.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 排序算法：选择排序算法：选择最小的放在有序的开始位置 arr[i,n) 未排序，arr[0,i) 已排序
 * 1. 先把最小的拿出来，放到i=0,i++
 * 2. 剩下的，再把最小的拿出来,放大i的位置，i++
 * ....
 * 每次选择还没处理的元素里最小的元素
 */
public class SelectSort {
    private SelectSort() {
    }

    /**
     * arr[i,n) 已排序，arr[0,i) 未排序
     *
     * @param arr
     * @param <E>
     */
    public static <E extends Comparable<E>> void sortTail(E[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            //i 从n开始排序
            int maxIndex = i;
            for (int j = i; j >= 0; j--) {
                if (arr[j].compareTo(arr[maxIndex]) > 0) {
                    maxIndex = j;
                }
            }
            if (maxIndex < i) {
                swap(arr, i, maxIndex);
            }
        }
    }

    /**
     * 从头到尾部排序 arr[0,i) 已排序，arr[i,n) 未排序
     *
     * @param arr
     * @param <E>
     */
    public static <E extends Comparable<E>> void sort(E[] arr) {
        for (int i = 0; i < arr.length; i++) {
            //选择arr[i,n) 中的最小值索引，其中arr[0,i)是有序的
            int minIndex = i;
            for (int j = i; j < arr.length; j++) {
                //compareTo 相当于arr[j]-arr[minIndex] < 0
                if (arr[j].compareTo(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            //交换位置 arr[i] 和 arr[minIndex] minIndex 一定是大于i的如果 等于就没必要交换
            if (minIndex > i) {
                swap(arr, i, minIndex);
            }
        }
    }

    private static <E extends Comparable<E>> void swap(E[] arr, int i, int minIndex) {
        E temp = arr[i];
        arr[i] = arr[minIndex];
        arr[minIndex] = temp;
    }

    public static void main(String[] args) {
        // 【6 4 2 3 1 5】 需要新开辟一个数组，占用了额外的空间。优化：可否实现原地排序？
        // 索引i 和 j, j 开始查找最小值
        // arr[i,n) 中最小值放到arr[i]的位置 i++
        Integer[] arr = {6, 4, 3, 1, 2, 5};
        sort(arr);
        for (Integer integer : arr) {
            System.out.println("arr = " + integer);
        }

        Student[] stus = {new Student("张三", 6), new Student("张三2", 1),
                new Student("张三3", 2), new Student("张三5", 3),
                new Student("张三4", 4), new Student("张三6", 5)};
        sort(stus);
        for (Student student : stus) {
            System.out.println("stu = " + student);
        }

        //验证选择排序算法的效率 算法复杂度是：O(n*n)
        Integer[] dataSize = {10000, 100000};
        for (Integer n : dataSize) {
            Integer[] arrs = ArrayGenerator.generateRandomArray(n, n);
            SortingHelper.sortTest("SelectionSortTail", arrs);
        }
    }


}
