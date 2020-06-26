package diarg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

public class SimpleRCFReasonerTest {
    SimpleRCFReasoner rcfReasoner = new SimpleRCFReasoner();

    @Test
    void testRCFReasoner() {
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");

        TestFrameworks testFrameworks = new TestFrameworks();
        DungTheory framework1 = testFrameworks.framework1;
        Collection<Extension> extensions1 = rcfReasoner.getModels(framework1);
        assertEquals(3,extensions1.size());
        Extension extension1a = new Extension();
        extension1a.add(a);
        extension1a.add(c);
        assertTrue(extensions1.contains(extension1a));
        Extension extension1b = new Extension();
        extension1b.add(a);
        extension1b.add(d);
        assertTrue(extensions1.contains(extension1b));
        Extension extension1c = new Extension();
        extension1c.add(a);
        extension1c.add(e);
        assertTrue(extensions1.contains(extension1c));

        DungTheory framework2 = testFrameworks.framework2;
        Collection<Extension> extensions2 = rcfReasoner.getModels(framework2);
        assertEquals(extensions2.size(), 3);
        Extension extension2a = new Extension();
        extension2a.add(a);
        assertTrue(extensions2.contains(extension2a));
        Extension extension2b = new Extension();
        extension2b.add(b);
        assertTrue(extensions2.contains(extension2b));
        Extension extension2c = new Extension();
        extension2c.add(c);
        assertTrue(extensions2.contains(extension2c));

        DungTheory framework3 = testFrameworks.framework7;
        Collection<Extension> extensions3 = rcfReasoner.getModels(framework3);
        assertEquals( 1, extensions3.size());
        Extension extension3 = new Extension();
        extension3.add(d);
        extension3.add(b);
        assertTrue(extensions3.contains(extension3));

        DungTheory framework4 = testFrameworks.framework8;
        Collection<Extension> extensions4 = rcfReasoner.getModels(framework4);
        assertEquals(3, extensions4.size());
        Extension extension4a = new Extension();
        extension4a.add(a);
        extension4a.add(c);
        assertTrue(extensions4.contains(extension4a));
        Extension extension4b = new Extension();
        extension4b.add(a);
        extension4b.add(d);
        assertTrue(extensions4.contains(extension4b));
        Extension extension4c = new Extension();
        extension4c.add(b);
        extension4c.add(d);
        assertTrue(extensions4.contains(extension4c));


        /* Test against all example frameworks from
         * Baroni, Pietro, Martin Caminada, and Massimiliano Giacomin.
         * "Abstract argumentation frameworks and their semantics."
         * Handbook of Formal Argumentation 1 (2018): 157-234.
         */
        HandbookTestFrameworks hTestFrameworks = new HandbookTestFrameworks();

        DungTheory hFramework1 = hTestFrameworks.framework1;
        Collection<Extension> extensionsH1 = rcfReasoner.getModels(hFramework1);
        assertEquals( 1, extensionsH1.size());
        Extension extensionsH1Should = new Extension();
        extensionsH1Should.add(a);
        extensionsH1Should.add(c);
        assertTrue(extensionsH1.contains(extensionsH1Should));

        DungTheory hFramework2 = hTestFrameworks.framework2;
        Collection<Extension> extensionsH2 = rcfReasoner.getModels(hFramework2);
        assertEquals( 2, extensionsH2.size());
        Extension extensionsH2Should1 = new Extension();
        extensionsH2Should1.add(a);
        assertTrue(extensionsH2.contains(extensionsH2Should1));
        Extension extensionsH2Should2 = new Extension();
        extensionsH2Should2.add(b);
        assertTrue(extensionsH2.contains(extensionsH2Should2));

        DungTheory hFramework3 = hTestFrameworks.framework3;
        Collection<Extension> extensionsH3 = rcfReasoner.getModels(hFramework3);
        assertEquals( 2, extensionsH3.size());
        Extension extensionsH3Should1 = new Extension();
        extensionsH3Should1.add(a);
        extensionsH3Should1.add(c);
        assertTrue(extensionsH3.contains(extensionsH3Should1));
        Extension extensionsH3Should2 = new Extension();
        extensionsH3Should2.add(a);
        extensionsH3Should2.add(d);
        assertTrue(extensionsH3.contains(extensionsH3Should2));

        DungTheory hFramework4 = hTestFrameworks.framework4;
        Collection<Extension> extensionsH4 = rcfReasoner.getModels(hFramework4);
        assertEquals( 2, extensionsH4.size());
        Extension extensionsH4Should1 = new Extension();
        extensionsH4Should1.add(a);
        extensionsH4Should1.add(d);
        assertTrue(extensionsH4.contains(extensionsH4Should1));
        Extension extensionsH4Should2 = new Extension();
        extensionsH4Should2.add(b);
        extensionsH4Should2.add(d);
        assertTrue(extensionsH4.contains(extensionsH4Should2));

        DungTheory hFramework5 = hTestFrameworks.framework5;
        Collection<Extension> extensionsH5 = rcfReasoner.getModels(hFramework5);
        assertEquals( 3, extensionsH5.size());
        Extension extensionsH5Should1 = new Extension();
        extensionsH5Should1.add(a);
        assertTrue(extensionsH5.contains(extensionsH5Should1));
        Extension extensionsH5Should2 = new Extension();
        extensionsH5Should2.add(b);
        assertTrue(extensionsH5.contains(extensionsH5Should2));
        Extension extensionsH5Should3 = new Extension();
        extensionsH5Should3.add(c);
        assertTrue(extensionsH5.contains(extensionsH5Should3));

        DungTheory hFramework6 = hTestFrameworks.framework6;
        Collection<Extension> extensionsH6 = rcfReasoner.getModels(hFramework6);
        assertEquals( 4, extensionsH6.size());
        Extension extensionsH6Should1 = new Extension();
        extensionsH6Should1.add(a);
        extensionsH6Should1.add(c);
        assertTrue(extensionsH6.contains(extensionsH6Should1));
        Extension extensionsH6Should2 = new Extension();
        extensionsH6Should2.add(a);
        extensionsH6Should2.add(d);
        assertTrue(extensionsH6.contains(extensionsH6Should2));
        Extension extensionsH6Should3 = new Extension();
        extensionsH6Should3.add(a);
        extensionsH6Should3.add(e);
        assertTrue(extensionsH6.contains(extensionsH6Should3));
        Extension extensionsH6Should4 = new Extension();
        extensionsH6Should4.add(b);
        extensionsH6Should4.add(d);
        assertTrue(extensionsH6.contains(extensionsH6Should4));

        DungTheory hFramework7 = hTestFrameworks.framework7;
        Collection<Extension> extensionsH7 = rcfReasoner.getModels(hFramework7);
        assertEquals( 2, extensionsH7.size());
        Extension extensionsH7Should1 = new Extension();
        extensionsH7Should1.add(a);
        assertTrue(extensionsH7.contains(extensionsH7Should1));
        Extension extensionsH7Should2 = new Extension();
        extensionsH7Should2.add(b);
        assertTrue(extensionsH7.contains(extensionsH7Should2));

        DungTheory hFramework8 = hTestFrameworks.framework8;
        Collection<Extension> extensionsH8 = rcfReasoner.getModels(hFramework8);
        assertEquals( 1, extensionsH8.size());
        Extension extensionsH8Should = new Extension();
        extensionsH8Should.add(b);
        assertTrue(extensionsH8.contains(extensionsH8Should));

        DungTheory hFramework9 = hTestFrameworks.framework9;
        Collection<Extension> extensionsH9 = rcfReasoner.getModels(hFramework9);
        assertEquals( 1, extensionsH9.size());
        Extension extensionsH9Should = new Extension();
        extensionsH9Should.add(a);
        assertTrue(extensionsH9.contains(extensionsH9Should));

        DungTheory hFramework10 = hTestFrameworks.framework10;
        Collection<Extension> extensionsH10 = rcfReasoner.getModels(hFramework10);
        assertEquals( 1, extensionsH10.size());
        Extension extensionsH10Should = new Extension();
        extensionsH10Should.add(a);
        extensionsH10Should.add(e);
        extensionsH10Should.add(f);
        assertTrue(extensionsH10.contains(extensionsH10Should));

    }
}
