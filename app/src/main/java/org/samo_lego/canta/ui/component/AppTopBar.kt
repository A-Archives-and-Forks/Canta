package org.samo_lego.canta.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import org.samo_lego.canta.APP_NAME
import org.samo_lego.canta.ui.menu.FiltersMenu
import org.samo_lego.canta.ui.menu.MoreOptionsMenu
import org.samo_lego.canta.ui.viewmodel.AppListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CantaTopBar(
    openBadgesInfoDialog: () -> Unit,
    openLogsScreen: () -> Unit,
) {
    var showMoreOptionsMenu by remember { mutableStateOf(false) }
    var showFiltersMenu by remember { mutableStateOf(false) }
    var searchActive by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val appListViewModel = viewModel<AppListViewModel>()

    TopAppBar(
        title = {
            Box(
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            searchActive = false
                            appListViewModel.searchQuery = ""
                            searchFocusRequester.freeFocus()
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    TextField(
                        modifier = Modifier.focusRequester(searchFocusRequester),
                        value = appListViewModel.searchQuery,
                        onValueChange = {
                            appListViewModel.searchQuery = it
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                    )
                }
            }

            if (!searchActive) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(APP_NAME)
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    appListViewModel.searchQuery = ""
                    searchActive = true
                    searchFocusRequester.requestFocus()
                    keyboardController?.show()
                }
            ) {
                Icon(
                    if (searchActive) Icons.Default.Clear
                    else Icons.Default.Search,
                    contentDescription = "Search"
                )
            }

            IconButton(
                onClick = { showFiltersMenu = !showFiltersMenu },
            ) {
                Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
            }

            IconButton(
                onClick = { showMoreOptionsMenu = !showMoreOptionsMenu },
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }

            FiltersMenu(
                showMenu = showFiltersMenu,
                onDismiss = { showFiltersMenu = false },
            )

            MoreOptionsMenu(
                showMenu = showMoreOptionsMenu,
                showBadgeInfoDialog = {
                    openBadgesInfoDialog()
                },
                openLogsScreen = {
                    openLogsScreen()
                },
                onDismiss = { showMoreOptionsMenu = false },
            )
        },
        colors = TopAppBarColors(
            containerColor = if (!searchActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}