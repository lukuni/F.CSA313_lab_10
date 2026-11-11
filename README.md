# Лаборатори №10 – Өгөгдлийн урсгалын тест (DU хосууд)

Нэр: Ч.Оюумаа  
Оюутны код: B232270133

## Ажлын орчин
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

## Хэсэг A – gcd (мөр 13–20)

| # | Хувьсагч | Def мөр | Use мөр | Төрөл | Def-clear зам | Тестийн ID | Хамрагдсан |
|---|----------|---------|---------|-------|---------------|------------|------------|
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

Мөр 14 (`int tmp;`) нь утга оноогоогүй зарлалт тул def биш.

Тестүүд (`GcdTest.java`):
- `zeroIterations` – gcd(7,0)=7. Давталт 0 удаа ажиллана. x(13→20) ба y(13→(15,F)) хосыг
  ГАНЦААРАА хамардаг тест энэ, учир нь эдгээр хос давталтыг огт орохгүй байхыг шаарддаг.
- `twoIterations` – gcd(12,8)=4. Давталт 2 удаа ажиллана: (12,8)→(8,4)→(4,0).
  Def нь давталт дотор (17, 18), use нь давталтын эхэнд (15T, 16, 17) байгаа хосууд ХОЁР дахь
  давталтыг шаарддаг: x(17→16), y(18→15T), y(18→16), y(18→17).
- Хоёр тест хамтдаа gcd-ийн 13 DU хосыг бүгдийг хамарна (all-DU-pairs).

## Хэсэг B – bonus (мөр 31–56)

### B1 – CFG
![bonus-ийн CFG](cfg-bonus.png)

Мөр 38 (for-each): "attendance-д дараагийн элемент байгаа эсэх" нөхцөл нь `attendance`-ийн p-use;
T ирмэг дээр дараагийн элементийг `a`-д онооно (`a`-ийн def). CFG-ийн ирмэг бүрийн хажууд
түүнийг дайрдаг тестийн метод нэрийг бичсэн.

