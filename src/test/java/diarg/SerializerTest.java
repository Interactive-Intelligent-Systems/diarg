package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SemanticsType;
import diarg.enums.SequenceType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SerializerTest {
    Semantics rcfSemantics = new Semantics(SemanticsType.NSACF2);
    TestFrameworks testFrameworks = new TestFrameworks();
    Serializer serializer = new Serializer();
    AFSequence sequence;

    @BeforeAll
    void init() {
        sequence = new AFSequence(SequenceType.EXPANDING, ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                rcfSemantics, true);
        sequence.addFramework(testFrameworks.framework4);
        sequence.addFramework(testFrameworks.framework6);
        sequence.resolveFramework(0);
    }
    @Test
    void extensionToJSON() {
        JSONArray jExtension1 = serializer.extensionToJSON(rcfSemantics.getModel(testFrameworks.framework4));
        assertTrue(jExtension1.toString().equals(testFrameworks.extensionSerialization1));
        JSONArray jExtension2 = serializer.extensionToJSON(rcfSemantics.getModel(testFrameworks.framework6));
        assertTrue(jExtension2.toString().equals(testFrameworks.extensionSerialization2));
    }

    @Test
    void frameworkToJSON() {
        JSONObject jFramework1 = serializer.frameworkToJSON(testFrameworks.framework4);
        assertTrue(jFramework1.toString().equals(testFrameworks.frameworkSerialization1));
        JSONObject jFramework2 = serializer.frameworkToJSON(testFrameworks.framework6);
        assertTrue(jFramework2.toString().equals(testFrameworks.frameworkSerialization2));
    }

    @Test
    void sequenceToJSON() {
        JSONObject jSequence = serializer.sequenceToJSON(sequence);
        assertTrue(jSequence.toString().equals(testFrameworks.sequenceSerialization1));
    }

    @Test
    void contextToJSON() {
        Context context = new Context("testContext", rcfSemantics.getModel(testFrameworks.framework4));
        JSONObject jContext = serializer.contextToJSON(context);
        assertTrue(jContext.toString().equals(testFrameworks.contextSerialization1));
    }

    @Test
    void extensionFromJSON() {
        JSONArray jExtension1 = new JSONArray();
        JSONArray jExtension2 = new JSONArray();
        try {
            jExtension1 = new JSONArray(testFrameworks.extensionSerialization1);
            jExtension2 = new JSONArray(testFrameworks.extensionSerialization2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertTrue(serializer.extensionFromJSON(jExtension1).equals(rcfSemantics.getModel(testFrameworks.framework4)));
        assertTrue(serializer.extensionFromJSON(jExtension2).equals(rcfSemantics.getModel(testFrameworks.framework6)));
    }

    @Test
    void frameworkFromJSON() {
        JSONObject jFramework1 = new JSONObject();
        JSONObject jFramework2 = new JSONObject();
        try {
            jFramework1 = new JSONObject(testFrameworks.frameworkSerialization1);
            jFramework2 = new JSONObject(testFrameworks.frameworkSerialization2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertTrue(serializer.frameworkFromJSON(jFramework1).equals(testFrameworks.framework4));
        assertTrue(serializer.frameworkFromJSON(jFramework2).equals(testFrameworks.framework6));
    }

    @Test
    void sequenceFromJSON() {
        JSONObject jSequence = new JSONObject();
        try {
            jSequence = new JSONObject(testFrameworks.sequenceSerialization1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AFSequence sequenceShould = serializer.sequenceFromJSON(jSequence);
        assertTrue(serializer.sequenceToJSON(sequenceShould).equals(serializer.sequenceToJSON(sequence)));
    }

    @Test
    void contextFromJSON() {
        JSONObject jContext = new JSONObject();
        Extension extension = new Extension();
        extension.add(new Argument("a"));
        Context context = new Context("testContext", extension);
        try {
            jContext = new JSONObject(testFrameworks.contextSerialization1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Context contextShould = serializer.contextFromJSON(jContext);
        assertTrue(serializer.contextToJSON(contextShould).equals(serializer.contextToJSON(context)));
    }
}
