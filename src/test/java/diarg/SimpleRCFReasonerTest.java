package diarg;

import net.sf.tweety.arg.dung.syntax.Attack;
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
        TestFrameworks testFrameworks = new TestFrameworks();
        DungTheory framework1 = testFrameworks.framework1;
        Collection<Extension> extensions1 = rcfReasoner.getModels(framework1);
        assertEquals(3,extensions1.size());
        Extension extension1a = new Extension();
        extension1a.add(new Argument("a"));
        extension1a.add(new Argument("c"));
        assertTrue(extensions1.contains(extension1a));
        Extension extension1b = new Extension();
        extension1b.add(new Argument("a"));
        extension1b.add(new Argument("d"));
        assertTrue(extensions1.contains(extension1b));
        Extension extension1c = new Extension();
        extension1c.add(new Argument("a"));
        extension1c.add(new Argument("e"));
        assertTrue(extensions1.contains(extension1c));

        DungTheory framework2 = testFrameworks.framework2;
        Collection<Extension> extensions2 = rcfReasoner.getModels(framework2);
        assertEquals(extensions2.size(), 3);
        Extension extension2a = new Extension();
        extension2a.add(new Argument("a"));
        assertTrue(extensions2.contains(extension2a));
        Extension extension2b = new Extension();
        extension2b.add(new Argument("b"));
        assertTrue(extensions2.contains(extension2b));
        Extension extension2c = new Extension();
        extension2c.add(new Argument("c"));
        assertTrue(extensions2.contains(extension2c));

        DungTheory framework3 = testFrameworks.framework7;
        Collection<Extension> extensions3 = rcfReasoner.getModels(framework3);
        assertEquals( 1, extensions3.size());
    }
}
