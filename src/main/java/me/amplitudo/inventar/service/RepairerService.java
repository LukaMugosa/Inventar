package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.Repairer;
import me.amplitudo.inventar.repository.RepairerRepository;
import me.amplitudo.inventar.service.dto.RepairerDTO;
import me.amplitudo.inventar.service.mapper.RepairerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Repairer}.
 */
@Service
@Transactional
public class RepairerService {

    private final Logger log = LoggerFactory.getLogger(RepairerService.class);

    private final RepairerRepository repairerRepository;

    private final RepairerMapper repairerMapper;

    public RepairerService(RepairerRepository repairerRepository, RepairerMapper repairerMapper) {
        this.repairerRepository = repairerRepository;
        this.repairerMapper = repairerMapper;
    }

    /**
     * Save a repairer.
     *
     * @param repairerDTO the entity to save.
     * @return the persisted entity.
     */
    public RepairerDTO save(RepairerDTO repairerDTO) {
        log.debug("Request to save Repairer : {}", repairerDTO);
        Repairer repairer = repairerMapper.toEntity(repairerDTO);
        repairer = repairerRepository.save(repairer);
        return repairerMapper.toDto(repairer);
    }

    /**
     * Get all the repairers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repairers");
        return repairerRepository.findAll(pageable)
            .map(repairerMapper::toDto);
    }


    /**
     * Get one repairer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RepairerDTO> findOne(Long id) {
        log.debug("Request to get Repairer : {}", id);
        return repairerRepository.findById(id)
            .map(repairerMapper::toDto);
    }

    /**
     * Delete the repairer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Repairer : {}", id);
        repairerRepository.deleteById(id);
    }
}
