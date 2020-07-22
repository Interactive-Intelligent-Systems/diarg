package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Provides abstractions for argumentation framework tuples.
 * Can check if a tuple forms a (normal or ordinary) expansion or submodule.
 * Can check for reference independent and rationally monotonic (normal or ordinary) smallest expansions and largest
 * submodules.
 * @author Timotheus Kampik
 */

public class AFTuple {
    private DungTheory framework1, framework2, smallerEqualFramework, largerEqualFramework;
    private Collection<DungTheory>  largestNormalRISubmodules, largestNormalCMSubmodules,
                                    smallestNormalRIExpansions, smallestNormalCMExpansions;

    public AFTuple(DungTheory framework1, DungTheory framework2) {
        this.framework1 = framework1;
        this.framework2 = framework2;
        if(this.framework2.containsAll(framework1.getNodes()) &&
                !(this.framework1.containsAll(framework2) && this.framework1.size() > this.framework2.size())) {
            this.smallerEqualFramework = framework1;
            this.largerEqualFramework = framework2;
        } else {
            this.smallerEqualFramework = framework2;
            this.largerEqualFramework = framework1;
        }
        this.largestNormalRISubmodules = new LinkedList<>();
        this.largestNormalCMSubmodules = new LinkedList<>();
        this.smallestNormalRIExpansions = new LinkedList<>();
        this.smallestNormalCMExpansions = new LinkedList<>();
    }

    /**
     * Checks if framework2 is an expansion of framework1.
     * @param framework1 The framework that is (supposedly) expanded by {@code framework2}
     * @param framework2 The framework that is (supposedly) expanding {@code framework1}
     * @return {@code true} or {@code false}
     */
    private static boolean isExpansion(DungTheory framework1, DungTheory framework2) {
        return  framework2.containsAll(framework1.getNodes()) &&
                framework2.containsAll(framework1.getAttacks());
    }

    /**
     * Checks if one argumentation framework is a normal expansion of another argumentation framework.
     * @param framework1 The framework that is (supposedly) normally expanded by {@code framework2}
     * @param framework2 The framework that is (supposedly) normally expanding {@code framework1}
     * @return {@code true} or {@code false}
     */
    private static boolean isNormalExpansion(DungTheory framework1, DungTheory framework2) {
        if (!isExpansion(framework1, framework2)) {
            return false;
        }
        for(Attack attack: framework2.getAttacks()) {
            if(
                    !framework1.containsAttack(attack) &&
                            framework1.contains(attack.getAttacker()) &&
                            framework1.contains(attack.getAttacked())) {
                return false;
            }
        }
        return true;

    }

    /**
     * Checks if an extension ({@code resolution2}) is reference independent w.r.t. a framework ({@code framework1})
     * and its resolution ({@code resolution1}).
     * @param framework1 The base framework
     * @param resolution1 The base framework's resolution
     * @param resolution2 The new resolution that is supposedly reference independent w.r.t. to the base framework and
     *                    its resolution
     * @return {@code true} or {@code false}
     */
    private static boolean isReferenceIndependent(DungTheory framework1, Extension resolution1, Extension resolution2) {
        return !framework1.containsAll(resolution2) || resolution1.equals(resolution2);
    }

    /**
     * Checks if an argumentation framework {@code framework2} is rationally monotonic w.r.t. another framework
     * {@code framework1} and its resolution {@code resolution1}, given a specific semantics {@code semantics}.
     * @param framework1 The base framework
     * @param framework2 The framework whose resolution is (supposedly) reference independent w.r.t. to the base framework and
     *                   its resolution
     * @param resolution1 The base framework's resolution
     * @param semantics The semantics that helps resolve {@code framework2}
     * @return {@code true} or {@code false}
     */
    private static boolean isRationallyMonotonic(DungTheory framework1, DungTheory framework2,
                                                  Extension resolution1, Semantics semantics) {
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        Extension extensionAttackingArgs = new Extension();
        for (Argument argument: tempFramework.getNodes()) {
                if(!framework1.contains(argument) && tempFramework.isAttackedBy(argument, resolution1)) {
                    extensionAttackingArgs.add(argument);
            }
        }
        tempFramework.removeAll(extensionAttackingArgs);
        return semantics.getModels(framework2).size() == 1 && semantics.getModel(tempFramework).containsAll(resolution1);
    }

