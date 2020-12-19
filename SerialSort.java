import java.io.*;
import java.util.Arrays;


public class SerialSort {
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

class FileLoader {
    FileLoader(){super();}
    public int[] readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                if(line.trim().length() ==0 ){
                    return new int[0];
                }
                return getIntArrayFromStringArray(line.split(" "));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new int[0];
    }

    public static int[] getIntArrayFromStringArray(String[] strArray){
        if(strArray==null){
            return new int[0];
        }
        int[] result = new int [strArray.length];
        for(int i=0;i<result.length;i++){
            result[i] = Integer.valueOf(strArray[i]);
        }
        return result;
    }
}

class SortSolution {
    SortSolution(){super();}
    void QuickSort(int[] nums,int low,int high,int n){
        if(low<high) {
            int pivotpos = partition(nums,low,high);
            QuickSort(nums,low,pivotpos-1,n);
            QuickSort(nums,pivotpos+1,high,n);
        }
    }
    int partition(int[] nums,int low,int high){
        int pivot = nums[low];
        while(low<high) {
            while(low<high && nums[high]>=pivot)--high;
            nums[low] = nums[high];
            while(low<high && nums[low]<=pivot) ++low;
            nums[high] = nums[low];
        }
        nums[low] = pivot;
        return low;
    }

    void mergeSortInOrder(int[] arr,int bgn,int mid, int end){
        int l = bgn, m = mid +1, e = end;
        int[] arrs = new int[end - bgn + 1];
        int k = 0;
        while(l <= mid && m <= e){
            if(arr[l] < arr[m]){
                arrs[k++] = arr[l++];
            }else{
                arrs[k++] = arr[m++];
            }
        }
        while(l <= mid){
            arrs[k++] = arr[l++];
        }
        while(m <= e){
            arrs[k++] = arr[m++];
        }
        for(int i = 0; i < arrs.length; i++){
            arr[i + bgn] = arrs[i];
        }
    }
    void MergeSort(int[] arr, int bgn, int end)
    {
        if(bgn >= end){
            return;
        }
        int mid = (bgn + end) >> 1;
        MergeSort(arr,bgn,mid);
        MergeSort(arr,mid + 1, end);
        mergeSortInOrder(arr,bgn,mid,end);
    }

    private static final int OFFSET = 50000;

    public int[] CountingSort(int[] nums) {
        int len = nums.length;
        int size = 100001;

        int[] count = new int[size];
        for (int num : nums) {
            count[num + OFFSET]++;
        }

        for (int i = 1; i < size; i++) {
            count[i] += count[i - 1];
        }

        int[] temp = new int[len];
        System.arraycopy(nums, 0, temp, 0, len);
        for (int i = len - 1; i >= 0; i--) {
            int index = count[temp[i] + OFFSET] - 1;
            nums[index] = temp[i];
            count[temp[i] + OFFSET]--;
        }
        return nums;
    }
}
