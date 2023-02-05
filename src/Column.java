import java.util.ArrayList;

public class Column {
    private String name;
    private ArrayList<String> list;
    private Boolean showable; // this value will allow us to select whether to show or not a selected column in the GUI

    public String getName() {
        return name;
    }

    public Boolean getShowable() {
        return showable;
    }

    public void switchShowable() {
        showable = !showable;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public int getSize() {
        return list.size();
    }

    public String getRowValue(int i) {
        if (i >= list.size() || i < 0) {
            System.out.println("Index is out of bounds");
            System.exit(0);
        }
        return list.get(i);
    }

    public void setRowValue(int i, String value) {
        if (i >= list.size() || i < 0) {
            System.out.println("Index is out of bounds");
            System.exit(0);
        } else {
            list.set(i, value);
            if (i == 0) name = value;
        }
    }

    public void addRowValue(String value) { //adds a value to the end of the column
        if(value != null){
            list.add(list.size(), value);
        }
    }

    public Column(String name){
        if(name != null){
            list = new ArrayList<>();
            this.name = name;
            this.addRowValue(name);
            showable = true;
        }
    }
}
