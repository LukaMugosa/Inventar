package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.ProblemReport;
import me.amplitudo.inventar.domain.ProblemReportCategory;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.repository.ProblemReportRepository;
import me.amplitudo.inventar.service.ProblemReportService;
import me.amplitudo.inventar.service.dto.ProblemReportDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportMapper;
import me.amplitudo.inventar.service.dto.ProblemReportCriteria;
import me.amplitudo.inventar.service.ProblemReportQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProblemReportResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProblemReportResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_AT = "BBBBBBBBBB";

    @Autowired
    private ProblemReportRepository problemReportRepository;

    @Autowired
    private ProblemReportMapper problemReportMapper;

    @Autowired
    private ProblemReportService problemReportService;

    @Autowired
    private ProblemReportQueryService problemReportQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProblemReportMockMvc;

    private ProblemReport problemReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProblemReport createEntity(EntityManager em) {
        ProblemReport problemReport = new ProblemReport()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return problemReport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProblemReport createUpdatedEntity(EntityManager em) {
        ProblemReport problemReport = new ProblemReport()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return problemReport;
    }

    @BeforeEach
    public void initTest() {
        problemReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createProblemReport() throws Exception {
        int databaseSizeBeforeCreate = problemReportRepository.findAll().size();
        // Create the ProblemReport
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(problemReport);
        restProblemReportMockMvc.perform(post("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isCreated());

        // Validate the ProblemReport in the database
        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeCreate + 1);
        ProblemReport testProblemReport = problemReportList.get(problemReportList.size() - 1);
        assertThat(testProblemReport.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblemReport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProblemReport.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProblemReport.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProblemReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProblemReport.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createProblemReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = problemReportRepository.findAll().size();

        // Create the ProblemReport with an existing ID
        problemReport.setId(1L);
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(problemReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemReportMockMvc.perform(post("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProblemReport in the database
        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemReportRepository.findAll().size();
        // set the field null
        problemReport.setTitle(null);

        // Create the ProblemReport, which fails.
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(problemReport);


        restProblemReportMockMvc.perform(post("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isBadRequest());

        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemReportRepository.findAll().size();
        // set the field null
        problemReport.setDescription(null);

        // Create the ProblemReport, which fails.
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(problemReport);


        restProblemReportMockMvc.perform(post("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isBadRequest());

        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProblemReports() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList
        restProblemReportMockMvc.perform(get("/api/problem-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT)));
    }
    
    @Test
    @Transactional
    public void getProblemReport() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get the problemReport
        restProblemReportMockMvc.perform(get("/api/problem-reports/{id}", problemReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(problemReport.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT));
    }


    @Test
    @Transactional
    public void getProblemReportsByIdFiltering() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        Long id = problemReport.getId();

        defaultProblemReportShouldBeFound("id.equals=" + id);
        defaultProblemReportShouldNotBeFound("id.notEquals=" + id);

        defaultProblemReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProblemReportShouldNotBeFound("id.greaterThan=" + id);

        defaultProblemReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProblemReportShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProblemReportsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title equals to DEFAULT_TITLE
        defaultProblemReportShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the problemReportList where title equals to UPDATED_TITLE
        defaultProblemReportShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title not equals to DEFAULT_TITLE
        defaultProblemReportShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the problemReportList where title not equals to UPDATED_TITLE
        defaultProblemReportShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProblemReportShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the problemReportList where title equals to UPDATED_TITLE
        defaultProblemReportShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title is not null
        defaultProblemReportShouldBeFound("title.specified=true");

        // Get all the problemReportList where title is null
        defaultProblemReportShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllProblemReportsByTitleContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title contains DEFAULT_TITLE
        defaultProblemReportShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the problemReportList where title contains UPDATED_TITLE
        defaultProblemReportShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where title does not contain DEFAULT_TITLE
        defaultProblemReportShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the problemReportList where title does not contain UPDATED_TITLE
        defaultProblemReportShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllProblemReportsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description equals to DEFAULT_DESCRIPTION
        defaultProblemReportShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the problemReportList where description equals to UPDATED_DESCRIPTION
        defaultProblemReportShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description not equals to DEFAULT_DESCRIPTION
        defaultProblemReportShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the problemReportList where description not equals to UPDATED_DESCRIPTION
        defaultProblemReportShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProblemReportShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the problemReportList where description equals to UPDATED_DESCRIPTION
        defaultProblemReportShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description is not null
        defaultProblemReportShouldBeFound("description.specified=true");

        // Get all the problemReportList where description is null
        defaultProblemReportShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProblemReportsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description contains DEFAULT_DESCRIPTION
        defaultProblemReportShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the problemReportList where description contains UPDATED_DESCRIPTION
        defaultProblemReportShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where description does not contain DEFAULT_DESCRIPTION
        defaultProblemReportShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the problemReportList where description does not contain UPDATED_DESCRIPTION
        defaultProblemReportShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt equals to DEFAULT_CREATED_AT
        defaultProblemReportShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the problemReportList where createdAt equals to UPDATED_CREATED_AT
        defaultProblemReportShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt not equals to DEFAULT_CREATED_AT
        defaultProblemReportShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the problemReportList where createdAt not equals to UPDATED_CREATED_AT
        defaultProblemReportShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultProblemReportShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the problemReportList where createdAt equals to UPDATED_CREATED_AT
        defaultProblemReportShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt is not null
        defaultProblemReportShouldBeFound("createdAt.specified=true");

        // Get all the problemReportList where createdAt is null
        defaultProblemReportShouldNotBeFound("createdAt.specified=false");
    }
                @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt contains DEFAULT_CREATED_AT
        defaultProblemReportShouldBeFound("createdAt.contains=" + DEFAULT_CREATED_AT);

        // Get all the problemReportList where createdAt contains UPDATED_CREATED_AT
        defaultProblemReportShouldNotBeFound("createdAt.contains=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByCreatedAtNotContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where createdAt does not contain DEFAULT_CREATED_AT
        defaultProblemReportShouldNotBeFound("createdAt.doesNotContain=" + DEFAULT_CREATED_AT);

        // Get all the problemReportList where createdAt does not contain UPDATED_CREATED_AT
        defaultProblemReportShouldBeFound("createdAt.doesNotContain=" + UPDATED_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultProblemReportShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProblemReportShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultProblemReportShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultProblemReportShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultProblemReportShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the problemReportList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProblemReportShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt is not null
        defaultProblemReportShouldBeFound("updatedAt.specified=true");

        // Get all the problemReportList where updatedAt is null
        defaultProblemReportShouldNotBeFound("updatedAt.specified=false");
    }
                @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt contains DEFAULT_UPDATED_AT
        defaultProblemReportShouldBeFound("updatedAt.contains=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportList where updatedAt contains UPDATED_UPDATED_AT
        defaultProblemReportShouldNotBeFound("updatedAt.contains=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportsByUpdatedAtNotContainsSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        // Get all the problemReportList where updatedAt does not contain DEFAULT_UPDATED_AT
        defaultProblemReportShouldNotBeFound("updatedAt.doesNotContain=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportList where updatedAt does not contain UPDATED_UPDATED_AT
        defaultProblemReportShouldBeFound("updatedAt.doesNotContain=" + UPDATED_UPDATED_AT);
    }


    @Test
    @Transactional
    public void getAllProblemReportsByProblemReportCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);
        ProblemReportCategory problemReportCategory = ProblemReportCategoryResourceIT.createEntity(em);
        em.persist(problemReportCategory);
        em.flush();
        problemReport.setProblemReportCategory(problemReportCategory);
        problemReportRepository.saveAndFlush(problemReport);
        Long problemReportCategoryId = problemReportCategory.getId();

        // Get all the problemReportList where problemReportCategory equals to problemReportCategoryId
        defaultProblemReportShouldBeFound("problemReportCategoryId.equals=" + problemReportCategoryId);

        // Get all the problemReportList where problemReportCategory equals to problemReportCategoryId + 1
        defaultProblemReportShouldNotBeFound("problemReportCategoryId.equals=" + (problemReportCategoryId + 1));
    }


    @Test
    @Transactional
    public void getAllProblemReportsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        problemReport.setEmployee(employee);
        problemReportRepository.saveAndFlush(problemReport);
        Long employeeId = employee.getId();

        // Get all the problemReportList where employee equals to employeeId
        defaultProblemReportShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the problemReportList where employee equals to employeeId + 1
        defaultProblemReportShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProblemReportShouldBeFound(String filter) throws Exception {
        restProblemReportMockMvc.perform(get("/api/problem-reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT)));

        // Check, that the count call also returns 1
        restProblemReportMockMvc.perform(get("/api/problem-reports/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProblemReportShouldNotBeFound(String filter) throws Exception {
        restProblemReportMockMvc.perform(get("/api/problem-reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProblemReportMockMvc.perform(get("/api/problem-reports/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProblemReport() throws Exception {
        // Get the problemReport
        restProblemReportMockMvc.perform(get("/api/problem-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProblemReport() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        int databaseSizeBeforeUpdate = problemReportRepository.findAll().size();

        // Update the problemReport
        ProblemReport updatedProblemReport = problemReportRepository.findById(problemReport.getId()).get();
        // Disconnect from session so that the updates on updatedProblemReport are not directly saved in db
        em.detach(updatedProblemReport);
        updatedProblemReport
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(updatedProblemReport);

        restProblemReportMockMvc.perform(put("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isOk());

        // Validate the ProblemReport in the database
        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeUpdate);
        ProblemReport testProblemReport = problemReportList.get(problemReportList.size() - 1);
        assertThat(testProblemReport.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblemReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProblemReport.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProblemReport.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProblemReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProblemReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingProblemReport() throws Exception {
        int databaseSizeBeforeUpdate = problemReportRepository.findAll().size();

        // Create the ProblemReport
        ProblemReportDTO problemReportDTO = problemReportMapper.toDto(problemReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemReportMockMvc.perform(put("/api/problem-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProblemReport in the database
        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProblemReport() throws Exception {
        // Initialize the database
        problemReportRepository.saveAndFlush(problemReport);

        int databaseSizeBeforeDelete = problemReportRepository.findAll().size();

        // Delete the problemReport
        restProblemReportMockMvc.perform(delete("/api/problem-reports/{id}", problemReport.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProblemReport> problemReportList = problemReportRepository.findAll();
        assertThat(problemReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
