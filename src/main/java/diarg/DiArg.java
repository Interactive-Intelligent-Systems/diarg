
package diarg;

import net.sf.tweety.arg.dung.reasoner.SimpleCF2Reasoner;
import net.sf.tweety.arg.dung.reasoner.SimpleStageReasoner;
import net.sf.tweety.arg.dung.reasoner.SimplePreferredReasoner;
import net.sf.tweety.arg.dung.reasoner.SimpleStageReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

/**
 * Example code for CF2 semantics
 */
public class DiArg {
    public static void main(String[] args){
        // create some Dung theory
        /*DungTheory theory = new DungTheory();
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        // Argument d = new Argument("d");
        theory.add(a);
        theory.add(b);
        theory.add(c);
        theory.add(d);
        theory.add(e);
        // theory.add(d);
        theory.add(new Attack(a,b));
        theory.add(new Attack(b,d));
        theory.add(new Attack(d,c));
        theory.add(new Attack(c,e));
        theory.add(new Attack(e,a));
        theory.add(new Attack(d,d));
        theory.add(new Attack(e,e));
        // AF = ({a, b, c}. {(a, b)})
        // AF' = ({a, b, c, d, e}. {(a, b), (b, d), (d, c), (c, e), (e, a), (d, d), (e, e)})
        SimpleCF2Reasoner cf2Reasoner = new SimpleCF2Reasoner();
        //SimpleStageReasoner stageReasoner = new SimpleStageReasoner();*/

        DungTheory theory = new DungTheory();
        Argument a = new Argument("a");
        Argument b = new Argument("b");
        Argument c = new Argument("c");
        Argument d = new Argument("d");
        Argument e = new Argument("e");
        theory.add(a);
        theory.add(b);
        theory.add(c);
        theory.add(d);
        theory.add(e);
        theory.add(new Attack(a,b));
        // theory.add(new Attack(b,a));
        theory.add(new Attack(b,c));
        theory.add(new Attack(c,a));
        theory.add(new Attack(c,d));
        theory.add(new Attack(d,e));
        theory.add(new Attack(e,c));

        SimpleRCFReasoner rcfReasoner = new SimpleRCFReasoner();

        for(Extension ext: rcfReasoner.getModels(theory)){
            System.out.println("RCF:");
            System.out.println(ext);
        }

        SimpleRSReasoner rsReasoner = new SimpleRSReasoner();

        for(Extension ext: rsReasoner.getModels(theory)){
            System.out.println("RS:");
            System.out.println(ext);
        }

        DungTheory framework = new DungTheory();
        framework.add(a);
        framework.add(b);
        framework.add(c);
        framework.add(d);
        framework.add(new Attack(a, b));
        framework.add(new Attack(b, c));
        //framework.add(new Attack(c, b));
        framework.add(new Attack(c, d));
        framework.add(new Attack(d, c));
        SimpleCF2Reasoner cf2Reasoner = new SimpleCF2Reasoner();

        for(Extension ext: cf2Reasoner.getModels(framework)){
            System.out.println("CF2: ({a, b, c ,d}, {(a, b), (b, c), (c, d), (d, c)})");
            System.out.println(ext);
        }



    }
}