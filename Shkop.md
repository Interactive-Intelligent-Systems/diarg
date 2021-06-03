# The Shkop Approach
DiArg supports the Shkop approach to abstract argumentation.
This tutorial explains the approach *hands-on*.

A key question in abstract argumentation is how to determine the extensions of an argumentation framework.
To answer this question, the Shkop approach first simplifies the problem by assuming that any argumentation process is
sequential, *i.e.*, that instead of one argumentation framework, we resolve a *sequence of argumentation frameworks* and
that given an argumentation framework sequence AFS = <AF<sub>0</sub>, ..., AF<sub>n</sub>>, AF<sub>0</sub> contains
exactly one argument and each AF<sub>i</sub>, 0 < i &le; n, has the following properties:

* AF<sub>i</sub> is a normal expansion of AF<sub>i-1</sub>;
* AF<sub>i</sub> contains exactly one more argument than AF<sub>i-1</sub>.

We call any argumentation framework sequence with these properties a *Shkop sequence*. The first argumentation framework
in any Shkop sequence can be resolved by determining the unique grounded extension (because the framework contains
exactly one argument, the unique grounded extension is empty in case of a self-attack; otherwise, it contains the only
argument). Then, we proceed as follows, starting with i=1:

2. Check if the currently inferred set of arguments passes the *Shkop test*, given the current argumentation framework.
   As a 'pre-test' to the Shkop-test, we check if an upstream attacker is added to the argumentation framework
   (ignoring self-attacking arguments). If this is the case, the pre-test fails, which implies failure of the Shkop
   test. The default Shkop test checks if the set of arguments is conflict-free with the grounded extension of the
   current argumentation framework, from which all self-attacking arguments, as well as all alread
   'discarded' upstream arguments have been removed (custom Shkop tests can be implemented). If the argument fails the
   Shkop test, we need to reject the current extension, *i.e.* we set the new extension to *null*; to mitigate the null
   pointer, we may move the new argument to the beginning of our Shkop sequence, and start anew. If our inferred set of
   arguments passes the Shkop test, we add the new argument to the inference set, if it does not have any conflicts with
   this set.
3. If i < n, set i := i+1 and go to 1.

We call this approach *sequential Shkop*.
With DiArg, we can create and resolve Shkop sequences as follows, for example:

```java
AFSequence shkopSequence = new AFSequence(SequenceType.SHKOP, ResolutionType.SHKOP, new Semantics(SemanticsType.SHKOP),
                           true);

DungTheory framework0 = new DungTheory();
DungTheory framework1 = new DungTheory();
DungTheory framework2 = new DungTheory();

Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");

framework0.add(a);

framework1.add(a);
framework1.add(b);
framework1.add(new Attack(a, b));
framework1.add(new Attack(b, a));

framework2.add(framework1);
framework2.add(c);
framework2.add(new Attack(b,c));
framework2.add(new Attack(c,a));

shkopSequence.addFramework(framework0);
shkopSequence.addFramework(framework1);
shkopSequence.addFramework(framework2);

shkopSequence.resolveFrameworks();

```

To turn the Shkop approach into an abstract argumentation semantics, we proceed as follows, given any argumentation
framework AF = (AR, AT):

1. Determine all permutation sequences of the set of arguments AR.
2. For every permutation sequence <a<sub>0</sub>, ..., a<sub>m</sub>>, proceed as follows:  
    2.1.  Generate a Shkop sequence <AF<sub>0</sub>, ..., AF<sub>m</sub>>, where for any AF<sub>j</sub>,
          0 &le; j &le; m, AF<sub>j</sub> is the restriction of AF to the set of arguments in
          <a<sub>0</sub>, ..., a<sub>j</sub>>.  
    2.2.  Determine the *Shkop resolution* of the Shkop sequence by applying the sequential Shkop approach.
3. Return the set of all *Shkop resolutions* as the argumentation framework's extensions, ignoring the Shkop
   resolutions that are *null*.

With DiArg, the Shkop extensions of an argumentation framework can be determined as follows, for example:

```java
SimpleShkopReasoner shkopReasoner = new SimpleShkopReasoner();

DungTheory framework = new DungTheory();

framework.add(a);
framework.add(b);
framework.add(c);
framework.add(new Attack(a, b));
framework.add(new Attack(b, a));
framework.add(new Attack(b, c));
framework.add(new Attack(c, c));

Collection<Extension> extensions = shkopReasoner.getModels(framework);
```
   
To customize the Shkop test, we can use the abstract ShkopTest class and implement our ShkopTest analogously to the
[GroundedShkopTest](./src/main/java/diarg/GroundedShkopTest.java) class. Then, we can set the custom ShkopTest as
follows:

```java
ShkopTest shkopTest = new MyShkopTest();
shkopReasoner.setShkopTest(shkopTest);
```

