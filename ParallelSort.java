import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


public class ParallelSort {
    public static final String filename = "random.txt";
    public static int run_times = 100;

    public static void main(String[] args) throws InterruptedException {
        FileLoader fileLoader = new FileLoader();
        int[] dataToSort = fileLoader.readFile(filename);
        SortSolution sortSolution = new SortSolution();

        double[] quickSortRunTime = new double[run_times];
        double[] coutingSortRunTime = new double[run_times];
        double[] mergeSortRunTime = new double[run_times];
        for (int i = 0; i < run_times; i++) {
            int[] quickSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] countingSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] mergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] fourMergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] forkMergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] fourForkMergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] eightMergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);

            double duelquick = duelThreadQuickSort(quickSortData);
            quickSortRunTime[i] = duelquick;
            double duelcount = duelThreadCountingSort(countingSortData);
            coutingSortRunTime[i] = duelcount;
            double duelmerge = duelThreadMergeSort(mergeSortData);
            mergeSortRunTime[i] = duelmerge;
//            double forkjoin = ForkJoinImplementOfMergeSort(forkMergeSortData);
//            quickSortRunTime[i] = forkjoin;
//            double fourthread = fourThreadMergeSort(fourMergeSortData);
//            coutingSortRunTime[i] = fourthread;
//            double eightthread = eightThreadMergeSort(eightMergeSortData);
//            quickSortRunTime[i] = eightthread;
        }
        calculateMeanAndStdOfParallel(quickSortRunTime, coutingSortRunTime, mergeSortRunTime);


        int[] quickSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        int[] countingSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        int[] mergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        duelThreadQuickSort(quickSortData);
        duelThreadCountingSort(countingSortData);
        duelThreadMergeSort(mergeSortData);
        sortSolution.verify(quickSortData);
        sortSolution.verify(countingSortData);
        sortSolution.verify(mergeSortData);
        fileLoader.writeFile("order4.txt", quickSortData);
        fileLoader.writeFile("order5.txt", countingSortData);
        fileLoader.writeFile("order6.txt", mergeSortData);
    }

    public static double duelThreadMergeSort(int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length >> 1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, 0, center);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, center + 1, data.length - 1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data, 0, center, data.length - 1);
        long endTime = System.currentTimeMillis();
        return (double) (endTime - startTime);
    }

    public static double fourThreadMergeSort(int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int secondcenter = data.length >> 1;
        int firstcenter = (0 + secondcenter) >> 1;
        int thirdcenter = (secondcenter + data.length) >> 1;
        CountDownLatch latch = new CountDownLatch(4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, 0, firstcenter);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, firstcenter + 1, secondcenter);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, secondcenter + 1, thirdcenter);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, thirdcenter + 1, data.length - 1);
                latch.countDown();
            }
        }).start();
        latch.await();
        sortSolution.mergeSortInOrder(data, 0, firstcenter, secondcenter);
        sortSolution.mergeSortInOrder(data, secondcenter + 1, thirdcenter, data.length - 1);
        sortSolution.mergeSortInOrder(data, 0, secondcenter, data.length - 1);
        long endTime = System.currentTimeMillis();
        return (double) (endTime - startTime);
    }

    public static double eightThreadMergeSort(int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int fourth = data.length >> 1;
        int second = (0 + fourth) >> 1;
        int first = (0 + second) >> 1;
        int third = (second + fourth) >> 1;
        int sixth = (fourth + data.length) >> 1;
        int fifth = (fourth + sixth) >> 1;
        int seven = (sixth + data.length) >> 1;
        CountDownLatch latch = new CountDownLatch(4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, 0, first);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, first + 1, second);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, second + 1, third);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, third + 1, fourth);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, fourth + 1, fifth);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, fifth + 1, sixth);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, sixth + 1, seven);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.MergeSort(data, seven + 1, data.length - 1);
                latch.countDown();
            }
        }).start();
        latch.await();
        sortSolution.mergeSortInOrder(data, 0, first, second);
        sortSolution.mergeSortInOrder(data, second + 1, third, fourth);
        sortSolution.mergeSortInOrder(data, fourth + 1, fifth, sixth);
        sortSolution.mergeSortInOrder(data, sixth + 1, seven, data.length - 1);
        sortSolution.mergeSortInOrder(data, 0, second, fourth);
        sortSolution.mergeSortInOrder(data, fourth + 1, sixth, data.length - 1);
        sortSolution.mergeSortInOrder(data, 0, fourth, data.length - 1);
        long endTime = System.currentTimeMillis();
        return (double) (endTime - startTime);
    }


    public static double duelThreadQuickSort(int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length >> 1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.QuickSort(data, 0, center, center + 1);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.QuickSort(data, center + 1, data.length - 1, data.length - center - 1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data, 0, center, data.length - 1);
        long endTime = System.currentTimeMillis();
        return (double) (endTime - startTime);
    }

    public static double duelThreadCountingSort(int[] data) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        SortSolution sortSolution = new SortSolution();
        int center = data.length >> 1;
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.CountingSort(data, 0, center);
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sortSolution.CountingSort(data, center + 1, data.length - 1);
                latch.countDown();
            }
        }).start();
        latch.await();

        sortSolution.mergeSortInOrder(data, 0, center, data.length - 1);
        long endTime = System.currentTimeMillis();
        return (double) (endTime - startTime);
    }

    static double ForkJoinImplementOfMergeSort(int[] mergeSortData) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ParallelSort.mergeTask task = new ParallelSort.mergeTask(mergeSortData, 0, mergeSortData.length - 1);//创建任务
        forkJoinPool.execute(task);//执行任务
        forkJoinPool.awaitTermination(20, TimeUnit.MILLISECONDS);//阻塞当前线程直到pool中的任务都完成了
        long endTime = System.currentTimeMillis(); //quickSort over
        return (double) (endTime - startTime);
    }

    static class mergeTask extends RecursiveAction {
        private static final int THRESHOLD = 2;//设置任务大小阈值
        private int start;
        private int end;
        private int[] data;

        public mergeTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            SortSolution sortSolution = new SortSolution();
            if ((end - start) <= THRESHOLD) {
                sortSolution.MergeSort(data, start, end);
            } else {
                int center = (start + end) >> 1;
                ParallelSort.mergeTask leftTask = new ParallelSort.mergeTask(data, start, center);
                ParallelSort.mergeTask rightTask = new ParallelSort.mergeTask(data, center + 1, end);
                leftTask.fork();
                rightTask.fork();
                leftTask.join();
                rightTask.join();
                sortSolution.mergeSortInOrder(data, start, center, end);
            }
        }
    }

    static void calculateMeanAndStdOfParallel(double[] quick, double[] count, double[] merge) {
        SortSolution sortSolution = new SortSolution();
        System.out.println("quick sort parallel runtime mean in " + run_times + " tims is: " + sortSolution.Mean(quick));
        System.out.println("quick sort parallel runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(quick));
        System.out.println("count sort parallel runtime mean in " + run_times + " tims is: " + sortSolution.Mean(count));
        System.out.println("count sort parallel runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(count));
        System.out.println("merge sort parallel runtime mean in " + run_times + " tims is: " + sortSolution.Mean(merge));
        System.out.println("merge sort parallel runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(merge));
    }
}

