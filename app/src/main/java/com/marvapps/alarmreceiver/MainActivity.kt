package com.marvapps.alarmreceiver

import android.Manifest
import android.app.Notification.Action
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import xyz.kumaraswamy.autostart.Autostart
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    lateinit var permission: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }





        val enabled: Boolean = Autostart.getSafeState(this)

        if (isXiaomiDevice()) {
            if (!enabled) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("You Are Using a Xiaomi Device")
                alert.setIcon(R.drawable.ic_launcher_foreground)
                alert.setMessage("" +
                        "This application requires the auto-Start permission to work properly on Xiaomi devices.")
                alert.setPositiveButton("Enable Auto-Start",
                    DialogInterface.OnClickListener { dialog, which ->
                        val intent = Intent().setComponent(
                            ComponentName(
                                "com.miui.securitycenter",
                                "com.miui.permcenter.autostart.AutoStartManagementActivity"
                            )
                        )
                        startActivity(intent)
                    })
                alert.create().show()

            }
        }
    }


    fun isXiaomiDevice(): Boolean {
        return Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true)
    }


    fun checkPermission(view: View): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                Snackbar.make(view, "We Need Permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction("okey", {
                        permission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }).show()
            } else {
                permission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            return false
        }
        return true
    }


    fun createAlarm(view: View) {

        if (!checkPermission(view)) {
            return
        }

        val txtCode = findViewById<EditText>(R.id.txtCode).text.toString().toIntOrNull()
        val txtTime = findViewById<EditText>(R.id.txtTime).text.toString().toLongOrNull()

        txtCode.let { code ->
            txtTime.let { zaman ->

                try {
                    val time = LocalDateTime.now().plusSeconds(zaman!!)
                    val alarm = AlarmScheduler(this)

                    alarm.schedule(time, code!!)

                    Toast.makeText(this, "Succesfully Added ${code}", Toast.LENGTH_LONG).show()

                } catch (e: Exception) {
                    Toast.makeText(this, "Error ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun cancelAlarm(view: View) {
        if (!checkPermission(view)) {
            return
        }
        try {
            val txtCode = findViewById<EditText>(R.id.txtCode).text.toString().toIntOrNull()

            txtCode.let {
                val alarm = AlarmScheduler(this)
                alarm.cancel(it!!)

                Toast.makeText(this, "Succesfully Deleted", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}