import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Model {
    private DataFrame dataFrame;

    public DataFrame getDataFrame(){
        return dataFrame;
    }

    public Model(DataFrame dataFrame){
        this.dataFrame = dataFrame;
    }

    public ArrayList<String> getListOfHeadings() {
        return dataFrame.getColumnNames();
    }

    public ArrayList<ArrayList<String>> getColumnsAsStrings() {
        ArrayList<ArrayList<String>> arrayOfColumns = new ArrayList<>();
        for (Column c : dataFrame.getcList()) {
            arrayOfColumns.add(c.getList());
        }
        return arrayOfColumns;
    }

    public ArrayList<String> getLines() { // returns an arraylist of each of the lines in our csv file, used by the GUI to display each of its lines.
        ArrayList<String> listOfLines = new ArrayList<>();
        for (int j = 0; j < dataFrame.getRowCount(); j++) {
            String string = new String();
            for (int i = 0; i < dataFrame.getcList().size(); i++) {
                if(dataFrame.getcList().get(i).getShowable()){
                    string = string + dataFrame.getcList().get(i).getRowValue(j) + ",  ";
                }
            }
            listOfLines.add(string);
        }
        return listOfLines;
    }

    public ArrayList<String> searchLines(String searched){ //used by the GUI to implement searching
        ArrayList<String> listOfLines;
        ArrayList<String> results = new ArrayList<>();
        listOfLines = getLines();
        for(String s: listOfLines){
            if(s.contains(searched)){
                results.add(s);
            }
        }
        return results;
    }

    public void switchShowable(String string){ //changes showable value of a column to choose whether to display it or not in our GUI.
        for(Column column : dataFrame.getcList()){
            if(column.getName().equals(string)){
                column.switchShowable();
            }
        }
    }

    public static long calculateAgeInDays(String birthdate){ //simple code to calculate the age in days of a person by using the birthdate in format "yyyy-mm-dd"

        if(birthdate == null) return 0;

        String[] temp;
        String delimiter = "-";

        temp = birthdate.split(delimiter);
        int year = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int day = Integer.parseInt(temp[2]);

        LocalDate dateOfBirth = LocalDate.of(year, month, day);

        Date date = new Date();
        LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long days = ChronoUnit.DAYS.between(dateOfBirth, currentDate);

            if (currentDate != null) {
                return days;
            } else {
                return 0;
            }
    }

    public String getOldestPerson(){  //used by the GUI to be able to display the oldest person in our database.
        String oldestBirthdate = null;
        for(String s : dataFrame.getColumn("BIRTHDATE").getList()){
            if(s.equals("BIRTHDATE")) continue;
            if((calculateAgeInDays(oldestBirthdate) < calculateAgeInDays(s))){
                oldestBirthdate = s;
            }
        }
        return oldestBirthdate;
    }

}
