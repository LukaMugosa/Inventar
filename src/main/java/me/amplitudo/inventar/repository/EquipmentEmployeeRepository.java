package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentEmployee;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentEmployeeRepository extends JpaRepository<EquipmentEmployee, Long>, JpaSpecificationExecutor<EquipmentEmployee> {
}
