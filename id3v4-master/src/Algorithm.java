import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;

public class Algorithm {
    private final static MathContext MC   = new MathContext(5,RoundingMode.HALF_UP);
    private static final BigDecimal BASE2 = new BigDecimal(2);

    private Algorithm() {
    }

    public static void id3(DTNode node, AttributeCollection set, String target){

        BigDecimal maxGain      = new BigDecimal(0);
        int 	   maxGainIndex = -1;
        // get max gain, exclude the TARGET column.
        for(int i=1; i < set.attributeMapping.size() - 1; i++ ){
            // calculate information gain for the ith Attribute.
            BigDecimal gain  = gain(set, target, set.getAttributeName(i));
            // update gain if larger
            if (maxGain.compareTo(gain) == -1){
                maxGain = gain;
                maxGainIndex = i;
            }
            System.out.println( "Information Gain for: " + i + ":" + set.getAttributeName(i) + " :" + gain );
        }
        System.out.println( "The Highest Information Gain Attribute is: " + maxGainIndex + ":" + set.getAttributeName(maxGainIndex) + " :" + maxGain );

        // create Attribute node
        DTNode attributeNode = new DTNode(set.getAttributeName(maxGainIndex), true, false, false);
        node.branch( attributeNode );

        // A is the highest information gain Attribute, create a branch for each of its values
        Attribute[] attributeColumn = set.getAttributeColumn(set.getAttributeName(maxGainIndex));
        // get the entropy of the values for this attribute
        // get Attribute values
        Set<String> attributeValueNames = new HashSet<>();
        for (Attribute a : attributeColumn) {
//                System.out.println(a.value);
            attributeValueNames.add(a.value);
//                System.out.println("Size: " + attributeValueNames.size());
        }

        // Branch out
        Object[] values = attributeValueNames.toArray();
        for (int i = 0; i < attributeValueNames.size(); i++) {
            AttributeCollection attributeValueSubset = set.getAttributeValueSubset(maxGainIndex, (String)values[i]);
            // get entropy of subset
            BigDecimal entropy = entropy(attributeValueSubset, target);
            System.out.println("Entropy for Value: " + values[i] + " is " + entropy);
            DTNode valueNode = new DTNode((String) values[i], false, true, false);
            attributeNode.branch(valueNode);
            // if homogeneous set as Leaf
            if (entropy.compareTo(BigDecimal.ZERO) == 0){
                // set decision
                valueNode.branch(new DTNode( attributeValueSubset.collection[0][attributeValueSubset.getAttributeIndex(target)].value , false, true, true));
            }else{
                // else rerun id3 on subset
                id3(valueNode, attributeValueSubset.removeAttributeColumn(maxGainIndex), target);
                System.out.println("removed Attribute " + set.getAttributeName(maxGainIndex));
            }
        }

        System.out.println();
    }

//    public static void id3(List<Row> set, int target_attribute_index){
//
//        int attributeCount = set.get(0).getCount();
//        BigDecimal max_gain = new BigDecimal(0);
//        List<DTNode> tree = new ArrayList<>();
//
//        //for each attribute
//        for (int attribute_index = 0; attribute_index < attributeCount; attribute_index++) {
//            // if attribute value is "?" and discrete, assign the most common value label to it.
//
//            // if attribute value is "?" and continuous, assign the mean of the attribute value pairs to it.
//
//            // if continuous, classify. sort values and calculate best split Threshold = (Av1 + Av2) / 2
//            // values are then categorized via the threshold, ie <= , > . (ref: Mitchell)
//
//
//            // else get Gain
//            BigDecimal tmp = gain(set, target_attribute_index,attribute_index,false);
//            max_gain = max_gain.max(tmp);
//
//        }
//
//        System.out.println(max_gain);
//
//
//    }

