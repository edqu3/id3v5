import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DTNode {

    boolean isLeaf = false;
    boolean isValue = false;
    boolean isAttribute = false;
    
    String name;
    List<DTNode> branches = new ArrayList<>();

    public DTNode(){
    	isAttribute = true;
    }

    public DTNode(String name, boolean is_attribute, boolean is_value, boolean is_leaf ){
        this.name = name;
        this.isLeaf = is_leaf;
        this.isAttribute = is_attribute;
        this.isValue = is_value;
    }

    public void branch(DTNode child){
        branches.add(child);
    }

}