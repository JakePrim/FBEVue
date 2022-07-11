package com.algorithm.sort;

public class SortingHelper {
    private SortingHelper() {
    }

    /**
     * 判断一个数组是否是有序的
     *
     * @return
     */
    public static <E extends Comparable<E>> boolean isSorted(E[] arr) {
        for (int i = 1; i < arr.length; i++) {
            //从小到大的排序
            if (arr[i - 1].compareTo(arr[i]) > 0) return false;
        }
        return true;
    }

    public static <E extends Comparable<E>> void sortTest(String sortName, E[] arr) {
        long startTime = System.nanoTime();
        if (sortName.equals("SelectionSort")) {
            SelectSort.sort(arr);
        } else if (sortName.equals("SelectionSortTail")) {
            SelectSort.sortTail(arr);
        } else if (sortName.equals("InsertSort")) {
            InsertSort.sort(arr);
        }
        long endTime = System.nanoTime();

        double time = (endTime - startTime) / 1000000000.0;

        if (!SortingHelper.isSorted(arr)) {
            throw new RuntimeException(sortName + "failed");
        }

        System.out.printf("%s,n=%d:%fs%n", sortName, arr.length, time);
    }
}
