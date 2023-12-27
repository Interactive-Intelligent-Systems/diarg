package diarg;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.math.Quantiles;
import diarg.distances.DistanceMeasure;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An agreement scenario is an argumentation framework AF = (AR, AT), with a topic
 * T that is a subset of AR and a sequence of semantics that represent the inference
 * process of different agents
 * @author Timotheus Kampik
 */
public class AgreementScenario {

    private DungTheory argumentationFramework;
    private Collection<Argument> topic;
    private ArrayList<Semantics> semantics;
    private DistanceMeasure distanceMeasure;

    /**
     * Returns the agreement scenario's argumentation framework.
     * @return The agreement scenario's argumentation framework
     */
    public DungTheory getArgumentationFramework() {
        return argumentationFramework;
    }

    /**
     * Returns the agreement scenario's topic.
     * @return The agreement scenario's topic
     */
    public Collection<Argument> getTopic() {
        return topic;
    }

    /**
     * Returns the agreement scenario's semantics.
     * @return The agreement scenario's semantics
     */
    public ArrayList<Semantics> getSemantics() {
        return semantics;
    }

    public AgreementScenario (
            DungTheory argumentationFramework,
            Collection<Argument> topic,
            ArrayList<Semantics> semantics,
            DistanceMeasure distanceMeasure
    ) {
        try {
            if (!argumentationFramework.containsAll(topic)) {
                throw new Exception("Argumentation framework does not contain topic");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.argumentationFramework = argumentationFramework;
        this.topic = topic;
        this.semantics = semantics;
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * Adds a new argument to the agreement scenario's argumentation framework.
     * Does not add any attacks.
     * @param argument The argument that is to be added
     */
    public void addArgument(Argument argument) {
        argumentationFramework.add(argument);
    }

    /**
     * Adds a new attack to the agreement scenario's argumentation framework.
     * @param attack The attack that is to be added
     */
    public void addAttack(Attack attack) {
        argumentationFramework.add(attack);
    }

    /**
     * Adds a new topic argument to the agreement scenario's argumentation framework and also
     * adds this argument to the scenario's topic. Note that it is not possible to add an argument
     * to the topic that is not "new", i.e., that has already previously been added to the argumentation framework.
     * @param argument Topic argument that is to be added
     */
    public void addTopicArgument(Argument argument) {
        try {
            if (argumentationFramework.contains(argument) && !topic.contains(argument)) {
                throw new Exception("Argument exists already and hence cannot be added to topic.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        argumentationFramework.add(argument);
        topic.add(argument);
    }


    public void expand(
            Collection<Argument> arguments,
            Collection<Attack> attacks,
            Collection<Argument> topicArguments) {
        try {
            for(Argument topicArgument: topicArguments) {
                if (argumentationFramework.contains(topicArgument) && !topic.contains(topicArgument)) {
                    throw new Exception("Argument exists already and hence cannot be added to topic.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        argumentationFramework.addAll(arguments);
        argumentationFramework.addAll(topicArguments);
        argumentationFramework.addAllAttacks(attacks);
        topic.addAll(arguments);
    }

    /**
     * Determines the degree of satisfaction between two "agents" (semantics),
     * given the agreement scenario.
     * @param index1 Index of "agent"/semantics 1 in semantics collection
     * @param index2 Index of "agent"/semantics 2 in semantics collection
     * @return Degree of satisfaction
     */
    public double determineSatisfaction(int index1, int index2) {
        Semantics semantics1 = semantics.get(index1);
        Semantics semantics2 = semantics.get(index2);
        Collection<Extension> extensions1 = semantics1.getModels(argumentationFramework);
        Collection<Extension> extensions2 = semantics2.getModels(argumentationFramework);
        double greatestSimilarity = 0;
        for(Extension extension1: extensions1) {
            for(Extension extension2: extensions2) {
                double similarity = distanceMeasure.determineDistance(extension1, extension2, topic);
                if(similarity > greatestSimilarity) greatestSimilarity = similarity;
            }
        }
        return greatestSimilarity;
    }

    /**
     * Determines the degree of minimal agreement of the agreement scenario.
     * @return Degree of minimal agreement
     */
    public double determineMinimalAgreement() {
        ArrayList<Double> smallestSimilarities = new ArrayList<>();
        Collection<Argument> arguments = argumentationFramework.getNodes();
        Set<Argument> argumentSet = new HashSet<>(arguments);
        Set<Set<Argument>> argumentPowerSet = Sets.powerSet(argumentSet);
        for(Set<Argument> currentArgumentSet: argumentPowerSet) {
            double smallestSimilarity = 1;
            for(Semantics currentSemantics: semantics) {
                double bestExtensionSimilarity = 0;
                for(Extension extension: currentSemantics.getModels(argumentationFramework)) {
                    double extensionSimilarity = distanceMeasure.determineDistance(extension, currentArgumentSet, topic);
                    if(extensionSimilarity > bestExtensionSimilarity) {
                        bestExtensionSimilarity = extensionSimilarity;
                    }
                }
                if(bestExtensionSimilarity < smallestSimilarity) {
                    smallestSimilarity = bestExtensionSimilarity;
                }
            }
            smallestSimilarities.add(smallestSimilarity);
        }
        return smallestSimilarities.stream().mapToDouble(v -> v).max().orElse(0);
    }

    /**
     * Determines the degree of mean agreement of the agreement scenario.
     * @return Degree of mean agreement
     */
    public double determineMeanAgreement() {
        ArrayList<Double> meanSimilarities = new ArrayList<>();
        Collection<Argument> arguments = argumentationFramework.getNodes();
        Set<Argument> argumentSet = new HashSet<>(arguments);
        Set<Set<Argument>> argumentPowerSet = Sets.powerSet(argumentSet);
        for(Set<Argument> currentArgumentSet: argumentPowerSet) {
            double similaritySum = 0;
            for(Semantics currentSemantics: semantics) {
                double bestExtensionSimilarity = 0;
                for(Extension extension: currentSemantics.getModels(argumentationFramework)) {
                    double extensionSimilarity = distanceMeasure.determineDistance(extension, currentArgumentSet, topic);
                    if(extensionSimilarity > bestExtensionSimilarity) {
                        bestExtensionSimilarity = extensionSimilarity;
                    }
                }
                similaritySum += bestExtensionSimilarity;
            }
            meanSimilarities.add(similaritySum/semantics.size());
        }
        return meanSimilarities.stream().mapToDouble(v -> v).max().orElse(0);
    }

    /**
     * Determines the degree of median agreement of the agreement scenario.
     * @return Degree of median agreement
     */
    public double determineMedianAgreement() {
        ArrayList<Double> medianSimilarities = new ArrayList<>();
        Collection<Argument> arguments = argumentationFramework.getNodes();
        Set<Argument> argumentSet = new HashSet<>(arguments);
        Set<Set<Argument>> argumentPowerSet = Sets.powerSet(argumentSet);
        for(Set<Argument> currentArgumentSet: argumentPowerSet) {
            ArrayList<Double> similarities = new ArrayList<>();
            for(Semantics currentSemantics: semantics) {
                double bestExtensionSimilarity = 0;
                for(Extension extension: currentSemantics.getModels(argumentationFramework)) {
                    double extensionSimilarity = distanceMeasure.determineDistance(extension, currentArgumentSet, topic);
                    if(extensionSimilarity > bestExtensionSimilarity) {
                        bestExtensionSimilarity = extensionSimilarity;
                    }
                }
                similarities.add(bestExtensionSimilarity);
            }
            medianSimilarities.add(Quantiles.median().compute(similarities));
        }
        return medianSimilarities.stream().mapToDouble(v -> v).max().orElse(0);
    }

    /**
     * Determines the whether ``this`` agreement scenario is an expansion of the provided
     * agreement scenario.
     * @param otherScenario The argumentation scenario that is supposedly expanded
     * @return The result of the check
     */
    public boolean isExpansionOf(AgreementScenario otherScenario) {
        ArrayList<Semantics> otherSemantics = otherScenario.getSemantics();
        AFTuple afTuple = new AFTuple(otherScenario.getArgumentationFramework(), argumentationFramework);
        return
                afTuple.isExpansion() &&
                topic.containsAll(otherScenario.getTopic()) &&
                semantics.containsAll(otherSemantics) && otherSemantics.containsAll(semantics);
    }

    /**
     * Determines the whether ``this`` agreement scenario is a normal expansion of the provided
     * agreement scenario.
     * @param otherScenario The argumentation scenario that is supposedly normally expanded
     * @return The result of the check
     */
    public boolean isNormalExpansionOf(AgreementScenario otherScenario) {
        DungTheory otherFramework = otherScenario.getArgumentationFramework();
        Collection<Argument> otherTopic = otherScenario.getTopic();
        AFTuple afTuple = new AFTuple(otherScenario.getArgumentationFramework(), argumentationFramework);
        for(Argument topicArgument: topic) {
            if(otherFramework.contains(topicArgument) && !otherTopic.contains(topicArgument)) {
                return false;
            }
        }
        return afTuple.isNormalExpansion() && this.isExpansionOf(otherScenario);
    }
}
