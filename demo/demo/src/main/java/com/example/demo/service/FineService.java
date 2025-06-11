package com.example.demo.service;

import com.example.demo.model.Fine;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;
    private final ReaderRepository readerRepository;

    public FineService(FineRepository fineRepository, ReaderRepository readerRepository) {
        this.fineRepository = fineRepository;
        this.readerRepository = readerRepository;
    }

    public List<Fine> getFinesByEmail(String email) {
        return readerRepository.findByUserEmail(email)
                .map(fineRepository::findByReader)
                .orElse(Collections.emptyList());
    }
}