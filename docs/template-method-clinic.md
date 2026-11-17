# The Template Method Clinic — Week 10, Part D

Four experiments. D3 is the one worth doing carefully — it is the only experiment this semester
where you deliberately break your own code to see what was holding it together.

---

## D0 — The carry-over, and the limit it exposes · 4 pts

You built `disenchant` in US-7.5. This step is about what building it told you.

**Step 1.** Before US-7.5, `WeaponDecorator.unwrap()` had how many callers? ___0___
And its javadoc claimed what? ___Might have be useful for future feature ___

**Step 2.** Write the two implementations of "is there anything to remove?" side by side:

```java
// (a) the type test
if (w instanceof WeaponDecorator d) { ... }

// (b) what you actually wrote
Weapon peeled = w.peel();
        if (peeled == w) {
bus().message("The " + w.getName() + " has nothing on it to disenchant");
        return false;
        }
```

**One sentence on why (b) is better**, in terms of what each one would need changing if a third
kind of `Weapon` were added next semester.

This removes the need to constantly update the type check with every instance of decorated weapon if/when new weapon types are added.

**Step 3.** Start from `Flaming(Frost(sword))`. Using only `disenchant`, reach a weapon that is
Flaming but not Frost.

**What happened?** ___You cannot reach flaming weapon given this situation, we have to "peel" the flaming first and then Frost.__

**Step 4 — the one that is worth the points.** US-7.4 ended with the sentence *a decorator can
only see what is underneath it*. Rewrite that sentence so it describes what you just found out
about **removal** rather than about damage numbers.

> A decorator only knows about the object beneath it and it can only assert whether the object it tried peeling is aleady a base object or it was a decorated object.

**Step 5.** Name one feature a game might reasonably want that this limit makes awkward, and say
what you would have to do to support it. *(You do not have to build it. "Decorator is the wrong
tool for that" is an acceptable and correct answer, and saying it about a pattern you just spent
a week on is the point.)*

Trying to remove a nested decoration is a feature we may want to add in the future because enchantments should stack but also not be linear. The decorator is the wrong tool for the job here if that is our intended direction, it limits how our players play the game.

---

## D1 — The Template Method you already wrote · 5 pts

You have been using this pattern since Week 7. You did not know its name.

**Step 1.** Open `commands/AbstractCommand.java` and read `execute()`.

**Step 2.** Fill this in from that file, not from the reading.

| | In `AbstractCommand` |
|---|---|
| The template method | execute|
| Is it final? |yes |
| The primitive operation subclasses must implement | doExecute|
| How many steps does the template fix the order of? |3 |
| What happens to a subclass that forgets a step? |does not compile if it is abstract |

**Step 3.** `AbstractCommand.execute()` snapshots, acts, and then lets the monsters respond — **in
that order, always.**


**Name one bug that ordering makes impossible.** *(Hint: think about what undo needs, and when
the snapshot has to be taken.)*

We require a end to have ended for a snapshot to be recorded, if a turn hasn't ended then we end up with a snapshot from a midturn.

**Step 4.** In one sentence: why did Week 7 need this pattern before Week 10 taught it?

> *Answer:* We needed the template method in week 7 to ensure that our order was final so that game flow was guaranteed. We needed certain commands to react to the flow of the game and by ensuring the order we prevent unforseen bugs that future additons could bring if they mess with execute()'s order.

---

## D2 — Three copies, three different mistakes · 6 pts

**Step 1.** From Part 2 of your evidence sheet, reproduce the count:

| Builder | Steps it performs | Steps it skips |
|---|---|---|
| `buildCorridorLevel` |1 | 6|
| `buildCaveLevel` | 3| 5|
| `buildRingLevel` | 1| 6|

**Step 2.** Notice that no two of them skip the *same* step. Finish this sentence:

> With 3 copies of one algorithm, the set of steps that *some* copy gets wrong grows
> linearly, because each levelgenerator implements its own connectRoom().

**Step 3.** A tempting fix: add a comment at the top of each method listing the five steps in
order. Say in one sentence why that does not work.



**Step 4.** A second tempting fix: extract the five steps into five helper methods and have each
builder call them in order. That is genuinely better than what is there now.



**But it does not solve the problem.** Say why in one sentence, then name the thing Template
Method adds that this fix does not.

