package com.metrolauncher.ui.actioncenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Action Center — MetroV offers one on swipe-right from Start with quick
 * toggles. This MVP renders the toggle UI; wiring each toggle to the real
 * system service is roadmap work (Wi-Fi/Bluetooth toggles need runtime
 * permissions and are restricted on Android 10+; flashlight needs the
 * camera permission).
 */
@Composable
fun ActionCenterScreen(
    modifier: Modifier = Modifier,
) {
    var wifi by remember { mutableStateOf(false) }
    var bluetooth by remember { mutableStateOf(false) }
    var flashlight by remember { mutableStateOf(false) }
    var dnd by remember { mutableStateOf(false) }
    var airplane by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = "action center",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        ToggleRow(
            icon = Icons.Filled.Wifi,
            title = "wi-fi",
            checked = wifi,
            onCheckedChange = {
                wifi = it
                // TODO: toggle via WifiManager (needs CHANGE_WIFI_STATE; on
                // Android 10+ apps can only open the settings panel).
            },
        )
        ToggleRow(
            icon = Icons.Filled.Bluetooth,
            title = "bluetooth",
            checked = bluetooth,
            onCheckedChange = {
                bluetooth = it
                // TODO: toggle via BluetoothAdapter (needs BLUETOOTH_CONNECT).
            },
        )
        ToggleRow(
            icon = Icons.Filled.FlashlightOn,
            title = "flashlight",
            checked = flashlight,
            onCheckedChange = {
                flashlight = it
                // TODO: toggle via CameraManager.setTorchMode (needs CAMERA).
            },
        )
        ToggleRow(
            icon = Icons.Filled.DoNotDisturbOn,
            title = "do not disturb",
            checked = dnd,
            onCheckedChange = {
                dnd = it
                // TODO: toggle via NotificationManager (needs ACCESS_NOTIFICATION_POLICY).
            },
        )
        ToggleRow(
            icon = Icons.Filled.AirplanemodeActive,
            title = "airplane mode",
            checked = airplane,
            onCheckedChange = {
                airplane = it
                // TODO: airplane mode is read-only for third-party apps;
                // deep-link to Settings instead.
            },
        )
    }
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 16.dp),
        )
        Text(
            text = title,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
