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
 * Can check for reference independent and cautiously monotonic (normal or ordinary) smallest expansions and largest
 * submodules.
 * @author Timotheus Kampik
 */

public class AFTuple {
    private DungTheory framework1, framework2;
    private Collection<DungTheory>  largestNormalRISubmodules, largestNormalCMSubmodules,
                                    smallestNormalRIExpansions, smallestNormalCMExpansions;

    public AFTuple(DungTheory framework1, DungTheory framework2) {
        this.framework1 = framework1;
        this.framework2 = framework2;
        this.largestNormalRISubmodules = new LinkedList<>();
        this.largestNormalCMSubmodules = new LinkedList<>();
        this.smallestNormalRIExpansions = new LinkedList<>();
        this.smallestNormalCMExpansions = new LinkedList<>();
    }

    private static boolean isExpansion(DungTheory framework1, DungTheory framework2) {
        return  framework2.containsAll(framework1.getNodes()) &&
                framework2.containsAll(framework1.getAttacks());
    }

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

    private static boolean isReferenceIndependent(DungTheory framework1, Extension choice1, Extension choice2) {
        return !framework1.containsAll(choice2) || choice1.equals(choice2);
    }

    private static boolean isCautiouslyMonotonic(DungTheory framework1, DungTheory framework2,
                                                  Extension choice1, Semantics semantics) {
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        Extension extensionAttackingArgs = new Extension();
        for (Argument argument: tempFramework.getNodes()) {
                if(!framework1.contains(argument) && tempFramework.isAttackedBy(argument, choice1)) {
                    extensionAttackingArgs.add(argument);
            }
        }
        tempFramework.removeAll(extensionAttackingArgs);
        return semantics.getModels(framework2).size() == 1 && semantics.getModel(tempFramework).containsAll(choice1);
    }

    private static Argument getNewArgument(DungTheory framework, int lenght) {
        String argumentId = java.util.UUID.randomUUID().toString().substring(0,lenght);
        Argument argument = new Argument(argumentId);
        if(framework.contains(argument)) {
            return getNewArgument(framework, ++lenght);
        }
        return argument;
    }

    public boolean isExpansion() {
        return this.isExpansion(this.framework1, this.framework2);
    }

    public boolean isNormalExpansion() {
       return this.isNormalExpansion(this.framework1, this.framework2);
    }

    public boolean isSubmodule() {
        return isExpansion(this.framework2, this.framework1);
    }

    public boolean isNormalSubmodule() {
        return this.isNormalExpansion(this.framework2, this.framework1);
    }

