package com.ice.repository.test;

import com.ice.dto.test.TestEssayDto;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class TestEssayRepository {

    private final DSLContext dsl;

    public TestEssayRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<TestEssayDto> findAll() {
        return dsl.fetch("SELECT id, title FROM test_essay_list")
                .map(record -> new TestEssayDto(
                        (Integer) record.get(0),
                        (String) record.get(1)
                ));
    }
}
