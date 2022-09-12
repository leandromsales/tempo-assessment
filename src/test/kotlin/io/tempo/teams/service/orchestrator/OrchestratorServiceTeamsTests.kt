package io.tempo.teams.service.orchestrator

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.orchestrator.teams.TeamsService
import io.tempo.teams.service.orchestrator.teams.models.Team
import io.tempo.teams.service.orchestrator.users.UsersService
import io.tempo.teams.service.orchestrator.users.models.User
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrchestratorServiceTeamsTests {

    @Autowired
    private lateinit var teamsService: TeamsService

    @Autowired
    private lateinit var usersService: UsersService

    companion object {
        private var aTeam: Team? = null
        private var aUser: User? = null

        @BeforeAll
        @JvmStatic
        internal fun setUp() {
            aTeam = Team()
            aTeam!!.name = "Leandro's Team"

            aUser = User()
            aUser!!.firstName = "Leandro"
            aUser!!.lastName = "de Sales"
            aUser!!.avatarUrl = "https://tempo.io/users/leandroDeSales/avatar.jpg"
            aUser!!.location = "Maceio"
        }
    }

    @Test
    @DisplayName("Getting a team by an invalid id.")
    fun givenInvalidTeamId_thenThrowsException() {
        assertThrows<TempoException> { teamsService.get("wrong-id") }
    }

    @Test
    @Order(1)
    @DisplayName("Add an team and check attributes.")
    fun addTeam_thenCheckValues() {
        val savedUser: User? = assertDoesNotThrow { usersService.add(aUser!!) }
        aTeam!!.teamLead = savedUser
        val savedTeam: Team = assertDoesNotThrow { teamsService.save(aTeam!!) }
        Assertions.assertEquals(savedTeam.name, aTeam!!.name)
        Assertions.assertEquals(savedTeam.teamLead!!.id, savedUser!!.id)
        aTeam!!.id = savedTeam.id
    }

    @Test
    @Order(2)
    @DisplayName("Get a valid team by Id.")
    fun getValidTeam_thenCheckValues() {
        val savedTeam: Team = assertDoesNotThrow { teamsService.get(aTeam!!.id!!) }
        Assertions.assertEquals(savedTeam.name, aTeam!!.name)
    }

    @Test
    @Order(3)
    @DisplayName("Update a valid team by Id.")
    fun updateValidTeam_thenCheckChanges() {
        aTeam!!.name = "Joshua's Team"

        val savedTeam: Team = assertDoesNotThrow { teamsService.save(aTeam!!) }

        Assertions.assertEquals(savedTeam.name, aTeam!!.name)
    }

    @Test
    @Order(4)
    @DisplayName("Delete a valid team by Id.")
    fun deleteValidTeam_thenGetToCheck() {
        assertDoesNotThrow { teamsService.delete(aTeam!!.id!!) }
        assertThrows<TempoException> { teamsService.get(aTeam!!.id!!) }
    }

}