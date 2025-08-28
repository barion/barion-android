
# Barion Android

Barion Android provides you with the building blocks to create a checkout experience for your shoppers, allowing them to pay using the payment method of their choice.

## Installation

Our library is available on [Maven Central][mavenRepo].

```groovy
implementation("com.barion:barionsdk:0.3.0")
```

Our library uses Jetpack Compose and Java 8+ features so your application needs to support it as well:
```groovy
coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui")
```
:warning: _If you don't want to customize the payment UI you don't need to add compose._

### Usage

#### Setting up the BarionGatewayPlugin

You need to initialize the `BarionGatewayPlugin` in the `onCreate()` method like this:
```kotlin
val barionGatewayPlugin = BarionGatewayPlugin(this, null)
```

To present the BarionGatewayPlugin, it needs a client secret at least. You can get a client secret from the Barion API, read more [here](https://docs.barion.com).

Call the `present` function of the `BarionGatewayPlugin` instance to present the inline gateway in your app. The function will asynchronously return the payment attempt result. Pass the `clientSecret` as a parameter to the `present` method at least.
The SDK can decide which environment is used to get the client secret.

You MUST validate the result on your backend as well.

```kotlin
barionGatewayPlugin.present(clientSecret, null) { paymentResult ->
    // TODO handle payment result and validate it on your backend
}
```

The `paymentResult` will contain the funding source of the last payment attempt (even if it was unsuccessful).
If there was an error during the payment flow the `paymentError` will contain what the error was. 

You can subscribe to events from the SDK. You can use it to log the progress of the payment flow.
You should create a `BarionGatewayPluginConfiguration` object and pass it to the `BarionGatewayPlugin` initialization method.
Also, you need to implement the `SDKClientEventListener` protocol to catch the events.

```kotlin
val barionGatewayPluginConfiguration = BarionGatewayPluginConfiguration(sdkEventListener: this)
val barionGatewayPlugin = BarionGatewayPlugin(this, barionGatewayPluginConfiguration)
```

#### Customize the BarionGatewayPlugin

You can customize the SDK to fit into your application perfectly. Choose your own fonts, colors etc.
You can customize the order of the available payment methods. The default order is: Google Pay, new card. If you missed some payment methods don't worry the SDK will put the rest of the available payment methods at the end of the list.
The `paymentMethodOrder` possible values are:
```kotlin
["googlePay", "bankCard", "barionWallet", "newCard"]
```

:warning: _Currently Google Pay is not available, we are still working on it._

:warning: _The Google Pay won't be in the payment method list if your device doesn't support it._

Pass it to the `BarionGatewayPlugin` object `present` function.

```kotlin
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
    backGroundColor = Color.Blue,
    titleColor = Color.White,
    borderColor = null
)
val dialogNegativeButton = DialogButton(
    backGroundColor = Color.Red,
    titleColor = Color.White,
    borderColor = null
)
val dialogNeutralButton = DialogButton(
    backGroundColor = Color.Grey,
    titleColor = Color.White,
    borderColor = Color.Black
)
val dialogColors = DialogColors(
    titleColor = Color.Black,
    messageColor = Color.Black,
    backgroundColor = Color.White,
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
    appearance = BarionAppearance.AUTOMATIC,
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

barionGatewayPlugin.present(clientSecret, barionGatewayPluginOptions) { paymentResult ->
    // TODO handle payment result and validate it on your backend
}

```

:warning: _The font family of the fonts is used throughout the SDK. The SDK uses this font at multiple weights (e.g., regular, medium, semibold) if they exist._


## ProGuard

If you use ProGuard or R8, you do not need to manually add any rules, as they are automatically embedded in the artifacts.
Please let us know if you find any issues.

## Localization

The SDK supports these languages:
English, Hungarian, Czech, German, Slovak


## See also

* [Complete Documentation](https://docs.barion.com)

## Support

If you have a feature request, or spotted a bug or a technical problem, create a GitHub issue. For other questions, contact our [support team](https://barion.com).

[mavenRepo]: https://repo1.maven.org/maven2/com/barion/barionsdk/
