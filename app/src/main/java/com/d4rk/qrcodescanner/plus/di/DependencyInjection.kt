package com.d4rk.qrcodescanner.plus.di
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.d4rk.qrcodescanner.plus.QrCodeScanner
import com.d4rk.qrcodescanner.plus.usecase.BarcodeImageScanner
import com.d4rk.qrcodescanner.plus.usecase.BarcodeSaver
import com.d4rk.qrcodescanner.plus.usecase.BarcodeImageGenerator
import com.d4rk.qrcodescanner.plus.usecase.BarcodeImageSaver
import com.d4rk.qrcodescanner.plus.usecase.BarcodeParser
import com.d4rk.qrcodescanner.plus.usecase.WifiConnector
import com.d4rk.qrcodescanner.plus.usecase.OTPGenerator
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.usecase.BarcodeDatabaseFactory
import com.d4rk.qrcodescanner.plus.usecase.ContactHelper
import com.d4rk.qrcodescanner.plus.usecase.PermissionsHelper
import com.d4rk.qrcodescanner.plus.usecase.RotationHelper
import com.d4rk.qrcodescanner.plus.usecase.ScannerCameraHelper
val QrCodeScanner.settings get() = Settings.getInstance(applicationContext)
val barcodeParser get() = BarcodeParser
val barcodeImageScanner get() = BarcodeImageScanner
val barcodeImageGenerator get() = BarcodeImageGenerator
val barcodeSaver get() = BarcodeSaver
val barcodeImageSaver get() = BarcodeImageSaver
val wifiConnector get() = WifiConnector
val otpGenerator get() = OTPGenerator
val AppCompatActivity.barcodeDatabase get() = BarcodeDatabaseFactory.getInstance(this) // FIXME: Unresolved reference 'getInstance'.
val AppCompatActivity.settings get() = Settings.getInstance(this)
val contactHelper get() = ContactHelper
val permissionsHelper get() = PermissionsHelper
val rotationHelper get() = RotationHelper
val scannerCameraHelper get() = ScannerCameraHelper
val Fragment.barcodeDatabase get() = BarcodeDatabaseFactory.getInstance(requireContext()) // FIXME: Unresolved reference 'getInstance'.
val Fragment.settings get() = Settings.getInstance(requireContext())