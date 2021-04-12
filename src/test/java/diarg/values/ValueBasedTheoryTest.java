package diarg.values;

import diarg.Semantics;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValueBasedTheoryTest {
    ValueBasedTheory vbFramework;
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);

    @BeforeAll
    public void init() {
        vbFramework = new TestVBFrameworks().vbFramework1;
    }

    @Test
    void determineSubjectiveExtensions() {
        Collection<Extension> subjectiveExtensions =
                vbFramework.determineSubjectiveExtensions(preferredSemantics, 0);
        Extension extensionIs = subjectiveExtensions.iterator().next();
        Extension extensionShould = new Extension();
        extensionShould.add(a);
        extensionShould.add(d);
        assertTrue(extensionIs.containsAll(extensionShould));
        assertTrue(extensionShould.containsAll(extensionIs));
    }

    @Test
    void determineAllSubjectiveExtensions() {
        ArrayList<Collection<Extension>> allSubjectiveExtensions =
                vbFramework.determineAllSubjectiveExtensions(preferredSemantics);
        Extension extension1Is = allSubjectiveExtensions.get(0).iterator().next();
        Extension extension1Should = new Extension();
        extension1Should.add(a);
        extension1Should.add(d);
        assertTrue(extension1Is.containsAll(extension1Should));
        assertTrue(extension1Should.containsAll(extension1Is));
        Extension extension2Is = allSubjectiveExtensions.get(1).iterator().next();
        Extension extension2Should = new Extension();
        extension2Should.add(b);
        extension2Should.add(d);
        assertTrue(extension2Is.containsAll(extension2Should));
        assertTrue(extension2Should.containsAll(extension2Is));
        Extension extension3Is = allSubjectiveExtensions.get(2).iterator().next();
        Extension extension3Should = new Extension();
        extension3Should.add(a);
        extension3Should.add(c);
        extension3Should.add(d);
        assertTrue(extension3Is.containsAll(extension3Should));
        assertTrue(extension3Should.containsAll(extension3Is));
    }

    @Test
    void determineObjectiveExtensios() {
        Extension objectiveExtensionShould = new Extension();
        Extension objectiveExtensionIs = vbFramework.determineObjectiveExtension(preferredSemantics);
        objectiveExtensionShould.add(d);
        assertTrue(objectiveExtensionShould.containsAll(objectiveExtensionIs));
        assertTrue(objectiveExtensionIs.containsAll(objectiveExtensionShould));
    }
}
