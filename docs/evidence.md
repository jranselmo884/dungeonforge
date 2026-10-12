# Week 7 Evidence

## 1. BEFORE

```bash
mvn -q exec:java
```

Play a few turns, then answer:

**Paste five consecutive `else if` branches from `gameLoop()`:**

```java
if (verb.isEmpty()) {
} else if (verb.equals("north") || verb.equals("south") || verb.equals("down")) {
} else if (verb.equals("attack")) {
} else if (verb.equals("look")) {
} else if (verb.equals("take")) {if (i.getName().toLowerCase().contains(rest.toLowerCase())) { found = i; break; }
```

**You want to add `equip`. List every edit you would make:**
1. Main.java import dungeonforge.items.Weapon;
2. Main.java add another else if statement for the new verb "equip" in gameloop
3. Main.java set tookATurn = true in the new branch for #2
4. Main.java lines 219-220, add "equip" to the println statements
5. Player.java import dungeonforge.items.Weapon;
6. Player.java private Weapon equippedWeapon; + equip(Weapon) + getEqippedWeapon()
7. Player.java make the weapon count toward damage
8. Player.java describe() lines 59 reads attackPower, not the getter
9. Combat.java int damage = player.getAttackPower();
10. src/test/java/dungeonforge/EquipTest.java
11. README-WEEK7.md and any verb list in docs

**You want `e` to mean `equip`. List every *additional* edit:**

Starter reference

1. Functional edit || verb.equals("e")
2. Documentation 2 lines edited by hand, or it silently drifts
3. Can it come from config? No
4. Can you list every alias? No
5. Collision safety silent
6. You would have to modify the conditional block to test for "e" OR "equip" in a case sensitive way.

**You want undo. Where in `gameLoop()` would the code go?** *(There is no good answer. Say
why in one sentence.)*

There is no good place to put the undo in gameLoop because the undo method will have to have copy a complete state of the 
delve at the moment that we want to undo and will all of the else if statements if gets tricky to copy that complete 
state.

## 2. AFTER — US-5.1

**Paste your new `gameLoop()` in full. It should fit in a dozen lines:**

```java
    private static void gameLoop(CommandParser parser, GameContext ctx) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        while (ctx.isRunning() && ctx.getPlayer().isAlive()) {
            System.out.print("\n[HP " + ctx.getPlayer().getHp() + "/" + ctx.getPlayer().getMaxHp() + "] > ");
            System.out.flush();

            String line;
            try { line = in.readLine(); } catch (Exception e) { break; }
            if (line == null) break;
            System.out.println();

            Command command = parser.parse(line, ctx);
            command.execute();
        }
    }
```

**Paste the registry lines that add `drop` and its alias:**

```java
register("drop", DropCommand::new);

```

## 3. AFTER — US-5.2

**The naive-undo failure table from Part D2 step 2:**

