# The "Which Factory?" Clinic — Lab 4, Part D

> Week 3's hard part was refusing a pattern. **This week's hard part is telling three very
> similar patterns apart.** Students who leave Week 4 unable to distinguish them will misuse
> all three for the rest of the semester — and Exam 1 will ask.

## D1 — The experiment: what does a fourth theme cost? · 8 pts

The Abstract Factory's whole claim is *"adding a new family is cheap and touches nothing
else."* Claims like that should be measured, not believed.

**Add a fourth theme.** Anything you like — Fungal, Drowned, Clockwork. It needs a kit class,
a couple of monster blueprints in `monsters.json`, and loot.

Before you start, **commit your current work** so `git diff --stat` is meaningful.

| Question | Your answer |
|---|---|
| How many **new** files did you create? |1 |
| How many **existing** files did you modify? |3 |
| Which existing files? |ThemeRegistry.java, config.json, monsters.json |
| Did `GameWorld.java` change? | no|
| Did any `RoomPopulator` subclass change? |no |
| Did `Monster`, `Room`, or `DungeonLevel` change? |no |

**Paste the output of `git diff --stat`:**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat
 docs/factory-clinic.md                                     | 12 ++++++------
 src/main/java/dungeonforge/factory/ThemeRegistry.java      |  1 +
 src/main/java/dungeonforge/factory/WinterfellThemeKit.java | 28 +++++++++++++---------------
 src/main/resources/data/config.json                        |  2 +-
 src/main/resources/data/monsters.json                      |  7 ++++++-
 5 files changed, 27 insertions(+), 23 deletions(-)
```

**In two or three sentences: what does that number tell you about the Open/Closed
Principle — "open for extension, closed for modification"? Was it satisfied, and how do you
know from evidence rather than from a definition?**
The small number of modified and created files tells me that, aside from the hardcoded registry, the code is very open to extension by adding more ThemeKits and closed for modification because of the factory methods and the abstract factory ThemeKit. The evidence is how the winterfell theme was integrated by modifying just the registry and the json files. The WinterfellThemeKit class was created as a copy of ForgeThemeKit and with the name adjustments it merged in nicely.

> Set `dungeonDepth` to 4 in `config.json` and run it, so you can see your fourth theme.
> Then set it back to 3 before you open the PR.

## D2 — Classification · 12 pts

For each scenario: which of the three applies? Answer **Simple Factory**, **Factory Method**,
**Abstract Factory**, or **none of them** — and give a one-sentence reason.

| # | Scenario | Which? | Why |
|---|---|---|---|
| 1 | One place in the code turns a monster id string into a `Monster`, so `new Monster` appears once |Simple Factory |It takes object creation out of the Monster class and delegates it to MonsterFactory so that we program to an interface and not an implementation.|
| 2 | A boss room, a treasure room and an ordinary room each fill themselves differently, but always in the same order: prose, then monsters, then a chest | Factory Method|This is a factory method because it takes the class room and gives it subclasses that take care of object creation. These subclasses determine which room it is as well as the contents, but since they are all rooms they follow the room "procedure". |
| 3 | An ice level must contain ice monsters AND ice loot AND ice prose, never a mix |Abstract Factory |This is an abstract factory because we have a family of familair objects which are grouped togther as "kits" such as the "ice kit". Other kits will also contain monsters, loot, and prose but will not mix their "kit" with others like the ice kit. |
| 4 | Week 9: a weapon can be made flaming, then vampiric, then blessed, in any combination |None |The "kits" would not be separate and mixing them violates what is considered an abstract factory.|
| 5 | Week 12: save files must be written as JSON now and possibly as XML later, with matched reader and writer | Abstract Factory|The matched reader and writer are a family of familiar objects. |
| 6 | A method returns a `Player` object, built from the name typed at startup |None |Object is created after runtime and is not created with a factory of any kind. |

> Scenarios 4 and 6 are traps. One is a different pattern entirely; the other is not a pattern
> at all. Say so if you think so — "none of them" is a correct answer to at least one row.

## D3 — The distinction, in your own words · 5 pts

**Simple Factory is not one of the Gang of Four patterns.** Your textbook says so explicitly
before it teaches Factory Method.

**In three or four sentences: what can Factory Method do that Simple Factory cannot?** Do not
define either one. Describe a change someone might ask you to make, and explain why it would
be easy with one and awkward with the other.

A factory method gives you options when it comes to not only object creation but specific methods for different subclasses. In the pizza factory example, the factory method gave us a way to "franchise" the pizza factory to make NY, Chicago, and even California pizzas with potential for expansion. The Chicago pizza was even able to override the cut method from the parent class to be cut in square shapes. If we were asked to add a LA pizza its a matter of adding a new class in a factory method, but in a simple factory expansion would require a refactor of the whole simple factory itself.

## D4 — One honest question

What is still blurry about these three patterns? A specific confusion is worth more to me
than a confident summary.

I think that the need for a simple factory over a factory method is still blurry for me, I can understand how an abstract factory would be needed or not needed depending on whether its a family of familiar objects or the need for options, defaults and overrides. I think right now I just see a simple factory as a stepping point for a factory method but I guess I see how "simpler" code can be better if the need for expansion would never arise.