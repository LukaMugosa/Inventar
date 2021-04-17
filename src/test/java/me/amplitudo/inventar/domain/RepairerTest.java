package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class RepairerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repairer.class);
        Repairer repairer1 = new Repairer();
        repairer1.setId(1L);
        Repairer repairer2 = new Repairer();
        repairer2.setId(repairer1.getId());
        assertThat(repairer1).isEqualTo(repairer2);
        repairer2.setId(2L);
        assertThat(repairer1).isNotEqualTo(repairer2);
        repairer1.setId(null);
        assertThat(repairer1).isNotEqualTo(repairer2);
    }
}
