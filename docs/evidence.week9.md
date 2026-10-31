# Week 9 Evidence

Fill this in as you go. Parts 0–2 and Part 5 are graded on the *measurement*, not on the code.

---

## 0. The Sprint 6 carry-over (US-6.7)

Before any Decorator work. `docs/sprint-07-plan.md` has the story.

```bash
grep -n 'isRunning\|!running' src/main/java/dungeonforge/Main.java \
                              src/main/java/dungeonforge/commands/GameContext.java
```

**Paste the output. How many places spell out the game-over rule?** ______

**Now open `GameOverState.java`. It lists four verbs. Try to reach one in play. What happens,
and why?**


**One sentence: `running` and `GameOverState` both mean "the game is over". Do they? Say what
each one actually means.**


---

## 1. BEFORE — count the problem

```bash
ls src/main/java/dungeonforge/items/ | grep -E 'Weapon|Enchant'
grep -rln "damage + 4" src/main/java/dungeonforge/items/
grep -rho "instanceof" src/main/java/dungeonforge/items/*.java | wc -l
grep -c "return null" src/main/java/dungeonforge/items/EnchantmentForge.java
wc -l src/main/java/dungeonforge/items/EnchantmentForge.java
```

| | Count |
|---|---|
| Weapon/enchantment classes |8 |
| Files containing the fire damage bonus | 4|
| `instanceof` tests in `items/` | 34|
| `return null` paths in `EnchantmentForge` |13 |
| Lines in `EnchantmentForge` |108 |

**Open `FlamingFrostVampiricWeapon.java`. Which of its methods contain arithmetic that also
appears, word for word, in another file?**

getDamage() and getValue()

**You want a fourth enchantment, `keen`. Before you look anything up: how many new classes
would this package need?** __15__

*(Write it down before reading on.)* For N enchantments the package needs 2^N − 1 classes.
N = 3 is 7. N = 4 is 15. N = 5 is 31.

---

## 2. BEFORE — reproduce all four defects

You start carrying a **Notched Shortsword**, so this works on turn one. Run
`mvn -q exec:java` and type exactly this:

```
equip shortsword
status
enchant shortsword with flaming
status
inventory
equip shortsword
status
enchant shortsword with flaming
enchant shortsword with keen
quit
```

**(a) Paste the two `status` lines from before and after the first `enchant`:**

```
  Delver  HP 80/80  ATK 15  DEF 3  Gold 0  XP 0  Carry 60.0kg  wielding Notched Shortsword [dmg 5] (2.5kg, 25g)

  Delver  HP 80/80  ATK 15  DEF 3  Gold 0  XP 0  Carry 60.0kg  wielding Notched Shortsword [dmg 5] (2.5kg, 25g)

```

**Did your damage change? Explain in one sentence why not.**

No my damage did not change because the "shortsword" object that was equiped was not updated when we enchanted our
shortsword.

**(b) Paste the refusal from the second `enchant ... with flaming`:**

```
  That weapon cannot take another flaming enchantment.

```

**Is there anything wrong with what you asked for? Yes or no — then say what the refusal was
really reporting.**

If we as game developers intend for players to be able to stack enchantments then the refusal to add another flaming 
enchantment is a bug. The refusal was essentially reporting that we are already a "Flaming" weapon and that we were 
attempting to change the weapon object into a subclass weapon type that it already was, Flaming. 

**(c) Paste the response to `enchant ... with keen`:**

```
  No such enchantment: keen

```

**(d) Open `EnchantmentForge.baseDamage()`. In one sentence: why does this method exist at all?
What would have to be true for it to be unnecessary?**

This method exists to allow us to return an item back to its base type and correctly reducing the damage it does,
basically undoing all the math we did to the weapon upon enchanting. If we had applied the principles of the decorator 
pattern we could "unwrap" the object from its decorations and it would have revert its properties back to a default or 
base object.

**(e) Classify all four.** Two of these are the *design*; one is an ordinary *bug* that would
exist in any design. Which is which, and how do you know?

| Defect | Design, or ordinary bug? | How you can tell |
|---|---|---|
| (a) stale hand | design pattern|the enchantment factory is not equipped to handle changes to already equipped weapons |
| (b) no double enchantment |design pattern | once an object becomes a "flaming" object it cannot stack the same flaming enchantment because it is already of the "flaming" type|
| (c) no fourth enchantment | design pattern| with a separate class for each combination of classes we have only implentented 3 enchantments deep so there are no classes that can have an object be 4 different enchantments.|
| (d) base-value recovery | bug| this is a bug because we have not accounted for the base attack value to return to its base after unenchanting a weapon|

---

## 3. AFTER — US-7.3, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth enchantment:**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ git diff --stat HEAD~1
 src/main/java/dungeonforge/commands/EnchantCommand.java   |  26 +++++----
 src/main/java/dungeonforge/commands/TurnSnapshot.java     |   4 ++
 src/main/java/dungeonforge/items/EnchantmentForge.java    | 116 ----------------------------------------
 src/main/java/dungeonforge/items/EnchantmentRegistry.java |  32 +++++++++++
 src/main/java/dungeonforge/items/KeenWeapon.java          |  25 +++++++++
 src/main/java/dungeonforge/items/WeaponDecorator.java     |   2 +-
 src/test/java/dungeonforge/DecoratorTest.java             | 234 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 7 files changed, 308 insertions(+), 131 deletions(-)
