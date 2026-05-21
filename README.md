# Barion Android

Barion Android provides you with the building blocks to create a checkout experience for your
shoppers, allowing them to pay using the payment method of their choice.

## Installation

Our library is available on [Maven Central][mavenRepo].

```groovy
implementation("com.barion:barionsdk:1.0.0")
```

Our library uses Jetpack Compose and Java 8+ features so your application needs to support it as
well:

```groovy
coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui")
```

:warning: _If you don't want to customize the payment UI or use the `GooglePayButton` you don't need
to add compose._

## Usage

### Setting up the BarionGatewayPlugin

You need to initialize the `BarionGatewayPlugin` in the `onCreate()` method like this:

```kotlin
val barionGatewayPlugin = BarionGatewayPlugin(this, null)
```

You can subscribe to events from the SDK. You can use it to log the progress of the payment flow.
You should create a `BarionGatewayPluginConfiguration` object and pass it to the
`BarionGatewayPlugin` initialization method.
Also, you need to implement the `SDKClientEventListener` protocol to catch the events.

```kotlin
val barionGatewayPluginConfiguration = BarionGatewayPluginConfiguration(this)
val barionGatewayPlugin = BarionGatewayPlugin(this, barionGatewayPluginConfiguration)
```

:warning: _Currently there are no events._

### Present the inline gateway

