package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.EquipmentCategoryService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.EquipmentCategoryDTO;
import me.amplitudo.inventar.service.dto.EquipmentCategoryCriteria;
import me.amplitudo.inventar.service.EquipmentCategoryQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.EquipmentCategory}.
 */
@RestController
@RequestMapping("/api")
public class EquipmentCategoryResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentCategoryResource.class);

    private static final String ENTITY_NAME = "equipmentCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentCategoryService equipmentCategoryService;

    private final EquipmentCategoryQueryService equipmentCategoryQueryService;

    public EquipmentCategoryResource(EquipmentCategoryService equipmentCategoryService, EquipmentCategoryQueryService equipmentCategoryQueryService) {
        this.equipmentCategoryService = equipmentCategoryService;
        this.equipmentCategoryQueryService = equipmentCategoryQueryService;
    }

    /**
     * {@code POST  /equipment-categories} : Create a new equipmentCategory.
     *
     * @param equipmentCategoryDTO the equipmentCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentCategoryDTO, or with status {@code 400 (Bad Request)} if the equipmentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-categories")
    public ResponseEntity<EquipmentCategoryDTO> createEquipmentCategory(@Valid @RequestBody EquipmentCategoryDTO equipmentCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentCategory : {}", equipmentCategoryDTO);
        if (equipmentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentCategoryDTO result = equipmentCategoryService.save(equipmentCategoryDTO);
        return ResponseEntity.created(new URI("/api/equipment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-categories} : Updates an existing equipmentCategory.
     *
     * @param equipmentCategoryDTO the equipmentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-categories")
    public ResponseEntity<EquipmentCategoryDTO> updateEquipmentCategory(@Valid @RequestBody EquipmentCategoryDTO equipmentCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentCategory : {}", equipmentCategoryDTO);
        if (equipmentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentCategoryDTO result = equipmentCategoryService.save(equipmentCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-categories} : get all the equipmentCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentCategories in body.
     */
    @GetMapping("/equipment-categories")
    public ResponseEntity<List<EquipmentCategoryDTO>> getAllEquipmentCategories(EquipmentCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EquipmentCategories by criteria: {}", criteria);
        Page<EquipmentCategoryDTO> page = equipmentCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-categories/count} : count all the equipmentCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/equipment-categories/count")
    public ResponseEntity<Long> countEquipmentCategories(EquipmentCategoryCriteria criteria) {
        log.debug("REST request to count EquipmentCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipmentCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipment-categories/:id} : get the "id" equipmentCategory.
     *
     * @param id the id of the equipmentCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-categories/{id}")
    public ResponseEntity<EquipmentCategoryDTO> getEquipmentCategory(@PathVariable Long id) {
        log.debug("REST request to get EquipmentCategory : {}", id);
        Optional<EquipmentCategoryDTO> equipmentCategoryDTO = equipmentCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentCategoryDTO);
    }

    /**
     * {@code DELETE  /equipment-categories/:id} : delete the "id" equipmentCategory.
     *
     * @param id the id of the equipmentCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-categories/{id}")
    public ResponseEntity<Void> deleteEquipmentCategory(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentCategory : {}", id);
        equipmentCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
