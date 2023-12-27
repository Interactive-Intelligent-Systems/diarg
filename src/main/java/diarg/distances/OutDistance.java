package diarg.distances;

import net.sf.tweety.arg.dung.syntax.Argument;

import java.util.ArrayList;
import java.util.Collection;

public class OutDistance extends DistanceMeasure{
    /**
     * Determines the similarity between two sets of arguments, given a "base" set, considering only the arguments in
     * the sets:
     * (|arguments in base set and in the complements of set1 of set2 w.r.t the base set|)
     * divided by |arguments in base set and in either of thee complements|
     * @param set1 Set of arguments 1
     * @param set2 Set of arguments 2
     * @param baseSet Base set
     * @return The similarity between the sets
     */
    @Override
    public double determineDistance(Collection<Argument> set1, Collection<Argument> set2, Collection<Argument> baseSet) {
        Collection<Argument> setInAllComplements = new ArrayList<>();
        Collection<Argument> setInSomeComplements = new ArrayList<>();
        for(Argument argument: baseSet) {
            if(!set1.contains(argument) && !set2.contains(argument) && baseSet.contains(argument)) {
                setInAllComplements.add(argument);
                setInSomeComplements.add(argument);
            } else if(baseSet.contains(argument) && (!set1.contains(argument) || !set2.contains(argument))){
                setInSomeComplements.add(argument);
            }
        }
        if (setInSomeComplements.size() == 0) {
            return 1;
        }
        return setInAllComplements.size() / (double) setInSomeComplements.size();
    }
}
