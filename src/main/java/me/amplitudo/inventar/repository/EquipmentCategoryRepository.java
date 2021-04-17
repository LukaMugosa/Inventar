package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long>, JpaSpecificationExecutor<EquipmentCategory> {
}
