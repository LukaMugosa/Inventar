package me.amplitudo.inventar.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import me.amplitudo.inventar.domain.EquipmentImage;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentImageRepository;
import me.amplitudo.inventar.service.dto.EquipmentImageCriteria;
import me.amplitudo.inventar.service.dto.EquipmentImageDTO;
import me.amplitudo.inventar.service.mapper.EquipmentImageMapper;

/**
 * Service for executing complex queries for {@link EquipmentImage} entities in the database.
 * The main input is a {@link EquipmentImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentImageDTO} or a {@link Page} of {@link EquipmentImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentImageQueryService extends QueryService<EquipmentImage> {

    private final Logger log = LoggerFactory.getLogger(EquipmentImageQueryService.class);

    private final EquipmentImageRepository equipmentImageRepository;

    private final EquipmentImageMapper equipmentImageMapper;

    public EquipmentImageQueryService(EquipmentImageRepository equipmentImageRepository, EquipmentImageMapper equipmentImageMapper) {
        this.equipmentImageRepository = equipmentImageRepository;
        this.equipmentImageMapper = equipmentImageMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentImageDTO> findByCriteria(EquipmentImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentImage> specification = createSpecification(criteria);
        return equipmentImageMapper.toDto(equipmentImageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentImageDTO> findByCriteria(EquipmentImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentImage> specification = createSpecification(criteria);
        return equipmentImageRepository.findAll(specification, page)
            .map(equipmentImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentImage> specification = createSpecification(criteria);
        return equipmentImageRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentImage> createSpecification(EquipmentImageCriteria criteria) {
        Specification<EquipmentImage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentImage_.id));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentId(),
                    root -> root.join(EquipmentImage_.equipment, JoinType.LEFT).get(Equipment_.id)));
            }
        }
        return specification;
    }
}
