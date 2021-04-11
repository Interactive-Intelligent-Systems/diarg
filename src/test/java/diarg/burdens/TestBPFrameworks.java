package diarg.burdens;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides example burden of persuasion frameworks (examples from the paper) for test purposes
 * bpFramework1: (({a}, {b, c, l}), {(a, c), (a, l), (b, a), (c, b)})
 * bpFramework2: (({a, b}, {c}, {d, e}), {(a, b), (a, e), (b, a), (b, e), (c, d), (d, c), (e, a), (e, b)})
 * bpFramework3: (({a, b}, {c, d, e}, {f}), {(a, c), (a, e), (c, d), (d, b), (d, f), (e, a), (e, c), (f, d), (f, b)})
 */
public final class TestBPFrameworks {

    public BurdenOfPersuasionTheory bpFramework1;
    public BurdenOfPersuasionTheory bpFramework2;
    public BurdenOfPersuasionTheory bpFramework3;


    public TestBPFrameworks() {
        Argument l = new Argument("l");
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");
        // bpFramework1
        Collection<Attack> attacks1 = new ArrayList<>();
        attacks1.add(new Attack(a, c));
        attacks1.add(new Attack(a, l));
        attacks1.add(new Attack(b, a));
        attacks1.add(new Attack(c, b));
        Collection<Argument> burden11 = new ArrayList<>();
        Collection<Argument> burden12 = new ArrayList<>();
        burden11.add(a);
        burden12.add(b);
        burden12.add(c);
        burden12.add(l);
        ArrayList<Collection<Argument>> burdens1 = new ArrayList<>();
        burdens1.add(burden11);
        burdens1.add(burden12);
        bpFramework1 = new BurdenOfPersuasionTheory(burdens1, attacks1);
        // bpFramework2
        Collection<Attack> attacks2 = new ArrayList<>();
        attacks2.add(new Attack(a, b));
        attacks2.add(new Attack(a, e));
        attacks2.add(new Attack(b, a));
        attacks2.add(new Attack(b, e));
        attacks2.add(new Attack(c, d));
        attacks2.add(new Attack(d, c));
        attacks2.add(new Attack(e, a));
        attacks2.add(new Attack(e, b));
        Collection<Argument> burden21 = new ArrayList<>();
        Collection<Argument> burden22 = new ArrayList<>();
        Collection<Argument> burden23 = new ArrayList<>();
        burden21.add(a);
        burden21.add(b);
        burden22.add(c);
        burden23.add(d);
        burden23.add(e);
        ArrayList<Collection<Argument>> burdens2 = new ArrayList<>();
        burdens2.add(burden21);
        burdens2.add(burden22);
        burdens2.add(burden23);
        bpFramework2 = new BurdenOfPersuasionTheory(burdens2, attacks2);
        // bpFramework3
        Collection<Attack> attacks3 = new ArrayList<>();
        attacks3.add(new Attack(a, c));
        attacks3.add(new Attack(a, e));
        attacks3.add(new Attack(c, d));
        attacks3.add(new Attack(d, b));
        attacks3.add(new Attack(d, f));
        attacks3.add(new Attack(e, a));
        attacks3.add(new Attack(e, c));
        attacks3.add(new Attack(f, d));
        attacks3.add(new Attack(f, b));
        Collection<Argument> burden31 = new ArrayList<>();
        Collection<Argument> burden32 = new ArrayList<>();
        Collection<Argument> burden33 = new ArrayList<>();
        burden31.add(a);
        burden31.add(b);
        burden32.add(c);
        burden32.add(d);
        burden32.add(e);
        burden33.add(f);
        ArrayList<Collection<Argument>> burdens3 = new ArrayList<>();
        burdens3.add(burden31);
        burdens3.add(burden32);
        burdens3.add(burden33);
        bpFramework3 = new BurdenOfPersuasionTheory(burdens3, attacks3);
    }
}