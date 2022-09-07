package io.tempo.teams.util

class EnvProfilesUtil {

    companion object {
        private val SPRING_PROFILES_ACTIVE = System.getenv("SPRING_PROFILES_ACTIVE").split(",")

        // TODO: is there a better way to validate e-mails?
        @JvmStatic
        fun isProfileActivated(profileName: String): Boolean {
            return SPRING_PROFILES_ACTIVE.contains(profileName)
        }
    }
}