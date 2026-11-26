# The Adapter / Facade Clinic — Week 12, Part D

Fifteen points. Graded on **measurement and precision**, not prose. The rule from Week 9 still
holds: **a number without the command that produced it earns half marks.**

---

## D1 — Three classes with the same shape · 5 pts 🔎

**This is the highest-value step in the clinic, and it is the most common patterns-interview
question in existence.** You have written all three answers.

**Step 1.** Put these three declarations in a table, from your own repo, with file and line:

```java
class WeaponDecorator        implements ___Weapon___  { ___protected final Weapon___ wrapped; }   // Week 9
class LegacyBestiaryAdapter  implements ___MonsterSource___  { ___private final LegacyBestiary___ adaptee; }   // this week
class GameSession                               { eight methods/collaborators }        // this week
```

**Step 2.** For the first two, answer one question: **is the held type the same as the
implemented type?**

| | implements | holds | same type? |
|---|---|---|---|
| `WeaponDecorator` | Weapon|Weapon | yes|
| `LegacyBestiaryAdapter` |MonsterSource |LegacyBeastiary |no |

**Step 3.** That answer is the whole distinction. Write it as one sentence beginning *"A
decorator implements the same interface as the thing it holds, which is why…"*

> __________________________________A decorator implements the same interface as the thing it holds, which is why it can stack, while an adapter uses a second interface to translate between two different interfaces.__________________________________

**Step 4 — the consequence, not the definition.** `Flaming(Frost(sword))` is a legal weapon.
Write down what `new LegacyBestiaryAdapter(new LegacyBestiaryAdapter(b))` does.

It does not compile

**Try it. Paste the compiler error.**

```
error: incompatible types: LegacyBeastiaryAdapter cannot be converted to LegacyBeastiary
```

**Now say why that error is a property of the pattern and not a limitation of your code.**


The class of the output from an adapter is different from the class of the object it takes as a parameter. 

**Step 5.** `GameSession` holds eight things and implements nothing. In one sentence, what makes
it a facade rather than "a class with a lot of fields"?

The intent behind the pattern, to group together classes that all typically work together as a unit, is what makes the
difference and each of the subsystem classes are still public and accessible to individual method calls or other facades.

---

## D2 — The missing column · 3 pts

**Step 1.** Name the three honest options for a target field with no source. ___Do not record/use, derive it and say so, or use a default without saying so.___

**Step 2.** You took one. Paste the method.

```java
   private int experienceFor(int hp, int attack, String legacyId) {
    notes.add(legacyId + ": xp derived (no xp column in the export)");
    return Math.max(1, hp / 2 + attack);
```

**Step 3.** Now read Head First p. 249 — `EnumerationIterator.remove()`. **Which of your three
options did Freeman & Robson take, and why was theirs available when yours was not?**

They chose to not use it and threw an error. They could refuse because Iterator lets a method fail 
after compilation, at runtime. But because MonsterDef needs an int for xp at comiple time we did not have that choice.

**Step 4.** Try `Arrays.asList("a","b").add("c")`. **Paste the exception.** Which option is that?

java.lang.UnsupportedOperationException / it is refusal to record/use


**Step 5 — the one that is worth the points.** A colleague changes your derivation from
`hp / 2 + attack` to `hp / 3 + attack`. Every test still passes.

**Is that a problem? Answer yes or no and defend it in two sentences.**

Yes, because nothing else in the code is going to check to make sure the derivation is still correct, you would have to hard code the unit tests. 
translationNotes() lets a human understand this as it is hidden in gameplay/output. 

---

## D3 — The listener nobody subscribed · 4 pts 🔨

**Step 1.** Before US-9.2, how many times did `DamageMeter` appear anywhere outside its own
file? ___0___ **Command:** ___0___

**Step 2.** It compiled, it was correct, and it had never run. **Name every mechanism in this
project that could have caught it, and say why each one did not:**

| Mechanism | Why it did not catch this |
|---|---|
| the Java compiler | class was legal just unused, compiler doesn't check for method usages|
| the 137-test suite | Every test we had was asserting correct behavior from each class, not whether they were wired together to work in unison.|
| code review | We added damage meter correctly we just forgot to subscribe it, which is in a different file|
| the acceptance-criteria harness |there was no acceptance-criteria to assert that we had subscribed to damagemeter |

**Step 3 — the experiment.** In your finished build, comment out `bus.subscribe(meter)` in
`GameSession.start()` and run `mvn test`.

**How many tests fail?** ___2.___ 

**Name them.** 

___theDamageMeterActuallyFiresNow(), theSessionSubscribesTheListenerThatNobodyEverWiredUp()___

