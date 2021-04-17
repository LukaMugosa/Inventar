package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.ProblemReportService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.ProblemReportDTO;
import me.amplitudo.inventar.service.dto.ProblemReportCriteria;
import me.amplitudo.inventar.service.ProblemReportQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.ProblemReport}.
 */
@RestController
@RequestMapping("/api")
public class ProblemReportResource {

    private final Logger log = LoggerFactory.getLogger(ProblemReportResource.class);

    private static final String ENTITY_NAME = "problemReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProblemReportService problemReportService;

    private final ProblemReportQueryService problemReportQueryService;

    public ProblemReportResource(ProblemReportService problemReportService, ProblemReportQueryService problemReportQueryService) {
        this.problemReportService = problemReportService;
        this.problemReportQueryService = problemReportQueryService;
    }

    /**
     * {@code POST  /problem-reports} : Create a new problemReport.
     *
     * @param problemReportDTO the problemReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new problemReportDTO, or with status {@code 400 (Bad Request)} if the problemReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/problem-reports")
    public ResponseEntity<ProblemReportDTO> createProblemReport(@Valid @RequestBody ProblemReportDTO problemReportDTO) throws URISyntaxException {
        log.debug("REST request to save ProblemReport : {}", problemReportDTO);
        if (problemReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new problemReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProblemReportDTO result = problemReportService.save(problemReportDTO);
        return ResponseEntity.created(new URI("/api/problem-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /problem-reports} : Updates an existing problemReport.
     *
     * @param problemReportDTO the problemReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemReportDTO,
     * or with status {@code 400 (Bad Request)} if the problemReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the problemReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/problem-reports")
    public ResponseEntity<ProblemReportDTO> updateProblemReport(@Valid @RequestBody ProblemReportDTO problemReportDTO) throws URISyntaxException {
        log.debug("REST request to update ProblemReport : {}", problemReportDTO);
        if (problemReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProblemReportDTO result = problemReportService.save(problemReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /problem-reports} : get all the problemReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of problemReports in body.
     */
    @GetMapping("/problem-reports")
    public ResponseEntity<List<ProblemReportDTO>> getAllProblemReports(ProblemReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProblemReports by criteria: {}", criteria);
        Page<ProblemReportDTO> page = problemReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /problem-reports/count} : count all the problemReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/problem-reports/count")
    public ResponseEntity<Long> countProblemReports(ProblemReportCriteria criteria) {
        log.debug("REST request to count ProblemReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(problemReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /problem-reports/:id} : get the "id" problemReport.
     *
     * @param id the id of the problemReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the problemReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/problem-reports/{id}")
    public ResponseEntity<ProblemReportDTO> getProblemReport(@PathVariable Long id) {
        log.debug("REST request to get ProblemReport : {}", id);
        Optional<ProblemReportDTO> problemReportDTO = problemReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(problemReportDTO);
    }

    /**
     * {@code DELETE  /problem-reports/:id} : delete the "id" problemReport.
     *
     * @param id the id of the problemReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/problem-reports/{id}")
    public ResponseEntity<Void> deleteProblemReport(@PathVariable Long id) {
        log.debug("REST request to delete ProblemReport : {}", id);
        problemReportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
