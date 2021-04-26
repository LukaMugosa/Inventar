package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentServicingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentServicing} and its DTO {@link EquipmentServicingDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, EquipmentMapper.class, RepairerMapper.class})
public interface EquipmentServicingMapper extends EntityMapper<EquipmentServicingDTO, EquipmentServicing> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "repairer.id", target = "repairerId")
    EquipmentServicingDTO toDto(EquipmentServicing equipmentServicing);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "repairerId", target = "repairer")
    EquipmentServicing toEntity(EquipmentServicingDTO equipmentServicingDTO);

    default EquipmentServicing fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentServicing equipmentServicing = new EquipmentServicing();
        equipmentServicing.setId(id);
        return equipmentServicing;
    }
}
