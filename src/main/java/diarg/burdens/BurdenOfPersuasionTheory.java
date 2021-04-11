package diarg.burdens;

import diarg.Semantics;
import diarg.Utils;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;

public class BurdenOfPersuasionTheory {

    /**
     * Gets the arguments of a burden of persuasion theory (BPF)
     * @return The BPF's arguments
     */
    public Collection<Argument> getArguments() {
        return arguments;
    }

    /**
     * Gets the argument oder of a burden of persuasion theory (BPF)
     * @return The BPF's argument order
     */
    public ArrayList<Collection<Argument>> getArgumentOrder() {
        return argumentOrder;
    }

    /**
     *  Gets the attacks of a burden of persuasion theory (BPF)
     * @return The BPF's attacks
     */
    public Collection<Attack> getAttacks() {
        return attacks;
    }

    private Collection<Argument> arguments = new ArrayList<>();
    private ArrayList<Collection<Argument>> argumentOrder;
    private Collection<Attack> attacks;

    /**
     * Instantiates a burden of persuasion theory (BPF)
     * @param argumentOrder The BPF's argument order: a sequence (ArrayList) of sets of arguments. Each argument must be
     *                      in only of of the sequence's sets
     * @param attacks       A binary relation on all arguments of the BPF
     */
    public BurdenOfPersuasionTheory(ArrayList<Collection<Argument>> argumentOrder, Collection<Attack> attacks) {
        for(Collection<Argument> orderArguments: argumentOrder) {
            for(Argument argument: orderArguments) {
                if(arguments.contains(argument)) {
                    try {
                        throw new Exception("At least on argument is contained by different burden levels" +
                                "This may cause problems when drawing inferences from this burden of persuasion " +
                                "framework.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
            arguments.addAll(orderArguments);
        }
        this.argumentOrder = argumentOrder;
        this.attacks = attacks;
    }

    /**
     * Determines all extensions of the BPF
     * @param semantics The argumentation semantics that is to be applied
     * @return The extensions that the argumentation semantics yields for the BPF
     */
    public Collection<Extension> determineExtensions(Semantics semantics) {
        ArrayList<DungTheory> argumentationFrameworks = new ArrayList<>();
        DungTheory baseFramework = new DungTheory();
        baseFramework.addAll(arguments);
        baseFramework.addAllAttacks(attacks);
        int i = 0;
        for(Collection<Argument> burdenArguments: argumentOrder) {
            DungTheory burdenFramework;
            if(i == 0) {
                burdenFramework = (DungTheory) baseFramework.getRestriction(burdenArguments);
            } else {
                Collection<Argument> union = new ArrayList<>();
                union.addAll(burdenArguments);
                union.addAll(argumentationFrameworks.get(i-1).getNodes());
                burdenFramework = (DungTheory) baseFramework.getRestriction(union);
            }
            argumentationFrameworks.add(burdenFramework);
            i++;
        }
        int lastIndex = argumentationFrameworks.size() - 1;
        Collection<Extension> extensions =
                semantics.getModels(argumentationFrameworks.get(lastIndex));
        int j = 0;
        Collection<Extension> previousExtensions = new ArrayList<>();
        for(DungTheory argumentationFramework: argumentationFrameworks) {
            if(j == lastIndex) break;
            Collection<Extension> burdenExtensions = semantics.getModels(argumentationFramework);
            previousExtensions.addAll(burdenExtensions);
            extensions = Utils.determinePOMaxMonotonicExtensions(extensions, previousExtensions);
        }
        return extensions;
    }

    /**
     * Checks if a set of arguments is entailed by every extension of the BPF
     * Note: non-optimized: determines all extensions first
     * @param arguments The set of arguments whose status is to be assessed
     * @param semantics The semantics that is to be applied
     * @return The credulous acceptability status of the set of arguments
     */
    public boolean isSkepticallyAccepted(Collection<Argument> arguments, Semantics semantics) {
        Collection<Extension> extensions = determineExtensions(semantics);
        for(Extension extension: extensions) {
            if(!extension.containsAll(arguments)) return false;
        }
        return true;
    }

    /**
     * Checks if a set of arguments is entailed by at least one extension of the BPF
     * Note: non-optimized: determines all extensions first
     * @param arguments The set of arguments whose status is to be assessed
     * @param semantics The semantics that is to be applied
     * @return The skeptical acceptability status of the set of arguments
     */
    public boolean isCredulouslyAccepted(Collection<Argument> arguments, Semantics semantics) {
        Collection<Extension> extensions = determineExtensions(semantics);
        for(Extension extension: extensions) {
            if(extension.containsAll(arguments)) return true;
        }
        return false;
    }
}
