package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentCategory;
import me.amplitudo.inventar.repository.EquipmentCategoryRepository;
import me.amplitudo.inventar.service.dto.EquipmentCategoryDTO;
import me.amplitudo.inventar.service.mapper.EquipmentCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentCategory}.
 */
@Service
@Transactional
public class EquipmentCategoryService {

    private final Logger log = LoggerFactory.getLogger(EquipmentCategoryService.class);

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    private final EquipmentCategoryMapper equipmentCategoryMapper;

    public EquipmentCategoryService(EquipmentCategoryRepository equipmentCategoryRepository, EquipmentCategoryMapper equipmentCategoryMapper) {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
        this.equipmentCategoryMapper = equipmentCategoryMapper;
    }

    /**
     * Save a equipmentCategory.
     *
     * @param equipmentCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentCategoryDTO save(EquipmentCategoryDTO equipmentCategoryDTO) {
        log.debug("Request to save EquipmentCategory : {}", equipmentCategoryDTO);
        EquipmentCategory equipmentCategory = equipmentCategoryMapper.toEntity(equipmentCategoryDTO);
        equipmentCategory = equipmentCategoryRepository.save(equipmentCategory);
        return equipmentCategoryMapper.toDto(equipmentCategory);
    }

    /**
     * Get all the equipmentCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentCategories");
        return equipmentCategoryRepository.findAll(pageable)
            .map(equipmentCategoryMapper::toDto);
    }


    /**
     * Get one equipmentCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentCategoryDTO> findOne(Long id) {
        log.debug("Request to get EquipmentCategory : {}", id);
        return equipmentCategoryRepository.findById(id)
            .map(equipmentCategoryMapper::toDto);
    }

    /**
     * Delete the equipmentCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentCategory : {}", id);
        equipmentCategoryRepository.deleteById(id);
    }
}
