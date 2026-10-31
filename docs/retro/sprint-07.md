# Sprint Retrospective — Sprint 07 — Week 09

> Copy this to `docs/retro/sprint-NN.md` at the end of every sprint.
>
> This is the single highest-value ceremony for a solo developer, because nobody else is
> going to notice your patterns for you. Ten minutes. Be honest — nobody is grading you
> on having had a good week.

## 1. What went well?

- The decorator pattern was fairly easy to understand, the changes we were making to dungeonforge this week were 
expected and needed to add immersion to the game.

## 2. What slowed me down?

- Taking the time to trace each of the enchantments and how stacking them resulted in final total. And understanding how
it starts from a BasicWeapon which also implements Weapon so a FlamingVampiricKeen Weapon still has to implement the 
abstract methods from Weapon. It was a bit to wrap my head around. 

## 3. ONE thing I will do differently next sprint

> One. Specific. Actionable. "Be more organised" is not actionable.
> "Write the test before the implementation for at least one story" is.

- For the next sprint I really want to take the example we get from the textbook and try to relate it directly to the 
pattern and the way we apply it to dungeonforge. I sometimes have trouble seeing the pattern as we are implementing it
but I usually understand how the book explains it. Looking at the UML does help but seeing the code in action takes a 
bit to sink in.

---

## Numbers

- Points committed: __11__
- Points completed: __11__
- Hours spent: __13__
- Did I break my WIP limit of 2? ☐ yes x no — if yes, when and why?

## Carried over to next sprint

| Issue | Why it did not finish |
|---|---|
| # |  |

## Last sprint's "one thing" — did I actually do it?

> Look up the previous retro. Be honest. A retro nobody reads is a diary, not a process.

- Last sprint I said I would: Continue working on sprint to catch up on this week's pattern
- Did I? ☐ yes x partly ☐ no
- What I learned from that: This week had a lighter Lab and the pattern was easier to understand once we started writing
the unit tests which helped me see the pattern from the other end so to speak. Writing unit tests seems to help cement 
what each pattern is doing for our game and what it does and does not give us.
