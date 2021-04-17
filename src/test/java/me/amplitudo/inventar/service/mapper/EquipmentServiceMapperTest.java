package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentServiceMapperTest {

    private EquipmentServiceMapper equipmentServiceMapper;

    @BeforeEach
    public void setUp() {
        equipmentServiceMapper = new EquipmentServiceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentServiceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentServiceMapper.fromId(null)).isNull();
    }
}
