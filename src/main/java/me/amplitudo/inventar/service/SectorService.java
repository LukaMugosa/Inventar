package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.Sector;
import me.amplitudo.inventar.repository.SectorRepository;
import me.amplitudo.inventar.service.dto.SectorDTO;
import me.amplitudo.inventar.service.mapper.SectorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Sector}.
 */
@Service
@Transactional
public class SectorService {

    private final Logger log = LoggerFactory.getLogger(SectorService.class);

    private final SectorRepository sectorRepository;

    private final SectorMapper sectorMapper;

    public SectorService(SectorRepository sectorRepository, SectorMapper sectorMapper) {
        this.sectorRepository = sectorRepository;
        this.sectorMapper = sectorMapper;
    }

    /**
     * Save a sector.
     *
     * @param sectorDTO the entity to save.
     * @return the persisted entity.
     */
    public SectorDTO save(SectorDTO sectorDTO) {
        log.debug("Request to save Sector : {}", sectorDTO);
        Sector sector = sectorMapper.toEntity(sectorDTO);
        sector = sectorRepository.save(sector);
        return sectorMapper.toDto(sector);
    }

    /**
     * Get all the sectors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SectorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sectors");
        return sectorRepository.findAll(pageable)
            .map(sectorMapper::toDto);
    }


    /**
     * Get one sector by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SectorDTO> findOne(Long id) {
        log.debug("Request to get Sector : {}", id);
        return sectorRepository.findById(id)
            .map(sectorMapper::toDto);
    }

    /**
     * Delete the sector by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sector : {}", id);
        sectorRepository.deleteById(id);
    }
}
