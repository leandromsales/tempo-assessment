package io.tempo.teams.service.orchestrator

import io.tempo.teams.service.core.exceptions.TempoException
import io.tempo.teams.service.orchestrator.users.UsersService
import io.tempo.teams.service.orchestrator.users.models.User
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrchestratorServiceUsersTests {

    companion object {
        private var aUser: User? = null

        @BeforeAll
        @JvmStatic
        internal fun setUp() {
            aUser = User()
            aUser!!.firstName = "Leandro"
            aUser!!.lastName = "de Sales"
            aUser!!.avatarUrl = "https://tempo.io/users/leandroDeSales/avatar.jpg"
            aUser!!.location = "Maceio"
        }
    }

    @Autowired
    private lateinit var usersService: UsersService

    @Test
    @DisplayName("Getting a user by an invalid id.")
    fun givenInvalidUserId_thenThrowsException() {
        assertThrows<TempoException> { usersService.get("wrong-id") }
    }

    @Test
    @Order(1)
    @DisplayName("Add an user and check attributes.")
    fun addUser_thenCheckAttributes() {
        val savedUser: User? = assertDoesNotThrow { usersService.add(aUser!!) }
        aUser!!.id = savedUser?.id!!
    }

    @Test
    @Order(2)
    @DisplayName("Get a valid user by Id.")
    fun getValidUser_thenCheckValues() {
        val savedUser: User? = assertDoesNotThrow { usersService.get(aUser!!.id!!) }

        Assertions.assertEquals(savedUser?.firstName, aUser!!.firstName)
        Assertions.assertEquals(savedUser?.lastName, aUser!!.lastName)
        Assertions.assertEquals(savedUser?.avatarUrl, aUser!!.avatarUrl)
        Assertions.assertEquals(savedUser?.location, aUser!!.location)
    }

    @Test
    @Order(3)
    @DisplayName("Update a valid user by Id.")
    fun updateValidUser_thenCheckChanges() {
        aUser!!.firstName = "Joshua"
        aUser!!.lastName = "Chioma"
        aUser!!.avatarUrl = "https://tempo.io/users/joshuaChioma/avatar.jpg"
        aUser!!.location = "Nigeria"

        val savedUser: User = assertDoesNotThrow { usersService.update(aUser!!) }

        Assertions.assertEquals(savedUser.firstName, aUser!!.firstName)
        Assertions.assertEquals(savedUser.lastName, aUser!!.lastName)
        Assertions.assertEquals(savedUser.avatarUrl, aUser!!.avatarUrl)
        Assertions.assertEquals(savedUser.location, aUser!!.location)
    }

    @Test
    @Order(4)
    @DisplayName("Delete a valid user by Id.")
    fun deleteValidUser_thenGetToCheck() {
        assertDoesNotThrow { usersService.delete(aUser!!.id!!) }
        assertThrows<TempoException> { usersService.get(aUser!!.id!!) }
    }

}