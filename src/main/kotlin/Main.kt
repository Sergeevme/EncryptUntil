import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.Color500
import theme.CryptomeTheme
import java.awt.FileDialog
import java.io.File


fun main() = application {
    CryptomeTheme {
        Window(onCloseRequest = ::exitApplication, title = "EncryptUntil",
            icon = painterResource("fingerprint.png"), resizable = false) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                app()
            }
        }
    }
}

@Composable
fun image() {
    Card(
        modifier = Modifier.size(100.dp),
        shape = CircleShape
    ) {
        Image(
            painterResource("fingerprint.png"),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun app() {

    // this is test commit
    val textBtn by remember { mutableStateOf("Load the file") }
    var filename by remember { mutableStateOf( "You haven't chosen any file") }
    var file : Set<File>

    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Encrypt", "Decrypt")
    var selectedIndex by remember { mutableStateOf(0) }

    var visibleHelper by remember { mutableStateOf(true) }
    var textHelper by remember { mutableStateOf("Fill in the fields, then click Start") }

    Column(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        image()

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(onClick = {
            file = openFileDialog(window = ComposeWindow(), title = "Choose file", allowedExtensions =
                        listOf("txt", "7z"), allowMultiSelection = true)

            if (file.isNotEmpty()) {
                filename = file.elementAt(0).toString()
            }
        },
            shape = CircleShape,
            border= BorderStroke(1.dp, Color500),
            contentPadding = PaddingValues(10.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color500)
        ) {
            Text(filename)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(modifier = Modifier.width(90.dp).wrapContentSize(Alignment.TopStart)) {
            //Text(items[selectedIndex], modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(Color.White))

            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.height(40.dp).width(90.dp),  //avoid the oval shape
                shape = CircleShape,
                border = BorderStroke(1.dp, Color500),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color500),
            ) {
                //Icon(Icons.Default.Add, contentDescription = "content description")
                Text(items[selectedIndex])
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(90.dp).background(Color500),
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = s, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        var passwordOne by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = passwordOne,
            onValueChange = {
                if (it.length <= 30) passwordOne = it
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        var passwordRepeat by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = passwordRepeat,
            onValueChange = {
                if (it.length <= 30) passwordRepeat = it
            },
            label = { Text("Repeat your password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier.width(150.dp),
            onClick = {
                if (passwordRepeat == passwordOne) {
                    val bool = cryptFile(filename, items[selectedIndex], passwordOne)
                    if (bool) {
                        textHelper = "The file has been modified"
                        passwordRepeat = ""
                        passwordOne = ""
                        filename = "You haven't chosen any file"
                    }
                    if (!bool) textHelper = "Fail"
                } else {
                    textHelper = "The passwords do not match"
                }
                      },
            shape = CircleShape
        ){
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = visibleHelper,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            // Content that needs to appear/disappear goes here:
            Text(text = textHelper, color = Color500, fontSize = 14.sp, fontStyle = FontStyle.Italic)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

fun cryptFile(filename: String, mode: String, password: String): Boolean {
    if (filename == "You haven't chosen any file") return false
    return try {
        if (mode == "Encrypt") CryptoYou.encryptFile(filename, password)
        else if (mode == "Decrypt") CryptoYou.decryptFile(filename, password)
        true
    } catch (ignored: NoSuchFileException) {
        false
    }
}

@Composable
fun dropDownMenu() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Encrypt", "Decrypt")
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.width(100.dp).wrapContentSize(Alignment.TopStart)) {
            //Text(items[selectedIndex], modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(Color.White))

            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.height(40.dp).width(90.dp),  //avoid the oval shape
                shape = CircleShape,
                border = BorderStroke(1.dp, Color500),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color500),
            ) {
                //Icon(Icons.Default.Add, contentDescription = "content description")
                Text(items[selectedIndex])
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(90.dp).background(Color500),
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = s, color = Color.White)
                    }
                }
            }
        }
    }
}

fun openFileDialog(window: ComposeWindow, title: String, allowedExtensions: List<String>, allowMultiSelection: Boolean): Set<File> {
    return FileDialog(window, title, FileDialog.LOAD).apply {
        isMultipleMode = allowMultiSelection

        // windows
        file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

        // linux
        setFilenameFilter { _, name ->
            allowedExtensions.any {
                name.endsWith(it)
            }
        }

        isVisible = true
    }.files.toSet()
}
