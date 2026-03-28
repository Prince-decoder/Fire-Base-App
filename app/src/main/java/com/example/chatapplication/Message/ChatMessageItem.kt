package com.example.chatapplication.Message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapplication.R


@Composable
fun chatMessageItem(message: Message, isowner: Boolean)
{
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = if (isowner)
        {
            Alignment.End
        }else{
            Alignment.Start})
    {
        Box(modifier = Modifier.background(
            if (isowner)
            {
                colorResource(R.color.purple_500)
            }
            else{
                colorResource(R.color.teal_700)
            },
            shape = RoundedCornerShape(8.dp)
        ).padding(8.dp))
        {
            Text(text = message.text,
                color = Color.White,
                fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.senderFirstName,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}