package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class ProblemReportCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemReportCategoryDTO.class);
        ProblemReportCategoryDTO problemReportCategoryDTO1 = new ProblemReportCategoryDTO();
        problemReportCategoryDTO1.setId(1L);
        ProblemReportCategoryDTO problemReportCategoryDTO2 = new ProblemReportCategoryDTO();
        assertThat(problemReportCategoryDTO1).isNotEqualTo(problemReportCategoryDTO2);
        problemReportCategoryDTO2.setId(problemReportCategoryDTO1.getId());
        assertThat(problemReportCategoryDTO1).isEqualTo(problemReportCategoryDTO2);
        problemReportCategoryDTO2.setId(2L);
        assertThat(problemReportCategoryDTO1).isNotEqualTo(problemReportCategoryDTO2);
        problemReportCategoryDTO1.setId(null);
        assertThat(problemReportCategoryDTO1).isNotEqualTo(problemReportCategoryDTO2);
    }
}
