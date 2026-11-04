# Week 10 Evidence

Parts 1–3 and Part 5 are graded on the measurement, not the code, and all of them happen
**before** you write anything.

---

## 0. The Sprint 7 carry-over (US-7.5)

Before any Template Method work. `docs/sprint-08-plan.md` has the story.

```bash
grep -rn 'unwrap' src/ | grep -v 'public Weapon unwrap'
grep -rl 'WEEK 9 STARTER' src/main/java | wc -l
```

**Paste the output of the first command.** How many callers does `unwrap()` have? ______

**And its javadoc says it is "used by undo". Open `TurnSnapshot.java`. Is it?** ______

**One sentence on why dead code with a comment vouching for it is worse than dead code.**


**The second command prints a number.** ______ Open any one of those files and read its header.
**Name one sentence in it that is no longer true, and say what made it stop being true.**


### The limit you will find

Build `disenchant`, then run this in the game:

```
enchant shortsword with frost
enchant shortsword with flaming
disenchant shortsword
```

**What is in your hand now?** ______

**Now get to a weapon that is Flaming but not Frost, starting from Flaming(Frost(sword)), using
only `disenchant`.**

**Write down what happened and why.** *(One sentence. It is the same sentence as US-7.4's,
pointed the other way.)*


---

## 1. BEFORE — run the tests, then run the diagnostic three times

```bash
mvn test
mvn -q exec:java -Dexec.args="--report"
mvn -q exec:java -Dexec.args="--report --seed=3"
mvn -q exec:java -Dexec.args="--report --seed=7"
```

**How many tests pass?** ___110___

**Paste all three dungeon reports:**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ mvn -q exec:java -Dexec.args="--report"
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper (file:/usr/share/maven/lib/guava.jar)
WARNING: Please consider reporting this to the maintainers of class com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 20260818


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     4 !!       0    yes
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 4   spawn-camped levels: 0   unreachable exits: 0
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip?


chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ mvn -q exec:java -Dexec.args="--report --seed=3"
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper (file:/usr/share/maven/lib/guava.jar)
WARNING: Please consider reporting this to the maintainers of class com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 3


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     4 !!       0    yes
  3      Frost             8        0    1 !!    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 4   spawn-camped levels: 1   unreachable exits: 0
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip
  
  chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ mvn -q exec:java -Dexec.args="--report --seed=7"
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper (file:/usr/share/maven/lib/guava.jar)
WARNING: Please consider reporting this to the maintainers of class com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 7


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     5 !!       0  NO !!
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 5   spawn-camped levels: 0   unreachable exits: 1
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip?
```

**How many of the four levels satisfy every invariant, on each seed?**
default: ___3___   seed 3: ___2___   seed 7: ___3(cannot reach the last 2)___

**Seed 7 shows a symptom the other two do not.** Look at the EXIT column. What does it mean for
a player who reaches that level?

A player who reaches the Forge level when the seed is 7 will not be able to leave the room and advance to the Winterfell level

**Now put the test count and the default-seed result in one sentence.** *(This is the most
important sentence you will write this week. Two points.)*

> While all of our unit tests passed we had an invariant in the Forge level with 4 orphans left behind after we leave the level. 

**The three reports disagree. Why?** *(Look at what decides whether a room gets monsters. Then say
what that implies about how long a bug like this can survive.)*

The reports disagree because each seed exposes a different bug with the level and monster creations. A bug like this could survive this long because we have similar algorithms which have drifted into conflicts, something we probably can fix with the template pattern.

**What does all of this tell you about the test suite you have been building for eight weeks?**

Our test suite, specifically the creation unit tests did not test for orphaned rooms and monsters. We did not account for this mismatch of states and rooms when implentmented undo as well.

---

## 2. BEFORE — find the drift

Open `core/GameWorld.java`. The three `build*Level` methods are 90% the same code. Put two of
them side by side in two editor windows.

**Fill this in by reading. Do not guess.**

| Step | Corridor | Cave | Ring |
|---|---|---|---|
| 1. create the rooms |r = 0 | r = 1|r = 0 |
| 2. connect them | linked in a north/south fashion | linked randomly | linked in a circular coridor |
| 3. entrance flavour |room.get(0) | room.get(0)| room.get(0)|
| 4. populate — **which index does the loop start at?** |r = 1 | r = 1|r = 0 |
| 5. prune unreachable rooms |Yes | No| Yes|

**Which step is genuinely different between the three?** ___2___

**Which differences are NOT about the shape of the level?**




**For each, say what the consequence is for a player:**

| Drift | What the player experiences |
|---|---|
| populate starting at different indexes| orphaned rooms, disconnected rooms|
|cave missing the pruneUnreachable() method |unable to advance levels or rooms |

**Neither of these is a typo. Both are what happens when one algorithm exists in three copies. In
one sentence: why did the compiler not help?**

The compiler did not help because we wrote the steps separately and did not enforce the order/steps, as we saw in the textbook we need the template pattern to make sure an order/algorithm stays untouched and "final"


**A faster way to find this class of bug, which you should keep:**

```bash
grep -n 'for (int r = [01]; r < rooms.size(); r++)' src/main/java/dungeonforge/core/GameWorld.java
```

**Paste the output. What does the pattern of 0s and 1s tell you?**

````bash
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ grep -n 'for (int r = [01]; r < rooms.size(); r++)' src/main/java/dungeonforge/core/GameWorld.java
96:        for (int r = 1; r < rooms.size(); r++) {
120:        for (int r = 1; r < rooms.size(); r++) {
133:        for (int r = 1; r < rooms.size(); r++) {
180:        for (int r = 0; r < rooms.size(); r++) {
````
The 0 and 1s tell us that we have an invariant which will cause unintended behavior.

---

## 3. BEFORE — the counting

```bash
wc -l src/main/java/dungeonforge/core/GameWorld.java
grep -c 'new Room("L"' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'createRoomFlavor()' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'populatorFor(' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'switch (d % ' src/main/java/dungeonforge/core/GameWorld.java
```

| | Before | After |
|---|---|---|
| `GameWorld.java` lines |269 | |
| times room creation is written |3 | |
| times entrance flavour is written |3 | |
| times the populate loop is written |4 | |
| `switch` statements over level shape |1 | |
| levels satisfying every invariant (seed 3) |2 of 4 | |

*(Fill the After column at the end of the sprint. Nothing in it may be a guess.)*

---

## 4. AFTER — US-8.4, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth shape:**

```

