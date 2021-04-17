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

import me.amplitudo.inventar.domain.ProblemReport;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.ProblemReportRepository;
import me.amplitudo.inventar.service.dto.ProblemReportCriteria;
import me.amplitudo.inventar.service.dto.ProblemReportDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportMapper;

/**
 * Service for executing complex queries for {@link ProblemReport} entities in the database.
 * The main input is a {@link ProblemReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProblemReportDTO} or a {@link Page} of {@link ProblemReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProblemReportQueryService extends QueryService<ProblemReport> {

    private final Logger log = LoggerFactory.getLogger(ProblemReportQueryService.class);

    private final ProblemReportRepository problemReportRepository;

    private final ProblemReportMapper problemReportMapper;

    public ProblemReportQueryService(ProblemReportRepository problemReportRepository, ProblemReportMapper problemReportMapper) {
        this.problemReportRepository = problemReportRepository;
        this.problemReportMapper = problemReportMapper;
    }

    /**
     * Return a {@link List} of {@link ProblemReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProblemReportDTO> findByCriteria(ProblemReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProblemReport> specification = createSpecification(criteria);
        return problemReportMapper.toDto(problemReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProblemReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemReportDTO> findByCriteria(ProblemReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProblemReport> specification = createSpecification(criteria);
        return problemReportRepository.findAll(specification, page)
            .map(problemReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProblemReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProblemReport> specification = createSpecification(criteria);
        return problemReportRepository.count(specification);
    }

    /**
     * Function to convert {@link ProblemReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProblemReport> createSpecification(ProblemReportCriteria criteria) {
        Specification<ProblemReport> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProblemReport_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ProblemReport_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProblemReport_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedAt(), ProblemReport_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedAt(), ProblemReport_.updatedAt));
            }
            if (criteria.getProblemReportCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getProblemReportCategoryId(),
                    root -> root.join(ProblemReport_.problemReportCategory, JoinType.LEFT).get(ProblemReportCategory_.id)));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(ProblemReport_.employee, JoinType.LEFT).get(Employee_.id)));
            }
        }
        return specification;
    }
}
