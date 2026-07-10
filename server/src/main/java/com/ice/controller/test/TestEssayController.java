package com.ice.controller.test;

import com.ice.dto.test.TestEssayDto;
import com.ice.dto.test.TestRequest;
import com.ice.service.test.TestEssayService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestEssayController {

    private final TestEssayService service;

    public TestEssayController(TestEssayService service) {
        this.service = service;
    }

    @PostMapping("/essay-list")
    public List<TestEssayDto> getList(@RequestBody TestRequest request) {
        return service.getAll(request);
    }
}
