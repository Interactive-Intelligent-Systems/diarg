
package examples;

import diarg.Semantics;
import diarg.AgreementScenario;
import diarg.values.*;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Example code: argumentation-based agreement scenarios
 */
public class AgreementScenarios {
    public static void main(String[] args){
        // initial argumentation framework
        DungTheory framework = new DungTheory();
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        framework.add(a);
        framework.add(b);
        framework.add(c);
        framework.add(d);
        framework.add(e);
        framework.add(new Attack(e, a));
        framework.add(new Attack(d, b));
        framework.add(new Attack(b, d));
        framework.add(new Attack(d, c));
        framework.add(new Attack(c, d));
        framework.add(new Attack(d, d));
        framework.add(new Attack(e, e));

        // agreement scenario
        Collection<Argument> topic = new Extension();
        ArrayList<Semantics> semantics = new ArrayList<>();
        AgreementScenario aScenario;
        topic.add(a);
        topic.add(b);
        topic.add(c);
        Semantics stage = new Semantics(SemanticsType.STAGE);
        Semantics preferred = new Semantics(SemanticsType.PREFERRED);
        Semantics grounded = new Semantics(SemanticsType.GROUNDED);
        semantics.add(stage);
        semantics.add(preferred);
        semantics.add(grounded);
        aScenario = new AgreementScenario(framework, topic, semantics);

        // degrees of satisfaction, AAS
        double satisfaction01 = aScenario.determineSatisfaction(0, 1);
        double satisfaction02 = aScenario.determineSatisfaction(0, 2);
        double satisfaction12 = aScenario.determineSatisfaction(1, 2);
        System.out.println("AAS: Degree of satisfaction, 0, 1: " + satisfaction01);
        System.out.println("AAS: Degree of satisfaction, 0, 2: " + satisfaction02);
        System.out.println("AAS: Degree of satisfaction, 1, 2: " + satisfaction12);

        // degrees of agreement, AAS
        double minimalAgreement = aScenario.determineMinimalAgreement(); // 1/3
        double meanAgreement = aScenario.determineMeanAgreement(); // 2/3
        double medianAgreement = aScenario.determineMedianAgreement();
        System.out.println("AAS: Degree of minimal agreement: " + minimalAgreement);
        System.out.println("AAS: Degree of mean agreement: " + meanAgreement);
        System.out.println("AAS: Degree of median agreement: " + medianAgreement);

        // value-based argumentation framework
        DungTheory aFramework = new DungTheory();
        aFramework.add(a);
        aFramework.add(b);
        aFramework.add(c);
        aFramework.add(d);
        aFramework.add(new Attack(a, b));
        aFramework.add(new Attack(b, a));
        aFramework.add(new Attack(c, b));
        aFramework.add(new Attack(d, c));
        Collection<Value> values = new ArrayList<>();
        ArgumentValueMapping argValMapping = new ArgumentValueMapping();
        ArrayList<ValuePreferenceOrder>  valPrefOrders = new ArrayList<>();
        Value av = new Value("av");
        Value bv = new Value("bv");
        Value cv = new Value("cv");
        Value dv = new Value("dv");
        values.add(av);
        values.add(bv);
        values.add(cv);
        values.add(dv);
        argValMapping.setValueAssignment(a, av);
        argValMapping.setValueAssignment(b, bv);
        argValMapping.setValueAssignment(c, cv);
        argValMapping.setValueAssignment(d, dv);
        ValuePreferenceOrder valPrefOrder1 = new ValuePreferenceOrder();
        valPrefOrder1.addValuePreference(new ValuePreference(av, bv));
        ValuePreferenceOrder valPrefOrder2 = new ValuePreferenceOrder();
        valPrefOrder2.addValuePreference(new ValuePreference(bv, av));
        ValuePreferenceOrder valPrefOrder3 = new ValuePreferenceOrder();
        valPrefOrder3.addValuePreference(new ValuePreference(cv, dv));
        valPrefOrders.add(valPrefOrder1);
        valPrefOrders.add(valPrefOrder2);
        valPrefOrders.add(valPrefOrder3);
        ValueBasedTheory vbFramework = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);

        // Subjective extensions
        Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
        ArrayList<Collection<Extension>> allSubjectiveExtensions = vbFramework.determineAllSubjectiveExtensions(preferredSemantics);
        System.out.println("VAF: Subjective extensions: " + allSubjectiveExtensions.toString());

        // Objective extensions
        Extension objectiveExtension = vbFramework.determineObjectiveExtension(preferredSemantics);
        System.out.println("VAF: Objective extension: " + objectiveExtension.toString());

        // Value-based argumentation-based agreement scenario
        Collection<Argument> vTopic = new ArrayList<>();
        vTopic.add(a);
        vTopic.add(b);
        vTopic.add(c);
        vTopic.add(d);
        ValueBasedAgreementScenario vbScenario = new ValueBasedAgreementScenario(vbFramework, vTopic, preferredSemantics);

        // degrees of satisfaction and agreement, VAAS
        double vSatisfaction01 = vbScenario.determineSatisfaction(0, 1);
        double vMinimalAgreement = vbScenario.determineMinimalAgreement();
        double vMeanAgreement = vbScenario.determineMeanAgreement();
        double vMedianAgreement = vbScenario.determineMedianAgreement();
        System.out.println("VAAS: Degree of satisfaction, 0, 1: " + vSatisfaction01);
        System.out.println("VAAS: Degree of minimal agreement: " + vMinimalAgreement);
        System.out.println("VAAS: Degree of mean agreement: " + vMeanAgreement);
        System.out.println("VAAS: Degree of median agreement: " + vMedianAgreement);

        // impact on degrees of satisfaction and agreement, VAAS
        double satisfactionImpact = vbScenario.determineSatisfactionImpact(0, 1, bv);
        double minimalAgreementImpact = vbScenario.determineMinimalAgreementImpact(bv);
        double meanAgreementImpact = vbScenario.determineMeanAgreementImpact(bv);
        double medianAgreementImpact = vbScenario.determineMedianAgreementImpact(bv);
        System.out.println("VAAS: Impact of value bv on degree of satisfaction, 0, 1: " + satisfactionImpact);
        System.out.println("VAAS: Impact of value bv on degree of minimal agreement: " + minimalAgreementImpact);
        System.out.println("VAAS: Impact of value bv on degree of mean agreement: " + meanAgreementImpact);
        System.out.println("VAAS: Impact of value bv on degree of median agreement: " + medianAgreementImpact);
    }
}