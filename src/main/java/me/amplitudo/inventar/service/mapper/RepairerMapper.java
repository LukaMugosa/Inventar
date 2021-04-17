package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.RepairerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Repairer} and its DTO {@link RepairerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepairerMapper extends EntityMapper<RepairerDTO, Repairer> {


    @Mapping(target = "equipmentServices", ignore = true)
    @Mapping(target = "removeEquipmentService", ignore = true)
    Repairer toEntity(RepairerDTO repairerDTO);

    default Repairer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Repairer repairer = new Repairer();
        repairer.setId(id);
        return repairer;
    }
}
