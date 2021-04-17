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

import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentRepository;
import me.amplitudo.inventar.service.dto.EquipmentCriteria;
import me.amplitudo.inventar.service.dto.EquipmentDTO;
import me.amplitudo.inventar.service.mapper.EquipmentMapper;

/**
 * Service for executing complex queries for {@link Equipment} entities in the database.
 * The main input is a {@link EquipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentDTO} or a {@link Page} of {@link EquipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentQueryService extends QueryService<Equipment> {

    private final Logger log = LoggerFactory.getLogger(EquipmentQueryService.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentQueryService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findByCriteria(EquipmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentMapper.toDto(equipmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findByCriteria(EquipmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.findAll(specification, page)
            .map(equipmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Equipment> createSpecification(EquipmentCriteria criteria) {
        Specification<Equipment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Equipment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Equipment_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Equipment_.description));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Equipment_.stock));
            }
            if (criteria.getPricePerUnit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPricePerUnit(), Equipment_.pricePerUnit));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Equipment_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Equipment_.updatedAt));
            }
            if (criteria.getEquipmentRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentRequestId(),
                    root -> root.join(Equipment_.equipmentRequests, JoinType.LEFT).get(EquipmentRequest_.id)));
            }
            if (criteria.getEquipmentServiceId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentServiceId(),
                    root -> root.join(Equipment_.equipmentServices, JoinType.LEFT).get(EquipmentService_.id)));
            }
            if (criteria.getEquipmentEmployeesId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentEmployeesId(),
                    root -> root.join(Equipment_.equipmentEmployees, JoinType.LEFT).get(EquipmentEmployee_.id)));
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(buildSpecification(criteria.getImagesId(),
                    root -> root.join(Equipment_.images, JoinType.LEFT).get(EquipmentImage_.id)));
            }
            if (criteria.getManufacturerId() != null) {
                specification = specification.and(buildSpecification(criteria.getManufacturerId(),
                    root -> root.join(Equipment_.manufacturer, JoinType.LEFT).get(Manufacturer_.id)));
            }
            if (criteria.getEquipmentCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentCategoryId(),
                    root -> root.join(Equipment_.equipmentCategory, JoinType.LEFT).get(EquipmentCategory_.id)));
            }
            if (criteria.getSuplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSuplierId(),
                    root -> root.join(Equipment_.suplier, JoinType.LEFT).get(Supplier_.id)));
            }
        }
        return specification;
    }
}
