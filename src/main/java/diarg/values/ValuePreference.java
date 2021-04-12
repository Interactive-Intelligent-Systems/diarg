package diarg.values;

/**
 * Preference between two values, for value-based argumentation frameworks
 * @author Timotheus Kampik
 */

public class ValuePreference {

    public Value getSuperiorValue() {
        return superiorValue;
    }

    public Value getInferiorValue() {
        return inferiorValue;
    }

    Value superiorValue;
    Value inferiorValue;

    public ValuePreference(Value superiorValue, Value inferiorValue) {
        try {
            if (superiorValue.getId().equals(inferiorValue.getId())) {
                throw new Exception("Value must not be preferred over itself.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.superiorValue = superiorValue;
        this.inferiorValue = inferiorValue;
    }
}
