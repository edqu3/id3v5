import java.util.ArrayList;
import java.util.List;

public class Row {

    private List<Attribute> attributes = new ArrayList<>();
    private Row(){}

    public static Row buildRow(Attribute[] attribute_names, String row_string){
        String[] values = row_string.split("\t");
        return new Row().extractAttributes(attribute_names, values);
    }

    private Row extractAttributes(Attribute[] attribute_list, String[] row_content) {
        for (int i = 0; i < row_content.length; i++) {
            Attribute completeAttribute = Attribute.newInstance(attribute_list[i]);
            completeAttribute.value = row_content[i];
            attributes.add(completeAttribute);
        }
        return this;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}