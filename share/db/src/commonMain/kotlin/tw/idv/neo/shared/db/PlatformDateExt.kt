package tw.idv.neo.shared.db

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun createLocalDateTime(): LocalDateTime = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())