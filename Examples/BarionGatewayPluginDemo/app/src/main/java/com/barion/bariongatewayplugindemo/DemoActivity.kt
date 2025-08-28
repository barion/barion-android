package com.barion.bariongatewayplugindemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import barion.sdk.shared.BarionGatewayPlugin
import barion.sdk.shared.SDKClientEventListener
import barion.sdk.shared.models.BarionGatewayPluginOptions
import barion.sdk.shared.models.Colors
import barion.sdk.shared.models.DialogButton
import barion.sdk.shared.models.DialogColors
import barion.sdk.shared.models.FundingSourceColors
import barion.sdk.shared.models.InputColors
import barion.sdk.shared.models.PaymentResult
import barion.sdk.shared.models.PrimaryButton
import barion.sdk.shared.models.RenderOptions
import barion.sdk.shared.models.Shadow

class DemoActivity: AppCompatActivity(), SDKClientEventListener {

    private var barionGatewayPlugin: BarionGatewayPlugin? = null
    private val TAG = DemoActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the BarionGatewayPlugin and subscribe for SDK events
        barionGatewayPlugin = BarionGatewayPlugin(this, this)

    }

    /**
    This method shows the simplest integration of the Barion SDK.
    The clientSecret parameter comes from your backend.
     */
    private fun presentBarionGatewayPlugin(clientSecret: String){
        // The result of the payment attempt will be returned in the callback function, and you MUST validate it on your backend as well.
        barionGatewayPlugin?.present(
            clientSecret, null
        ) { paymentResult ->
            when (paymentResult) {
                is PaymentResult.Success -> {
                    // TODO handle successful payment result and validate it on your backend
                    Log.d(TAG, "success, ${paymentResult.result}")
                }

                is PaymentResult.Failure -> {
                    // TODO handle the error
                    Log.d(TAG, "failed")
                }
            }
        }
    }

    private fun presentBarionGatewayPluginWithCustomization(clientSecret: String){
        val barionGatewayPluginOptions = prepareBarionGatewayPluginCustomization()
        barionGatewayPlugin?.present(
            clientSecret, barionGatewayPluginOptions
        ) { paymentResult ->
            when (paymentResult) {
                is PaymentResult.Success -> {
                    Log.d(TAG, "success, ${paymentResult.result}")
                }

                is PaymentResult.Failure -> {
                    Log.d(TAG, "failed")
                }
            }
        }
    }

    private fun prepareBarionGatewayPluginCustomization(): BarionGatewayPluginOptions{
        val inputColors = InputColors(
            background = Color.White,
            border = Color.Black,
            placeholder = Color.Black,
            label = Color.Black,
            error = Color.Red,
            link = Color.Blue
        )
        val fundingSourceColors = FundingSourceColors(
            title = Color.Black,
            subtitle = Color.Black,
            background = Color.White
        )
        val dialogPositiveButton = DialogButton(
            backgroundColor = Color.Blue,
            titleColor = Color.White,
            borderColor = null
        )
        val dialogNegativeButton = DialogButton(
            backgroundColor = Color.Red,
            titleColor = Color.White,
            borderColor = null
        )
        val dialogNeutralButton = DialogButton(
            backgroundColor = Color.Gray,
            titleColor = Color.White,
            borderColor = Color.Black
        )
        val dialogColors = DialogColors(
            titleColor = Color.Black,
            messageColor = Color.Black,
            backgroundColor = Color.Black,
            positiveButton = dialogPositiveButton,
            negativeButton = dialogNegativeButton,
            neutralButton = dialogNeutralButton
        )
        val colors = Colors(
            background = Color.White,
            primary = Color.Red,
            title = Color.Black,
            subtitle = Color.Black,
            text = Color.Black,
            secondaryText = Color.Black,
            fundingSourceColors = fundingSourceColors,
            inputColors = inputColors,
            dialogColors = dialogColors
        )
        val shadow = Shadow(
            shadowColor = Color.Black,
            shadowOffset = DpSize(10.dp, 10.dp),
            shadowOpacity = 0.5f,
            shadowRadius = 10f
        )
        val primaryButton = PrimaryButton(
            titleColor = Color.White,
            backgroundColor = Color.Black,
            shadow = shadow,
            font = FontFamily.SansSerif
        )
        val renderOptions = RenderOptions(
            font = FontFamily.SansSerif,
            colors = colors,
            primaryButton = primaryButton,
            paymentMethodOrder = null,
            hidePoweredByLabel = false
        )
        val barionGatewayPluginOptions = BarionGatewayPluginOptions(
            renderOptions = renderOptions,
            locale = "en_US"
        )
        return barionGatewayPluginOptions
    }

    override fun onEvent(event: String) {
        Log.d(TAG, "event: $event")
    }
}