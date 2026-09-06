# Artifact Review Clinic — Lab 2, Part C

> **This is the only document you write this week.** Everything else — the epics, the
> stories, the acceptance criteria, the Definition of Done, the sprint plans — was written
> for you.
>
> Reading critically is a harder and more useful skill than writing from a blank page, and it
> is the one that will make your own stories good when you start writing them in Week 6.

Read all three before answering:
- `docs/backlog.md`
- `docs/definition-of-done.md`
- `docs/sprint-01-plan.md`

---

## C1 — Find the three planted flaws · 12 pts

There is **exactly one deliberate defect in each of the three documents**: one bad user
story, one unverifiable Definition-of-Done criterion, and one sprint-plan item that isn't
what it claims to be.

> **Hint for the story:** re-read INVEST first. The bad one fails more than one letter.
>
> **Hint for the DoD:** ask of every checkbox — *could two reasonable people disagree about
> whether this is true?* If yes, it isn't a criterion. It's an opinion.

### Flaw 1 — in `docs/backlog.md`

**Which item:**
US-1.4 — Improve the configuration code

**What's wrong with it:**
Super vague, does not describe what "better and more professional" code is. Description mentions doing "refactoring" the code to use a hashmap and even is assigned 5 points which seems like too much.

**Which INVEST letter(s) it violates, and how:**
This user story violates V for valuable because it mentions changing the code just for developer clarity with vague goals.
It also violates T for testable because the acceptance criteria does not give a definite test nor are the subtasks actually attainable.
N: violates negotiable because it tells developer to use hashmap with double-locking
V: subjective professional code, subjective to developer
T: no "more professional code" is subjective, an opinion.

**My repaired version:**

Pick a real beneficiary and define in measurable turns what better code is. 
The performance of this code increases by 5%.
```
As a ...,
I want ...,
so that ...

Acceptance Criteria
- Given ..., when ..., then ...
- Given ..., when ..., then ...
```

---

### Flaw 2 — in `docs/definition-of-done.md`

**Which checkbox:**

"The code is well written and easy for someone else to understand"

**Why it can't actually be checked:**

No tangible test or measure, is subjective and an opinion.

**My replacement, phrased so that it can be:**

Every public class has a comment on why it exists.
All code standards are verified, naming conventions, etc.

---

### Flaw 3 — in `docs/sprint-01-plan.md`

**Which item:**
The risk "I might get busy this week" 

**Why it isn't really what the document calls it:**
It is not a risk relating to the actual work and its mitigation is not tangible or actionable 

**My repaired version, including a mitigation someone could actually act on:**
The workload for this story might be too much for one story, best to split into smaller stories if possible to manage expectations.
---

## C2 — Say what's good, and why · 9 pts

Pick the **three strongest user stories** in `docs/backlog.md`. For each, two or three
sentences.

> Praise is harder than criticism, and it's where most of the learning is. "It's clear" earns
> nothing. "Its third criterion names an observable output — the same object reference — so
> two people would always agree whether it passed" earns full marks.

### Strong story 1: "S0.1 — The project builds and tests itself"

**INVEST letters it satisfies especially well:**
I: Is fully independent and does not rely on other user stories
V: Valuable to customer because it allows for the developer to save time by having a test.

**What specifically makes its acceptance criteria checkable:**
We are given examples of how to verify that the criteria is met by running "mvn test" and "mvn exec:java" or checking the Java sdk

### Strong story 2: "US-1.2 — The same seed produces the same dungeon"

**INVEST letters it satisfies especially well:**

T: all of the acceptable criteria is testable and can be checked by tests 

V: the so that... bug can be reproduced and removes the actual randomness to mirror behavior from developer to user

**What specifically makes its acceptance criteria checkable:**
Can be checked by unit tests and are no opinions. There is no refuting the result if it matches the criteria.



### Strong story 3: "S0.3 — The board shows the truth
"

**INVEST letters it satisfies especially well:**

V: valuable because it prevents the developer from forgetting progress while working on the project as a whole. Elimiates future guesswork

S: it is a smaller task that is reflected in its points, simple and the criteria are related

**What specifically makes its acceptance criteria checkable:**

One can take each of the acceptance criteria as step by step instructions to verify that they work as intended. Intended behavior is clearly outlined.

---

## C3 — Trace a story to code · 4 pts

Take **US-1.1** (settings live in one place). **Write no Java.** In plain English, describe
what you'd expect to see in the pull-request diff when this story is done, and which
acceptance criterion each piece satisfies.

| What I'd expect in the diff                                                                                                   | Which acceptance criterion it satisfies                                                                                                |
|-------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| Write a config.json file that has all the tunable values and use GameConfig to access and use them over the hard coded values | Given config.json sets playerStartingHp to 80, when a new game starts, then the player has 80 hit points.                              
| Leave hard coded default values in the configuation class only                                                                | "Given the source tree, when I search for a hardcoded starting-HP literal, then there are zero matches outside the configuration class. 
| Making getInstance() static makes any two separate calls to GameConfig.getInstance() the same reference                       | "Given two separate calls to GameConfig.getInstance(), when I compare the two references, then they are the same object."              |  |
| If config.json is gone or courupted then the hard coded values left in the configuration class will be used | "Given a missing or unreadable config.json, when the game starts, then documented default values are used and the game still runs."|                                                                                                                                        |
|                                                                                                                               |                                                                                                                                        |
|                                                                                                                               |                                                                                                                                        |

**One sentence: how did the acceptance criteria help you predict the shape of the work?**

The acceptance criteria guided production and gave the developer its needs in clarity

---

## C4 — The bonus catch · up to +3 bonus

Once you have dealt with the bad story, something in `docs/sprint-01-plan.md` no longer adds
up the way it did.

**What is it:**

The commited story points likely will not add up as they should now that the last story in that Sprint has been adjusted. It also goes over the capacity story points as those are ~10.

**What a real team would do about it in sprint planning:**

A real team might take a look at 1.4 and decide to split it up or adjust how many points it would require.

**What this suggests about the relationship between vague work and over-committed sprints:**

Without percise goals and testable criteria vague sprints can cause over commitment and too much work. Wasted effort on unclear goals can cause confusion within a Scrum group.

---

## C5 — One honest question

What is one thing about the Scrum process you still don't understand after this week? A good
question here is worth more to me than a confident wrong answer.

I guess one thing I still don't understand about Scrum is how it works when a customer is not sure about what they want. In my understanding of Scrum the customer will ask for features and fixes but the depth of what they ask for and know what works for them depends on their knowledge of the matter at hand.