package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TenantMapperTest {

    private TenantMapper tenantMapper;

    @BeforeEach
    public void setUp() {
        tenantMapper = new TenantMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(tenantMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tenantMapper.fromId(null)).isNull();
    }
}
