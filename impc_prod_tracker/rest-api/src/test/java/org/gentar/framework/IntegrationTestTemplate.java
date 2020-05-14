package org.gentar.framework;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

/** Base class for integration tests that need the full context of the Spring Boot application. */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
    DbUnitTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})

@SpringBootTest
@ActiveProfiles("test")

@WebAppConfiguration
public abstract class IntegrationTestTemplate
{
    @BeforeEach
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }
}