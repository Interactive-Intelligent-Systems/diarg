package diarg.values;

import diarg.Semantics;
import diarg.TestFrameworks;
import diarg.distances.CombinedDistance;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaperDeltaTest {

    @Test
    void determineAgreementDelta() {
        DungTheory framework0 = new DungTheory();
        DungTheory framework1 = new DungTheory();
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");

        framework0.add(a);
        framework0.add(b);
        framework0.add(c);
        framework0.add(new Attack(a, b));
        framework0.add(new Attack(b, a));
        framework0.add(new Attack(b, c));

        framework1.add(a);
        framework1.add(b);
        framework1.add(c);
        framework1.add(d);
        framework1.add(new Attack(a, b));
        framework1.add(new Attack(b, a));
        framework1.add(new Attack(b, c));
        framework1.add(new Attack(d, a));

        ValueBasedTheory vbFramework0;
        ValueBasedTheory vbFramework1;
        Collection<Value> values0 = new ArrayList<>();
        ArgumentValueMapping argValMapping0 = new ArgumentValueMapping();
        ArrayList<ValuePreferenceOrder>  valPrefOrders0 = new ArrayList<>();
        Collection<Value> values1 = new ArrayList<>();
        ArgumentValueMapping argValMapping1 = new ArgumentValueMapping();
        ArrayList<ValuePreferenceOrder>  valPrefOrders1 = new ArrayList<>();
        Value av = new Value("av");
        Value bv = new Value("bv");
        Value cv = new Value("cv");
        Value dv = new Value("dv");

        values0.add(av);
        values0.add(bv);
        values0.add(cv);

        values1.add(av);
        values1.add(bv);
        values1.add(cv);
        values1.add(dv);

        argValMapping0.setValueAssignment(a, av);
        argValMapping0.setValueAssignment(b, bv);
        argValMapping0.setValueAssignment(c, cv);

        argValMapping1.setValueAssignment(a, av);
        argValMapping1.setValueAssignment(b, bv);
        argValMapping1.setValueAssignment(c, cv);
        argValMapping1.setValueAssignment(d, dv);

        ValuePreferenceOrder valPrefOrder1 = new ValuePreferenceOrder();
        valPrefOrder1.addValuePreference(new ValuePreference(av, bv));
        ValuePreferenceOrder valPrefOrder2 = new ValuePreferenceOrder();
        valPrefOrder2.addValuePreference(new ValuePreference(bv, av));
        ValuePreferenceOrder valPrefOrder3 = new ValuePreferenceOrder();
        valPrefOrder1.addValuePreference(new ValuePreference(av, bv));
        valPrefOrder3.addValuePreference(new ValuePreference(dv, av));

        valPrefOrders0.add(valPrefOrder1);
        valPrefOrders0.add(valPrefOrder2);

        valPrefOrders1.add(valPrefOrder3);
        valPrefOrders1.add(valPrefOrder2);

        vbFramework0 = new ValueBasedTheory(framework0, values0, argValMapping0, valPrefOrders0);
        vbFramework1 = new ValueBasedTheory(framework1, values1, argValMapping1, valPrefOrders1);

        ValueBasedAgreementScenario vbScenario0;
        ValueBasedAgreementScenario vbScenario1;
        CombinedDistance measure = new CombinedDistance();
        Collection<Argument> topic0 = new ArrayList<>();
        Collection<Argument> topic1 = new ArrayList<>();
        topic0.add(a);
        topic0.add(b);
        topic1.add(a);
        topic1.add(b);
        topic1.add(d);
        Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);

        vbScenario0 = new ValueBasedAgreementScenario(vbFramework0, topic0, preferredSemantics, measure);

        double minimalAgreement0 = vbScenario0.determineMinimalAgreement();
        double meanAgreement0 = vbScenario0.determineMeanAgreement();
        double medianAgreement0 = vbScenario0.determineMedianAgreement();
        assertEquals(0.5, minimalAgreement0);
        assertEquals(0.5, meanAgreement0);
        assertEquals(0.5, medianAgreement0);

        vbScenario1 = new ValueBasedAgreementScenario(vbFramework1, topic1, preferredSemantics, measure);

        double minimalAgreement1 = vbScenario1.determineMinimalAgreement();
        double meanAgreement1 = vbScenario1.determineMeanAgreement();
        double medianAgreement1 = vbScenario1.determineMedianAgreement();
        assertEquals(1, minimalAgreement1);
        assertEquals(1, meanAgreement1);
        assertEquals(1, medianAgreement1);

    }
}
