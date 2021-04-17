package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentEmployeeMapperTest {

    private EquipmentEmployeeMapper equipmentEmployeeMapper;

    @BeforeEach
    public void setUp() {
        equipmentEmployeeMapper = new EquipmentEmployeeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentEmployeeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentEmployeeMapper.fromId(null)).isNull();
    }
}
