package diarg.distances;

import net.sf.tweety.arg.dung.syntax.Argument;

import java.util.Collection;

/**
 * Abstract distance measure between sets
 * @author Timotheus Kampik
 */
public abstract class DistanceMeasure {

        public abstract double determineDistance(Collection<Argument> set1,
                                                 Collection<Argument> set2,
                                                 Collection<Argument> baseSet);
}
