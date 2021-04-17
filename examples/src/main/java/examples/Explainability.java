package examples;

import diarg.AFTuple;
import diarg.Semantics;
import diarg.Utils;
import diarg.enums.SemanticsType;
import diarg.explanations.NonmonotonyExplainer;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.Collection;

public class Explainability {
    /**
     * Example code: explaining monotony violations
     */
    public static void main(String[] args){
        // AF
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        DungTheory framework1 = new DungTheory();
        framework1.add(a);
        framework1.add(b);
        framework1.add(c);
        framework1.add(d);
        framework1.add(new Attack(a, b));
        framework1.add(new Attack(b, a));
        System.out.println("AF: " + framework1);
        // Determine preferred extensions of AF
        Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
        System.out.println("All preferred extensions of AF: " + preferredSemantics.getModels(framework1));
        Extension af1Extension = preferredSemantics.getModel(framework1);
        System.out.println("Preferred extension selected from AF: " + af1Extension);
        // Expand AF to AF'
        Argument e = new Argument("e");
        Argument f = new Argument("f");
        Argument g = new Argument("g");
        DungTheory framework2 = new DungTheory();
        framework2.addAll(framework1);
        framework2.add(e);
        framework2.add(f);
        framework2.add(g);
        framework2.add(new Attack(e, c));
        framework2.add(new Attack(e, d));
        framework2.add(new Attack(e, f));
        framework2.add(new Attack(f, a));
        framework2.add(new Attack(f, e));
        framework2.add(new Attack(f, g));
        framework2.add(new Attack(g, f));
        System.out.println("AF': " + framework2);
        // Determine preferred extensions of AF'
        Collection<Extension> af2Extensions = preferredSemantics.getModels(framework2);
        System.out.println("All preferred extensions of AF': " + af2Extensions);
        // Determine maximal monotony extensions
        Collection<Extension> maxMonExtensions = Utils.determineCardinalityMaxMonotonicExtensions(af2Extensions, af1Extension);
        System.out.println("Maximally monotonic extensions: " + maxMonExtensions);
        Extension maxMonExtension = maxMonExtensions.iterator().next();
        // Generate explanations
        AFTuple afTuple = new AFTuple(framework1, framework2);
        Collection<Argument> explanations = NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple, af1Extension, maxMonExtension);
        System.out.println("Monotony violation explanations: " + explanations);
    }
}
