package diarg;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Provides abstract Shkop test facilities for DiArg's Shkop reasoner
 * @author Timotheus Kampik
 */
abstract class ShkopTest {
    /**
     * Takes a framework and an argument, and determines whether the argument passes the Shkop test
     * @param framework
     * @param argument
     * @return
     */
    abstract boolean run(DungTheory framework, Argument argument);
}
