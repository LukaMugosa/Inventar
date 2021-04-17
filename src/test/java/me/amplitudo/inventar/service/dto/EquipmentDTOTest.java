package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDTO.class);
        EquipmentDTO equipmentDTO1 = new EquipmentDTO();
        equipmentDTO1.setId(1L);
        EquipmentDTO equipmentDTO2 = new EquipmentDTO();
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO2.setId(equipmentDTO1.getId());
        assertThat(equipmentDTO1).isEqualTo(equipmentDTO2);
        equipmentDTO2.setId(2L);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO1.setId(null);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
    }
}
