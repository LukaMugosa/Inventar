package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentEmployee;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.repository.EquipmentEmployeeRepository;
import me.amplitudo.inventar.service.EquipmentEmployeeService;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeDTO;
import me.amplitudo.inventar.service.mapper.EquipmentEmployeeMapper;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeCriteria;
import me.amplitudo.inventar.service.EquipmentEmployeeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.amplitudo.inventar.domain.enumeration.EquipmentStatus;
/**
 * Integration tests for the {@link EquipmentEmployeeResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentEmployeeResourceIT {

    private static final Instant DEFAULT_DATE_OF_RENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_RENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final EquipmentStatus DEFAULT_STATUS = EquipmentStatus.IN_USE;
    private static final EquipmentStatus UPDATED_STATUS = EquipmentStatus.ON_REPAIRMENT;

    @Autowired
    private EquipmentEmployeeRepository equipmentEmployeeRepository;

    @Autowired
    private EquipmentEmployeeMapper equipmentEmployeeMapper;

    @Autowired
    private EquipmentEmployeeService equipmentEmployeeService;

    @Autowired
    private EquipmentEmployeeQueryService equipmentEmployeeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentEmployeeMockMvc;

    private EquipmentEmployee equipmentEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentEmployee createEntity(EntityManager em) {
        EquipmentEmployee equipmentEmployee = new EquipmentEmployee()
            .dateOfRent(DEFAULT_DATE_OF_RENT)
            .active(DEFAULT_ACTIVE)
            .status(DEFAULT_STATUS);
        return equipmentEmployee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentEmployee createUpdatedEntity(EntityManager em) {
        EquipmentEmployee equipmentEmployee = new EquipmentEmployee()
            .dateOfRent(UPDATED_DATE_OF_RENT)
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS);
        return equipmentEmployee;
    }

    @BeforeEach
    public void initTest() {
        equipmentEmployee = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentEmployee() throws Exception {
        int databaseSizeBeforeCreate = equipmentEmployeeRepository.findAll().size();
        // Create the EquipmentEmployee
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);
        restEquipmentEmployeeMockMvc.perform(post("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentEmployee in the database
        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentEmployee testEquipmentEmployee = equipmentEmployeeList.get(equipmentEmployeeList.size() - 1);
        assertThat(testEquipmentEmployee.getDateOfRent()).isEqualTo(DEFAULT_DATE_OF_RENT);
        assertThat(testEquipmentEmployee.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testEquipmentEmployee.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createEquipmentEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentEmployeeRepository.findAll().size();

        // Create the EquipmentEmployee with an existing ID
        equipmentEmployee.setId(1L);
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentEmployeeMockMvc.perform(post("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentEmployee in the database
        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateOfRentIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentEmployeeRepository.findAll().size();
        // set the field null
        equipmentEmployee.setDateOfRent(null);

        // Create the EquipmentEmployee, which fails.
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);


        restEquipmentEmployeeMockMvc.perform(post("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentEmployeeRepository.findAll().size();
        // set the field null
        equipmentEmployee.setActive(null);

        // Create the EquipmentEmployee, which fails.
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);


        restEquipmentEmployeeMockMvc.perform(post("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentEmployeeRepository.findAll().size();
        // set the field null
        equipmentEmployee.setStatus(null);

        // Create the EquipmentEmployee, which fails.
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);


        restEquipmentEmployeeMockMvc.perform(post("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployees() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfRent").value(hasItem(DEFAULT_DATE_OF_RENT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipmentEmployee() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get the equipmentEmployee
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees/{id}", equipmentEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentEmployee.getId().intValue()))
            .andExpect(jsonPath("$.dateOfRent").value(DEFAULT_DATE_OF_RENT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        Long id = equipmentEmployee.getId();

        defaultEquipmentEmployeeShouldBeFound("id.equals=" + id);
        defaultEquipmentEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentEmployeeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentEmployeesByDateOfRentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where dateOfRent equals to DEFAULT_DATE_OF_RENT
        defaultEquipmentEmployeeShouldBeFound("dateOfRent.equals=" + DEFAULT_DATE_OF_RENT);

        // Get all the equipmentEmployeeList where dateOfRent equals to UPDATED_DATE_OF_RENT
        defaultEquipmentEmployeeShouldNotBeFound("dateOfRent.equals=" + UPDATED_DATE_OF_RENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByDateOfRentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where dateOfRent not equals to DEFAULT_DATE_OF_RENT
        defaultEquipmentEmployeeShouldNotBeFound("dateOfRent.notEquals=" + DEFAULT_DATE_OF_RENT);

        // Get all the equipmentEmployeeList where dateOfRent not equals to UPDATED_DATE_OF_RENT
        defaultEquipmentEmployeeShouldBeFound("dateOfRent.notEquals=" + UPDATED_DATE_OF_RENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByDateOfRentIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where dateOfRent in DEFAULT_DATE_OF_RENT or UPDATED_DATE_OF_RENT
        defaultEquipmentEmployeeShouldBeFound("dateOfRent.in=" + DEFAULT_DATE_OF_RENT + "," + UPDATED_DATE_OF_RENT);

        // Get all the equipmentEmployeeList where dateOfRent equals to UPDATED_DATE_OF_RENT
        defaultEquipmentEmployeeShouldNotBeFound("dateOfRent.in=" + UPDATED_DATE_OF_RENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByDateOfRentIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where dateOfRent is not null
        defaultEquipmentEmployeeShouldBeFound("dateOfRent.specified=true");

        // Get all the equipmentEmployeeList where dateOfRent is null
        defaultEquipmentEmployeeShouldNotBeFound("dateOfRent.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where active equals to DEFAULT_ACTIVE
        defaultEquipmentEmployeeShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the equipmentEmployeeList where active equals to UPDATED_ACTIVE
        defaultEquipmentEmployeeShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where active not equals to DEFAULT_ACTIVE
        defaultEquipmentEmployeeShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the equipmentEmployeeList where active not equals to UPDATED_ACTIVE
        defaultEquipmentEmployeeShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultEquipmentEmployeeShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the equipmentEmployeeList where active equals to UPDATED_ACTIVE
        defaultEquipmentEmployeeShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where active is not null
        defaultEquipmentEmployeeShouldBeFound("active.specified=true");

        // Get all the equipmentEmployeeList where active is null
        defaultEquipmentEmployeeShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where status equals to DEFAULT_STATUS
        defaultEquipmentEmployeeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the equipmentEmployeeList where status equals to UPDATED_STATUS
        defaultEquipmentEmployeeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where status not equals to DEFAULT_STATUS
        defaultEquipmentEmployeeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the equipmentEmployeeList where status not equals to UPDATED_STATUS
        defaultEquipmentEmployeeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEquipmentEmployeeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the equipmentEmployeeList where status equals to UPDATED_STATUS
        defaultEquipmentEmployeeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        // Get all the equipmentEmployeeList where status is not null
        defaultEquipmentEmployeeShouldBeFound("status.specified=true");

        // Get all the equipmentEmployeeList where status is null
        defaultEquipmentEmployeeShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentEmployeesByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);
        Equipment equipment = EquipmentResourceIT.createEntity(em);
        em.persist(equipment);
        em.flush();
        equipmentEmployee.setEquipment(equipment);
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);
        Long equipmentId = equipment.getId();

        // Get all the equipmentEmployeeList where equipment equals to equipmentId
        defaultEquipmentEmployeeShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the equipmentEmployeeList where equipment equals to equipmentId + 1
        defaultEquipmentEmployeeShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentEmployeesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        equipmentEmployee.setEmployee(employee);
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);
        Long employeeId = employee.getId();

        // Get all the equipmentEmployeeList where employee equals to employeeId
        defaultEquipmentEmployeeShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the equipmentEmployeeList where employee equals to employeeId + 1
        defaultEquipmentEmployeeShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentEmployeeShouldBeFound(String filter) throws Exception {
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfRent").value(hasItem(DEFAULT_DATE_OF_RENT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentEmployeeShouldNotBeFound(String filter) throws Exception {
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentEmployee() throws Exception {
        // Get the equipmentEmployee
        restEquipmentEmployeeMockMvc.perform(get("/api/equipment-employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentEmployee() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        int databaseSizeBeforeUpdate = equipmentEmployeeRepository.findAll().size();

        // Update the equipmentEmployee
        EquipmentEmployee updatedEquipmentEmployee = equipmentEmployeeRepository.findById(equipmentEmployee.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentEmployee are not directly saved in db
        em.detach(updatedEquipmentEmployee);
        updatedEquipmentEmployee
            .dateOfRent(UPDATED_DATE_OF_RENT)
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS);
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(updatedEquipmentEmployee);

        restEquipmentEmployeeMockMvc.perform(put("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentEmployee in the database
        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeUpdate);
        EquipmentEmployee testEquipmentEmployee = equipmentEmployeeList.get(equipmentEmployeeList.size() - 1);
        assertThat(testEquipmentEmployee.getDateOfRent()).isEqualTo(UPDATED_DATE_OF_RENT);
        assertThat(testEquipmentEmployee.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testEquipmentEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentEmployee() throws Exception {
        int databaseSizeBeforeUpdate = equipmentEmployeeRepository.findAll().size();

        // Create the EquipmentEmployee
        EquipmentEmployeeDTO equipmentEmployeeDTO = equipmentEmployeeMapper.toDto(equipmentEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentEmployeeMockMvc.perform(put("/api/equipment-employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentEmployeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentEmployee in the database
        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentEmployee() throws Exception {
        // Initialize the database
        equipmentEmployeeRepository.saveAndFlush(equipmentEmployee);

        int databaseSizeBeforeDelete = equipmentEmployeeRepository.findAll().size();

        // Delete the equipmentEmployee
        restEquipmentEmployeeMockMvc.perform(delete("/api/equipment-employees/{id}", equipmentEmployee.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentEmployee> equipmentEmployeeList = equipmentEmployeeRepository.findAll();
        assertThat(equipmentEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
