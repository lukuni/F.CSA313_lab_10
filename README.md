# Lab 10 – Data-flow testing (DU pairs)

Name: Oyumaa Chimeddagva  
Student ID: B232270133

## Environment
```
$ java -version
openjdk version "27" 2026-09-15
OpenJDK Runtime Environment Homebrew (build 27)
OpenJDK 64-Bit Server VM Homebrew (build 27, mixed mode, sharing)
$ mvn -version
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: /opt/homebrew/Cellar/maven/3.9.16/libexec
Java version: 27, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/27/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "26.1", arch: "aarch64", family: "mac"
```

## Part A – gcd (lines 13–20)

| # | Var | Def | Use | Type | Def-clear path | Test ID | Covered |
|---|-----|-----|-----|------|----------------|---------|---------|
| 1 | x | 13 | 16 | c-use | 13→15→16 | twoIterations | ✔ |
| 2 | x | 13 | 20 | c-use | 13→15→20 | zeroIterations | ✔ |
| 3 | x | 17 | 16 | c-use | 17→18→15→16 | twoIterations | ✔ |
| 4 | x | 17 | 20 | c-use | 17→18→15→20 | twoIterations | ✔ |
| 5 | y | 13 | (15,T) | p-use | 13→15→16 | twoIterations | ✔ |
| 6 | y | 13 | (15,F) | p-use | 13→15→20 | zeroIterations | ✔ |
| 7 | y | 13 | 16 | c-use | 13→15→16 | twoIterations | ✔ |
| 8 | y | 13 | 17 | c-use | 13→15→16→17 | twoIterations | ✔ |
| 9 | y | 18 | (15,T) | p-use | 18→15→16 | twoIterations | ✔ |
| 10 | y | 18 | (15,F) | p-use | 18→15→20 | twoIterations | ✔ |
| 11 | y | 18 | 16 | c-use | 18→15→16 | twoIterations | ✔ |
| 12 | y | 18 | 17 | c-use | 18→15→16→17 | twoIterations | ✔ |
| 13 | tmp | 16 | 18 | c-use | 16→17→18 | twoIterations | ✔ |

Line 14 (`int tmp;`) is a declaration without a value, so it is not a def.

Tests (`GcdTest.java`):
- `zeroIterations` – gcd(7,0)=7. The loop runs 0 times. It is the ONLY test that covers
  x(13→20) and y(13→(15,F)), because these pairs need the loop to be skipped.
- `twoIterations` – gcd(12,8)=4. The loop runs 2 times: (12,8)→(8,4)→(4,0).
  Pairs whose def is inside the loop (17, 18) and whose use is at the top of the loop
  (15T, 16, 17) need a SECOND iteration: x(17→16), y(18→15T), y(18→16), y(18→17).
- Two tests together cover all 13 DU pairs (all-DU-pairs).

## Part B – bonus (lines 31–56)

### B1 – CFG
![CFG of bonus](cfg-bonus.png)

Line 38 (for-each): the condition "attendance has a next element" is a p-use of `attendance`;
on the T edge the next element is assigned to `a` (def of `a`).

### B2 – Def/use per line
```
31: def attendance, def threshold
32: p-use attendance (== null), p-use threshold (< 0)   [|| short-circuit: threshold only if attendance != null]
35: def present
36: def streak
37: def best
38: p-use attendance (has next?), def a
39: p-use a
40: c-use present, def present
41: c-use streak, def streak
42: p-use streak, p-use best
43: c-use streak, def best
46: def streak
49: def score
50: p-use present, p-use threshold
51: def score
53: p-use best
54: c-use score, def score
56: c-use score
```

### B3 – DU pairs
Test IDs: AD1 = allDefs_brokenThenLongStreak, AD2 = allDefs_allAbsentBelowThreshold,
DU1 = du_nullAttendance, DU2 = du_negativeThreshold, DU3 = du_emptyZeroThreshold,
DU4 = du_shortStreakBelowThreshold, DU5 = du_longStreakBelowThreshold