[ERROR]   AdapterFacadeTest.theDamageMeterActuallyFiresNow:201 DamageMeter has existed since Week 5 and had never fired, because main() never subscribed it. The facade owns the listener list now. ==> expected: <true> but was: <false>                                                             
[ERROR]   AdapterFacadeTest.theSessionSubscribesTheListenerThatNobodyEverWiredUp:187 quests, achievements, log, DAMAGE METER and the transcript; got 4 ==> expected: <true> but was: <false>                                                                                                          
[INFO]
[ERROR] Tests run: 158, Failures: 2, Errors: 0, Skipped: 0

**Put the line back.**

**Step 4.** You just demonstrated the difference between the old wiring and the new wiring, and
it is **not** that the new one cannot be wrong. State what it actually is, in one sentence.

The new wiring can still be wrong but now we can point a unit test at the previous gap and locate where an issue is. 

**Step 5.** A count of `bus.subscribe` calls is not evidence that a listener works. Which of your
acceptance criteria *is* that evidence, and what makes it different in kind?

___A3: Given a session whose player is below the damage threshold, when a DAMAGE_TAKEN event is published, then the warning appears in the transcript. (A count of subscriptions is not proof that one of them works.)___

This is different from a count of subscriptions because we are checking for behavior vs checking the count.

---

## D4 — What each pattern cost · 3 pts

**Step 1.** Paste `git diff --stat` for your US-9.1 commit and your US-9.2 commit.

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat feat/us-9.1
 docs/adapter-facade-clinic.md                       |  57 ++++++++++++++++++++++++---------
 docs/evidence.week12.md                             |  79 ++++++++++++++++++++++++++++++++++-----------
 src/main/java/dungeonforge/Main.java                | 126 +++++++++++++++++-------------------------------------------------------
 src/main/java/dungeonforge/core/GameWorld.java      |   8 +++--
 src/main/java/dungeonforge/session/GameSession.java | 151 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 src/test/java/dungeonforge/AdapterFacadeTest.java   | 139 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++--
 src/test/java/dungeonforge/CommandTest.java         |   8 +++--
 src/test/java/dungeonforge/DecoratorTest.java       |   3 +-
 src/test/java/dungeonforge/FactoryTest.java         |   8 ++---
 src/test/java/dungeonforge/SingletonTest.java       |   7 ++--
 src/test/java/dungeonforge/StateTest.java           |   8 +++--
 src/test/java/dungeonforge/TemplateMethodTest.java  |   9 +++---
 12 files changed, 449 insertions(+), 154 deletions(-)
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat feat/us-9.2
 docs/adapter-facade-clinic.md                     | 57 ++++++++++++++++++++++++++++++++++++++++++---------------
 docs/evidence.week12.md                           | 79 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++-------------------
 src/main/java/dungeonforge/Main.java              |  3 ---
 src/test/java/dungeonforge/AdapterFacadeTest.java | 46 ++++++++++++++++++++++++++++++++++++++--------
 4 files changed, 140 insertions(+), 45 deletions(-)
```

**Step 2.** `GameSession.start()` builds eight objects and a dungeon, every time it is called.
**Write a test that needs only a `Player` and an `EventBus`.** Can you still do it?

**What does that tell you about the cost of a front door?**

Yes, it will still work because the subsystems are still public and accessible to anything that wants to call them. 
However, when we call for a GameSession to start() the game we will only get a "full session" that starts everything 
exactly as it lays them out.

**Step 3.** The Principle of Least Knowledge says `Main` should not have to know eight classes.
It now knows one. **Name one diagnostic or debugging task that got harder**, specifically.

Trying to debug the whole wiring of the Gamesession.start() method, instead of semi-modular pieces of code in main, we have an
all or nothing approach with the facade. 

**Step 4 — the sentence to take into the final.** Adapter and Facade are both "a class standing
in front of other classes." In one sentence each, from having written both:



| Pattern | What it is actually for |
|---|---|
| Adapter |adapting one class interface to fit and work in a consistent manner with a different kind |
| Facade |taking the calls a client code has to make and reducing them to one call to a commmon interface. |

**And: which of the two did your codebase genuinely need, and which was mostly tidiness?**
Defend the answer with a number from Part 3.

Our game's code really needed the facade method to fix the damagemeter that never fired and having the turn sequence in two copies, which let a player walk past a live monster when using a script for input.  ___Before US-9.2, how many times did DamageMeter appear anywhere outside its own file? 0___

The adapter pattern we implemented was just to incorporate the new monsters from an old bestiary file that was still relevant to our game. New content, no bug fixes or feature adding.

---

## Turning it in

Commit `docs/adapter-facade-clinic.md` with D1–D4 complete, the compiler error in D1 Step 4
pasted verbatim, the exception in D2 Step 4 pasted verbatim, and `bus.subscribe(meter)` **put
back** in `GameSession`.
