package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentImageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentImage.class);
        EquipmentImage equipmentImage1 = new EquipmentImage();
        equipmentImage1.setId(1L);
        EquipmentImage equipmentImage2 = new EquipmentImage();
        equipmentImage2.setId(equipmentImage1.getId());
        assertThat(equipmentImage1).isEqualTo(equipmentImage2);
        equipmentImage2.setId(2L);
        assertThat(equipmentImage1).isNotEqualTo(equipmentImage2);
        equipmentImage1.setId(null);
        assertThat(equipmentImage1).isNotEqualTo(equipmentImage2);
    }
}
