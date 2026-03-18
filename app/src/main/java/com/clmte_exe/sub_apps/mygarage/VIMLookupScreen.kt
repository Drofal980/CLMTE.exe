package com.clmte_exe.sub_apps.mygarage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clmte_exe.app.api_calls.NhtsaApi
import kotlinx.coroutines.launch

@Composable
fun VIMLookupScreen(
    onVehicleFound: (Vehicle) -> Unit,
    onCancel: () -> Unit
) {
    var vin by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var foundVehicle by remember { mutableStateOf<Vehicle?>(null) }
    
    val scope = rememberCoroutineScope()
    val api = remember { NhtsaApi() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Win98Gray)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Enter VIN Number:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Win98Black
        )

        Win98TextField(
            value = vin,
            onValueChange = { 
                vin = it 
                foundVehicle = null // Reset if VIN changes
            },
            placeholder = "17-character VIN"
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        if (foundVehicle != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .win98Border(pressed = true)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Vehicle Found:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Win98Blue
                    )
                    Text(text = "Year: ${foundVehicle!!.year}", fontSize = 13.sp, color = Win98Black)
                    Text(text = "Make: ${foundVehicle!!.make}", fontSize = 13.sp, color = Win98Black)
                    Text(text = "Model: ${foundVehicle!!.model}", fontSize = 13.sp, color = Win98Black)
                    Text(text = "Type: ${foundVehicle!!.vehicle_type}", fontSize = 13.sp, color = Win98Black)
                    Text(text = "Drive: ${foundVehicle!!.drive_type}", fontSize = 13.sp, color = Win98Black)
                    Text(text = "Trans: ${foundVehicle!!.transmission}", fontSize = 13.sp, color = Win98Black)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Win98Blue
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            Win98Button(text = "Cancel", onClick = onCancel)
            
            if (foundVehicle == null) {
                Win98Button(
                    text = "Find Car",
                    enabled = vin.length >= 11 && !isLoading,
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val vehicleData = api.fetchVehicle(vin)
                                
                                foundVehicle = Vehicle(
                                    make = vehicleData.getMake() ?: "",
                                    model = vehicleData.getModel() ?: "",
                                    year = vehicleData.getYear() ?: 0,
                                    vin_number = vin,
                                    vehicle_type = vehicleData.getBodyClass() ?: "Sedan",
                                    drive_type = vehicleData.getDriveType() ?: "FWD",
                                    transmission = vehicleData.getTransmission() ?: "Automatic"
                                )
                            } catch (e: Exception) {
                                errorMessage = "Error finding vehicle. Please check VIN."
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            } else {
                Win98Button(
                    text = "Continue",
                    onClick = { onVehicleFound(foundVehicle!!) }
                )
            }
        }
    }
}
