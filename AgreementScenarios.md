# Agreement Scenarios
DiArg supports modeling and analyzing *argumentation-based agreement scenarios* for both abstract and value-based
argumentation. This tutorial describes how (for more details, consider consulting the API documentation).
The complete code for this example is available in the ``examples`` directory.

## Abstract Argumentation-based Agreement Scenarios (AAS)
An (abstract) Argumentation-based Agreement Scenario (AAS) allows to model, given an argumentation framework (AF, AT)
and a "topic" subset of the arguments AR, differences in the inferences that are drawn by different agents. Each agent
is represented by its own argumentation semantics.
Let us model an AAS with DiArg:

```java
DungTheory framework = new DungTheory();
Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");
Argument d = new Argument("d");
Argument e = new Argument("e");
framework.add(a);
framework.add(b);
framework.add(c);
framework.add(d);
framework.add(e);
framework.add(new Attack(e, a));
framework.add(new Attack(d, b));
framework.add(new Attack(b, d));
framework.add(new Attack(d, c));
framework.add(new Attack(c, d));
framework.add(new Attack(d, d));
framework.add(new Attack(e, e));

Collection<Argument> topic = new Extension();
ArrayList<Semantics> semantics = new ArrayList<>();
AgreementScenario aScenario;
topic.add(a);
topic.add(b);
topic.add(c);
Semantics stage = new Semantics(SemanticsType.STAGE);
Semantics preferred = new Semantics(SemanticsType.PREFERRED);
Semantics grounded = new Semantics(SemanticsType.GROUNDED);
semantics.add(stage);
semantics.add(preferred);
semantics.add(grounded);
CombinedDistance measure = new CombinedDistance();
aScenario = new AgreementScenario(framework, topic, semantics, measure);
```
The agreement scenario has the argumentation framework
({a, b, c, d, e}, {(e, a), (d, b), (b, d), (d, c), (c, d), (d, d), (e, e)}), the topic {a, b, c} and three agents with
the following semantics:

* Agent 0: stage semantics;
* Agent 1: preferred semantics;
* Agent 2: grounded semantics.

For this agreement scenario, we want to determine the degrees of satisfaction between two agents. To determine the
degree of satisfaction of an agent A1 with another agent's (A2's) inference, we determine, roughly speaking, the maximal
alignment between any tuple of A1's and A2's extensions.

```java
double satisfaction01 = aScenario.determineSatisfaction(0, 1); // 2/3
double satisfaction02 = aScenario.determineSatisfaction(0, 2); // 0
double satisfaction12 = aScenario.determineSatisfaction(1, 2); // 1/3
```

Based on the degrees of satisfaction, we want to determine the *degrees of agreement*, i.e.:

* The **degree of minimal agreement** is the lowest degree of satisfaction of any agent, given an option that allows for
  a maximal lowest degree of satisfaction among all agents.
* The **degree of mean agreement** is the mean degree of satisfaction of all agents, given an option that allows for a
  maximal mean degree of satisfaction among all agents.
* Similarly, the **degree of median agreement** is the median degree of satisfaction of all agents, given an option that allows for a
  maximal median degree of satisfaction among all agents.
  
With DiArg, we can determine the degrees of agreement as follows:

```java
double minimalAgreement = aScenario.determineMinimalAgreement(); // 1/3
double meanAgreement = aScenario.determineMeanAgreement(); // 2/3
double medianAgreement = aScenario.determineMedianAgreement(); // 2/3
```

## Value-based Argumentation
As an alternative for modeling differences in how agents draw inferences from an argumentation framework, we can use
Value-based Argumentation Frameworks (VAFs). A value-based argumentation framework extends an abstract argumentation
framework by adding a set of values, a mapping from arguments to values, and, for each agent, a preference relation on
the values. The preference relation is and transitive, irreflexive, and asymmetric binary relation.
Below is an example of a VAF in DiArg:

