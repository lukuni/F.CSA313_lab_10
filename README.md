# Lab 10 – Data-flow testing (DU pairs)

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
