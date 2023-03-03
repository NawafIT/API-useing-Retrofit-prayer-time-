package com.api.prayerapi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract.DisplayPhoto
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.api.prayerapi.mvvm.TimingViewModel
import com.api.prayerapi.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrayerApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MyApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val isNetworkAvailable = connectivityManager?.activeNetworkInfo?.isConnected ?: false


    if (isNetworkAvailable) {
        // Your app content goes here
        Greeting()
    } else {
        /* Show a Snackbar with a message and action to take user to Wi-Fi settings */
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "No internet connection",
                actionLabel = "Turn on Wi-Fi",
                duration = SnackbarDuration.Short
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { snackbarData ->
                Snackbar(
                    action = {
                        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                        context.startActivity(intent)
                        snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = snackbarData.message)
                }
            }
        )
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Greeting() {

    val viewModel: TimingViewModel = viewModel()
    val myObject by viewModel.myObject.observeAsState()
    Scaffold(
        topBar = { TopAppOne() }, backgroundColor = Teal22
    ) {
        if (myObject == null) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = blue22)
            }

        } else {
            val list = listOf(
                myObject!!.data.timings.Fajr,
                myObject!!.data.timings.Dhuhr,
                myObject!!.data.timings.Asr,
                myObject!!.data.timings.Maghrib,
                myObject!!.data.timings.Isha
            )
            val listNames = listOf(
                "الفجر",
                "الظهر",
                "العصر",
                "المغرب",
                "العشاء"
            )
            val listPhoto = listOf(
                R.drawable.sunrise,
                R.drawable.sun,
                R.drawable.sun,
                R.drawable.sunrise,
                R.drawable.moon
            )



            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    viewModel.ChangeTheCity(menu())
                }
                Card(
                    backgroundColor = blue33,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .fillMaxHeight(0.9f)

                ) {
                    LazyColumn {
                        itemsIndexed(list) { index, time ->
                            Display(time, listPhoto[index], listNames[index])
                        }
                    }
                }

            }

        }


    }


}

@Composable
fun TopAppOne() {
    TopAppBar(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 10.dp,
                bottom = 64.dp,
                end = 16.dp
            )
            .clip(RoundedCornerShape(16.dp)),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(), Alignment.Center
            ) {
                Text(
                    text = "أذان",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.tajawal))

                )
            }
        },
        backgroundColor = Teal2
    )
}


@Composable
fun Display(time: String, photo: Int, name: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = 450.dp, height = 95.dp)
            .padding(start = 14.dp, end = 14.dp, top = 16.dp),
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.background(back2)

        ) {
            Column(
                modifier = Modifier
                    .padding(start = 45.dp)
            ) {
//                Image(
//                    painter = painterResource(id = photo),
//                    contentDescription = "Photo",
//                    modifier = Modifier.size(35.dp)
//                )
                ImageAnimation(photo)
                RotatingImage(photo)
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Column {
                Text(
                    text = convertTimeTo12HourFormat(time), fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.tajawal))
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 32.dp))
            Column {
                Text(
                    name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.tajawal))

                )
            }
        }
    }
}

fun convertTimeTo12HourFormat(time24: String): String {
    val timeSplit = time24.split(":")
    var hour = timeSplit[0].toInt()
    val minute = timeSplit[1]
    val suffix = if (hour >= 12) "مـ" else "صـ"

    if (hour == 0) {
        hour = 12
    } else if (hour > 12) {
        hour -= 12
    }

    return "${hour}:${minute} ${suffix}"
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun menu(): String {
    val listItems = arrayOf("جدة", "الرياض","المدينة المنورة", "الشرقية")

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        OutlinedTextField(
            modifier = Modifier
                .width(180.dp)
                .padding(horizontal = 4.dp),
            textStyle = TextStyle(fontFamily = FontFamily((Font(R.font.tajawal)))),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = blue3,
                focusedBorderColor = blue2,
                trailingIconColor = blue2,
                focusedTrailingIconColor = blue3,
                textColor = blue22
            )
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    expanded = false
                }) {
                    Text(
                        text = selectedOption,
                        color = Color.DarkGray,
                        fontFamily = FontFamily(Font(R.font.tajawal))
                    )
                }
            }
        }
    }
    return selectedItem
}


@Composable
fun RotatingImage(photo: Int) {
    if (photo == R.drawable.sun) {
        var degrees by remember { mutableStateOf(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(30) // adjust this value to change the speed of rotation
                degrees += 1f
            }
        }
        AnimatedVisibility(
            visible = true, // change this to control the visibility of the image
            enter = fadeIn() + expandIn(),
            exit = shrinkOut()
        ) {
            Image(
                painter = painterResource(id = photo),
                contentDescription = null,
                modifier = Modifier
                    .rotate(degrees)
                    .size(35.dp)
                    .alpha(0.8f)
            )
        }
    }
}

@Composable
fun ImageAnimation(photo: Int) {
    if (photo != R.drawable.sun) {
        var visible by remember { mutableStateOf(false) }
        val transition = updateTransition(targetState = visible, label = "Image Transition")
        val translateY by transition.animateDp(
            label = "Translate Y",
            transitionSpec = {
                if (false isTransitioningTo true) {
                    tween(durationMillis = 600)
                } else {
                    tween(durationMillis = 500, delayMillis = 1000)
                }
            }
        ) {
            if (it) 3.dp else (-3).dp
        }

        LaunchedEffect(Unit) {
            while (true) {
                visible = !visible
                delay(2000)
            }
        }

        Image(
            painter = painterResource(id = photo),
            contentDescription = "Animated Image",
            modifier = Modifier
                .size(35.dp)
                .offset(y = translateY)
                .alpha(0.8f)
        )
    }

}




