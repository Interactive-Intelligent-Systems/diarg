package diarg.explanations;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;


/**
 * Contains example argumentation frameworks testing monotony violation explanations
 * framework1: ({a, b, c, d}, {(a, b), (b, a)})
 * framework2: ({a, b, c, d, e, f, g}, {(a, b), (b, a), (e, c), (e, d), (e, f), (f, a), (f, e), (f, g), (g, f)})
 * framework3: ({a, b, c}, {(a, b), (b, a)})
 * framework4: ({a, b, c, d}, {(a, b), (b, a), (d, c)})
 * framework5: ({a, b, c, d, e}, {(a, b), (b, a), (b, e), (e, c), (e, d)})
 * framework6: ({a, b, c}, {(c, b), (b, a)})
 * @author Timotheus Kampik
 */
public final class ExplanationTestFrameworks {

    public DungTheory framework1 = new DungTheory();
    public DungTheory framework2 = new DungTheory();
    public DungTheory framework3 = new DungTheory();
    public DungTheory framework4 = new DungTheory();
    public DungTheory framework5 = new DungTheory();
    public DungTheory framework6 = new DungTheory();

    public ExplanationTestFrameworks() {
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");
        Argument g = new Argument("g");

        framework1.add(a);
        framework1.add(b);
        framework1.add(c);
        framework1.add(d);
        framework1.add(new Attack(a, b));
        framework1.add(new Attack(b, a));

        framework2.add(a);
        framework2.add(b);
        framework2.add(c);
        framework2.add(d);
        framework2.add(e);
        framework2.add(f);
        framework2.add(g);
        framework2.add(new Attack(a, b));
        framework2.add(new Attack(b, a));
        framework2.add(new Attack(e, c));
        framework2.add(new Attack(e, d));
        framework2.add(new Attack(e, f));
        framework2.add(new Attack(f, a));
        framework2.add(new Attack(f, e));
        framework2.add(new Attack(f, g));
        framework2.add(new Attack(g, f));

        framework3.add(a);
        framework3.add(b);
        framework3.add(c);
        framework3.add(new Attack(a, b));
        framework3.add(new Attack(b, a));

        framework4.add(a);
        framework4.add(b);
        framework4.add(c);
        framework4.add(d);
        framework4.add(new Attack(a, b));
        framework4.add(new Attack(b, a));
        framework4.add(new Attack(d, c));

        framework5.add(a);
        framework5.add(b);
        framework5.add(c);
        framework5.add(d);
        framework5.add(e);
        framework5.add(new Attack(a, b));
        framework5.add(new Attack(b, a));
        framework5.add(new Attack(b, e));
        framework5.add(new Attack(e, c));
        framework5.add(new Attack(e, d));

        framework6.add(a);
        framework6.add(b);
        framework6.add(c);
        framework6.add(new Attack(c, b));
        framework6.add(new Attack(b, a));
    }
}
