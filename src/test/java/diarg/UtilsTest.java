package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
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
}
