import java.util.HashMap;
import java.util.Map;

public class Attribute {

    public String name;
    public String value;
    public boolean isContinuous;
    public boolean useGainRatio;

    public Attribute(String name, boolean isContinuous, boolean useGainRatio){
        this.name = name;
        this.isContinuous = isContinuous;
        this.useGainRatio = useGainRatio;
    }


    public static Attribute newInstance(Attribute old_attribute){
        return new Attribute(old_attribute.name, old_attribute.isContinuous, old_attribute.useGainRatio);
    }
}
