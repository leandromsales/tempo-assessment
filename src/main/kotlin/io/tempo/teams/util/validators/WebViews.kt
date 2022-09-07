package io.tempo.teams.util.validators

class View {
    open class Public { }

    open class ExtendedPublic : Public() { }

    class Internal : ExtendedPublic() { }

}