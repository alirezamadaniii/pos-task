package com.example.postask.domain.usecase

import com.example.postask.data.utils.IsoUtils
import com.example.postask.domain.repository.IsoRepository
import org.jpos.iso.ISOMsg

class IsoInteractorUseCase(
    private val repository: IsoRepository
) {
    fun buildIsoMessage(pan: String, amount: String): Pair<ISOMsg, ByteArray> {
        val stan = repository.getNextStan()
        return IsoUtils.buildIsoMessageBytes(stan, pan, amount)
    }

    fun parseIsoMessage(bytes: ByteArray): ISOMsg {
        return IsoUtils.parseIsoFromBytes(bytes)
    }
}