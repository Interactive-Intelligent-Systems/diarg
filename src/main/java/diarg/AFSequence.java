package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SequenceType;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Class for generating and managing argumentation sequence objects
 * @author Timotheus Kampik
 */
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

    /**
     * Adds an argumentation framework to the sequence; only adds the framework if it is compliant with the configured
     * sequence type.
     * @param framework The argumentation framework that is to be added to the sequence
     * @return {@code true} if the framework was successfully added; else {@code false}.
     */
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
        try {
            throw(new Exception(
                    "Could not add framework, because this would infringe compliance with " +
                        this.sequenceType + " sequence."
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes framework at the specified index; in case of reference independent and cautiously monotonic resolution
     * approaches, the last framework in the sequence cannot be removed to ensure principle compliance.
     * @param index The index of the argumentation framework that is to be removed from the sequence.
     * @return {@code true} if the framework was successfully removed; else {@code false}.
     */
    public boolean removeFramework(int index) {
        boolean isStandard = this.resolutionType == ResolutionType.STANDARD;
        if(index != this.frameworks.size()-1 || isStandard) {
            this.frameworks.remove(index);
            if(this.resolutions.size() > index) {
                this.resolutions.remove(index);
            }
            return true;
        }
        try {
            throw(new Exception(
                    "Could not remove framework, because this would infringe compliance with " +
                            this.sequenceType + " sequence."
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Resolves an argumentation framework in the sequence using the specified semantics and, if configured, the
     * specified approach to ensure reference independence or cautious monotony.
     * @param index The index of the to-be-resolve framework
     * @return The resolution (i.e. one extension) of the framework
     */
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
                return null;
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
        Extension resolution = this.semantics.getModel(framework);
        Extension annihilatorArguments = new Extension();
        for(Argument argument: resolution) {
            if(!this.frameworks.get(index).contains(argument)) {
                annihilatorArguments.add(argument);
            }
        }
        resolution.removeAll(annihilatorArguments);
        this.resolutions.add(resolution);
        return resolution;
    }

    /**
     * Resolves all frameworks of the sequence by calling the {@code resolveFramework} method for each index in the
     * list of frameworks.
     * @return The resolutions of all frameworks.
     */
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

    /**
     * Resolves a framework as the provided extension, if this extension is a valid resolution.
     * @param index The index of the framework in the sequence.
     * @param extension The extension as which the framework should be resolved.
     * @return the provided extension if it is a valid resolution; else {@code null}.
     */
    public Extension resolveFramework(int index, Extension extension) {
        DungTheory previousFramework;
        Extension previousResolution;
        if(index == 0) {
            previousFramework = new DungTheory();
            previousResolution = new Extension();
        } else {
            previousFramework = this.frameworks.get(index-1);
            previousResolution = this.resolutions.get(index-1);
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
                        sequenceLink.determineSmallestNormalCMExpansions(this.semantics, previousResolution);
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case REDUCTIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineLargestNormalCMSubmodules(this.semantics, previousResolution);
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case EXPANSIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineSmallestNormalRIExpansions(this.semantics, previousResolution);
                for(DungTheory framework: frameworks) {
                    if(this.semantics.getModel(framework).containsAll(extension)){
                        isResolution = true;
                    }
                }
                break;
            case REDUCTIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineLargestNormalRISubmodules(this.semantics, previousResolution);
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
        resolutions.add(index, extension);
        return extension;
    }

    /**
     * Determines all extensions of framework at the specified index, given a standard resolution type (the other
     * resolution types require determining expansions or submodules.
     * @param index index of the framework in the sequence
     * @return The framework's extensions if resolution type is {@code STANDARD}; else {@code null}.
     */
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

    /**
     * For the framework at the specified index, determines all expansions or submodules that can be resolved to a
     * reference independent or cautiously monotonic extension.
     * @param index index of the framework in the sequence
     * @return The resolvable frameworks if resolution type is not {@code STANDARD}; else {@code null}.
     */
    public Collection<DungTheory> determineResolvableFrameworks(int index) {
        DungTheory previousFramework;
        Extension previousResolution;
        if(index == 0) {
            previousFramework = new DungTheory();
            previousResolution = new Extension();
        } else {
            previousFramework = this.frameworks.get(index-1);
            try {
                previousResolution = this.resolutions.get(index-1);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        AFTuple sequenceLink  = new AFTuple(previousFramework, this.frameworks.get(index));
        Collection<DungTheory> frameworks;
        switch(this.resolutionType) {
            case EXPANSIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineSmallestNormalCMExpansions(this.semantics, previousResolution);
                break;
            case REDUCTIONIST_CAUTIOUSLY_MONOTONIC:
                frameworks =
                        sequenceLink.determineLargestNormalCMSubmodules(this.semantics, previousResolution);
                break;
            case EXPANSIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineSmallestNormalRIExpansions(this.semantics, previousResolution);
                break;
            case REDUCTIONIST_REFERENCE_INDEPENDENT:
                frameworks =
                        sequenceLink.determineLargestNormalRISubmodules(this.semantics, previousResolution);
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
