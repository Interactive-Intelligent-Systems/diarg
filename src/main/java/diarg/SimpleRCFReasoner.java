package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.reasoner.SimpleCF2Reasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner;

/**
 * Implements Rational Conflict-Free (RCF) semantics by providing a wrapper around the Tweety SimpleCF2Reasoner
 * @author Timotheus Kampik
 */
public class SimpleRCFReasoner extends AbstractExtensionReasoner{

    private SimpleCF2Reasoner cf2Reasoner = new SimpleCF2Reasoner();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        Collection<Argument> unattackedArguments = Utils.determineUnattackedArguments(bbase);
        Collection<Extension> prefilteredExtensions = cf2Reasoner.getModels(Utils.removeSelfAttackedArguments(bbase));
        Collection<Extension> rcfExtensions = new LinkedList<>();
        for(Extension extension: prefilteredExtensions) {
            if(extension.containsAll(unattackedArguments)) {
                rcfExtensions.add(extension);
            }
        }
        return rcfExtensions;
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
