# The Singleton Audit — Lab 3, Part C

> **The hard part of Singleton week is not writing one. It is 12 lines of code.**
> The hard part is knowing when *not* to.
>
> Singleton is the most over-applied pattern in the book. A student who leaves this week able
> to write one has learned the easy half. A student who leaves able to *refuse* to write one
> has learned the half that matters.

Below are **eight** candidate classes from DungeonForge's future. Three you have already met;
five arrive in Weeks 4 to 15. For each, decide: **Singleton, or not?**

Answer with the test we will use all semester:

> **Would a second instance be a BUG, or merely unusual?**
>
> If two instances would produce *incorrect behaviour* — not just wasted memory, not just
> inconvenience — the class may deserve to be a Singleton.
> If two instances would merely be *odd*, it is a dependency, and you should pass it in.

Fill in every row. Two of the eight are genuine singletons; you already know which, because
you built them this week. Your job is to defend the other six answers.

| # | Class | What it does | Singleton? | Would a 2nd instance be a bug, or just unusual? Why? |
|---|---|---|---|---|
| 1 | `GameConfig` | Holds every tunable setting | x| |
| 2 | `RandomSource` | The one seeded RNG | x| |
| 3 | `Player` | The player character | | x|
| 4 | `MonsterFactory` (Wk 4) | Turns blueprints into monsters | | x|
| 5 | `EventBus` (Wk 5) | Publishes game events to subscribers | ?| ?|
| 6 | `CommandHistory` (Wk 7) | The undo stack | |x |
| 7 | `SaveSystemFacade` (Wk 12) | Reads and writes save files | | x|
| 8 | `Logger` | Writes diagnostic output to a file | ?| ?|

## The three that will cause arguments

Rows 5, 7 and 8 are the interesting ones, and reasonable engineers disagree about all three.
Pick **one** of them and write a paragraph:

**Which one:** _SaveSystemFacade_

**The case FOR making it a Singleton:**
My case for making this a singleton even with file locking is when a save system involves something like cloud saves which rely on a timestamp. I have seen many issues with cloud saves or even just mismatching times on local machines. Which then when adding the cloud to the mix and multiple people pinging the saves around the same time would cause issues. I firmly believe that a save program should just have one instance to run to prevent data loss or overwritten data.

**The case AGAINST:**
My case against making this a singleton is that while the above is true, checks and prompts before any data is overwritten goes a long way to making the user aware of what is going to be changed. A more permanent solution could be creating a log of any previous save data and keeping them in hidden in case of accidental save conflicts one can go back to the save they meant to work on. Similar to "git reflog" which keeps a master log of all changes that can be restored. 

**What you would actually do in this project, and why:**
I would personally use a combination of file locking and client side confirmation, especially paying detail to making sure timestamps from UTC are displayed. I would definitely make a log of all saves whether deleted of not to recover any lost data, and make accessible to the client.

> There is no answer key for this paragraph. You are graded on whether you engaged with the
> tension, not on which side you landed.

## One more question

Your `GameConfig` has a method called `resetForTests()`. It exists only so that tests can
undo the global state that the Singleton created.

**In one or two sentences: what is that method telling you about the pattern?**
This method tells me that when using a singleton you must "reset" the state of the instance the singleton created if you wish to make unit tests for CI. Becasue of the way a singleton creates a single and global state the instance must be cleared to test and after the test is done. 

