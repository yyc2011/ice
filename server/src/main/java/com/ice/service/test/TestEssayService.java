package com.ice.service.test;

import com.ice.dto.test.TestEssayDto;
import com.ice.dto.test.TestRequest;
import com.ice.repository.test.TestEssayRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestEssayService {

    private final TestEssayRepository repository;

    public TestEssayService(TestEssayRepository repository) {
        this.repository = repository;
    }

    public List<TestEssayDto> getAll(TestRequest request) {
        return repository.findAll();
    }
}
