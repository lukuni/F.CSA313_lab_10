package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BonusM2KillTest {

    @Test
    @DisplayName("killM2 {1,1,0,1,1,1} thr=1 -> 5: absence BEFORE longest streak, so present != streak at line 43")
    void m2_absenceBeforeLongestStreak() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1, 1, 0, 1, 1, 1}, 1));
    }
}
