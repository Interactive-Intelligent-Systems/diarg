
package examples;

import diarg.*;
import diarg.enums.*;

import net.sf.tweety.arg.dung.reasoner.SimpleCF2Reasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

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
        Semantics rcfSemantics = new Semantics(SemanticsType.RCF);
        AFSequence sequence = new AFSequence(
                SequenceType.NORMALLY_EXPANDING,
                ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics,
                true);
        sequence.addFramework(initialFramework);

        // Resolve initial framework
        Extension resolution = sequence.resolveFramework(0);
        System.out.println(resolution);

        // Add user feedback to recommendation
        DungTheory initialUserFeedback = new DungTheory();
        initialUserFeedback.add(initialFramework);
        Argument d = new Argument("d");
        initialUserFeedback.add(d);
        initialUserFeedback.addAttack(d, c);
        sequence.addFramework(initialUserFeedback);
        sequence.resolveFramework(1);

        // create "weekend" context with argument "d"
        Extension contextArguments = new Extension();
        Context context = new Context("weekend", contextArguments);

        // Add same framework with weekend context



    }
}