package com.d4rk.qrcodescanner.plus.ui.screens.scan

/*
class ScanViewModel(application: Application) : BaseViewModel(application) {

    val dataStore = AppCoreManager.dataStore
    val barCodeDatabase = AppCoreManager.database

    // We start with a single mutable state containing our UiScanScreen
    private val _uiState = mutableStateOf(UiScanScreen())
    val uiState: State<UiScanScreen> get() = _uiState

    // Flows from DataStore
    val isFlashEnabledFlow = dataStore.flash
    val isBackCameraFlow = dataStore.isBackCamera
    val simpleAutoFocusFlow = dataStore.simpleAutoFocus
    val continuousScanningFlow = dataStore.continuousScanning
    val confirmScansManuallyFlow = dataStore.confirmScansManually
    val saveScannedBarcodesToHistoryFlow = dataStore.saveScannedBarcodesToHistory
    val doNotSaveDuplicatesFlow = dataStore.doNotSaveDuplicates

    // If you want to read whether a format is selected
    fun isFormatSelected(format: BarcodeFormat): Flow<Boolean> {
        return dataStore.isFormatSelected(format)
    }

    init {
        viewModelScope.launch {
            // Just as an example, you can read some flows here, if needed.
            // E.g. val isQrCodeSelected = dataStore.isFormatSelected(BarcodeFormat.QR_CODE).first()
        }
    }

    */
/**
     * Called when we decode a new barcode result, used to coordinate the next steps.
     * This is extracted from your decodeCallback in CodeScanner.
     *//*

    fun handleBarcodeResult(
        resultText: String ,
        resultFormat: String ,
        rawBytes: ByteArray? ,
        continuousScanning: Boolean ,
        confirmScansManually: Boolean ,
        saveScannedBarcodesToHistory: Boolean ,
        doNotSaveDuplicates: Boolean ,
        navigateToBarcodeScreen: (Barcode) -> Unit ,
        onShowMessage: (Int) -> Unit
    ) {
        val currentState = uiState.value
        val barcode = barcodeParser.parseResult(
            text = resultText,
            formatString = resultFormat,
            rawBytes = rawBytes
        )

        // If we are continuous scanning and we got the same result => just restart
        if (continuousScanning && barcode.equalTo(currentState.lastResult)) {
            restartPreviewWithDelay(
                showMessage = false,
                onShowMessage = onShowMessage
            )
            return
        }

        vibrateIfNeeded() // if you have a separate method

        when {
            confirmScansManually -> {
                // show dialog
                _uiState.value = currentState.copy(
                    showConfirmationDialog = true,
                    scannedBarcode = barcode
                )
            }

            saveScannedBarcodesToHistory || continuousScanning -> {
                viewModelScope.launch {
                    try {
                        val id = saveScannedBarcode(barcode, doNotSaveDuplicates)
                        // update lastResult
                        _uiState.value = currentState.copy(lastResult = barcode)

                        if (continuousScanning) {
                            restartPreviewWithDelay(
                                showMessage = true,
                                onShowMessage = onShowMessage
                            )
                        } else {
                            navigateToBarcodeScreen(barcode.copy(id = id))
                        }
                    } catch (e: Exception) {
                        // handle error, e.g. log or show error message
                    }
                }
            }

            else -> {
                // If there's no need to confirm or save, simply navigate
                navigateToBarcodeScreen(barcode)
            }
        }
    }

    */
/**
     * Called when user confirms the barcode in ConfirmBarcodeDialog
     *//*

    fun onConfirm(
        continuousScanning: Boolean,
        doNotSaveDuplicates: Boolean,
        saveScannedBarcodesToHistory: Boolean,
        navigateToBarcodeScreen: (Barcode) -> Unit,
        onShowMessage: (Int) -> Unit
    ) {
        val currentState = uiState.value
        val barcode = currentState.scannedBarcode ?: return

        // Hide dialog
        _uiState.value = currentState.copy(showConfirmationDialog = false)

        if (saveScannedBarcodesToHistory) {
            viewModelScope.launch {
                try {
                    val id = saveScannedBarcode(barcode, doNotSaveDuplicates)
                    if (continuousScanning) {
                        restartPreviewWithDelay(showMessage = true, onShowMessage = onShowMessage)
                    } else {
                        navigateToBarcodeScreen(barcode.copy(id = id))
                    }
                } catch (e: Throwable) {
                    // handle error
                }
            }
        } else {
            // no saving, just navigate
            navigateToBarcodeScreen(barcode)
        }
    }

    */
/**
     * Called when user declines the barcode in ConfirmBarcodeDialog
     *//*

    fun onDecline() {
        val currentState = uiState.value
        _uiState.value = currentState.copy(showConfirmationDialog = false)
        restartPreviewWithDelay(showMessage = false, onShowMessage = {})
    }

    // ------------------
    // Zoom methods
    // ------------------

    fun onIncreaseZoom() {
        val currentState = uiState.value
        if (currentState.zoom < currentState.maxZoom) {
            _uiState.value = currentState.copy(zoom = currentState.zoom + 1)
        }
    }

    fun onDecreaseZoom() {
        val currentState = uiState.value
        if (currentState.zoom > 0) {
            _uiState.value = currentState.copy(zoom = currentState.zoom - 1)
        }
    }

    fun setZoom(newZoom: Int) {
        val currentState = uiState.value
        _uiState.value = currentState.copy(
            zoom = newZoom.coerceIn(0, currentState.maxZoom)
        )
    }

    fun setMaxZoom(value: Int) {
        val currentState = uiState.value
        _uiState.value = currentState.copy(maxZoom = value)
    }

    // ------------------
    // Private methods
    // ------------------

    private fun vibrateIfNeeded() {
        // Implement device vibration logic if needed
    }

    private fun restartPreviewWithDelay(
        showMessage: Boolean,
        onShowMessage: (Int) -> Unit
    ) {
        viewModelScope.launch {
            delay(CONTINUOUS_SCANNING_PREVIEW_DELAY)
            if (showMessage) {
                onShowMessage(R.string.saved)
            }
            // We cannot start the preview directly in the ViewModel if it requires a
            // reference to CodeScanner. The UI can observe some event or we can expose
            // a callback for the UI to call codeScanner.startPreview().
            // So maybe you just post a state change that the UI interprets as "go restart preview".
        }
    }

    private suspend fun saveScannedBarcode(
        barcode: Barcode,
        doNotSaveDuplicates: Boolean
    ): Long {
        return withContext(Dispatchers.IO) {
            barcodeDatabase.save(barcode, doNotSaveDuplicates)
        }
    }

    companion object {
        private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L
    }
}*/
