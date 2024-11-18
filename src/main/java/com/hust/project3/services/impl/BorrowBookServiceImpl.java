package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.borrowing.BorrowBookRequestDTO;
import com.hust.project3.entities.BorrowBook;
import com.hust.project3.entities.User;
import com.hust.project3.repositories.BorrowBookRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowBookServiceImpl implements BorrowBookService {
    private final BorrowBookRepository borrowBookRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Page<BorrowBook> getBorrowingsByUserLogin(String jwt, BorrowBookRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        return null;
    }

    @Override
    public BorrowBook getBorrowingById(String jwt, Long id) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email).get();

        return borrowBookRepository.findById(id).orElse(null);
    }

    @Override
    public BorrowBook addBorrowBook(String jwt, BorrowBookRequestDTO dto) {
        return null;
    }
}
