package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equipment} and its DTO {@link EquipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ManufacturerMapper.class, EquipmentCategoryMapper.class, SupplierMapper.class})
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {

    @Mapping(source = "manufacturer.id", target = "manufacturerId")
    @Mapping(source = "equipmentCategory.id", target = "equipmentCategoryId")
    @Mapping(source = "suplier.id", target = "suplierId")
    EquipmentDTO toDto(Equipment equipment);

    @Mapping(target = "equipmentRequests", ignore = true)
    @Mapping(target = "removeEquipmentRequest", ignore = true)
    @Mapping(target = "equipmentServicings", ignore = true)
    @Mapping(target = "removeEquipmentServicing", ignore = true)
    @Mapping(target = "equipmentEmployees", ignore = true)
    @Mapping(target = "removeEquipmentEmployees", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "removeImages", ignore = true)
    @Mapping(source = "manufacturerId", target = "manufacturer")
    @Mapping(source = "equipmentCategoryId", target = "equipmentCategory")
    @Mapping(source = "suplierId", target = "suplier")
    Equipment toEntity(EquipmentDTO equipmentDTO);

    default Equipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }
}
