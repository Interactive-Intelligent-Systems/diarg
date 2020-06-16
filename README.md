![Java CI with Gradle](https://github.com/TimKam/diarg/workflows/Java%20CI%20with%20Gradle/badge.svg)

# DiArg - An Argumentation-based Dialogue Reasoner

DiArg is a Java library for argumentation-based dialogue reasoning.
The focus of DiArg is the management of *argumentation framework sequences*.
During a dialogue, arguments and attacks are iteratively added to an argumentation framework and the framework is
resolved after each iteration (*i.e.*, after a set of arguments and attack relations have been added). For such types of
dialogues, DiArg provides object-oriented abstractions (the initial argumentation framework and its additions are
managed in an *argumentation sequence* object), as well as the following features:

1. **Expansion management**. DiArg can enforce (if desirable) that each argumentation framework in an argumentation
    sequence is, with respect to its predecessor, an expansion (only additions to the preceding framework), or a normal
    expansion (only additions of arguments and no additions of attack relations between existing arguments).
    
2. **Enforcement of consistent conclusions**. DiArg can enforce the consistency of the conclusions that are made at the
    different iteration steps; *i.e.*, when normally expanding an argumentation framework, an argument that has
    previously been considered a valid conclusion can only be discarded if any of the new arguments is now considered
    a valid conclusion. Similarly, DiArg can enforce cautious monotony across iterations.

3. **Context management**. DiArg provides a *context* abstraction that allows for the deactivation of specific argument
    sets at any iteration step. For example, given the application scenario of a health recommender system, certain
    arguments might be deactivated during weekends, to allow the user to deviate from the otherwise strict dietary
    recommendations.

You find the Javadoc of this project at
[https://people.cs.umu.se/~tkampik/docs/diarg/](https://people.cs.umu.se/~tkampik/docs/diarg/).

## Installation
The installation instructions assume [Gradle](https://gradle.org/) is used for the build setup.
The library is available in a custom Maven repository, which needs to be added to the project's configuration.

```
repositories {
    maven {
        url 'https://people.cs.umu.se/~tkampik/mvn/'
    }
    ...
}
```

Then, the dependency to DiArg can be added as follows:

```
dependencies {
    compile group: 'diarg', name: 'di-arg', version: '0.1-SNAPSHOT'
    ...
}
```

DiArg needs to be used in combination with [Tweety](http://tweetyproject.org/), which can be added as follows:

```
dependencies {
    ...
    implementation('net.sf.tweety:tweety-full:1.16') {
        exclude group: 'org.ojalgo', module: 'ojalgo'
        exclude group: 'jspf', module: 'core'
    }
    ...
```

Note that this excludes some Tweety dependencies that are not required for most Tweety/DiArg applications.

## Tutorial
To show how DiArg can be applied, let us introduce the following application example. We are developing a digital
assistant (mobile application) for stress management. The application recommends stress-relieving activities
(represented by arguments) to an end-user; the end-user can then either accept the recommendation and add it to their
schedule or reject the activity by adding an argument that attacks the activity. This argument will be considered when
providing future recommendations. The argument may be context-dependent. For example, a user may reject stress-relieving
activities that are particularly time-consuming during weekdays, but accept them at weekends.

Let us present a simplified example program that models such a recommender system.
We start with an argumentation framework that has three arguments representing the execution of
different activities. Because at any point in time, only one activity can be executed, all arguments
attack each other. Note that for defining argumentation frameworks, we use the [Tweety](http://tweetyproject.org/) library.

```java
DungTheory initialFramework = new DungTheory();
Argument a = new Argument("a"); // Meditate
Argument b = new Argument("b"); // Join social lunch
Argument c = new Argument("c"); // Go hiking
initialFramework.add(a);
initialFramework.add(b);
initialFramework.add(c);
initialFramework.addAttack(a, b);
initialFramework.addAttack(b, a);
initialFramework.addAttack(b, c);
initialFramework.addAttack(c, b);
initialFramework.addAttack(c, a);
initialFramework.addAttack(a, c);
```

In the program code above, we defined the argumentation framework
AF = (\{a, b, c\}, \{(a, b), (b, a), (b, c), (c, b), (c, a), (a, c)\}).
It is clear that a relatively credulous argumentation semantics should be used for the application
scenario.
Then, the recommender system can either allow the user to choose any of the extensions a specific
semantics returns for the framework, or it can add an *annihilator argument* to the framework to
enforce a unique extension.

Let us assume we want the system to do the later.
For this, we add the following program code:

```java
AFSequence sequence = new AFSequence(
    SequenceType.NORMALLY_EXPANDING,
    ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
    rcfSemantics,
    true);
sequence.add(initialFramework);
```
Note that we have configured the sequence to have the following properties:

* In the sequence, each argumentation framework must be a normal expansion of its predecessor.
* The conclusion of an argumentation framework is determined by expanding the framework until a
  unique (exactly one) extension is determined that implies consistent inferences considering previous conclusions;
  *i.e.*, rejecting an argument of a previous conclusion requires the inclusion of a new argument in the conclusion, or
  a change in contexts.
* Extensions of argumentation frameworks are determined using rational conflict-free (RCF) semantics.
* We allow for the definition of contexts.

Now, we resolve the initial framework:

```java
sequence.resolve(0)
```

The system adds an annhilator argument to the initial framework to generate a normal expansion that has exactly one
extension, given the specified semantics.
In our case, the system adds the argument an_a (the name may differ) to the framework, and the attack (an, a).
It follows that the conclusion is \{an_a, b\}.


The full source code of the example is available [here](./examples).

## Development and Testing
Checkout the source code:

``git clone git@github.com:TimKam/diarg.git``.

Build the project:

``./gradlew build``

Run unit tests:

``./gradlew test``

## License

[BSD 2-Clause License](./LICENSE)

## Authors

* [Timotheus Kampik](https://github.com/TimKam/)
