package diarg;

import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;

/**
 * Contains Tweety models of all examples of the following book chapter of the Handbook of Formal Argumentation:
 * Baroni, Pietro, Martin Caminada, and Massimiliano Giacomin. "Abstract argumentation frameworks and their semantics."
 * Handbook of Formal Argumentation 1 (2018): 157-234.
 * framework1: ({a, b, c}, {(a, b), (b, c)})
 * framework2: ({a, b}, {(a, b), (b, a)})
 * framework3: ({a, b, c, d}, {(a, b), (b, c), (c, d), (d, c)})
 * framework4: ({a, b, c, d}, {(a, b), (b, a), (a, c), (b, c), (c, d)})
 * framework5: ({a, b, c}, {(a, b), (b, c), (c, a)})
 * framework6: ({a, b, c, d, e}, {(a, b), (b, a), (b, c), (c, d), (d, e), (e, c)})
 * framework7: ({a, b, c}, {(a, b), (b, a), (b, c), (c, c)})
 * framework8: ({a, b}, {(a, a), (a, b)})
 * framework9: ({a, b, c}, {(a, b), (b, c), (c, c)})
 * From: van der Torre, Leon, and Srdjan Vesic. "The principle-based approach to abstract argumentation semantics."
 * IfCoLog Journal of Logics and Their Applications (2017).
 * framework10: ({a, b, c, d, e, f}, {(b, a), (c, a), (d, c), (e, b), (e, d), (f, c)})
 * @author Timotheus Kampik
 */
public class HandbookTestFrameworks {
    DungTheory framework1 = new DungTheory();
    DungTheory framework2 = new DungTheory();
    DungTheory framework3 = new DungTheory();
    DungTheory framework4 = new DungTheory();
    DungTheory framework5 = new DungTheory();
    DungTheory framework6 = new DungTheory();
    DungTheory framework7 = new DungTheory();
    DungTheory framework8 = new DungTheory();
    DungTheory framework9 = new DungTheory();
    DungTheory framework10 = new DungTheory();

    public HandbookTestFrameworks() {
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");

        framework1.add(a);
        framework1.add(b);
        framework1.add(c);
        framework1.add(new Attack(a, b));
        framework1.add(new Attack(b, c));

        framework2.add(a);
        framework2.add(b);
        framework2.add(new Attack(a, b));
        framework2.add(new Attack(b, a));

        framework3.add(a);
        framework3.add(b);
        framework3.add(c);
        framework3.add(d);
        framework3.add(new Attack(a, b));
        framework3.add(new Attack(b, c));
        framework3.add(new Attack(c, d));
        framework3.add(new Attack(d, c));

        framework4.add(a);
        framework4.add(b);
        framework4.add(c);
        framework4.add(d);
        framework4.add(new Attack(a, b));
        framework4.add(new Attack(b, a));
        framework4.add(new Attack(a, c));
        framework4.add(new Attack(b, c));
        framework4.add(new Attack(c, d));

        framework5.add(a);
        framework5.add(b);
        framework5.add(c);
        framework5.add(new Attack(a, b));
        framework5.add(new Attack(b, c));
        framework5.add(new Attack(c, a));

        framework6.add(a);
        framework6.add(b);
        framework6.add(c);
        framework6.add(d);
        framework6.add(e);
        framework6.add(new Attack(a, b));
        framework6.add(new Attack(b, a));
        framework6.add(new Attack(b, c));
        framework6.add(new Attack(c, d));
        framework6.add(new Attack(d, e));
        framework6.add(new Attack(e, c));

        framework7.add(a);
        framework7.add(b);
        framework7.add(c);
        framework7.add(new Attack(a, b));
        framework7.add(new Attack(b, a));
        framework7.add(new Attack(b, c));
        framework7.add(new Attack(c, c));

        framework8.add(a);
        framework8.add(b);
        framework8.add(new Attack(a, a));
        framework8.add(new Attack(a, b));

        framework9.add(a);
        framework9.add(b);
        framework9.add(c);
        framework9.add(new Attack(a, b));
        framework9.add(new Attack(b, c));
        framework9.add(new Attack(c, c));

        framework10.add(a);
        framework10.add(b);
        framework10.add(c);
        framework10.add(d);
        framework10.add(e);
        framework10.add(f);
        framework10.add(new Attack(b, a));
        framework10.add(new Attack(c, a));
        framework10.add(new Attack(d, c));
        framework10.add(new Attack(e, b));
        framework10.add(new Attack(e, d));
        framework10.add(new Attack(f, c));
    }
}
