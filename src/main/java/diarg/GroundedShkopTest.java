package diarg;

import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Provides grounded Shkop test facilities for DiArg's Shkop reasoner
 * @author Timotheus Kampik
 */
public class GroundedShkopTest extends ShkopTest{
    SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    @Override
    boolean run(DungTheory framework, Argument argument) {
        if(groundedReasoner.getModel(framework).contains(argument)) {
            return true;
        }
        return false;
    }
}
