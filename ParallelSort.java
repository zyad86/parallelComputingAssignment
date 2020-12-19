import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;


public class ParallelSort {
    public static final String filename = "random.txt";

    public static void main(String[] args)  throws InterruptedException{
        long startTime = System.currentTimeMillis(); //program start
        FileLoader fileLoader = new FileLoader();
        int []dataToSort = fileLoader.readFile(filename);
        int[] quickSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        int[] countingSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        int[] mergeSortData = (int[])Arrays.copyOf(dataToSort,dataToSort.length);
        duelThreadQuickSort(quickSortData);
        duelThreadCountingSort(countingSortData);
        duelThreadMergeSort(mergeSortData);

        fileLoader.writeFile("order4.txt",quickSortData);
        fileLoader.writeFile("order5.txt",countingSortData);
        fileLoader.writeFile("order6.txt",mergeSortData);

}

    public static void duelThreadMergeSort (int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length>>1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.MergeSort(data,0,center);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.MergeSort(data,center+1,data.length-1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data,0, center, data.length-1);
        long testTime = System.currentTimeMillis();
        System.out.println("Duel MergeSort spend : "+ (testTime - startTime)+"ms");
        sortSolution.verify(data);
    }

    public static void duelThreadQuickSort (int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length>>1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.QuickSort(data,0,center,center+1);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.QuickSort(data,center+1,data.length-1,data.length-center-1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data,0, center, data.length-1);
        long testTime = System.currentTimeMillis();
        System.out.println("Duel QuickSort spend : "+ (testTime - startTime)+"ms");
        sortSolution.verify(data);
    }

    public static void duelThreadCountingSort (int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length>>1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.CountingSort(data,0,center);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                sortSolution.CountingSort(data,center+1,data.length-1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data,0, center, data.length-1);
        long testTime = System.currentTimeMillis();
        System.out.println("Duel CountingSort spend : "+ (testTime - startTime)+"ms");
        sortSolution.verify(data);
    }

    static void ForkJoinImplementOfMergeSort (int[] mergeSortData)throws InterruptedException{
        long mergeSortStartTime  = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ParallelSort.mergeTask task = new ParallelSort.mergeTask(mergeSortData, 0, mergeSortData.length-1);//创建任务
        forkJoinPool.execute(task);//执行任务
        forkJoinPool.awaitTermination(100, TimeUnit.MILLISECONDS);//阻塞当前线程直到pool中的任务都完成了
        long mergeSortEndTime   = System.currentTimeMillis(); //quickSort over
        System.out.println("MergeSort spend : "+ (mergeSortEndTime - mergeSortStartTime)+"ms");
    }

    static class mergeTask extends RecursiveAction {
        private static final int THRESHOLD = 2;//设置任务大小阈值
        private int start;
        private int end;
        private int[] data;

        public mergeTask(int[] data, int start, int end){
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute(){
            SortSolution sortSolution = new SortSolution();
            if((end - start)<=THRESHOLD){
                sortSolution.MergeSort(data,start,end);
            }else{
                int center = (start + end)>>1;
                ParallelSort.mergeTask leftTask = new ParallelSort.mergeTask(data, start, center);
                ParallelSort.mergeTask rightTask = new ParallelSort.mergeTask(data,center+1, end);
                leftTask.fork();
                rightTask.fork();
                leftTask.join();
                rightTask.join();
                sortSolution.mergeSortInOrder(data, start, center, end);
            }
        }
    }
}

