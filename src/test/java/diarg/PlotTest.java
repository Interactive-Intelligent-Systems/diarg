package diarg;

import diarg.distances.CombinedDistance;
import diarg.enums.SemanticsType;
import diarg.values.*;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class PlotTest {
    Random random = new Random();
    ValueBasedTheory vbFramework;
    ValueBasedTheory vbFramework0;
    ValueBasedTheory vbFramework1;
    Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
    ValueBasedAgreementScenario vbScenario0;
    ValueBasedAgreementScenario vbScenario1;
    CombinedDistance measure = new CombinedDistance();

    @Test
    void generatePlotDataDegrees() {
        List<Double> minDegrees = new ArrayList<>();
        List<Double> medDegrees = new ArrayList<>();
        List<Double> meanDegrees = new ArrayList<>();
        boolean topicFixed = false;
        int iterations = 15;
        int sample_per_iteration = 20;
        for (int i = 0; i < iterations; i++) {
            double agrMinDegree = 0d;
            double agrMedianDegree = 0d;
            double agrMeanDegree = 0d;
            for (int j = 0; j < sample_per_iteration; j++) {
                // generate base argumentation framework, 5 to 10 arguments
                Collection<Argument> topic = new ArrayList<>();
                DungTheory aFramework = new DungTheory();
                for(int y = 0; y < random.nextInt(6) + 5; y++) {
                    aFramework.add(new Argument("a" + y));
                }
                System.out.println("Size of AF: " + aFramework.size());
                for(Argument argument: aFramework.getNodes()) {
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                }
                // add values for each argument
                List<Value> values = new ArrayList<>();
                HashMap<String, Value> valueMap = new HashMap<>();
                Set<Value> relevantValues = new HashSet<>();
                ArgumentValueMapping argValMapping = new ArgumentValueMapping();
                ArrayList<ValuePreferenceOrder> valPrefOrders = new ArrayList<>();
                int argIndex = 0;
                for(Argument argument: aFramework.getNodes()) {
                    Value value = new Value(argument.getName());
                    values.add(value);
                    valueMap.put(argument.getName(), value);
                    argValMapping.setValueAssignment(argument, value);
                    // add arguments to topic
                    if(Math.random() < 0.5  && argIndex < 5) { // comment out the "&& argIndex < 5" part to have topic expansion
                        topic.add(argument);
                    }
                    argIndex++;
                }
                // generate 3 agents, each with 5 random value preferences
                for(int k = 0; k < 3; k++) {
                    ValuePreferenceOrder valPrefOrder = new ValuePreferenceOrder();
                    Collection<ValuePreference> cValPrefOrder = new ArrayList<>();
                    for(int l = 0; l < 5 + i; l++) {
                        boolean valueFound = false;
                        Value value0;
                        Value value1;
                        int valueFinderCounter = 0;
                        while(!valueFound) {
                            valueFinderCounter++;
                            if(valueFinderCounter > 5) {
                                System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + " (" + i + "): "  + valueFinderCounter);
                            }
                            Optional<Attack> randomOptionalAttack = aFramework.getAttacks().stream()
                                    .skip((int) (aFramework.getAttacks().size() * Math.random()))
                                    .findFirst();
                            Attack randomAttack;
                            if(randomOptionalAttack.isPresent()) {
                                randomAttack = randomOptionalAttack.get();
                                String attackedName = randomAttack.getNodeB().getName();
                                String attackerName = randomAttack.getNodeA().getName();
                                value0 = valueMap.get(attackedName);
                                value1 = valueMap.get(attackerName);
                                ValuePreference valuePreference = new ValuePreference(value0, value1);
                                Collection<ValuePreference> ccValPrefOrder = new ArrayList<>();
                                ccValPrefOrder.addAll(cValPrefOrder);
                                ccValPrefOrder.add(valuePreference);
                                if(ValuePreferenceOrder.checkTransitivity(ccValPrefOrder)) {
                                    cValPrefOrder.add(valuePreference);
                                    valPrefOrder.addValuePreference(valuePreference);
                                    relevantValues.add(value0);
                                    valueFound = true;
                                }
                            } else {
                                valueFound = true;
                            }
                        }
                    }
                    valPrefOrders.add(valPrefOrder);
                }
                // instantiate agreement scenario
                vbFramework0 = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);
                vbScenario0 = new ValueBasedAgreementScenario(vbFramework0, topic, preferredSemantics, measure);
                // determine the degrees of agreement
                double minimalAgreementDegree = vbScenario0.determineMinimalAgreement();
                double medianAgreementDegree = vbScenario0.determineMedianAgreement();
                double meanAgreementDegree = vbScenario0.determineMeanAgreement();
                // normally expand argumentation by adding i arguments to the framework and generate new values
                Collection<Argument> newArguments = new ArrayList<>();
                Collection<Value> newValues = new ArrayList<>();
                Collection<Attack> newAttacks = new ArrayList<>();
                for(int m = 0; m <= i; m++) {
                    Argument newArgument = new Argument("a"+aFramework.getNodes().size());
                    newArguments.add(newArgument);
                    aFramework.add(newArgument);
                    Value value = new Value(newArgument.getName());
                    values.add(value);
                    newValues.add(value);
                    valueMap.put(newArgument.getName(), value);
                }
                for(Argument argument: newArguments) {
                    // strongly expand
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            Attack newAttack = new Attack(argument, targetArgument);
                            aFramework.add(newAttack);
                            newAttacks.add(newAttack);
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            Attack newAttack = new Attack(argument, targetArgument);
                            aFramework.add(newAttack);
                            newAttacks.add(newAttack);
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            Attack newAttack = new Attack(argument, targetArgument);
                            aFramework.add(newAttack);
                            newAttacks.add(newAttack);
                        }
                    }
                }
                // add new value preferences, by sampling from the i new values and add new value preference relating to
                // a value with 20% probability
                for(int k = 0; k < 3; k++) {
                    boolean valueFound = false;
                    Value value0;
                    Value value1;
                    Collection<ValuePreference> valPrefOrder = new ArrayList<>();
                    valPrefOrder.addAll(valPrefOrders.get(k).getValuePreferences());
                    Collection<ValuePreference> cValPrefOrder = new ArrayList<>();
                    while (!valueFound) {
                        Optional<Attack> randomOptionalAttack = newAttacks.stream()
                                .skip((int) (newAttacks.size() * Math.random()))
                                .findFirst();
                        Attack randomAttack;
                        if (randomOptionalAttack.isPresent()) {
                            randomAttack = randomOptionalAttack.get();
                            // System.out.println(randomAttack.toString());
                            if (newArguments.contains(randomAttack.getNodeA()) || newArguments.contains(randomAttack.getNodeB())) {
                                String attackedName = randomAttack.getNodeB().getName();
                                String attackerName = randomAttack.getNodeA().getName();
                                value0 = valueMap.get(attackedName);
                                value1 = valueMap.get(attackerName);
                                ValuePreference valuePreference = new ValuePreference(value0, value1);
                                Collection<ValuePreference> ccValPrefOrder = new ArrayList<>();
                                ccValPrefOrder.addAll(valPrefOrder);
                                ccValPrefOrder.add(valuePreference);
                                if (ValuePreferenceOrder.checkTransitivity(ccValPrefOrder)) {
                                    // System.out.println(ccValPrefOrder.toString());
                                    cValPrefOrder.add(valuePreference);
                                    valPrefOrder.add(valuePreference);
                                    relevantValues.add(value0);
                                }
                            }
                        }
                        valueFound = true;
                    }
                    ValuePreferenceOrder newValPrefOrder = new ValuePreferenceOrder();
                    for(ValuePreference valPref: valPrefOrder) {
                        newValPrefOrder.addValuePreference(valPref);
                    }
                    valPrefOrders.add(k,newValPrefOrder);
                }
                // If topic is not fixed, expand topic by randomly selecting half of the new arguments
                if(!topicFixed) {
                    for(Argument argument: newArguments) {
                        if(Math.random() > 0.5) {
                            topic.add(argument);
                        }
                    }
                }
                // instantiate new agreement scenario and determine the degrees of agreement
                vbFramework1 = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);
                vbScenario1 = new ValueBasedAgreementScenario(vbFramework0, topic, preferredSemantics, measure);
                // determine delta
                double newMinimalAgreementDegree = vbScenario0.determineMinimalAgreement();
                double newMedianAgreementDegree = vbScenario0.determineMedianAgreement();
                double newMeanAgreementDegree = vbScenario0.determineMeanAgreement();
                // Log (absolute) change in degree of agreement
                agrMinDegree += Math.abs(minimalAgreementDegree - newMinimalAgreementDegree);
                agrMedianDegree += Math.abs(medianAgreementDegree - newMedianAgreementDegree);
                agrMeanDegree += Math.abs(meanAgreementDegree - newMeanAgreementDegree);
            }
            minDegrees.add(agrMinDegree / sample_per_iteration);
            medDegrees.add(agrMedianDegree / sample_per_iteration);
            meanDegrees.add(agrMeanDegree / sample_per_iteration);
        }
        System.out.println("min_degrees = " + minDegrees.toString());
        System.out.println("med_degrees = " + medDegrees.toString());
        System.out.println("mean_degrees = " + meanDegrees.toString());
    }

    @Test
    void generatePlotDataImpact() {
        List<Double> minValueImpact = new ArrayList<>();
        List<Double> meanValueImpact = new ArrayList<>();
        List<Double> medValueImpact = new ArrayList<>();
        int iterations = 15;
        int sample_per_iteration = 20;
        for (int i = 0; i < iterations; i++) {
            Collection<Argument> topic = new ArrayList<>();
            double agrMinImpact = 0d;
            double agrMedianImpact = 0d;
            double agrMeanImpact = 0d;
            for (int j = 0; j < sample_per_iteration; j++) {
                // generate base argumentation framework, 5 to iterations + 5 arguments
                DungTheory aFramework = new DungTheory();
                for(int y = 0; y < i + 5; y++) {
                    aFramework.add(new Argument("a" + y));
                }
                for(Argument argument: aFramework.getNodes()) {
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                    if(Math.random() > 0.5) {
                        Argument targetArgument = aFramework.getNodes().stream()
                                .skip((int) (aFramework.getNodes().size() * Math.random()))
                                .findFirst().get();
                        if(!targetArgument.equals(argument)) {
                            aFramework.add(new Attack(argument, targetArgument));
                        }
                    }
                }
                // add values for each argument
                List<Value> values = new ArrayList<>();
                HashMap<String, Value> valueMap = new HashMap<>();
                Set<Value> relevantValues = new HashSet<>();
                ArgumentValueMapping argValMapping = new ArgumentValueMapping();
                ArrayList<ValuePreferenceOrder> valPrefOrders = new ArrayList<>();
                int argIndex = 0;
                for(Argument argument: aFramework.getNodes()) {
                    Value value = new Value(argument.getName());
                    values.add(value);
                    valueMap.put(argument.getName(), value);
                    argValMapping.setValueAssignment(argument, value);
                    // add arguments to topic
                    if(Math.random() < 0.5 && argIndex < 5) { // comment out the "&& argIndex < 5" part to have topic expansion
                        topic.add(argument);
                    }
                    argIndex++;
                }
                // generate 3 agents, each with 5 + i random value preferences
                for(int k = 0; k < 3; k++) {
                    ValuePreferenceOrder valPrefOrder = new ValuePreferenceOrder();
                    Collection<ValuePreference> cValPrefOrder = new ArrayList<>();
                    for(int l = 0; l < 5 + i; l++) {
                        boolean valueFound = false;
                        Value value0;
                        Value value1;
                        int valueFinderCounter = 0;
                        while(!valueFound) {
                            valueFinderCounter++;
                            if(valueFinderCounter > 5) {
                                System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + " (" + i + "): "  + valueFinderCounter);
                            }
                            Optional<Attack> randomOptionalAttack = aFramework.getAttacks().stream()
                                    .skip((int) (aFramework.getAttacks().size() * Math.random()))
                                    .findFirst();
                            Attack randomAttack;
                            if(randomOptionalAttack.isPresent()) {
                                randomAttack = randomOptionalAttack.get();
                                String attackedName = randomAttack.getNodeB().getName();
                                String attackerName = randomAttack.getNodeA().getName();
                                value0 = valueMap.get(attackedName);
                                value1 = valueMap.get(attackerName);
                                ValuePreference valuePreference = new ValuePreference(value0, value1);
                                Collection<ValuePreference> ccValPrefOrder = new ArrayList<>();
                                ccValPrefOrder.addAll(cValPrefOrder);
                                ccValPrefOrder.add(valuePreference);
                                if(ValuePreferenceOrder.checkTransitivity(ccValPrefOrder)) {
                                    cValPrefOrder.add(valuePreference);
                                    valPrefOrder.addValuePreference(valuePreference);
                                    relevantValues.add(value0);
                                    valueFound = true;
                                }
                            } else {
                                valueFound = true;
                            }
                        }
                    }
                    valPrefOrders.add(valPrefOrder);
                }
                // instantiate agreement scenario
                vbFramework = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);
                vbScenario0 = new ValueBasedAgreementScenario(vbFramework, topic, preferredSemantics, measure);
                // determine the value impact of a random, relevant, value
                Value impactValue = relevantValues.iterator().next();
                agrMinImpact += Math.abs(vbScenario0.determineMinimalAgreementImpact(impactValue));
                agrMedianImpact += Math.abs(vbScenario0.determineMedianAgreementImpact(impactValue));
                agrMeanImpact += Math.abs(vbScenario0.determineMeanAgreementImpact(impactValue));
            }
            minValueImpact.add(agrMinImpact / sample_per_iteration);
            medValueImpact.add(agrMedianImpact / sample_per_iteration);
            meanValueImpact.add(agrMeanImpact / sample_per_iteration);
        }
        System.out.println("min_impacts = " + minValueImpact.toString());
        System.out.println("med_impacts = " + medValueImpact.toString());
        System.out.println("mean_impacts = " + meanValueImpact.toString());
    }
}
