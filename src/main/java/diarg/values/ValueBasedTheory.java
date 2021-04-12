package diarg.values;

import diarg.Semantics;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstractions and solver for value-based argumentation frameworks
 * @author Timotheus Kampik
 */
public class ValueBasedTheory implements Cloneable {

    @Override
    public ValueBasedTheory clone() {
        DungTheory clonedDungTheory = new DungTheory();
        clonedDungTheory.addAll(this.dungTheory);
        clonedDungTheory.addAllAttacks(this.dungTheory.getAttacks());
        Collection<Value> clonedValues = new ArrayList<>(values);
        ArgumentValueMapping clonedAValueMapping = new ArgumentValueMapping();
        for(Argument argument: this.argumentValueMapping.getMap().keySet()) {
            clonedAValueMapping.setValueAssignment(argument, argumentValueMapping.getMap().get(argument));
        }
        ArrayList<ValuePreferenceOrder> clonedValPrefOrders = new ArrayList<>();
        for(ValuePreferenceOrder valPrefOrder: valuePreferenceOrders) {
            ValuePreferenceOrder clonedValPrefOrder = new ValuePreferenceOrder();
            for(ValuePreference valPref: valPrefOrder.getValuePreferences()) {
                clonedValPrefOrder.addValuePreference(valPref);
            }
            clonedValPrefOrders.add(clonedValPrefOrder);
        }
        return new ValueBasedTheory(
                clonedDungTheory,
                clonedValues,
                clonedAValueMapping,
                clonedValPrefOrders);
    }

    DungTheory dungTheory;
    Collection<Value> values;
    ArgumentValueMapping argumentValueMapping;
    ArrayList<ValuePreferenceOrder> valuePreferenceOrders;

    public void setDungTheory(DungTheory dungTheory) {
        this.dungTheory = dungTheory;
    }

    public void setValues(Collection<Value> values) {
        this.values = values;
    }

    public void setArgumentValueMapping(ArgumentValueMapping argumentValueMapping) {
        this.argumentValueMapping = argumentValueMapping;
    }

    public void setValuePreferenceOrders(ArrayList<ValuePreferenceOrder> valuePreferenceOrders) {
        this.valuePreferenceOrders = valuePreferenceOrders;
    }

    public DungTheory getDungTheory() {
        return dungTheory;
    }

    public Collection<Value> getValues() {
        return values;
    }

    public ArgumentValueMapping getArgumentValueMapping() {
        return argumentValueMapping;
    }

    public ArrayList<ValuePreferenceOrder> getValuePreferenceOrders() {
        return valuePreferenceOrders;
    }

    public ValueBasedTheory(
            DungTheory dungTheory,
            Collection<Value> values,
            ArgumentValueMapping argumentValueMapping,
            ArrayList<ValuePreferenceOrder> valuePreferenceOrders
    ) {
       this.dungTheory = dungTheory;
       this.values = values;
       this.argumentValueMapping = argumentValueMapping;
       this.valuePreferenceOrders = valuePreferenceOrders;
    }

    /**
     * Determines the subjective extension of a given "agent" (value preference order)
     * @param semantics The abstract argumentation semantics that is to be applied
     * @param valuePreferenceIndex The index of the agent's value preference order
     * @return The subjective extensions
     */
    public Collection<Extension> determineSubjectiveExtensions(Semantics semantics, int valuePreferenceIndex) {
        DungTheory subjectiveFramework = new DungTheory();
        subjectiveFramework.addAll(dungTheory);
        subjectiveFramework.addAllAttacks(dungTheory.getAttacks());
        for(Attack attack: subjectiveFramework.getAttacks()) {
            Value attackerValue = argumentValueMapping.getMap().get(attack.getAttacker());
            Value attackedValue = argumentValueMapping.getMap().get(attack.getAttacked());
            ValuePreferenceOrder valuePreferenceOrder = valuePreferenceOrders.get(valuePreferenceIndex);
            for(ValuePreference valuePreference: valuePreferenceOrder.getValuePreferences()) {
                if(
                        valuePreference.inferiorValue.equals(attackerValue) &&
                        valuePreference.superiorValue.equals(attackedValue)
                ) {
                    subjectiveFramework.remove(attack);
                }
            }
        }
        return semantics.getModels(subjectiveFramework);
    }

    /**
     * Determines all subjective extensions (for all agents/value preference orders in the value-based AF)
     * @param semantics The abstract argumentation semantics that is to be applied
     * @return All subjective extensions of all agents/value preference orders
     */
    public ArrayList<Collection<Extension>> determineAllSubjectiveExtensions(Semantics semantics) {
        ArrayList<Collection<Extension>> subjectiveExtensions = new ArrayList<>();
        for(ValuePreferenceOrder valuePreferenceOrder: valuePreferenceOrders) {
            subjectiveExtensions.add(
                    determineSubjectiveExtensions(semantics, valuePreferenceOrders.indexOf(valuePreferenceOrder)));
        }
        return subjectiveExtensions;
    }

    /**
     * Determines the objective extension, i.e. all arguments that are in all extensions of all agents/value preference
     * orders
     * @param semantics The abstract argumentation semantics that is to be applied
     * @return The objective extension
     */
    public Extension determineObjectiveExtension(Semantics semantics) {
        Extension objectiveExtension = new Extension();
        objectiveExtension.addAll(dungTheory.getNodes());
        ArrayList<Collection<Extension>> subjectiveExtensions = determineAllSubjectiveExtensions(semantics);
        for(Argument argument: dungTheory.getNodes()) {
            for(Collection<Extension> extensions: subjectiveExtensions) {
                for(Extension subjectiveExtension: extensions) {
                    if(!subjectiveExtension.contains(argument)) {
                        objectiveExtension.remove(argument);
                        break;
                    }
                }
            }
        }
        return objectiveExtension;
    }


}
