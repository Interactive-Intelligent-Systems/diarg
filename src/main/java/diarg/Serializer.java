package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SemanticsType;
import diarg.enums.SequenceType;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;

public final class Serializer {

    public JSONObject sequenceToJSON(AFSequence sequence) {
        JSONObject jSequence = new JSONObject();
        JSONArray jFrameworks = new JSONArray();
        try {
            jSequence.put("semanticsType", sequence.getSemantics().semanticsType);
            jSequence.put("sequenceType", sequence.getSequenceType());
            jSequence.put("resolutionType", sequence.getResolutionType());
            for(int i = 0; i < sequence.getFrameworks().size(); i++) {
                DungTheory framework = sequence.getFramework(i);
                JSONObject jFramework = new JSONObject();
                jFramework.put("framework", frameworkToJSON(framework));
                if(sequence.getResolutions().size() > i) {
                    Extension resolution = sequence.getResolution(i);
                    jFramework.put("resolution", extensionToJSON(resolution));
                }
                jFrameworks.put(jFramework);

            }
            jSequence.put("frameworks", jFrameworks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSequence;
    }

    public AFSequence sequenceFromJSON(JSONObject jSequence) {
        try {
            SemanticsType semanticsType = SemanticsType.valueOf(jSequence.get("semanticsType").toString());
            SequenceType sequenceType = SequenceType.valueOf(jSequence.get("sequenceType").toString());
            ResolutionType resolutionType = ResolutionType.valueOf(jSequence.get("resolutionType").toString());
            Semantics semantics = new Semantics(semanticsType);
            AFSequence sequence = new AFSequence(sequenceType, resolutionType, semantics);
            JSONArray jFrameworks = jSequence.getJSONArray("frameworks");
            for(int i = 0; i < jFrameworks.length(); i++) {
                JSONObject jFramework = jFrameworks.getJSONObject(i);
                sequence.addFramework(frameworkFromJSON(jFramework.getJSONObject("framework")));
                if(jFramework.has("resolution")){
                    JSONArray jResolution = jFramework.getJSONArray("resolution");
                    Extension resolution = extensionFromJSON(jResolution);
                    sequence.resolveFramework(i, resolution);

                }
            }
            return sequence;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject frameworkToJSON(DungTheory framework) {
        JSONObject jFramework = new JSONObject();
        JSONArray jArguments = new JSONArray();
        JSONArray jAttacks = new JSONArray();
        for(Argument argument: framework.getNodes()) {
            jArguments.put(argument.getName());
        }
        for(Attack attack: framework.getAttacks()) {
            JSONArray jAttack = new JSONArray();
            jAttack.put(attack.getAttacker().getName());
            jAttack.put(attack.getAttacked().getName());
            jAttacks.put(jAttack);
        }
        try {
            jFramework.put("arguments", jArguments);
            jFramework.put("attacks", jAttacks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jFramework;
    }

    public DungTheory frameworkFromJSON(JSONObject jFramework) {
        DungTheory framework = new DungTheory();
        try {
            Extension extension = extensionFromJSON(jFramework.getJSONArray("arguments"));
            framework.addAll(extension);
            JSONArray jAttacks = jFramework.getJSONArray("attacks");
            for(int i = 0; i < jAttacks.length(); i++) {
                Attack attack = new Attack(
                        new Argument(jAttacks.getJSONArray(i).getString(0)),
                        new Argument(jAttacks.getJSONArray(i).getString(1)));
                framework.add(attack);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return framework;
    }

    public JSONArray extensionToJSON(Extension extension) {
        JSONArray jExtension = new JSONArray();
        for(Argument argument: extension) {
            jExtension.put(argument.getName());
        }
        return jExtension;
    }

    public Extension extensionFromJSON(JSONArray jExtension) {
        Extension extension = new Extension();
        for(int i = 0; i < jExtension.length(); i++) {
            try {
                extension.add(new Argument(jExtension.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return extension;
    }
}
