package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentImageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentImage} and its DTO {@link EquipmentImageDTO}.
 */
@Mapper(componentModel = "spring", uses = {EquipmentMapper.class})
public interface EquipmentImageMapper extends EntityMapper<EquipmentImageDTO, EquipmentImage> {

    @Mapping(source = "equipment.id", target = "equipmentId")
    EquipmentImageDTO toDto(EquipmentImage equipmentImage);

    @Mapping(source = "equipmentId", target = "equipment")
    EquipmentImage toEntity(EquipmentImageDTO equipmentImageDTO);

    default EquipmentImage fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentImage equipmentImage = new EquipmentImage();
        equipmentImage.setId(id);
        return equipmentImage;
    }
}
