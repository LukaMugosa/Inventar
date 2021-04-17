package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentRequestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentRequestDTO.class);
        EquipmentRequestDTO equipmentRequestDTO1 = new EquipmentRequestDTO();
        equipmentRequestDTO1.setId(1L);
        EquipmentRequestDTO equipmentRequestDTO2 = new EquipmentRequestDTO();
        assertThat(equipmentRequestDTO1).isNotEqualTo(equipmentRequestDTO2);
        equipmentRequestDTO2.setId(equipmentRequestDTO1.getId());
        assertThat(equipmentRequestDTO1).isEqualTo(equipmentRequestDTO2);
        equipmentRequestDTO2.setId(2L);
        assertThat(equipmentRequestDTO1).isNotEqualTo(equipmentRequestDTO2);
        equipmentRequestDTO1.setId(null);
        assertThat(equipmentRequestDTO1).isNotEqualTo(equipmentRequestDTO2);
    }
}