    /**
     * Given a framework, determines a new argument with a name that is unique in the framework.
     * @param framework The argumentation framework, for which a new argument is to be generated.
     * @param lenght The minimum character length of the newly generated argument.
     * @return The new argument
     */
    private static Argument getNewArgument(DungTheory framework, int lenght) {
        String argumentId = java.util.UUID.randomUUID().toString().substring(0,lenght);
        Argument argument = new Argument(argumentId);
        if(framework.contains(argument)) {
            return getNewArgument(framework, ++lenght);
        }
        return argument;
    }

    /**
     * Checks if {@code framework2} in the tuple is an expansion of {@code framework1}.
     * @return {@code true} or {@code false}
     */
    public boolean isExpansion() {
        return this.isExpansion(this.framework1, this.framework2);
    }

    /**
     * Checks if {@code framework2} in the tuple is a normal expansion of {@code framework1}.
     * @return {@code true} or {@code false}
     */
    public boolean isNormalExpansion() {
       return this.isNormalExpansion(this.framework1, this.framework2);
    }

    /**
     * Checks if {@code framework2} in the tuple is a submodule of {@code framework1}.
     * @return {@code true} or {@code false}
     */
    public boolean isSubmodule() {
        return isExpansion(this.framework2, this.framework1);
    }

    /**
     * Checks if {@code framework2} in the tuple is a normal submodule of {@code framework1}.
     * @return {@code true} or {@code false}
     */
    public boolean isNormalSubmodule() {
        return this.isNormalExpansion(this.framework2, this.framework1);
    }

