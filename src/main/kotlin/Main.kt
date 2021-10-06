import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.Color500
import theme.CryptomeTheme
import java.awt.FileDialog
import java.io.File

fun main() = application {
    CryptomeTheme {
        Window(onCloseRequest = ::exitApplication, title = "EncryptUntil") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                image()
                Spacer(modifier = Modifier.height(10.dp))
                app()
                Spacer(modifier = Modifier.height(10.dp))
                dropDownMenu()
            }
        }
    }
}

@Composable
fun image() {
    Card(
        modifier = Modifier.size(92.dp),
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

@Composable
fun app() {

    // this is test commit
    val textBtn by remember { mutableStateOf("Load the file") }
    var filename by remember { mutableStateOf( "You haven't chosen any file") }
    var file : Set<File>

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = filename
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            file = openFileDialog(window = ComposeWindow(), title = "Choose file", allowedExtensions =
                        listOf("txt"), allowMultiSelection = true)

            if (file.isNotEmpty()) {
                filename = file.elementAt(0).toString()
                CryptoYou.encryptFile(filename, "LETSGO")
            }

        }) {
            Text(textBtn)
        }
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

            OutlinedButton(onClick = { expanded = true },
                modifier= Modifier.height(50.dp).width(100.dp),  //avoid the oval shape
                shape = CircleShape,
                border= BorderStroke(1.dp, Color500),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color500),
            ) {
                //Icon(Icons.Default.Add, contentDescription = "content description")
                Text(items[selectedIndex])
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(100.dp).background(Color500),
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
