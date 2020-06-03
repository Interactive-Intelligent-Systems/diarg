package diarg;

import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

public class SimpleRSReasonerTest {

    SimpleRSReasoner rsReasoner = new SimpleRSReasoner();

    @Test
    void testRCFReasoner() {
        TestFrameworks testFrameworks = new TestFrameworks();
        DungTheory framework1 = testFrameworks.framework1;
        Collection<Extension> extensions1 = rsReasoner.getModels(framework1);
        assertEquals(extensions1.size(), 3);
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
        Collection<Extension> extensions2 = rsReasoner.getModels(framework2);
        assertEquals(extensions2.size(), 1);
        Extension extension2a = new Extension();
        extension2a.add(new Argument("b"));
        assertTrue(extensions2.contains(extension2a));
    }
}
