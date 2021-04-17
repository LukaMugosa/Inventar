package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentCategory} and its DTO {@link EquipmentCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentCategoryMapper extends EntityMapper<EquipmentCategoryDTO, EquipmentCategory> {


    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "removeEquipments", ignore = true)
    EquipmentCategory toEntity(EquipmentCategoryDTO equipmentCategoryDTO);

    default EquipmentCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setId(id);
        return equipmentCategory;
    }
}
