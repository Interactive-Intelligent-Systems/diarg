package diarg;

import java.util.*;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

public class Utils {

    public static DungTheory removeSelfAttackedArguments(DungTheory bbase) {
        Collection<Argument> arguments = bbase.getNodes();
        Collection<Argument> selfAttackedArguments = new LinkedList<>();
        for (Argument argument : arguments) {
            if (bbase.isAttackedBy(argument, argument)) {
                selfAttackedArguments.add(argument);
            }
        }
        bbase.removeAll(selfAttackedArguments);
        return bbase;
    }

    public static Collection<Argument> determineUnattackedArguments(DungTheory bbase) {
        Collection<Argument> unattackedArguments = new LinkedList<>();
        for(Argument argument: bbase.getNodes()) {
            if(bbase.getAttackers(argument).isEmpty()) {
                unattackedArguments.add(argument);
            }
        }
        return unattackedArguments;
    }
}