| # | Var | Def | Use | Type | Def-clear path | Test ID | Covered |
|---|-----|-----|-----|------|----------------|---------|---------|
| 1 | attendance | 31 | (32,T) | p | 31→32→33 | DU1 | ✔ |
| 2 | attendance | 31 | (32,F) | p | 31→32→32b | AD1 | ✔ |
| 3 | attendance | 31 | (38,T) | p | 31→32→35..37→38→39 | AD1 | ✔ |
| 4 | attendance | 31 | (38,F) | p | 31→32→35..37→38→49 | DU3 | ✔ |
| 5 | threshold | 31 | (32,T) | p | 31→32→32b→33 | DU2 | ✔ |
| 6 | threshold | 31 | (32,F) | p | 31→32→32b→35 | AD1 | ✔ |
| 7 | threshold | 31 | (50,T) | p | 31→…→49→50→51 | AD1 | ✔ |
| 8 | threshold | 31 | (50,F) | p | 31→…→49→50→53 | AD2 | ✔ |
| 9 | present | 35 | 40 | c | 35→36→37→38→39→40 | AD1 | ✔ |
| 10 | present | 35 | (50,T) | p | 35→…→38→49→50→51 | DU3 | ✔ |
| 11 | present | 35 | (50,F) | p | 35→…→38→(39F→46→38)*→49→50→53 | AD2 | ✔ |
| 12 | present | 40 | 40 | c | 40→41→42→(43)→38→39→40 | AD1 | ✔ |
| 13 | present | 40 | (50,T) | p | 40→41→42→…→38→49→50→51 | AD1 | ✔ |
| 14 | present | 40 | (50,F) | p | 40→41→42→…→38→49→50→53 | DU4 | ✔ |
| 15 | streak | 36 | 41 | c | 36→37→38→39→40→41 | AD1 | ✔ |
| 16 | streak | 41 | 41 | c | 41→42→(43)→38→39→40→41 | AD1 | ✔ |
| 17 | streak | 41 | (42,T) | p | 41→42→43 | AD1 | ✔ |
| 18 | streak | 41 | (42,F) | p | 41→42→38 | AD1 (day 3) | ✔ |
| 19 | streak | 41 | 43 | c | 41→42→43 | AD1 | ✔ |
| 20 | streak | 46 | 41 | c | 46→38→39→40→41 | AD1 (day 2→3) | ✔ |
| 21 | best | 37 | (42,T) | p | 37→38→39→40→41→42→43 | AD1 | ✔ |
| 22 | best | 37 | (42,F) | p | 37→38→39→40→41→42→38 | – | infeasible |
| 23 | best | 37 | (53,T) | p | 37→38→49→50→53→54 | – | infeasible |
| 24 | best | 37 | (53,F) | p | 37→38→…→49→50→53→56 | AD2 | ✔ |
| 25 | best | 43 | (42,T) | p | 43→38→39→40→41→42→43 | AD1 | ✔ |
| 26 | best | 43 | (42,F) | p | 43→38→39→46→38→39→40→41→42→38 | AD1 | ✔ |
| 27 | best | 43 | (53,T) | p | 43→38→49→50→53→54 | AD1 | ✔ |
| 28 | best | 43 | (53,F) | p | 43→38→49→50→53→56 | DU4 | ✔ |
| 29 | a | 38 | (39,T) | p | 38→39→40 | AD1 | ✔ |
| 30 | a | 38 | (39,F) | p | 38→39→46 | AD1 | ✔ |
| 31 | score | 49 | 54 | c | 49→50(F)→53(T)→54 | DU5 | ✔ |
| 32 | score | 49 | 56 | c | 49→50(F)→53(F)→56 | AD2 | ✔ |
| 33 | score | 51 | 54 | c | 51→53(T)→54 | AD1 | ✔ |
| 34 | score | 51 | 56 | c | 51→53(F)→56 | DU3 | ✔ |
| 35 | score | 54 | 56 | c | 54→56 | AD1 | ✔ |

Total: 35 def–use combinations with a def-clear path; 33 feasible – all 33 covered; 2 infeasible.

**Kill by line 46 (`streak = 0`):** it kills the streak defs from line 36 and line 41 on every path
that goes through the else branch. For example, 41→41 through 46 is NOT def-clear, but pair 16 still
exists through the direct path 41→42→38→39→40→41.

**Combinations that are NOT DU pairs (no def-clear path):** streak 36→42, 36→43, 46→42, 46→43.
To reach 42 or 43 the path must go through line 41, which redefines streak (kill).
Also score 49→56 through 50T is killed by line 51 (but pair 32 exists through 50F).

**Infeasible:**
- #22 best(37,(42,F)): while best is still 0, line 41 has just made streak ≥ 1, so `streak > best` is always true.
- #23 best(37,(53,T)): if best is never updated it stays 0, and `0 >= 5` is always false.

### B4 – All-defs: why 2 tests is the minimum
13 defs must each reach at least one use. `score` is defined on line 49 and on line 51.
Line 50 runs only once per call: if it is true, line 51 overwrites line 49 before any use;
if it is false, line 51 never runs. So no single execution can take BOTH score defs to a use,
and we need at least 2 tests: AD1 (present ≥ threshold → def 51) and AD2 (present < threshold → def 49).
AD1 alone covers all other defs (present 35/40, streak 36/41/46, best 37/43, a 38, score 51/54).

