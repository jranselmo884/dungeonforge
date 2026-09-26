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
| How many **new** files? | |
| Did `Combat.java` change? | |
| Did `EventBus.java` change? | |
| Did any existing listener change? | |
| Which files changed at all? | |

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


## D3 — The swap, demonstrated · 5 pts

**Run the game and find a line in the combat log like:**

```
Forge Golem changes tactics: aggressive -> skittish.
```

**Paste yours:**

```

```

**Now answer:** at the moment that line was printed, what changed about the `Forge Golem`
object? Be precise. Its class? Its fields? Its identity? What *specifically* is different
about it one instruction later?


**Then add a fifth strategy** of your own invention. How many existing files did you have to
modify, and which?


## D4 — One honest question · 3 pts

**A prompt, because this one is worth surfacing now:** `CombatStrategy` and Week 8's `State`
pattern have almost identical UML — an interface, several implementations, an object that
holds one and delegates to it.

**Without looking ahead, guess:** what could possibly distinguish them? You are not expected
to be right. You're expected to have a hypothesis on record before Week 8 tells you.


**And anything else that's still unclear:**

