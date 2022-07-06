package com.imtmobileapps.util

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.imtmobileapps.model.*
import com.imtmobileapps.util.Constants.CRYPTO_SENSITIVE_DATA_FILE
import com.imtmobileapps.util.Constants.ENABLED
import com.imtmobileapps.util.Constants.MINIMUM_CHARS
import com.imtmobileapps.util.Constants.ROLE_USER
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigDecimal
import java.nio.charset.StandardCharsets


enum class DataSource {
    LOCAL,
    REMOTE
}

enum class CoinSort {
    NAME,
    SYMBOL,
    RANK
}

enum class RowType {
    COIN_NAME,
    COIN_PRICE,
    QUANTITY_HELD,
    HOLDING_VALUE,
    YOUR_COST,
    INCREASE,
    TOTAL_VALUE,
    TOTAL_COST,
    TOTAL_INCREASE

}

enum class SheetType {
    CRYPTO_VALUE,
    COIN
}

enum class SearchAppBarState {
    OPENED,
    CLOSED
}

fun Double.roundDecimal(digit: Int): String{
    return "%.${digit}f".format(this)
}

fun sortCryptoValueList(
    list: List<CryptoValue>,
    coinSort: CoinSort,
): List<CryptoValue> {
    when (coinSort) {
        CoinSort.NAME -> {
            return list.sortedBy {
                it.coin.coinName
            }
        }

        CoinSort.SYMBOL -> {
            return list.sortedBy {
                it.coin.coinSymbol
            }
        }

        CoinSort.RANK -> {
            return list.sortedBy {
                it.coin.cmcRank
            }
        }
    }

}

fun removeWhiteSpace(str: String): String {
    return if (str.isNotEmpty()) {
        str.filter {
            !it.isWhitespace()
        }
    } else {
        ""
    }

}

// WRITE to application sandbox
fun writeUsernameAndPassword(context: Context, uname: String, pass: String) {

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    val fileToWrite = CRYPTO_SENSITIVE_DATA_FILE

    val storagePath = context.filesDir
    val encryptedFile = EncryptedFile.Builder(
        File(storagePath, fileToWrite),
        context,
        mainKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
    val str = "$uname:$pass"
    val fileContent = str.toByteArray(StandardCharsets.UTF_8)
    encryptedFile.openFileOutput().apply {
        write(fileContent)
        flush()
        close()
    }
}

// DELETE
fun deleteSensitiveFile(context: Context): Boolean {
    val fileToDelete = CRYPTO_SENSITIVE_DATA_FILE
    val storagePath = context.filesDir
    val data = File(storagePath, fileToDelete)
    return data.delete()

}

// READ from application sandbox
fun readUsernameAndPassword(context: Context): String {

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    val fileToRead = CRYPTO_SENSITIVE_DATA_FILE
    val storagePath = context.filesDir

    val encryptedFile = EncryptedFile.Builder(
        File(storagePath, fileToRead),
        context,
        mainKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    val inputStream = encryptedFile.openFileInput()
    val byteArrayOutputStream = ByteArrayOutputStream()
    var nextByte: Int = inputStream.read()
    while (nextByte != -1) {
        byteArrayOutputStream.write(nextByte)
        nextByte = inputStream.read()
    }

    inputStream.apply {
        close()
    }

    return byteArrayOutputStream.toString()

}

fun validateUsername(uname: String): Boolean {
    return uname.isNotEmpty() && uname.length >= MINIMUM_CHARS
}

fun validatePassword(pass: String): Boolean {
    return pass.isNotEmpty() && pass.length >= MINIMUM_CHARS
}

fun validateEmail(email : String): Boolean{
    var isValidEmail = false
    if (email.isNotEmpty()){
        isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
   return isValidEmail

}

fun validateAddHoldingValues(quantity: String, cost: String): Boolean {
    val quantityClean = removeWhiteSpace(quantity)
    val costClean = removeWhiteSpace(cost)
    return quantityClean.isNotEmpty() && costClean.isNotEmpty()
}

fun createEmptySignUp(): SignUp {
    // DEFAULT state set id to  = 3
    val state = State(3, "", "", "")
    val person = Person(
        personId = 0,
        firstName = "",
        lastName = "",
        email = "",
        phone = "",
        address = "",
        city = "",
        zip = "",
        state = state

    )

    val auth = Auth(
        auth_id = 0,
        username = "",
        password = "",
        person_id = 0,
        role = ROLE_USER,
        enabled = ENABLED
    )

    return SignUp(
        person = person,
        auth = auth
    )
}


fun getDummyCryptoValue(): CryptoValue {

    return CryptoValue(
        id = "",
        USD = "",
        coin = getDummyCoin(1),
        holdingValue = "testing",
        percentage = "percentage",
        cost = "cost",
        increaseDecrease = "inc/de",
        quantity = 0.12
    )
}

fun getDummyTotalsValue(): TotalValues {
    return TotalValues(
        personId = -1,
        totalCost = "1",
        totalValue = "1",
        totalChange = "1",
        increaseDecrease = ""

    )
}

fun getDummyCoin(index: Int): Coin {

    return Coin(
        0,
        "Dummy",
        "DUM",
        1,
        "slug",
        "imageurl",
        "largeimageurl",
        BigDecimal.valueOf(0.123455),
        index,
        BigDecimal.valueOf(0.123455)
    )
}

fun getDummyPerson(): Person {
    return Person(
        personId = 0,
        firstName = "Sam",
        lastName = "Spade",
        email = "",
        phone = "",
        address = "",
        city = "",
        zip = "",
        state = State(id = 0, "", "", "")
    )
}

fun getDummyGeckoCoin(): GeckoCoin {
    return GeckoCoin(
        id = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
        currentPrice = 21498.0,
        marketCap = 410081413149,
        marketCapRank = 1,
        fullyDilutedValuation = 451505281176,
        totalVolume = 27438047234,
        high24H = 21544.0,
        low24H = 19931.52,
        priceChange24H = 852.23,
        priceChangePercentage24H = 4.12777,
        marketCapChange24H = 1.7520067602E10,
        marketCapChangePercentage24H = 4.46301,
        circulatingSupply = 1.9073331E7,
        totalSupply = 21000000,
        maxSupply = 21000000,
        ath = 69045.0,
        athChangePercentage = -68.89821,
        athDate = "2021-11-10T14:24:11.849Z",
        atl = 67.81,
        atlChangePercentage = 31568.6009,
        atlDate = "2013-07-06T00:00:00.000Z",
        roi = null,
        lastUpdated = "2022-06-21T14:30:00.682Z",
        sparklineIn7D = SparklineIn7D(
            price = listOf(21968.036545102044,
                14849.555915839494,
                22155.984872884674,
                22230.58772295747,
                22468.231730265863,
                21605.29024309619,
                22223.152110755702,
                22170.026650934054,
                21960.692677005474,
                21493.5917548155,
                21143.838083404593,
                21227.23204290085,
                21341.784049291196,
                20814.501908313905,
                20384.64184671442,
                20216.09012566128,
                30607.52197569402,
                34081.288650842314,
                37237.929805416657,
                35642.498986783747,
                29215.03696741113,
                28401.476072468475,
                26735.252212100244,
                25102.219264985462,
                21641.444230944264,
                22709.047428128575)

        ))

}

