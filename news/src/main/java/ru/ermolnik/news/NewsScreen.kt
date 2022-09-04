package ru.ermolnik.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.value) {
            is NewsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
            is NewsState.Error -> {
                Text(
                    text = (state.value as NewsState.Error).throwable.toString(),
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            is NewsState.Content -> {
                Text(
                    text = (state.value as NewsState.Content).id.toString(),
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
