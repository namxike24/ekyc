package ai.ftech.ekyc.infras

import android.util.Base64
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

object EncodeRSA {
    private val PUBLIC_KEY: String = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcwwhLaw9suYrbgsS58p9Mqe/fqKfpfmllBh7DB+IghiHxu5JSYTAkt0QVki/5HVDJI9U5249N9KqRHN7FV4BWSzATFudpsMkM7OY6tU6bZJ/34CAIYNMOReHWWAuM5PVhiItS59yhWPJyZWSkEpM9s/xOhtZisXgBQOJLqt5n4wIDAQAB"

    fun encryptData(text: String): String {
        var encoded: String = ""
        //TODO bundle + servicesID + key + timestamp để mã hóa
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
        return encoded
    }
}
