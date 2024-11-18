package com.hust.project3.repositories;

import com.hust.project3.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublisherRepository extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {
}
