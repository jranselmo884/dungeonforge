# The Twins Clinic — Lab 8, Part D

> Week 5 you were asked to guess what could possibly distinguish State from Strategy, given
> that their UML is nearly identical. **This is where you find out.**

## D1 — Grade your own Week 5 prediction · 8 pts

Open `docs/strategy-observer-clinic.md` from Week 5 and find your D4 answer.

**Paste it here, unedited:**

```
When we first started this week's chapter I thought we were going to be essentially working with "flags" but now having 
gone through and implemented the Observer method its clear to me that this is something similar but actually meant as a 
subscriber to subject relationship. The way both sides have different methods of sending these changes across each other
is asymmetrical, with pushes and pulls differs from what I imagine the state pattern might be. I imagine the state
pattern to follow similar principles as the Observer but with identical pushes and pulls as the point would be to modify 
a objects state or flags to have the object treated in a certain way.
```

**Now, having built both, mark your own work:**

| | Your Week 5 guess | What you would say now |
|---|---|---|
| Who decides which one is active? |the client - by pushing and pulling |the object - next() returns its own successor |
| Do the alternatives know about each other? | No, the strategy classes do not reference each other| Yes. HuntingState.next() names Enraged, Fleeing, and Dead.|
| What question does it answer about a monster? | The way a monster acts and fights| WHEN it fights, rages, runs, or stops exisiting|
| Can it change while the object is alive? | Sometimes, when a monster takes enough damage or etc.| Constantly, and it swaps itself|

**How close were you?** Circle one and justify it in a sentence:
*dead on · (half right) · reasonable but wrong · nowhere near*

I would consider my answer from Week 5 was half right because the state pattern has an object treated differently based
its current state, however I failed to guess that the object itself would be what decides the state or the next state. 

> **You are not graded on having been right.** You are graded on the honesty of the comparison
> and on what you can now articulate that you could not in Week 5.

## D2 — What does a new state cost? · 7 pts

The Week 7 code stored a monster's state as a **string** with conditionals in three places.
Your Week 8 code stores it as an **object**. Measure the difference.

### First, on the STARTER code (before your refactor)

Add a `"stunned"` state that lasts one turn and prevents acting.

| Question | Your answer |
|---|---|
| Which methods in `Combat` did you have to edit? | 4|
| How many conditionals did you add or change? | 3|
| What stops someone typing `setStateName("stnuned")` and getting no error? |having a hardcoded guard rail to catch any unknown inputs |

> Revert this experiment before continuing. It exists to be measured, not kept.

### Then, on YOUR refactored code

Add the same `StunnedState`.

| Question | Your answer |
|---|---|
| New files | 1|
| Existing files modified |2 |
| Did `Combat.java` change? |No |
| Did `Monster.java` change? | No|

**Paste `git diff --stat`:**

```

```

**One paragraph: what did the compiler start doing for you that it could not do when the
state was a string?**

The compiler now checks the state for available verbs/commands verus just checking against a library of commands/aliases
and can accept certain commands in certain states. It also ensures the order of our states instead of just transitioning 
with guard rails, the monster has a lifecycle that must be followed. In addition, we are always in a state because 
next() returns a MonsterState regardless. 

## D3 — Rejected versus impossible · 7 pts

In Week 7, typing `north` during a fight got you told off. In Week 8 it is not on the menu.

**Paste your Week 7 behaviour (from `git show` on an old tag, or just describe it):**

```

```

**Paste your Week 8 behaviour, including the `help` output in both modes:**

```

```

### The questions

**1.** Count the mode guards you deleted from command classes: _3_
Which files were they in?

Command.java

**2.** `help` now prints a different list in each mode, and you did not write any code to make
that happen. **Why not?**

Because each state now encapsulates the verbs associated with it. The CommandParser just delegates to the context.

**3.** Here is the one that matters. Suppose a teammate adds an `equip` command next week and
forgets to think about combat. **In the Week 7 design, what goes wrong? In the Week 8 design,
what goes wrong?** Be specific about *when* the mistake would be noticed.

In the Week 7 design the issue with adding an equip command without considering combat results in being able to use equip 
while fighting a monster or in other "states" that we shouldn't be able to.

In the Week 8 design, since each state is aware of which commands are avaiable to it, when someone replicates the equip 
command and tries to use in it a state that has not defined it then it will not work. Having equip work in a specific 
state requires it to be intentionally done so and more than likely will be allowed behavior.

## D4 — One honest question · 3 pts

`GameContext.syncState()` deliberately does **not** pull the player out of the pack screen
when a monster arrives.

**Is that the right call?** Argue either side in a short paragraph. There is no answer key —
real games disagree about this, and knowing that a design decision is *contested* is worth
more than picking the popular side.

I believe this is the right call as it is because if a monster does show up then having the pack still open the player 
can continue to use the pack and plan accordingly. Because once the player closes the pack they will not be able to open
it until there are no monsters on the floor. Also helps with immersion, would be be good game design to just pull player
out of pack. 

**And anything still unclear:**

In the syncState() method if the player is dead or if the game is not running then we enter a GameOverState. If we
wanted the player to have the opportunity to check quests or status before they close the game, is adding a 
CloseGameState a viable solution or are we looking for something else?

