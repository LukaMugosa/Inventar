package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.ManufacturerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manufacturer} and its DTO {@link ManufacturerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManufacturerMapper extends EntityMapper<ManufacturerDTO, Manufacturer> {


    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "removeEquipments", ignore = true)
    Manufacturer toEntity(ManufacturerDTO manufacturerDTO);

    default Manufacturer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(id);
        return manufacturer;
    }
}
