package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.reasoner.SimpleStageReasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner;


/**
 * Implements Restricted Stage (RS) semantics by providing a wrapper around the Tweety SimpleStageReasoner
 * @author Timotheus Kampik
 */
public class SimpleRSReasoner extends AbstractExtensionReasoner  {

    private SimpleStageReasoner stageReasoner = new SimpleStageReasoner();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        Collection<Argument> arguments = bbase.getNodes();
        Collection<Argument> unattackedArguments = new LinkedList<>();
        for(Argument argument: arguments) {
            if(bbase.isAttackedBy(argument, argument)) {
                bbase.remove(argument);
            }
            if(bbase.getAttackers(argument).isEmpty()) {
                unattackedArguments.add(argument);
            }
        }
        Collection<Extension> prefilteredExtensions = stageReasoner.getModels(bbase);
        Collection<Extension> rsExtensions = new LinkedList<>();
        for(Extension extension: prefilteredExtensions) {
            if(extension.containsAll(unattackedArguments)) {
                rsExtensions.add(extension);
            }
        }
        return rsExtensions;
    }

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModel(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Extension getModel(DungTheory bbase) {
        Collection<Extension> extensions = this.getModels(bbase);
        return extensions.iterator().next();
    }

}
