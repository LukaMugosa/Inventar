package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class ProblemReportDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemReportDTO.class);
        ProblemReportDTO problemReportDTO1 = new ProblemReportDTO();
        problemReportDTO1.setId(1L);
        ProblemReportDTO problemReportDTO2 = new ProblemReportDTO();
        assertThat(problemReportDTO1).isNotEqualTo(problemReportDTO2);
        problemReportDTO2.setId(problemReportDTO1.getId());
        assertThat(problemReportDTO1).isEqualTo(problemReportDTO2);
        problemReportDTO2.setId(2L);
        assertThat(problemReportDTO1).isNotEqualTo(problemReportDTO2);
        problemReportDTO1.setId(null);
        assertThat(problemReportDTO1).isNotEqualTo(problemReportDTO2);
    }
}
