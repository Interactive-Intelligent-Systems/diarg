package diarg;

import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Provides nsa grounded Shkop test facilities for DiArg's Shkop reasoner
 * grounded Shkop test that ignores self-attacking arguments
 * @author Timotheus Kampik
 */
public class GroundedShkopTest extends ShkopTest{
    SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    @Override
    boolean run(DungTheory framework, Argument argument) {
        if(groundedReasoner.getModel(Utils.removeSelfAttackedArguments(framework)).contains(argument)) {
            return true;
        }
        return false;
    }
}
