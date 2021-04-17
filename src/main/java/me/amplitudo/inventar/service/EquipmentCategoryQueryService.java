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

import me.amplitudo.inventar.domain.EquipmentCategory;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentCategoryRepository;
import me.amplitudo.inventar.service.dto.EquipmentCategoryCriteria;
import me.amplitudo.inventar.service.dto.EquipmentCategoryDTO;
import me.amplitudo.inventar.service.mapper.EquipmentCategoryMapper;

/**
 * Service for executing complex queries for {@link EquipmentCategory} entities in the database.
 * The main input is a {@link EquipmentCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentCategoryDTO} or a {@link Page} of {@link EquipmentCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentCategoryQueryService extends QueryService<EquipmentCategory> {

    private final Logger log = LoggerFactory.getLogger(EquipmentCategoryQueryService.class);

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    private final EquipmentCategoryMapper equipmentCategoryMapper;

    public EquipmentCategoryQueryService(EquipmentCategoryRepository equipmentCategoryRepository, EquipmentCategoryMapper equipmentCategoryMapper) {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
        this.equipmentCategoryMapper = equipmentCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentCategoryDTO> findByCriteria(EquipmentCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentCategory> specification = createSpecification(criteria);
        return equipmentCategoryMapper.toDto(equipmentCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentCategoryDTO> findByCriteria(EquipmentCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentCategory> specification = createSpecification(criteria);
        return equipmentCategoryRepository.findAll(specification, page)
            .map(equipmentCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentCategory> specification = createSpecification(criteria);
        return equipmentCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentCategory> createSpecification(EquipmentCategoryCriteria criteria) {
        Specification<EquipmentCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EquipmentCategory_.name));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EquipmentCategory_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EquipmentCategory_.updatedAt));
            }
            if (criteria.getEquipmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentsId(),
                    root -> root.join(EquipmentCategory_.equipments, JoinType.LEFT).get(Equipment_.id)));
            }
        }
        return specification;
    }
}
