# The Coupling Clinic — Lab 5, Part D

> Week 3 was about refusing a pattern. Week 4 was about telling three similar patterns apart.
> **Week 5 is about coupling** — and both of this week's patterns exist to reduce it, in two
> completely different directions.

## D1 — The arithmetic, BEFORE you write any code · 8 pts

**Do this section first.** It takes ten minutes and it decides whether the rest of the week
makes sense to you.

DungeonForge has **15 monster species**. We want **4 combat behaviours**: aggressive, ranged,
skittish, healer.

### The subclassing approach

Suppose behaviour is expressed by subclassing `Monster` — `AggressiveSkeleton`,
`SkittishSkeleton`, `RangedImp`, and so on.

| Question | Your answer |
|---|---|
| How many classes for 15 species × 4 behaviours? |60 |
| Add a 5th behaviour (say, "berserk"). How many NEW classes? | 15|
| Add a 16th species. How many NEW classes? | 64|
| A skeleton is losing badly and should start running. **Can a `SkittishSkeleton` object become an `AggressiveSkeleton` object at runtime?** Answer yes or no and say why. | No, because the object is a concrete instance that already has a defined state that cannot be changed.|

### The composition approach

| Question | Your answer |
|---|---|
| How many classes for 15 species + 4 strategies? | 19|
| Add a 5th behaviour. How many NEW classes? | 1|
| Add a 16th species. How many NEW **Java** files? | 1|
| Can a monster change behaviour at runtime? How? | Yes, by changing the state of an object's behavior using a setBehavior method like the textbooks example of setQuackBehavior and setFlyBehavior. A model duck can be changed from not flying to using a rocket flying method by using setFlyBehavior and changing the FlyBehavior to FlyRocketPowered in runtime. |

**Now write two or three sentences.** Head First calls this the SimUDuck problem. In your own
words: **what is the actual defect in the subclassing design?** Not "it's more classes" —
there's a deeper problem that the last row of each table points at.

The "disease" is the issue of static behavior at runtime, using subclasses to setBehavior states is not sustainable. If behaviors need to changed by using a config.json file, input, or even a database tying behaviors to classes limits the potential for adding, removing, and changing behaviors. In dungeonforge it would leave us without the ability to change a monster's behavior as a result of it taking sufficient damage. A monster cannot change itself into a fleeing monster at runtime if its "standard" behavior is a class.

## D2 — The coupling experiment · 9 pts

The Observer pattern's claim is that **a publisher need never know its subscribers**. Measure
it.

**Commit your work first**, so `git diff --stat` means something.

**Add a fourth listener.** Something simple — a `StatisticsCollector` that counts events by
type, or a `DangerMeter` that notices when your HP drops below 25%. Subscribe it in `Main`.

| Question | Your answer |
|---|---|
| How many **new** files? | 1|
| Did `Combat.java` change? |No |
| Did `EventBus.java` change? |No |
| Did any existing listener change? |No |
| Which files changed at all? | Added 1 and changed Main.java|

**Paste `git diff --stat`:**

```

```

### Then the question that matters

`Combat` could simply have called `questTracker.onMonsterKilled(monster)` directly. That's one
line, it's obvious, and it needs no `EventBus`, no `GameEvent`, and no `GameEventListener` —
**three fewer classes.**

**Write a paragraph.** What does the direct call cost you that the bus does not? Give a
*concrete* scenario — a change somebody might ask for — where the direct-call version forces
you to edit `Combat` and the bus version does not.

> A good answer names a specific future feature. A great answer names one from this course's
> remaining schedule.
> 
 By using a direct call without implementing the Observer pattern it becomes tightly coupled to the concrete classes
that would require a refactor of Combat everytime we need a new "listener". It also tightly couples together future 
features that may greatly benefit from having loose coupling such as implementing a GUI. Having the GUI tightly coupled
to the concrete objects would greatly increase the chance of something breaking everything code needs to be refactored.
While we do add three classes using the bus and event classes we gain something thats invaluable to us as OOP
programmers, loosely coupled methods and objects that do not break that as we add more features.


## D3 — The swap, demonstrated · 5 pts

**Run the game and find a line in the combat log like:**

```
Forge Golem changes tactics: aggressive -> skittish.
```

**Paste yours:**

```
-- strategy highlights --
  Skeleton changes tactics. aggressive -> skittish
  Skeleton flees into the dark.
  Bone Priest mends Wight
  Wight changes tactics. aggressive -> skittish
  Wight flees into the dark.
  Imp changes tactics. ranged -> skittish
  Imp flees into the dark.
  Imp changes tactics. ranged -> skittish
  Imp flees into the dark.
  Ember Sprite changes tactics. ranged -> skittish
```

**Now answer:** at the moment that line was printed, what changed about the `Forge Golem`
object? Be precise. Its class? Its fields? Its identity? What *specifically* is different
about it one instruction later?

When the Skeleton changed it tactics what changed about it was one of its fields, the strategy interface.

**Then add a fifth strategy** of your own invention. How many existing files did you have to
modify, and which?

Just one class would change and we would add a file for the new strategy.

## D4 — One honest question · 3 pts

**A prompt, because this one is worth surfacing now:** `CombatStrategy` and Week 8's `State`
pattern have almost identical UML — an interface, several implementations, an object that
holds one and delegates to it.

**Without looking ahead, guess:** what could possibly distinguish them? You are not expected
to be right. You're expected to have a hypothesis on record before Week 8 tells you.

When we first started this week's chapter I thought we were going to be essentially working with "flags" but now having 
gone through and implemented the Observer method its clear to me that this is something similar but actually meant as a 
subscriber to subject relationship. The way both sides have different methods of sending these changes across each other
is asymmetrical, with pushes and pulls differs from what I imagine the state pattern might be. I imagine the state
pattern to follow similar principles as the Observer but with identical pushes and pulls as the point would be to modify 
a objects state or flags to have the object treated in a certain way.

**And anything else that's still unclear:**

The only thing that is a bit unclear to me is the specifics of what the pull that subscribers can do, because said subscribers
can choose which information to include in the "subscriber started update" and how that affects any pushes the subject 
does.
