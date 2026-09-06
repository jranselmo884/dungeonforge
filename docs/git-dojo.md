# Git Dojo — my recovery notes

> Part D of Lab 2. For each drill: the command(s) you ran, **one sentence in your own
> words** on what it did, and one on when you would reach for it again.
>
> Graded on the sentences, not the commands. Commands can be copied; understanding cannot.

## The three trees — in my own words

| Tree | What lives here                                                      |
|---|----------------------------------------------------------------------|
| Working Directory | the coding we are currently editing                                  |
| Staging Area (Index) | code or edits that are waiting to be committed to the current branch |
| HEAD | the place in commits where we are currently at                       |

---

## Drill 1 — Committed to `main` by accident

**Commands I ran:**
```bash
git switch -c fix/rescued-work # branch now points at your commit
git switch main
git reset --hard origin/main 

```
**What it did:**
These commands created a new branch, pointed it at the accidental commint and then reset the main branch to the remote origin branch to remove any accidental edits to main.

**When I would use it again:**

I would definitely use this again if I accidentally find myself in main and make substantial progress but commit to main when I still want to keep the work to a branch.

---

## Drill 2 — Wrong commit message / forgot a file

**Commands I ran:**
```bash
echo "x" > note.txt && git add note.txt && git commit -m "asdf"
git commit --amend -m "docs: add note file"
```
**What it did:**
These commands created a note.txt and commited the change but initally with a bad commit message. Then the second one fixed the commit message without changing the commit itself.

**Why you must not do this to a commit you already pushed:**
Should not do this to a commit thats already been pushed because it rewrites history and will mess things up if its been commited to the remote branch.

---

## Drill 3 — Committed a file that should be ignored

**Commands I ran:**
```bash
git rm -r --cached target # stop tracking, keep the local files
echo "target/" >> .gitignore
git add .gitignore && git commit -m "chore: untrack build output and ignore target/"
```
**What it did:**
It fixes the accidentally commited file that was supposed to be ignored by first untracking it, then adding to gitignore, and finally commiting the untrack and ignore add.

**Why adding it to `.gitignore` alone was not enough:**
If the target was not untracked than regardless of the gitignore it would still continue to be tracked.

---

## Drill 4 — Merge conflict

**Commands I ran:**
```bash
git switch main
git switch -c feature/a
printf '# DungeonForge - branch A title\n' > README.md
git commit -am "docs: title from branch A"
git switch main
git switch -c feature/b
printf '# DungeonForge - branch B title\n' > README.md
git commit -am "docs: title from branch B"
git switch main
git merge feature/a # clean
git merge feature/b # CONFLICT

<<<<<<< HEAD
# DungeonForge - branch A title
=======
# DungeonForge - branch B title
>>>>>>> feature/b
```
**In the conflict markers, which side was "mine"?**
The left side was "mine"

**What it did:**
The code made two separate branches off of main and made changes to the same line to both. Then two were merged and after a conflict was found the conflict was resolved and merged.

**How I would back out of a merge I regretted starting:**
To back out of a merge you would run the "git merge --abort" command to put it all back.

---

## Drill 5 — "I destroyed everything"

**Commands I ran:**
```bash
git log --oneline # note the current hash
git reset --hard HEAD~3 # nuke the last three commits
git log --oneline # gone
git reflog # every position HEAD has held
git reset --hard <hash-from-before>
```
**What `git reflog` showed me:**
That no matter what I delete from the commits, "reflog" keeps a log of where the HEAD has been for 90 days. As long as I commit regularly then work can usually never be lost.

**One sentence on why this changes how nervous I should be about Git:**
This means that regardless of the changes that are made throughout, if Git if properly used than there is no need to be nervous about Git even on the command line.

---

## Stretch — Drill 6 (detached HEAD, interactive rebase)

**Notes:**
From what I understand about this drill, a detatched HEAD is useful for checking out prior commits and even "squashing" them together.

---

## The one command I want to remember from today
I definitely need to remember the Drill 1 commmand to fix commiting to main on accident. I would forget to branch off all the time.
