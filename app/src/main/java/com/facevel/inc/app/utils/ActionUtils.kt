package com.facevel.inc.app.utils

import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.facevel.inc.app.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.*


object ActionUtils {
    private val LOG_TAG = "Network Check"

    fun copyToClipboard(message: String, context: Context) {
        try {
            val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, message)
            clipBoard.setPrimaryClip(clipData)
        } catch (e: Exception) {
            Log.i(TAG, "copyToClipboard: $e")
        }
    }

    fun String.copyText(context: Context) {
        try {
            val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, this)
            clipBoard.setPrimaryClip(clipData)
        } catch (e: Exception) {
            Log.i(TAG, "copyToClipboard: $e")
        }
    }

    open fun showToast(message: String, context: Context, isLong: Boolean = false) {
        val length = when (isLong) {
            true -> Toast.LENGTH_LONG
            false -> Toast.LENGTH_SHORT
        }
        Toast.makeText(context, message, length).show()
    }

    fun String.showSnackbar(view: View, durationLong: Boolean) {
        val duration = when (durationLong) {
            true -> Snackbar.LENGTH_LONG
            false -> Snackbar.LENGTH_SHORT
        }
        val mySnackbar = Snackbar.make(
            view,
            this, duration
        )

        mySnackbar.let {
            /*    mySnackbar.setAction("Open Termux", MyUndoListener())
                mySnackbar.setActionTextColor(view.context.resources.getColor(R.color.colorAccent))*/

            it.setTextColor(ContextCompat.getColor(view.context, R.color.snackbar_text))
            val viewSnackbar = it.view
            val textSnackbar =
                viewSnackbar.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textSnackbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            it.setBackgroundTint(ContextCompat.getColor(view.context, R.color.snackbar_bg))
            it.show()
        }
    }

    fun String.showSnackbarWithAction(
        action: () -> Unit,
        buttonTitle: String,
        view: View,
        durationLong: Boolean
    ) {
        val duration = when (durationLong) {
            true -> Snackbar.LENGTH_LONG
            false -> Snackbar.LENGTH_SHORT
        }
        val mySnackbar = Snackbar.make(
            view,
            this, duration
        )

        mySnackbar.let {
            mySnackbar.setAction(buttonTitle) { action() }
            mySnackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.colorAccent))

            it.setTextColor(ContextCompat.getColor(view.context, R.color.snackbar_text))
            val viewSnackbar = it.view
            val textSnackbar =
                viewSnackbar.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textSnackbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            it.setBackgroundTint(ContextCompat.getColor(view.context, R.color.snackbar_bg))
            it.show()
        }
    }

    fun launchSendEmailIntent(
        subject: String,
        body: String,
        context: Context,
        emails: Array<String>
    ) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, emails)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(
                    Intent.EXTRA_TEXT,
                    body
                )
                context.startActivity(Intent.createChooser(emailIntent, "Send us message via..."))
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                context,
                "No email clients found on device. Please install one to continue.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun String.showNormalSnackbar(view: View, durationLong: Boolean) {
        val duration = when (durationLong) {
            true -> Snackbar.LENGTH_LONG
            false -> Snackbar.LENGTH_SHORT
        }
        val snackbar = Snackbar.make(
            view,
            this, duration
        )
        snackbar.apply {
            setBackgroundTint(R.color.cardBackgroundColorCustom.getColor(view.context))
            setTextColor(R.color.primaryTextColorDark.getColor(view.context))
            this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines =
                5
            show()
        }

    }


    open fun showTermuxOpeningSnackBar(view: View, context: Context) {
        val mySnackbar = Snackbar.make(
            view.rootView,
            "Command Copied!", Snackbar.LENGTH_LONG
        )

        val openTermuxClickListener = View.OnClickListener {
            if (checkTermux(context)) {
                context.startActivity(context.packageManager.getLaunchIntentForPackage("com.termux"))
            } else {
                val appPackageName =
                    "com.termux" // getPackageName() from Context or Activity object
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
        }



        mySnackbar.let {
            mySnackbar.setAction("Open Termux", openTermuxClickListener)
            mySnackbar.setActionTextColor(context.resources.getColor(R.color.colorAccent))
            it.setTextColor(ContextCompat.getColor(context, R.color.snackbar_text))
            it.setBackgroundTint(ContextCompat.getColor(context, R.color.snackbar_bg))
        }

        mySnackbar.show()
    }

    private fun checkTermux(context: Context): Boolean {
        val appsInfo = context.packageManager.getInstalledApplications(0)
        for (appInfo in appsInfo) {
            if (appInfo.packageName.contains("com.termux")) return true
        }
        return false
    }

    val CPUArch: String
        get() {
            for (androidArch in Build.SUPPORTED_ABIS) {
                when (androidArch) {
                    "arm64-v8a" -> return "aarch64"
                    "armeabi-v7a" -> return "arm"
                    "x86_64" -> return "x86_64"
                    "x86" -> return "i686"
                }
            }
            throw RuntimeException(
                "Unable to determine arch from Build.SUPPORTED_ABIS =  " +
                        Arrays.toString(Build.SUPPORTED_ABIS)
            )
        }

    open fun isConnected(): Boolean {
        var inetAddress: InetAddress? = null
        try {
            val future: Future<InetAddress?> = Executors.newSingleThreadExecutor().submit(Callable {
                try {
                    return@Callable InetAddress.getByName("google.com")
                } catch (e: UnknownHostException) {
                    return@Callable null
                }
            })
            inetAddress = future[5000, TimeUnit.MILLISECONDS]
            future.cancel(true)
        } catch (e: InterruptedException) {
            Log.i(TAG, "isConnected: " + System.err)
        } catch (e: ExecutionException) {
            Log.i(TAG, "isConnected: " + System.err)
        } catch (e: TimeoutException) {
            Log.i(TAG, "isConnected: " + System.err)
        }
        return inetAddress != null && inetAddress.toString() != ""
    }

    open fun showInternetToast(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            Toast.makeText(
                context,
                "Please connect your device to internet. If you're already connected then switch to mobile data or Wi-Fi.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    open fun getBrowser(context: Context, link: String?) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(browserIntent)
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                context,
                "No browser found. Please install one to continue.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

/* open fun showNoConnection(context: Context) {
     Alerter.create((context as Activity?)!!)
             .setTitle(R.string.no_connection)
             .setText(R.string.features_limited)
             .setDuration(10000)
             .setIcon(R.drawable.ic_baseline_signal_wifi_off_24)
             .setBackgroundColorRes(R.color.red)
             .show()
 }*/

    fun String.openYoutubeLink(context: Context) {
        try {
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$this"))
            val intentBrowser =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$this"))
            try {
                context.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                context.startActivity(intentBrowser)
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "We could not launch the Youtube app or a browser. Please try after installing one.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    open fun isConnected(timeOut: Int): Boolean {
        var iNetAddress: InetAddress? = null
        try {
            val future = Executors.newSingleThreadExecutor().submit<InetAddress>(Callable {
                try {
                    return@Callable InetAddress.getByName("google.com")
                } catch (e: UnknownHostException) {
                    return@Callable null
                }
            })
            iNetAddress = future[timeOut.toLong(), TimeUnit.MILLISECONDS]
            future.cancel(true)
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        } catch (e: TimeoutException) {
        }
        return iNetAddress != null && iNetAddress.toString().isNotEmpty()
    }

    fun isTermuxInstalled(packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo("com.termux", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


//    @SuppressLint("InflateParams")
//    open fun showPurchaseFailed(type: String, context: Context, code: Int) {
//        val dialog = RoundedBottomSheetDialog(context)
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.premium_purchase_failed_bottom_sheet, null, false)
//        dialog.setContentView(view)
//        dialog.show()
//
//        view.close_failed.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        view.email_button_failed_payment.setOnClickListener {
//            val subject = "Payment Failed | $type [$code]"
//            val message = "Payment Failed \n Time - ${getDate()}"
//            sendEmailIntent(
//                subject = subject,
//                message = message,
//                context = context,
//                email = "support@andronix.app"
//            )
//            dialog.dismiss()
//        }
//
//    }

//    @SuppressLint("InflateParams")
//    open fun showFraudPurchase(type: String, context: Context, code: Int) {
//        val dialog = RoundedBottomSheetDialog(context)
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.premium_purchase_failed_bottom_sheet, null, false)
//        dialog.setContentView(view)
//        dialog.show()
//
//        view.server_picture.visibility = View.VISIBLE
//        view.purchase_error_lottie.visibility = View.GONE
//        view.purchase_error_text.text = context.getString(R.string.purchase_unverified)
//        view.close_failed.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        view.email_button_failed_payment.setOnClickListener {
//            val subject = "Payment Unverified | $type [$code]"
//            val message = "Payment Unverified \n Time - ${getDate()}"
//            sendEmailIntent(
//                subject = subject,
//                message = message,
//                context = context,
//                email = "support@andronix.app"
//            )
//            dialog.dismiss()
//        }
//
//    }


    private fun getDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        return formatter.format(date)
    }

    private fun sendEmailIntent(email: String, subject: String, message: String, context: Context) {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            showToast("Please install an email app on your device", context)
        }
    }
//
//    open fun showServerUnavailableSheet(context: Context) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val dialog = RoundedBottomSheetDialog(context)
//            val inflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val view = inflater.inflate(R.layout.server_error_bottomsheet, null, false)
//            dialog.setContentView(view)
//            dialog.show()
//
//            view.close_server_error.setOnClickListener {
//                dialog.dismiss()
//            }
//            view.status_check_button.setOnClickListener {
//                getBrowser(context, ANDRONIX_STATUS)
//            }
//        }
//    }

/*  open fun checkInternet(context: Context) {
      val dialog = RoundedBottomSheetDialog(context)
      val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      val view = inflater.inflate(R.layout.no_connectdion_bottomsheet, null, false)
      dialog.setContentView(view)

      view.close.setOnClickListener {
          dialog.dismiss()
      }

      GlobalScope.launch(Dispatchers.IO) {
          val isConnected = async { checkActiveInternetConnection(context) }.await()
          GlobalScope.launch(Dispatchers.Main) {
              when (isConnected) {
                  false -> {
                      dialog.show()
                  }
              }
          }
      }

  }

  open fun checkActiveInternetConnection(context: Context): Boolean {
      return if (context.isNetworkAvailable()) {
          try {
              val urlc: HttpURLConnection = URL("http://www.google.com").openConnection() as HttpURLConnection
              urlc.setRequestProperty("User-Agent", "Test")
              urlc.setRequestProperty("Connection", "close")
              urlc.connectTimeout = 1500
              urlc.connect()
              return urlc.responseCode == 200
          } catch (e: IOException) {
              false
          }
      } else {
          false
      }
  }

  open fun Context.isNetworkAvailable(): Boolean {
      val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
      val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
      return activeNetworkInfo != null
  }*/

    fun shareApp(context: Context) {
        val packageName = context.packageName
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val postData =
            "Install your favourite Linux Distro without root on your Android Device. Download the Andronix app now from the Google Play Store https://play.google.com/store/apps/details?id=$packageName"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Now!")
        shareIntent.putExtra(Intent.EXTRA_TEXT, postData)
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Show us love Via"))
    }

//
//    suspend fun InputStream.getMD5FromInputStream(context: Context): String? {
//        var hash: String? = null
//        try {
//            val outputDir = context.cacheDir
//            val outputFile = withContext(Dispatchers.IO) {
//                File.createTempFile("andronix_", ".apk", outputDir)
//            }
//
//            this.use { input ->
//                outputFile.outputStream().use { output ->
//                    input.copyTo(output)
//                }
//            }
//            if (outputFile.length() > 0) {
//                hash = outputFile.getMD5Hash()
//            }
//            return hash
//        } catch (e: Exception) {
//            println("Exception occurred $e")
//            return null
//        }
//    }

    fun String.matchMD5HashTo(hash2: String): Boolean {
        return this == hash2
    }

    fun String.log() {
        Log.i("ANDRONIX LOGGER", this)
    }

    /*  fun createNotificationChannel(context: Context) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              val serviceChannel = NotificationChannel(
                      CHANNEL_ID,
                      "Andronix Progress Notifications",
                      NotificationManager.IMPORTANCE_DEFAULT
              )
              val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
              manager.createNotificationChannel(serviceChannel)
          }
      }*/

}

fun Int.getColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

interface DrawerLocker {
    fun setDrawerEnabled(enabled: Boolean)
}


