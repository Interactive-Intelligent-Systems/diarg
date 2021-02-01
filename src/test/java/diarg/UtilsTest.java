package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
