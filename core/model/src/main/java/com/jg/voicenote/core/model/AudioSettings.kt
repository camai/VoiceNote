package com.jg.voicenote.core.model

/**
 * 오디오 샘플레이트 설정
 */
enum class SampleRate(val value: Int, val displayName: String) {
    RATE_8000(8000, "8000Hz"),
    RATE_16000(16000, "16000Hz"),
    RATE_22050(22050, "22050Hz"),
    RATE_44100(44100, "44100Hz"),
    RATE_48000(48000, "48000Hz");
    
    companion object {
        fun fromValue(value: Int): SampleRate {
            return entries.find { it.value == value } ?: RATE_44100
        }
        
        fun fromDisplayName(displayName: String): SampleRate {
            return entries.find { it.displayName == displayName } ?: RATE_44100
        }
    }
}

/**
 * 오디오 비트레이트 설정
 */
enum class BitRate(val value: Int, val displayName: String) {
    RATE_64(64000, "64kbps"),
    RATE_96(96000, "96kbps"),
    RATE_128(128000, "128kbps"),
    RATE_192(192000, "192kbps"),
    RATE_256(256000, "256kbps"),
    RATE_320(320000, "320kbps");
    
    companion object {
        fun fromValue(value: Int): BitRate {
            return entries.find { it.value == value } ?: RATE_192
        }
        
        fun fromDisplayName(displayName: String): BitRate {
            return entries.find { it.displayName == displayName } ?: RATE_192
        }
    }
}

/**
 * 오디오 품질 설정
 */
enum class AudioQuality(val sampleRate: SampleRate, val bitRate: BitRate, val displayName: String) {
    LOW(SampleRate.RATE_22050, BitRate.RATE_96, "낮은 음질"),
    MEDIUM(SampleRate.RATE_44100, BitRate.RATE_192, "정상 음질"),
    HIGH(SampleRate.RATE_44100, BitRate.RATE_256, "높은 음질"),
    ULTRA(SampleRate.RATE_48000, BitRate.RATE_320, "최고 음질");
    
    companion object {
        fun fromSettings(sampleRate: Int, bitRate: Int): AudioQuality {
            return entries.find { 
                it.sampleRate.value == sampleRate && it.bitRate.value == bitRate 
            } ?: MEDIUM
        }
        
        fun fromDisplayName(displayName: String): AudioQuality {
            return entries.find { it.displayName == displayName } ?: MEDIUM
        }
    }
}
