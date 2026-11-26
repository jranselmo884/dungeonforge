# Week 12 Evidence

Fill this in as you go. Parts 1–3 and Part 5 are graded on the **measurement**, not on the code.

**Every counting command here pipes through `scripts/codeonly.py`.** The reason is in that
file's header: a bare grep for `ctx.syncState()` finds the javadoc that *mentions*
`ctx.syncState()`. Grep the code, not the commentary.

---

## 0. The Sprint 8 carry-over (US-8.5)

```bash
grep -rn 'MONSTER_SATE_CHANGED' src/main/java | wc -l
```

**How many files share the misspelling?** ___4___

**It works perfectly. In one sentence: what does it cost, and when?**


**Now the other one.** Before changing anything, write a test asserting
`EnchantmentRegistry.isKnown("FLAMING")`. Run it.

**Paste the failure.**

```

```

**One sentence: 137 tests passed and this did not work. What were those 137 tests about?**


---

## 1. BEFORE — count the listeners

```bash
ls src/main/java/dungeonforge/events/*.java | xargs grep -l 'implements GameEventListener' | wc -l
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'bus.subscribe('
```

**Listener classes in `events/`:** ___6___  **`bus.subscribe` calls in `Main`:** ___4___

**Those two numbers are equal and that is not reassuring. Why not?** *(Look at what the fourth
subscription actually subscribes, and where that class lives.)*



**Which listener class is never subscribed?** ___DamageMeter___

**Open it. What does it do, and when was it written?**

DamageMeter warns the player when their HP drops below the threshold and gives them a chance to act before they fall.

**Now the sentence. Put both counts and the consequence in one sentence.** *(This is the most
important sentence you will write this week. Two points.)*

> The counts were equal but the sets were not. One of the four subcriptions(listeners) is ConsolePrinter, which does not live in the events package. That means that one of the four listener classes was not subscribed, DamageMeter.

**Nobody broke it. In one sentence: what kind of mistake is this, if it is not a bug?**

This mistake is a result of drift and could be considered a design flaw, we learned that the facade pattern can simplify these listeners into a interface that keeps them away from main.

---

## 2. BEFORE — the two games

```bash
printf 'north\nlook\nnorth\nlook\n' > demo.txt
mvn -q exec:java -Dexec.args="--script=demo.txt"
```

**Paste the scripted output:**

```
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.12.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[script] > north
  You go north.
  Ghoul is now hunting.

[script] > look
  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (18/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[script] > north
  You go north.

  Delver  HP 80/80  ATK 10  DEF 3  Gold 0  XP 0  Carry 60.0kg  (unarmed)
  Actions this session: 3   |   undo stack depth: 2
```

Now run `mvn -q exec:java` and type the same four commands by hand.

**Paste that output:**

```
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.12.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[Exploring | HP 80/80] > north

  You go north.
  Ghoul is now hunting.
  Combat! 1 hostile(s). Try 'attack'.

[Combat | HP 80/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (18/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Combat | HP 80/80] > north

  You cannot do that in Combat mode. 
    Available here: attack, look, use, equip, inventory, status, history, undo, help, quit

[Combat | HP 80/80] > 
```

**They differ. Name the file, the method and the line that is the difference.** ___syncState()___

**Put the two turn sequences side by side:**

| | the turn |
|---|---|
| `gameLoop()` | |
| `runScript()` | |

**What can a script do that a player cannot?**


**`runScript`'s javadoc has been promising something since Week 7. Quote it, and say whether it
is true.**


---

## 3. BEFORE — the counting

```bash
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'ctx.syncState()'
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'history.push('
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'DamageMeter'
awk '/public static void main/{on=1} on{n++} on&&/^    }$/&&n>3{print n; exit}' src/main/java/dungeonforge/Main.java
```

| | Before | After |
|---|---|---|
| `main()` lines | 191| 133|
| `ctx.syncState()` in `Main` | 3|0 |
| copies of the turn sequence | 3| 0|
| `DamageMeter` mentioned outside its own file |0| 1|
| listener classes subscribed, of 4 | 3| 4|
| monster blueprints available |19 |29 |
| a script can walk past a live hostile | yes| no|

*(Fill the After column at the end of the sprint. **Nothing in it may be a guess.**)*

---

## 4. AFTER — the two receipts

### US-9.1, the Adapter

