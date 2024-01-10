package user_management

import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class SHA256PasswordService : PasswordService {
    override fun generateSalt(): String {
        val random = SecureRandom()
        val bytes = ByteArray(16)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    override fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")

        // apply the salt and calculate the hash
        val saltedInput = salt + password
        val messageDigest = md.digest(saltedInput.toByteArray())

        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)

        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }

        return hashtext
    }
}
