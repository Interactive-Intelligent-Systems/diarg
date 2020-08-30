package diarg;

import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;

/**
 * Provides example argumentation frameworks for test purposes
 * framework1: ({a, b, c, d, e},{(a, b), (b, c), (c, b), (c, d), (d, e), (e, c)})
 * framework2: ({a, b, c}, {(a, b), (b, a), (b, c), (c, a)})
 * framework3: ({a, b, c}, {(a, b), (b, c), (c, a)})
 * framework4: ({a, b}, {(a, b)})
 * framework5: ({a, b}, {(a, b), (b, a)})
 * framework6: ({a, b, c,  d}, {(a, b), (b, a), (c, d), (d, c)})
 * framework7: ({a, b, c, d}, {(a, b), (b, c), (c, a), (d, a)})
 * framework8 ({a, b, c, d}, {(a, b), (b, a), (b, c), (c, d), (d, c)})
 * @author Timotheus Kampik
 */

public final class TestFrameworks {

    DungTheory framework1 = new DungTheory();
    DungTheory framework2 = new DungTheory();
    DungTheory framework3 = new DungTheory();
    DungTheory framework4 = new DungTheory();
    DungTheory framework5 = new DungTheory();
    DungTheory framework6 = new DungTheory();
    DungTheory framework7 = new DungTheory();
    DungTheory framework8 = new DungTheory();
    String extensionSerialization1 = "[\"a\"]";
    String extensionSerialization2 = "[\"a\",\"c\"]";
    String frameworkSerialization1 = "{\"arguments\":[\"a\",\"b\"],\"attacks\":[[\"a\",\"b\"]]}";
    String frameworkSerialization2 = "{\"arguments\":[\"a\",\"b\",\"c\",\"d\"]," +
            "\"attacks\":[[\"a\",\"b\"],[\"c\",\"d\"],[\"b\",\"a\"],[\"d\",\"c\"]]}";
    String sequenceSerialization1 = "{\"semanticsType\":\"NSACF2\",\"sequenceType\":\"EXPANDING\",\"resolutionType\":\"EXPANSIONIST_REFERENCE_INDEPENDENT\",\"frameworks\":[{\"framework\":{\"arguments\":[\"a\",\"b\"],\"attacks\":[[\"a\",\"b\"]]},\"contextSummary\":[],\"contexts\":[],\"resolution\":[\"a\"]},{\"framework\":{\"arguments\":[\"a\",\"b\",\"c\",\"d\"],\"attacks\":[[\"a\",\"b\"],[\"c\",\"d\"],[\"b\",\"a\"],[\"d\",\"c\"]]},\"contextSummary\":[],\"contexts\":[]}]}";
    String contextSerialization1 = "{\"name\":\"testContext\",\"arguments\":[\"a\"]}";

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

        framework3.add(a);
        framework3.add(b);
        framework3.add(c);
        framework3.add(new Attack(a, b));
        framework3.add(new Attack(b, c));
        framework3.add(new Attack(c, a));

        framework4.add(a);
        framework4.add(b);
        framework4.add(new Attack(a, b));

        framework5.add(a);
        framework5.add(b);
        framework5.add(new Attack(a, b));
        framework5.add(new Attack(b, a));

        framework6.add(a);
        framework6.add(b);
        framework6.add(c);
        framework6.add(d);
        framework6.add(new Attack(a, b));
        framework6.add(new Attack(b, a));
        framework6.add(new Attack(c, d));
        framework6.add(new Attack(d, c));

        framework7.add(a);
        framework7.add(b);
        framework7.add(c);
        framework7.add(d);
        framework7.add(new Attack(a, b));
        framework7.add(new Attack(b, c));
        framework7.add(new Attack(c, a));
        framework7.add(new Attack(d, a));

        framework8.add(a);
        framework8.add(b);
        framework8.add(c);
        framework8.add(d);
        framework8.add(new Attack (a, b));
        framework8.add(new Attack (b, a));
        framework8.add(new Attack (b, c));
        framework8.add(new Attack (c, d));
        framework8.add(new Attack (d, c));
    }
}

