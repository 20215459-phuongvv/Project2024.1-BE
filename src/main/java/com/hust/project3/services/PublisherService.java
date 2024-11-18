package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.publisher.PublisherRequestDTO;
import com.hust.project3.entities.Publisher;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PublisherService {
    Page<Publisher> getPublishersByProperties(PublisherRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    Publisher getPublisherById(Long id) throws NotFoundException;

    Publisher addPublisher(String jwt, PublisherRequestDTO dto);

    Publisher updatePublisher(String jwt, PublisherRequestDTO dto) throws NotFoundException;

    List<Publisher> deletePublishers(String jwt, List<Long> idList) throws NotFoundException;
}
