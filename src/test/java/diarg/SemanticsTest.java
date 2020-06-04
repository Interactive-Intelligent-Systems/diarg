package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SemanticsTest {
    Semantics rcfSemantics = new Semantics(SemanticsType.RCF);
    TestFrameworks testFrameworks = new TestFrameworks();

    @Test
    void getModels() {
        DungTheory framework1 = testFrameworks.framework1;
        Collection<Extension> extensions1 = rcfSemantics.getModels(framework1);
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

    }

    @Test
    void getModel() {
        Extension extensions = rcfSemantics.getModel(testFrameworks.framework1);
        Extension extensionShould = new Extension();
        extensionShould.add(new Argument("a"));
        extensionShould.add(new Argument("c"));
        assertTrue(extensions.equals(extensionShould));
    }
}