To present the `BarionGatewayPlugin`, it needs a client secret at least. You can get a client secret
from the Barion API, read more [here](https://docs.barion.com).

Call the `present` function of the `BarionGatewayPlugin` instance to present the inline gateway in
your app. The function will asynchronously return the payment attempt result. Pass the
`clientSecret` as a parameter to the `present` method at least.
The SDK can decide which environment is used to get the client secret.

You MUST validate the result on your backend as well.

```kotlin
barionGatewayPlugin.present(clientSecret, null) { paymentResult ->
    // handle payment result and validate it on your backend
}
```

See more about the `paymentResult` object [here](#payment-result).

#### Customization

You can customize the UI of the SDK to fit into your application perfectly. Create your `barionGatewayPluginOptions`. Then pass it to the `BarionGatewayPlugin` object's `present` function.

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
    backgroundColor = Color.Grey,
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
    // handle payment result and validate it on your backend
}
```

See `BarionAppearance` entries [here](#barion-appearance).

:warning: _The font family of the fonts is used throughout the SDK. The SDK uses this font at
multiple weights (e.g., regular, medium, semibold) if they exist._

:warning: _The Google Pay will appear in the payment method list only if your device supports it and if it is enabled and properly configured for the merchant account._

### Google Pay component

The Barion SDK supports presenting a Google Pay button directly on your UI.

Using the GooglePay button, you still need
to [initialize the BarionGatewayPlugin](#setting-up-the-BarionGatewayPlugin).
After the plugin instance is created, you MUST call the `isGooglePayAvailable` function first. This
will perform all the necessary checks to determine whether the
Google Pay button will be available. To call this function it needs a client secret at least. You
can get a client secret from the Barion API, read more [here](https://docs.barion.com).

```kotlin
barionGatewayPlugin.isGooglePayAvailable(clientSecret) { isAvailable ->
    // handle the boolean result
}
```
:warning: _The Google Pay buttin will be available only if your device supports it and if it is enabled and properly configured for the merchant account._

If isAvailable == true, you can call the `GooglePayButton` function to show the Google Pay button. You MUST validate the result on your backend as well.

```kotlin
barionGatewayPlugin.GooglePayButton(null) { paymentResult ->
    // handle payment result and validate it on your backend
}
```

See more about the `paymentResult` object [here](#payment-result).

:warning: _This is a composable function which can be called from composable context only._

:warning: _If the `isGooglePayAvailable` function was not called before the `GooglePayButton`
function call, the button won't appear and the function will return the following error:
IS_GOOGLE_PAY_AVAILABLE_WAS_NOT_CALLED._

:warning: _If the `isGooglePayAvailable` functions result was false, the button won't appear and
the `GooglePayButton` function will return the following error: GOOGLE_PAY_NOT_AVAILABLE._

#### Customization

You can customize the button to fit into your application perfectly. Create your `googlePayButtonRenderOptions`. Then pass it to the `BarionGatewayPlugin` object's `GooglePayButton` function.

```kotlin
val googlePayButtonRenderOptions = GooglePayButtonRenderOptions(
    appearance = BarionAppearance.AUTOMATIC,
    type = GooglePayButtonType.PAY,
    cornerRadiusDp = 24
)
barionGatewayPlugin.GooglePayButton(googlePayButtonRenderOptions) { paymentResult ->
   // handle payment result and validate it on your backend
}
```

See `BarionAppearance` entries [here](#barion-appearance).

#### Google Pay button type
The `GooglePayButtonType` has the following values:
- BOOK
- BUY
- CHECKOUT
- DONATE
- ORDER
- PAY (default)
- PLAIN
- SUBSCRIBE

Read more about this [here](https://developers.google.com/pay/api/android/guides/brand-guidelines).

### Payment result

The `paymentResult` will contain the funding source of the last payment attempt (even if it was
unsuccessful).
If there was an error during the payment flow the `paymentError` will contain what the error was.
The list of the possible errors are the followings:

| Code                                   | Description                                                                               |
|----------------------------------------|-------------------------------------------------------------------------------------------|
| MISSING_CLIENT_SECRET                  | The passed client secret is not valid.                                                    |
| MISSING_PAYMENT_CONFIG                 | There was an error accessing the payment configuration.                                   |
| PAYMENT_CANCELLED                      | The payment was cancelled by the user.                                                    |
| IS_GOOGLE_PAY_AVAILABLE_WAS_NOT_CALLED | The `isGooglePayAvailable` function was not called before the `GooglePayButton` function. |
| GOOGLE_PAY_NOT_AVAILABLE               | It is either not available on the device or for merchat.                                  |
| GOOGLE_PAY_CANCELLED                   | GooglePay sheet was cancelled by the user.                                                |
| GOOGLE_PAY_ERROR                       | GooglePay error.                                                                          |
| CANNOT_START_GOOGLE_PAY                | The button is available but starting the Google pay flow is not possible.                 |
| UNKNOWN                                | -                                                                                         |

### Barion appearance

The `BarionApperanace` enum has the following values:
- AUTOMATIC (default, same as what was set at the OS level on the device)
- LIGHT
- DARK


## Accessing GooglePay production mode
If you want to make Google Pay available to your users, you need to follow the steps below.

1. Publish your app on Google Play Console (It doesn't have to be a live version, a closed test is enough.)
2. Go to [Google Pay and Wallet console](https://pay.google.com/business/console) and create a business profile
   1. Add your public business name, type of business (merchant) and location, read and accept the term of service
   2. Complete your business data on the profile
3. Share your Merchant ID with Barion
4. Get Production access to your app
   1. On the Google Pay and Wallet console, go to Google Pay api, then App Integration and you will see your apps there. The apps which are available in Play Console even if they are not published yet or only available for closed or open testing. If your app is not visible, you have to invite the e-mail address with is connected to the app in the Play Console. For that go to Users → Invite a user
   2. Complete all steps
      1. Select integration type: Gateway
      2. Upload screenshots of your app (using the Barion SDK in test environment because in this case Google Pay will be available in test mode)
      3. Submit for validation and wait for approval

When your app is approved in the Google Pay and Wallet console, Google Pay in production mode will be available in the Barion SDK.

## ProGuard

If you use ProGuard or R8, you do not need to manually add any rules, as they are automatically
embedded in the artifacts.
Please let us know if you find any issues.

## Localization

The SDK supports these languages:
English, Hungarian, Czech, German, Slovak, Polish, Romanian

## See also

* [Complete Documentation](https://docs.barion.com)

## Support

If you have a feature request, or spotted a bug or a technical problem, create a GitHub issue. For
other questions, contact our [support team](https://barion.com).

[mavenRepo]: https://repo1.maven.org/maven2/com/barion/barionsdk/
