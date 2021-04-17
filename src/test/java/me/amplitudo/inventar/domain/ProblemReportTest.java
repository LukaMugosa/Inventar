package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class ProblemReportTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemReport.class);
        ProblemReport problemReport1 = new ProblemReport();
        problemReport1.setId(1L);
        ProblemReport problemReport2 = new ProblemReport();
        problemReport2.setId(problemReport1.getId());
        assertThat(problemReport1).isEqualTo(problemReport2);
        problemReport2.setId(2L);
        assertThat(problemReport1).isNotEqualTo(problemReport2);
        problemReport1.setId(null);
        assertThat(problemReport1).isNotEqualTo(problemReport2);
    }
}
