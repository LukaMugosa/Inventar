package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentServicingMapperTest {

    private EquipmentServicingMapper equipmentServicingMapper;

    @BeforeEach
    public void setUp() {
        equipmentServicingMapper = new EquipmentServicingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentServicingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentServicingMapper.fromId(null)).isNull();
    }
}
