package diarg.explanations;

import diarg.AFTuple;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NonmonotonyExplainerTest {
    ExplanationTestFrameworks testFrameworks = new ExplanationTestFrameworks();
    Argument a = new Argument("a");
    Argument b = new Argument("b");
    Argument c = new Argument("c");
    Argument d = new Argument("d");
    Argument e = new Argument("e");
    Argument f = new Argument("f");

    @Test
    void determineMonotonyViolationExplanations() {
        DungTheory oneArgFramework = new DungTheory();
        oneArgFramework.add(a);
        AFTuple afTuple1 = new AFTuple(testFrameworks.framework1, testFrameworks.framework2);
        AFTuple afTuple2 = new AFTuple(testFrameworks.framework3, testFrameworks.framework4);
        AFTuple afTuple3 = new AFTuple(testFrameworks.framework1, testFrameworks.framework5);
        AFTuple afTuple5 = new AFTuple(oneArgFramework, testFrameworks.framework6);

        Extension baseExtension1 = new Extension();
        baseExtension1.add(a);
        baseExtension1.add(b);
        baseExtension1.add(c);
        Extension targetExtension1 = new Extension();
        targetExtension1.add(b);
        targetExtension1.add(c);
        targetExtension1.add(d);
        targetExtension1.add(f);
        Collection<Argument> expectedExplanations1 = new ArrayList<>();
        expectedExplanations1.add(e);
        expectedExplanations1.add(f);
        Collection<Argument> actualExplanations1 =
                NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple1, baseExtension1, targetExtension1);
        assertEquals(2, actualExplanations1.size());
        assertTrue(actualExplanations1.containsAll(expectedExplanations1));

        Extension baseExtension2 = new Extension();
        baseExtension2.add(b);
        baseExtension2.add(c);
        Extension targetExtension2 = new Extension();
        targetExtension2.add(b);
        targetExtension2.add(d);
        Collection<Argument> expectedExplanations2 = new ArrayList<>();
        expectedExplanations2.add(d);
        Collection<Argument> actualExplanations2 =
                NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple2, baseExtension2, targetExtension2);
        assertEquals(1, actualExplanations2.size());
        assertTrue(actualExplanations2.containsAll(expectedExplanations2));

        Extension baseExtension3 = new Extension();
        baseExtension3.add(a);
        baseExtension3.add(d);
        baseExtension3.add(c);
        Extension targetExtension3 = new Extension();
        targetExtension3.add(b);
        targetExtension3.add(d);
        targetExtension3.add(c);
        Collection<Argument> expectedExplanations3 = new ArrayList<>();
        expectedExplanations3.add(e);
        Collection<Argument> actualExplanations3 =
                NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple3, baseExtension3, targetExtension3);
        assertEquals(1, actualExplanations3.size());
        assertTrue(actualExplanations3.containsAll(expectedExplanations3));

        DungTheory framework0 = new DungTheory();
        AFTuple afTuple4 = new AFTuple(framework0, testFrameworks.framework1);
        framework0.add(a);
        Extension baseExtension4 = new Extension();
        baseExtension4.add(a);
        Extension targetExtension4 = new Extension();
        targetExtension4.add(a);
        targetExtension4.add(c);
        targetExtension4.add(d);
        Collection<Argument> actualExplanations4 =
                NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple4, baseExtension4, targetExtension4);
        assertEquals(0, actualExplanations4.size());

        Extension baseExtension5 = new Extension();
        baseExtension5.add(a);
        Extension targetExtension5 = new Extension();
        targetExtension5.add(a);
        targetExtension5.add(c);
        Collection<Argument> actualExplanations5 =
                NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple5, baseExtension5, targetExtension5);
        assertEquals(0, actualExplanations5.size());
    }
}
