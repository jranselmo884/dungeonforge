# Week 8 Evidence

## 1. BEFORE — the two problems

### Problem A: mode checks scattered across commands

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

**Paste it. How many command classes re-implement the same check?**

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
src/main/java/dungeonforge/commands/CommandParser.java:172:        if (room.hasLivingMonsters()) {
src/main/java/dungeonforge/commands/MoveCommand.java:19:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/TakeCommand.java:13:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/DropCommand.java:21:        if (room().hasLivingMonsters()) {
```

**Play a turn: start a fight, then type `north`. Paste what happens:**

```
hit
[HP 78/80] > 
  You hit Ghoul for 10.
  Ghoul changes tactics: hunting -> enraged.
  Ghoul hits you for 3.

n
[HP 75/80] > 
  You cannot walk away with enemies still standing.


```

**Now type `help` during that fight. Does it list `north`?** _Yes_
**Should it?**

It technically should not, because we cannot move while in a fight.

### Problem B: the monster lifecycle as a string plus conditionals

```bash
grep -n "getStateName" src/main/java/dungeonforge/core/Combat.java
```

**Paste it. Which three methods branch on the state name?**

```
grep -n "getStateName" src/main/java/dungeonforge/core/Combat.java
65:        String current = m.getStateName();
109:        String state = m.getStateName();
147:        if (m.getStateName().equals("enraged")) {
```

**Answer before you refactor:**

1. To add a "stunned" state, which methods must you edit?

2. Reading `updateMonsterState()`, which states can follow `enraged`? How long did it take you
   to be sure?

3. What stops `setStateName("dancing")` from compiling?


## 2. AFTER — US-6.1 and US-6.2

**Paste `help` in Exploring and in Combat, side by side:**

```

        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.7.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[Exploring | HP 80/80] > help

  [Exploring] you can: north, south, down, look, take, loot, drop, use, inventory, pack, status, quests, history, undo, help, quit

[Exploring | HP 80/80] > north

  You go north.
  Ghoul is now hunting.
  Combat! 1 hostile(s). Try 'attack'!

[Combat | HP 80/80] > help

  [Combat] you can: attack, look, use, inventory, status, history, undo, help, quit

[Combat | HP 80/80] > 
```

**Paste an attempt to move during combat:**

```

[Combat | HP 80/80] > north

  You cannot do that in Combat mode. 
     Available here: attack, look, use, inventory, status, history, undo, help, quit

[Combat | HP 80/80] > 
```

**Paste the grep proving the guards are gone:**

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
src/main/java/dungeonforge/commands/GameContext.java:58:        if (currentRoom.hasLivingMonsters()) transitionTo(new CombatState());
src/main/java/dungeonforge/commands/CommandParser.java:181:        if (room.hasLivingMonsters()) {
```

**Paste entering and leaving the pack, and the prompt in each mode:**

```
[Exploring | HP 80/80] > pack

   --- Your Pack --- ('back' to close

back
[Pack | HP 80/80] 
```

## 3. AFTER — US-6.3

**Paste the grep proving `Combat` no longer knows any state name:**

```bash
grep -n "getStateName\|\"enraged\"\|\"idle\"" src/main/java/dungeonforge/core/Combat.java
```

```
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ grep -n "getStateName\|\"enraged\"\|\"idle\"" src/main/java/dungeonforge/core/Combat.java
chemito@chemito-Lenovo:~/IdeaProjects/dungeonforge$ 
```

**Paste a fight showing a monster moving through its lifecycle:**

```
[Exploring | HP 80/80] > north

  You go north.
  Ghoul is now hunting.
  Combat! 1 hostile(s). Try 'attack'!

[Combat | HP 80/80] > hit

  You hit Ghoul for 10.
  Ghoul hits you for 2.
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Ghoul changes tactics: dungeonforge.behavior.RangedStrategy@32635e72 -> aggressive.
  Ghoul is now enraged.

[Combat | HP 78/80] > hit

  You hit Ghoul for 10.
  ACHIEVEMENT: First Blood -- Defeat your first monster
  Ghoul dies. +12 XP.
  The room falls quiet.

[Exploring | HP 78/80] > 
```

## 4. AFTER — US-6.4

**Paste the test output (or a demo) showing a healer that becomes aggressive when enraged:**

```

```

**In one sentence: which one changed the other, and could it have gone the other way?**


## 5. Tests and CI

**`mvn test` summary:**

```

```

**Green CI URL:**

## 6. Sprint review — one sentence

> What can the project do now that it could not do last week?

