import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataLoader {
    private static ArrayList<String> headingsList = new ArrayList<>();
    private static DataFrame dataFrame = new DataFrame();

    public static ArrayList<String> getHeadingsList() {
        return headingsList;
    }

    public static DataFrame getDataFrame() {
        return dataFrame;
    }

    public static DataFrame loadFile(String filename) throws IOException {
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(filename)); // bufferedReader is faster than using a Scanner
            headingsList = new ArrayList<>(Arrays.asList(reader.readLine().split(","))); //adds each word in the first line of csv file as the name of columns.
            for(String x : headingsList){
                Column column = new Column(x);
                dataFrame.addColumn(column); //one column per word in the first line (headings).
            }

            int numberOfColumns = dataFrame.getColumnNames().size();

            String line;
            String[] words;
            while((line = reader.readLine()) != null){
                words = line.split(",");
                if(words.length < numberOfColumns){ // this is for the special case when the last value of the line in the csv file is blank, in order to have columns of the same size and avoid displacement.
                    words = increaseSize(words);
                }
                for(int i = 0; i< words.length; i++){ //adds each word in a line to their respective columns
                    int indexMod = i % numberOfColumns;
                    dataFrame.getColumn(indexMod).addRowValue(words[i]);
                }
            }
        } catch (IOException ioe) {
            throw ioe;      //for cases when we try to open inexistent files, caught in GUI class.
        } finally {
            reader.close();
        }
        return dataFrame;
    }

    public static String[] increaseSize(String[] array){    //used for cases when the last value of a line is null, it just increases the size of an array of strings by 1 and adds a blank entry at the end of the array/
        if(array != null){
            String[] newArray = new String[array.length+1];
            for(int i=0; i< array.length; i++){
                newArray[i] = array[i];
            }
            newArray[newArray.length-1] = "";
            return newArray;
        }
        return null;
    }

    public DataLoader(){
        dataFrame = new DataFrame();
        headingsList = new ArrayList<>();
    }
}
