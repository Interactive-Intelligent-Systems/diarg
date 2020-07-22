package diarg;

import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;


import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AFTupleTest {

    TestFrameworks testFrameworks;
    AFTuple tuple1, tuple2, tuple3, tuple4, tuple5, tuple6, tuple7;
    Semantics rcfSemantics = new Semantics(SemanticsType.RCF);

    @BeforeAll
    public void init() {
        testFrameworks = new TestFrameworks();
        tuple1 = new AFTuple(testFrameworks.framework4, testFrameworks.framework3);
        tuple2 = new AFTuple(testFrameworks.framework3, testFrameworks.framework4);
        tuple3 = new AFTuple(testFrameworks.framework3, testFrameworks.framework2);
        tuple4 = new AFTuple(testFrameworks.framework2, testFrameworks.framework3);
        tuple5 = new AFTuple(testFrameworks.framework5, testFrameworks.framework6);
        tuple6 = new AFTuple(testFrameworks.framework4, testFrameworks.framework4);
        tuple7 = new AFTuple(new DungTheory(), testFrameworks.framework4);
    }

    @Test
    void isExpansion() {
        assertTrue(tuple1.isExpansion());
        assertTrue(tuple3.isExpansion());
        assertFalse(tuple2.isExpansion());
        assertFalse(tuple4.isExpansion());
    }

    @Test
    void isNormalExpansion() {
        assertTrue(tuple1.isNormalExpansion());
        assertFalse(tuple3.isNormalExpansion());
        assertFalse(tuple2.isNormalExpansion());
        assertFalse(tuple4.isNormalExpansion());
    }

    @Test
    void isSubmodule() {
        assertFalse(tuple1.isSubmodule());
        assertFalse(tuple3.isSubmodule());
        assertTrue(tuple2.isSubmodule());
        assertTrue(tuple4.isSubmodule());
    }

    @Test
    void isNormalSubmodule() {
        assertFalse(tuple1.isNormalSubmodule());
        assertFalse(tuple3.isNormalSubmodule());
        assertTrue(tuple2.isNormalSubmodule());
        assertFalse(tuple4.isNormalSubmodule());
    }

    @Test
    void determineLargestNormalRISubmodules() {
        Extension choice1 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> lnriSubmodules1 = tuple1.determineLargestNormalRISubmodules(rcfSemantics, choice1);
        assertEquals(2, lnriSubmodules1.size());

        Iterator<DungTheory> iterator1 = lnriSubmodules1.iterator();
        DungTheory lnriSubmoduleShould1a = new DungTheory();
        Argument a = new Argument("a");
        Argument c = new Argument("c");
        lnriSubmoduleShould1a.add(a);
        lnriSubmoduleShould1a.add(c);
        lnriSubmoduleShould1a.add(new Attack(c, a));
        DungTheory lnriSubmoduleIs1a = iterator1.next();
        assertTrue(lnriSubmoduleIs1a.prettyPrint().equals(lnriSubmoduleShould1a.prettyPrint()));

        DungTheory lnriSubmoduleShould1b = new DungTheory();
        Argument b = new Argument("b");
        lnriSubmoduleShould1b.add(a);
        lnriSubmoduleShould1b.add(b);
        lnriSubmoduleShould1b.add(new Attack(a, b));
        DungTheory lnriSubmoduleIs1b = iterator1.next();
        assertTrue(lnriSubmoduleIs1b.getSignature().equals(lnriSubmoduleShould1b.getSignature()));


        Extension choice2 = new Extension();
        Collection<DungTheory> lnriSubmodules2 = tuple5.determineLargestNormalRISubmodules(rcfSemantics, choice2);
        assertEquals(4, lnriSubmodules2.size());


        Iterator<DungTheory> iterator2 = lnriSubmodules2.iterator();
        DungTheory lnriSubmoduleShould2a = new DungTheory();
        Argument d = new Argument("d");
        lnriSubmoduleShould2a.add(b);
        lnriSubmoduleShould2a.add(d);
        DungTheory lnriSubmoduleIs2a = iterator2.next();
        assertTrue(lnriSubmoduleIs2a.getSignature().equals(lnriSubmoduleShould2a.getSignature()));

        DungTheory lnriSubmoduleShould2b = new DungTheory();
        lnriSubmoduleShould2b.add(b);
        lnriSubmoduleShould2b.add(c);
        DungTheory lnriSubmoduleIs2b = iterator2.next();
        assertTrue(lnriSubmoduleIs2b.getSignature().equals(lnriSubmoduleShould2b.getSignature()));

        DungTheory lnriSubmoduleShould2c = new DungTheory();
        lnriSubmoduleShould2c.add(a);
        lnriSubmoduleShould2c.add(d);
        DungTheory lnriSubmoduleIs2c = iterator2.next();
        assertTrue(lnriSubmoduleIs2c.getSignature().equals(lnriSubmoduleShould2c.getSignature()));

        DungTheory lnriSubmoduleShould2d = new DungTheory();
        lnriSubmoduleShould2d.add(a);
        lnriSubmoduleShould2d.add(c);
        DungTheory lnriSubmoduleIs2d = iterator2.next();
        assertTrue(lnriSubmoduleIs2d.getSignature().equals(lnriSubmoduleShould2d.getSignature()));


        Extension choice3 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> lnriSubmodules3 = tuple6.determineLargestNormalRISubmodules(rcfSemantics, choice3);
        assertEquals(1,lnriSubmodules3.size());
        assertTrue(testFrameworks.framework4.prettyPrint().equals(lnriSubmodules3.iterator().next().prettyPrint()));
    }

    @Test
    void determineLargestNormalRMSubmodules() {
        Extension choice1 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> lncmSubmodules1 = tuple1.determineLargestNormalRMSubmodules(rcfSemantics, choice1);
        assertEquals(2, lncmSubmodules1.size());

        Iterator<DungTheory> iterator1 = lncmSubmodules1.iterator();
        DungTheory lncmSubmoduleShould1a = new DungTheory();
        Argument a = new Argument("a");
        Argument c = new Argument("c");
        lncmSubmoduleShould1a.add(a);
        lncmSubmoduleShould1a.add(c);
        lncmSubmoduleShould1a.add(new Attack(c, a));
        DungTheory lncmSubmoduleIs1a = iterator1.next();
        assertTrue(lncmSubmoduleIs1a.prettyPrint().equals(lncmSubmoduleShould1a.prettyPrint()));

        DungTheory lncmSubmoduleShould1b = new DungTheory();
        Argument b = new Argument("b");
        lncmSubmoduleShould1b.add(a);
        lncmSubmoduleShould1b.add(b);
        lncmSubmoduleShould1b.add(new Attack(a, b));
        DungTheory lncmSubmoduleIs1b = iterator1.next();
        assertTrue(lncmSubmoduleIs1b.getSignature().equals(lncmSubmoduleShould1b.getSignature()));


        Extension choice3 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> lncmSubmodules3 = tuple6.determineLargestNormalRMSubmodules(rcfSemantics, choice3);
        assertEquals(1,lncmSubmodules3.size());
        assertTrue(testFrameworks.framework4.prettyPrint().equals(lncmSubmodules3.iterator().next().prettyPrint()));
    }

    @Test
    void determineSmallestNormalRIExpansions() {
        Extension choice1 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> snriExpansions1 = tuple1.determineSmallestNormalRIExpansions(rcfSemantics, choice1);
        assertEquals(2, snriExpansions1.size());

        Iterator<DungTheory> iterator1 = snriExpansions1.iterator();
        DungTheory snriExpansionShould1a = new DungTheory();
        snriExpansionShould1a.add(testFrameworks.framework3);
        DungTheory snriExpansionShould1b = new DungTheory();
        snriExpansionShould1b.add(testFrameworks.framework3);
        DungTheory snriExpansionIs1a = iterator1.next();
        DungTheory snriExpansionIs1b = iterator1.next();
        for(Argument argument: snriExpansionIs1a) {
            if(!snriExpansionShould1a.contains(argument)) {
                snriExpansionShould1a.add(argument);
                snriExpansionShould1b.add(argument);
                snriExpansionShould1a.add(new Attack(argument, new Argument("b")));
                snriExpansionShould1b.add(new Attack(argument, new Argument("c")));
            }
        }
        assertTrue(snriExpansionIs1a.prettyPrint().equals(snriExpansionShould1a.prettyPrint()));
        assertTrue(snriExpansionIs1b.prettyPrint().equals(snriExpansionShould1b.prettyPrint()));

        Collection<DungTheory> snriExpansions2 =
                tuple7.determineSmallestNormalRIExpansions(rcfSemantics, choice1);
        Iterator<DungTheory> iterator2 = snriExpansions2.iterator();
        assertTrue(iterator2.next().prettyPrint().equals(testFrameworks.framework4.prettyPrint()));

    }

    @Test
    void determineSmallestNormalRMExpansions() {
        Extension choice1 = rcfSemantics.getModel(testFrameworks.framework4);
        Collection<DungTheory> sncmExpansions1 = tuple1.determineSmallestNormalRMExpansions(rcfSemantics, choice1);
        assertEquals(3, sncmExpansions1.size());

        Iterator<DungTheory> iterator1 = sncmExpansions1.iterator();
        DungTheory sncmExpansionShould1a = new DungTheory();
        sncmExpansionShould1a.add(testFrameworks.framework3);
        DungTheory sncmExpansionShould1b = new DungTheory();
        sncmExpansionShould1b.add(testFrameworks.framework3);
        DungTheory sncmExpansionShould1c = new DungTheory();
        sncmExpansionShould1c.add(testFrameworks.framework3);
        DungTheory sncmiExpansionIs1a = iterator1.next();
        DungTheory sncmExpansionIs1b = iterator1.next();
        DungTheory sncmExpansionIs1c = iterator1.next();
        for(Argument argument: sncmiExpansionIs1a) {
            if(!sncmExpansionShould1a.contains(argument)) {
                sncmExpansionShould1a.add(argument);
                sncmExpansionShould1b.add(argument);
                sncmExpansionShould1c.add(argument);
                sncmExpansionShould1a.add(new Attack(argument, new Argument("a")));
                sncmExpansionShould1b.add(new Attack(argument, new Argument("b")));
                sncmExpansionShould1c.add(new Attack(argument, new Argument("c")));
            }
        }
        assertTrue(sncmiExpansionIs1a.prettyPrint().equals(sncmExpansionShould1a.prettyPrint()));
        assertTrue(sncmExpansionIs1b.prettyPrint().equals(sncmExpansionShould1b.prettyPrint()));
        assertTrue(sncmExpansionIs1c.prettyPrint().equals(sncmExpansionShould1c.prettyPrint()));
    }
}
