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

@Composable
fun Watermark(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    padding: PaddingValues = PaddingValues(16.dp),
    sizeDp: Int = 100,
    contentDescription: String = "Watermark",
    resId: Int = R.drawable.watermark
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
