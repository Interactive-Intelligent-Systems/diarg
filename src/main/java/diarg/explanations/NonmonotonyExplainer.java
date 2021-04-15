package diarg.explanations;

import diarg.AFTuple;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Identifies sets of arguments that serve as explanations for the violation of monotony in the context of admissible
 * set-based semantics.
 * @author Timotheus Kampik
 */
public class NonmonotonyExplainer {

    /**
     * Determines a collection of arguments that explain the violation of monotony in a scenario where first, an extension
     * is inferred from an argumentation framework, the argumentation framework is then normally expanded, and another
     * extension is inferred from the normal expansion. Ff monotony is not violated, the collection is empty.
     * Only proven to work for some admissible set-based semantics: complete, preferred, grounded
     * @param afTuple Argumentation framework tuple that provides the original argumentation framework and one of its normal expansions
     * @param baseExtension The base extension that has been inferred from the original framework
     * @param targetExtension The target extension that has been inferred from the normal expansion
     * @return The monotony violation explanations
     */
    static Collection<Argument> determineMonotonyViolationExplanations(AFTuple afTuple, Extension baseExtension, Extension targetExtension) {
        DungTheory afPrime = afTuple.getFramework2();
        Collection<Argument> args = afTuple.getFramework1().getNodes();
        Collection<Argument> newArgs = new ArrayList<>(afTuple.getFramework2().getNodes());
        newArgs.removeAll(args);
        Collection<Argument> explanations = new ArrayList<>();
        for(Argument arg: newArgs) {
            if(afPrime.isAttackedBy(arg, baseExtension) && (!afPrime.isAttacked(arg, baseExtension) || !afPrime.isAttacked(arg, targetExtension))) {
                explanations.add(arg);
            }
        }
        return explanations;
    }
}
