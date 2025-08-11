package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nightfire.tonkotsu.feature.search.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val currentQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Local text state to capture user typing without triggering search yet
    var textFieldValue by remember { mutableStateOf(currentQuery.query ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search anime...") },
            leadingIcon = {
                IconButton(onClick = {
                    // Trigger search on icon click
                    viewModel.updateSearchQuery(currentQuery.copy(query = textFieldValue.ifBlank { null }))
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Trigger search when user presses search on keyboard
                    viewModel.updateSearchQuery(currentQuery.copy(query = textFieldValue.ifBlank { null }))
                    // Optionally hide keyboard here if you want
                }
            )
        )

        LazyColumn {
            items(searchResults.itemCount) { index ->
                val anime = searchResults[index]
                anime?.let {
                    Text(text = anime.title, modifier = Modifier.padding(8.dp))
                }
            }

            searchResults.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(Modifier.fillMaxWidth())
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(Modifier.fillMaxWidth())
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            Text("Error: ${e.error.localizedMessage}")
                        }
                    }
                }
            }
        }
    }
}

