import java.util.*;

public class AttributeCollection {

    public Attribute[][] collection;
    private int rows, columns;

    public Map<String, List<String>> attributeValueMapping = new HashMap<>();
    public Map<String, Integer> attributeMapping = new HashMap<>();
    public Map<Integer, String> attributeIndexMapping = new HashMap<>();

    public AttributeCollection(Attribute[][] attribute_array){
        collection = attribute_array;

        this.rows = attribute_array.length;
        this.columns = attribute_array[0].length;

        for (int i = 0; i < attribute_array.length; i++) {
            for (int j = 0; j < attribute_array[i].length ; j++) {

                // pair attribute and its index

                // for java 7
                if (attributeMapping.get(attribute_array[i][j].name) == null){
                    attributeMapping.put(attribute_array[i][j].name, j);
                }
                // for java 8
//                attributeMapping.putIfAbsent(attributes[i][j].name, j);

                //pair attribute and its values
                List<String> values = attributeValueMapping.get(attribute_array[i][j].name);
                // duplicate values allowed, can be used to count # of instances.
                if (values == null){
                    values = new ArrayList<>();
                    values.add(attribute_array[i][j].value);
                    attributeValueMapping.put(attribute_array[i][j].name, values);
                }else{
                    values.add(attribute_array[i][j].value);
                }

            }
        }
        for(Map.Entry<String, Integer> entry : attributeMapping.entrySet()){
            attributeIndexMapping.put(entry.getValue(), entry.getKey());
        }
    }

    public AttributeCollection removeAttributeColumn(int attributeIndex){

        List<Attribute[]> attributes = new ArrayList<>();

        for (int i = 0; i < this.collection.length; i++) {
            List<Attribute> row = new ArrayList<>();
            for (int j = 0; j < this.collection[i].length; j++) {
                if (j != attributeIndex){
                    row.add(this.collection[i][j]);
                }
            }
            Object[] objects = row.toArray();
            Attribute[] attr = new Attribute[objects.length];
            for (int j = 0; j < objects.length; j++) {
                attr[j] = (Attribute)objects[j];
            }
            attributes.add(attr);
        }

        return listToAttributeCollection(attributes);
    }

    public AttributeCollection getAttributeValueSubset(int attributeIndex, String value){

        List<Attribute[]> list = new ArrayList<>(collection.length);

        for (int i = 0; i < collection.length; i++) {
            if (collection[i][attributeIndex].value.equals(value)){
                list.add(collection[i]);
            }
        }
        return listToAttributeCollection(list);
    }

    public static AttributeCollection getSubset(List<Attribute[]> new_list){
    	
    	Attribute[][] ac = new Attribute[new_list.size()][new_list.get(0).length];
    	
    	
    	
    	return null;
	}

    public static AttributeCollection listToAttributeCollection(List<Attribute[]> attributes){
        Attribute[][] ac = new Attribute[attributes.size()][attributes.get(0).length];

        for (int i = 0; i < attributes.size(); i++) {
            for (int j = 0; j < attributes.get(i).length; j++) {
                ac[i][j] = attributes.get(i)[j];
            }
        }

        return new AttributeCollection(ac);
    }

    public Attribute[] getAttributeColumn(String attribute){
        Attribute[] a = new Attribute[collection.length];
        Integer index = attributeMapping.get(attribute);
        for (int i = 0; i < a.length; i++) {
            a[i] = collection[i][index];
        }

        return a;
    }

    /**
     * get all columns for attributeName Attribute.
     * */
    public Attribute[][] getSubset(String attributeName){
        return null;
    }

    /**
     * get all columns that match attributeValue under  the attributeName Attribute
     * */
    public Attribute[][] getSubset(String attributeName, String attributeValue){

        return null;

    }


    public int getAttributeIndex(String target) {
        return attributeMapping.get(target);
    }

    public String getAttributeName(int index) {
        return attributeIndexMapping.get(index);
    }
}
