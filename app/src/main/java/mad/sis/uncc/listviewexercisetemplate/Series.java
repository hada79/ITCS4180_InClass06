package mad.sis.uncc.listviewexercisetemplate;

import java.io.Serializable;
import java.util.ArrayList;

public class Series implements Serializable{
    String id;
    String name;
    String description;
    String imgUrl;
    String url;
    ArrayList<SrCharacter> characters;

    @Override
    public String toString() {
        return name;
    }
}