```bash
git diff --stat HEAD~1          # on the us-9.1 commit
diff src/main/java/dungeonforge/legacy/LegacyBestiary.java  <the file you were given>
diff src/main/java/dungeonforge/factory/MonsterFactory.java <the file you started with>
python3 scripts/codeonly.py src/main/java/dungeonforge/factory/MonsterFactory.java \
  | grep -ciE 'bestiary|csv|MOB_ID|HIT_DICE'
```

**Paste all four.**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat HEAD~1
 docs/evidence.week12.md                             |  14 ++++----
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
 11 files changed, 354 insertions(+), 127 deletions(-)
 chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff src/main/java/dungeonforge/legacy/LegacyBestiary.java
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff src/main/java/dungeonforge/factory/MonsterFactory.java
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ python3 scripts/codeonly.py src/main/java/dungeonforge/factory/MonsterFactory.java \
  | grep -ciE 'bestiary|csv|MOB_ID|HIT_DICE'
0
```

**Two of those commands are the deliverable. Which two, and why?**

git diff src/main/java/dungeonforge/legacy/LegacyBestiary.java 
git diff src/main/java/dungeonforge/factory/MonsterFactory.java 

Both are the deliverable because we get nothing as an output, we have not changed these two files, we are using LegacyBestiaryAdapter to have them work together.

**`blueprintCount()` before:** ___19___ **after:** ___29___

### US-9.2, the Facade

```bash
git diff --stat HEAD~1          # on the us-9.2 commit
```

**Paste it.**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat HEAD~1
 docs/evidence.week12.md                             |  35 ++++++++++++++------
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
 11 files changed, 373 insertions(+), 129 deletions(-)
```

**One sentence each on what the two numbers mean.** *(They mean different things. One is about a
format; one is about an order.)*



---

## 5. AFTER — what you did not have to change

**List every file that learned a CSV exists.** ___LegacyBeastiary, LegacyBeastiaryAdapter, GameSession___

**Is `MonsterFactory` one of them?** ___No___

**`ThemeKit` picks which monsters appear in a room. How many lines of it did you change to make
ten new monsters appear?** ___0___

**In two or three sentences: explain how a class can use a blueprint that arrived from a file
format it has never heard of.**

By using our adapter pattern we were able to use the comparable parts of the beastiary.csv to match to the monster def 
we already had. We had a match for most parts of a monster and had fallbacks for when we were missing data for whatever
reason. Using composition we can pass a legacybeastiary as a monstersource to have the factory create monsters from the 
beastiary.csv as well.

---

## 6. AFTER — the honest accounting

### The Adapter's honest half

**`bestiary.csv` has no xp column. Which of the three options did you take?** ___We derived it___

**Where in your code does a reader find out that the number is derived?** ___translationNotes()___

**One sentence: what would have gone wrong if you had silently defaulted it to 5?**

Nothing inherently goes wrong if we default to 5, makes code less manageable if someone wants to change how xp scales, etc.

**`SAPPER` has no counterpart. What did you do, and what happens when the eleventh unknown AI
name arrives next semester?**

We wrote a default branch to map it and any future unknowns to aggressive.

### The Facade's honest half

**Complete this honestly:**

> `GameSession` now knows about eight collaborators and the order they go together in, which
> means the coupling _________________________was moved to another object to 'own' and manage(GameSession) aka coding to
an interface._____________________

**Name one thing that got HARDER because `Main` no longer knows those eight classes.** *(There
is a real answer. Think about what a test needs.)*

Our unit tests broke when we moved the facade logic out of main and since we changed GameWorld to two parameters we had 
to introduce a MonsterFactory variable for each test that called a GameWorld, and then add in the new parameter each time
the tests were calling it.

---

## 7. Retro line

**One sentence on something each pattern gave you, and one on something each one took away.**
Four sentences, and the two costs are not the same cost.

| Pattern | Gave | Took |
|---|---|---|
| Adapter |Use of older interface without modifying something that already works |Cannot translate everything 1:1 and we had to make up xp for the beastiary monsters |
| Facade |one place for the GameSession to hold its eight collaborators |start method is approaching "god object" status as it builds everything |

**And the one-sentence distinction, in your own words, having now written both:** Adapter and
Facade are both "a class in front of other classes." What made them different in practice?

The Adapter Pattern is a class that sits between two classes that normally cannot interface with each other directly,
while the Facade Pattern puts the facade in front of all the logic classes it intends to manage in a specific way.
