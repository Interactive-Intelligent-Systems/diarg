package diarg;

import net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner;
import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import com.google.common.collect.Collections2;

import java.util.*;

/**
 * Determines the Shkop extensions, given any argumentation framework AF = (AR, AT), as follows:
 * 1. Generate all permutation sequences of AR.
 *    Technically, optimize by only considering one sub-sequence of all arguments in the grounded extensions,
 *    arguments attacked by the grounded extensions, and self-attacking arguments. Generate all permutations that
 *    "entail" this sequence.
 * 2. For each permutation sequence:
 *      2.1: Construct the corresponding Shkop sequence; start with an empty extension.
 *      2.2: Resolve the Shkop sequence by adding arguments one-by-one to an initially empty extension, "discard"
 *      extensions that fail the Shkop testM, by default: extensions that are in conflict with the grounded extension of
 *      the current expansion. If an extensions is not discarded; add the new argument to it if this argument is neither
 *      self-attacking nor in conflict with the previously inferred extension.
 * 3. Return all extensions that have not been discarded
 *
 * @author Timotheus Kampik
 * @author Dov Gabbay
 */

public class SimpleShkopReasoner extends AbstractExtensionReasoner {

    private SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    private ShkopTest shkopTest = new GroundedShkopTest();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        bbase = Utils.removeSelfAttackedArguments(bbase);
        Extension groundedExtension = groundedReasoner.getModel(bbase);
        Extension baseSet = new Extension();
        Extension remainingSet = new Extension();
        baseSet.addAll(groundedExtension);
        List<Argument> baseList = new ArrayList<>(groundedExtension);
        for(Argument argument: bbase.getNodes()) {
            if(!groundedExtension.contains(argument) && (
                    bbase.isAttackedBy(argument, groundedExtension) ||
                    bbase.isAttacked(argument, groundedExtension) ||
                    bbase.isAttackedBy(argument, argument)
            )) {
                baseList.add(argument);
            } else if (!groundedExtension.contains(argument)) {
                remainingSet.add(argument);
            }
        }
        Collection<Extension> extensions = new LinkedList<>();
        Collection<List<Argument>> permutations = Collections2.permutations(remainingSet);
        for(List<Argument> permutationTail: permutations) {
            List<Argument> permutation = new ArrayList<>();
            permutation.addAll(baseList);
            permutation.addAll(permutationTail);
            Extension extension = new Extension();
            DungTheory framework = new DungTheory();
            for(Argument argument: permutation) {
                if(extension == null) {
                    break;
                }
                framework = shkopExpandFramework(bbase, framework, argument);
                if(!this.shkopTest.run(framework, extension) || (Utils.isUpstreamAttacker(argument, framework.getNodes(), framework) && !baseList.contains(argument))) {
                    extension = null;
                } else {
                    Extension counterfactualExtension = new Extension();
                    counterfactualExtension.addAll(extension);
                    counterfactualExtension.add(argument);
                    if (counterfactualExtension.isConflictFree(framework)) {
                        extension.add(argument);
                    }
                }
            }
            if(!extensions.contains(extension) && extension != null){
                extensions.add(extension);
            }
        }
        return extensions;
    }

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModel(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Extension getModel(DungTheory bbase) {
        Collection<Extension> extensions = this.getModels(bbase);
        return extensions.iterator().next();
    }

    /**
     * Shkop-expands a permutation by adding the "next" argument and its attack relations
     * to all existing arguments (according to the base framework) to a gradually expanded permutation
     * @param bbase Original framework that is to be resolved
     * @param currentFramework Current permutation-based Shkop-framework
     * @param argument Argument that should be added to the current framework
     * @return Shkop-expanded argumentation framework
     */
    private static DungTheory shkopExpandFramework(DungTheory bbase, DungTheory currentFramework, Argument argument) {
        currentFramework.add(argument);
        Collection<Argument> arguments = currentFramework.getNodes();
        arguments.add(argument);
        return (DungTheory) bbase.getRestriction(arguments);
    }

    /**
     * Sets a custom ShkopTest
     * @param shkopTest Custom ShkopTest
     */
    public void setShkopTest(ShkopTest shkopTest) {
        this.shkopTest = shkopTest;
    }
}
