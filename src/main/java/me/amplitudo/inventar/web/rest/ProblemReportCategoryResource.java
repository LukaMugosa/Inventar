package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.ProblemReportCategoryService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryDTO;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryCriteria;
import me.amplitudo.inventar.service.ProblemReportCategoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link me.amplitudo.inventar.domain.ProblemReportCategory}.
 */
@RestController
@RequestMapping("/api")
public class ProblemReportCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProblemReportCategoryResource.class);

    private static final String ENTITY_NAME = "problemReportCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProblemReportCategoryService problemReportCategoryService;

    private final ProblemReportCategoryQueryService problemReportCategoryQueryService;

    public ProblemReportCategoryResource(ProblemReportCategoryService problemReportCategoryService, ProblemReportCategoryQueryService problemReportCategoryQueryService) {
        this.problemReportCategoryService = problemReportCategoryService;
        this.problemReportCategoryQueryService = problemReportCategoryQueryService;
    }

    /**
     * {@code POST  /problem-report-categories} : Create a new problemReportCategory.
     *
     * @param problemReportCategoryDTO the problemReportCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new problemReportCategoryDTO, or with status {@code 400 (Bad Request)} if the problemReportCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/problem-report-categories")
    public ResponseEntity<ProblemReportCategoryDTO> createProblemReportCategory(@Valid @RequestBody ProblemReportCategoryDTO problemReportCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save ProblemReportCategory : {}", problemReportCategoryDTO);
        if (problemReportCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new problemReportCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProblemReportCategoryDTO result = problemReportCategoryService.save(problemReportCategoryDTO);
        return ResponseEntity.created(new URI("/api/problem-report-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /problem-report-categories} : Updates an existing problemReportCategory.
     *
     * @param problemReportCategoryDTO the problemReportCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemReportCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the problemReportCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the problemReportCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/problem-report-categories")
    public ResponseEntity<ProblemReportCategoryDTO> updateProblemReportCategory(@Valid @RequestBody ProblemReportCategoryDTO problemReportCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update ProblemReportCategory : {}", problemReportCategoryDTO);
        if (problemReportCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProblemReportCategoryDTO result = problemReportCategoryService.save(problemReportCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemReportCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /problem-report-categories} : get all the problemReportCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of problemReportCategories in body.
     */
    @GetMapping("/problem-report-categories")
    public ResponseEntity<List<ProblemReportCategoryDTO>> getAllProblemReportCategories(ProblemReportCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProblemReportCategories by criteria: {}", criteria);
        Page<ProblemReportCategoryDTO> page = problemReportCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /problem-report-categories/count} : count all the problemReportCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/problem-report-categories/count")
    public ResponseEntity<Long> countProblemReportCategories(ProblemReportCategoryCriteria criteria) {
        log.debug("REST request to count ProblemReportCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(problemReportCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /problem-report-categories/:id} : get the "id" problemReportCategory.
     *
     * @param id the id of the problemReportCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the problemReportCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/problem-report-categories/{id}")
    public ResponseEntity<ProblemReportCategoryDTO> getProblemReportCategory(@PathVariable Long id) {
        log.debug("REST request to get ProblemReportCategory : {}", id);
        Optional<ProblemReportCategoryDTO> problemReportCategoryDTO = problemReportCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(problemReportCategoryDTO);
    }

    /**
     * {@code DELETE  /problem-report-categories/:id} : delete the "id" problemReportCategory.
     *
     * @param id the id of the problemReportCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/problem-report-categories/{id}")
    public ResponseEntity<Void> deleteProblemReportCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProblemReportCategory : {}", id);
        problemReportCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
