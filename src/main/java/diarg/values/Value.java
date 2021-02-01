package diarg.values;

/**
 * Atomic item that defines a value for value-based argumentation
 */
public class Value {
    public String getId() {
        return id;
    }

    String id;

    public Value (String id) {
        this.id = id;
    }

    public boolean equals(Object object) {
        if(this == object) return true;
        if(!(object instanceof Value)) return false;
        Value value = (Value) object;
        return id.equals(value.getId());
    }

}
