# Principle-based Explanations of Non-Monotonic Inference in Abstract Argumentation
DiArg provides abstractions that, given an abstract argumentation framework `AF` and one of its normal expansions `AF'`
and an argumentation semantics sigma, allow for selecting sigma-extensions from `AF'` that are maximally monotonic wrt.
a previously inferred sigma-extension of `AF`. In this context, DiArg can also identify specific arguments that have
been added when expanding `AF` to `AF'` and that can explain the violation of monotony of inference (if this principle)
is indeed violated.

The following example showcases these features. For instance, let us start with the argumentation framework
`AF = (AR, AT) = ({a, b, c, d}, {(a, b), (b, a)})`:

```java
Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");
Argument d = new Argument("d");
DungTheory framework1 = new DungTheory();
framework1.add(a);
framework1.add(b);
framework1.add(c);
framework1.add(d);
framework1.add(new Attack(a, b));
framework1.add(new Attack(b, a));
```

Using preferred semantics, we infer 'either `{a, c, d}` or `{b, c, d}`':

```java
Semantics preferredSemantics = new Semantics(SemanticsType.PREFERRED);
preferredSemantics.getModels(framework1)
```

Let us assume we select `{a, c, d}` as our extension.

```java
Extension af1Extension = preferredSemantics.getModel(framework1);
```

Now, we normally expand `AF'` to  `AF' = (AR', AT') = ({a, b, c, d, e, f, g}, {(a, b), (b, a), (e, c), (e,
d), (e, f), (f, a), (f, e), (f, g), (g, f)})`:

```java
Argument e = new Argument("e");
Argument f = new Argument("f");
Argument g = new Argument("g");
DungTheory framework2 = new DungTheory();
framework2.addAll(framework1);
framework2.add(e);
framework2.add(f);
framework2.add(g);
framework2.add(new Attack(e, c));
framework2.add(new Attack(e, d));
framework2.add(new Attack(e, f));
framework2.add(new Attack(f, a));
framework2.add(new Attack(f, e));
framework2.add(new Attack(f, g));
framework2.add(new Attack(g, f));
```

Preferred semantics allows us to infer 'either `{a, e, g}`, or `{b, e, g}`,  or `{b, c, d, f}`':

```java
Collection<Extension> af2Extensions = preferredSemantics.getModels(framework2);
```

Given the constraints of the semantics, we are forced to violate monotony. Intuitively, a reasonable  decision is to
infer `{b, c, d, f}` because it minimizes the number of arguments (w.r.t cardinality) that are part of  the previous
conclusion but not part of the current conclusion, *i.e.* `|{ a, c, d } \ { b, c, d, f }| < | { a, c, d } \
{ a, e, g } | < | { a, c, d } \ { b, e, g } |`:

```java
Collection<Extension> maxMonExtensions = Utils.determineCardinalityMaxMonotonicExtensions(af2Extensions, af1Extension);
```

To *explain* the violation of monotony, we can proceed by highlighting *monotony violation explanations*, *i.e.*
arguments that satisfy the following constraints:

* The argument has been newly added.
* The argument attacks an argument that was in the previous conclusion.
* The argument is not attacked by any subset of arguments in the new conclusion that defends itself against all
  attackers and is not in conflict with the previous conclusion.

The *monotony violation explanations* of the conclusion `{b, c, d, f}` as inferred from `AF'` w.r.t. the previous
conclusion `{a, c, d}` that has been inferred from `AF` are the arguments comprising the set `{e, f}`:

```java
Extension maxMonExtension = maxMonExtensions.iterator().next();
AFTuple afTuple = new AFTuple(framework1, framework2);
Collection<Argument> explanations = NonmonotonyExplainer.determineMonotonyViolationExplanations(afTuple, af1Extension, maxMonExtension);
```

The complete code for this example is available in the [examples directory](https://github.com/Interactive-Intelligent-Systems/diarg/tree/master/examples)
and can be executed by running `./gradlew explainabilityExample`.