| | HP | XP | Gold | Monster HP |
|---|---|---|---|---|
| Before attack |[HP 80/80] | | | Ghoul (17/17 HP|
| After attack |[HP 78/80] | | |Ghoul (7/17 HP |
| After naive undo |[HP 78/80] | | | Ghoul (17/17 HP|

**The same table after `TurnSnapshot`:**

| | HP | XP | Gold | Monster HP |
|---|---|---|---|---|
| Before attack |45 |12 |30 |10 |
| After attack |45 |18 |30 |0 |
| After snapshot undo |45 |12 |30 |10 |

## 4. AFTER — US-5.3 and US-5.4

You have replay backwards because if you undid the children forward, you would end up only going back in history 1
command. First in, last out.

**Paste a `loot` that picks up three things, then an `undo` that puts them all back:**

```
look
[HP 76/80] > 
  [L1R1Level 1: Crypt
    "Damp stone. Something drips in the dark, patiently."
     Iron-bound Chest: 
    - Bone Shortsword [dmg 6] (2.0kg, 50g)
    - Bone Shortsword [dmg 6] (2.0kg, 50g)
    - Grave Shroud [def 3] (3.0kg, 43g)
    Exits: south, north

loot
[HP 76/80] > 
  You take Bone Shortsword.
  You take Bone Shortsword.
  You take Grave Shroud.
```

**Paste the hourglass rewinding a turn, with `inventory` before and after:**

```
inventory
[HP 76/80] > 
  You are carrying:
     -Small Healing Draught [heals 22] (0.3kg, 20g)
     -Chronomaster's Hourglass [2charges] (rewinds one turn)
     -Bone Shortsword [dmg 6] (2.0kg, 50g)
     -Bone Shortsword [dmg 6] (2.0kg, 50g)
     -Grave Shroud [def 3] (3.0kg, 43g)

use Chronomaster's Hourglass
[HP 76/80] > 
  Sand runs backwards. The last moment unhappens.

inventory
[HP 76/80] > 
  You are carrying nothing.
```

**How long did US-5.4 actually take you?** _30_ minutes

## 5. The replay

**Paste your `history` output and the first ten lines of replaying it with `--script=`:**

```
[INFO] --- exec-maven-plugin:3.2.0:java (default-cli) @ dungeonforge ---
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.6.0   seed 20260829

  Type 'help' for what you can do. You are carrying a hourglass -- 'use Chornomaster's Hourglass' rewinds a turn.
  [L1R0Level 1: Crypt
    "Burial niches line the walls. Most are empty. MOST."
    Exits: north

[script] > n
  You go north.
  Ghoul Ghoullooses a shot for 2

[script] > help
  Available: [north, south, down, attack, take, drop, use, look, loot, inventory, status, quests, help, quit]
 Aliases: {n=north, s=south, d=down, a=attack, kill=attack, hit=attack, get=take, t=take, drink=use, quaff=use, i=inventory, ?=help, exit=quit}

[script] > n
  You cannot walk away with enemies still standing.

[script] > hit
  You hit Ghoul for 10
  Ghoul Ghoullooses a shot for 2

[script] > hit
  You hit Ghoul for 10
  ACHIEVEMENT: First Blood -- Defeat your first monster
  Ghoul dies.12

[script] > loot
  You take Bone Shortsword.
  You take Bone Shortsword.
  You take Grave Shroud.

[script] > n
  You go north.

[script] > look
  [L1R2Level 1: Crypt
    "Burial niches line the walls. Most are empty. MOST."
    Exits: south, north

[script] > quit
  You lay down your pack.
```

## 6. Tests and CI

**`mvn test` summary:**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ mvn test
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper (file:/usr/share/maven/lib/guava.jar)
WARNING: Please consider reporting this to the maintainers of class com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.7.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ dungeonforge ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 3 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Recompiling the module because of added or removed source files.
[INFO] Compiling 60 source files with javac [debug release 21] to target/classes
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ dungeonforge ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/chemito/IdeaProjects/dungeonforge/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 4 source files with javac [debug release 21] to target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.039 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.SingletonTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.StrategyObserverTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.015 s
[INFO] Finished at: 2026-10-11T05:00:04-07:00
[INFO] ------------------------------------------------------------------------
```

**D3**

Replay depends on the Singleton instance we created in Week 3 because the game runs on a seed and as such will have
identical gameplay given the same seed and inputs. A fifth capability that it has it open for extension but closed for 
modification to an extent. The example of adding a GUI later on is a much easier task with everything programmed to an 
interface, GUI elements can be tied to these interfaces.

**D4**

RoomPopulator's populate method is very similar to the AbstractCommand.execute() as they both defer steps to other 
objects. It is the factory method pattern.



**Green CI URL:**

## 7. Sprint review — one sentence

> What can the project do now that it could not do last week?

The project can now take aliases as inputs, encapsulates requests as objects in order to store them to use and treat as
objects. Using the hourglass item, the game can be set to a previous state with the turn being recreated using a 
complete state copy.
