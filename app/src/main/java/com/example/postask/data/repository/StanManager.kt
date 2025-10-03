package com.example.postask.data.repository

import android.content.SharedPreferences
import com.example.postask.domain.repository.IsoRepository

class StanManager(private val prefs: SharedPreferences) : IsoRepository {

    companion object {
        private const val KEY_STAN = "key_stan_last_value"
        private const val MAX_STAN = 999999
        private const val MIN_STAN = 1
    }


    //  Calculates and returns the next 6-digit STAN.Resets to 1 after reaching 999999
    override fun getNextStan(): String {
        val current = prefs.getInt(KEY_STAN, 0)
        val next = if (current >= MAX_STAN) MIN_STAN else current + 1
        prefs.edit().putInt(KEY_STAN, next).apply()
        return "%06d".format(next)
    }
}
