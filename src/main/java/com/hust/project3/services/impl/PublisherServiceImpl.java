package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.publisher.PublisherRequestDTO;
import com.hust.project3.entities.Publisher;
import com.hust.project3.enums.EntityStatusEnum;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.PublisherRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.PublisherService;
import com.hust.project3.specification.PublisherSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Page<Publisher> getPublishersByProperties(PublisherRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize());
        Specification<Publisher> spec = PublisherSpecification.byCriteria(dto);
        return publisherRepository.findAll(spec, pageable);
    }

    @Override
    public Publisher getPublisherById(Long id) throws NotFoundException {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
    }

    @Override
    public Publisher addPublisher(String jwt, PublisherRequestDTO dto) {
        Publisher publisher = Publisher.builder()
                .name(dto.getName())
                .updatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .build();
        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher updatePublisher(String jwt, PublisherRequestDTO dto) throws NotFoundException {
        Publisher publisher = publisherRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Publisher not found"));
        publisher.setName(dto.getName());
        publisher.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
        return publisherRepository.save(publisher);
    }

    @Override
    public List<Publisher> deletePublishers(String jwt, List<Long> idList) throws NotFoundException {
        List<Publisher> result = new ArrayList<>();
        for (long id : idList) {
            Publisher publisher = getPublisherById(id);
            publisher.setStatus(EntityStatusEnum.INACTIVE.ordinal());
            publisher.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
            result.add(publisherRepository.save(publisher));
        }
        return result;
    }

}