```java
DungTheory aFramework = new DungTheory();
Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");
Argument d = new Argument("d");
aFramework.add(a);
aFramework.add(b);
aFramework.add(c);
aFramework.add(d);
aFramework.add(new Attack(a, b));
aFramework.add(new Attack(b, a));
aFramework.add(new Attack(c, b));
aFramework.add(new Attack(d, c));
Collection<Value> values = new ArrayList<>();
ArgumentValueMapping argValMapping = new ArgumentValueMapping();
ArrayList<ValuePreferenceOrder>  valPrefOrders = new ArrayList<>();
Value av = new Value("av");
Value bv = new Value("bv");
Value cv = new Value("cv");
Value dv = new Value("dv");
values.add(av);
values.add(bv);
values.add(cv);
values.add(dv);
argValMapping.setValueAssignment(a, av);
argValMapping.setValueAssignment(b, bv);
argValMapping.setValueAssignment(c, cv);
argValMapping.setValueAssignment(d, dv);
ValuePreferenceOrder valPrefOrder1 = new ValuePreferenceOrder();
valPrefOrder1.addValuePreference(new ValuePreference(av, bv));
ValuePreferenceOrder valPrefOrder2 = new ValuePreferenceOrder();
valPrefOrder2.addValuePreference(new ValuePreference(bv, av));
ValuePreferenceOrder valPrefOrder3 = new ValuePreferenceOrder();
valPrefOrder3.addValuePreference(new ValuePreference(cv, dv));
valPrefOrders.add(valPrefOrder1);
valPrefOrders.add(valPrefOrder2);
valPrefOrders.add(valPrefOrder3);
ValueBasedTheory vbFramework = new ValueBasedTheory(aFramework, values, argValMapping, valPrefOrders);
```
Note that a semi-formal representation of the VAF is as follows:
({a, b, c, d},{(a, b), (b, a), (c, b), (c, d)}, {av, bv, cv, dv}, {a &rarr; av, b &rarr; bv, c &rarr; cv, d &rarr; dv}, ({av &gt; bv}, {bv &gt; av), {cv &gt; dv}))

Given a VAF and an argumentation semantics, we can determine the VAF's *subjective extensions*, i.e., the extensions
that consider the value preference orders of the agents whose preferences are modeled by the VAF, as follows:

```java
Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
ArrayList<Collection<Extension>> allSubjectiveExtensions = vbFramework.determineAllSubjectiveExtensions(preferredSemantics);
```
In the example above, we apply preferred semantics, which yields the following extensions:

* Agent 0: {{a, d}};
* Agent 1: {{b, d}};
* Agent 2: {{a, c, d}}.

To determine the subjective extensions for one specific agent,  we can proceed as follows (again assuming preferred
semantics):

```java
Collection<Extension> subjectiveExtensions = vbFramework.determineSubjectiveExtensions(preferredSemantics, 0);
```
We infer one extension: {a, d}.

The VAF's objectve extension is the subset-maximal set of arguments that is inferred by all subjective extensions
(in this case: {d}):

```java
Extension objectiveExtension = vbFramework.determineObjectiveExtension(preferredSemantics);
```

## Value-based Argumentation-based Agreement Scenarios (VAAS)
Value-based Argumentation-based Agreement Scenarios (VAAS) are, roughly speaking, the VAF-based equivalent to AAS.
Because in VAFs subjective inference is modelled via value preferencees, a VAAS has only one semantics. Below, we
instantiate a VAAS:

```java
Collection<Argument> vTopic = new ArrayList<>();
vTopic.add(a);
vTopic.add(b);
vTopic.add(c);
vTopic.add(d);
ValueBasedAgreementScenario vbScenario = new ValueBasedAgreementScenario(vbFramework, vTopic, preferredSemantics, measure);
```

For a VAAS, we can determine the degrees of satisfaction and agreements, in the same way we do this for an AAS:

```java
double vSatisfaction01 = vbScenario.determineSatisfaction(0, 1); // 1/2
double vMinimalAgreement = vbScenario.determineMinimalAgreement(); // 1/2
double vMeanAgreement = vbScenario.determineMeanAgreement(); // 3/4
double vMedianAgreement = vbScenario.determineMedianAgreement(); // 3/4
```

In addition, we can determine the impact of a value on a degree of satisfaction or agreement. This impact is a
counterfactual measure that determines the change to the degree when "removing" all preferences that relate to this value.

```java
double satisfactionImpact = vbScenario.determineSatisfactionImpact(0, 1, bv); // -1/2
double minimalAgreementImpact = vbScenario.determineMinimalAgreementImpact(bv); // -1/4
double meanAgreementImpact = vbScenario.determineMeanAgreementImpact(bv); // -1/6
double medianAgreementImpact = vbScenario.determineMedianAgreementImpact(bv); // -1/4
```

