package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SequenceType;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class AFSequence {

    private ArrayList<DungTheory> frameworks = new ArrayList<>();
    private ArrayList<Extension> resolutions = new ArrayList<>();
    private SequenceType sequenceType;
    private ResolutionType resolutionType;
    private Semantics semantics;

    public AFSequence(SequenceType sequenceType, ResolutionType resolutionType,
                      Semantics semantics) {
        this.sequenceType = sequenceType;
        this.resolutionType = resolutionType;
        this.semantics = semantics;
    }


    public Semantics getSemantics() {
        return this.semantics;
    }

    public ArrayList<DungTheory> getFrameworks() {
        return this.frameworks;
    }

    public ArrayList<Extension> getResolutions() {
        return this.resolutions;
    }

    public DungTheory getFramework(int index) {
        return this.frameworks.get(index);
    }

    public Extension getResolution(int index) {
        return this.resolutions.get(index);
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public ResolutionType getResolutionType() {
        return this.resolutionType;
    }
    public SelectionType getSelectionType() {
        return this.selectionType;
    }

    public boolean addFramework(DungTheory framework) {
        if(this.frameworks.size() == 0 || this.sequenceType == SequenceType.STANDARD) {
            this.frameworks.add(framework);
            return true;
        }
        AFTuple sequenceLink = new AFTuple(this.frameworks.get(this.frameworks.size() - 1), framework);
        if(this.sequenceType == SequenceType.EXPANDING && sequenceLink.isExpansion()) {
            this.frameworks.add(framework);
            return true;
        }
        if(this.sequenceType == SequenceType.NORMALLY_EXPANDING && sequenceLink.isNormalExpansion()) {
            this.frameworks.add(framework);
            return true;
        }
        return false;
    }

    public boolean removeFramework(int index) {
        boolean isStandard = this.resolutionType == ResolutionType.STANDARD;
        if(frameworks.size() == 1 || index == this.frameworks.size()-1 || isStandard) {
            this.frameworks.remove(index);
            if(this.resolutions.size() > index) {
                this.resolutions.remove(index);
            }
            return true;
        }
        return false;

    }

    public Extension resolveFramework(int index) {
        if(this.resolutions.size() > index) {
            return this.resolutions.get(index);
        }
        AFTuple sequenceLink;
        Extension previousResolution = new Extension();
        if(index != 0) {
            try {
                sequenceLink = new AFTuple(this.frameworks.get(index-1), this.frameworks.get(index));
                previousResolution = this.resolutions.get(index-1);
            } catch (Exception e) {
                new Exception("Previous framework in sequence has not been resolved.").printStackTrace();
                return new Extension();
            }
        } else {
            sequenceLink = new AFTuple(new DungTheory(), this.frameworks.get(index));
        }
        Collection<DungTheory> frameworks;
        DungTheory framework;
        switch (this.resolutionType) {
            case EXPANSIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks = sequenceLink.determineSmallestNormalCMExpansions(this.semantics, previousResolution);
                break;
            case REDUCTIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks = sequenceLink.determineLargestNormalCMSubmodules(this.semantics, previousResolution);
                break;
            case EXPANSIONIST_REFERENCE_INDEPENDENT:
                frameworks = sequenceLink.determineSmallestNormalRIExpansions(this.semantics, previousResolution);
                break;
            case REDUCTIONIST_REFERENCE_INDEPENDENT:
                frameworks = sequenceLink.determineLargestNormalRISubmodules(this.semantics, previousResolution);
                break;
            case STANDARD:
            default:
                frameworks = new LinkedList<>();
                frameworks.add(this.frameworks.get(index));

        }
        framework = frameworks.iterator().next();
        return this.semantics.getModel(framework);
    }

    public Collection<Extension> resolveFrameworks() {
        Collection<Extension> extensions = new LinkedList<>();
        if(this.frameworks.size() == this.resolutions.size()) {
            return this.resolutions;
        }
        for(int i = 0; i < this.frameworks.size(); i++) {
            extensions.add(this.resolveFramework(i));
        }
        return extensions;
    }

    public Extension resolveFramework(int index, Extension extension) {
        DungTheory previousFramework;
        if(index == 0) {
            previousFramework = new DungTheory();
        } else {
            previousFramework = this.frameworks.get(index-1);
        }
        AFTuple sequenceLink  = new AFTuple(previousFramework, this.frameworks.get(index));
        if(this.resolutions.size() > index && this.resolutions.get(index).containsAll(extension)){
            return extension;
        }
        boolean isResolution = false;
        Collection<DungTheory> frameworks;
        switch(this.resolutionType) {
            case EXPANSIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineSmallestNormalCMExpansions(this.semantics, this.resolutions.get(index-1));
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case REDUCTIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineLargestNormalCMSubmodules(this.semantics, this.resolutions.get(index-1));
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case EXPANSIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineSmallestNormalRIExpansions(this.semantics, this.resolutions.get(index-1));
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case REDUCTIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineLargestNormalRISubmodules(this.semantics, this.resolutions.get(index-1));
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case STANDARD:
            default:
                if(this.semantics.getModels(this.frameworks.get(index)).contains(extension)) {
                    isResolution = true;
                }
        }
        if(!isResolution) {
            try {
                throw(new Exception("The provided resolution is not valid."));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        resolutions.set(index, extension);
        return extension;
    }

    public Collection<Extension> determineExtensions(int index) {
        if(this.resolutionType == ResolutionType.STANDARD) {
            return this.semantics.getModels(this.frameworks.get(index));
        } else {
            try {
                throw(new Exception(
                        "The configured resolution type requires determining resolvable frameworks"+
                                "(expansions or submodules) instead"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Collection<DungTheory> determineResolvableFrameworks(int index) {
        DungTheory previousFramework;
        if(index == 0) {
            previousFramework = new DungTheory();
        } else {
            previousFramework = this.frameworks.get(index-1);
        }
        AFTuple sequenceLink  = new AFTuple(previousFramework, this.frameworks.get(index));
        Collection<DungTheory> frameworks;
        switch(this.resolutionType) {
            case EXPANSIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineSmallestNormalCMExpansions(this.semantics, this.resolutions.get(index-1));
                break;
            case REDUCTIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineLargestNormalCMSubmodules(this.semantics, this.resolutions.get(index-1));
                break;
            case EXPANSIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineSmallestNormalRIExpansions(this.semantics, this.resolutions.get(index-1));
                break;
            case REDUCTIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineLargestNormalRISubmodules(this.semantics, this.resolutions.get(index-1));
                break;
            default:
            case STANDARD:
                try {
                    throw(new Exception(
                            "The configured resolution type requires determining resolvable frameworks"+
                                    "(expansions or submodules) instead"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
        }
        return frameworks;
    }
}
