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

import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EmployeeRepository;
import me.amplitudo.inventar.service.dto.EmployeeCriteria;
import me.amplitudo.inventar.service.dto.EmployeeDTO;
import me.amplitudo.inventar.service.mapper.EmployeeMapper;

/**
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeDTO} or a {@link Page} of {@link EmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeQueryService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeMapper.toDto(employeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page)
            .map(employeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Employee_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Employee_.lastName));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Employee_.fullName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Employee_.phoneNumber));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Employee_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Employee_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Employee_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getEquipmentRequestsId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentRequestsId(),
                    root -> root.join(Employee_.equipmentRequests, JoinType.LEFT).get(EquipmentRequest_.id)));
            }
            if (criteria.getProblemReportsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProblemReportsId(),
                    root -> root.join(Employee_.problemReports, JoinType.LEFT).get(ProblemReport_.id)));
            }
            if (criteria.getEquipmentServiceId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentServiceId(),
                    root -> root.join(Employee_.equipmentServices, JoinType.LEFT).get(EquipmentService_.id)));
            }
            if (criteria.getEquipmentEmployeesId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentEmployeesId(),
                    root -> root.join(Employee_.equipmentEmployees, JoinType.LEFT).get(EquipmentEmployee_.id)));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildSpecification(criteria.getTenantId(),
                    root -> root.join(Employee_.tenant, JoinType.LEFT).get(Tenant_.id)));
            }
            if (criteria.getPositionId() != null) {
                specification = specification.and(buildSpecification(criteria.getPositionId(),
                    root -> root.join(Employee_.position, JoinType.LEFT).get(Position_.id)));
            }
            if (criteria.getCreatedNotificationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedNotificationsId(),
                    root -> root.join(Employee_.createdNotifications, JoinType.LEFT).get(Notification_.id)));
            }
            if (criteria.getReceivedNotificationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedNotificationsId(),
                    root -> root.join(Employee_.receivedNotifications, JoinType.LEFT).get(Notification_.id)));
            }
        }
        return specification;
    }
}
