package com.example.composelearning

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val list = listOf("a","b","C","d","e","f")
        runBlocking {
            print(list.stream() })
        }
    }

    private suspend fun appendAscii(string: String) : List<String> {
        if (string=="a")
          delay(3000)

        return listOf(string, string)
    }
}
