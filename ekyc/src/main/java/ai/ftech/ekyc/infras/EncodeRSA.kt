package ai.ftech.ekyc.infras

import android.util.Base64
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

object EncodeRSA {
    private val PUBLIC_KEY: String = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKvWIEhxiogTnaK9e7xH3UxhLPB73BafhsQSyt1eG5+3AbM3Nb4L974d6I41mPuF7OX/iBjxkX3qoHmEv0TnJCauBXuVt2irQ4mKx+Yyu1pi1YxH4xqKBIM37zNMsnC8k1upZ3LSBjjUf5xJLx+QVrZ5T1i2pBUUHOJcjIfkS8mQIDAQAB"

    fun encryptData(key: String, bundle: String?): String {
        var encoded = ""
        val text = "${bundle},${Calendar.getInstance().timeInMillis},${key}"
        val encrypted: ByteArray
        try {
            val publicBytes = Base64.decode(PUBLIC_KEY, Base64.DEFAULT)
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val pubKey = keyFactory.generatePublic(keySpec)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            encrypted = cipher.doFinal(text.toByteArray())
            encoded = Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return URLEncoder.encode(encoded, "utf-8")
    }
}
