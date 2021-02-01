package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

/**
 * Utils for manipulating and analyzing argumentation frameworks
 * @author Timotheus Kampik
 */
public class Utils {

    /**
     * Removes all self-attacking arguments from an argumentation framework.
     * @param framework Argumentation framework, from which all self-attacking arguments should be removed.
     * @return Argumentation framework without the self-attacking arguments
     */
    public static DungTheory removeSelfAttackedArguments(DungTheory framework) {
        Collection<Argument> arguments = framework.getNodes();
        Collection<Argument> selfAttackedArguments = new LinkedList<>();
        for (Argument argument : arguments) {
            if (framework.isAttackedBy(argument, argument)) {
                selfAttackedArguments.add(argument);
            }
        }
        framework.removeAll(selfAttackedArguments);
        return framework;
    }

    /**
     * Determines all unattacked arguments of an argumentation framework.
     * @param framework The argumentation framework, for which all unattacked arguments should be determined.
     * @return All unattacked arguments in the provided argumentation framework
     */
    public static Collection<Argument> determineUnattackedArguments(DungTheory framework) {
        Collection<Argument> unattackedArguments = new LinkedList<>();
        for(Argument argument: framework.getNodes()) {
            if(framework.getAttackers(argument).isEmpty()) {
                unattackedArguments.add(argument);
            }
        }
        return unattackedArguments;
    }

    /**
     * Determines the reachable range of a set of arguments
     * @param set The set whose range should be determined
     * @param framework The framework from which the set is taken
     * @return The set's reachable range
     */
    public static Collection<Argument> determineReachableRange(Collection<Argument> set, DungTheory framework) {
        Collection<Argument> reachableRange = new LinkedList<>();
        for(Argument fArgument: framework.getNodes()) {
            for(Argument sArgument: set) {
                if(framework.existsDirectedPath(sArgument, fArgument)) {
                    reachableRange.add(fArgument);
                    break;
                }
            }
        }
        return reachableRange;
    }

    /**
     * Determines sets union with all arguments are attacked by the set's reachably defended arguments
     * @param set The set whose reachable defense should be determined
     * @param framework The framework from which the set is taken
     * @return The arguments in the set that are reachably defended by the set
     */
    public static Collection<Argument> reachablyDefendedRanges(Collection<Argument> set, DungTheory framework) {
        Collection<Argument> reachableDefense = new LinkedList<>();
        for(Argument sArgument: set) {
            boolean isReachableDefense = true;
            Extension sExtension = new Extension();
            sExtension.add(sArgument);
            Collection<Argument> reachableArguments = determineReachableRange(sExtension, framework);
            for(Argument fArgument: framework.getNodes()) {
                boolean isReachableAttack = reachableArguments.contains(fArgument) &&
                        framework.containsAttack(new Attack(fArgument, sArgument));
                if(isReachableAttack) {
                    Collection<Argument> fArgumentAttackers = framework.getAttackers(fArgument);
                    for(Argument sArgumentPrime: set) {
                        isReachableDefense = false;
                        if(
                                fArgumentAttackers.contains(sArgumentPrime) &&
                                !reachableArguments.contains(sArgumentPrime) &&
                                !sArgumentPrime.equals(sArgument)
                        ) {
                            isReachableDefense = true;
                            break;
                        }
                    }
                }
            }
            if(isReachableDefense) {
                reachableDefense.add(sArgument);
            }
        }
        Collection<Argument> reachablyDefendedRange = new LinkedList<>();
        reachablyDefendedRange.addAll(set);
        for(Argument argument: reachableDefense) {
            reachablyDefendedRange.addAll(framework.getAttacked(argument));
        }
        return reachablyDefendedRange;
    }

    /**
     * Determines the similarity between two sets of arguments, given a "base" set, using a simple similarity measure:
     * (|arguments in base set and in set1 and set2| + |arguments in base set and neither in set1 nor in set two|)
     * divided by |arguments in base set|
     * @param set1 Set of arguments 1
     * @param set2 Set of arguments 2
     * @param baseSet Base set
     * @return The similarity between the sets
     */
    public static double determineSetSimilarity(
            Collection<Argument> set1,
            Collection<Argument> set2,
            Collection<Argument> baseSet) {
        if (baseSet.size() == 0) {
            return 1;
        }
        Collection<Argument> setInAll = new ArrayList<>();
        Collection<Argument> setInNone = new ArrayList<>();
        for(Argument argument: baseSet) {
            if(set1.contains(argument) && set2.contains(argument)) {
                setInAll.add(argument);
            } else if(!set1.contains(argument) && !set2.contains(argument)){
                setInNone.add(argument);
            }
        }
        return ((setInAll.size() + setInNone.size()) / (double) baseSet.size());
    }
}

