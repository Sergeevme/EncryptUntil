import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.CryptomeTheme
import java.awt.FileDialog
import java.io.File

fun main() = application {
    CryptomeTheme {
        Window(onCloseRequest = ::exitApplication, title = "EncryptUntil") {
            app()
        }
    }
}

@Composable
@Preview
fun app() {

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
            }
        }) {
            Text(textBtn)
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