```

**New files:** ______  **Modified files:** ______  **Lines added:** ______

**Is `GameWorld.java` in that diff?** ______ **Why not?**


### The other half of the receipt — what the old design would have cost

**(a)** To add a spoke shape to the starter, which existing method would you have copied?
*(Be honest — you would copy the one whose shape is closest, not the one that is correct.)*

______

**(b)** Roughly how many lines would that have added to `GameWorld.java`? ______

**(c)** **The question that matters.** Look at the method you named in (a). Run the `grep` from
Part 2 on it. Does it have any of the drift you catalogued?

______

**So what would have happened to the new level's invariants?**


**(d)** Now check your answer against reality. Your instructor measured it: the fourth shape was
added to the starter by copying `buildRingLevel`, and on **the default seed** the report went
from `orphaned rooms: 4, spawn-camped levels: 0` to `orphaned rooms: 4, spawn-camped levels: 1`
— **the newly spawn-camped level being level 4, the one that had just been added.**
`GameWorld.java` went from 269 lines to 317, the count of populate loops starting at `r = 0`
went from 1 to 2, and **it compiled on the first attempt.**

> Write the conclusion as a sentence with the word *propagate* in it.

---

## 5. AFTER — what you did not write

Your fourth shape produces levels with no orphaned rooms and an empty entrance.

**Find the reachability check in `SpokeGenerator.java`.** Where is it? ______

**Find the entrance guard in `SpokeGenerator.java`.** Where is it? ______

**In two or three sentences: explain how a class can satisfy an invariant it does not implement,
and what had to be true of `generate()` for that to work.**


---

## 6. AFTER — the honest accounting

Two of the starter's levels were broken. Both are fixed now.

**Did you write a bug fix? Quote the lines if you did.**


**If you did not, describe what you actually did instead.** *(There is a precise way to say this,
and it involves the word "deleted".)*


---

## 7. Retro line

**First, the carry-over.** One sentence on what `disenchant` taught you about Decorator that
building the decorators did not.




**One sentence on something Template Method gave you, and one on something it took away.** *(It
does take something. Head First is clear about it on p. 302 — a subclass cannot change the order
of the steps, and one day you will want to.)*
