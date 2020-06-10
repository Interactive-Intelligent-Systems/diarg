package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

/**
 * Utils for manipulating and analyzing argumentation frameworks
 * @author Timotheus Kampik
 */
public class Utils {

    /**
     * Removes all self-attacking arguments from an argumentation framework.
     * @param framework Argumentation framework, from which all self-attacking arguments should be removed.
     * @return Argumentation framework without the self-attacking arguments
     */
    public static DungTheory removeSelfAttackedArguments(DungTheory framework) {
        Collection<Argument> arguments = framework.getNodes();
        Collection<Argument> selfAttackedArguments = new LinkedList<>();
        for (Argument argument : arguments) {
            if (framework.isAttackedBy(argument, argument)) {
                selfAttackedArguments.add(argument);
            }
        }
        framework.removeAll(selfAttackedArguments);
        return framework;
    }

    /**
     * Determines all unattacked arguments of an argumentation framework.
     * @param framework The argumentation framework, for which all unattacked arguments should be determined.
     * @return All unattacked arguments in the provided argumentation framework
     */
    public static Collection<Argument> determineUnattackedArguments(DungTheory framework) {
        Collection<Argument> unattackedArguments = new LinkedList<>();
        for(Argument argument: framework.getNodes()) {
            if(framework.getAttackers(argument).isEmpty()) {
                unattackedArguments.add(argument);
            }
        }
        return unattackedArguments;
    }
}

