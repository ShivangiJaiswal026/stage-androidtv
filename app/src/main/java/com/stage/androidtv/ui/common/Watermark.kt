package com.stage.androidtv.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.stage.androidtv.R

private const val DEFAULT_WATERMARK_DESC = "Watermark"
private val DEFAULT_WATERMARK_PADDING = PaddingValues(16.dp)

private val WATERMARK_RES_ID = R.drawable.watermark

@Composable
fun Watermark(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    padding: PaddingValues = DEFAULT_WATERMARK_PADDING,
    sizeDp: Int = 100,
    contentDescription: String = DEFAULT_WATERMARK_DESC,
    resId: Int = WATERMARK_RES_ID
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = modifier
                .align(alignment)
                .padding(padding)
                .size(sizeDp.dp)
        )
    }
}


