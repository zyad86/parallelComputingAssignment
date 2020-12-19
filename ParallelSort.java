import java.io.*;


public class ParallelSort {
    public static final String filename = "random.txt";

    public static void main(String[] args) {
        FileLoader fileLoader = new FileLoader();
        int []dataToSort = fileLoader.readFile(filename);
        System.out.println("Hello World");
    }
}

class FileLoader {
    FileLoader(){super();}
    public int[] readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
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

