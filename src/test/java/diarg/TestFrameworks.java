package diarg;

import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;

import java.util.Collection;
import java.util.LinkedList;

public final class TestFrameworks {

    DungTheory framework1 = new DungTheory();
    DungTheory framework2 = new DungTheory();

    public TestFrameworks() {
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");

        framework1.add(a);
        framework1.add(b);
        framework1.add(c);
        framework1.add(d);
        framework1.add(e);
        framework1.add(new Attack(a,b));
        framework1.add(new Attack(b,c));
        framework1.add(new Attack(c,b));
        framework1.add(new Attack(c,d));
        framework1.add(new Attack(d,e));
        framework1.add(new Attack(e,c));

        framework2.add(a);
        framework2.add(b);
        framework2.add(c);
        framework2.add(new Attack(a,b));
        framework2.add(new Attack(b,a));
        framework2.add(new Attack(b,c));
        framework2.add(new Attack(c,a));
    }
}

