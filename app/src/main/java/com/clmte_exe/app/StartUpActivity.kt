package com.clmte_exe.app

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.content.Intent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class StartUpActivity: ComponentActivity() {

    private val setupLogic = SetUpLogic()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        // Need to add the XML (activity_startup is just example)
        setContentView(R.layout.activity_startup)

        // Setting up the var using either VIN or Manual
        val setupVinBut = findViewById<Button>(R.id.btnVinSetup)
        val setupManBut = findViewById<Button>(R.id.btnManualSetup)

        // When button clicked it will go to vin or manual.
        setupVinBut.setOnClickListener{
            vinSetup()
        }

        setupManBut.setOnClickListener {
            manualSetup()
        }
    }

    // Function for the vin to do the setup
    private fun vinSetup()
    {

        // Where to put the actual XML (activity_vin_setup is an example)
        setContentView(R.layout.activity_vin_setup)

        // Variables for the input to connect to the button or text.
        val nicknameInput = findViewById<EditText>(R.id.inputNickname)
        val vinInput = findViewById<EditText>(R.id.inputVin)
        val submitBut = findViewById<Button>(R.id.submitVin)

        submitBut.setOnClickListener {
            val nickname = nicknameInput.text.toString()
            val vin = vinInput.text.toString()

            val finish = setupLogic.setupWithVin(
                nickName = nickname,
                carVin = vin
            )

            // If it is successful then we will save the data... hasn't been found yet
            finish.onSuccess { car ->
                Log.d("SETUP", "VIN setup successful!")
                Toast.makeText(this, "Car added!", Toast.LENGTH_SHORT).show()
                // Need to put a nav to the main screen for the user.
                // Need to save the data somewhere.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.onFailure { error ->
                showError(error.message)
            }
        }
    }

    // The manual setup.
    private fun manualSetup() {

        // Where to put the actual XML (activity_manual_setup is an example)
        setContentView(R.layout.activity_manual_setup)


        // Variables to interact with the XML
        val nicknameInput = findViewById<EditText>(R.id.inputNickname)
        val makeInput = findViewById<EditText>(R.id.inputMake)
        val modelInput = findViewById<EditText>(R.id.inputModel)
        val yearInput = findViewById<EditText>(R.id.inputYear)
        val carTypeInput = findViewById<EditText>(R.id.inputCarType)
        val engineInput = findViewById<EditText>(R.id.inputEngineSize)
        val submitBut = findViewById<Button>(R.id.submitManual)

        // When the user interacts with the buttons or edittext
        submitBut.setOnClickListener {
            val nickname = nicknameInput.text.toString()
            val make = makeInput.text.toString()
            val model = modelInput.text.toString()
            val carType = carTypeInput.text.toString()
            val year = yearInput.text.toString().toIntOrNull()
            val engineSize = engineInput.text.toString().toFloatOrNull()


            if (year == null) {
                showError("Year must be entered")
                return@setOnClickListener
            }

            val result = setupLogic.setupManual(
                nickName = nickname,
                carType = carType,
                make = make,
                model = model,
                year = year,
                engineSize = engineSize
            )

            result.onSuccess { car ->
                Log.d("SETUP", "Manual setup successful: $car")
                Toast.makeText(this, "Car added!", Toast.LENGTH_SHORT).show()
                // Need to put a nav to the main screen for the user.
                // Need to save the data somewhere.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }.onFailure { error ->
                showError(error.message)
            }
        }
    }

// Error messages
    private fun showError(message: String?) {
        Toast.makeText(
            this,
            message ?: "Unknown error",
            Toast.LENGTH_LONG
        ).show()
    }

}