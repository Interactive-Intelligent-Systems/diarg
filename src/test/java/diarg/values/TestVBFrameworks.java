package diarg.values;

import diarg.TestFrameworks;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides example value-based argumentation frameworks for test purposes
 * vbFramework1: ({a, b, c, d},{(a, b), (b, a), (c, b), (c, d)}, {av, bv, cv, dv},
 * {a &rarr; av, b &rarr; bv, c &rarr; cv, d &rarr; dv}, ({av &gt; bv}, {bv &gt; av), {cv &gt; dv}))
 */
public final class TestVBFrameworks {

    public ValueBasedTheory vbFramework1;
    DungTheory aFramework = new TestFrameworks().framework10;
    Collection<Value> values = new ArrayList<>();
    ArgumentValueMapping argValMapping = new ArgumentValueMapping();
    ArrayList<ValuePreferenceOrder>  valPrefOrders = new ArrayList<>();
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Value av = new Value("av");
    Value bv = new Value("bv");
    Value cv = new Value("cv");
    Value dv = new Value("dv");

    public TestVBFrameworks() {
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
        vbFramework1 = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);
    }
}