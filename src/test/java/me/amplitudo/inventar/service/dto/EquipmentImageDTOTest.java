package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentImageDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentImageDTO.class);
        EquipmentImageDTO equipmentImageDTO1 = new EquipmentImageDTO();
        equipmentImageDTO1.setId(1L);
        EquipmentImageDTO equipmentImageDTO2 = new EquipmentImageDTO();
        assertThat(equipmentImageDTO1).isNotEqualTo(equipmentImageDTO2);
        equipmentImageDTO2.setId(equipmentImageDTO1.getId());
        assertThat(equipmentImageDTO1).isEqualTo(equipmentImageDTO2);
        equipmentImageDTO2.setId(2L);
        assertThat(equipmentImageDTO1).isNotEqualTo(equipmentImageDTO2);
        equipmentImageDTO1.setId(null);
        assertThat(equipmentImageDTO1).isNotEqualTo(equipmentImageDTO2);
    }
}
