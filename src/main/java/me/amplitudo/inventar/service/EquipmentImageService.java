package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentImage;
import me.amplitudo.inventar.repository.EquipmentImageRepository;
import me.amplitudo.inventar.service.dto.EquipmentImageDTO;
import me.amplitudo.inventar.service.mapper.EquipmentImageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentImage}.
 */
@Service
@Transactional
public class EquipmentImageService {

    private final Logger log = LoggerFactory.getLogger(EquipmentImageService.class);

    private final EquipmentImageRepository equipmentImageRepository;

    private final EquipmentImageMapper equipmentImageMapper;

    public EquipmentImageService(EquipmentImageRepository equipmentImageRepository, EquipmentImageMapper equipmentImageMapper) {
        this.equipmentImageRepository = equipmentImageRepository;
        this.equipmentImageMapper = equipmentImageMapper;
    }

    /**
     * Save a equipmentImage.
     *
     * @param equipmentImageDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentImageDTO save(EquipmentImageDTO equipmentImageDTO) {
        log.debug("Request to save EquipmentImage : {}", equipmentImageDTO);
        EquipmentImage equipmentImage = equipmentImageMapper.toEntity(equipmentImageDTO);
        equipmentImage = equipmentImageRepository.save(equipmentImage);
        return equipmentImageMapper.toDto(equipmentImage);
    }

    /**
     * Get all the equipmentImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentImages");
        return equipmentImageRepository.findAll(pageable)
            .map(equipmentImageMapper::toDto);
    }


    /**
     * Get one equipmentImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentImageDTO> findOne(Long id) {
        log.debug("Request to get EquipmentImage : {}", id);
        return equipmentImageRepository.findById(id)
            .map(equipmentImageMapper::toDto);
    }

    /**
     * Delete the equipmentImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentImage : {}", id);
        equipmentImageRepository.deleteById(id);
    }
}
