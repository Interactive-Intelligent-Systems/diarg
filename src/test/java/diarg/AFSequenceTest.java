package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SemanticsType;
import diarg.enums.SequenceType;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AFSequenceTest {
    TestFrameworks testFrameworks;
    AFSequence standardSequence, expandingSequence, eriSequence, rriSequence, ecmSequence, rcmSequence;
    Argument a, b, c;
    Semantics rcfSemantics;

    @BeforeAll
    public void init() {
        testFrameworks = new TestFrameworks();
        rcfSemantics = new Semantics(SemanticsType.RCF);
        a = new Argument("a");
        b = new Argument("b");
        c = new Argument("c");
    }

    @BeforeEach
    public void empty() {
        standardSequence = new AFSequence(SequenceType.STANDARD, ResolutionType.STANDARD, rcfSemantics, true);
        expandingSequence = new AFSequence(SequenceType.EXPANDING, ResolutionType.STANDARD, rcfSemantics, true);
        eriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics, true);
        rriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics, true);
        ecmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_CAUTIOUSLY_MONOTONIC,
                rcfSemantics, true);
        rcmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_CAUTIOUSLY_MONOTONIC,
                rcfSemantics, true);
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
        resolutionShould1b.add(a);
        assertEquals(1, resolutionIs1b.size());
        assertTrue(resolutionIs1b.containsAll(resolutionShould1b));

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs2a = eriSequence.resolveFramework(0);
        Extension resolutionShould2a = new Extension();
        resolutionShould2a.add(a);
        assertTrue(resolutionIs2a.containsAll(resolutionShould2a));
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
        assertTrue(resolutionIs3b.containsAll(resolutionShould3b));

        ecmSequence.addFramework(testFrameworks.framework4);
        ecmSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs4a = ecmSequence.resolveFramework(0);
        Extension resolutionShould4a = new Extension();
        resolutionShould4a.add(a);
        assertTrue(resolutionIs4a.containsAll(resolutionShould4a));
        Extension resolutionShould4b = new Extension();
        Extension resolutionIs4b = ecmSequence.resolveFramework(1);
        assertTrue(resolutionIs4b.containsAll(resolutionShould4b));

        rcmSequence.addFramework(testFrameworks.framework4);
        rcmSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs5a = rcmSequence.resolveFramework(0);
        Extension resolutionShould5a = new Extension();
        resolutionIs5a.add(a);
        assertTrue(resolutionIs5a.containsAll(resolutionShould5a));
        Extension resolutionShould5b = new Extension();
        Extension resolutionIs5b = rcmSequence.resolveFramework(1);
        assertTrue(resolutionIs5b.containsAll(resolutionShould5b));


    }

    @Test
    void resolveFrameworks() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        Extension resolutionShoulda = new Extension();
        resolutionShoulda.add(a);
        Collection<Extension> resolutionsShould1 = new LinkedList<>();
        resolutionsShould1.add(resolutionShoulda);
        resolutionsShould1.add(resolutionShoulda);
        Collection<Extension> resolutionsIs1 = standardSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs1.size());
        assertTrue(resolutionsIs1.containsAll(resolutionsShould1));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs2 = rriSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs2.size());
        assertTrue(resolutionsIs2.containsAll(resolutionsShould1));

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs3 = eriSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs3.size());
        assertTrue(resolutionsIs3.containsAll(resolutionsShould1));

        rcmSequence.addFramework(testFrameworks.framework4);
        rcmSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs4 = rcmSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs4.size());
        assertTrue(resolutionsIs4.containsAll(resolutionsShould1));

        ecmSequence.addFramework(testFrameworks.framework4);
        ecmSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs5 = ecmSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs5.size());
        assertTrue(resolutionsIs5.containsAll(resolutionsShould1));
    }

    @Test
    void determineExtensions() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        standardSequence.resolveFramework(0);
        Extension resolutionShould1a = new Extension();
        resolutionShould1a.add(a);
        Extension resolutionShould1b = new Extension();
        resolutionShould1b.add(b);
        Extension resolutionShould1c = new Extension();
        resolutionShould1c.add(c);
        Collection<Extension> extensionsShould1a = new LinkedList<>();
        extensionsShould1a.add(resolutionShould1a);
        extensionsShould1a.add(resolutionShould1b);
        extensionsShould1a.add(resolutionShould1c);
        Collection<Extension> extensionsIs1a = standardSequence.determineExtensions(0);
        assertEquals(3, extensionsIs1a.size());
        assertTrue(extensionsIs1a.containsAll(extensionsShould1a));
        Collection<Extension> extensionsShould1b = new LinkedList<>();
        extensionsShould1b.add(resolutionShould1a);
        Collection<Extension> extensionsIs1b = standardSequence.determineExtensions(1);
        assertEquals(1, extensionsIs1b.size());
        assertTrue(extensionsIs1b.containsAll(extensionsShould1b));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        assertTrue(rriSequence.resolveFramework(1) == null);
    }

    @Test
    void determineResolvableFrameworks() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        assertTrue(standardSequence.determineResolvableFrameworks(0) == null);

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Collection<DungTheory> resolvableFrameworks1 = rriSequence.determineResolvableFrameworks(1);
        assertTrue(resolvableFrameworks1 == null);

        Collection<DungTheory> resolvableFrameworks2 = rriSequence.determineResolvableFrameworks(0);
        Collection<DungTheory> resolvableFrameworksShould2 = new LinkedList<>();
        resolvableFrameworksShould2.add(testFrameworks.framework4);
        assertEquals(1, resolvableFrameworks2.size());
        assertTrue(resolvableFrameworks2.containsAll(resolvableFrameworksShould2));

        rriSequence.resolveFramework(0);
        Collection<DungTheory> resolvableFrameworks3 = rriSequence.determineResolvableFrameworks(1);
        DungTheory resolvableFrameworkShould3b = new DungTheory();
        resolvableFrameworkShould3b.add(a);
        resolvableFrameworkShould3b.add(c);
        resolvableFrameworkShould3b.add(new Attack(c, a));
        assertEquals(2, resolvableFrameworks3.size());
        Iterator<DungTheory> iterator1 = resolvableFrameworks3.iterator();
        DungTheory resolvableFrameworkIs3a = iterator1.next();
        DungTheory resolvableFrameworkIs3b = iterator1.next();
        assertTrue(resolvableFrameworkIs3a.prettyPrint().equals(resolvableFrameworkShould3b.prettyPrint()));
        assertTrue(resolvableFrameworkIs3b.prettyPrint().equals(testFrameworks.framework4.prettyPrint()));

        Extension resolutionShould3a = rcfSemantics.getModel(resolvableFrameworkIs3a);
        Extension resolutionIs3a = rriSequence.resolveFramework(1, resolutionShould3a);
        Extension resolutionShould3b = rcfSemantics.getModel(resolvableFrameworkIs3b);
        Extension resolutionIs3b = rriSequence.resolveFramework(1, resolutionShould3b);
        assertEquals(resolutionShould3a.size(), resolutionIs3a.size());
        assertTrue(resolutionIs3a.containsAll(resolutionShould3a));
        assertEquals(resolutionShould3b.size(), resolutionIs3b.size());
        assertTrue(resolutionIs3b.containsAll(resolutionShould3b));
    }

}
