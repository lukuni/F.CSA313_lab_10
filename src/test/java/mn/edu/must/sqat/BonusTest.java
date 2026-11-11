package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BonusTest {

    // ===== Stage 1: ALL-DEFS (minimum 2 tests) =====

    @Test
    @DisplayName("allDefs1 {1,0,1,1,1,1,1} thr=1 -> 8: present35,40 streak36,41,46 best37,43 a38 score51,54")
    void allDefs_brokenThenLongStreak() {
        assertEquals(8, DataFlowTargets.bonus(new int[]{1, 0, 1, 1, 1, 1, 1}, 1));
    }

    @Test
    @DisplayName("allDefs2 {0,0} thr=1 -> 0: score49 reaches 56 (50F)")
    void allDefs_allAbsentBelowThreshold() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{0, 0}, 1));
    }
}
