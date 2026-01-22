package com.clmte_exe.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class DesktopActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desktop)

        val gridView = findViewById<GridView>(R.id.desktop_grid)
        gridView.adapter = DesktopAppAdapter(desktopApps)
    }

    private inner class DesktopAppAdapter(val apps: List<AppConfig>) : BaseAdapter() {
        override fun getCount(): Int = apps.size
        override fun getItem(position: Int): Any = apps[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@DesktopActivity)
                .inflate(R.layout.item_desktop_icon, parent, false)
            
            val app = apps[position]
            val iconView = view.findViewById<ImageView>(R.id.app_icon)
            val labelView = view.findViewById<TextView>(R.id.app_label)

            labelView.text = app.label
            
            when (val icon = app.icon) {
                is Int -> iconView.setImageResource(icon)
                else -> iconView.setImageResource(android.R.drawable.sym_def_app_icon)
            }

            view.setOnClickListener {
                //Toast.makeText(this@DesktopActivity, "Opening ${app.label}...", Toast.LENGTH_SHORT).show()
                // In a full implementation, you'd show a Dialog with Win98 style here
                when (app.label) {
                    "My Car" -> {
                        // Direct this to MyCarActivity.kt
                        val intent = Intent(this@DesktopActivity, MyCarActivity()::class.java)
                        startActivity(intent)
                    }
                    "Settings" -> {
                        // Todo: Add Settings Activity
                        // Direct this to SettingsActivity.kt
                        val intent = Intent(this@DesktopActivity, SettingsActivity()::class.java)
                        startActivity(intent)

                    }
                }
            }

            return view
        }
    }
}

data class AppConfig(
    val label: String,
    val icon: Any
)

val desktopApps = listOf(
    AppConfig("My Car", R.drawable.ic_launcher_foreground), //Todo: Add Icon
    AppConfig("Settings", android.R.drawable.ic_menu_preferences) //Todo: Add Icon
)
