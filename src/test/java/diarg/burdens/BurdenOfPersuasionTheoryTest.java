package diarg.burdens;

import diarg.Semantics;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class BurdenOfPersuasionTheoryTest {
    TestBPFrameworks testBPFrameworks = new TestBPFrameworks();
    BurdenOfPersuasionTheory bpFramework1 = testBPFrameworks.bpFramework1;
    BurdenOfPersuasionTheory bpFramework2 = testBPFrameworks.bpFramework2;
    BurdenOfPersuasionTheory bpFramework3 = testBPFrameworks.bpFramework3;
    Semantics scf2 = new Semantics(SemanticsType.SCF2);
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");

    @Test
    void determineExtensions() {
        Collection<Extension> extensions1 = bpFramework1.determineExtensions(scf2);
        Extension expectedExtension1a = new Extension();
        expectedExtension1a.add(a);
        assertEquals(1, extensions1.size());
        assertTrue(extensions1.contains(expectedExtension1a));
        Collection<Extension> extensions2 = bpFramework2.determineExtensions(scf2);
        assertEquals(2, extensions2.size());
        Extension expectedExtension2a = new Extension();
        expectedExtension2a.add(a);
        expectedExtension2a.add(c);
        assertTrue(extensions2.contains(expectedExtension2a));
        Extension expectedExtension2b = new Extension();
        expectedExtension2b.add(b);
        expectedExtension2b.add(c);
        assertTrue(extensions2.contains(expectedExtension2b));
        Collection<Extension> extensions3 = bpFramework3.determineExtensions(scf2);
        assertEquals(1, extensions3.size());
        Extension expectedExtension3a = new Extension();
        expectedExtension3a.add(a);
        expectedExtension3a.add(d);
        assertTrue(extensions3.contains(expectedExtension3a));
    }

    @Test
    void isSkepticallyAccepted() {
        Extension extension1a = new Extension();
        extension1a.add(a);
        Extension extension1b = new Extension();
        extension1b.add(b);
        assertTrue(bpFramework1.isSkepticallyAccepted(extension1a, scf2));
        assertFalse(bpFramework1.isSkepticallyAccepted(extension1b, scf2));
        assertTrue(bpFramework1.isSkepticallyAccepted(new Extension(), scf2));
        Extension extension2a = new Extension();
        extension2a.add(a);
        extension2a.add(c);
        Extension extension2b = new Extension();
        extension2b.add(c);
        assertFalse(bpFramework2.isSkepticallyAccepted(extension2a, scf2));
        assertTrue(bpFramework2.isSkepticallyAccepted(extension2b, scf2));
        Extension extension3a = new Extension();
        extension3a.add(a);
        Extension extension3b = new Extension();
        extension3b.add(d);
        assertTrue(bpFramework3.isSkepticallyAccepted(extension3a, scf2));
        assertTrue(bpFramework3.isSkepticallyAccepted(extension3b, scf2));
    }

    @Test
    void isCredulouslyAccepted() {
        Extension extension1a = new Extension();
        extension1a.add(a);
        Extension extension1b = new Extension();
        extension1b.add(b);
        assertTrue(bpFramework1.isCredulouslyAccepted(extension1a, scf2));
        assertFalse(bpFramework1.isCredulouslyAccepted(extension1b, scf2));
        assertTrue(bpFramework1.isCredulouslyAccepted(new Extension(), scf2));
        Extension extension2a = new Extension();
        extension2a.add(a);
        extension2a.add(c);
        Extension extension2b = new Extension();
        extension2b.add(c);
        Extension extension2c = new Extension();
        extension2c.add(d);
        assertTrue(bpFramework2.isCredulouslyAccepted(extension2a, scf2));
        assertTrue(bpFramework2.isCredulouslyAccepted(extension2b, scf2));
        assertFalse(bpFramework2.isCredulouslyAccepted(extension2c, scf2));
        Extension extension3a = new Extension();
        extension3a.add(a);
        Extension extension3b = new Extension();
        extension3b.add(d);
        assertTrue(bpFramework3.isCredulouslyAccepted(extension3a, scf2));
        assertTrue(bpFramework3.isCredulouslyAccepted(extension3b, scf2));
    }
}
