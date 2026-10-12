# The Undo Clinic — Lab 7, Part D

> Week 3: refuse a pattern. Week 4: tell three lookalikes apart. Week 5: two directions of
> decoupling. **This week: discover that a pattern gives you a hook, not an answer.**

## D1 — Measure the if/else · 6 pts

**Before you delete anything**, on `main`:

| Question | Your answer |
|---|---|
| Lines in `Main.gameLoop()` | 126 lines|
| Number of `else if` branches | 13|
| To add "equip", which file(s) and how many places? | 4 files and 1 documentation|
| To add alias "e" for "equip", how many *additional* places? |1 assigning the verb variable to the alias |
| Which line of `gameLoop()` would you attach undo to? | There is no good place to add undo to Gameloop as the undo method requires a complete state copy.|

That last row has no answer, and saying so clearly is worth full marks.

**Now finish the refactor and measure again:**

| Question | Your answer |
|---|---|
| Lines in the new `gameLoop()` | 16|
| `else if` branches remaining |0 |
| To add "equip": files created, files modified | |
| To add alias "e": how many places? |1 |

## D2 — The naive undo, and why it fails · 12 pts 🔑

**Do these steps in order. Do not skip step 1 — the failure is the lesson.**

### Step 1 — build the obvious version

Give `AttackCommand` a naive undo: a single field remembering the target's hit points before
the blow, restored on `undo()`. No snapshot, no other state.

```java
private int targetHpBefore;      // the "obvious" approach
```

### Step 2 — break it, on purpose

Write down `HP`, `XP` and `Gold` from `status`. Attack something that survives and hits back.
Check `status` again. Now `undo`, and check `status` a third time.

| | HP | XP | Gold | Monster HP |
|---|---|---|---|---|
| Before attack | 45| 12|30 | 10|
| After attack |45 |18 | 30| 0|
| After naive undo |45 |18 |30 | 10|

**Which values did NOT come back?**
Any achievements the were attained were not undone and monsters who died did not come back to life.
**Why not? Answer in terms of what happened during that one turn**, not in terms of code:

The achievement for first slayed monster was archived but when I used the naive version of undo it did not reserve that
achievement nor the xp gained. On my first attempt the monsters would not come back to life until I stored a copy of the
monster every turn to always have the backup to restore.

### Step 3 — list everything one turn can change

Before you write the fix, enumerate it. Your undo has to reverse all of it.

- [ ] the target monster's hit points
- [ ] player's hit points
- [ ] any strategy changes must be reversed, for example any damaged monsters who become skiddish must return to their
previous state.
- [ ] xp gained 
- [ ] achievements
- [ ] monsters' lives
- [ ] loot picked up
- [ ] items used
- [ ] floor/room change
- [ ] monsters who have fled

> Two of these are easy to miss. One involves Week 5's strategy swap; the other involves a
> monster that isn't in the room any more.

### Step 4 — fix it, then answer this

Build `TurnSnapshot` and make `undo()` restore it. Re-run the table from step 2 and confirm
everything returns.

**Now the question worth the marks.** The Command pattern promised "supports undoable
operations." **Did Command actually give you undo?** Answer carefully — there's a real
distinction here, and it's the most useful thing in this week.

Command itself did not give us the specific undo we needed, we had to refer to the momento pattern to copy and be able 
to restore a complete state of a turn. The command pattern does give us a basic return like the light switch example, 
but when dealing with a game like ours that has different object whose states can influence each other than it does not 
suffice. 

## D3 — The capabilities you didn't ask for · 5 pts

The pattern's definition lists four things. You wanted one.

**Record a session.** Play for a dozen commands, run `history`, and paste the log:

```
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.6.0   seed 20260829

  Type 'help' for what you can do. You are carrying a hourglass -- 'use Chornomaster's Hourglass' rewinds a turn.
  [L1R0Level 1: Crypt
    "Burial niches line the walls. Most are empty. MOST."
    Exits: north

n
[HP 80/80] > 
  You go north.
  Ghoul Ghoullooses a shot for 2

hit
[HP 78/80] > 
  You hit Ghoul for 10
  Ghoul Ghoullooses a shot for 2

hit
[HP 76/80] > 
  You hit Ghoul for 10
  ACHIEVEMENT: First Blood -- Defeat your first monster
  Ghoul dies.12

n
[HP 76/80] > 
  You go north.

n
[HP 80/80] > 
  You go north.

n
[HP 80/80] > 
  You go north.

n
[HP 80/80] > 
  You go north.
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

hit
[HP 73/80] > 
  You hit Bone Priest for 10
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

hit
[HP 66/80] > 
  You hit Bone Priest for 10
  Bone Priest dies.12
  Wight Wightlunges at you for 4

hit
[HP 62/80] > 
  You hit Wight for 10
  Wight Wightlunges at you for 4

hit
[HP 58/80] > 
  You hit Wight for 10
  Wight dies.10

look
[HP 58/80] > 
  [L1R5Level 1: Crypt
    "Burial niches line the walls. Most are empty. MOST."
    Exits: south, north

n
[HP 58/80] > 
  You go north.
  Skeleton Skeletonlunges at you for 4
  Bone Priest Bone Priest strikes with a censer for 3

hit
[HP 59/80] > 
  You hit Skeleton for 10
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Skeleton changes tactics: aggressive -> skittish
  ACHIEVEMENT: Terrifying -- Make something run away from you
  Skeleton flees into the dark.
  Bone Priest Bone Priest strikes with a censer for 3


```

**Save those lines to a file and replay them:**

```bash
mvn -q exec:java -Dexec.args="--script=mysession.txt"
```

**Did you get the same game? Why does that depend on something you built in Week 3?**

Yes I got the same game because of the singleton pattern we implemented, since the seed it reads from config.json stays
the same, any game played with the same seed and commands will play out the exact same.

**Finally:** name a **fifth** capability that falls out of actions-as-objects which the
pattern's definition doesn't mention. *(Hint: think about the optional GUI track, or about
anything that isn't a keyboard.)*

A fifth capability that it has it open for extension but closed for
modification to an extent. The example of adding a GUI later on is a much easier task with everything programmed to an
interface, GUI elements can be tied to these interfaces.

## D4 — One honest question · 2 pts

`AbstractCommand.execute()` is `final` and defines a fixed order: snapshot, act, monster
turns. Subclasses fill in one step.

**You have seen this shape before this week, in a pattern you already know. Which one, and
where?** *(You built something structurally identical in Week 4.)*

This is similar shape to our factory method in the RoomPopulator's populate method, they both play similar roles being 
interfaces that the subclasses will fill out and provide the specialized methods needed.

**And anything still unclear:**

Lambda expressions and the way they are able to initizatie objects from classes like AbstractCommand is still iffy to 
me, I understand the idea but the implementation is shakey. I do understand they are limited to one abstract method
classes.