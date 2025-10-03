package com.example.postask.data.utils

import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.ISO87BPackager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object IsoUtils {

    //Field 4: Amount Transaction
    fun formatAmount(amount: String) = amount.filter { it.isDigit() }.padStart(12, '0')

    //Field 7: Transmission Date Time
    fun getField7DateTime() = SimpleDateFormat("MMddHHmmss", Locale.US).format(Date())

    //create iso8583 message
    fun buildIsoMessageBytes(stan: String, pan: String, amount: String): Pair<ISOMsg, ByteArray> {
        val iso = ISOMsg().apply {
            mti = "0200"
            set(2, pan)
            set(4, formatAmount(amount))
            set(7, getField7DateTime())
            set(11, stan)
            packager = ISO87BPackager()
        }
        return Pair(iso, iso.pack())
    }

    //parse Iso8583
    fun parseIsoFromBytes(bytes: ByteArray): ISOMsg {
        return ISOMsg().apply {
            packager = ISO87BPackager()
            unpack(bytes)
        }
    }

    //convert byte to hex for show message
    fun bytesToHex(bytes: ByteArray) =
        bytes.joinToString("") { "%02X".format(it) }
}
