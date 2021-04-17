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

import me.amplitudo.inventar.domain.Repairer;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.RepairerRepository;
import me.amplitudo.inventar.service.dto.RepairerCriteria;
import me.amplitudo.inventar.service.dto.RepairerDTO;
import me.amplitudo.inventar.service.mapper.RepairerMapper;

/**
 * Service for executing complex queries for {@link Repairer} entities in the database.
 * The main input is a {@link RepairerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RepairerDTO} or a {@link Page} of {@link RepairerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairerQueryService extends QueryService<Repairer> {

    private final Logger log = LoggerFactory.getLogger(RepairerQueryService.class);

    private final RepairerRepository repairerRepository;

    private final RepairerMapper repairerMapper;

    public RepairerQueryService(RepairerRepository repairerRepository, RepairerMapper repairerMapper) {
        this.repairerRepository = repairerRepository;
        this.repairerMapper = repairerMapper;
    }

    /**
     * Return a {@link List} of {@link RepairerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RepairerDTO> findByCriteria(RepairerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Repairer> specification = createSpecification(criteria);
        return repairerMapper.toDto(repairerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RepairerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairerDTO> findByCriteria(RepairerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Repairer> specification = createSpecification(criteria);
        return repairerRepository.findAll(specification, page)
            .map(repairerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RepairerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Repairer> specification = createSpecification(criteria);
        return repairerRepository.count(specification);
    }

    /**
     * Function to convert {@link RepairerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Repairer> createSpecification(RepairerCriteria criteria) {
        Specification<Repairer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Repairer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Repairer_.name));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Repairer_.phoneNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Repairer_.email));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Repairer_.address));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Repairer_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Repairer_.updatedAt));
            }
            if (criteria.getEquipmentServiceId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentServiceId(),
                    root -> root.join(Repairer_.equipmentServices, JoinType.LEFT).get(EquipmentService_.id)));
            }
        }
        return specification;
    }
}
