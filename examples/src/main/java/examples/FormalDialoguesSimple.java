package examples;

import diarg.Semantics;
import diarg.enums.SemanticsType;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Simple integration example: construct arguments based on the output of a DMN engine
 * @author timotheuskampik
 */
public class FormalDialoguesSimple {

    public static void main(String[] args){
        // set up DMN decision automation engine
        DmnEngineConfiguration configuration = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
        DmnEngine dmnEngine = configuration.buildEngine();
        // Load decision: determine applicant eligibility for Lead Software Engineer position
        DmnDecision decision;
        File initialFile = new File("./src/main/resources/examples/admission-eligibility-decision.dmn11.xml");
        // Run case: good references, 8 years of experience, 240 score on ability test
        try {
            System.out.println("Determine eligibility of candidate, Lead Software Engineer: good references, 8 years of experience, 240 score on ability test");
            InputStream inputStream = new FileInputStream(initialFile);
            decision = dmnEngine.parseDecision("decision", inputStream);
            VariableMap variables = Variables
                    .putValue("experienceYears", 8)
                    .putValue("totalScore", 240)
                    .putValue("references", "good");

            DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decision, variables);
            /* Construct argumentation framework if results are ambiguous; no preference, i.e., mutual attacks
             * This loop is pretty generic
             */
            System.out.println("DMN decision results: " + results.toString());
            if(results.size() > 1) { // if results are indecisive, construct argumentation framework
                DungTheory argFramework = new DungTheory();
                ArrayList<Attack> attacks = new ArrayList<>();
                for (DmnDecisionRuleResult result : results) {
                    String argumentKey = result.keySet().iterator().next();
                    Boolean argumentValue = result.getFirstEntry();
                    String argumentName = argumentKey + ": " + argumentValue;
                    Argument argument = new Argument(argumentName);
                    argFramework.add(argument);
                    for (DmnDecisionRuleResult otherResult : results) {
                        String otherArgumentKey = otherResult.keySet().iterator().next();
                        Boolean otherArgumentValue = otherResult.getFirstEntry();
                        if(argumentKey.equals(otherArgumentKey) && argumentValue != otherArgumentValue) {
                            String otherArgumentName = otherArgumentKey + ": " + otherArgumentValue;
                            Argument otherArgument = new Argument(otherArgumentName);
                            attacks.add(new Attack(argument, otherArgument));
                        }
                    }
                }
                argFramework.addAllAttacks(attacks);
                System.out.println("Initial argumentation framework: \n" + argFramework.prettyPrint());
                // Assuming preferred semantics, we get more than one extension from the argumentation framework:
                Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
                System.out.println("No conclusion, yet: \n" + preferredSemantics.getModels(argFramework));
                /* In our case, we need a human who breaks the tie. This might be the hiring manager. Here, we assume a
                 * front end that instructs our reasoner to build the argumentation framework expansion that we
                 * hard-code below: */
                Argument managerArgument = new Argument("Position to be filled");
                // In the real world, we would need to have a proper lookup of the 2nd argument, but let us simplify here:
                Attack managerAttack = new Attack(managerArgument, new Argument("IsEligible: false"));
                // We create a new expansion to keep track of history
                DungTheory managerExpansion = new DungTheory();
                managerExpansion.add(argFramework);
                managerExpansion.add(managerArgument);
                managerExpansion.add(managerAttack);
                // Now, we can infer a clear conclusion:
                System.out.println("First expansion: \n" + managerExpansion.prettyPrint());
                System.out.println("According to the manager, we can move on with the candidate: \n" + preferredSemantics.getModels(managerExpansion));
                /* However, HR needs to approve the case and finds out it violates diversity requirements (other,
                * more 'diverse' candidate formally equally or better qualified) */
                DungTheory hrExpansion = new DungTheory();
                Argument hrArgument = new Argument("Diversity requirements not satisfied");
                Attack hrAttack1 = new Attack(hrArgument, managerArgument);
                Attack hrAttack2 = new Attack(hrArgument, new Argument("IsEligible: true"));
                hrExpansion.add(managerExpansion);
                hrExpansion.add(hrArgument);
                hrExpansion.add(hrAttack1);
                hrExpansion.add(hrAttack2);
                System.out.println("Second expansion: \n" + hrExpansion.prettyPrint());
                System.out.println("According to HR, the candidate is not eligible: \n" + preferredSemantics.getModels(hrExpansion));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
