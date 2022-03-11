package com.example.composelearning

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container


class MyViewModel : ContainerHost<MyState, MySideEffects>, ViewModel() {
    override val container: Container<MyState, MySideEffects> = container(MyState(0))
    val sideFlow = container.sideEffectFlow
    val state = container.stateFlow

    fun onStartProgress() = intent {
        repeat(10) {
            delay(200)
            updateCounter(it)
        }
    }

    fun onChangeConstantValue() = intent {
        reduce {
            state.copy(
                contantValue = "Updated at [${System.currentTimeMillis()}]"
            )
        }
    }

    private fun updateCounter(it: Int) = intent {
        reduce {
            state.copy(
                progress = it
            )
        }
    }
}

@Stable
data class MyState(
    val progress: Int,
    val contantValue : String = "ContantValue"
)

sealed class MySideEffects {

}