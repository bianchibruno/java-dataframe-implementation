import java.util.ArrayList;

public class DataFrame {
    private ArrayList<Column> cList;

    public ArrayList<Column> getcList() {
        return cList;
    }

    public void addColumn(String name) {
        if(name != null){
            Column column = new Column(name);
            cList.add(column);
        }
    }

    public void addColumn(Column column) {
        cList.add(column);
    }

    public Column getColumn(int i) { //get column by index in cList
        if (i >= 0 && i < cList.size()) {
            return cList.get(i);
        } else {
            return null;
        }
    }

    public Column getColumn(String columnName){ //scan through cList and get column by column name
        for(Column column : cList){
            if(column.getName().equals(columnName)){
                return column;
            }
        }
        return null;
    }

    public ArrayList<String> getColumnNames() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < cList.size(); i++) {
            list.add(cList.get(i).getName());
        }
        return list;
    }

    public int getRowCount(int i) {
        if (i >= 0 && i < cList.size()){
            return cList.get(i).getSize();
        }
        return -1;
    }

    public int getRowCount() {
        return cList.get(0).getSize();
    }

    public String getValue(String columnName, int row){
        if(row >= 0 && row <= getRowCount()-1){
            for(Column column : cList){
                if (column.getName().equals(columnName)){
                    return column.getRowValue(row);
                }
            }
        }
        return null;
    }

    public void putValue(String columnName, int row, String value){
        if(value != null){
            for(Column column : cList){
                if(column.getName().equals(columnName)){
                    column.setRowValue(row, value);
                }
            }
        }
    }

    public void putValue(int columnNumber, int row, String value){
        if ((columnNumber >= 0 && columnNumber < cList.size()) && value != null){
            Column column = cList.get(columnNumber);
            column.setRowValue(row, value);
        }
    }

    public void addValue(String columnName, String value){
        if(value != null){
            for(Column column : cList){
                if(column.getName().equals(columnName)){
                    column.addRowValue(value);
                }
            }
        }
    }

    public DataFrame() {
        cList = new ArrayList<>();
    }
}
