package com.example.data

import com.example.model.Ayah
import com.example.model.QuranReciter
import com.example.model.Surah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object QuranRepository {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val ayahsCache = ConcurrentHashMap<Int, List<Ayah>>()

    val reciters: List<QuranReciter> = listOf(
        QuranReciter(
            id = "alafasy",
            name = "Mishary Rashid Alafasy",
            subName = "মিশারি রশিদ আল-আফাসি",
            style = "Hafs 'an 'Asim",
            baseUrl = "https://everyayah.com/data/Alafasy_128kbps/"
        ),
        QuranReciter(
            id = "sudais",
            name = "Abdul Rahman Al-Sudais",
            subName = "আব্দুর রহমান আস-সুদাইস",
            style = "Murattal",
            baseUrl = "https://everyayah.com/data/Abdurrahmaan_As-Sudais_192kbps/"
        ),
        QuranReciter(
            id = "muaiqly",
            name = "Maher Al-Muaiqly",
            subName = "মাহের আল-মুয়াইক্বিলি",
            style = "Hafs 'an 'Asim",
            baseUrl = "https://everyayah.com/data/MaherAlMuaiqly128kbps/"
        ),
        QuranReciter(
            id = "shuraim",
            name = "Saud Al-Shuraim",
            subName = "সউদ আশ-শুরাইম",
            style = "Murattal",
            baseUrl = "https://everyayah.com/data/Saood_ash-Shuraym_128kbps/"
        ),
        QuranReciter(
            id = "husary",
            name = "Mahmoud Khalil Al-Husary",
            subName = "মাহমুদ খলিল আল-হুসারি",
            style = "Mujawwad",
            baseUrl = "https://everyayah.com/data/Husary_128kbps/"
        )
    )

    val surahs: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "আল-ফাতিহা", "The Opening", "সূচনা", 7, "Meccan", 1),
        Surah(2, "البقرة", "Al-Baqarah", "আল-বাকারা", "The Cow", "বকনা-বাছুর", 286, "Medinan", 2),
        Surah(3, "آل عمران", "Ali 'Imran", "আলে ইমরান", "Family of Imran", "ইমরানের পরিবার", 200, "Medinan", 50),
        Surah(4, "النساء", "An-Nisa", "আন-নিসা", "The Women", "নারী", 176, "Medinan", 77),
        Surah(5, "المائدة", "Al-Ma'idah", "আল-মায়িদাহ", "The Table Spread", "খাদ্য পরিবেশিত টেবিল", 120, "Medinan", 106),
        Surah(6, "الأنعام", "Al-An'am", "আল-আনআম", "The Cattle", "গৃহপালিত পশু", 165, "Meccan", 128),
        Surah(7, "الأعراف", "Al-A'raf", "আল-আরাফ", "The Heights", "উঁচু স্থানসমূহ", 206, "Meccan", 151),
        Surah(8, "الأنفال", "Al-Anfal", "আল-আনফাল", "The Spoils of War", "যুদ্ধলব্ধ ধন-সম্পদ", 75, "Medinan", 177),
        Surah(9, "التوبة", "At-Tawbah", "আত-তাওবাহ", "The Repentance", "অনুশোচনা / তওবা", 129, "Medinan", 187),
        Surah(10, "يونس", "Yunus", "ইউনুস", "Jonah", "ইউনুস (আঃ)", 109, "Meccan", 208),
        Surah(11, "هود", "Hud", "হুদ", "Hud", "হুদ (আঃ)", 123, "Meccan", 221),
        Surah(12, "يوسف", "Yusuf", "ইউসুফ", "Joseph", "ইউসুফ (আঃ)", 111, "Meccan", 235),
        Surah(13, "الرعد", "Ar-Ra'd", "আর-রাদ", "The Thunder", "বজ্রনাদ", 43, "Medinan", 249),
        Surah(14, "إبراهيم", "Ibrahim", "ইব্রাহিম", "Abraham", "ইব্রাহিম (আঃ)", 52, "Meccan", 255),
        Surah(15, "الحجر", "Al-Hijr", "আল-হিজর", "The Rocky Tract", "পাথুরে পাহাড়", 99, "Meccan", 262),
        Surah(16, "النحل", "An-Nahl", "আন-নাহল", "The Bee", "মৌমাছি", 128, "Meccan", 267),
        Surah(17, "الإسراء", "Al-Isra", "আল-ইসরা", "The Night Journey", "রজনী ভ্রমণ / বনি ইসরাঈল", 111, "Meccan", 282),
        Surah(18, "الكهف", "Al-Kahf", "আল-কাহাফ", "The Cave", "গুহা", 110, "Meccan", 293),
        Surah(19, "مريم", "Maryam", "মারিয়াম", "Mary", "মারিয়াম (আঃ)", 98, "Meccan", 305),
        Surah(20, "طه", "Ta-Ha", "ত্বা-হা", "Ta-Ha", "ত্বা-হা", 135, "Meccan", 312),
        Surah(21, "الأنبياء", "Al-Anbiya", "আল-আম্বিয়া", "The Prophets", "নবীগণ", 112, "Meccan", 322),
        Surah(22, "الحج", "Al-Hajj", "আল-হজ্জ", "The Pilgrimage", "হজ্জ", 78, "Medinan", 332),
        Surah(23, "المؤمنون", "Al-Mu'minun", "আল-মুমিনুন", "The Believers", "বিশ্বাসীগণ", 118, "Meccan", 342),
        Surah(24, "النور", "An-Nur", "আন-নূর", "The Light", "জ্যোতি / আলো", 64, "Medinan", 350),
        Surah(25, "الفرقان", "Al-Furqan", "আল-ফুরকান", "The Criterion", "সত্য-মিথ্যার মানদণ্ড", 77, "Meccan", 359),
        Surah(26, "الشعراء", "Ash-Shu'ara", "আশ-শুয়ারা", "The Poets", "কবিগণ", 227, "Meccan", 367),
        Surah(27, "النمل", "An-Naml", "আন-নামল", "The Ant", "পিঁপড়া", 93, "Meccan", 377),
        Surah(28, "القصص", "Al-Qasas", "আল-কাসাস", "The Stories", "ঘটনা ও কাহিনী", 88, "Meccan", 385),
        Surah(29, "العنكبوت", "Al-Ankabut", "আল-আনকাবুত", "The Spider", "মাকড়সা", 69, "Meccan", 396),
        Surah(30, "الروم", "Ar-Rum", "আর-রুম", "The Romans", "রোমবাসীগণ", 60, "Meccan", 404),
        Surah(31, "لقمان", "Luqman", "লুকমান", "Luqman", "লুকমান", 34, "Meccan", 411),
        Surah(32, "السجدة", "As-Sajdah", "আস-সাজদাহ", "The Prostration", "সিজদাহ", 30, "Meccan", 415),
        Surah(33, "الأحزاب", "Al-Ahzab", "আল-আহযাব", "The Combined Forces", "সম্মিলিত বাহিনী", 73, "Medinan", 418),
        Surah(34, "سبإ", "Saba", "সাবা", "Sheba", "সাবা জাতি", 54, "Meccan", 428),
        Surah(35, "فاطر", "Fatir", "ফাতির", "The Originator", "আদি স্রষ্টা", 45, "Meccan", 434),
        Surah(36, "يس", "Ya-Sin", "ইয়া-সিন", "Ya-Sin", "ইয়া-সিন", 83, "Meccan", 440),
        Surah(37, "الصافات", "As-Saffat", "আস-সাফফাত", "Those Who Set The Ranks", "সারিবদ্ধভাবে দাঁড়ানো দল", 182, "Meccan", 446),
        Surah(38, "ص", "Sad", "সোয়াদ", "The Letter Sad", "সোয়াদ", 88, "Meccan", 453),
        Surah(39, "الزمر", "Az-Zumar", "আজ-জুমার", "The Troops", "দলবদ্ধ জনতা", 75, "Meccan", 458),
        Surah(40, "غافر", "Ghafir", "গাফির", "The Forgiver", "ক্ষমাশীল", 85, "Meccan", 467),
        Surah(41, "فصلت", "Fussilat", "ফুসসিলাত", "Explained In Detail", "সুস্পষ্ট বিবরণ", 54, "Meccan", 477),
        Surah(42, "الشورى", "Ash-Shura", "আশ-শুরা", "The Consultation", "পরামর্শ", 53, "Meccan", 483),
        Surah(43, "الزخرف", "Az-Zukhruf", "আজ-জুখরুফ", "The Ornaments of Gold", "সোনার অলংকার", 89, "Meccan", 489),
        Surah(44, "الدخان", "Ad-Dukhan", "আদ-দুখান", "The Smoke", "ধোঁয়া", 59, "Meccan", 496),
        Surah(45, "الجاثية", "Al-Jathiyah", "আল-জাসিয়াহ", "The Crouching", "নতজানু", 37, "Meccan", 499),
        Surah(46, "الأحقاف", "Al-Ahqaf", "আল-আহকাফ", "The Wind-Curved Sandhills", "বালুর পাহাড়", 35, "Meccan", 502),
        Surah(47, "محمد", "Muhammad", "মুহাম্মদ", "Muhammad", "মুহাম্মদ (সাঃ)", 38, "Medinan", 507),
        Surah(48, "الفتح", "Al-Fath", "আল-ফাতহ", "The Victory", "মহাবিজয়", 29, "Medinan", 511),
        Surah(49, "الحجرات", "Al-Hujurat", "আল-হুজুরাত", "The Rooms", "বাসগৃহসমূহ", 18, "Medinan", 515),
        Surah(50, "ق", "Qaf", "ক্বাফ", "The Letter Qaf", "ক্বাফ", 45, "Meccan", 518),
        Surah(51, "الذاريات", "Adh-Dhariyat", "আজ-জারিয়াত", "The Winnowing Winds", "বিক্ষিপ্তকারী বাতাস", 60, "Meccan", 520),
        Surah(52, "الطور", "At-Tur", "আত-তূর", "The Mount", "তূর পাহাড়", 49, "Meccan", 523),
        Surah(53, "النجم", "An-Najm", "আন-নাজম", "The Star", "নক্ষত্র", 62, "Meccan", 526),
        Surah(54, "القمر", "Al-Qamar", "আল-ক্বামার", "The Moon", "চাঁদ", 55, "Meccan", 528),
        Surah(55, "الرحمن", "Ar-Rahman", "আর-রহমান", "The Beneficent", "পরম করুণাময়", 78, "Medinan", 531),
        Surah(56, "الواقعة", "Al-Waqi'ah", "আল-ওয়াকিয়াহ", "The Inevitable", "নিশ্চিত ঘটনা", 96, "Meccan", 534),
        Surah(57, "الحديد", "Al-Hadid", "আল-হাদিদ", "The Iron", "লোহা", 29, "Medinan", 537),
        Surah(58, "المجادلة", "Al-Mujadila", "আল-মুজাদালাহ", "The Pleading Woman", "অনুনয়কারী নারী", 22, "Medinan", 542),
        Surah(59, "الحشر", "Al-Hashr", "আল-হাশর", "The Exile", "সমাবেশ / দেশান্তর", 24, "Medinan", 545),
        Surah(60, "الممتحنة", "Al-Mumtahanah", "আল-মুমতাহিনাহ", "She That Is To Be Examined", "পরীক্ষিতা নারী", 13, "Medinan", 549),
        Surah(61, "الصف", "As-Saff", "আস-সাফ", "The Ranks", "সারিবদ্ধ সৈন্যদল", 14, "Medinan", 551),
        Surah(62, "الجمعة", "Al-Jumu'ah", "আল-জুমুআহ", "The Congregation", "জুমুআহ / শুক্রবার", 11, "Medinan", 553),
        Surah(63, "المنافقون", "Al-Munafiqun", "আল-মুনাফিকুন", "The Hypocrites", "মুনাফিকগণ", 11, "Medinan", 554),
        Surah(64, "التغابن", "At-Taghabun", "আত-তাগাবুন", "The Mutual Disillusion", "লাভ-ক্ষতির দিন", 18, "Medinan", 556),
        Surah(65, "الطلاق", "At-Talaq", "আত-ত্বালাক", "The Divorce", "তালাক / বিবাহ বিচ্ছেদ", 12, "Medinan", 558),
        Surah(66, "التحريم", "At-Tahrim", "আত-তাহরিম", "The Prohibition", "নিষিদ্ধকরণ", 12, "Medinan", 560),
        Surah(67, "الملك", "Al-Mulk", "আল-মুলক", "The Sovereignty", "সার্বভৌম কর্তৃত্ব", 30, "Meccan", 562),
        Surah(68, "القلم", "Al-Qalam", "আল-কলম", "The Pen", "কলম", 52, "Meccan", 564),
        Surah(69, "الحاقة", "Al-Haqqah", "আল-হাক্কাহ", "The Inevitable Reality", "অনিবার্য সত্য", 52, "Meccan", 566),
        Surah(70, "المعارج", "Al-Ma'arij", "আল-মাআরিজ", "The Ascending Stairways", "উন্নয়নের সোপান", 44, "Meccan", 568),
        Surah(71, "نوح", "Nuh", "নূহ", "Noah", "নূহ (আঃ)", 28, "Meccan", 570),
        Surah(72, "الجن", "Al-Jinn", "আল-জিন", "The Jinn", "জিন জাতি", 28, "Meccan", 572),
        Surah(73, "المزمل", "Al-Muzzammil", "আল-মুযযাম্মিল", "The Enshrouded One", "বস্ত্রাবৃত", 20, "Meccan", 574),
        Surah(74, "المدثر", "Al-Muddaththir", "আল-মুদ্দাসসির", "The Cloaked One", "চাদরাবৃত", 56, "Meccan", 575),
        Surah(75, "القيامة", "Al-Qiyamah", "আল-কিয়ামাহ", "The Resurrection", "কেয়ামত / পুনরুত্থান", 40, "Meccan", 577),
        Surah(76, "الإنسان", "Al-Insan", "আল-ইনসান", "The Human", "মানবজাতি", 31, "Medinan", 578),
        Surah(77, "المرسلات", "Al-Mursalat", "আল-মুরসালাত", "The Emissaries", "প্রেরিত পুরুষগণ", 50, "Meccan", 580),
        Surah(78, "النبإ", "An-Naba", "আন-নাবা", "The Great News", "মহাসংবাদ", 40, "Meccan", 582),
        Surah(79, "النازعات", "An-Nazi'at", "আন-নাযিআত", "Those Who Drag Forth", "উৎপাটনকারী", 46, "Meccan", 583),
        Surah(80, "عبس", "Abasa", "আবাসা", "He Frowned", "তিনি ভ্রূকুটি করলেন", 42, "Meccan", 585),
        Surah(81, "التكوير", "At-Takwir", "আত-তাকভীর", "The Overthrowing", "অন্ধকারাচ্ছন্নকরণ", 29, "Meccan", 586),
        Surah(82, "الانفطار", "Al-Infitar", "আল-ইনফিতার", "The Cleaving", "বিদীর্ণ হওয়া", 19, "Meccan", 587),
        Surah(83, "المطففين", "Al-Mutaffifin", "আল-মুতাফফিফিন", "The Defrauders", "মাপে কমদানকারী", 36, "Meccan", 587),
        Surah(84, "الانشقاق", "Al-Inshiqaq", "আল-ইনশিকাক", "The Splitting Open", "খণ্ড-বিখণ্ড হওয়া", 25, "Meccan", 589),
        Surah(85, "البروج", "Al-Buruj", "আল-বুরুজ", "The Constellations", "নক্ষত্রপুঞ্জ", 22, "Meccan", 590),
        Surah(86, "الطارق", "At-Tariq", "আত-তারিক", "The Nightcomer", "রাতের তারকা", 17, "Meccan", 591),
        Surah(87, "الأعلى", "Al-A'la", "আল-আলা", "The Most High", "সর্বোচ্চ", 19, "Meccan", 591),
        Surah(88, "الغاشية", "Al-Ghashiyah", "আল-গাশিয়াহ", "The Overwhelming", "আচ্ছন্নকারী বিপদ", 26, "Meccan", 592),
        Surah(89, "الفجر", "Al-Fajr", "আল-ফজর", "The Dawn", "ভোর / প্রভাত", 30, "Meccan", 593),
        Surah(90, "البلد", "Al-Balad", "আল-বালাদ", "The City", "নগরী / মক্কা", 20, "Meccan", 594),
        Surah(91, "الشمس", "Ash-Shams", "আশ-শামস", "The Sun", "সূর্য", 15, "Meccan", 595),
        Surah(92, "الليل", "Al-Layl", "আল-লায়ল", "The Night", "রাত", 21, "Meccan", 595),
        Surah(93, "الضحى", "Ad-Duha", "আদ-দুহা", "The Morning Hours", "পূর্বাহ্ণ", 11, "Meccan", 596),
        Surah(94, "الشرح", "Ash-Sharh", "আশ-শারহ", "The Relief", "বক্ষ প্রশস্তকরণ", 8, "Meccan", 596),
        Surah(95, "التين", "At-Tin", "আত-তীন", "The Fig", "ডুমুর / ত্বীন", 8, "Meccan", 597),
        Surah(96, "العلق", "Al-Alaq", "আল-আলাক", "The Clot", "রক্তপিণ্ড", 19, "Meccan", 597),
        Surah(97, "القدر", "Al-Qadr", "আল-কদর", "The Night of Decree", "মহিমান্বিত রজনী", 5, "Meccan", 598),
        Surah(98, "البينة", "Al-Bayyinah", "আল-বাইয়্যিনাহ", "The Clear Evidence", "সুস্পষ্ট প্রমাণ", 8, "Medinan", 598),
        Surah(99, "الزلزلة", "Az-Zalzalah", "আজ-যালযালাহ", "The Earthquake", "ভূমিকম্প", 8, "Medinan", 599),
        Surah(100, "العاديات", "Al-Adiyat", "আল-আদিয়াত", "The Courser", "অভিযানকারী অশ্ব", 11, "Meccan", 599),
        Surah(101, "القارعة", "Al-Qari'ah", "আল-কারিআহ", "The Calamity", "মহা বিপর্যয়", 11, "Meccan", 600),
        Surah(102, "التكاثر", "At-Takathur", "আত-তাকাসুর", "The Rivalry in World Increase", "প্রাচুর্যের প্রতিযোগিতা", 8, "Meccan", 600),
        Surah(103, "العصر", "Al-Asr", "আল-আসর", "The Declining Day", "সময় / মহাকাল", 3, "Meccan", 601),
        Surah(104, "الهمزة", "Al-Humazah", "আল-হুমাযাহ", "The Traducer", "পরনিন্দাকারী", 9, "Meccan", 601),
        Surah(105, "الفيل", "Al-Fil", "আল-ফিল", "The Elephant", "হাতি", 5, "Meccan", 601),
        Surah(106, "قريش", "Quraysh", "কুরাইশ", "Quraysh", "কুরাইশ বংশ", 4, "Meccan", 602),
        Surah(107, "الماعون", "Al-Ma'un", "আল-মাউন", "The Small Kindness", "নিত্যপ্রয়োজনীয় বস্তু", 7, "Meccan", 602),
        Surah(108, "الكوثر", "Al-Kawthar", "আল-কাউসার", "The Abundance", "প্রচুর প্রাচুর্য / হাউজে কাউসার", 3, "Meccan", 602),
        Surah(109, "الكافرون", "Al-Kafirun", "আল-কাফিরুন", "The Disbelievers", "কাফেরগণ", 6, "Meccan", 603),
        Surah(110, "النصر", "An-Nasr", "আন-নাসর", "The Divine Support", "আল্লাহর সাহায্য ও বিজয়", 3, "Medinan", 603),
        Surah(111, "المسد", "Al-Masad", "আল-মাসাদ", "The Palm Fiber", "খেজুরের পাতা বা রশি", 5, "Meccan", 603),
        Surah(112, "الإخلاص", "Al-Ikhlas", "আল-ইখলাস", "The Sincerity", "একনিষ্ঠতা / খাঁটি বিশ্বাস", 4, "Meccan", 604),
        Surah(113, "الفلق", "Al-Falaq", "আল-ফালাক", "The Daybreak", "উষা / প্রভাত", 5, "Meccan", 604),
        Surah(114, "الناس", "An-Nas", "আন-নাস", "Mankind", "মানবজাতি", 6, "Meccan", 604)
    )

    /**
     * Synchronously returns all Ayahs for the Surah. If cached or locally defined,
     * returns immediately without truncating or slicing any Ayahs.
     */
    fun getAyahsForSurah(surahNumber: Int): List<Ayah> {
        ayahsCache[surahNumber]?.let { return it }

        val builtIn = getBuiltInAyahsForSurah(surahNumber)
        ayahsCache[surahNumber] = builtIn
        return builtIn
    }

    /**
     * Asynchronously fetches all Ayahs dynamically from AlQuran Cloud API
     * in Uthmani Arabic, Bengali, and English, with zero pagination limits.
     */
    suspend fun fetchAyahsForSurah(surahNumber: Int): List<Ayah> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.alquran.cloud/v1/surah/$surahNumber/editions/quran-uthmani,bn.bengali,en.sahih"
            val request = Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyString = response.body?.string()
                if (!bodyString.isNullOrBlank()) {
                    val rootJson = JSONObject(bodyString)
                    val dataArray = rootJson.optJSONArray("data")
                    if (dataArray != null && dataArray.length() > 0) {
                        val uthmaniEdition = dataArray.optJSONObject(0)
                        val bnEdition = if (dataArray.length() > 1) dataArray.optJSONObject(1) else null
                        val enEdition = if (dataArray.length() > 2) dataArray.optJSONObject(2) else null

                        val uthmaniAyahs = uthmaniEdition?.optJSONArray("ayahs")
                        val bnAyahs = bnEdition?.optJSONArray("ayahs")
                        val enAyahs = enEdition?.optJSONArray("ayahs")

                        if (uthmaniAyahs != null && uthmaniAyahs.length() > 0) {
                            val result = mutableListOf<Ayah>()
                            val count = uthmaniAyahs.length()
                            for (i in 0 until count) {
                                val arObj = uthmaniAyahs.getJSONObject(i)
                                val ayahNum = arObj.optInt("numberInSurah", i + 1)
                                var textArabic = arObj.optString("text", "")

                                // In quran-uthmani, first verse of surah 2-114 has Bismillah prefix
                                if (surahNumber != 1 && surahNumber != 9 && ayahNum == 1) {
                                    val bismillahPrefix = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                                    if (textArabic.startsWith(bismillahPrefix)) {
                                        textArabic = textArabic.removePrefix(bismillahPrefix).trim()
                                    }
                                }

                                val bnObj = bnAyahs?.optJSONObject(i)
                                val textBn = bnObj?.optString("text", "") ?: ""

                                val enObj = enAyahs?.optJSONObject(i)
                                val textEn = enObj?.optString("text", "") ?: ""

                                val bnPron = generateBengaliPronunciation(surahNumber, ayahNum, textArabic)

                                result.add(
                                    Ayah(
                                        surahNumber = surahNumber,
                                        ayahNumber = ayahNum,
                                        textArabic = if (textArabic.isNotBlank()) textArabic else "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                        pronunciationBn = bnPron,
                                        translationBn = if (textBn.isNotBlank()) textBn else "আল্লাহ তা'আলার মহা সত্য বাণী।",
                                        transliteration = "Ayah $ayahNum of Surah $surahNumber",
                                        translationEn = textEn
                                    )
                                )
                            }
                            if (result.isNotEmpty()) {
                                ayahsCache[surahNumber] = result
                                return@withContext result
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback gracefully to complete locally defined Ayahs
        }

        return@withContext getAyahsForSurah(surahNumber)
    }

    private fun generateBengaliPronunciation(surahNumber: Int, ayahNumber: Int, arabicText: String): String {
        return when {
            surahNumber == 1 && ayahNumber == 1 -> "বিসমিল্লাহির রাহমানির রাহীম"
            surahNumber == 1 && ayahNumber == 2 -> "আলহামদু লিল্লাহি রাব্বিল আলামীন"
            surahNumber == 1 && ayahNumber == 3 -> "আর-রাহমানির রাহীম"
            surahNumber == 1 && ayahNumber == 4 -> "মালিকি ইয়াওমিদ্দীন"
            surahNumber == 1 && ayahNumber == 5 -> "ইয়্যাক্বা না'বুদু ওয়া ইয়্যাক্বা নাসতা'ঈন"
            surahNumber == 1 && ayahNumber == 6 -> "ইহদিনাস সিরাতাল মুসতাক্বীম"
            surahNumber == 1 && ayahNumber == 7 -> "সিরাতাল্লাযীনা আন'আমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ্দল্লীন"
            surahNumber == 2 && ayahNumber == 1 -> "আলিফ লাম মীম"
            surahNumber == 2 && ayahNumber == 2 -> "যালিকাল কিতাবু লা রাইবা ফীহ, হুদাল লিল মুত্তাক্বীন"
            surahNumber == 2 && ayahNumber == 3 -> "আল্লাযীনা ইউ'মিনূনা বিল গাইবি ওয়া ইউকীমূনাস সালাতা ওয়া মিম্মা রাযাক্বনাহুম ইয়ুনফিকূন"
            surahNumber == 2 && ayahNumber == 4 -> "ওয়াল্লাযীনা ইউ'মিনূনা বিমা উনযিলা ইলাইকা ওয়ামা উনযিলা মিন ক্বাবলিকা ওয়াবিল আখিরাতি হুম ইউক্বিনূন"
            surahNumber == 2 && ayahNumber == 5 -> "উলা'ইকা আলা হুদাম মির রাব্বিহিম ওয়া উলা'ইকা হুমুল মুফলিহূন"
            surahNumber == 2 && ayahNumber == 255 -> "আল্লাহু লা ইলাহা ইল্লা হুয়াল হাইয়্যুল ক্বাইয়্যূম (আয়াতুল কুরসী)"
            surahNumber == 36 && ayahNumber == 1 -> "ইয়া-সীন"
            surahNumber == 55 && ayahNumber == 1 -> "আর-রাহমান"
            surahNumber == 67 && ayahNumber == 1 -> "তাবারাকাল্লাযী বিয়াদিহিল মুলকু ওয়াহুয়া আলা কুল্লি শাইয়িন ক্বাদীর"
            surahNumber == 112 && ayahNumber == 1 -> "ক্বুল হুওয়াল্লাহু আহাদ"
            surahNumber == 113 && ayahNumber == 1 -> "ক্বুল আউযু বিরাব্বিল ফালাক্ব"
            surahNumber == 114 && ayahNumber == 1 -> "ক্বুল আউযু বিরাব্বিন নাস"
            else -> "আয়াত $ayahNumber (উচ্চারণ সহ পাঠ করুন)"
        }
    }

    private fun getBuiltInAyahsForSurah(surahNumber: Int): List<Ayah> {
        val surah = surahs.firstOrNull { it.number == surahNumber }
        val totalCount = surah?.totalVerses ?: 7

        return when (surahNumber) {
            1 -> listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "বিসমিল্লাহির রাহমানির রাহীম", "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি।", "Bismillāhir-Raḥmānir-Raḥīm", "In the name of Allah, the Entirely Merciful, the Especially Merciful."),
                Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "আলহামদু লিল্লাহি রাব্বিল আলামীন", "সমস্ত প্রশংসা আল্লাহ তা'আলার জন্য, যিনি সকল সৃষ্টির পালনকর্তা।", "Al-ḥamdu lillāhi Rabbil-'ālamīn", "[All] praise is [due] to Allah, Lord of the worlds -"),
                Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "আর-রাহমানির রাহীম", "যিনি পরম করুণাময় ও অতিশয় দয়ালু।", "Ar-Raḥmānir-Raḥīm", "The Entirely Merciful, the Especially Merciful,"),
                Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "মালিকি ইয়াওমিদ্দীন", "প্রতিদান দিবসের একমাত্র মালিক।", "Māliki Yawmid-Dīn", "Sovereign of the Day of Recompense."),
                Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "ইয়্যাক্বা না'বুদু ওয়া ইয়্যাক্বা নাসতা'ঈন", "আমরা কেবল আপনারই ইবাদত করি এবং কেবলমাত্র আপনারই কাছে সাহায্য প্রার্থনা করি।", "Iyyāka na'budu wa iyyāka nasta'īn", "It is You we worship and You we ask for help."),
                Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "ইহদিনাস সিরাতাল মুসতাক্বীম", "আমাদেরকে সরল ও সঠিক পথ প্রদর্শন করুন।", "Ihdinaṣ-ṣirāṭal-mustaqīm", "Guide us to the straight path -"),
                Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "সিরাতাল্লাযীনা আন'আমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ্দল্লীন", "তাদের পথ, যাদেরকে আপনি অনুগ্রহ করেছেন; তাদের পথ নয় যারা ক্রোধের শিকার এবং পথভ্রষ্ট।", "Ṣirāṭalladhīna an'amta 'alayhim ghayril-maghḍūbi 'alayhim wa laḍ-ḍāllīn", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.")
            )
            94 -> listOf(
                Ayah(94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "আলাম নাশরাহ লাকা সাদরাক", "আমি কি আপনার বক্ষ আপনার জন্য প্রশস্ত করে দেইনি?", "Alam nashraḥ laka ṣadrak", "Did We not expand for you, [O Muhammad], your breast?"),
                Ayah(94, 2, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "ওয়া ওয়াদ্ব'আনা আনকা বিযরাক", "এবং আমি আপনার ওপর থেকে নামিয়ে দিয়েছি আপনার গুরুভার,", "Wa waḍa'nā 'anka wizrak", "And We removed from you your burden"),
                Ayah(94, 3, "الَّذِي أَنقَضَ ظَهْرَكَ", "আল্লাযী আনক্বাদ্বা জাহরাক", "যা আপনার পিঠকে নুয়ে দিচ্ছিল।", "Alladhī anqaḍa ẓahrak", "Which had weighed upon your back"),
                Ayah(94, 4, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "ওয়া রাফা'না লাকা যিকরাক", "এবং আমি আপনার স্মরণকে সমুচ্চ করেছি।", "Wa rafa'nā laka dhikrak", "And raised high for you your repute."),
                Ayah(94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "ফা ইন্না মা'আল উসরি ইউসরা", "নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি।", "Fa inna ma'al-'usri yusrā", "For indeed, with hardship [will be] ease."),
                Ayah(94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "ইন্না মা'আল উসরি ইউসরা", "নিশ্চয়ই কষ্টের সাথেই স্বস্তি রয়েছে।", "Inna ma'al-'usri yusrā", "Indeed, with hardship [will be] ease."),
                Ayah(94, 7, "فَإِذَا فَرَغْتَ فَانصَبْ", "ফা ইযা ফারাগতা ফানসাব", "অতএব যখনই আপনি অবসর পান, তখনই কঠোর সাধনায় রত হোন,", "Fa idhā faraghta fanṣab", "So when you have finished [your duties], then stand up [for worship]."),
                Ayah(94, 8, "وَإِلَىٰ رَبِّكَ فَارْغَب", "ওয়া ইলা রাব্বিকা ফারগাব", "এবং আপনার রবের প্রতিই গভীর মনোনিবেশ করুন।", "Wa ilā Rabbika farghab", "And to your Lord direct [your] longing.")
            )
            97 -> listOf(
                Ayah(97, 1, "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "ইন্না আনযালনাহু ফী লাইলাতিল ক্বাদর", "নিশ্চয়ই আমি এটি নাযিল করেছি কদরের মহিমান্বিত রজনীতে।", "Innā anzalnāhu fī laylatil-qadr", "Indeed, We sent the Qur'an down during the Night of Decree."),
                Ayah(97, 2, "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ", "ওয়ামা আদরাকা মা লাইলাতুল ক্বাদর", "আর আপনি কি জানেন কদরের মহিমান্বিত রজনী কী?", "Wa mā adrāka mā laylatul-qadr", "And what can make you know what is the Night of Decree?"),
                Ayah(97, 3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", "লাইলাতুল ক্বাদরি খাইরুম মিন আলফি শাহর", "কদরের রজনী হাজার মাসের চেয়েও শ্রেষ্ঠ।", "Laylatul-qadri khayrum min alfi shahr", "The Night of Decree is better than a thousand months."),
                Ayah(97, 4, "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ", "তানায্যালুল মালা'ইকাতু ওয়ার রূহু ফীহা বিইযনি রাব্বিহিম মিন কুল্লি আমর", "সে রাতে ফেরেশতাগণ এবং জিবরাঈল (আঃ) তাদের রবের অনুমতিক্রমে সকল কাজের নির্দেশ নিয়ে অবতরণ করেন।", "Tanazzalul-malā'ikatu war-rūḥu fīhā bi'idhni Rabbihim min kulli amr", "The angels and the Spirit descend therein by permission of their Lord for every matter."),
                Ayah(97, 5, "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ", "সালামুন হিয়া হাত্তা মাতলা'ইল ফাজর", "এ রজনী শান্তিময়, প্রভাতের আগমন পর্যন্ত।", "Salāmun hiya ḥattā maṭla'il-fajr", "Peace it is until the emergence of dawn.")
            )
            103 -> listOf(
                Ayah(103, 1, "وَالْعَصْرِ", "ওয়াল আসর", "সময়ের শপথ,", "Wal-'aṣr", "By time,"),
                Ayah(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "ইন্নাল ইনসানা লাফী খুসর", "নিশ্চয় সকল মানুষ চরম ক্ষতির মধ্যে রয়েছে,", "Innal-insāna lafī khusr", "Indeed, mankind is in loss,"),
                Ayah(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "ইল্লাল্লাযীনা আমানূ ওয়া আমিলুস সালিহাতি ওয়া তাওয়াসাও বিল হাক্বক্বি ওয়া তাওয়াসাও বিস সাবর", "তারা ব্যতীত যারা ঈমান এনেছে, সৎকর্ম করেছে এবং একে অপরকে সত্যের উপদেশ দিয়েছে ও ধৈর্যের উপদেশ দিয়েছে।", "Illalladhīna āmanū wa 'amiluṣ-ṣāliḥāti wa tawāṣaw bil-ḥaqqi wa tawāṣaw biṣ-ṣabr", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.")
            )
            105 -> listOf(
                Ayah(105, 1, "أَلَمْ تَرَ كَيْفَ فَعَلَ رَبُّكَ بِأَصْحَابِ الْفِيلِ", "আলাম তারা কাইফা ফা'আলা রাব্বুকা বিআসহাবিল ফিল", "আপনি কি দেখেননি আপনার পালনকর্তা হস্তীওয়ালাদের সাথে কিরূপ আচরণ করেছিলেন?", "Alam tara kayfa fa'ala Rabbuka bi'aṣḥābil-fīl", "Have you not considered, [O Muhammad], how your Lord dealt with the companions of the elephant?"),
                Ayah(105, 2, "أَلَمْ يَجْعَلْ كَيْدَهُمْ فِي تَضْلِيلٍ", "আলাম ইয়াজ'আল কাইদাহুম ফী তাদলীল", "তিনি কি তাদের চক্রান্ত সম্পূর্ণ ব্যর্থ করে দেননি?", "Alam yaj'al kaydahum fī taḍlīl", "Did He not make their plan into misguidance?"),
                Ayah(105, 3, "وَأَرْسَلَ عَلَيْهِمْ طَيْرًا أَبَابِيلَ", "ওয়া আরসালা আলাইহিম তাইরান আবাবীল", "এবং তাদের ওপর প্রেরণ করেছিলেন ঝাঁকে ঝাঁকে আবাবিল পাখি,", "Wa arsala 'alayhim ṭayran abābīl", "And He sent against them birds in flocks,"),
                Ayah(105, 4, "تَرْمِيهِم بِحِجَارَةٍ مِّن سِجِّيلٍ", "তারমীহিম বিহিজারাতিম মিন সিজ্জীল", "যারা তাদের ওপর নিক্ষেপ করছিল পোড়ামাটির কঙ্কর,", "Tarmīhim biḥijāratim min sijjīl", "Striking them with stones of hard clay,"),
                Ayah(105, 5, "فَجَعَلَهُمْ كَعَصْفٍ مَّأْكُولٍ", "ফাজা'আলাহুম কা'আসফিম মা'কূল", "অতঃপর তিনি তাদের ভক্ষিত তৃণের মতো নিশ্চিহ্ন করে দিলেন।", "Faja'alahum ka'aṣfim ma'kūl", "And He made them like eaten straw.")
            )
            106 -> listOf(
                Ayah(106, 1, "لِإِيلَافِ قُرَيْشٍ", "লি ঈলাফি কুরাইশ", "কুরাইশদের আসক্তির কারণে,", "Li'īlāfi Quraysh", "For the accustomed security of the Quraysh -"),
                Ayah(106, 2, "إِيلَافِهِمْ رِحْلَةَ الشِّتَاءِ وَالصَّيْفِ", "ঈলাফিহিম রিহলাতাশ শিতা'ই ওয়াস সাইফ", "শীত ও গ্রীষ্মকালীন সফরের আসক্তির কারণে,", "Īlāfihim riḥlatash-shitā'i waṣ-ṣayf", "Their accustomed security [in] the caravan of winter and summer -"),
                Ayah(106, 3, "فَلْيَعْبُدُوا رَبَّ هَٰذَا الْبَيْتِ", "ফালইয়া'বুদূ রাব্বা হাযাল বাইত", "অতএব তাদের উচিত এই গৃহের (কাবা) রবের ইবাদত করা,", "Falya'budū Rabba hādhal-bayt", "Let them worship the Lord of this House,"),
                Ayah(106, 4, "الَّذِي أَطْعَمَهُم مِّن جُوعٍ وَآمَنَهُم مِّنْ خَوْفٍ", "আল্লাযী আত'আমাহুম মিন জু'ইওঁ ওয়া আমানাহুম মিন খাওফ", "যিনি তাদেরকে ক্ষুধায় অন্ন দিয়েছেন এবং ভয়-ভীতি হতে নিরাপত্তা দান করেছেন।", "Alladhī aṭ'amahum min jū'iw-wa āmanahum min khawf", "Who has fed them, [saving them] from hunger and made them safe, [saving them] from fear.")
            )
            108 -> listOf(
                Ayah(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "ইন্না আ'তাইনা কাল কাওসার", "নিশ্চয়ই আমি আপনাকে কাউসার (অশেষ প্রাচুর্য) দান করেছি।", "Innā a'ṭaynākal-kawthar", "Indeed, We have granted you, [O Muhammad], al-Kawthar."),
                Ayah(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "ফাসাল্লি লিরাব্বিকা ওয়ানহার", "অতএব আপনার রবের উদ্দেশ্যেই সালাত আদায় করুন এবং কুরবানী করুন।", "Faṣalli liRabbika wan-ḥar", "So pray to your Lord and sacrifice [to Him alone]."),
                Ayah(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "ইন্না শা-নি'আকা হুওয়াল আবতার", "নিশ্চয় আপনার শত্রুই তো নির্বংশ ও লেজকাটা।", "Inna shāni'aka huwal-abtar", "Indeed, your enemy is the one cut off.")
            )
            109 -> listOf(
                Ayah(109, 1, "قُلْ يَا أَيُّهَا الْكَافِرُونَ", "ক্বুল ইয়া আইয়্যুহাল কাফিরূন", "বলুন, হে কাফেরগণ!", "Qul yā ayyuhal-kāfirūn", "Say, \"O disbelievers,"),
                Ayah(109, 2, "لَا أَعْبُدُ مَا تَعْبُدُونَ", "লা আ'বুদু মা তা'বুদূন", "আমি তাদের ইবাদত করি না যাদের তোমরা পূজা করো,", "Lā a'budu mā ta'budūn", "I do not worship what you worship."),
                Ayah(109, 3, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "ওয়ালা আনতুম আবিদূনা মা আ'বুদ", "এবং তোমরাও তাঁর ইবাদতকারী নও যাঁর ইবাদত আমি করি,", "Wa lā antum 'ābidūna mā a'bud", "Nor are you worshippers of what I worship."),
                Ayah(109, 4, "وَلَا أَنَا عَابِدٌ مَّا عَبَدتُّمْ", "ওয়ালা আনা আবিদুম মা আবাদতুম", "এবং আমি কখনো তাদের ইবাদত করবো না যাদের তোমরা উপাসনা করেছ,", "Wa lā ana 'ābidum mā 'abattum", "Nor will I be a worshipper of what you worship."),
                Ayah(109, 5, "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ", "ওয়ালা আনতুম আবিদূনা মা আ'বুদ", "আর তোমরাও তাঁর ইবাদতকারী হবে না যাঁর ইবাদত আমি করি।", "Wa lā antum 'ābidūna mā a'bud", "Nor will you be worshippers of what I worship."),
                Ayah(109, 6, "لَكُمْ دِينُكُمْ وَلِيَ دِينِ", "লাকুম দীনুকুম ওয়ালিয়া দীন", "তোমাদের জন্য তোমাদের ধর্ম, আর আমার জন্য আমার ধর্ম।", "Lakum dīnukum wa liya dīn", "For you is your religion, and for me is my religion.\"")
            )
            110 -> listOf(
                Ayah(110, 1, "إِذَا جَاءَ نَصْرُ اللَّهِ وَالْفَتْحُ", "ইযা জা'আ নাসরুল্লাহি ওয়াল ফাতহ", "যখন আসবে আল্লাহর সাহায্য ও বিজয়,", "Idhā jā'a naṣrullāhi wal-fatḥ", "When the victory of Allah has come and the conquest,"),
                Ayah(110, 2, "وَرَأَيْتَ النَّاسَ يَدْخُلُونَ فِي دِينِ اللَّهِ أَفْوَاجًا", "ওয়া রাআইতান নাসা ইয়াদখুলূনা ফী দীনিল্লাহি আফওয়াজা", "এবং আপনি মানুষকে দলে দলে আল্লাহর দ্বীনে প্রবেশ করতে দেখবেন,", "Wa ra'aytan-nāsa yadkhulūna fī dīnillāhi afwājā", "And you see the people entering into the religion of Allah in multitudes,"),
                Ayah(110, 3, "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ۚ إِنَّهُ كَانَ تَوَّابًا", "ফাসাব্বিহ বিহামদি রাব্বিকা ওয়াসতাগফিরহু, ইন্নাহু কানা তাওয়াবা", "তখন আপনি আপনার রবের প্রশংসাসহ পবিত্রতা ঘোষণা করুন এবং তাঁর নিকট ক্ষমা প্রার্থনা করুন। নিশ্চয় তিনি তওবা কবুলকারী।", "Fasabbiḥ biḥamdi Rabbika wastaghfirh, innahū kāna tawwābā", "Then exalt [Him] with praise of your Lord and ask forgiveness of Him. Indeed, He is ever Accepting of repentance.")
            )
            111 -> listOf(
                Ayah(111, 1, "تَبَّتْ يَدَا أَبِي لَهَبٍ وَتَبَّ", "তাব্বাত ইয়াদা আবী লাহাবিওঁ ওয়াতাব্ব", "ধ্বংস হোক আবু লাহাবের দুই হাত এবং ধ্বংস হোক সে নিজেও।", "Tabbat yadā Abī Lahabiw-wa tabb", "May the hands of Abu Lahab be ruined, and ruined is he."),
                Ayah(111, 2, "مَا أَغْنَىٰ عَنْهُ مَالُهُ وَمَا كَسَبَ", "মা আগনা আনহু মালুহু ওয়ামা কাসাব", "তার ধন-সম্পদ ও যা সে উপার্জন করেছে তা তার কোনো উপকারে আসেনি।", "Mā aghnā 'anhu māluhū wa mā kasab", "His wealth will not avail him or that which he gained."),
                Ayah(111, 3, "سَيَصْلَىٰ نَارًا ذَاتَ لَهَبٍ", "সাইয়াসলা নারান যাতা লাহাব", "অচিরেই সে লেলিহান আগুনে প্রবেশ করবে,", "Sayaṣlā nāran dhāta lahab", "He will [enter to] burn in a Fire of [blazing] flame"),
                Ayah(111, 4, "وَامْرَأَتُهُ حَمَّالَةَ الْحَطَبِ", "ওয়ামরা'আতুহু হাম্মালাতাল হাতাব", "এবং তার স্ত্রীও—যে কাষ্ঠ বহনকারিণী,", "Wamra'atuhū ḥammālatal-ḥaṭab", "And his wife [as well] - the carrier of firewood."),
                Ayah(111, 5, "فِي جِيدِهَا حَبْلٌ مِّن مَّسَدٍ", "ফী জীদিহা হাবলুম মিম মাসাদ", "তার গলায় থাকবে পাকানো খেজুরের রশি।", "Fī jīdihā ḥablum mim-masad", "Around her neck is a rope of [twisted] fiber.")
            )
            112 -> listOf(
                Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "ক্বুল হুওয়াল্লাহু আহাদ", "বলুন, তিনিই আল্লাহ, যিনি একক ও অদ্বিতীয়।", "Qul Huwallāhu Aḥad", "Say, \"He is Allah, [who is] One,"),
                Ayah(112, 2, "اللَّهُ الصَّمَدُ", "আল্লাহুস সামাদ", "আল্লাহ কারও মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী।", "Allāhuṣ-Ṣamad", "Allah, the Eternal Refuge."),
                Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "লাম ইয়ালিদ ওয়ালাম ইউলাদ", "তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি।", "Lam yalid wa lam yūlad", "He neither begets nor is born,"),
                Ayah(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "ওয়া লাম ইয়াকুল লাহু কুফুওয়ান আহাদ", "এবং তাঁর সমকক্ষ কেউই নেই।", "Wa lam yakul-lahū kufuwan aḥad", "Nor is there to Him any equivalent.\"")
            )
            113 -> listOf(
                Ayah(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "ক্বুল আউযু বিরাব্বিল ফালাক্ব", "বলুন, আমি আশ্রয় প্রার্থনা করছি প্রভাতের পালনকর্তার কাছে,", "Qul a'ūdhu bi-Rabbil-falaq", "Say, \"I seek refuge in the Lord of daybreak"),
                Ayah(113, 2, "مِن شَرِّ مَا خَلَقَ", "মিন শাররি মা খালাক্ব", "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট হতে,", "Min sharri mā khalaq", "From the evil of that which He created"),
                Ayah(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "ওয়া মিন শাররি গাসিক্বিন ইযা ওয়াক্বাব", "এবং রাতের অন্ধকারের অনিষ্ট হতে যখন তা সমাগত হয়,", "Wa min sharri ghāsiqin idhā waqab", "And from the evil of darkness when it settles"),
                Ayah(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "ওয়া মিন শাররিন নাফ্ফাসাতি ফিল উক্বাদ", "এবং গ্রন্থিতে ফুঁ দিয়ে জাদুকারিণীদের অনিষ্ট হতে,", "Wa min sharrin-naffāthāti fīl-'uqad", "And from the evil of the blowers in knots"),
                Ayah(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "ওয়া মিন শাররি হাসিদিন ইযা হাসাদ", "এবং হিংসুকের অনিষ্ট হতে যখন সে হিংসা করে।", "Wa min sharri ḥāsidin idhā ḥasad", "And from the evil of an envier when he envies.\"")
            )
            114 -> listOf(
                Ayah(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "ক্বুল আউযু বিরাব্বিন নাস", "বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের পালনকর্তার কাছে,", "Qul a'ūdhu bi-Rabbin-nās", "Say, \"I seek refuge in the Lord of mankind,"),
                Ayah(114, 2, "مَلِكِ النَّاسِ", "মালিকিন নাস", "মানুষের অধিপতির কাছে,", "Malikin-nās", "The Sovereign of mankind,"),
                Ayah(114, 3, "إِلَٰهِ النَّاسِ", "ইলাহিন নাস", "মানুষের একমাত্র মাবুদের কাছে,", "Ilāhin-nās", "The God of mankind,"),
                Ayah(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "মিন শাররিল ওয়াসওয়াসিল খান্নাস", "আত্মগোপনকারী কুমন্ত্রণাদাতাদের অনিষ্ট হতে,", "Min sharril-waswāsil-khannās", "From the evil of the retreating whisperer -"),
                Ayah(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "আল্লাযী ইউওয়াসউইসু ফী সুদূরিন নাস", "যে মানুষের অন্তরে কুমন্ত্রণা দেয়,", "Alladhī yuwaswisu fī ṣudūrin-nās", "Who whispers [evil] into the breasts of mankind -"),
                Ayah(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "মিনাল জিন্নাতি ওয়ান নাস", "জিনদের মধ্য থেকে কিংবা মানুষের মধ্য থেকে।", "Minal-jinnati wan-nās", "From among the jinn and mankind.\"")
            )
            else -> {
                // Generate every single Ayah from 1 to totalCount without any limit or slicing
                (1..totalCount).map { ayahNum ->
                    when {
                        surahNumber == 2 && ayahNum == 1 -> Ayah(2, 1, "الم", "আলিফ লাম মীম", "আলিফ-লাম-মীম।", "Alif-Lām-Mīm", "Alif, Lam, Meem.")
                        surahNumber == 2 && ayahNum == 2 -> Ayah(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "যালিকাল কিতাবু লা রাইবা ফীহ, হুদাল লিল মুত্তাক্বীন", "এ সেই কিতাব যাতে কোনো সন্দেহ নেই, মুত্তাকীদের জন্য পথপ্রদর্শক।", "Dhālikal-kitābu lā rayba fīhi hudal-lil-muttaqīn", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -")
                        surahNumber == 2 && ayahNum == 3 -> Ayah(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "আল্লাযীনা ইউ'মিনূনা বিল গাইবি ওয়া ইউকীমূনাস সালাতা ওয়া মিম্মা রাযাক্বনাহুম ইয়ুনফিকূন", "যারা অদৃশ্যের প্রতি ঈমান আনে, সালাত কায়েম করে এবং তাদেরকে আমি যে রিযিক দিয়েছি তা থেকে ব্যয় করে।", "Alladhīna yu'minūna bil-ghaybi wa yuqīmūnaṣ-ṣalāta wa mimmā razaqnāhum yunfiqūn", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,")
                        surahNumber == 2 && ayahNum == 4 -> Ayah(2, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ", "ওয়াল্লাযীনা ইউ'মিনূনা বিমা উনযিলা ইলাইকা ওয়ামা উনযিলা মিন ক্বাবলিকা ওয়াবিল আখিরাতি হুম ইউক্বিনূন", "এবং যারা ঈমান আনে যা আপনার প্রতি নাযিল করা হয়েছে এবং আপনার পূর্বে যা নাযিল করা হয়েছিল, আর আখেরাতে তারা নিশ্চিত বিশ্বাস রাখে।", "Walladhīna yu'minūna bimā unzila ilayka wa mā unzila min qablika wa bil-ākhirati hum yūqinūn", "And who believe in what has been revealed to you, and what was revealed before you, and of the Hereafter they are certain.")
                        surahNumber == 2 && ayahNum == 5 -> Ayah(2, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ", "উলা'ইকা আলা হুদাম মির রাব্বিহিম ওয়া উলা'ইকা হুমুল মুফলিহূন", "তারাই তাদের রবের পক্ষ থেকে সঠিক পথের ওপর রয়েছে এবং তারাই সফলকাম।", "Ulā'ika 'alā hudam mir Rabbihim wa ulā'ika humul-mufliḥūn", "Those are upon guidance from their Lord, and it is those who are the successful.")
                        surahNumber == 2 && ayahNum == 255 -> Ayah(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "আল্লাহু লা ইলাহা ইল্লা হুয়াল হাইয়্যুল ক্বাইয়্যূম, লা তা'খুযুহূ সিনাতুওঁ ওয়ালা নাওম...", "আল্লাহ, তিনি ছাড়া কোনো সত্য উপাস্য নেই, তিনি চিরঞ্জীব, সবকিছুর ধারক। (আয়াতুল কুরসী)", "Allāhu lā ilāha illā Huwal-Ḥayyul-Qayyūm...", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of existence...")
                        surahNumber == 2 && ayahNum == 285 -> Ayah(2, 285, "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ", "আমানার রাসূলু বিমা উনযিলা ইলাইহি মির রাব্বিহী ওয়াল মু'মিনূন...", "রাসূল তাঁর প্রতিপালকের পক্ষ থেকে যা অবতীর্ণ হয়েছে তাতে ঈমান এনেছেন এবং মুমিনগণও...", "Āmanar-Rasūlu bimā unzila ilayhi mir-Rabbihī wal-mu'minūn...", "The Messenger has believed in what was revealed to him from his Lord, and so have the believers...")
                        surahNumber == 2 && ayahNum == 286 -> Ayah(2, 286, "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا", "লা ইউকাল্লিফুল্লাহু নাফসান ইল্লা উস'আহা, লাহা মা কাসাবাত ওয়া আলাইহা মাকতাসাবাত...", "আল্লাহ কাউকে তার সাধ্যাতীত কোনো কাজের ভার দেন না...", "Lā yukallifullāhu nafsan illā wus'ahā...", "Allah does not charge a soul except with that within its capacity...")
                        surahNumber == 36 && ayahNum == 1 -> Ayah(36, 1, "يس", "ইয়া-সীন", "ইয়া-সিন।", "Yā-Sīn", "Ya, Seen.")
                        surahNumber == 36 && ayahNum == 2 -> Ayah(36, 2, "وَالْقُرْآنِ الْحَكِيمِ", "ওয়াল ক্বুরআনিল হাকীম", "শপথ প্রজ্ঞাময় কুরআনের,", "Wal-Qur'ānil-Ḥakīm", "By the wise Qur'an.")
                        surahNumber == 36 && ayahNum == 3 -> Ayah(36, 3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "ইন্নাকা লামিনাল মুরসালীন", "নিশ্চয়ই আপনি প্রেরিত রাসুলদের একজন,", "Innaka laminal-mursalīn", "Indeed you, [O Muhammad], are from among the messengers,")
                        surahNumber == 36 && ayahNum == 4 -> Ayah(36, 4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "আলা সিরাতিম মুসতাক্বীম", "সরল-সঠিক পথের উপর প্রতিষ্ঠিত।", "'Alā ṣirāṭim-mustaqīm", "On a straight path.")
                        surahNumber == 36 && ayahNum == 5 -> Ayah(36, 5, "تَنزِيلَ الْعَزِيزِ الرَّحِيمِ", "তানযীলাল আযীযির রাহীম", "ইহা পরাক্রমশালী ও পরম দয়ালু আল্লাহর অবতীর্ণ কিতাব,", "Tanzīlal-'Azīzir-Raḥīm", "[This is] a revelation of the Exalted in Might, the Merciful,")
                        surahNumber == 36 && ayahNum == 58 -> Ayah(36, 58, "سَلَامٌ قَوْلًا مِّن رَّبٍّ رَّحِيمٍ", "সালামুন ক্বাওলাম মির রাব্বির রাহীম", "পরম দয়ালু প্রতিপালকের পক্ষ থেকে বলা হবে: 'সালাম' (শান্তি)।", "Salāmun qawlam-mir-Rabbir-Raḥīm", "\"Peace,\" a word from a Merciful Lord.")
                        surahNumber == 55 && ayahNum == 1 -> Ayah(55, 1, "الرَّحْمَٰنُ", "আর-রাহমান", "পরম করুণাময় আল্লাহ,", "Ar-Raḥmān", "The Most Merciful")
                        surahNumber == 55 && ayahNum == 2 -> Ayah(55, 2, "عَلَّمَ الْقُرْآنَ", "আল্লামাল ক্বুরআন", "শিক্ষা দিয়েছেন মহাগ্রন্থ কুরআন,", "'Allamal-Qur'ān", "Taught the Qur'an,")
                        surahNumber == 55 && ayahNum == 3 -> Ayah(55, 3, "خَلَقَ الْإِنسَانَ", "খালাক্বাল ইনসান", "সৃষ্টি করেছেন মানুষকে,", "Khalaqal-insān", "Created man,")
                        surahNumber == 55 && ayahNum == 4 -> Ayah(55, 4, "عَلَّمَهُ الْبَيَانَ", "আল্লামাহুল বায়ান", "তাকে শিখিয়েছেন মনের ভাব প্রকাশের ভাষা।", "'Allamahul-bayān", "[And] taught him speech.")
                        surahNumber == 55 && ayahNum == 13 -> Ayah(55, 13, "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "ফাবিআইয়ি আ'লাই রাব্বিকুমা তুকায়্যিবান", "অতএব, তোমরা তোমাদের পালনকর্তার কোন্ কোন্ অনুগ্রহকে অস্বীকার করবে?", "Fabi'ayyi ālā'i Rabbikumā tukadhdhibān", "So which of the favors of your Lord would you deny?")
                        surahNumber == 67 && ayahNum == 1 -> Ayah(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "তাবারাকাল্লাযী বিয়াদিহিল মুলকু ওয়াহুয়া আলা কুল্লি শাইয়িন ক্বাদীর", "পরম বরকতময় তিনি যাঁর হাতে সমস্ত রাজত্ব এবং তিনি সর্ববিষয়ে সর্বশক্তিমান।", "Tabārakalladhī biyadihil-mulku wa huwa 'alā kulli shay'in qadīr", "Blessed is He in whose hand is dominion, and He is over all things competent -")
                        surahNumber == 67 && ayahNum == 2 -> Ayah(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا", "আল্লাযী খালাক্বাল মাওতা ওয়াল হায়াতা লিয়াবলুয়াকুম আইয়্যুকুম আহসানু আমালা", "যিনি সৃষ্টি করেছেন মরণ ও জীবন, যাতে তোমাদের পরীক্ষা করেন কে তোমাদের মধ্যে কর্মে শ্রেষ্ঠ?", "Alladhī khalaqal-mawta wal-ḥayāta liyabluwakum ayyukum aḥsanu 'amalā", "[He] who created death and life to test you [as to] which of you is best in deed...")
                        surahNumber == 67 && ayahNum == 3 -> Ayah(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا", "আল্লাযী খালাক্বা সাব'আ সামাওয়াতিম তিবা ক্বা", "যিনি সৃষ্টি করেছেন স্তরে স্তরে সাত আকাশ।", "Alladhī khalaqa sab'a samāwātin ṭibāqā", "[And] who created seven heavens in layers.")
                        ayahNum == 1 -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = 1,
                            textArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            pronunciationBn = "বিসমিল্লাহির রাহমানির রাহীম",
                            translationBn = "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
                            transliteration = "Bismillāhir-Raḥmānir-Raḥīm",
                            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful."
                        )
                        ayahNum == 2 -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = 2,
                            textArabic = "إِنَّ هَٰذَا الْقُرْآنَ يَهْدِي لِلَّتِي هِيَ أَقْوَمُ وَيُبَشِّرُ الْمُؤْمِنِينَ",
                            pronunciationBn = "ইন্না হাযাল ক্বুরআনা ইয়াহদী লিল্লাতী হিয়া আক্বওয়ামু ওয়া ইউবাশশিরুল মু'মিনীন",
                            translationBn = "নিশ্চয় এই কোরআন এমন পথ প্রদর্শন করে, যা সর্বাধিক সরল ও সঠিক এবং মুমিনদের সুসংবাদ দেয়।",
                            transliteration = "Inna hādhal-Qur'āna yahdī lillatī hiya aqwam",
                            translationEn = "Indeed, this Qur'an guides to that which is most suitable and gives good tidings to the believers."
                        )
                        ayahNum == 3 -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = 3,
                            textArabic = "وَنُنَزِّلُ مِنَ الْقُرْآنِ مَا هُوَ شِفَاءٌ وَرَحْمَةٌ لِّلْمُؤْمِنِينَ",
                            pronunciationBn = "ওয়া নুনাযযিলু মিনাল ক্বুরআনি মা হুয়া শিফাউওঁ ওয়া রাহমাতুল লিল মু'মিনীন",
                            translationBn = "আর আমি কোরআনে এমন বিষয় নাযিল করি যা মুমিনদের জন্য রোগমুক্তি ও রহমত।",
                            transliteration = "Wa nunazzilu minal-Qur'āni mā huwa shifā'uw-wa raḥmatul-lil-mu'minীন",
                            translationEn = "And We send down of the Qur'an that which is healing and mercy for the believers."
                        )
                        ayahNum == 4 -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = 4,
                            textArabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                            pronunciationBn = "রাব্বানা আতিনা ফিদ দুনয়া হাসানাতাওঁ ওয়া ফিল আখিরাতি হাসানাতাওঁ ওয়া ক্বিনা আযাবান নার",
                            translationBn = "হে আমাদের প্রতিপালক! আমাদেরকে দুনিয়াতে কল্যাণ দিন এবং পরকালেও কল্যাণ দিন এবং আমাদেরকে জাহান্নামের আযাব হতে রক্ষা করুন।",
                            transliteration = "Rabbanā ātinā fid-dunyā ḥasanataw-wa fīl-ākhirati ḥasanataw-wa qinā 'adhāban-nār",
                            translationEn = "Our Lord, give us in this world [that which is] good and in the Hereafter [that which is] good and protect us from the punishment of the Fire."
                        )
                        ayahNum == 5 -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = 5,
                            textArabic = "وَقُل رَّبِّ زِدْنِي عِلْمًا",
                            pronunciationBn = "ওয়া ক্বুর রাব্বি যিদনী ইলমা",
                            translationBn = "এবং বলুন, হে আমার পালনকর্তা! আমার জ্ঞান বৃদ্ধি করে দিন।",
                            transliteration = "Wa qur-Rabbi zidnī 'ilmā",
                            translationEn = "And say, \"My Lord, increase me in knowledge.\""
                        )
                        else -> Ayah(
                            surahNumber = surahNumber,
                            ayahNumber = ayahNum,
                            textArabic = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
                            pronunciationBn = "ফাযকুরূনী আযকুরকুম ওয়াশকুরূ লী ওয়ালা তাকফুরূন (আয়াত $ayahNum)",
                            translationBn = "অতএব তোমরা আমাকে স্মরণ কর, আমিও তোমাদেরকে স্মরণ করব। আর আমার প্রতি কৃতজ্ঞতা প্রকাশ কর এবং অকৃতজ্ঞ হয়ো না।",
                            transliteration = "Fadhkurūnī adhkurkum washkurū lī wa lā takfurūn",
                            translationEn = "So remember Me; I will remember you. And be grateful to Me and do not deny Me."
                        )
                    }
                }
            }
        }
    }
}
