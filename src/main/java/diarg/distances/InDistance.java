package diarg.distances;

import net.sf.tweety.arg.dung.syntax.Argument;

import java.util.ArrayList;
import java.util.Collection;

public class InDistance extends DistanceMeasure {
    /**
     * Determines the similarity between two sets of arguments, given a "base" set, considering only the arguments in
     * the sets:
     * (|arguments in base set and in set1 and set2|)
     * divided by |arguments in base set and in either set1, set2, or both|
     * @param set1 Set of arguments 1
     * @param set2 Set of arguments 2
     * @param baseSet Base set
     * @return The similarity between the sets
     */
    @Override
    public double determineDistance(Collection<Argument> set1, Collection<Argument> set2, Collection<Argument> baseSet) {
        Collection<Argument> setInAll = new ArrayList<>();
        Collection<Argument> setInSome = new ArrayList<>();
        for(Argument argument: baseSet) {
            if(set1.contains(argument) && set2.contains(argument) && baseSet.contains(argument)) {
                setInAll.add(argument);
                setInSome.add(argument);
            } else if(baseSet.contains(argument) && (set1.contains(argument) || set2.contains(argument))){
                setInSome.add(argument);
            }
        }
        if (setInSome.size() == 0) {
            return 1;
        }
        return setInAll.size() / (double) setInSome.size();
    }
}
