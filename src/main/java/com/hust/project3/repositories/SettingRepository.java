package com.hust.project3.repositories;

import com.hust.project3.entities.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long>, JpaSpecificationExecutor<Setting> {
    boolean existsByKey(String name);

    Optional<Setting> findByKey(String name);
}
