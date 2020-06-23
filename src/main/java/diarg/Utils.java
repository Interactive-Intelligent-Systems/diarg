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
     * Determines the all arguments in a set that are reachably defended by the set
     * @param set The set whose reachable defense should be determined
     * @param framework The framework from which the set is taken
     * @return The arguments in the set that are reachably defended by the set
     */
    public static Collection<Argument> determineReachableDefense(Collection<Argument> set, DungTheory framework) {
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
                        }
                    }
                }
            }
            if(isReachableDefense) {
                reachableDefense.add(sArgument);
            }
        }
        return reachableDefense;
    }
}

