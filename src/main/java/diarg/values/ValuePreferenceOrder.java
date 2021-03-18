package diarg.values;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Value preference order for value based argumentation
 */
public class ValuePreferenceOrder {

    public Collection<ValuePreference> getValuePreferences() {
        return valuePreferences;
    }

    Collection<ValuePreference> valuePreferences;

    public ValuePreferenceOrder() {
        this.valuePreferences = new ArrayList<>();
    }

    /**
     * Adds a new value preference to the value preference order
     * @param valuePreference
     */
    public void addValuePreference(ValuePreference valuePreference) {
        Collection<ValuePreference> counterFactualvaluePreferences = new ArrayList<>(valuePreferences);
        counterFactualvaluePreferences.add(valuePreference);
        try {
            if (!checkTransitivity(counterFactualvaluePreferences)) {
                throw new Exception("Value preference order must be transitive.");
            }
            if (!checkAsymmetry(counterFactualvaluePreferences)) {
                throw new Exception("Value preference order must be asymmetric.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        valuePreferences = counterFactualvaluePreferences;
    }

    /**
     * Removes a value preference from the value preference order
     * @param valuePreference
     */
    public void removeValuePreference(ValuePreference valuePreference) {
        Collection<ValuePreference> counterFactualvaluePreferences = new ArrayList<>(valuePreferences);
        counterFactualvaluePreferences.remove(valuePreference);
        try {
            if (!checkTransitivity(counterFactualvaluePreferences)) {
                throw new Exception("Value preference order must be transitive.");
            }
            if (!checkAsymmetry(counterFactualvaluePreferences)) {
                throw new Exception("Value preference order must be asymmetric.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        valuePreferences = counterFactualvaluePreferences;
    }

    /**
     * Checks if a value preference order is transitive
     * @param valuePreferences The to-be-checked value preference order
     * @return the results of the check
     */
    private static boolean checkTransitivity(Collection<ValuePreference> valuePreferences) {
        for(ValuePreference currentValuePreference: valuePreferences) {
            for(ValuePreference otherValuePreference: valuePreferences) {
                Value currentSuperior = currentValuePreference.getSuperiorValue();
                Value currentInferior = currentValuePreference.getInferiorValue();
                Value otherSuperior = otherValuePreference.getSuperiorValue();
                Value otherInferior = otherValuePreference.getInferiorValue();
                if(currentInferior.getId().equals(otherSuperior.getId())) {
                    boolean intermediateTransitivity = false;
                    for(ValuePreference thirdValuePreference: valuePreferences) {
                        Value thirdSuperior = thirdValuePreference.getSuperiorValue();
                        Value thirdInferior= thirdValuePreference.getInferiorValue();
                        if(
                                thirdSuperior.getId().equals(currentSuperior.getId()) &&
                                thirdInferior.getId().equals(otherInferior.getId())
                        ) {
                            intermediateTransitivity = true;
                            break;
                        }
                    }
                    if(!intermediateTransitivity) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if a value preference order is asymmetric
     * @param valuePreferences The to-be-checked value preference order
     * @return the results of the check
     */
    private static boolean checkAsymmetry(Collection<ValuePreference> valuePreferences) {
        for(ValuePreference currentValuePreference: valuePreferences) {
            for(ValuePreference otherValuePreference: valuePreferences) {
                boolean inferiorEqualsSuperior = currentValuePreference.getInferiorValue().getId().equals(
                        otherValuePreference.getSuperiorValue().getId());
                boolean superiorEqualsInferior = currentValuePreference.getSuperiorValue().getId().equals(
                        otherValuePreference.getInferiorValue().getId());
                if(inferiorEqualsSuperior && superiorEqualsInferior) {
                    return false;
                }
            }
        }
        return true;
    }
}