### B2 – Мөр бүрийн def/use
```
31: def attendance, def threshold
32: p-use attendance (== null), p-use threshold (< 0)   [|| богино холболт: threshold нь зөвхөн attendance != null үед шалгагдана]
35: def present
36: def streak
37: def best
38: p-use attendance (дараагийн элемент бий юу?), def a
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

### B3 – DU хосууд
Тестийн ID: AD1 = allDefs_brokenThenLongStreak, AD2 = allDefs_allAbsentBelowThreshold,
DU1 = du_nullAttendance, DU2 = du_negativeThreshold, DU3 = du_emptyZeroThreshold,
DU4 = du_shortStreakBelowThreshold, DU5 = du_longStreakBelowThreshold

| # | Хувьсагч | Def | Use | Төрөл | Def-clear зам | Тестийн ID | Хамрагдсан |
|---|----------|-----|-----|-------|---------------|------------|------------|
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
| 18 | streak | 41 | (42,F) | p | 41→42→38 | AD1 (3-р өдөр) | ✔ |
| 19 | streak | 41 | 43 | c | 41→42→43 | AD1 | ✔ |
| 20 | streak | 46 | 41 | c | 46→38→39→40→41 | AD1 (2→3-р өдөр) | ✔ |
| 21 | best | 37 | (42,T) | p | 37→38→39→40→41→42→43 | AD1 | ✔ |
| 22 | best | 37 | (42,F) | p | 37→38→39→40→41→42→38 | – | биелэшгүй |
| 23 | best | 37 | (53,T) | p | 37→38→49→50→53→54 | – | биелэшгүй |
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

Нийт: def-clear замтай 35 def–use хослол; үүнээс 33 нь биелэх боломжтой – 33-уулаа хамрагдсан; 2 нь биелэшгүй.

**Мөр 46-ийн (`streak = 0`) kill:** else салаагаар дайрах бүх зам дээр мөр 36 ба мөр 41-ийн
streak-ийн def-ийг алдаг (kill). Жишээ нь 46-аар дайрсан 41→41 зам def-clear БИШ, гэхдээ 16-р хос
шууд зам 41→42→38→39→40→41-ээр def-clear хэвээр байгаа тул хос гэж тоологдоно.

**DU хос БИШ хослолууд (def-clear зам байхгүй):** streak 36→42, 36→43, 46→42, 46→43.
42 эсвэл 43-т хүрэхийн тулд зам заавал мөр 41-ээр дайрах ба тэнд streak дахин тодорхойлогддог (kill).
Мөн 50T-ээр дайрсан score 49→56 замыг мөр 51 алдаг (гэхдээ 50F-ээр 32-р хос оршин байна).

**Биелэшгүй хосууд:**
- #22 best(37,(42,F)): best 0 хэвээр байх үед мөр 41 дөнгөж streak-ийг ≥ 1 болгосон тул `streak > best` үргэлж үнэн.
- #23 best(37,(53,T)): best хэзээ ч шинэчлэгдээгүй бол 0 хэвээр үлдэх ба `0 >= 5` үргэлж худал.

### B4 – All-defs: яагаад хамгийн багадаа 2 тест вэ
13 def тус бүр дор хаяж нэг use-д хүрэх ёстой. `score` мөр 49 ба мөр 51-д тодорхойлогддог.
Мөр 50 нэг дуудалтад ганцхан удаа ажилладаг: үнэн бол мөр 51 нь мөр 49-ийн утгыг ямар ч use-ээс
өмнө дарж бичнэ; худал бол мөр 51 огт ажиллахгүй. Тиймээс score-ийн ХОЁР def-ийг нэг гүйцэтгэлд
use-д хүргэх боломжгүй, дор хаяж 2 тест хэрэгтэй: AD1 (present ≥ threshold → def 51) ба
AD2 (present < threshold → def 49). Бусад бүх def-ийг AD1 ганцаараа хамарна
(present 35/40, streak 36/41/46, best 37/43, a 38, score 51/54).

### B5 – JaCoCo: branch ба DU хамралт
| Шат | Tests run | BRANCH missed / covered | LINE missed / covered | Файл |
|-----|-----------|--------------------------|------------------------|------|
| all-defs | 4 | 2 / 14 | 2 / 22 | results/jacoco-alldefs.csv, results/mvn-verify-alldefs.txt |
| эцсийн | 10 | 0 / 16 | 1 / 23 | results/jacoco-final.csv, results/mvn-verify-final.txt |

- Эцсийн тайлан дахь LINE_MISSED = 1 нь мөр 7 – Java-гийн далд default constructor `DataFlowTargets()`.
- **All-defs хангагдсан ч branch 100% биш байж болох уу?** Тийм. Зөвхөн AD1 ба AD2-оор 13 def бүгд
  use-д хүрдэг ч мөр 32-ын хоёр ҮНЭН салаа (`attendance == null`, `threshold < 0`) болон мөр 33
  огт ажиллаагүй (14/16).
- **Branch 100% болсон ч хамрагдаагүй DU хос үлдэж болох уу?** Тийм. {AD1, AD2, DU1, DU2} багц
  16/16 branch өгдөг ч 5 DU хосыг алгасна: #10 present(35,(50,T)), #14 present(40,(50,F)),
  #28 best(43,(53,F)), #31 score(49,54), #34 score(51,56). Жишээ нь #31 нь мөр 50 ХУДАЛ **ба** мөр 53
  ҮНЭН байхыг НЭГ гүйцэтгэлд шаарддаг; branch хамралт харин салаа бүрийг аль ч тестэд нэг удаа
  дайрахад л хангалттай. gcd-д ч мөн адил: gcd(8,4) ганцаараа мөр 15-ын хоёр салааг хамардаг ч
  давталт ганц удаа ажиллах тул x(17→16), y(18→15T), y(18→16), y(18→17) хамрагдахгүй үлдэнэ.
  → Branch хамралт all-DU-pairs хамралтыг баталгаажуулдаггүй.

## Хэсэг C – Fault seeding (алдаа тарих)

| Мутаци | Төрөл | Үр дүнгийн файл | Tests run / Failures | Унасан тест: мессеж | Үр дүн |
|--------|-------|-----------------|----------------------|---------------------|--------|
| M1: мөр 41 `streak = streak + 1;`-ийг устгах | дутуу def (missing update) | results/mvn-test-M1.txt | 9 / 2 | `allDefs_brokenThenLongStreak: expected: <8> but was: <5>`; `du_longStreakBelowThreshold: expected: <3> but was: <0>` | баригдсан |
| M2: мөр 43 `best = streak;` → `best = present;` | буруу хувьсагч (wrong variable) | results/mvn-test-M2.txt | 9 / 0 | – | **амьд үлдсэн** |
| M2 + шинэ тест | | results/mvn-test-M2-kill.txt | 10 / 1 | `m2_absenceBeforeLongestStreak: expected: <5> but was: <8>` | баригдсан |
| M3: давталтын дараа `int total = present + best;` | үхсэн def (dead definition) | results/mvn-test-M3.txt | 10 / 0 | – | амьд үлдсэн (тестээр барих боломжгүй) |

**M1:** мөр 41 байхгүй бол `streak` үргэлж 0 хэвээр, `streak > best` (42) хэзээ ч үнэн болохгүй,
`best` 0 хэвээр үлдэж +3 оноо хэзээ ч нэмэгдэхгүй.

**M2 – бүх 33 DU хос хамрагдсан атал яагаад баригдсангүй вэ:** `best` шинэчлэгдэх мөчид (мөр 43)
өмнө нь тасалсан өдөр байгаагүй бол `present == streak` байдаг. DU3–DU5-д цувааны өмнө тасалсан өдөр
байхгүй тул `present` ба `streak` тэнцүү. AD1-д ({1,0,1,1,1,1,1}) тэд ялгаатай ч мутантын эцсийн best
мөн 5 болж, `best >= 5` шалгалтын нэг талд үлддэг тул оноо (8) өөрчлөгдөхгүй. DU хамралт нь def→use зам
бүр ажилласныг л баталгаажуулдаг болохоос буруу утга гаралтыг өөрчилнө гэдгийг баталгаажуулдаггүй.
**Унагах тест** `BonusM2KillTest.m2_absenceBeforeLongestStreak`: {1,1,0,1,1,1}, threshold 1. Жинхэнэ
хамгийн урт цуваа 3 → оноо 5. Мутант 6-р өдөр best = present = 5 болгоно → 5 + 3 = 8.

**M3 – яагаад ямар ч тест унахгүй вэ:** `total` тодорхойлогдсон ч хаана ч хэрэглэгдэхгүй тул түүнд
DU хос байхгүй, гаралтад нөлөөлж чадахгүй. javac ашиглагдаагүй локал хувьсагчийн талаар анхааруулдаггүй.
Үүнийг static analysis багаж барина: IDE-ийн шалгалт, SonarLint, PMD (`UnusedLocalVariable`),
SpotBugs (`DLS_DEAD_LOCAL_STORE`).
IDE-ийн анхааруулга (VS Code, Java өргөтгөл), мутацлагдсан файлын мөр 49:
`The value of the local variable total is not used` – Java(536870973)

## Дүгнэлт
DU анализ нь def ба түүний use-ийг нэг гүйцэтгэлд def-clear замаар дайрахыг шаарддаг тул branch
хамралтаас илүү хүчтэй бөгөөд branch хамралт алгасаж болох хослолуудыг (жишээ нь score 49→54) олдог.
Мөн давталтаар дамжих хосуудыг ил гаргадаг – энэ нь gcd(12,8)-д хоёр дахь давталтыг шаардсан.
Гэхдээ илүү үнэтэй: JaCoCo DU хамралтыг хэмждэггүй тул CFG, def/use жагсаалт, 35 мөр бүхий хүснэгтийг
kill болон биелэшгүй хосуудын хамт гараар байгуулж шалгах шаардлагатай болсон.
All-DU-pairs ч бүх алдааг барихад хангалтгүй: M2 буруу утга нь гаралтыг өөрчлөөгүй тул амьд үлдсэн,
M3-ыг зөвхөн static analysis-аар олох боломжтой.
Хамгийн их цаг авсан алхам нь bonus-ийн DU хүснэгт (B3), мөн хоёр шатны commit болон JaCoCo-гийн
үр дүнг зөв гаргах явдал байсан (JaCoCo нь target/jacoco.exec-д нэмж бичдэг тул зөв тоо гаргахын тулд
`mvn clean verify` хэрэгтэй).

## AI ашигласан тухай
AI туслах (Claude)-г алхам алхмаар заавар авах, DU хүснэгт болон README-ийн текстийн ноорог гаргах,
README-г монгол хэл рүү орчуулах, тестийн оролтыг сонгох, cfg-bonus.png-г Graphviz-ээр үүсгэхэд ашигласан.
Энэ README-ийн бүх тоог results/ доторх файлуудаас хуулсан.
