package com.example.composelearning

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MyViewModel()
        val outsideCallback: () -> Unit = {
            viewModel.onChangeConstantValue()
        }
        setContent {
            RootView(viewModel, outsideCallback = outsideCallback)
        }
    }

    @Composable
    private fun RootView(viewModel: MyViewModel, outsideCallback: () -> Unit) {
        val state by viewModel.state.collectAsState()
        val context = LocalContext.current
        val exoplayer = remember(context) {
            ExoPlayer.Builder(context).build()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Callback created inside Composable
            CallbackFromOutSide(content = state.contantValue, outsideCallback = outsideCallback)

            //Callback created inside Composable
            ChangeContantButton(content = state.contantValue, changeContent = {
                viewModel.onChangeConstantValue()
            })

            //View Listening to progress
            StartProgressButton(progress = state.progress, onStartProgress = {
                viewModel.onStartProgress()
            })

            //A Unstable Object
            TestView(exoplayer)
        }
    }

    @Composable
    private fun CallbackFromOutSide(content: String, outsideCallback: () -> Unit) {
        LogCompositions(tag = "CallbackFromOutSide Callback")
        Row {
            Button(onClick = outsideCallback) {
                Text(text = "Update via Outsider Callback")
            }
            Text(text = content)
        }
    }

    @Composable
    private fun ChangeContantButton(content: String, changeContent: () -> Unit) {
        LogCompositions(tag = "ChangeContantButton Callback}")
        Row {
            Button(onClick = changeContent) {
                Text(text = "Update Value")
            }
            Text(text = content)
        }
    }


    @Composable
    private fun StartProgressButton(progress: Int, onStartProgress: () -> Unit) {
        LogCompositions(tag = "StartProgressButton")
        Row {
            Button(onClick = onStartProgress) {
                Text(text = "Start Progress")
            }
            Text(text = "$progress")
        }
    }


    @Composable
    private fun TestView(param: ExoPlayer) {
        LogCompositions(tag = "TestViewExoPlayer ${param.hashCode()}")
        Text(text = "${param.hashCode()}")
    }

}

@Stable
data class StableObject<T>(val value: T)

class Ref(var value: Int)


@Composable
inline fun LogCompositions(tag: String) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    Log.d("RecompositionTrack", "$tag Compositions: ${ref.value}")
}

