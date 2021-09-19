package com.example.authentication.hashPassword

import java.security.MessageDigest

class Md5 (var input: String){
    init{
        input = toMd5Hash()
    }
    private fun toMd5Hash(): String{
        val md = MessageDigest.getInstance("MD5")
        return toHex(md.digest(input.toByteArray()))
    }
    private fun toHex(byteArray: ByteArray): String{
        return byteArray.joinToString { "%02x".format(it) }
    }
}