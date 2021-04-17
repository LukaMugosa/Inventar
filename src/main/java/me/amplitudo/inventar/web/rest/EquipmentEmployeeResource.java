package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.EquipmentEmployeeService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeDTO;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeCriteria;
import me.amplitudo.inventar.service.EquipmentEmployeeQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.EquipmentEmployee}.
 */
@RestController
@RequestMapping("/api")
public class EquipmentEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentEmployeeResource.class);

    private static final String ENTITY_NAME = "equipmentEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentEmployeeService equipmentEmployeeService;

    private final EquipmentEmployeeQueryService equipmentEmployeeQueryService;

    public EquipmentEmployeeResource(EquipmentEmployeeService equipmentEmployeeService, EquipmentEmployeeQueryService equipmentEmployeeQueryService) {
        this.equipmentEmployeeService = equipmentEmployeeService;
        this.equipmentEmployeeQueryService = equipmentEmployeeQueryService;
    }

    /**
     * {@code POST  /equipment-employees} : Create a new equipmentEmployee.
     *
     * @param equipmentEmployeeDTO the equipmentEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentEmployeeDTO, or with status {@code 400 (Bad Request)} if the equipmentEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-employees")
    public ResponseEntity<EquipmentEmployeeDTO> createEquipmentEmployee(@Valid @RequestBody EquipmentEmployeeDTO equipmentEmployeeDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentEmployee : {}", equipmentEmployeeDTO);
        if (equipmentEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentEmployeeDTO result = equipmentEmployeeService.save(equipmentEmployeeDTO);
        return ResponseEntity.created(new URI("/api/equipment-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-employees} : Updates an existing equipmentEmployee.
     *
     * @param equipmentEmployeeDTO the equipmentEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-employees")
    public ResponseEntity<EquipmentEmployeeDTO> updateEquipmentEmployee(@Valid @RequestBody EquipmentEmployeeDTO equipmentEmployeeDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentEmployee : {}", equipmentEmployeeDTO);
        if (equipmentEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentEmployeeDTO result = equipmentEmployeeService.save(equipmentEmployeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentEmployeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-employees} : get all the equipmentEmployees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentEmployees in body.
     */
    @GetMapping("/equipment-employees")
    public ResponseEntity<List<EquipmentEmployeeDTO>> getAllEquipmentEmployees(EquipmentEmployeeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EquipmentEmployees by criteria: {}", criteria);
        Page<EquipmentEmployeeDTO> page = equipmentEmployeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-employees/count} : count all the equipmentEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/equipment-employees/count")
    public ResponseEntity<Long> countEquipmentEmployees(EquipmentEmployeeCriteria criteria) {
        log.debug("REST request to count EquipmentEmployees by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipmentEmployeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipment-employees/:id} : get the "id" equipmentEmployee.
     *
     * @param id the id of the equipmentEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-employees/{id}")
    public ResponseEntity<EquipmentEmployeeDTO> getEquipmentEmployee(@PathVariable Long id) {
        log.debug("REST request to get EquipmentEmployee : {}", id);
        Optional<EquipmentEmployeeDTO> equipmentEmployeeDTO = equipmentEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentEmployeeDTO);
    }

    /**
     * {@code DELETE  /equipment-employees/:id} : delete the "id" equipmentEmployee.
     *
     * @param id the id of the equipmentEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-employees/{id}")
    public ResponseEntity<Void> deleteEquipmentEmployee(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentEmployee : {}", id);
        equipmentEmployeeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
