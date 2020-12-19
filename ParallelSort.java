import java.io.*;
import java.util.Arrays;


public class ParallelSort {
    public static final String filename = "random.txt";
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //program start
        FileLoader fileLoader = new FileLoader();
        int []dataToSort = fileLoader.readFile(filename);
        int[] quickSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        int[] countingSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        int[] mergeSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        long dataLoadTime   = System.currentTimeMillis(); //dataload
        System.out.println("dataload spend : "+ (dataLoadTime-startTime)+"ms");
        SortSolution sortSolution = new SortSolution();
        //QuickSort
        long quickSortStartTime   = System.currentTimeMillis();
        sortSolution.QuickSort(quickSortData,0,quickSortData.length-1,quickSortData.length);
//        System.out.println(Arrays.toString(quickSortData));
        long quickSortEndTime   = System.currentTimeMillis(); //quickSort over
        System.out.println("QuickSort spend : "+ (quickSortEndTime - quickSortStartTime)+"ms");
        long countingSortStartTime   = System.currentTimeMillis();
        sortSolution.CountingSort(countingSortData);
//        System.out.println(Arrays.toString(countingSortData));
        long countingSortEndTime   = System.currentTimeMillis(); //quickSort over
        System.out.println("CountSort spend : "+ (countingSortEndTime - countingSortStartTime)+"ms");
        long mergeSortStartTime  = System.currentTimeMillis();
        sortSolution.MergeSort(mergeSortData,0,mergeSortData.length-1);
//        System.out.println(Arrays.toString(mergeSortData));
        long mergeSortEndTime   = System.currentTimeMillis(); //quickSort over
        System.out.println("MergeSort spend : "+ (mergeSortEndTime - mergeSortStartTime)+"ms");


//        System.out.println(Arrays.toString(dataToSort));
    }
}