```

**New files:** __1__  **Modified files:** __1__  **Lines added to modified files:** __1__

**Now re-run the Part 1 counts on the finished package:**

| | Before | After |
|---|---|---|
| Weapon/enchantment classes |8 | 7|
| Files containing the fire bonus |4 | 1|
| `instanceof` in `items/` | 34| 0|
| `return null` paths in the selection class | 13| 0|
| Lines in the selection class |108 | 33|

---

## 4. AFTER — US-7.4, order

**Paste your two order tests, and their four numbers:**

```java
    @Test
    void orderChangesTheResultForMultiplyingDecorators() {
        int keenThenFire = new FlamingWeapon(new KeenWeapon(sword())).getDamage();
        int fireThenKeen = new KeenWeapon(new FlamingWeapon(sword())).getDamage();

        assertEquals(16, keenThenFire);
        assertEquals(18, fireThenKeen);
        assertNotEquals(keenThenFire, fireThenKeen,
                "If these were equal, order would not matter and this test would be pointless.");
    }

    @Test
    void orderChangesHowMuchAVampiricWeaponDrinks() {
        assertEquals(3, new VampiricWeapon(new FlamingWeapon(sword())).lifesteal());
        assertEquals(2, new FlamingWeapon(new VampiricWeapon(sword())).lifesteal());
    }
```

| Stack | Result |
|---|---|
| `Flaming(Keen(sword))` | dmg __16__ |
| `Keen(Flaming(sword))` | dmg __18__ |
| `Vampiric(Flaming(sword))` | drain __3__ |
| `Flaming(Vampiric(sword))` | drain __2__ |

**One paragraph. Why is this correct behaviour rather than a bug?** *(There is one idea to
state, and everything else follows from it.)*

These different results from the different orders come from rearranging the math and following the order of operations
(PEMDAS). In the Keen Flaming Sword we have the 1.5 multiplier from the Keen decorator which takes the base 8 dmg to 12 dmg 
followed the Flaming decorator which adds 4 to make a final total of 16 dmg for the final total. When we swap the 
positons of the Keen and Flaming decorators we have 8 which the Flaming adds 4 to for 12 and then Keen multiplies it by
1.5 for a total of 18 dmg for the Flaming Keen Sword. While decorators that only add or subtract can be swapped for the 
same result, if you mix in any other kind of math like multiplication, squaring, or division, you end up with different
results. As a player this would be expected behavior just from having taken math classes in school.

---

## 5. The payoff — what did NOT change

```bash
git diff --stat main -- src/main/java/dungeonforge/core/
```

**Which files in `core/` changed during this entire sprint?** __0__ 
In src/main/java/dungeonforge/core we had not files that changed, and 2 that changed from src/main/java/dungeonforge 
since the last pull request to main branch.

**`Combat.playerStrikes()` computes the damage the player deals and the hit points they drink
back. You added four enchantments this week, two of which change damage and one of which
changes healing. How many lines of `Combat.java` did you edit?** __0__

**Explain that number** in two or three sentences, using the phrase *programs to the
interface* and naming which interface.

Combat did not change at all because we changed Weapon to an interface, changed Player.java to include getAttackPower 
and lifesteal(), which essentially gave Player a "better answer to the question" Combat was already asking. By 
programming to the interfaces that are now Weapon and WeaponDecorator we can change how Combat receives the dmg info it 
already gets without changing Combat at all. Combat has no idea about the new classes/decorators we created yet is 
absolutely impacted by them. Open/Closed principle.


---

## 6. The honest accounting

**Of the four defects in Part 2, which one did Decorator fix, and which one did you fix?**

The defect that we fixed was the one in EnchantCommand where the result was a new object instead of the same one that
was in our inventory, enchanting a weapon would not update the new weapon the player was holding. Now with our fix the 
player can enchant that weapon they are holding and not have to reequip. 


The defect that the Decorator fixed was the inability to stack enchantments of the same type such as having a Flaming 
Flaming Sword, and even had a enchant limit depth of 3. By implementing the Decorator pattern we can allow the player to
stack as many enchantments as they would like.

**Quote the lines that fixed the one Decorator did not.**

```java
        player().removeItem(w);
        player().addItem(after);
        if (wasEquipped) { player().equip(after); }
        bus().message("The " + w.getName() + " is now " + after.describe());
```

**Would those lines have worked before the refactor?** __Yes__

---

## 7. Retro line

**One sentence on something Decorator gave you, and one on something it did not.**

The decorator pattern gave us unlimited stackable enchantments and allowed us to stack multiplies of the same
enchantment type and what the decorator pattern did not give us was a way for the game to not "reequip" the same weapon
that is already being held. 