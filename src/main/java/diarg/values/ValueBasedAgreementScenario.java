package diarg.values;

import diarg.AgreementScenario;
import diarg.Semantics;
import diarg.distances.DistanceMeasure;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstraction and solver for value-based agreement scenarios
 * @author Timotheus Kampik
 */
public class ValueBasedAgreementScenario {

    @Override
    public ValueBasedAgreementScenario clone() {
        Collection<Argument> clonedTopic = new ArrayList<>(this.topic);
        return new ValueBasedAgreementScenario(this.vbFramework.clone(), clonedTopic, this.semantics, this.distanceMeasure);
    }

    ValueBasedTheory vbFramework;
    Collection<Argument> topic;
    Semantics semantics;
    private DistanceMeasure distanceMeasure;

    public ValueBasedTheory getVbFramework() {
        return vbFramework;
    }

    public Collection<Argument> getTopic() {
        return topic;
    }

    public Semantics getSemantics() {
        return semantics;
    }

    public ValueBasedAgreementScenario(ValueBasedTheory vbFramework, Collection<Argument> topic, Semantics semantics, DistanceMeasure distanceMeasure) {
        this.vbFramework = vbFramework;
        this.topic = topic;
        this.semantics = semantics;
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * Removes a value from the value-based agreement scenario
     * @param value The value that is to be removed
     */
    public void removeValue(Value value) {
        Collection<Value> values = vbFramework.getValues();
        values.remove(value);
        vbFramework.values = values;
        ArgumentValueMapping argumentValueMapping = vbFramework.getArgumentValueMapping();
        Collection<Argument> toBeRemovedKeys = new ArrayList<>();
        for(Argument argument: argumentValueMapping.getMap().keySet()) {
            if(argumentValueMapping.getMap().get(argument).equals(value)) {
                toBeRemovedKeys.add(argument);
            }
        }
        for(Argument argument: toBeRemovedKeys) {
            argumentValueMapping.removeValueAssignment(argument);
        }
        vbFramework.argumentValueMapping = argumentValueMapping;
        ArrayList<ValuePreferenceOrder> valuePreferenceOrders = vbFramework.getValuePreferenceOrders();
        ArrayList<ValuePreferenceOrder> newValuePreferenceOrders = new ArrayList<>();
        for(ValuePreferenceOrder valuePreferenceOrder: valuePreferenceOrders) {
            for(ValuePreference valuePreference: valuePreferenceOrder.getValuePreferences()) {
                if(
                        valuePreference.getInferiorValue().equals(value) ||
                        valuePreference.getSuperiorValue().equals(value)
                ) {
                        valuePreferenceOrder.removeValuePreference(valuePreference);
                }
            }
            newValuePreferenceOrders.add(valuePreferenceOrder);
        }
        vbFramework.valuePreferenceOrders = newValuePreferenceOrders;
    }

    /**
     * Maps the value-based agreement scenario to an abstract agreement scenario.
     * @return The abstract agreement scenario that characterizes the value-based agreement scenario.
     */
    public AgreementScenario toAgreementScenario() {
        ArrayList<DungTheory> subjectiveFrameworks = new ArrayList<>();
        ArgumentValueMapping argumentValueMapping = vbFramework.getArgumentValueMapping();
        for(ValuePreferenceOrder valuePreferenceOrder: vbFramework.getValuePreferenceOrders()) {
            DungTheory subjectiveFramework = new DungTheory();
            subjectiveFramework.addAll(vbFramework.getDungTheory());
            subjectiveFramework.addAllAttacks(vbFramework.getDungTheory().getAttacks());
            for(Attack attack: subjectiveFramework.getAttacks()) {
                Value attackerValue = argumentValueMapping.getMap().get(attack.getAttacker());
                Value attackedValue = argumentValueMapping.getMap().get(attack.getAttacked());
                for(ValuePreference valuePreference: valuePreferenceOrder.getValuePreferences()) {
                    if(
                            valuePreference.inferiorValue.equals(attackerValue) &&
                                    valuePreference.superiorValue.equals(attackedValue)
                    ) {
                        subjectiveFramework.remove(attack);
                    }
                }
            }
            subjectiveFrameworks.add(subjectiveFramework);
        }
        ArrayList<Semantics> subjectiveSemanticsList = new ArrayList<>();
        for(DungTheory subjectiveFramework: subjectiveFrameworks) {
            Semantics subjectiveSemantics = new Semantics(SemanticsType.SUBJECTIVE);
            Collection<Extension> subjectiveExtensions =  semantics.getModels(subjectiveFramework);
            subjectiveSemantics.setSubjectiveExtensions(subjectiveExtensions);
            subjectiveSemanticsList.add(subjectiveSemantics);
        }
        return new AgreementScenario(vbFramework.getDungTheory(), topic, subjectiveSemanticsList, distanceMeasure);
    }

    /**
     * Determines the degree of satisfaction between two "agents" (value preference orders),
     * given the agreement scenario.
     * @param index1 Index of "agent"/value preference order 1 in sequence
     * @param index2 Index of "agent"/value preference order 2 in sequence
     * @return Degree of satisfaction
     */
    public double determineSatisfaction(int index1, int index2) {
        return this.toAgreementScenario().determineSatisfaction(index1, index2);
    }

    /**
     * Determines the degree of minimal agreement of the value-based agreement scenario
     * @return Degree of minimal agreement
     */
    public double determineMinimalAgreement() {
        return this.toAgreementScenario().determineMinimalAgreement();
    }

    /**
     * Determines the degree of mean agreement of the value-based agreement scenario
     * @return Degree of mean agreement
     */
    public double determineMeanAgreement() {
        return this.toAgreementScenario().determineMeanAgreement();
    }

    /**
     * Determines the degree of median agreement of the value-based agreement scenario
     * @return Degree of median agreement
     */
    public double determineMedianAgreement() {
        return this.toAgreementScenario().determineMedianAgreement();
    }

    /**
     * Determines the impact of a value on a 2-agent degree of satisfaction.
     * @param index1 Index of "agent"/value preference order 1 in sequence
     * @param index2 Index of "agent"/value preference order 2 in sequence
     * @param value Value whose impact is to be assessed
     * @return Difference: counterfactual degree of satisfaction (given value is absent) - actual degree of satisfaction
     */
    public double determineSatisfactionImpact(int index1, int index2, Value value) {
        ValueBasedAgreementScenario counterfactualVBAScenario = this.clone();
        counterfactualVBAScenario.removeValue(value);
        return determineSatisfaction(index1, index2) - counterfactualVBAScenario.determineSatisfaction(index1, index2);
    }

    /**
     * Determines the impact of a value on the minimal degree of agreement.
     * @param value Value whose impact is to be assessed
     * @return Difference: counterfactual minimal degree of agreement (given value is absent) -
     * actual minimal degree of agreement
     */
    public double determineMinimalAgreementImpact(Value value) {
        ValueBasedAgreementScenario counterfactualVBAScenario = this.clone();
        counterfactualVBAScenario.removeValue(value);
        return determineMinimalAgreement() - counterfactualVBAScenario.determineMinimalAgreement();
    }

    /**
     * Determines the impact of a value on the mean degree of agreement.
     * @param value Value whose impact is to be assessed
     * @return Difference: counterfactual mean degree of agreement (given value is absent) -
     * actual mean degree of agreement
     */
    public double determineMeanAgreementImpact(Value value) {
        ValueBasedAgreementScenario counterfactualVBAScenario = this.clone();
        counterfactualVBAScenario.removeValue(value);
        return determineMeanAgreement() - counterfactualVBAScenario.determineMeanAgreement();
    }

    /**
     * Determines the impact of a value on the median degree of agreement.
     * @param value Value whose impact is to be assessed
     * @return Difference: counterfactual median degree of agreement (given value is absent) -
     * actual median degree of agreement
     */
    public double determineMedianAgreementImpact(Value value) {
        ValueBasedAgreementScenario counterfactualVBAScenario = this.clone();
        counterfactualVBAScenario.removeValue(value);
        return determineMedianAgreement() - counterfactualVBAScenario.determineMedianAgreement();
    }


}
