package diarg.distances;

import net.sf.tweety.arg.dung.syntax.Argument;

import java.util.ArrayList;
import java.util.Collection;

public class CombinedDistance extends DistanceMeasure {
    /**
     * Determines the similarity between two sets of arguments, given a "base" set, using a simple similarity measure:
     * (|arguments in base set and in set1 and set2| + |arguments in base set and neither in set1 nor in set two|)
     * divided by |arguments in base set|
     * @param set1 Set of arguments 1
     * @param set2 Set of arguments 2
     * @param baseSet Base set
     * @return The similarity between the sets
     */
    @Override
    public double determineDistance(Collection<Argument> set1, Collection<Argument> set2, Collection<Argument> baseSet) {
        if (baseSet.size() == 0) {
            return 1;
        }
        Collection<Argument> setInAll = new ArrayList<>();
        Collection<Argument> setInNone = new ArrayList<>();
        for(Argument argument: baseSet) {
            if(set1.contains(argument) && set2.contains(argument) && baseSet.contains(argument)) {
                setInAll.add(argument);
            } else if(!set1.contains(argument) && !set2.contains(argument) && baseSet.contains(argument)){
                setInNone.add(argument);
            }
        }
        return ((setInAll.size() + setInNone.size()) / (double) baseSet.size());
    }
}
