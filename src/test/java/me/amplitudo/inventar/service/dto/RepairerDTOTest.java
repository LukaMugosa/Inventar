package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class RepairerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairerDTO.class);
        RepairerDTO repairerDTO1 = new RepairerDTO();
        repairerDTO1.setId(1L);
        RepairerDTO repairerDTO2 = new RepairerDTO();
        assertThat(repairerDTO1).isNotEqualTo(repairerDTO2);
        repairerDTO2.setId(repairerDTO1.getId());
        assertThat(repairerDTO1).isEqualTo(repairerDTO2);
        repairerDTO2.setId(2L);
        assertThat(repairerDTO1).isNotEqualTo(repairerDTO2);
        repairerDTO1.setId(null);
        assertThat(repairerDTO1).isNotEqualTo(repairerDTO2);
    }
}
