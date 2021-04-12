package examples;

import diarg.Semantics;
import diarg.burdens.BurdenOfPersuasionTheory;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;

import java.util.ArrayList;
import java.util.Collection;

public class BurdenOfPersuasion {

    /**
     * Example code: burden of persuasion framework
     */
    public static void main(String[] args){
        Semantics scf2 = new Semantics(SemanticsType.SCF2);
        BurdenOfPersuasionTheory bpFramework;
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        Argument f = new Argument("f");
        Collection<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack(a, c));
        attacks.add(new Attack(a, e));
        attacks.add(new Attack(c, d));
        attacks.add(new Attack(d, b));
        attacks.add(new Attack(d, f));
        attacks.add(new Attack(e, a));
        attacks.add(new Attack(e, c));
        attacks.add(new Attack(f, d));
        attacks.add(new Attack(f, b));
        Collection<Argument> burden1 = new ArrayList<>();
        Collection<Argument> burden2 = new ArrayList<>();
        Collection<Argument> burden3 = new ArrayList<>();
        burden1.add(a);
        burden1.add(b);
        burden2.add(c);
        burden2.add(d);
        burden2.add(e);
        burden3.add(f);
        ArrayList<Collection<Argument>> burdens = new ArrayList<>();
        burdens.add(burden1);
        burdens.add(burden2);
        burdens.add(burden3);
        bpFramework = new BurdenOfPersuasionTheory(burdens, attacks);
        System.out.println("Burden of persuasion framework: (" +
                bpFramework.getArgumentOrder() + ", " +
                bpFramework.getAttacks()+ ")"
        );
        Collection<Extension> extensions = bpFramework.determineExtensions(scf2);
        System.out.println("Extension of burden of persuasion framework: " + extensions);
    }
}
