import java.io.*;
import java.util.Arrays;


public class SerialSort {
    public static final String filename = "random.txt";
    public static int run_times = 100;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //program start
        FileLoader fileLoader = new FileLoader();
        int[] dataToSort = fileLoader.readFile(filename);
        double[] quickSortRunTime = new double[run_times];
        double[] coutingSortRunTime = new double[run_times];
        double[] mergeSortRunTime = new double[run_times];

        for (int i = 0; i < run_times; i++) {
            int[] quickSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] countingSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            int[] mergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
            long dataLoadTime = System.currentTimeMillis(); //dataload
            SortSolution sortSolution = new SortSolution();
            //QuickSort
            long quickSortStartTime = System.currentTimeMillis();
            sortSolution.QuickSort(quickSortData, 0, quickSortData.length - 1, quickSortData.length);
            long quickSortEndTime = System.currentTimeMillis(); //quickSort over
//            System.out.println("QuickSort spend : " + (quickSortEndTime - quickSortStartTime) + "ms");
            quickSortRunTime[i] = (double) (quickSortEndTime - quickSortStartTime);
            long countingSortStartTime = System.currentTimeMillis();
            sortSolution.CountingSort(countingSortData, 0, countingSortData.length - 1);
            long countingSortEndTime = System.currentTimeMillis(); //quickSort over
//            System.out.println("CountSort spend : " + (countingSortEndTime - countingSortStartTime) + "ms");
            coutingSortRunTime[i] = (double) (countingSortEndTime - countingSortStartTime);
            long mergeSortStartTime = System.currentTimeMillis();
            sortSolution.MergeSort(mergeSortData, 0, mergeSortData.length - 1);
            long mergeSortEndTime = System.currentTimeMillis(); //quickSort over
//            System.out.println("MergeSort spend : " + (mergeSortEndTime - mergeSortStartTime) + "ms");
            mergeSortRunTime[i] = (double) (mergeSortEndTime - mergeSortStartTime);
            //verify!
//            sortSolution.verify(quickSortData);
//            sortSolution.verify(countingSortData);
//            sortSolution.verify(mergeSortData);
        }
        calculateMeanAndStd(quickSortRunTime, coutingSortRunTime, mergeSortRunTime);
        SortSolution sortSolution = new SortSolution();

        int[] quickSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        int[] countingSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        int[] mergeSortData = (int[]) Arrays.copyOf(dataToSort, dataToSort.length);
        sortSolution.QuickSort(quickSortData, 0, quickSortData.length - 1, quickSortData.length);
        sortSolution.CountingSort(countingSortData, 0, countingSortData.length - 1);
        sortSolution.MergeSort(mergeSortData, 0, mergeSortData.length - 1);
        sortSolution.verify(quickSortData);
        sortSolution.verify(countingSortData);
        sortSolution.verify(mergeSortData);
        fileLoader.writeFile("order1.txt", quickSortData);
        fileLoader.writeFile("order2.txt", countingSortData);
        fileLoader.writeFile("order3.txt", mergeSortData);
    }

    static void calculateMeanAndStd(double[] quick, double[] count, double[] merge) {
        SortSolution sortSolution = new SortSolution();
        System.out.println("quick sort runtime mean in " + run_times + " tims is: " + sortSolution.Mean(quick));
        System.out.println("quick sort runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(quick));
        System.out.println("count sort runtime mean in " + run_times + " tims is: " + sortSolution.Mean(count));
        System.out.println("count sort runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(count));
        System.out.println("merge sort runtime mean in " + run_times + " tims is: " + sortSolution.Mean(merge));
        System.out.println("merge sort runtime std in " + run_times + " tims is: " + sortSolution.POP_STD_dev(merge));
    }
}

class FileLoader {
    FileLoader() {
        super();
    }

    public int[] readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                if (line.trim().length() == 0) {
                    return new int[0];
                }
                return getIntArrayFromStringArray(line.split(" "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    public void writeFile(String filename, int[] sortedOrder) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < sortedOrder.length; i++) {
                b.append(sortedOrder[i]);
                b.append(" ");
            }
            String fileContent = b.toString();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.close();
            System.out.println(filename + "Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getIntArrayFromStringArray(String[] strArray) {
        if (strArray == null) {
            return new int[0];
        }
        int[] result = new int[strArray.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.valueOf(strArray[i]);
        }
        return result;
    }
}

class SortSolution {
    SortSolution() {
        super();
    }

    public static void verify(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                System.out.println("sort failed");
                return;
            }

        }
        System.out.println("sort successful");
    }

    double Sum(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++)
            sum = sum + data[i];
        return sum;
    }

    double Mean(double[] data) {
        double mean = 0;
        mean = Sum(data) / data.length;
        return mean;
    }

    // population variance 总体方差
    double POP_Variance(double[] data) {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - Mean(data)), 2));
        }
        variance = variance / data.length;
        return variance;
    }

    // population standard deviation 总体标准差
    double POP_STD_dev(double[] data) {
        double std_dev;
        std_dev = Math.sqrt(POP_Variance(data));
        return std_dev;
    }

    public static void QuickSort(int[] nums, int low, int high, int n) {
        if (low < high) {
            int pivotpos = partition(nums, low, high);
            QuickSort(nums, low, pivotpos - 1, n);
            QuickSort(nums, pivotpos + 1, high, n);
        }
    }

    public static int partition(int[] nums, int low, int high) {
        int pivot = nums[low];
        while (low < high) {
            while (low < high && nums[high] >= pivot) --high;
            nums[low] = nums[high];
            while (low < high && nums[low] <= pivot) ++low;
            nums[high] = nums[low];
        }
        nums[low] = pivot;
        return low;
    }

    public static void mergeSortInOrder(int[] arr, int bgn, int mid, int end) {
        int l = bgn, m = mid + 1, e = end;
        int[] arrs = new int[end - bgn + 1];
        int k = 0;
        while (l <= mid && m <= e) {
            if (arr[l] < arr[m]) {
                arrs[k++] = arr[l++];
            } else {
                arrs[k++] = arr[m++];
            }
        }
        while (l <= mid) {
            arrs[k++] = arr[l++];
        }
        while (m <= e) {
            arrs[k++] = arr[m++];
        }
        for (int i = 0; i < arrs.length; i++) {
            arr[i + bgn] = arrs[i];
        }
    }

    public static void MergeSort(int[] arr, int bgn, int end) {
        if (bgn >= end) {
            return;
        }
        int mid = (bgn + end) >> 1;
        MergeSort(arr, bgn, mid);
        MergeSort(arr, mid + 1, end);
        mergeSortInOrder(arr, bgn, mid, end);
    }

    private static final int OFFSET = 50000;

    public static int[] CountingSort(int[] nums, int bgn, int end) {

//        int len = nums.length;
        int len = end + 1;
        int size = 100001;
        int[] count = new int[size];
        for (int i = bgn; i <= end; i++) {
            count[nums[i] + OFFSET]++;
        }
        for (int i = 1; i < size; i++) {
            count[i] += count[i - 1];
        }

        int[] temp = new int[nums.length];
        System.arraycopy(nums, bgn, temp, bgn, end - bgn + 1);
        for (int i = len - 1; i >= bgn; i--) {
            int index = count[temp[i] + OFFSET] - 1 + bgn;
            nums[index] = temp[i];
            count[temp[i] + OFFSET]--;
        }
        return nums;
    }
}
