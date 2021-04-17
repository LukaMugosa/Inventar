package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SupplierMapperTest {

    private SupplierMapper supplierMapper;

    @BeforeEach
    public void setUp() {
        supplierMapper = new SupplierMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(supplierMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(supplierMapper.fromId(null)).isNull();
    }
}
