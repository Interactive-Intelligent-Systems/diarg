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
    private DungTheory framework1;
    private DungTheory framework2;
    private Collection<DungTheory> largestNormalRISubmodules;

    public AFTuple(DungTheory framework1, DungTheory framework2) {
        this.framework1 = framework1;
        this.framework2 = framework2;
        this.largestNormalRISubmodules = new LinkedList<>();
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
            Semantics semantics, Extension choice, DungTheory tempFramework) {
        Collection<Argument> argumentsNotInChoice = new LinkedList<>();
        for(Argument argument: tempFramework.getNodes()) {
            if(!choice.contains(argument)) argumentsNotInChoice.add(argument);
        }
        for(Argument argument: argumentsNotInChoice) {
            DungTheory submodule = new DungTheory();
            submodule.add(tempFramework);
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
            for (Argument argument: argumentsNotInChoice) {
                DungTheory submodule = new DungTheory();
                submodule.add(tempFramework);
                submodule.remove(argument);
                determineLargestNormalRISubmodules(semantics, choice, submodule);
            }
        }
        return this.largestNormalRISubmodules;
    }

    public Collection<DungTheory> determineLargestNormalRISubmodules(Semantics semantics, Extension choice) {
        Collection<Extension> extensions = semantics.getModels(this.framework2);
        Extension extension = extensions.iterator().next();

        if(extensions.size() == 1 && this.isReferenceIndependent(this.framework1, extension, choice)) {
            this.largestNormalRISubmodules.clear();
            this.largestNormalRISubmodules.add(this.framework2);
            return this.largestNormalRISubmodules;
        }
        return this.determineLargestNormalRISubmodules(semantics, choice, this.framework2);
    }

    public Collection<DungTheory> determineSmallestNormalRIExtensions() {
        Collection<DungTheory> smallestNormalRIExtensions = new LinkedList<>();
        return smallestNormalRIExtensions;
    }

    public Collection<DungTheory> determineLargestNormalCMSubmodules() {
        Collection<DungTheory> largestNormalCMSubmodules = new LinkedList<>();
        return largestNormalCMSubmodules;
    }

    public Collection<DungTheory> determineSmallestNormalCMExtensions() {
        Collection<DungTheory> determineSmallestNormalCMExtensions = new LinkedList<>();
        return determineSmallestNormalCMExtensions;
    }
}