    public static BigDecimal entropy(AttributeCollection set, String target) {

        HashMap<String, BigDecimal> bin = new HashMap<>();
        for (int i = 0; i < set.collection.length; i++) {
            String value = set.collection[i][set.getAttributeIndex(target)].value;

            if (bin.get(value) == null){
                // create type
                bin.put(value, new BigDecimal(1));
            }else {
                // add to type
                bin.put(value, bin.get(value).add(new BigDecimal(1)));
            }
        }

        Iterator<Map.Entry<String, BigDecimal>> iterator = bin.entrySet().iterator();
        BigDecimal total = new BigDecimal(0);

        List<BigDecimal> tmp = new ArrayList<>();
        List<BigDecimal> probabilities = new ArrayList<>();

        while (iterator.hasNext()){
            Entry<String, BigDecimal> pair = iterator.next();
            BigDecimal p = (BigDecimal)pair.getValue();
            tmp.add(p);
            total = total.add(p);
            iterator.remove();
        }

        for (BigDecimal p : tmp ) {
            BigDecimal d = p.divide(total,MC);
            probabilities.add(d);
        }

        BigDecimal entropy = new BigDecimal(0);

        for (BigDecimal p : probabilities){
            entropy = entropy.add(p.negate().multiply(logBaseX(BASE2,p))).round(MC);
        }

        return entropy;
    }

    public static BigDecimal gain(AttributeCollection set, String setAttribute, String subsetAttribute){
    	// stores a list for each attribute value.
    	HashMap<String, List<Attribute[]>> subset = new HashMap<>();
        
    	Attribute[][] setCollection = set.collection;
    	    	
    	BigDecimal setSize = new BigDecimal(setCollection.length);
//    	int setIndex 	   = set.getAttributeIndex(setAttribute);
//      int subsetIndex    = set.getAttributeIndex(subsetAttribute);
        
        // create a list of rows for each value of Attribute
        Attribute[] attributeColumn = set.getAttributeColumn(subsetAttribute);
        for (int i = 0; i < setCollection.length; i++) {	//n rows
            List<Attribute[]> attributes = subset.get(attributeColumn[i].value);            
            if (attributes == null){
                List<Attribute[]> list = new ArrayList<>();
                list.add(setCollection[i]);
                subset.put(attributeColumn[i].value, list);
            }else{
                attributes.add(setCollection[i]);
            }
        }

        Iterator<Map.Entry<String, List<Attribute[]>>> iterator = subset.entrySet().iterator();
        BigDecimal iGainSum    	   = new BigDecimal(0);
        BigDecimal sInformationSum = new BigDecimal(0);
        
        // calculate the pi*entropy(Sv) for each value under Attribute 
        while (iterator.hasNext()){
            Map.Entry<String, List<Attribute[]>> pair = iterator.next();
            List<Attribute[]> values = pair.getValue();
            
            AttributeCollection subsetCollection = AttributeCollection.listToAttributeCollection(values);
            
            BigDecimal S  = setSize;
            BigDecimal Sv = new BigDecimal( values.size() );            
            BigDecimal pi = Sv.divide(S,MC); // S_v / S
            
            // find entropy of Sv
            BigDecimal subsetEntropy = entropy(subsetCollection, setAttribute);
            // pi*subsetEntropy 
            BigDecimal i = pi.multiply( subsetEntropy ).round(MC).stripTrailingZeros();
            iGainSum = iGainSum.add(i);
            // split information
            BigDecimal si   = pi.negate().multiply((logBaseX(BASE2,pi))).round(MC).stripTrailingZeros();
            sInformationSum = sInformationSum.add(si);
        }

        BigDecimal informationGain, splitEntropy;
        BigDecimal setEntropy = entropy(set, setAttribute);    		// entropy(S)
        
        informationGain = setEntropy.subtract(iGainSum).round(MC);  // Entropy(S) - SUM[ (S_v/S)* Entropy(S_v) ]
        splitEntropy    = sInformationSum;
        
        // if attribute is an attribute with many values, then apply split entropy
        return (set.collection[0][set.getAttributeIndex(subsetAttribute)].useGainRatio) ? informationGain.divide(splitEntropy,MC) : informationGain;        
    }

    // log base 2 (8) = 3
    // 2^3 = 8
    // x = 2 , y = 3, z = 8
    private static BigDecimal logBaseX(BigDecimal x, BigDecimal z){
        final int SCALE = 10;
        return BigFunctions.ln(z,SCALE).divide(BigFunctions.ln(x,SCALE),(MC));
    }

}
