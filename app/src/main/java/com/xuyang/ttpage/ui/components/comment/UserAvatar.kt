package com.xuyang.ttpage.ui.components.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * UserAvatar
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
@Composable
fun UserAvatar(
    authorId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val avatarResourceId = remember(authorId) {
        try {
            val resourceName = "avatar_$authorId"
            context.resources.getIdentifier(
                resourceName,
                "drawable",
                context.packageName
            )
        } catch (e: Exception) {
            0
        }
    }
    if (avatarResourceId != 0) {
        Image(
            painter = painterResource(id = avatarResourceId),
            contentDescription = "用户头像",
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "默认头像",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

