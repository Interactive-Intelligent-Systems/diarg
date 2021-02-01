package diarg.values;

import net.sf.tweety.arg.dung.syntax.Argument;

import java.util.HashMap;

public class ArgumentValueMapping {

    public HashMap<Argument, Value> getMap() {
        return map;
    }

    HashMap<Argument, Value> map = new HashMap();

    public ArgumentValueMapping() { }

    public void setValueAssignment(Argument argument, Value value) {
        map.put(argument, value);
    }

    public void removeValueAssignment(Argument argument) {
        map.remove(argument);
    }


}
