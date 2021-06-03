package diarg;

import net.sf.tweety.arg.dung.reasoner.SimpleGroundedReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides nsa grounded Shkop test facilities for DiArg's Shkop reasoner
 * grounded Shkop test that ignores self-attacking arguments
 * @author Timotheus Kampik
 */
public class GroundedShkopTest extends ShkopTest{
    SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
    @Override
    boolean run(DungTheory framework, Extension extension, Argument newArgument) {
        Collection<Argument> restrictionArgs = new ArrayList<>();
        for(Argument argument: framework.getNodes()) {
            if(extension.contains(argument) || framework.existsDirectedPath(newArgument, argument)) {
                restrictionArgs.add(argument);
            }
        }
        Extension groundedExtension = groundedReasoner.getModel(Utils.removeSelfAttackedArguments((DungTheory) framework.getRestriction(restrictionArgs)));
        if(framework.isAttacked(extension, groundedExtension)) {
            return false;
        }
        return true;
    }
}
