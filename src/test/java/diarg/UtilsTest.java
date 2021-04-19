package diarg;

import diarg.burdens.BurdenOfPersuasionTheory;
import diarg.enums.SemanticsType;
import diarg.explanations.ExplanationTestFrameworks;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Argument e = new Argument("e");
    Argument f = new Argument("f");

    @Test
    public void determineSetSimilarity() {
        Collection<Argument> baseSet = new Extension();
        baseSet.add(a);
        baseSet.add(b);
        baseSet.add(c);
        Collection<Argument> set1 = new Extension();
        set1.add(d);
        Collection<Argument> set2 = new Extension();
        set2.add(b);
        set2.add(c);
        set2.add(d);
        Collection<Argument> set3 = new Extension();
        set3.addAll(baseSet);
        set3.add(d);
        double distance12Is = Utils.determineSetSimilarity(set1, set2, baseSet);
        double distance13Is = Utils.determineSetSimilarity(set1, set3, baseSet);
        double distance23Is = Utils.determineSetSimilarity(set2, set3, baseSet);
        assertEquals(1/3d, distance12Is);
        assertEquals(0, distance13Is);
        assertEquals(2/3d, distance23Is);
    }

    @Test
    void determineSubsetMaxMonotonicExtensions() {
        Extension extension1 = new Extension();
        Extension extension2 = new Extension();
        Extension extension3 = new Extension();
        Extension extension4 = new Extension();
        extension2.add(a);
        extension2.add(b);
        extension3.add(a);
        extension4.add(c);
        Collection<Extension> extensions1 = new ArrayList<>();
        Collection<Extension> extensions2 = new ArrayList<>();
        extensions1.add(extension1);
        extensions1.add(extension2);
        extensions2.add(extension3);
        extensions2.add(extension4);
        Collection<Extension> maxMonExtensions = Utils.determineSubsetMaxMonotonicExtensions(extensions1, extensions2);
        assertEquals(2, maxMonExtensions.size());
        assertTrue(maxMonExtensions.contains(extension1));
        assertTrue(maxMonExtensions.contains(extension2));
        Collection<Extension> poMaxMonExtensions = Utils.determinePOMaxMonotonicExtensions(extensions1, extensions2);
        assertEquals(1, poMaxMonExtensions.size());
        assertTrue(poMaxMonExtensions.contains(extension2));

    }

    @Test
    void determineCardinalityMaxMonotonicExtensions() {
        Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
        ExplanationTestFrameworks testFrameworks = new ExplanationTestFrameworks();
        DungTheory framework2 = testFrameworks.framework2;
        DungTheory framework4 = testFrameworks.framework4;
        DungTheory framework5 = testFrameworks.framework5;

        Extension extension1 = new Extension();
        extension1.add(a);
        extension1.add(b);
        extension1.add(c);
        Extension expectedCardinalityMaxExtension1a = new Extension();
        expectedCardinalityMaxExtension1a.add(b);
        expectedCardinalityMaxExtension1a.add(c);
        expectedCardinalityMaxExtension1a.add(d);
        expectedCardinalityMaxExtension1a.add(f);
        Collection<Extension> cardinalityMaxExtensions1 =
                Utils.determineCardinalityMaxMonotonicExtensions(preferredSemantics.getModels(framework2), extension1);
        assertEquals(1, cardinalityMaxExtensions1.size());
        assertTrue(cardinalityMaxExtensions1.contains(expectedCardinalityMaxExtension1a));

        Extension extension2 = new Extension();
        extension2.add(b);
        extension2.add(c);
        Extension expectedCardinalityMaxExtension2a = new Extension();
        expectedCardinalityMaxExtension2a.add(b);
        expectedCardinalityMaxExtension2a.add(d);
        Collection<Extension> cardinalityMaxExtensions2 =
                Utils.determineCardinalityMaxMonotonicExtensions(preferredSemantics.getModels(framework4), extension2);
        assertEquals(1, cardinalityMaxExtensions2.size());
        assertTrue(cardinalityMaxExtensions2.contains(expectedCardinalityMaxExtension2a));

        Extension extension3 = new Extension();
        extension3.add(a);
        extension3.add(d);
        extension3.add(c);
        Extension expectedCardinalityMaxExtension3a = new Extension();
        expectedCardinalityMaxExtension3a.add(b);
        expectedCardinalityMaxExtension3a.add(d);
        expectedCardinalityMaxExtension3a.add(c);
        Collection<Extension> cardinalityMaxExtensions3 =
                Utils.determineCardinalityMaxMonotonicExtensions(preferredSemantics.getModels(framework5), extension3);
        assertEquals(1, cardinalityMaxExtensions3.size());
        assertTrue(cardinalityMaxExtensions3.contains(expectedCardinalityMaxExtension3a));
    }
}