### B5 – JaCoCo: branch vs DU coverage
| Stage | Tests run | BRANCH missed / covered | LINE missed / covered | File |
|-------|-----------|--------------------------|------------------------|------|
| all-defs | 4 | 2 / 14 | 2 / 22 | results/jacoco-alldefs.csv, results/mvn-verify-alldefs.txt |
| final | 10 | 0 / 16 | 1 / 23 | results/jacoco-final.csv, results/mvn-verify-final.txt |

- LINE_MISSED = 1 in the final report is line 7, the implicit default constructor `DataFlowTargets()`.
- **all-defs satisfied but branch < 100%?** Yes. With only AD1 and AD2, all 13 defs reach a use,
  but the two TRUE branches on line 32 (`attendance == null`, `threshold < 0`) and line 33 are never executed (14/16).
- **Branch 100% but DU pairs still uncovered?** Yes. The set {AD1, AD2, DU1, DU2} gives 16/16 branches,
  but misses 5 DU pairs: #10 present(35,(50,T)), #14 present(40,(50,F)), #28 best(43,(53,F)),
  #31 score(49,54), #34 score(51,56). For example, #31 needs line 50 FALSE **and** line 53 TRUE in the SAME run;
  branch coverage only needs each branch once, in any test. The same happens in gcd: gcd(8,4) alone covers both
  branches of line 15, but runs the loop only once, so x(17→16), y(18→15T), y(18→16), y(18→17) stay uncovered.
  → Branch coverage does NOT imply all-DU-pairs coverage.

## Part C – Fault seeding

| Mutation | Type | Result file | Tests run / Failures | Failed test: message | Result |
|----------|------|-------------|----------------------|----------------------|--------|
| M1: delete line 41 `streak = streak + 1;` | missing update | results/mvn-test-M1.txt | 9 / 2 | `allDefs_brokenThenLongStreak: expected: <8> but was: <5>`; `du_longStreakBelowThreshold: expected: <3> but was: <0>` | killed |
| M2: line 43 `best = streak;` → `best = present;` | wrong variable | results/mvn-test-M2.txt | 9 / 0 | – | **survived** |
| M2 + new test | | results/mvn-test-M2-kill.txt | 10 / 1 | `m2_absenceBeforeLongestStreak: expected: <5> but was: <8>` | killed |
| M3: `int total = present + best;` after the loop | dead definition | results/mvn-test-M3.txt | 10 / 0 | – | survived (cannot be killed by tests) |

**M1:** without line 41 `streak` stays 0, so `streak > best` (42) is never true, `best` stays 0 and the +3 is never added.

**M2 – why it survived although all 33 DU pairs were covered:** at the moment `best` is updated (line 43),
`present == streak` unless there was an absence earlier. In DU3–DU5 there is no absence before the streak,
so `present` and `streak` are equal. In AD1 ({1,0,1,1,1,1,1}) they differ, but the mutant's final best is still 5,
the same side of the `best >= 5` check, so the score (8) does not change. DU coverage only guarantees that
each def→use path is executed, not that a wrong value changes the output.
**Kill test** `BonusM2KillTest.m2_absenceBeforeLongestStreak`: {1,1,0,1,1,1}, threshold 1. The real longest
streak is 3 → score 5. The mutant sets best = present = 5 on day 6 → 5 + 3 = 8.

**M3 – why no test fails:** `total` is defined but never used, so it has no DU pair and cannot affect the output.
javac does not warn about unused local variables. A static analysis tool catches it: IDE inspections,
SonarLint, PMD (`UnusedLocalVariable`), SpotBugs (`DLS_DEAD_LOCAL_STORE`).
IDE warning (VS Code, Java extension), line 49 of the mutated file:
`The value of the local variable total is not used` – Java(536870973)

## Conclusion
DU analysis is stronger than branch coverage because it requires a def and its use to happen in the same run
along a def-clear path, so it finds combinations (like score 49→54) that branch coverage can skip.
It also makes loop-carried pairs visible, which forced the second loop iteration in gcd(12,8).
It is more expensive: JaCoCo cannot measure DU coverage, so the CFG, the def/use list and the 35-row table
had to be built and checked by hand, including kills and infeasible pairs.
Even all-DU-pairs is not enough to catch every fault: M2 survived because the wrong value did not change the output,
and M3 can only be found by static analysis.
The step that took the most time was the bonus DU table (B3) together with getting the two-stage commits
and JaCoCo results right (JaCoCo appends to target/jacoco.exec, so `mvn clean verify` is needed for correct numbers).

## AI use
An AI assistant (Claude) was used for step-by-step guidance, for drafting the DU tables and README text,
for choosing test inputs, and for generating cfg-bonus.png with Graphviz.
All numbers in this README were copied from the files in results/.
