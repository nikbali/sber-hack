package com.sberbank.hack

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class OraDataParser {
    @Scheduled(fixedRate = 10000)
    fun pollData() {
        
    }
}