package diarg;

import net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner;
import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Determines the Shkop extensions, given any argumentation framework AF = (AR, AT), as follows:
 * 1. Generate all permutation sequences of AR.
 * 2. For each permutation, construct the permutation's Shkop framework.
 *    Start with an empty framework ("Shkop framework") and add the permutation's arguments one-by-one,
 *    following the order of the permutation sequence. For each argument:
 *    2.1. Add the argument to the Shkop framework and add all attack relations in AT that exist between the argument
 *         and arguments in the Shkop framework.
 *    2.2. If the argument "closes a loop" in the Shkop framework, i.e., if the argument is self-attacking or in an SCC
 *         such that |SCC| greater than 1, remove the argument from the Shkop framework.
 * 3. Remove duplicated Shkop frameworks, then determine their grounded extensions.
 *    Note that Shkop frameworks are always acyclic.
 * 4. Return the grounded extensions.
 *
 * @author Timotheus Kampik
 * @author Dov Gabbay
 */

public class SimpleShkopReasoner extends AbstractExtensionReasoner {

    private SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    private ShkopTest shkopTest = new AdmissibleShkopTest();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        Collection<DungTheory> permutationFrameworks = new ArrayList<>();
        Collection<Extension> extensions = new LinkedList<>();
        ArrayList args = new ArrayList(bbase.getNodes());
        Collection<List<Argument>> permutations = Collections2.permutations(args);
        for(List<Argument> permutation: permutations) {
            DungTheory framework = new DungTheory();
            for(Argument argument: permutation) {
                DungTheory counterfactualFramework = new DungTheory();
                counterfactualFramework.add(framework);
                counterfactualFramework = shkopExpandFramework(bbase, counterfactualFramework, argument);
                Collection<Collection<Argument>> sccs = counterfactualFramework.getStronglyConnectedComponents();
                boolean addsCycle = false;
                for(Collection<Argument> scc: sccs) {
                    if(scc.size() > 1 || bbase.contains(new Attack(argument, argument))) {
                        addsCycle = true;
                        break;
                    }
                }
                if(!addsCycle) {
                    framework = shkopExpandFramework(bbase, framework, argument);
                } else if(shkopTest.run(bbase, argument)) {
                    framework.removeAll(counterfactualFramework.getAttackers(argument));
                    framework = shkopExpandFramework(bbase, framework, argument);
                }
            }
            if(!permutationFrameworks.contains(framework)){
                permutationFrameworks.add(framework);
            }
        }

        for(DungTheory permutationNetwork: permutationFrameworks) {
                Extension extension = groundedReasoner.getModels(permutationNetwork).iterator().next();
                if(!extensions.contains(extension)) {
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
    private static DungTheory shkopExpandFramework(
            DungTheory bbase, DungTheory currentFramework, Argument argument) {
        currentFramework.add(argument);
        for(Attack attack: bbase.getAttacks()) {
            if(     !currentFramework.contains(attack) &&
                    currentFramework.contains(attack.getNodeA()) &&
                    currentFramework.contains(attack.getNodeB())) {
                currentFramework.add(attack);
            }
        }
        return currentFramework;
    }

    /**
     * Sets a custom ShkopTest
     * @param shkopTest Custom ShkopTest
     */
    public void setShkopTest(ShkopTest shkopTest) {
        this.shkopTest = shkopTest;
    }
}
