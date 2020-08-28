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
public class SimpleNSACF2Reasoner extends AbstractExtensionReasoner{

    private SimpleConflictFreeReasoner cfReasoner = new SimpleConflictFreeReasoner();

    /* (non-Javadoc)
     * @see net.sf.tweety.arg.dung.reasoner.AbstractExtensionReasoner#getModels(net.sf.tweety.arg.dung.syntax.DungTheory)
     */
    @Override
    public Collection<Extension> getModels(DungTheory bbase) {
        bbase = Utils.removeSelfAttackedArguments(bbase);
        Collection<Extension> initialExtensions = cfReasoner.getModels(bbase);
        Collection<Extension> prefilteredExtensions = new ArrayList<>();
        prefilteredExtensions.addAll(initialExtensions);
        for(Extension iExtension: initialExtensions) {
            for(Extension jExtension: initialExtensions) {
                boolean isGreater = jExtension.containsAll(iExtension)
                        && jExtension.size() > iExtension.size();
                if(isGreater) {
                    prefilteredExtensions.remove(iExtension);
                }
            }
        }
        ArrayList<Extension> rsExtensions = new ArrayList<>();
        ArrayList<Collection<Argument>> reachablyDefendedRanges = new ArrayList<>();
        for(Extension extension: prefilteredExtensions) {
            Collection<Argument> reachableRange = Utils.determineReachableRange(extension, bbase);
            boolean isMaxReachableRange = reachableRange.size() == bbase.getNodes().size();
            if(isMaxReachableRange) {
                rsExtensions.add(extension);
                reachablyDefendedRanges.add(Utils.reachablyDefendedRanges(extension, bbase));
            }
        }
        ArrayList<Extension> toBeRemovedExtensions = new ArrayList<>();
        int indexI = 0;
        for(Extension extension: rsExtensions) {
            boolean toBeRemoved = false;
            Collection<Argument> rangeI = reachablyDefendedRanges.get(indexI);
            for(Collection<Argument> rangeJ: reachablyDefendedRanges) {
                boolean isGreater = rangeJ.containsAll(rangeI) && rangeJ.size() > rangeI.size();
                if(isGreater) {
                    toBeRemoved = true;
                    break;
                }
            }
            if(toBeRemoved) {
                toBeRemovedExtensions.add(extension);
            }
            indexI++;
        }
        rsExtensions.removeAll(toBeRemovedExtensions);
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
