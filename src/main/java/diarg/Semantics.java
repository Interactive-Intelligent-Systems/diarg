package diarg;

import net.sf.tweety.arg.dung.reasoner.*;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

public class Semantics {

    public SemanticsType semanticsType;

    public Semantics(SemanticsType semanticsType) {
        this.semanticsType = semanticsType;
    }

    public Collection<Extension> getModels(DungTheory bbase) {
        switch (this.semanticsType) {
            case CF2:
                SimpleCF2Reasoner cf2Reasoner = new SimpleCF2Reasoner();
                return cf2Reasoner.getModels(bbase);
            case COMPLETE:
                SimpleCompleteReasoner completeReasoner = new SimpleCompleteReasoner();
                return completeReasoner.getModels(bbase);
            case GROUNDED:
                SimpleGroundedReasoner groundedReasoner = new SimpleGroundedReasoner();
                return groundedReasoner.getModels(bbase);
            case IDEAL:
                SimpleIdealReasoner idealReasoner = new SimpleIdealReasoner();
                return idealReasoner.getModels(bbase);
            case PREFERRED:
                SimplePreferredReasoner preferredReasoner = new SimplePreferredReasoner();
                return preferredReasoner.getModels(bbase);
            case RCF:
                SimpleRCFReasoner rcfReasoner = new SimpleRCFReasoner();
                return rcfReasoner.getModels(bbase);
            case RS:
                SimpleRSReasoner rsReasoner = new SimpleRSReasoner();
                return rsReasoner.getModels(bbase);
            default:
            case STAGE:
                SimpleStageReasoner stageReasoner = new SimpleStageReasoner();
                return stageReasoner.getModels(bbase);
        }
    }

    public Extension getModel(DungTheory bbase) {
        Collection<Extension> extensions = this.getModels(bbase);
        return extensions.iterator().next();
    }
}
