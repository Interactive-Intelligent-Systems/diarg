package diarg;

import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.reasoner.*;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

/**
 * Provides an interface to different abstract argumentation semantics
 * @author Timotheus Kampik
 */
public class Semantics {

    public SemanticsType semanticsType;

    public Semantics(SemanticsType semanticsType) {
        this.semanticsType = semanticsType;
    }

    /**
     * Determines semantics' extensions for the specified argumentation framework.
     * @param framework The argumentation framework, for which the extensions should be returned
     * @return The argumentation framework's extension
     */
    public Collection<Extension> getModels(DungTheory framework) {
        switch (this.semanticsType) {
            case CF2:
                SccCF2Reasoner cf2Reasoner = new SccCF2Reasoner();
                return cf2Reasoner.getModels(framework);
            case COMPLETE:
                SimpleCompleteReasoner completeReasoner = new SimpleCompleteReasoner();
                return completeReasoner.getModels(framework);
            case GROUNDED:
                SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
                return groundedReasoner.getModels(framework);
            case IDEAL:
                SimpleIdealReasoner idealReasoner = new SimpleIdealReasoner();
                return idealReasoner.getModels(framework);
            case PREFERRED:
                SimplePreferredReasoner preferredReasoner = new SimplePreferredReasoner();
                return preferredReasoner.getModels(framework);
            case NSACF2:
                SimpleNSACF2Reasoner rcfReasoner = new SimpleNSACF2Reasoner();
                return rcfReasoner.getModels(framework);
            case RS:
                SimpleRSReasoner rsReasoner = new SimpleRSReasoner();
                return rsReasoner.getModels(framework);
            case SCF2:
                SCF2Reasoner scf2Reasoner = new SCF2Reasoner();
                return scf2Reasoner.getModels(framework);
            case SHKOP:
                SimpleShkopReasoner shkopReasoner = new SimpleShkopReasoner();
                return shkopReasoner.getModels(framework);
            default:
            case STAGE:
                SimpleStageReasoner stageReasoner = new SimpleStageReasoner();
                return stageReasoner.getModels(framework);
        }
    }

    /**
     * Returns one extension the semantics returns for the specified argumentation framework.
     * @param framework The argumentation framework for which the extension should be determined
     * @return An extension of the argumentation framework
     */
    public Extension getModel(DungTheory framework) {
        Collection<Extension> extensions = this.getModels(framework);
        return extensions.iterator().next();
    }
}
