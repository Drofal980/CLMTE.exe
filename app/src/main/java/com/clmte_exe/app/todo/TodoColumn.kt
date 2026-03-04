package com.clmte_exe.app.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TodoColumn(
    title: String,
    appRootCoordinates: LayoutCoordinates?,
    modifier: Modifier = Modifier,
    items: List<TodoItem>,
    onItemClick: (TodoItem) -> Unit,
    onDragStart: (TodoItem, Offset) -> Unit,
    onDrag: (androidx.compose.ui.input.pointer.PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = { it.id }) { item ->
                var itemPosition by remember { mutableStateOf(Offset.Zero) }

                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            if (appRootCoordinates != null) {
                                itemPosition = appRootCoordinates.localPositionOf(coordinates, Offset.Zero)
                            }
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { onDragStart(item, itemPosition) },
                                onDrag = onDrag,
                                onDragEnd = onDragEnd,
                                onDragCancel = onDragEnd
                            )
                        }
                ) {
                    TodoCard(item = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}