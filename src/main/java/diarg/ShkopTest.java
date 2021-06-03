package diarg;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Provides abstract Shkop test facilities for DiArg's Shkop reasoner
 * @author Timotheus Kampik
 */
abstract class ShkopTest {
    /**
     * Takes an argumentation framework and an argument, and determines whether the argument passes the Shkop test
     * @param framework Argumentation framework for the test
     * @param extension Extension for the test
     * @param argument Newly added argument for the test
     * @return Pass or failure of the test
     */
    abstract boolean run(DungTheory framework, Extension extension, Argument argument);
}
