package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SemanticsType;
import diarg.enums.SequenceType;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AFSequenceTest {
    TestFrameworks testFrameworks;
    AFSequence standardSequence, expandingSequence, eriSequence, rriSequence, ecmSequence, rcmSequence;
    Semantics rcfSemantics;

    @BeforeAll
    public void init() {
        testFrameworks = new TestFrameworks();
        rcfSemantics = new Semantics(SemanticsType.RCF);
    }

    @BeforeEach
    public void empty() {
        standardSequence = new AFSequence(SequenceType.STANDARD, ResolutionType.STANDARD, rcfSemantics);
        expandingSequence = new AFSequence(SequenceType.EXPANDING, ResolutionType.STANDARD, rcfSemantics);
        eriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics);
        rriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics);
        ecmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_CAUTIOUSLY_MONOTONIC,
                rcfSemantics);
        rcmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_CAUTIOUSLY_MONOTONIC,
                rcfSemantics);
    }

    @Test
    void addFramework() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        assertEquals(2, standardSequence.getFrameworks().size());

        expandingSequence.addFramework(testFrameworks.framework3);
        expandingSequence.addFramework(testFrameworks.framework4);
        expandingSequence.addFramework(testFrameworks.framework2);
        assertEquals(2, expandingSequence.getFrameworks().size());

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework2);
        assertEquals(2, eriSequence.getFrameworks().size());
    }

    @Test
    void removeFramework() {
        expandingSequence.addFramework(testFrameworks.framework4);
        expandingSequence.addFramework(testFrameworks.framework2);
        expandingSequence.removeFramework(1);
        assertEquals(1, expandingSequence.getFrameworks().size());
        expandingSequence.removeFramework(0);
        assertEquals(0, expandingSequence.getFrameworks().size());

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        eriSequence.removeFramework(1);
        assertEquals(2, eriSequence.getFrameworks().size());
        eriSequence.removeFramework(0);
        assertEquals(1, eriSequence.getFrameworks().size());
    }

    @Test
    void resolveFramework() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        Extension resolutionIs1a = standardSequence.resolveFramework(1);
        assertTrue(resolutionIs1a == null);
        Extension resolutionIs1b = standardSequence.resolveFramework(0);
        Extension resolutionShould1b = new Extension();
        Argument a = new Argument("a");
        resolutionShould1b.add(a);
        assertEquals(1, resolutionIs1b.size());
        assertTrue(resolutionIs1b.containsAll(resolutionShould1b));

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs2a = eriSequence.resolveFramework(0);
        Extension resolutionShould2a = new Extension();
        resolutionShould2a.add(a);
        assertTrue(resolutionIs2a.containsAll(resolutionShould2a));
        Argument c = new Argument("c");
        Extension resolutionShould2b = new Extension();
        resolutionShould2b.add(c);
        Extension resolutionIs2b = eriSequence.resolveFramework(1);
        assertTrue(resolutionIs2b.containsAll(resolutionShould2b));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs3a = rriSequence.resolveFramework(0);
        Extension resolutionShould3 = new Extension();
        resolutionShould3.add(a);
        assertTrue(resolutionIs3a.containsAll(resolutionShould3));
        Extension resolutionShould3b = new Extension();
        resolutionShould3b.add(c);
        Extension resolutionIs3b = rriSequence.resolveFramework(1);
        System.out.println(resolutionIs3b);
        assertTrue(resolutionIs3b.containsAll(resolutionShould3b));


    }

    @Test
    void resolveFrameworks() {

    }

    @Test
    void determineExtensions() {

    }

    @Test
    void determineResolvableFrameworks() {

    }

}
