package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class ManufacturerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManufacturerDTO.class);
        ManufacturerDTO manufacturerDTO1 = new ManufacturerDTO();
        manufacturerDTO1.setId(1L);
        ManufacturerDTO manufacturerDTO2 = new ManufacturerDTO();
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
        manufacturerDTO2.setId(manufacturerDTO1.getId());
        assertThat(manufacturerDTO1).isEqualTo(manufacturerDTO2);
        manufacturerDTO2.setId(2L);
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
        manufacturerDTO1.setId(null);
        assertThat(manufacturerDTO1).isNotEqualTo(manufacturerDTO2);
    }
}
