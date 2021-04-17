package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.SectorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sector} and its DTO {@link SectorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SectorMapper extends EntityMapper<SectorDTO, Sector> {


    @Mapping(target = "positions", ignore = true)
    @Mapping(target = "removePositions", ignore = true)
    Sector toEntity(SectorDTO sectorDTO);

    default Sector fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sector sector = new Sector();
        sector.setId(id);
        return sector;
    }
}
