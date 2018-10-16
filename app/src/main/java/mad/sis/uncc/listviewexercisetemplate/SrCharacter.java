package mad.sis.uncc.listviewexercisetemplate;

import java.io.Serializable;

public class SrCharacter implements Serializable {
    String name;
    String description;
    String imgUrl;

    @Override
    public String toString() {
        return name;
    }
}
