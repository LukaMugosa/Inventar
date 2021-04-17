package me.amplitudo.inventar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "profile_image_content_type")
    private String profileImageContentType;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentRequest> equipmentRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProblemReport> problemReports = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentService> equipmentServices = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentEmployee> equipmentEmployees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "employees", allowSetters = true)
    private Tenant tenant;

    @ManyToOne
    @JsonIgnoreProperties(value = "employees", allowSetters = true)
    private Position position;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> createdNotifications = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> receivedNotifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public Employee fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public Employee profileImage(byte[] profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageContentType() {
        return profileImageContentType;
    }

    public Employee profileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
        return this;
    }

    public void setProfileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Employee createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Employee updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public Employee user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<EquipmentRequest> getEquipmentRequests() {
        return equipmentRequests;
    }

    public Employee equipmentRequests(Set<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
        return this;
    }

    public Employee addEquipmentRequests(EquipmentRequest equipmentRequest) {
        this.equipmentRequests.add(equipmentRequest);
        equipmentRequest.setEmployee(this);
        return this;
    }

    public Employee removeEquipmentRequests(EquipmentRequest equipmentRequest) {
        this.equipmentRequests.remove(equipmentRequest);
        equipmentRequest.setEmployee(null);
        return this;
    }

    public void setEquipmentRequests(Set<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public Set<ProblemReport> getProblemReports() {
        return problemReports;
    }

    public Employee problemReports(Set<ProblemReport> problemReports) {
        this.problemReports = problemReports;
        return this;
    }

    public Employee addProblemReports(ProblemReport problemReport) {
        this.problemReports.add(problemReport);
        problemReport.setEmployee(this);
        return this;
    }

    public Employee removeProblemReports(ProblemReport problemReport) {
        this.problemReports.remove(problemReport);
        problemReport.setEmployee(null);
        return this;
    }

    public void setProblemReports(Set<ProblemReport> problemReports) {
        this.problemReports = problemReports;
    }

    public Set<EquipmentService> getEquipmentServices() {
        return equipmentServices;
    }

    public Employee equipmentServices(Set<EquipmentService> equipmentServices) {
        this.equipmentServices = equipmentServices;
        return this;
    }

    public Employee addEquipmentService(EquipmentService equipmentService) {
        this.equipmentServices.add(equipmentService);
        equipmentService.setEmployee(this);
        return this;
    }

    public Employee removeEquipmentService(EquipmentService equipmentService) {
        this.equipmentServices.remove(equipmentService);
        equipmentService.setEmployee(null);
        return this;
    }

    public void setEquipmentServices(Set<EquipmentService> equipmentServices) {
        this.equipmentServices = equipmentServices;
    }

    public Set<EquipmentEmployee> getEquipmentEmployees() {
        return equipmentEmployees;
    }

    public Employee equipmentEmployees(Set<EquipmentEmployee> equipmentEmployees) {
        this.equipmentEmployees = equipmentEmployees;
        return this;
    }

    public Employee addEquipmentEmployees(EquipmentEmployee equipmentEmployee) {
        this.equipmentEmployees.add(equipmentEmployee);
        equipmentEmployee.setEmployee(this);
        return this;
    }

    public Employee removeEquipmentEmployees(EquipmentEmployee equipmentEmployee) {
        this.equipmentEmployees.remove(equipmentEmployee);
        equipmentEmployee.setEmployee(null);
        return this;
    }

    public void setEquipmentEmployees(Set<EquipmentEmployee> equipmentEmployees) {
        this.equipmentEmployees = equipmentEmployees;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Employee tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Position getPosition() {
        return position;
    }

    public Employee position(Position position) {
        this.position = position;
        return this;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Set<Notification> getCreatedNotifications() {
        return createdNotifications;
    }

    public Employee createdNotifications(Set<Notification> notifications) {
        this.createdNotifications = notifications;
        return this;
    }
    public void setCreatedNotifications(Set<Notification> notifications) {
        this.createdNotifications = notifications;
    }

    public Set<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public Employee receivedNotifications(Set<Notification> notifications) {
        this.receivedNotifications = notifications;
        return this;
    }
    public void setReceivedNotifications(Set<Notification> notifications) {
        this.receivedNotifications = notifications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", profileImage='" + getProfileImage() + "'" +
            ", profileImageContentType='" + getProfileImageContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
