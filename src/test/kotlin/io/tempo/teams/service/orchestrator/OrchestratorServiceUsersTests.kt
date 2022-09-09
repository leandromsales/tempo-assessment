package io.tempo.teams.service.orchestrator

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.orchestrator.users.UsersService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrchestratorServiceUsersTests {

//    @Rule
//    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private lateinit var usersService: UsersService

    @Test
    fun getUserNotFound() {
        Assertions.assertThatThrownBy { usersService.get("wrong-id") }.isInstanceOf(TempoException::class.java)
    }

    // TODO: add more unit tests
}