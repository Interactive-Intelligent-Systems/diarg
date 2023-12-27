package diarg.values;

import diarg.AgreementScenario;
import diarg.Semantics;
import diarg.distances.CombinedDistance;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValueBasedAgreementScenarioTest {
    ValueBasedTheory vbFramework;
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Value bv = new Value("bv");
    Collection<Argument> topic = new ArrayList<>();
    Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
    ValueBasedAgreementScenario vbScenario;
    CombinedDistance measure = new CombinedDistance();

    @BeforeAll
    public void init() {
        vbFramework = new TestVBFrameworks().vbFramework1;
        vbScenario = new ValueBasedAgreementScenario(vbFramework, topic, preferredSemantics, measure);
        topic.add(a);
        topic.add(b);
        topic.add(c);
        topic.add(d);
    }

    @Test
    void removeValue() {
        Value av = new Value("av");
        ValueBasedAgreementScenario vbScenarioRM = vbScenario.clone();
        vbScenarioRM.removeValue(av);
        ValueBasedTheory vbFrameworkRM = vbScenarioRM.getVbFramework();

        Collection<Value> valuesRM = vbFrameworkRM.getValues();
        assert(!valuesRM.contains(av));

        ArgumentValueMapping argumentValueMappingRM = vbFrameworkRM.getArgumentValueMapping();
        assert(!argumentValueMappingRM.getMap().containsValue(av));

        ArrayList<ValuePreferenceOrder> valuePreferenceOrdersRM =
                vbFrameworkRM.getValuePreferenceOrders();

        boolean avInValPrefOrdersRM = false;
        for(ValuePreferenceOrder valuePreferenceOrderRM: valuePreferenceOrdersRM) {
            for(ValuePreference valuePreference: valuePreferenceOrderRM.getValuePreferences()) {
                if(valuePreference.getSuperiorValue().getId().equals(av.getId()) || valuePreference.getInferiorValue().getId().equals(av.getId())) {
                    avInValPrefOrdersRM = true;
                    break;
                }
            }
        }
        assert(!avInValPrefOrdersRM);
        assert(vbFramework.getValues().contains(av));
    }

    @Test
    void toAgreementScenario() {
        Semantics semantics1 = new Semantics(SemanticsType.SUBJECTIVE);
        Extension extension1 = new Extension();
        Collection<Extension> extensions1 = new ArrayList<>();
        extension1.add(a);
        extension1.add(d);
        extensions1.add(extension1);
        semantics1.setSubjectiveExtensions(extensions1);
        Semantics semantics2 = new Semantics(SemanticsType.SUBJECTIVE);
        Extension extension2 = new Extension();
        Collection<Extension> extensions2 = new ArrayList<>();
        extension2.add(b);
        extension2.add(d);
        extensions2.add(extension2);
        semantics2.setSubjectiveExtensions(extensions2);
        Semantics semantics3 = new Semantics(SemanticsType.SUBJECTIVE);
        Extension extension3 = new Extension();
        Collection<Extension> extensions3 = new ArrayList<>();
        extension3.add(a);
        extension3.add(c);
        extension3.add(d);
        extensions3.add(extension3);
        semantics3.setSubjectiveExtensions(extensions3);
        ArrayList<Semantics> semanticsList = new ArrayList<>();
        semanticsList.add(semantics1);
        semanticsList.add(semantics2);
        semanticsList.add(semantics3);
        AgreementScenario aScenario = new AgreementScenario(vbFramework.getDungTheory(), topic, semanticsList, measure);
        assertEquals(aScenario.determineMeanAgreement(), vbScenario.determineMeanAgreement());
    }

    @Test
    void determineSatisfaction() {
        double satisfaction12 = vbScenario.determineSatisfaction(0, 1);
        assertEquals(1/2d, satisfaction12);
    }

    @Test
    void determineMinimalAgreement() {
        double minimalAgreement = vbScenario.determineMinimalAgreement();
        assertEquals(1/2d, minimalAgreement);
    }

    @Test
    void determineMeanAgreement() {
        double meanAgreement = vbScenario.determineMeanAgreement();
        assertEquals(3/4d, meanAgreement);
    }

    @Test
    void determineMedianAgreement() {
        double medianAgreement = vbScenario.determineMedianAgreement();
        assertEquals(3/4d, medianAgreement);
    }

    @Test
    void determineSatisfactionImpact() {
        double satisfactionImpact = vbScenario.determineSatisfactionImpact(0, 1, bv);
        assertEquals(1/2d, satisfactionImpact);
    }

   @Test
    void determineMinimalAgreementImpact() {
        double minimalAgreementImpact = vbScenario.determineMinimalAgreementImpact(bv);
        assertEquals(1/4d, minimalAgreementImpact);
    }

    @Test
    void determineMeanAgreementImpact() {
        double meanAgreementImpact = vbScenario.determineMeanAgreementImpact(bv);
        assertEquals(0.16666666666666663d, meanAgreementImpact);
    }

    @Test
    void determineMedianAgreementImpact() {
        double medianAgreementImpact = vbScenario.determineMedianAgreementImpact(bv);
        assertEquals(1/4d, medianAgreementImpact);
    }

}
