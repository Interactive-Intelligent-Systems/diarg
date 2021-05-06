package examples;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormalDialoguesExtended {
    public static void main(String[] args){
        DmnDecision decision;
        DmnEngineConfiguration configuration = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
        DmnEngine dmnEngine = configuration.buildEngine();
        File initialFile = new File("./examples/src/main/resources/examples/support-satisfaction-decision.dmn11.xml");
        try {
            InputStream inputStream = new FileInputStream(initialFile);
            decision = dmnEngine.parseDecision("decision", inputStream);
            VariableMap variables = Variables
                    .putValue("AvgSatScore", 8)
                    .putValue("NumberofEscalations", 11);

            DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, variables);
            String output = result.getSingleResult().getSingleEntry();
            System.out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
