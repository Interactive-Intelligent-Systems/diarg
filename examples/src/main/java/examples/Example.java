
package examples;

import diarg.*;
import diarg.enums.*;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Example code: argumentation-based dialogue reasoning in a health recommender system,
 * see: https://github.com/TimKam/diarg/blob/master/README.md.
 */
public class Example {
    public static void main(String[] args){
        // Specify initial argumentation framework:
        DungTheory initialFramework = new DungTheory();
        Argument a = new Argument("a"); // Meditate
        Argument b = new Argument("b"); // Join social lunch
        Argument c = new Argument("c"); // Go hiking
        initialFramework.add(a);
        initialFramework.add(b);
        initialFramework.add(c);
        initialFramework.addAttack(a, b);
        initialFramework.addAttack(b, a);
        initialFramework.addAttack(b, c);
        initialFramework.addAttack(c, b);
        initialFramework.addAttack(c, a);
        initialFramework.addAttack(a, c);

        // Initialize argumentation sequence and add initial framework:
        Semantics cf2Semantics = new Semantics(SemanticsType.CF2);
        AFSequence sequence = new AFSequence(
                SequenceType.NORMALLY_EXPANDING,
                ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                cf2Semantics,
                true);
        sequence.addFramework(initialFramework);

        // Resolve initial framework
        Extension resolution0 = sequence.resolveFramework(0);
        System.out.println(String.format("Initial recommendation: %s", resolution0));

        // Add user feedback to recommendation
        DungTheory initialUserFeedback = new DungTheory();
        initialUserFeedback.add(initialFramework);
        Argument d = new Argument("d");
        initialUserFeedback.add(d);
        initialUserFeedback.addAttack(d, c);
        sequence.addFramework(initialUserFeedback);
        Extension resolution1 = sequence.resolveFramework(1);
        System.out.println(String.format("Recommendation after user feedback: %s", resolution1));

        // create "weekend" context with argument "d"
        Extension contextArguments = new Extension();
        contextArguments.add(d);
        Context context = new Context("weekend", contextArguments);
        Collection<Context> contexts = new LinkedList();
        contexts.add(context);

        // Add same framework with weekend context
        sequence.addFramework(initialUserFeedback, contexts);

        // Resolve framework with weekend context
        Extension resolution2 = sequence.resolveFramework(2);
        System.out.println(String.format("Recommendation after context switch: %s", resolution2));
    }
}