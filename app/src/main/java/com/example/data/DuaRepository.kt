package com.example.data

import com.example.model.DuaCategory
import com.example.model.IslamicDua

object DuaRepository {

    val duas: List<IslamicDua> = listOf(
        // Fasting & Ramadan
        IslamicDua(
            id = "niyyah_fasting",
            category = DuaCategory.FASTING_RAMADAN,
            titleEn = "Intention for Fasting (Niyyah)",
            titleBn = "রোজার নিয়ত",
            arabicText = "نَوَيْتُ أَنْ أَصُومَ غَدًا عَنْ أَدَاءِ فَرْضِ شَهْرِ رَمَضَانَ هَذِهِ السَّنَةِ لِلَّهِ تَعَالَى",
            transliteration = "Nawaitu an asūma ghadan 'an adā'i fardi shahri ramadāna hādhihis-sanati lillāhi ta'ālā.",
            translationEn = "I intend to fast tomorrow in fulfillment of the obligation of this month of Ramadan for the sake of Allah the Almighty.",
            translationBn = "আমি আল্লাহ তা\'আলার সন্তুষ্টির জন্য আগামীকালের রমজান মাসের ফরজ রোজা রাখার নিয়ত করলাম।",
            reference = "Sunan Abi Dawud",
            virtues = "Making intention from the night or before dawn is essential for the validity of the fast."
        ),
        IslamicDua(
            id = "iftar_dua",
            category = DuaCategory.FASTING_RAMADAN,
            titleEn = "Dua for Breaking the Fast (Iftar)",
            titleBn = "ইফতারের দু\'আ",
            arabicText = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
            transliteration = "Dhahabadh-dhama'u wabtallatil-'urūqu wa thabatal-ajru inshā'Allāh.",
            translationEn = "The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            translationBn = "পিপাসা দূরীভূত হলো, শিরা-উপশিরা সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান নিশ্চিত হলো।",
            reference = "Abu Dawud (2357)",
            virtues = "Said immediately after breaking the fast with dates or water."
        ),
        IslamicDua(
            id = "iftar_dua_allahumma",
            category = DuaCategory.FASTING_RAMADAN,
            titleEn = "Traditional Iftar Dua",
            titleBn = "প্রচলিত ইফতারের দু\'আ",
            arabicText = "اللَّهُمَّ لَكَ صُمْتُ وَعَلَى رِزْقِكَ أَفْطَرْتُ",
            transliteration = "Allāhumma laka ṣumtu wa 'alā rizqika afṭartu.",
            translationEn = "O Allah, for You I have fasted, and with Your provision I have broken my fast.",
            translationBn = "হে আল্লাহ, আমি আপনারই সন্তুষ্টির জন্য রোজা রেখেছিলাম এবং আপনারই দেওয়া রিজিক দিয়ে ইফতার করছি।",
            reference = "Abu Dawud (2358)"
        ),
        // Morning & Evening
        IslamicDua(
            id = "sayyidul_istighfar",
            category = DuaCategory.MORNING_EVENING,
            titleEn = "Sayyidul Istighfar (Chief of Forgiveness)",
            titleBn = "সাইয়্যিদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma Anta Rabbī lā ilāha illā Anta, khalaqtanī wa anā 'abduka, wa anā 'alā 'ahdika wa wa'dika mastata'tu, a'ūdhu bika min sharri mā ṣana'tu, abū'u laka bini'matika 'alayya, wa abū'u bidhanbī faghfir lī fa'innahū lā yaghfirudh-dhunūba illā Anta.",
            translationEn = "O Allah, You are my Lord, there is no deity except You. You created me and I am Your servant, and I abide by Your covenant and promise as best as I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favors upon me and I confess my sins to You, so forgive me, for none forgives sins except You.",
            translationBn = "হে আল্লাহ! আপনি আমার প্রতিপালক। আপনি ছাড়া কোনো ইলাহ নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি সাধ্যমত আপনার সাথে কৃত অঙ্গীকার ও প্রতিশ্রুতিতে অটল রয়েছি। আমি যা করেছি তার অনিষ্ট থেকে আপনার আশ্রয় চাই। আমার উপর আপনার সমস্ত নিয়ামত স্বীকার করছি এবং আমার পাপ স্বীকার করছি; অতএব আমাকে ক্ষমা করুন, কারণ আপনি ছাড়া কেউ গুনাহ ক্ষমা করতে পারে না।",
            reference = "Sahih al-Bukhari (6306)",
            virtues = "Whoever recites this during the day with conviction and dies before evening will be among the people of Paradise."
        ),
        IslamicDua(
            id = "morning_praise",
            category = DuaCategory.MORNING_EVENING,
            titleEn = "Morning Supplication for Protection",
            titleBn = "সকালের নিরাপত্তা ও হেদায়েতের দু\'আ",
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ",
            transliteration = "Aṣbaḥnā wa aṣbaḥal-mulku lillāhi, wal-ḥamdu lillāhi, lā ilāha illallāhu waḥdahū lā sharīka lah.",
            translationEn = "We have entered the morning and the entire dominion belongs to Allah. Praise be to Allah. There is no god but Allah alone, without partner.",
            translationBn = "আমরা ভোরে উপনীত হয়েছি এবং সমস্ত রাজত্ব মহান আল্লাহরই রয়েছে। সকল প্রশংসা আল্লাহর। আল্লাহ ছাড়া কোনো সত্য মাবুদ নেই, তিনি একক, তাঁর কোনো শরিক নেই।",
            reference = "Sahih Muslim (2723)"
        ),
        // Daily Life
        IslamicDua(
            id = "waking_up",
            category = DuaCategory.DAILY_LIFE,
            titleEn = "Upon Waking Up",
            titleBn = "ঘুম থেকে ওঠার দু\'আ",
            arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            transliteration = "Al-ḥamdu lillāhil-ladhī aḥyānā ba'da mā amātanā wa ilayhin-nushūr.",
            translationEn = "All praise is for Allah who gave us life after having given us death and unto Him is the resurrection.",
            translationBn = "সমস্ত প্রশংসা আল্লাহর জন্য, যিনি আমাদেরকে মৃত্যু (নিদ্রা) দানের পর পুনরায় জীবন দান করলেন এবং তাঁরই কাছে আমাদের প্রত্যাবর্তন।",
            reference = "Sahih al-Bukhari (6312)"
        ),
        IslamicDua(
            id = "before_sleeping",
            category = DuaCategory.DAILY_LIFE,
            titleEn = "Before Sleeping",
            titleBn = "ঘুমানোর পূর্বের দু\'আ",
            arabicText = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            transliteration = "Bismika Allāhumma amūtu wa aḥyā.",
            translationEn = "In Your name, O Allah, I die and I live.",
            translationBn = "হে আল্লাহ! আপনারই নামে আমি মারা যাই (ঘুমাই) এবং জীবিত হই।",
            reference = "Sahih al-Bukhari (6324)"
        ),
        IslamicDua(
            id = "before_eating",
            category = DuaCategory.DAILY_LIFE,
            titleEn = "Before Eating",
            titleBn = "খাওয়ার শুরুর দু\'আ",
            arabicText = "بِسْمِ اللَّهِ وَعَلَى بَرَكَةِ اللَّهِ",
            transliteration = "Bismillāhi wa 'alā barakatillāh.",
            translationEn = "In the name of Allah and upon the blessings of Allah.",
            translationBn = "আল্লাহর নামে এবং আল্লাহর বরকতের উপর শুরু করছি।",
            reference = "Al-Hakim"
        ),
        IslamicDua(
            id = "after_eating",
            category = DuaCategory.DAILY_LIFE,
            titleEn = "After Eating",
            titleBn = "খাওয়ার পরের দু\'আ",
            arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ",
            transliteration = "Al-ḥamdu lillāhil-ladhī aṭ'amanā wa saqānā wa ja'alanā Muslimīn.",
            translationEn = "All praise is for Allah who fed us, gave us drink, and made us Muslims.",
            translationBn = "সকল প্রশংসা আল্লাহর, যিনি আমাদেরকে আহার করালেন, পান করালেন এবং মুসলিম বানালেন।",
            reference = "Abu Dawud (3850)"
        ),
        // Travel
        IslamicDua(
            id = "riding_vehicle",
            category = DuaCategory.TRAVEL_JOURNEY,
            titleEn = "Dua for Boarding a Vehicle / Traveling",
            titleBn = "বাহনে চড়ার / সফরের দু\'আ",
            arabicText = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
            transliteration = "Subḥānal-ladhī sakhkhara lanā hādhā wa mā kunnā lahū muqrinīn, wa innā ilā Rabbinā lamunqalibūn.",
            translationEn = "Glory to Him who has brought this under our control, though we were unable to do so by ourselves, and indeed to our Lord we will surely return.",
            translationBn = "পবিত্র সেই সত্তা, যিনি একে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা একে বশীভূত করতে সমর্থ ছিলাম না। আর নিশ্চয়ই আমরা আমাদের রবের কাছেই প্রত্যাবর্তনকারী।",
            reference = "Surah Az-Zukhruf (43:13-14)"
        ),
        // Protection
        IslamicDua(
            id = "ayatul_kursi",
            category = DuaCategory.PROTECTION_RUQYAH,
            titleEn = "Ayatul Kursi (The Throne Verse)",
            titleBn = "আয়াতুল কুরসি (সর্বশ্রেষ্ঠ আয়াত)",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliteration = "Allāhu lā ilāha illā Huwal-Ḥayyul-Qayyūm, lā ta'khudhuhū sinatuw-wa lā nawm, lahū mā fis-samāwāti wa mā fīl-arḍ, man dhal-ladhī yashfa'u 'indahū illā bi'idhnih, ya'lamu mā bayna aydīhim wa mā khalfahum, wa lā yuḥīṭūna bishay'im-min 'ilmihī illā bimā shā', wasi'a kursiyyuhus-samāwāti wal-arḍ, wa lā ya'ūduhū ḥifẓuhumā, wa Huwal-'Aliyyul-'Aẓīm.",
            translationEn = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that could intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            translationBn = "আল্লাহ, তিনি ছাড়া কোনো সত্য ইলাহ নেই, তিনি চিরঞ্জীব, সর্বসত্তার ধারক ও পরিচালক। তন্দ্রা বা নিদ্রা তাঁকে স্পর্শ করে না। আকাশমন্ডলে ও পৃথিবীতে যা কিছু আছে সবই তাঁর। কে আছে এমন যে তাঁর অনুমতি ছাড়া তাঁর কাছে সুপারিশ করবে? তিনি জানেন যা তাদের সম্মুখে রয়েছে এবং যা তাদের পশ্চাতে রয়েছে। আর তাঁর জ্ঞানের কোনো কিছুই তারা আয়ত্ত করতে পারে না, তবে তিনি যতটুকু ইচ্ছা করেন। তাঁর কুরসি সমস্ত আসমান ও জমিন পরিব্যাপ্ত করে আছে, আর এ দুটির রক্ষণাবেক্ষণ তাঁর জন্য মোটেই ক্লান্তিকর নয়। আর তিনিই সর্বোচ্চ, সর্বশ্রেষ্ঠ।",
            reference = "Surah Al-Baqarah (2:255)",
            virtues = "Whoever recites this after every obligatory prayer, nothing will prevent him from entering Paradise except death."
        )
    )

    fun getByCategory(category: DuaCategory): List<IslamicDua> {
        return duas.filter { it.category == category }
    }
}