> *(This is the exam question for this week. The answer is one word long and it appears on the
> signature of `generate()`.)*

>final 

---

## D3 — Remove the `final` and watch · 6 pts 🔨

**Do this after US-8.2 is finished and committed, on a scratch branch you will throw away.**

**Step 1.** Confirm you are starting clean, on both seeds.

```bash
mvn -q exec:java -Dexec.args="--report"
mvn -q exec:java -Dexec.args="--report --seed=3"
```

Record the orphan and spawn totals: default ___0___ / ___0___   seed 3 ___0___ / ___0___

**Step 2.** Remove `final` from `LevelGenerator.generate(...)`.

**Step 3.** Write one more shape — call it `SloppyGenerator` — that extends `LevelGenerator`,
implements `connectRooms` however you like, **and overrides `generate()`** to create rooms,
connect them, populate them, and stop there.

You will find you cannot call the template's private steps, so you will have to write your own
versions. **Write them badly on purpose** — populate from index 0, skip the reachability check.
That is the whole point of the experiment.

**Step 4.** Register it and run `--report` again on both seeds.

| | orphans | spawn |
|---|---|---|
| with `final` | 0| 0|
| without `final` |0 |0 |

**Step 5.** Answer three questions.

1. Which of your existing tests failed? 

TemplateMethodTest.theTemplateMethodIsFinalSoNoSubclassCanReorderTheSteps
TemplateMethodTest.theRegistryHoldsOneEntryPerShape

2. Which test told you the `final` was gone? 

TemplateMethodTest.theTemplateMethodIsFinalSoNoSubclassCanReorderTheSteps

3. Did the compiler complain at any point? ___No___

**Step 6.** Put the `final` back, delete `SloppyGenerator`, and run `mvn test`.

**Step 7.** One paragraph. You have just demonstrated that `final` on a template method is
load-bearing. Explain what it is bearing the load *of* — and note that the thing it protects is
not a behaviour but an **agreement**.

The final keyword is bearing the load of not only the order of the template steps, but it also enforces an agreement between the parent and child classes. It lets the child classes rely on the parent class for the concrete steps while also preventing the child classes from doing their own steps to prevent drift and unexpected behavior.

> **Why this experiment exists.** Every year somebody argues that `final` is over-restrictive and
> that a subclass should be trusted to call the steps in order. That argument is exactly what
> produced `buildCaveLevel`. You have now run the experiment instead of having the argument.

---

## D4 — What did not change, and what you did not write · 4 pts

**Step 1.** List every file outside `generate/` that you modified this sprint.

```bash
git diff --stat main -- src/main/java/dungeonforge/ ':!*generate*'
```

**Step 2.** Fill this in.

| File | Lines changed this sprint |
|---|---|
| `core/GameWorld.java` | 83|
| `core/Room.java` | 0|
| `core/DungeonLevel.java` | 0|
| `core/Combat.java` | 0|
| `commands/MoveCommand.java` | 0|
| `core/DungeonReport.java` | 107|

**Step 3.** `DungeonReport` does its own breadth-first search. So does `LevelGenerator`. That is
duplication, and this week was about removing duplication.

**Should `DungeonReport` call the generator's version instead? Argue one way in two sentences.**

No because sharing a checker between the two is restrictive in the case that we want/need a checker to do something a bit different. While there is potential for drift by using two separate checkers we also gain flexibility for when our game grows.

> *(There is a defensible answer in each direction, and the marks are for the argument. The case
> against sharing: a checker that shares code with the thing it checks cannot catch that code
> being wrong. The case for sharing: two definitions of "reachable" can drift, which is the exact
> disease this week diagnosed.)*

**Step 4.** The honest half. Head First, p. 302, is direct about what this pattern costs:
subclasses cannot change the order of the steps.

**Name one level shape you can imagine wanting, that the current template cannot express.** Then
say what you would change about `generate()` to allow it — and what you would lose.

If we wanted to add in a level that has shifting rooms, i.e. the rooms change how they are connected after they are initially connected. 

---

## Turning it in

D1–D4 go in `docs/evidence.week10.md` and in the PR description for the story they belong to.
Command output, not paraphrases of command output. **A number without the command that produced
it earns half marks.**
