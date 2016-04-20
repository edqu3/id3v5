public class Main {

    /*
    * INDEX | DISC | TYPE
    * 0  | age            | continuous
    * 1  | work class     | discrete
    * 2  | fnlwgt         | continuous
    * 3  | education      | discrete
    * 4  | education num  | continuous
    * 5  | marital status | discrete
    * 6  | occupation     | discrete
    * 7  | relationship   | discrete
    * 8  | race           | discrete
    * 9  | sex            | discrete
    * 10 | capital gain   | continuous
    * 11 | capital loss   | continuous
    * 12 | hours/week     | continuous
    * 13 | country        | discrete
    * 14 | TARGET         | discrete
    * */
    public static void main(String[] args) {

//        Attribute[] attributeNames = new Attribute[]{
//                new Attribute("AGE", true, true),
//                new Attribute("WORK_CLASS", false, false),
//                new Attribute("FNLWGT", true, false), //use gainratio?
//                new Attribute("EDUCATION", false, false),
//                new Attribute("EDUCATION_NUM", true, false),
//                new Attribute("MARITAL_STATUS", false, false),
//                new Attribute("OCCUPATION", false, false),
//                new Attribute("RELATIONSHIP", false, false),
//                new Attribute("RACE", false, false),
//                new Attribute("SEX", false, false),
//                new Attribute("CAPITAL_GAIN", true, false),
//                new Attribute("CAPITAL_LOSS", true, false),
//                new Attribute("HOURS_PER_WEEK", true, false),
//                new Attribute("COUNTRY", false, false),
//                new Attribute("TARGET", false, false),
//
//        };
        
         Attribute[] attributeNames = new Attribute[]{
                 new Attribute("DAYS", false, true),
                 new Attribute("OUTLOOK", false, false),
                 new Attribute("TEMPERATURE", false, false),
                 new Attribute("HUMIDITY", false, false),
                 new Attribute("WIND", false, false),
                 new Attribute("TARGET", false, false)
         };

        start("simple.tsv", attributeNames);
    }

    private static void start(String source, Attribute[] attributeList) {
        
        //    attr attr attr     
        //row [B]  [1]  [3]
        //row [A]  [2]  [4]
        //row [A]  [4]  [6]
        
        //row = String[i][j->k]
        //col = String[i->k][j]
        Attribute[][] data = TSFReader.getInstance(source,attributeList).getData();

        AttributeCollection c = new AttributeCollection(data);

        kFoldCrossValidation.crossValidate(c, 5);
        
    }

}