    private Collection<DungTheory> determineLargestNormalRISubmodules(
            Semantics semantics, Extension choice, DungTheory framework) {
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
            if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, choice, extension)) {
                this.largestNormalRISubmodules.add(submodule);
            }
        }
        if(this.largestNormalRISubmodules.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory submodule = new DungTheory();
                submodule.add(framework);
                submodule.remove(argument);
                determineLargestNormalRISubmodules(semantics, choice, submodule);
            }
        }
        return this.largestNormalRISubmodules;
    }

    private Collection<DungTheory> determineSmallestNormalRIExpansions(
            Semantics semantics, Extension choice, DungTheory framework, Argument newArgument) {
        for(Argument argument: framework.getNodes()) {
            DungTheory expansion = new DungTheory();
            expansion.add(framework);
            expansion.add(new Attack(newArgument, argument));
            boolean existsSmaller = this.smallestNormalRIExpansions.size() > 0 &&
                    this.smallestNormalRIExpansions.iterator().next().getAttacks().size() <
                            expansion.getAttacks().size();
            boolean isIn = false;
            for(DungTheory snriExpansion: this.smallestNormalRIExpansions) {
                if(snriExpansion.prettyPrint() == expansion.prettyPrint()) {
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
            boolean isRefIndep = this.isReferenceIndependent(this.framework1, choice, extension);
            if(extensions.size() == 1 && isRefIndep) {
                this.smallestNormalRIExpansions.add(expansion);
            }
        }
        if(this.smallestNormalRIExpansions.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory expansion = new DungTheory();
                expansion.add(framework);
                expansion.add(new Attack(newArgument, argument));
                determineSmallestNormalRIExpansions(semantics, choice, expansion, newArgument);
            }
        }
        return this.smallestNormalRIExpansions;
    }

    private Collection<DungTheory> determineLargestNormalCMSubmodules(
            DungTheory tempFramework, Extension choice, Semantics semantics) {
        for(Argument argument: tempFramework.getNodes()) {
            DungTheory submodule = new DungTheory();
            submodule.add(tempFramework);
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
            boolean isCautiouslyMonotonic =  this.isCautiouslyMonotonic(this.framework1, submodule, choice, semantics);
            if(extensions.size() == 1 && isCautiouslyMonotonic) {
                this.largestNormalCMSubmodules.add(submodule);
            }
        }
        if(this.largestNormalCMSubmodules.size() == 0) {
            for (Argument argument: tempFramework.getNodes()) {
                DungTheory submodule = new DungTheory();
                submodule.add(tempFramework);
                submodule.remove(argument);
                determineLargestNormalCMSubmodules(submodule, choice, semantics);
            }
        }
        return this.largestNormalCMSubmodules;
    }

    private Collection<DungTheory> determineSmallestNormalCMExpansions(
            Semantics semantics, Extension choice, DungTheory framework, Argument newArgument) {
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
            boolean isCM = this.isCautiouslyMonotonic(this.framework1, expansion, choice, semantics);
            if(isCM) {
                this.smallestNormalCMExpansions.add(expansion);
            }
        }
        if(this.smallestNormalCMExpansions.size() == 0) {
            for (Argument argument: framework.getNodes()) {
                DungTheory expansion = new DungTheory();
                expansion.add(framework);
                expansion.add(new Attack(newArgument, argument));
                determineSmallestNormalCMExpansions(semantics, choice, expansion, newArgument);
            }
        }
        return this.smallestNormalCMExpansions;
    }

    public Collection<DungTheory> determineLargestNormalRISubmodules(Semantics semantics, Extension choice) {
        this.largestNormalRISubmodules.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();

        if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, extension, choice)) {
            this.largestNormalRISubmodules.add(this.framework2);
            return this.largestNormalRISubmodules;
        }
        return this.determineLargestNormalRISubmodules(semantics, choice, this.framework2);
    }

    public Collection<DungTheory> determineSmallestNormalRIExpansions(Semantics semantics, Extension choice) {
        /* Note: requires the semantics to include all unattacked arguments and to be conflict-free otherwise,
        it can potentially be the case that no expansion will be returned although a smallest normal RI expansion exists*/
        this.smallestNormalRIExpansions.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();

        if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, extension, choice)) {
            this.smallestNormalRIExpansions.add(this.framework2);
            return this.smallestNormalRIExpansions;
        }
        Argument newArgument = this.getNewArgument(this.framework2, 1);
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        tempFramework.add(newArgument);
        return this.determineSmallestNormalRIExpansions(semantics, choice, tempFramework, newArgument);
    }

    public Collection<DungTheory> determineLargestNormalCMSubmodules(Semantics semantics, Extension choice) {
        this.largestNormalCMSubmodules.clear();
        if(this.isCautiouslyMonotonic(this.framework1, this.framework2, choice, semantics)) {
            this.largestNormalCMSubmodules.add(this.framework2);
            return this.largestNormalCMSubmodules;
        }
        return this.determineLargestNormalCMSubmodules(this.framework2, choice, semantics);
    }

    public Collection<DungTheory> determineSmallestNormalCMExpansions(Semantics semantics, Extension choice) {
        /* Note: requires the semantics to include all unattacked arguments and to be conflict-free otherwise,
        it can potentially be the case that no expansion will be returned although a smallest normal CM expansion exists*/
        this.smallestNormalCMExpansions.clear();
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();

        if(extensions.size() == 1 && this.isCautiouslyMonotonic(this.framework1, this.framework2, choice, semantics)) {
            this.smallestNormalCMExpansions.add(this.framework2);
            return this.smallestNormalCMExpansions;
        }
        Argument newArgument = this.getNewArgument(this.framework2, 1);
        DungTheory tempFramework = new DungTheory();
        tempFramework.add(framework2);
        tempFramework.add(newArgument);
        return this.determineSmallestNormalCMExpansions(semantics, choice, tempFramework, newArgument);
    }
}

