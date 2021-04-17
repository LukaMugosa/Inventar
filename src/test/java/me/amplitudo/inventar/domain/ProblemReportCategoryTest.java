package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class ProblemReportCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemReportCategory.class);
        ProblemReportCategory problemReportCategory1 = new ProblemReportCategory();
        problemReportCategory1.setId(1L);
        ProblemReportCategory problemReportCategory2 = new ProblemReportCategory();
        problemReportCategory2.setId(problemReportCategory1.getId());
        assertThat(problemReportCategory1).isEqualTo(problemReportCategory2);
        problemReportCategory2.setId(2L);
        assertThat(problemReportCategory1).isNotEqualTo(problemReportCategory2);
        problemReportCategory1.setId(null);
        assertThat(problemReportCategory1).isNotEqualTo(problemReportCategory2);
    }
}
