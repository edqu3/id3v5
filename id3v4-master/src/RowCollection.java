import java.util.List;

public class RowCollection {

    private List<Row> rowList;
    private int rowCount;
    private AttributeCollection attributeCollection;

    public RowCollection(List<Row> rows){
        rowList = rows;
        rowCount = rows.size();
//        attributeCollection = new AttributeCollection(this);
    }

    public List<Row> getRowList() {
        return rowList;
    }



}