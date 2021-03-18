package diarg;

import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AgreementScenarioTest {
    DungTheory testFramework9 = new TestFrameworks().framework9;
    DungTheory testFramework10 = new DungTheory();
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Argument f = new Argument("f");
    Collection<Argument> topic1 = new Extension();
    Collection<Argument> topic2 = new Extension();
    ArrayList<Semantics> semantics1 = new ArrayList<>();
    ArrayList<Semantics> semantics2 = new ArrayList<>();
    ArrayList<Semantics> semantics3 = new ArrayList<>();
    AgreementScenario aScenario1;
    AgreementScenario aScenario2;
    AgreementScenario aScenario3;
    AgreementScenario aScenario4;
    Semantics stage = new Semantics(SemanticsType.STAGE);
    Semantics preferred = new Semantics(SemanticsType.PREFERRED);
    Semantics grounded = new Semantics(SemanticsType.GROUNDED);
    Semantics cf2 = new Semantics(SemanticsType.CF2);

    @BeforeAll
    public void init() {
        topic1.add(a);
        topic1.add(b);
        topic1.add(c);
        semantics1.add(stage);
        semantics1.add(preferred);
        semantics1.add(grounded);
        aScenario1 = new AgreementScenario(testFramework9, topic1, semantics1);

        topic2.addAll(topic1);
        topic2.add(d);
        semantics2.addAll(semantics1);
        aScenario2 = new AgreementScenario(testFramework9, topic2, semantics2);

        semantics3.addAll(semantics1);
        semantics3.add(cf2);
        aScenario3 = new AgreementScenario(testFramework9, topic2, semantics3);

        testFramework10.addAll(testFramework9);
        testFramework10.addAllAttacks(testFramework9.getAttacks());
        testFramework10.add(f);
        aScenario4 = new AgreementScenario(testFramework10, topic1, semantics1);
    }

    @Test
    void determineSatisfaction() {
        double satisfaction12 = aScenario1.determineSatisfaction(0, 1);
        double satisfaction13 = aScenario1.determineSatisfaction(0, 2);
        double satisfaction23 = aScenario1.determineSatisfaction(1, 2);
        assertEquals(2/3d, satisfaction12);
        assertEquals(0d, satisfaction13);
        assertEquals(1/3d, satisfaction23);
    }

    @Test
    void determineMinimalAgreement() {
        double minimalAgreement = aScenario1.determineMinimalAgreement();
        assertEquals(1/3d, minimalAgreement);
    }

    @Test
    void determineMeanAgreement() {
        double meanAgreement = aScenario1.determineMeanAgreement();
        assertEquals(2/3d, meanAgreement);
    }

    @Test
    void determineMedianAgreement() {
        double medianAgreement = aScenario1.determineMedianAgreement();
        assertEquals(2/3d, medianAgreement);
    }

    @Test
    void isExpansionOf() {
        assert(aScenario2.isExpansionOf(aScenario1));
        assert(!aScenario1.isExpansionOf(aScenario2));
        assert(!aScenario1.isExpansionOf(aScenario3));
        assert(!aScenario2.isExpansionOf(aScenario3));
        assert(aScenario4.isExpansionOf(aScenario1));
        assert(!aScenario1.isExpansionOf(aScenario4));
    }

    @Test
    void isNormalExpansionOf() {
        assert(!aScenario2.isNormalExpansionOf(aScenario1));
        assert(!aScenario1.isNormalExpansionOf(aScenario2));
        assert(!aScenario1.isNormalExpansionOf(aScenario3));
        assert(!aScenario2.isNormalExpansionOf(aScenario3));
        assert(aScenario4.isNormalExpansionOf(aScenario1));
        assert(!aScenario1.isExpansionOf(aScenario4));
    }
}
