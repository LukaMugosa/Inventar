package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentImage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentImageRepository extends JpaRepository<EquipmentImage, Long>, JpaSpecificationExecutor<EquipmentImage> {
}
