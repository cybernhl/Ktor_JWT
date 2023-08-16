package tw.idv.neo.shared.db.adapter

import app.cash.sqldelight.ColumnAdapter

class LongIntAdapter : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}