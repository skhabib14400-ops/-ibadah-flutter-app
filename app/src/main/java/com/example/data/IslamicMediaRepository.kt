package com.example.data

import com.example.model.IslamicVideo

object IslamicMediaRepository {

    val liveStreams: List<IslamicVideo> = listOf(
        IslamicVideo(
            id = "makkah_live_stream",
            title = "Makkah Live HD 24/7 - Masjid Al-Haram",
            speakerOrChannel = "Saudi Quran TV Official",
            duration = "LIVE",
            isLive = true,
            category = "Live",
            streamUrlOrEmbed = "https://www.youtube.com/watch?v=live_makkah",
            location = "Masjid al-Haram, Makkah"
        ),
        IslamicVideo(
            id = "madinah_live_stream",
            title = "Madinah Live HD 24/7 - Masjid An-Nabawi",
            speakerOrChannel = "Saudi Sunnah TV Official",
            duration = "LIVE",
            isLive = true,
            category = "Live",
            streamUrlOrEmbed = "https://www.youtube.com/watch?v=live_madinah",
            location = "Al-Masjid an-Nabawi, Madinah"
        )
    )

    val videos: List<IslamicVideo> = listOf(
        IslamicVideo(
            id = "quran_surah_kahf",
            title = "Surah Al-Kahf with Heart-Touching Recitation",
            speakerOrChannel = "Mishary Rashid Alafasy",
            duration = "28:45",
            isLive = false,
            category = "Quran",
            streamUrlOrEmbed = "https://example.com/kahf"
        ),
        IslamicVideo(
            id = "quran_surah_rahman",
            title = "Surah Ar-Rahman - Deeply Calming Recitation & English Meaning",
            speakerOrChannel = "Abdul Rahman Al-Sudais",
            duration = "16:20",
            isLive = false,
            category = "Quran",
            streamUrlOrEmbed = "https://example.com/rahman"
        ),
        IslamicVideo(
            id = "ramadan_blessings_lecture",
            title = "How to Maximize the Spiritual Blessings of Ramadan & Salah",
            speakerOrChannel = "Mufti Menk",
            duration = "24:10",
            isLive = false,
            category = "Lecture",
            streamUrlOrEmbed = "https://example.com/ramadan"
        ),
        IslamicVideo(
            id = "tahajjud_prayer_guide",
            title = "The Miracles and Inner Peace of Tahajjud Prayer",
            speakerOrChannel = "Omar Suleiman (Yaqeen Institute)",
            duration = "19:35",
            isLive = false,
            category = "Lecture",
            streamUrlOrEmbed = "https://example.com/tahajjud"
        ),
        IslamicVideo(
            id = "dua_forgiveness_lecture",
            title = "The Power of Istighfar & Sincere Repentance",
            speakerOrChannel = "Nouman Ali Khan (Bayyinah)",
            duration = "22:50",
            isLive = false,
            category = "Lecture",
            streamUrlOrEmbed = "https://example.com/istighfar"
        )
    )
}
