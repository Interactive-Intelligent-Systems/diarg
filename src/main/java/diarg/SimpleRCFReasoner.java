package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.reasoner.SimpleConflictFreeReasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner;

/**
 * Implements Rational Conflict-Free (RCF) semantics by providing a wrapper around the Tweety SimpleCF2Reasoner
 * @author Timotheus Kampik
 */
public class SimpleRCFReasoner extends AbstractExtensionReasoner{

    private SimpleConflictFreeReasoner cfReasoner = new SimpleConflictFreeReasoner();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        Collection<Argument> unattackedArguments = Utils.determineUnattackedArguments(bbase);
        Collection<Extension> prefilteredExtensions = cfReasoner.getModels(Utils.removeSelfAttackedArguments(bbase));
        Collection<Extension> prelimRCFExtensions = new LinkedList<>();
        for(Extension extension: prefilteredExtensions) {
            if(extension.containsAll(unattackedArguments)) {
                prelimRCFExtensions.add(extension);
            }
        }
        Collection<Extension> ranges = new LinkedList<>();
        for(Extension prelimRCFExtension: prelimRCFExtensions) {
            Extension range = new Extension();
            range.addAll(prelimRCFExtension);
            for(Argument afArgument: bbase.getNodes()) {
                for(Argument extArgument: prelimRCFExtension) {
                    if(bbase.isAttackedBy(afArgument, extArgument) && !bbase.isAttackedBy(extArgument, afArgument)) {
                        range.add(afArgument);
                    }
                }
            }
            ranges.add(range);
        }
        Collection<Extension> rcfExtensions = new LinkedList<>();
        int index = 0;
        for(Extension rangeI: ranges) {
            boolean isIn = true;
            for(Extension rangeJ: ranges) {
                if(rangeJ.containsAll(rangeI) && rangeJ.size() > rangeI.size()) {
                    isIn = false;
                }
            }
            if (isIn) {
                rcfExtensions.add(((LinkedList<Extension>) prelimRCFExtensions).get(index));
            }
            index++;
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
