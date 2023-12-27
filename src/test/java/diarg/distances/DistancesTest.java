package diarg.distances;

import diarg.Utils;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistancesTest {
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    CombinedDistance combinedMeasure = new CombinedDistance();
    InDistance inMeasure = new InDistance();
    OutDistance outMeasure = new OutDistance();

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
        double cDistance12Is = combinedMeasure.determineDistance(set1, set2, baseSet);
        double cDistance13Is = combinedMeasure.determineDistance(set1, set3, baseSet);
        double cDistance23Is = combinedMeasure.determineDistance(set2, set3, baseSet);
        assertEquals(1/3d, cDistance12Is);
        assertEquals(0, cDistance13Is);
        assertEquals(2/3d, cDistance23Is);
        double iDistance12Is = inMeasure.determineDistance(set1, set2, baseSet);
        double iDistance13Is = inMeasure.determineDistance(set1, set3, baseSet);
        double iDistance23Is = inMeasure.determineDistance(set2, set3, baseSet);
        assertEquals(0, iDistance12Is);
        assertEquals(0, iDistance13Is);
        assertEquals(2/3d, iDistance23Is);
        double oDistance12Is = outMeasure.determineDistance(set1, set2, baseSet);
        double oDistance13Is = outMeasure.determineDistance(set1, set3, baseSet);
        double oDistance23Is = outMeasure.determineDistance(set2, set3, baseSet);
        assertEquals(1/3d, oDistance12Is);
        assertEquals(0, oDistance13Is);
        assertEquals(0, oDistance23Is);

    }
}
