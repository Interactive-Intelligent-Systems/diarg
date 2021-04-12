# Burdens of Persuasion in DiArg
DiArg provides abstractions for modeling *burdens of persuasion*. The burden of persuasion is a crucial concept in legal
reasoning, and allows us to place *burdens* on a set of arguments in an argumentation framework. If in doubt -- *i.e.*,
given a somewhat credulous argumentation semantics and a somewhat 'ambiguous' argumentation framework-- the burden of
persuasion approach dictates to infer unburdened  arguments to the extent possible (roughly speaking). Arbitrary levels
of burdens are possible. For example, we may  have some arguments that are unburdened, some arguments that are burdened
with a level 1 burden, and some arguments that are burdened with a level 2 burden.

## Example
Let us introduce an example that illustrates how the burden of persuasion approach works.  Because we want to highlight
technical aspects, we stay on an abstract level and do not introduce a paritcular real-world problem. Consider the
abstract argumentation framework `AF_2 = ({a, b, c, d, e, f}, {(a, c), (a, e), (c, d), (d, b), (d, f), (e, a), (e, c),
(f, d), (f, b)})`. Let us assume `{a, b}` is unburdened, and let us place a level 1 burden on `{c, d, e}` and a level 2-
burden on `{f}`. In DiArg, we create the following Burden of Persuasion Framework (BPF):

```java
BurdenOfPersuasionTheory bpFramework;
Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");
Argument d = new Argument("d");
Argument e = new Argument("e");
Argument f = new Argument("f");
Collection<Attack> attacks = new ArrayList<>();
attacks.add(new Attack(a, c));
attacks.add(new Attack(a, e));
attacks.add(new Attack(c, d));
attacks.add(new Attack(d, b));
attacks.add(new Attack(d, f));
attacks.add(new Attack(e, a));
attacks.add(new Attack(e, c));
attacks.add(new Attack(f, d));
attacks.add(new Attack(f, b));
Collection<Argument> burden1 = new ArrayList<>();
Collection<Argument> burden2 = new ArrayList<>();
Collection<Argument> burden3 = new ArrayList<>();
burden1.add(a);
burden1.add(b);
burden2.add(c);
burden2.add(d);
burden2.add(e);
burden3.add(f);
ArrayList<Collection<Argument>> burdens = new ArrayList<>();
burdens.add(burden1);
burdens.add(burden2);
burdens.add(burden3);
bpFramework = new BurdenOfPersuasionTheory(burdens, attacks);
```
In semi-formal notation, we can denote the burden of persuasion framework by `AF_bp = (({a, b}, {c, d, e}, {f}), 
{(a, c), (a, e), (c, d), (d, b), (d, f), (e, a), (e, c), (f, d), (f, b)})`.
We can determine the burden of persuasion extensions as follows (using SCF2 semantics, for example):

```java
Semantics scf2 = new Semantics(SemanticsType.SCF2);
Collection<Extension> extensions = bpFramework.determineExtensions(scf2);
```

The only extension is `{a, d}`.

The complete code for this example is available in the [examples directory](https://github.com/Interactive-Intelligent-Systems/diarg/tree/master/examples).

Let us provide a rough intuition of how the solver determines the burden of persuasion extensions. Again, we apply SCF2
semantics, but for the BPF, applying preferred semantics would not make a difference at any of the steps that follow.
This may help the reader to follow along.  Based on `AF_bp`, we generate the following argumentation frameworks:

* `AF_0 = ({a, b}, {})`;
* `AF_1 = ({a, b, c, d, e}, {(a, c), (a, e), (c, d), (d, b), (e, a), (e, c)})`;
* `AF_2 = ({a, b, c, d, e, f}, {(a, c), (a, e), (c, d), (d, b), (d, f), (e, a), (e, c), (f, d), (f, b)})`, as defined
  above.

We determine the SCF2 extensions of the three argumentation frameworks:

* The SCF2-extensions of `AF_0` are `{{a, b}}`.
* The SCF2-extensions of `AF_1` are `{{a, d}, {e, d}}`.
* The SCF2-extensions of `AF_2` are `{{a, d}, {a, f}, {e, d}, {e, f}}`.

Then, we determine the SCF2-extensions of `AF_2` that are *maximally monotonic* (w.r.t. set inclusion) considering any
SCF2-extension of `AF_0` and also Pareto optimal among the maximally monotonic extensions. This leaves us with the
following two extensions:

`{{a, d}, {a, f}}`

Roughly speaking, we have to reject `b` in any case, but can potentially infer `a`. Hence, we remove all extensions that
do not entail `a`.  Then, we see if we can remove more extensions by trying to be pareto optimally maximally monotonic
w.r.t. the  SCF2-extensions of `AF_1` while *still considering that we must be maximally monotonic w.r.t. the
SCF2-extensions of* `AF_0`. This allows us to remove `{a, f}` and leaves us with the final inference result `{{a, d}}`.