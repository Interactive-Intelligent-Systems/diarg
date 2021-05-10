package diarg;

import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Provides nsa grounded Shkop test facilities for DiArg's Shkop reasoner
 * grounded Shkop test that ignores self-attacking arguments
 * @author Timotheus Kampik
 */
public class GroundedShkopTest extends ShkopTest{
    SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    @Override
    boolean run(DungTheory framework, Extension extension) {
        Extension groundedExtension = groundedReasoner.getModel(Utils.removeSelfAttackedArguments(framework));
        if(framework.isAttacked(extension, groundedExtension)) {
            return false;
        }
        return true;
    }
}
