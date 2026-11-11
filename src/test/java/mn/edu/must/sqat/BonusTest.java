package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // ===== Stage 2: extra tests for ALL-DU-PAIRS =====

    @Test
    @DisplayName("DU1 null -> IAE: attendance(31,32T)")
    void du_nullAttendance() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 1));
    }

    @Test
    @DisplayName("DU2 {1} thr=-1 -> IAE: threshold(31,32T)")
    void du_negativeThreshold() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[]{1}, -1));
    }

    @Test
    @DisplayName("DU3 {} thr=0 -> 5: present(35,50T) best(37,53F) score(51,56)")
    void du_emptyZeroThreshold() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{}, 0));
    }

    @Test
    @DisplayName("DU4 {1,1} thr=3 -> 0: present(40,50F) best(43,53F)")
    void du_shortStreakBelowThreshold() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{1, 1}, 3));
    }

    @Test
    @DisplayName("DU5 {1,1,1,1,1} thr=10 -> 3: score(49,54)")
    void du_longStreakBelowThreshold() {
        assertEquals(3, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1}, 10));
    }
}
