package io.tempo.teams.service.orchestrator.teams.models

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class TeamsUsersId : Serializable {

        @Column(name = "team_id")
        var teamId: String? = null

        @Column(name = "user_id")
        var userId: String? = null

        constructor()

        constructor(teamId: String, userId: String) {
                this.teamId = teamId
                this.userId = userId
        }

        override fun toString(): String {
                return "TeamsUsersId(teamId=$teamId, userId=$userId)"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as TeamsUsersId

                if (teamId != other.teamId) return false
                if (userId != other.userId) return false

                return true
        }

        override fun hashCode(): Int {
                var result = teamId.hashCode()
                result = 31 * result + userId.hashCode()
                return result
        }

}