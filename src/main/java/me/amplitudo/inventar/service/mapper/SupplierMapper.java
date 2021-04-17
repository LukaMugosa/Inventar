package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.SupplierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {


    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "removeEquipments", ignore = true)
    Supplier toEntity(SupplierDTO supplierDTO);

    default Supplier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplier;
    }
}
