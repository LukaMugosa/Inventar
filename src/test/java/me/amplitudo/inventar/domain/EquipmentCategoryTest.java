package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentCategory.class);
        EquipmentCategory equipmentCategory1 = new EquipmentCategory();
        equipmentCategory1.setId(1L);
        EquipmentCategory equipmentCategory2 = new EquipmentCategory();
        equipmentCategory2.setId(equipmentCategory1.getId());
        assertThat(equipmentCategory1).isEqualTo(equipmentCategory2);
        equipmentCategory2.setId(2L);
        assertThat(equipmentCategory1).isNotEqualTo(equipmentCategory2);
        equipmentCategory1.setId(null);
        assertThat(equipmentCategory1).isNotEqualTo(equipmentCategory2);
    }
}