    /**
     * Determines the largest normal reference independent submodules of a framework w.r.t. to another framework and
     * its resolution
     * @param semantics The semantics that is used to determine the extensions of the framework's submodules
     * @param resolution The "preceding" framework's resolution
     * @param framework The framework for which the largest normal reference independent submodules are to be returned
     * @return The framework's largest normal reference independent submodules
     */
    private Collection<DungTheory> determineLargestNormalRISubmodules(
            Semantics semantics, Extension resolution, DungTheory framework) {
        for(Argument argument: framework.getNodes()) {
            DungTheory submodule = new DungTheory();
            submodule.add(framework);
            submodule.remove(argument);
            boolean existsLarger = this.largestNormalRISubmodules.size() > 0 &&
                                    this.largestNormalRISubmodules.iterator().next().size() > submodule.size();
            boolean isIn = false;
            for(DungTheory lnriSubmodule: this.largestNormalRISubmodules) {
                if(lnriSubmodule.getSignature().equals(submodule.getSignature())) {
                    isIn = true;
                    break;
                }
            }
            if(existsLarger || isIn) {
                return this.largestNormalRISubmodules;
            }
            Collection<Extension> extensions = semantics.getModels(submodule);
            Extension extension = extensions.iterator().next();
            if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, resolution, extension)) {
                this.largestNormalRISubmodules.add(submodule);
            }
        }
        if(this.largestNormalRISubmodules.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory submodule = new DungTheory();
                submodule.add(framework);
                submodule.remove(argument);
                determineLargestNormalRISubmodules(semantics, resolution, submodule);
            }
        }
        return this.largestNormalRISubmodules;
    }

    /**
     * Determines the smallest normal reference independent expansions of a framework w.r.t. to another framework and
     * its resolution
     * @param semantics The semantics that is used to determine the extensions of the framework's expansions
     * @param resolution The "preceding" framework's resolution
     * @param framework The framework for which the smallest normal reference independent expansions are to be returned
     * @param newArgument An argument that can be added to the framework to be used as an annihilator
     * @return The smallest normal reference independent expansions
     */
    private Collection<DungTheory> determineSmallestNormalRIExpansions(
            Semantics semantics, Extension resolution, DungTheory framework, Argument newArgument) {
        for(Argument argument: framework.getNodes()) {
            DungTheory expansion = new DungTheory();
            expansion.add(framework);
            expansion.add(new Attack(newArgument, argument));
            boolean existsSmaller = this.smallestNormalRIExpansions.size() > 0 &&
                    this.smallestNormalRIExpansions.iterator().next().getAttacks().size() <
                            expansion.getAttacks().size();
            boolean isIn = false;
            for(DungTheory snriExpansion: this.smallestNormalRIExpansions) {
                if(snriExpansion.prettyPrint().equals(expansion.prettyPrint())) {
                    isIn = true;
                    break;
                }
            }
            if(existsSmaller || isIn) {
                return this.smallestNormalRIExpansions;
            }
            Collection<Extension> extensions = semantics.getModels(expansion);
            Extension extension = extensions.iterator().next();
            extension.remove(newArgument);
            boolean isRefIndep = isReferenceIndependent(this.framework1, resolution, extension);
            if(extensions.size() == 1 && isRefIndep) {
                this.smallestNormalRIExpansions.add(expansion);
            }
        }
        if(this.smallestNormalRIExpansions.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory expansion = new DungTheory();
                expansion.add(framework);
                expansion.add(new Attack(newArgument, argument));
                determineSmallestNormalRIExpansions(semantics, resolution, expansion, newArgument);
            }
        }
        return this.smallestNormalRIExpansions;
    }

    /**
     * Determines the largest normal rationally monotonic submodules of a framework w.r.t. to another framework and
     * its resolution
     * @param semantics The semantics that is used to determine the extensions of the framework's expansions
     * @param framework The framework for which the largest normal rationally monotonic submodules are to be returned
     * @param resolution The "preceding" framework's resolution
     * @return The framework's largest normal rationally monotonic submodules
     */
    private Collection<DungTheory> determineLargestNormalCMSubmodules(
            Semantics semantics, DungTheory framework, Extension resolution) {
        for(Argument argument: framework.getNodes()) {
            DungTheory submodule = new DungTheory();
            submodule.add(framework);
            submodule.remove(argument);
            boolean existsLarger = this.largestNormalCMSubmodules.size() > 0 &&
                    this.largestNormalCMSubmodules.iterator().next().size() > submodule.size();
            boolean isIn = false;
            for(DungTheory lncmSubmodule: this.largestNormalCMSubmodules) {
                if(lncmSubmodule.getSignature().equals(submodule.getSignature())) {
                    isIn = true;
                    break;
                }
            }
            if(existsLarger || isIn) {
                return this.largestNormalCMSubmodules;
            }
            Collection<Extension> extensions = semantics.getModels(submodule);
            boolean isCM =  this.isRationallyMonotonic(this.framework1, submodule, resolution, semantics);
            if(extensions.size() == 1 && isCM) {
                this.largestNormalCMSubmodules.add(submodule);
            }
        }
        if(this.largestNormalCMSubmodules.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory submodule = new DungTheory();
                submodule.add(framework);
                submodule.remove(argument);
                determineLargestNormalCMSubmodules(semantics, submodule, resolution);
            }
        }
        return this.largestNormalCMSubmodules;
    }

    /**
     Determines the smallest normal rationally monotonic expansions of a framework w.r.t. to another framework and
     * its resolution
     * @param semantics The semantics that is used to determine the extensions of the framework's expansions
     * @param framework The framework for which the smallest normal rationally monotonic expansions are to be returned
     * @param resolution The "preceding" framework's resolution
     * @param newArgument Annihilator argument
     * @return The framework's smallest normal rationally monotonic expansions
     */
    private Collection<DungTheory> determineSmallestNormalCMExpansions(
            Semantics semantics, Extension resolution, DungTheory framework, Argument newArgument) {
        for(Argument argument: framework.getNodes()) {
            DungTheory expansion = new DungTheory();
            expansion.add(framework);
            expansion.add(new Attack(newArgument, argument));
            boolean existsSmaller = this.smallestNormalCMExpansions.size() > 0 &&
                    this.smallestNormalCMExpansions.iterator().next().getAttacks().size() <
                            expansion.getAttacks().size();
            boolean isIn = false;
            for(DungTheory sncmExpansion: this.smallestNormalCMExpansions) {
                if(sncmExpansion.prettyPrint() == expansion.prettyPrint()) {
                    isIn = true;
                    break;
                }
            }
            if(existsSmaller || isIn) {
                return this.smallestNormalCMExpansions;
            }
            Collection<Extension> extensions = semantics.getModels(expansion);
            Extension extension = extensions.iterator().next();
            extension.remove(newArgument);
            boolean isCM = this.isRationallyMonotonic(this.framework1, expansion, resolution, semantics);
            if(isCM) {
                this.smallestNormalCMExpansions.add(expansion);
            }
        }
        if(this.smallestNormalCMExpansions.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory expansion = new DungTheory();
                expansion.add(framework);
                expansion.add(new Attack(newArgument, argument));
                determineSmallestNormalCMExpansions(semantics, resolution, expansion, newArgument);
            }
        }
        return this.smallestNormalCMExpansions;
    }

    /**
     * Determines the largest normal reference independent submodules of the second framework in the tuple w.r.t.
     * to the first framework in the tuple, its resolution, and the specified argumentation semantics.
     * @param semantics The semantics that is used to determine the framework's submodules
     * @param resolution The resolution of the tuple's first framework that is used to determine the framework's
     *                   submodules
     * @return The framework's largest normal reference independent submodules
     */
    public Collection<DungTheory> determineLargestNormalRISubmodules(Semantics semantics, Extension resolution) {
        this.largestNormalRISubmodules.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();

        if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, resolution, extension)) {
            this.largestNormalRISubmodules.add(this.framework2);
            return this.largestNormalRISubmodules;
        }
        return this.determineLargestNormalRISubmodules(semantics, resolution, this.framework2);
    }

    /**
     * Determines the smallest normal reference independent expansions of the second framework in the tuple w.r.t.
     * to the first framework in the tuple, its resolution, and the specified argumentation semantics.
     * @param semantics The semantics that is used to determine the framework's expansions
     * @param resolution The resolution of the tuple's first framework that is used to determine the framework's
     *                   expansions
     * @return The framework's smallest normal reference independent expansions
     */
    public Collection<DungTheory> determineSmallestNormalRIExpansions(Semantics semantics, Extension resolution) {
        /* Note: requires the semantics to include all unattacked arguments and to be conflict-free otherwise,
        it can potentially be the case that no expansion will be returned although a smallest normal RI expansion exists*/
        this.smallestNormalRIExpansions.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();
        if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, resolution, extension)) {
            this.smallestNormalRIExpansions.add(this.framework2);
            return this.smallestNormalRIExpansions;
        }
        Argument newArgument = this.getNewArgument(this.framework2, 1);
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        tempFramework.add(newArgument);
        return this.determineSmallestNormalRIExpansions(semantics, resolution, tempFramework, newArgument);
    }

    /**
     * Determines the largest normal rationally monotonic submodules of the second framework in the tuple w.r.t.
     * to the first framework in the tuple, its resolution, and the specified argumentation semantics.
     * @param semantics The semantics that is used to determine the framework's submodules
     * @param resolution The resolution of the tuple's first framework that is used to determine the framework's
     *                   submodules
     * @return The framework's largest normal rationally monotonic submodules
     */
    public Collection<DungTheory> determineLargestNormalCMSubmodules(Semantics semantics, Extension resolution) {
        this.largestNormalCMSubmodules.clear();
        if(this.isRationallyMonotonic(this.framework1, this.framework2, resolution, semantics)) {
            this.largestNormalCMSubmodules.add(this.framework2);
            return this.largestNormalCMSubmodules;
        }
        return this.determineLargestNormalCMSubmodules(semantics, this.framework2, resolution);
    }

    /**
     * Determines the smallest normal rationally monotonic expansions of the second framework in the tuple w.r.t.
     * to the first framework in the tuple, its resolution, and the specified argumentation semantics.
     * @param semantics The semantics that is used to determine the framework's expansions
     * @param resolution The resolution of the tuple's first framework that is used to determine the framework's
     *                   expansions
     * @return The framework's smallest normal rationally monotonic expansions
     */
    public Collection<DungTheory> determineSmallestNormalCMExpansions(Semantics semantics, Extension resolution) {
        /* Note: requires the semantics to include all unattacked arguments and to be conflict-free otherwise,
        it can potentially be the case that no expansion will be returned although a smallest normal CM expansion exists*/
        this.smallestNormalCMExpansions.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);

        if(extensions.size() == 1 && this.isRationallyMonotonic(this.framework1, this.framework2, resolution, semantics)) {
            this.smallestNormalCMExpansions.add(this.framework2);
            return this.smallestNormalCMExpansions;
        }
        Argument newArgument = this.getNewArgument(this.framework2, 1);
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        tempFramework.add(newArgument);
        return this.determineSmallestNormalCMExpansions(semantics, resolution, tempFramework, newArgument);
    }
}

