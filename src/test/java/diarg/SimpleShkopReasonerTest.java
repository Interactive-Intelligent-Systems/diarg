package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
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
        assertEquals(2, extensions2.size());
        Extension extension2b = new Extension();
        extension2b.add(b);
        assertTrue(extensions2.contains(extension2b));
        Extension extension2c = new Extension();
        extension2c.add(c);
        assertTrue(extensions2.contains(extension2c));
    }
}
