package com.algorithm.sort;

/**
 * 插入排序算法：每次处理一个数，把这个数插入到前面已经排好序到队列中
 * arr[0,i) 已排序，arr[i,n) 未排序，把arr[i]，放到合适到位置
 */
public class InsertSort {
    public static void main(String[] args) {
        Integer[] arr = {4, 6, 5, 3, 2, 1};
        sort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        //验证选择排序算法的效率 算法复杂度是：O(n*n)
        Integer[] dataSize = {10000, 100000};
        for (Integer n : dataSize) {
            Integer[] arrs = ArrayGenerator.generateRandomArray(n, n);
            SortingHelper.sortTest("InsertSort", arrs);
        }

    }

    public static <E extends Comparable<E>> void sort(E[] arr) {
        //4 6 3 5 2 1 使用插入排序法进行排序
        //3 4 6 5 2 1
        //3 4 5 6 2 1
        //2 3 4 5 6 1
        //1 2 3 4 5 6
        //当前位置前后比对 进行替换
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j].compareTo(arr[j - 1]) < 0) {
                    swap(arr, j, j - 1);
                } else break;
            }
        }
    }

    public static <E extends Comparable<E>> void sort2(E[] arr) {
        for (int i = 0; i < arr.length; i++) {
            E temp = arr[i];
            int j;
            for (j = i; j > 0 && temp.compareTo(arr[j - 1]) < 0; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = temp;
        }
    }

    private static <E extends Comparable<E>> void swap(E[] arr, int i, int j) {
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
