package com.example.composelearning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Stable
@Immutable
data class DummyState(
    val progress: Int = 0,
    val content: String = "content",
    val random: Int = 0
)

class DummyViewModel : ViewModel() {
    val state = MutableStateFlow(DummyState())

    fun onStartProgress() = viewModelScope.launch {
        repeat(10) {
            delay(200)
            state.value = state.value.copy(progress = it)
        }
    }

    fun updateContent() = viewModelScope.launch {
        state.value = state.value.copy(content = "${System.currentTimeMillis()}")
    }

    fun updateRandom(random: Int) = viewModelScope.launch {
        state.value = state.value.copy(random = random)
    }
}

@Composable
fun RootView(modifier: Modifier) {
    LogCompositions(tag = "RootView")
    val viewModel: DummyViewModel = viewModel()
    //pass lambda from outside or use method reference like viewModel::updateRandom
    val updateRandom: (Int) -> Unit = { viewModel.updateRandom(it) }
    RenderContent(modifier, viewModel, updateRandom)
}

@Composable
fun RenderContent(
    modifier: Modifier,
    viewModel: DummyViewModel = viewModel(),
    updateRandom: (Int) -> Unit
) {
    LogCompositions(tag = "RenderContent")
    val state by viewModel.state.collectAsState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Callback From outside")
        ContentText(
            content = state.content,
            updateContent = viewModel::updateContent
        )
        StartProgress(
            progress = state.progress,
            onStartProgress = viewModel::onStartProgress
        )
        RandomButton(random = state.random, updateRandom = updateRandom)
    }
}

@Composable
private fun ContentText(
    content: String,
    updateContent: () -> Unit = {}
) {
    LogCompositions(tag = "ContentText ${updateContent.hashCode()}")
    Row {
        Button(onClick = updateContent) {
            Text(text = "Out")
        }
        Text(text = content)
    }
}

@Composable
private fun RandomButton(
    random: Int,
    updateRandom: (Int) -> Unit
) {
    LogCompositions(tag = "RandomButton ${updateRandom.hashCode()}")
    Row {
        Button(onClick = { updateRandom(dummyList.random()) }) {
            Text(text = "Random")
        }
        Text(text = "$random")
    }
}

@Composable
private fun StartProgress(
    progress: Int,
    onStartProgress: () -> Unit = {}
) {
    LogCompositions(tag = "StartProgress ${onStartProgress.hashCode()}")
    Row {
        Button(onClick = onStartProgress) {
            Text(text = "Start Progress")
        }
        Text(text = "$progress")
    }
}

val dummyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
