package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class SectorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectorDTO.class);
        SectorDTO sectorDTO1 = new SectorDTO();
        sectorDTO1.setId(1L);
        SectorDTO sectorDTO2 = new SectorDTO();
        assertThat(sectorDTO1).isNotEqualTo(sectorDTO2);
        sectorDTO2.setId(sectorDTO1.getId());
        assertThat(sectorDTO1).isEqualTo(sectorDTO2);
        sectorDTO2.setId(2L);
        assertThat(sectorDTO1).isNotEqualTo(sectorDTO2);
        sectorDTO1.setId(null);
        assertThat(sectorDTO1).isNotEqualTo(sectorDTO2);
    }
}
