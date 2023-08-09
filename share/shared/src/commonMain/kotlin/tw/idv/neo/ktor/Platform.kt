package tw.idv.neo.ktor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform