package io.tempo.teams.service.orchestrator.roles.models

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class RolesTeamsUsersId : Serializable {

        @Column(name = "user_id")
        var userId: String? = null

        @Column(name = "team_id")
        var teamId: String? = null

        @Column(name = "role_id")
        var roleId: Long? = null

        constructor()

        constructor(userId: String, teamId: String, roleId: Long) {
                this.userId = userId
                this.teamId = teamId
                this.roleId = roleId
        }

        override fun toString(): String {
                return "RolesTeamsUsersId(userId=$userId, teamId=$teamId, roleId=$roleId)"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as RolesTeamsUsersId

                if (teamId != other.teamId) return false
                if (userId != other.userId) return false
                if (roleId != other.roleId) return false

                return true
        }

        override fun hashCode(): Int {
                var result = teamId.hashCode()
                result = 31 * result + userId.hashCode()
                result = 31 * result + roleId.hashCode()
                return result
        }

}