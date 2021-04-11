package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;
import java.util.stream.Collectors;

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

    /**
     * Determines the maximally monotonic extensions (w.r.t. set inclusion) of a list of target extensions w.r.t. to a
     * list of base extensions
     * @param target The target extensions, from which the maximally monotonic extensions should be selected
     * @param base The base extensions, based on which maximal monotony is to be determined
     * @return The subset-maximal extensions
     */
    public static Collection<Extension> determineSubsetMaxMonotonicExtensions(
            Collection<Extension> target, Collection<Extension> base) {
        Collection<Extension> maxExtensions = new ArrayList<>();
        for(Extension baseExtension: base) {
            List<Set<Argument>> intersections = new ArrayList<>();
            List<Set<Argument>> maximalIntersections = new ArrayList<>();
            for(Extension targetExtension: target) {
                Set<Argument> intersection =
                        targetExtension.stream().distinct().filter(baseExtension::contains).collect(Collectors.toSet());
                intersections.add(intersection);
            }
            for(Set<Argument> intersection: intersections) {
                if(maximalIntersections.size() == 0) {
                    maximalIntersections.add(intersection);
                } else {
                    boolean addIntersection = true;
                    Collection<Set<Argument>> toRemoveIntersections = new ArrayList<>();
                    for(Set<Argument> maximalIntersection: maximalIntersections) {
                        if(maximalIntersection.containsAll(intersection) && maximalIntersection.size() > intersection.size()) {
                            addIntersection = false;
                            break;
                        } else if (intersection.containsAll(maximalIntersection) && intersection.size() > maximalIntersection.size()) {
                            toRemoveIntersections.add(maximalIntersection);
                        }
                    }
                    if(addIntersection) maximalIntersections.add(intersection);
                    maximalIntersections.removeAll(toRemoveIntersections);
                }
            }
            for(Extension targetExtension: target) {
                for(Set<Argument> intersection: maximalIntersections) {
                    if(targetExtension.containsAll(intersection) && !maxExtensions.contains(targetExtension)) {
                        maxExtensions.add(targetExtension);
                    }
                }
            }
        }
        return maxExtensions;
    }

    /**
     * Determines the pareto optimal maximally monotonic extensions (w.r.t. set inclusion) of a list of target
     * extensions w.r.t. to a list of base extensions
     * @param target The target extensions, from which the maximally PO-monotonic extensions should be selected
     * @param base The base extensions, based on which maximal PO-monotony is to be determined
     * @return The subset-maximal PO-extensions
     */
    public static Collection<Extension> determinePOMaxMonotonicExtensions(
            Collection<Extension> target, Collection<Extension> base) {
        ArrayList<Extension> subsetMaxExtensions = new ArrayList(determineSubsetMaxMonotonicExtensions(target, base));
        ArrayList<Collection<Extension>> maxForBasesList = new ArrayList<>();
        for(Extension subsetMaxExtension1: subsetMaxExtensions) {
            Collection<Extension> maxForBases = new ArrayList<>();
            for(Extension baseExtension: base) {
                boolean isMax = true;
                Set<Argument> intersection1 =
                        subsetMaxExtension1.stream().distinct().filter(baseExtension::contains).collect(Collectors.toSet());
                for(Extension subsetMaxExtension2: subsetMaxExtensions) {
                    Set<Argument> intersection2 =
                            subsetMaxExtension2.stream().distinct().filter(baseExtension::contains).collect(Collectors.toSet());
                    if(intersection2.containsAll(intersection1) && intersection2.size() > intersection1.size()) {
                        isMax = false;
                        break;
                    }
                }
                if(isMax) {
                    maxForBases.add(baseExtension);
                }
            }
            maxForBasesList.add(maxForBases);
        }
        Collection<Extension> toBeRemovedExtensions = new ArrayList<>();
        int i = -1;
        for(Collection<Extension> maxForBases1: maxForBasesList) {
            i++;
            for(Collection<Extension> maxForBases2: maxForBasesList) {
                if(maxForBases2.containsAll(maxForBases1) && maxForBases2.size() > maxForBases1.size()) {
                    toBeRemovedExtensions.add(subsetMaxExtensions.get(i));
                    break;
                }
            }
        }
        for(Extension toBeRemovedExtension: toBeRemovedExtensions) {
            subsetMaxExtensions.remove(toBeRemovedExtension);
        }
        return subsetMaxExtensions;
    }
}

