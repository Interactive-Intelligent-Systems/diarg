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
        Collection<Argument> arguments = bbase.getNodes();
        for(Argument argument: arguments) {
            if(bbase.isAttackedBy(argument, argument)) {
                bbase.remove(argument);
            }
        }
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
        ArrayList<Collection<Argument>> reachableDefenses = new ArrayList<>();
        for(Extension extension: prefilteredExtensions) {
            Collection<Argument> reachableRange = Utils.determineReachableRange(extension, bbase);
            boolean isMaxReachableRange = reachableRange.size() == bbase.getNodes().size();
            if(isMaxReachableRange) {
                rsExtensions.add(extension);
                reachableDefenses.add(Utils.determineReachableDefense(extension, bbase));
            }
        }
        ArrayList<Extension> toBeRemovedExtensions = new ArrayList<>();
        int index = 0;
        for(Extension extension: rsExtensions) {
            boolean toBeRemoved = false;
            for(Collection<Argument> reachableDefense: reachableDefenses) {
                boolean isGreater = reachableDefense.containsAll(reachableDefenses.get(index))
                        && reachableDefense.size() > reachableDefenses.get(index).size();
                if(isGreater) {
                    toBeRemoved = true;
                    break;
                }
            }
            if(toBeRemoved) {
                toBeRemovedExtensions.add(extension);
            }
            index++;
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
