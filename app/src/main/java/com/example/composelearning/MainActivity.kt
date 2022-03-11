package com.example.composelearning

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                RootViewMvi()
                Divider()
                RootViewNoMvi()
            }
        }
    }

    @Composable
    private fun ColumnScope.RootViewMvi() {
        LogCompositions(tag = "RootViewMvi")
        val viewModel: MyViewModel = viewModel()
        val outsideCallback: () -> Unit = {
            viewModel.onChangeConstantValue()
        }
        val state by viewModel.state.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ORBIT MVI")
            //Callback created inside Composable
            CallbackFromOutSide(
                content = state.contantValue,
                outsideCallback = {
                    outsideCallback()
                })

            // Callback created inside Composable
            ChangeContantButton(content = state.contantValue, changeContent = {
                viewModel.onChangeConstantValue()
            })

            //View Listening to progress
            StartProgressButton(progress = state.progress) {
                viewModel.onStartProgress()
            }

            ChangeRandomButton(random = state.random) {
                viewModel.updateRandom(it)
            }

            //A Unstable Object
            TestView()
        }
    }

    @Composable
    private fun ColumnScope.RootViewNoMvi() {
        LogCompositions(tag = "RootViewNoMvi")
        var state by remember { mutableStateOf(MyState(0)) }
        var initProgress by remember { mutableStateOf(false) }
        LaunchedEffect(initProgress) {
            if (initProgress) {
                repeat(10) {
                    delay(200)
                    state = state.copy(progress = it)
                }
                initProgress = false
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Gray),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "STATEMVI")
            //Callback created inside Composable
            CallbackFromOutSide(
                log = "NOMVI",
                content = state.contantValue,
                outsideCallback = {
                    state = state.copy(contantValue = "${System.currentTimeMillis()}")
                })

            //Callback created inside Composable
            ChangeContantButton(log = "NOMVI", content = state.contantValue, changeContent = {
                state = state.copy(contantValue = "${System.currentTimeMillis()}")
            })

            //View Listening to progress
            StartProgressButton(progress = state.progress) {
                initProgress = true
            }

            ChangeRandomButton(log = "STATEMVI", random = state.random) {
                state = state.copy(random = list.random())
            }

            //A Unstable Object
            TestView(log = "NOMVI")
        }
    }

    @Composable
    private fun CallbackFromOutSide(
        log: String = "MVI",
        content: String,
        outsideCallback: () -> Unit = {}
    ) {
        LogCompositions(tag = "CallbackFromOutSide$log Callback")
        Row {
            Button(onClick = outsideCallback) {
                Text(text = "Out")
            }
            Text(text = content)
        }
    }

    @Composable
    private fun ChangeContantButton(
        log: String = "MVI",
        content: String,
        changeContent: () -> Unit = {}
    ) {
        LogCompositions(tag = "ChangeContantButton$log Callback")
        Row {
            Button(onClick = changeContent) {
                Text(text = "Update Out")
            }
            Text(text = content)
        }
    }

    @Composable
    private fun ChangeRandomButton(
        log: String = "MVI",
        random: Int,
        changeRandom: (Int) -> Unit = {}
    ) {
        LogCompositions(tag = "ChangeRandomButton$log ${changeRandom.hashCode()}")
        Row {
            Button(onClick = { changeRandom(list.random()) }) {
                Text(text = "Random")
            }
            Text(text = "$random")
        }
    }

    @Composable
    private fun StartProgressButton(
        log: String = "MVI",
        progress: Int,
        onStartProgress: () -> Unit = {}
    ) {
        LogCompositions(tag = "StartProgressButton$log")
        Row {
            Button(onClick = onStartProgress) {
                Text(text = "Start Progress")
            }
            Text(text = "$progress")
        }
    }


    @Composable
    private fun TestView(log: String = "MVI") {
        LogCompositions(tag = "TestViewExoPlayer$log")
        val context = LocalContext.current
        val exoplayer = remember(context) {
            ExoPlayer.Builder(context).build()
        }
        Text(text = "${exoplayer.hashCode()}")
    }

}

class Ref(var value: Int)

@Composable
inline fun LogCompositions(tag: String) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    Log.d("RecompositionTrack", "$tag Compositions: ${ref.value}")
}

val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)