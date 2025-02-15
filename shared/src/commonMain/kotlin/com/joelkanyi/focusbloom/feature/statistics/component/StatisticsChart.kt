/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.feature.statistics.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.core.utils.aAllEntriesAreZero
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.TickPosition
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import io.github.koalaplot.core.xygraph.rememberFloatLinearAxisModel

internal val padding = 8.dp
private const val BarWidth = 0.8f

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        label,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
fun BarChart(tickPositionState: TickPositionState, entries: List<Float>) {
    val yAxisRange = 0f..if (entries.aAllEntriesAreZero()) 20f else entries.maxOf { it }
    val xAxisRange = 0.5f..7.5f

    XYGraph(
        xAxisModel = rememberFloatLinearAxisModel(
            xAxisRange,
            minimumMajorTickIncrement = 1f,
            minimumMajorTickSpacing = 10.dp,
            minorTickCount = 0,
        ),
        yAxisModel = rememberFloatLinearAxisModel(
            yAxisRange,
            minimumMajorTickIncrement = 1f,
            minorTickCount = 0,
        ),
        xAxisStyle = rememberAxisStyle(
            tickPosition = tickPositionState.horizontalAxis,
        ),
        xAxisLabels = {
            AxisLabel(
                when (it) {
                    1f -> "Mon"
                    2f -> "Tue"
                    3f -> "Wed"
                    4f -> "Thu"
                    5f -> "Fri"
                    6f -> "Sat"
                    7f -> "Sun"
                    else -> ""
                },
                Modifier.padding(top = 2.dp),
            )
        },
        xAxisTitle = {
            AxisTitle(
                "Day of the Week",
                modifier = Modifier
                    .padding(top = 8.dp),
            )
        },
        yAxisStyle = rememberAxisStyle(tickPosition = tickPositionState.verticalAxis),
        yAxisLabels = {
            AxisLabel(it.toString(0), Modifier.absolutePadding(right = 2.dp))
        },
        yAxisTitle = {
            AxisTitle(
                "Tasks Completed",
                modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                    .padding(bottom = padding),
            )
        },
        verticalMajorGridLineStyle = null,
    ) {
        VerticalBarPlot(
            xData = (1..7).map { it.toFloat() }, // Days of the week
            yData = entries,
            bar = { value ->
                DefaultVerticalBar(
                    brush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(BarWidth),
                ) {
                    HoverSurface { Text(value.toString()) }
                }
            },
        )
    }
}

data class TickPositionState(
    val verticalAxis: TickPosition,
    val horizontalAxis: TickPosition,
)

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.LightGray,
        modifier = modifier.padding(padding),
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
