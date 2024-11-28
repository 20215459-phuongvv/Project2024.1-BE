package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.entities.User;
import com.hust.project3.enums.ReadingCardStatusEnum;
import com.hust.project3.enums.ReadingCardTypeEnum;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.ReadingCardRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.ReadingCardService;
import com.hust.project3.specification.ReadingCardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingCardServiceImpl implements ReadingCardService {
    private final ReadingCardRepository readingCardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ReadingCard getReadingCardByUser(String jwt) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return user.getReadingCard();
    }

    @Override
    @Transactional
    public ReadingCard addReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        ReadingCard readingCard = ReadingCard.builder()
                .code(UUID.randomUUID().toString().toUpperCase())
                .type(dto.getType())
                .startDate(LocalDate.now())
                .expiryDate(dto.getType() == ReadingCardTypeEnum.MONTHLY.ordinal() ? LocalDate.now().plusMonths(dto.getNumberOfPeriod()) : LocalDate.now().plusYears(dto.getNumberOfPeriod()))
                .violationCount(0)
                .status(ReadingCardStatusEnum.PENDING.ordinal())
                .updatedBy(email)
                .user(user)
                .build();
        return readingCardRepository.save(readingCard);
    }

    @Override
    public ReadingCard addPayment(String jwt, ReadingCardRequestDTO dto) {
        return null;
    }

    @Override
    public ReadingCard renewReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        ReadingCard readingCard = getReadingCardByUser(jwt);
        if (readingCard.getType() == ReadingCardTypeEnum.MONTHLY.ordinal()) {
            readingCard.setExpiryDate(readingCard.getExpiryDate().plusMonths(dto.getNumberOfPeriod()));
        } else if (readingCard.getType() == ReadingCardTypeEnum.YEARLY.ordinal()) {
            readingCard.setExpiryDate(readingCard.getExpiryDate().plusYears(dto.getNumberOfPeriod()));
        }
        return readingCardRepository.save(readingCard);
    }

    @Override
    public ReadingCard cancelReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        ReadingCard readingCard = getReadingCardByUser(jwt);
        readingCard.setStatus(ReadingCardStatusEnum.DEACTIVATED.ordinal());
        return readingCardRepository.save(readingCard);
    }

    @Override
    public Page<ReadingCard> getAllReadingCards(ReadingCardRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Specification<ReadingCard> spec = ReadingCardSpecification.byCriteria(dto);
        return readingCardRepository.findAll(spec, PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize()));
    }

    @Override
    public ReadingCard getReadingCardById(Long id) throws NotFoundException {
        return readingCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reading card not found with id: " + id));
    }

    @Override
    public ReadingCard warnReadingCard(Long id) throws NotFoundException {
        ReadingCard readingCard = getReadingCardById(id);
        readingCard.setViolationCount(readingCard.getViolationCount() + 1);
        if (readingCard.getViolationCount() >= 3) {
            readingCard.setStatus(ReadingCardStatusEnum.BANNED.ordinal());
        } else {
            readingCard.setStatus(ReadingCardStatusEnum.WARNED.ordinal());
        }
        readingCardRepository.save(readingCard);
        return readingCardRepository.save(readingCard);
    }

    @Override
    public ReadingCard toggleReadingCardStatus(Long id) throws NotFoundException {
        ReadingCard readingCard = getReadingCardById(id);
        if (readingCard.getStatus() == ReadingCardStatusEnum.BANNED.ordinal()) {
            readingCard.setStatus(ReadingCardStatusEnum.ACTIVATED.ordinal());
        } else {
            readingCard.setStatus(ReadingCardStatusEnum.BANNED.ordinal());
        }
        return readingCardRepository.save(readingCard);
    }
}
