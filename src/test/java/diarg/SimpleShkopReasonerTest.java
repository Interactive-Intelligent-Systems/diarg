package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleShkopReasonerTest {
    SimpleShkopReasoner shkopReasoner = new SimpleShkopReasoner();

    @Test
    void testShkopReasoner() {
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");
        Argument g = new Argument("g");

        DungTheory shkopTestFramework1 = new DungTheory();
        shkopTestFramework1.add(a);
        shkopTestFramework1.add(b);
        shkopTestFramework1.add(c);
        shkopTestFramework1.add(new Attack(a,b));
        shkopTestFramework1.add(new Attack(b,c));
        shkopTestFramework1.add(new Attack(c,b));

        DungTheory shkopTestFramework2 = new DungTheory();
        shkopTestFramework2.add(a);
        shkopTestFramework2.add(b);
        shkopTestFramework2.add(c);
        shkopTestFramework2.add(d);
        shkopTestFramework2.add(e);
        shkopTestFramework2.add(new Attack(a,b));
        shkopTestFramework2.add(new Attack(b,a));
        shkopTestFramework2.add(new Attack(a,c));
        shkopTestFramework2.add(new Attack(b,c));
        shkopTestFramework2.add(new Attack(c,d));
        shkopTestFramework2.add(new Attack(d,e));

        DungTheory shkopTestFramework3 = new DungTheory();
        shkopTestFramework3.add(a);
        shkopTestFramework3.add(b);
        shkopTestFramework3.add(c);
        shkopTestFramework3.add(d);
        shkopTestFramework3.add(e);
        shkopTestFramework3.add(f);
        shkopTestFramework3.add(g);
        shkopTestFramework3.add(new Attack(a,b));
        shkopTestFramework3.add(new Attack(b,a));
        shkopTestFramework3.add(new Attack(a,c));
        shkopTestFramework3.add(new Attack(b,c));
        shkopTestFramework3.add(new Attack(c,d));
        shkopTestFramework3.add(new Attack(d,e));
        shkopTestFramework3.add(new Attack(e,f));
        shkopTestFramework3.add(new Attack(f,g));
        shkopTestFramework3.add(new Attack(g,e));

        TestFrameworks testFrameworks = new TestFrameworks();

        DungTheory framework1 = testFrameworks.framework1;
        Collection<Extension> extensions1 = shkopReasoner.getModels(framework1);
        assertEquals(3, extensions1.size());
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
        Collection<Extension> extensions2 = shkopReasoner.getModels(framework2);
        assertEquals(3, extensions2.size());
        Extension extension2a = new Extension();
        extension2a.add(a);
        assertTrue(extensions2.contains(extension2a));
        Extension extension2b = new Extension();
        extension2b.add(b);
        assertTrue(extensions2.contains(extension2b));
        Extension extension2c = new Extension();
        extension2c.add(c);
        assertTrue(extensions2.contains(extension2c));

        // Test examples form Argumentation Handbook
        HandbookTestFrameworks hbtFrameworks = new HandbookTestFrameworks();
        DungTheory hbtFramework1 = hbtFrameworks.framework1;
        Collection<Extension> extensionsHBT1 = shkopReasoner.getModels(hbtFramework1);
        assertEquals(1, extensionsHBT1.size());
        Extension extensionHBT1a = new Extension();
        extensionHBT1a.add(a);
        extensionHBT1a.add(c);
        assertTrue(extensionsHBT1.contains(extensionHBT1a));

        DungTheory hbtFramework2 = hbtFrameworks.framework2;
        Collection<Extension> extensionsHBT2 = shkopReasoner.getModels(hbtFramework2);
        assertEquals(2, extensionsHBT2.size());
        Extension extensionHBT2a = new Extension();
        extensionHBT2a.add(a);
        assertTrue(extensionsHBT2.contains(extensionHBT2a));
        Extension extensionHBT2b = new Extension();
        extensionHBT2b.add(b);
        assertTrue(extensionsHBT2.contains(extensionHBT2b));

        DungTheory hbtFramework3 = hbtFrameworks.framework3;
        Collection<Extension> extensionsHBT3 = shkopReasoner.getModels(hbtFramework3);
        assertEquals(2, extensionsHBT3.size());
        Extension extensionHBT3a = new Extension();
        extensionHBT3a.add(a);
        extensionHBT3a.add(c);
        assertTrue(extensionsHBT3.contains(extensionHBT3a));
        Extension extensionHBT3b = new Extension();
        extensionHBT3b.add(a);
        extensionHBT3b.add(d);
        assertTrue(extensionsHBT3.contains(extensionHBT3b));

        DungTheory hbtFramework4 = hbtFrameworks.framework4;
        Collection<Extension> extensionsHBT4 = shkopReasoner.getModels(hbtFramework4);
        assertEquals(2, extensionsHBT3.size());
        Extension extensionHBT4a = new Extension();
        extensionHBT4a.add(a);
        extensionHBT4a.add(d);
        assertTrue(extensionsHBT4.contains(extensionHBT4a));
        Extension extensionHBT4b = new Extension();
        extensionHBT4b.add(b);
        extensionHBT4b.add(d);
        assertTrue(extensionsHBT4.contains(extensionHBT4b));

        DungTheory hbtFramework5 = hbtFrameworks.framework5;
        Collection<Extension> extensionsHBT5 = shkopReasoner.getModels(hbtFramework5);
        assertEquals(3, extensionsHBT5.size());
        Extension extensionHBT5a = new Extension();
        extensionHBT5a.add(a);
        assertTrue(extensionsHBT5.contains(extensionHBT5a));
        Extension extensionHBT5b = new Extension();
        extensionHBT5b.add(b);
        assertTrue(extensionsHBT5.contains(extensionHBT5b));
        Extension extensionHBT5c = new Extension();
        extensionHBT5c.add(c);
        assertTrue(extensionsHBT5.contains(extensionHBT5c));

        DungTheory hbtFramework6 = hbtFrameworks.framework6;
        Collection<Extension> extensionsHBT6 = shkopReasoner.getModels(hbtFramework6);
        assertEquals(4, extensionsHBT6.size());
        Extension extensionHBT6a = new Extension();
        extensionHBT6a.add(a);
        extensionHBT6a.add(c);
        assertTrue(extensionsHBT6.contains(extensionHBT6a));
        Extension extensionHBT6b = new Extension();
        extensionHBT6b.add(a);
        extensionHBT6b.add(d);
        assertTrue(extensionsHBT6.contains(extensionHBT6b));
        Extension extensionHBT6c = new Extension();
        extensionHBT6c.add(a);
        extensionHBT6c.add(e);
        assertTrue(extensionsHBT6.contains(extensionHBT6c));
        Extension extensionHBT6d = new Extension();
        extensionHBT6d.add(b);
        extensionHBT6d.add(d);
        assertTrue(extensionsHBT6.contains(extensionHBT6d));

        DungTheory hbtFramework7 = hbtFrameworks.framework7;
        Collection<Extension> extensionsHBT7 = shkopReasoner.getModels(hbtFramework7);
        assertEquals(2, extensionsHBT7.size());
        Extension extensionHBT7a = new Extension();
        extensionHBT7a.add(a);
        assertTrue(extensionsHBT7.contains(extensionHBT7a));
        Extension extensionHBT7b = new Extension();
        extensionHBT7b.add(b);
        assertTrue(extensionsHBT7.contains(extensionHBT7b));

        DungTheory hbtFramework8 = hbtFrameworks.framework8;
        Collection<Extension> extensionsHBT8 = shkopReasoner.getModels(hbtFramework8);
        assertEquals(1, extensionsHBT8.size());
        Extension extensionHBT8a = new Extension();
        extensionHBT8a.add(b);
        assertTrue(extensionsHBT8.contains(extensionHBT8a));

        DungTheory hbtFramework9 = hbtFrameworks.framework9;
        Collection<Extension> extensionsHBT9 = shkopReasoner.getModels(hbtFramework9);
        assertEquals(1, extensionsHBT9.size());
        Extension extensionHBT9a = new Extension();
        extensionHBT9a.add(a);
        assertTrue(extensionsHBT9.contains(extensionHBT9a));

        DungTheory hbtFramework10 = hbtFrameworks.framework10;
        Collection<Extension> extensionsHBT10 = shkopReasoner.getModels(hbtFramework10);
        assertEquals(1, extensionsHBT10.size());
        Extension extensionHBT10a = new Extension();
        extensionHBT10a.add(a);
        extensionHBT10a.add(e);
        extensionHBT10a.add(f);
        assertTrue(extensionsHBT10.contains(extensionHBT10a));

        Collection<Extension> extensionsShkop1 = shkopReasoner.getModels(shkopTestFramework1);
        assertEquals(1, extensionsShkop1.size());
        Extension extensionShkop1 = new Extension();
        extensionShkop1.add(a);
        extensionShkop1.add(c);
        assertTrue(extensionsShkop1.contains(extensionShkop1));

        Collection<Extension> extensionsShkop2 = shkopReasoner.getModels(shkopTestFramework2);
        assertEquals(2, extensionsShkop2.size());
        Extension extensionShkop2a = new Extension();
        extensionShkop2a.add(a);
        extensionShkop2a.add(d);
        assertTrue(extensionsShkop2.contains(extensionShkop2a));
        Extension extensionShkop2b = new Extension();
        extensionShkop2b.add(b);
        extensionShkop2b.add(d);
        assertTrue(extensionsShkop2.contains(extensionShkop2b));

        Collection<Extension> extensionsShkop3 = shkopReasoner.getModels(shkopTestFramework3);
        assertEquals(2, extensionsShkop3.size());
        Extension extensionShkop3a = new Extension();
        extensionShkop3a.add(a);
        extensionShkop3a.add(d);
        extensionShkop3a.add(f);
        assertTrue(extensionsShkop3.contains(extensionShkop3a));
        Extension extensionShkop3b = new Extension();
        extensionShkop3b.add(b);
        extensionShkop3b.add(d);
        extensionShkop3b.add(f);
        assertTrue(extensionsShkop3.contains(extensionShkop3b));
    }
}
