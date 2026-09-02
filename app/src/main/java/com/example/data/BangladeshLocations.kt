package com.example.data

import com.example.model.BangladeshDistrict
import com.example.model.BangladeshDivision
import com.example.model.GlobalCity

object BangladeshLocationsData {

    val divisions: List<BangladeshDivision> = listOf(
        BangladeshDivision(
            id = "dhaka",
            nameEn = "Dhaka",
            nameBn = "ঢাকা",
            districts = listOf(
                BangladeshDistrict("dhaka_city", "Dhaka", "ঢাকা", 23.8103, 90.4125, listOf("Dhanmondi", "Gulshan", "Uttara", "Mirpur", "Mohammadpur", "Motijheel", "Badda", "Tejgaon", "Savar", "Dhamrai", "Keraniganj", "Nawabganj", "Dohar")),
                BangladeshDistrict("gazipur", "Gazipur", "গাজীপুর", 24.0023, 90.4264, listOf("Gazipur Sadar", "Kaliakair", "Kapasia", "Sreepur", "Kaliganj", "Tongi")),
                BangladeshDistrict("narayanganj", "Narayanganj", "নারায়ণগঞ্জ", 23.6238, 90.5000, listOf("Narayanganj Sadar", "Bandar", "Rupganj", "Sonargaon", "Araihazar", "Siddhirganj")),
                BangladeshDistrict("tangail", "Tangail", "টাঙ্গাইল", 24.2513, 89.9167, listOf("Tangail Sadar", "Mirzapur", "Delduar", "Nagarpur", "Ghatail", "Kalihati", "Madhupur", "Sakhipur", "Gopalpur", "Bhuapur", "Basail", "Dhanbari")),
                BangladeshDistrict("faridpur", "Faridpur", "ফরিদপুর", 23.6071, 89.8429, listOf("Faridpur Sadar", "Boalmari", "Alfadanga", "Madhukhali", "Bhanga", "Nagarkanda", "Charbhadrasan", "Sadarpur", "Saltha")),
                BangladeshDistrict("manikganj", "Manikganj", "মানিকগঞ্জ", 23.8617, 90.0003, listOf("Manikganj Sadar", "Singair", "Shivalaya", "Saturia", "Harirampur", "Ghior", "Daulatpur")),
                BangladeshDistrict("munshiganj", "Munshiganj", "মুন্সীগঞ্জ", 23.5422, 90.5305, listOf("Munshiganj Sadar", "Sreenagar", "Sirajdikhan", "Louhajang", "Tongibari", "Gazaria")),
                BangladeshDistrict("narsingdi", "Narsingdi", "নরসিংদী", 23.9322, 90.7154, listOf("Narsingdi Sadar", "Palash", "Shibpur", "Raipura", "Monohardi", "Belabo")),
                BangladeshDistrict("gopalganj", "Gopalganj", "গোপালগঞ্জ", 23.0051, 89.8266, listOf("Gopalganj Sadar", "Kashiani", "Kotalipara", "Muksudpur", "Tungipara")),
                BangladeshDistrict("madaripur", "Madaripur", "মাদারীপুর", 23.1641, 90.1897, listOf("Madaripur Sadar", "Shibchar", "Kalkini", "Rajoir")),
                BangladeshDistrict("rajbari", "Rajbari", "রাজবাড়ী", 23.7574, 89.6445, listOf("Rajbari Sadar", "Goalanda", "Pangsha", "Baliakandi", "Kalukhali")),
                BangladeshDistrict("shariatpur", "Shariatpur", "শরীয়তপুর", 23.2423, 90.4348, listOf("Shariatpur Sadar", "Naria", "Zajira", "Bhedarganj", "Damudya", "Gosairhat")),
                BangladeshDistrict("kishoreganj", "Kishoreganj", "কিশোরগঞ্জ", 24.4449, 90.7766, listOf("Kishoreganj Sadar", "Bhairab", "Bajitpur", "Kuliarchar", "Pakundia", "Katiadi", "Karimganj", "Tarail", "Hossainpur", "Itna", "Mithamain", "Nikli", "Austagram"))
            )
        ),
        BangladeshDivision(
            id = "chittagong",
            nameEn = "Chittagong",
            nameBn = "চট্টগ্রাম",
            districts = listOf(
                BangladeshDistrict("ctg_city", "Chattogram", "চট্টগ্রাম", 22.3569, 91.7832, listOf("Kotwali", "Panchlaish", "Pahartali", "Halishahar", "Chandgaon", "Khulshi", "Agrabad", "Hathazari", "Sitakunda", "Mirsharai", "Patiya", "Boalkhali", "Anwara", "Chandanaish", "Lohagara", "Satkania", "Banshkhali", "Rangunia", "Raozan", "Sandwip", "Karnafuli")),
                BangladeshDistrict("coxsbazar", "Cox's Bazar", "কক্সবাজার", 21.4272, 92.0058, listOf("Cox's Bazar Sadar", "Chakaria", "Teknaf", "Ukhia", "Ramu", "Pekua", "Kutubdia", "Moheshkhali", "Eidgaon")),
                BangladeshDistrict("comilla", "Cumilla", "কুমিল্লা", 23.4682, 91.1788, listOf("Cumilla Adarsha Sadar", "Cumilla Sadar South", "Burichang", "Brahmanpara", "Debidwar", "Daudkandi", "Chandina", "Muradnagar", "Homna", "Meghna", "Laksam", "Monohargonj", "Nangalkot", "Chauddagram", "Titas", "Barura", "Lalmai")),
                BangladeshDistrict("brahmanbaria", "Brahmanbaria", "ব্রাহ্মণবাড়িয়া", 23.9571, 91.1119, listOf("Brahmanbaria Sadar", "Kasba", "Nasirnagar", "Nabinagar", "Bancharampur", "Sarail", "Ashuganj", "Akhaura", "Bijoynagar")),
                BangladeshDistrict("chandpur", "Chandpur", "চাঁদপুর", 23.2333, 90.6667, listOf("Chandpur Sadar", "Haimchar", "Matlab North", "Matlab South", "Shahrasti", "Kachua", "Faridganj", "Haziganj")),
                BangladeshDistrict("noakhali", "Noakhali", "নোয়াখালী", 22.8696, 91.0998, listOf("Noakhali Sadar", "Begumganj", "Chatkhil", "Companiganj", "Hatiya", "Senbagh", "Subarnachar", "Kabirhat", "Sonaimuri")),
                BangladeshDistrict("feni", "Feni", "ফেনী", 23.0159, 91.3976, listOf("Feni Sadar", "Chhagalnaiya", "Daganbhuiyan", "Parshuram", "Fulgazi", "Sonagazi")),
                BangladeshDistrict("lakshmipur", "Lakshmipur", "লক্ষ্মীপুর", 22.9425, 90.8412, listOf("Lakshmipur Sadar", "Raipur", "Ramganj", "Ramgati", "Kamalnagar")),
                BangladeshDistrict("rangamati", "Rangamati", "রাঙ্গামাটি", 22.6533, 92.1753, listOf("Rangamati Sadar", "Kaptai", "Kawkhali", "Baghaichhari", "Barkal", "Langadu", "Rajasthali", "Belaichhari", "Juraichhari", "Naniarchar")),
                BangladeshDistrict("bandarban", "Bandarban", "বান্দরবান", 22.1953, 92.2184, listOf("Bandarban Sadar", "Ruma", "Thanchi", "Rowangchhari", "Lama", "Alikadam", "Naikhongchhari")),
                BangladeshDistrict("khagrachhari", "Khagrachhari", "খাগড়াছড়ি", 23.1193, 91.9847, listOf("Khagrachhari Sadar", "Dighinala", "Panchhari", "Mahalchhari", "Matiranga", "Manikchhari", "Ramgarh", "Guimara", "Lakshmichhari"))
            )
        ),
        BangladeshDivision(
            id = "sylhet",
            nameEn = "Sylhet",
            nameBn = "সিলেট",
            districts = listOf(
                BangladeshDistrict("sylhet_sadar", "Sylhet", "সিলেট", 24.8949, 91.8687, listOf("Sylhet Sadar", "Beanibazar", "Golapganj", "Companiganj", "Fenchuganj", "Bishwanath", "Gowainghat", "Jaintiapur", "Kanaighat", "Zakiganj", "Dakshin Surma", "Osmani Nagar")),
                BangladeshDistrict("moulvibazar", "Moulvibazar", "মৌলভীবাজার", 24.4829, 91.7774, listOf("Moulvibazar Sadar", "Sreemangal", "Kulaura", "Kamalganj", "Barlekha", "Juri", "Rajnagar")),
                BangladeshDistrict("habiganj", "Habiganj", "হবিগঞ্জ", 24.3749, 91.4155, listOf("Habiganj Sadar", "Madhabpur", "Chunarughat", "Bahubal", "Nabiganj", "Baniachong", "Ajmiriganj", "Lakhai", "Sayestaganj")),
                BangladeshDistrict("sunamganj", "Sunamganj", "সুনামগঞ্জ", 25.0658, 91.3950, listOf("Sunamganj Sadar", "Chhatak", "Jagannathpur", "Derai", "Tahirpur", "Dharampasha", "Jamalganj", "Shantiganj", "Bishwamvarpur", "Dowarabazar", "Madhyanagar"))
            )
        ),
        BangladeshDivision(
            id = "rajshahi",
            nameEn = "Rajshahi",
            nameBn = "রাজশাহী",
            districts = listOf(
                BangladeshDistrict("rajshahi_sadar", "Rajshahi", "রাজশাহী", 24.3745, 88.6042, listOf("Boalia", "Rajpara", "Motihar", "Shah Makhdum", "Paba", "Godagari", "Tanore", "Bagmara", "Durgapur", "Charghat", "Puthia", "Bagha", "Mohanpur")),
                BangladeshDistrict("bogra", "Bogura", "বগুড়া", 24.8465, 89.3777, listOf("Bogura Sadar", "Shajahanpur", "Sherpur", "Shibganj", "Gabtali", "Sonatala", "Sariakandi", "Dhunat", "Adamdighi", "Dupchanchia", "Kahaloo", "Nandigram")),
                BangladeshDistrict("pabna", "Pabna", "পাবনা", 24.0064, 89.2372, listOf("Pabna Sadar", "Ishwardi", "Atgharia", "Bera", "Bhangura", "Chatmohar", "Faridpur", "Santhia", "Sujanagar")),
                BangladeshDistrict("sirajganj", "Sirajganj", "সিরাজগঞ্জ", 24.4534, 89.7008, listOf("Sirajganj Sadar", "Kazipur", "Ullapara", "Shahjadpur", "Raiganj", "Tarash", "Belkuchi", "Kamarkhanda", "Chauhali")),
                BangladeshDistrict("naogaon", "Naogaon", "নওগাঁ", 24.8109, 88.9320, listOf("Naogaon Sadar", "Mohadevpur", "Manda", "Niamatpur", "Atrai", "Raninagar", "Patnitala", "Dhamoirhat", "Sapahar", "Porsha", "Badalgachhi")),
                BangladeshDistrict("natore", "Natore", "নাটোর", 24.4206, 88.9320, listOf("Natore Sadar", "Baraigram", "Bagatipara", "Gurudaspur", "Lalpur", "Singra", "Naldanga")),
                BangladeshDistrict("chapainawabganj", "Chapai Nawabganj", "চাঁপাইনবাবগঞ্জ", 24.5965, 88.2775, listOf("Chapai Nawabganj Sadar", "Shibganj", "Gomastapur", "Nachole", "Bholahat")),
                BangladeshDistrict("joypurhat", "Joypurhat", "জয়পুরহাট", 25.1015, 89.0270, listOf("Joypurhat Sadar", "Panchbibi", "Akkelpur", "Khetlal", "Kalai"))
            )
        ),
        BangladeshDivision(
            id = "khulna",
            nameEn = "Khulna",
            nameBn = "খুলনা",
            districts = listOf(
                BangladeshDistrict("khulna_sadar", "Khulna", "খুলনা", 22.8456, 89.5403, listOf("Khulna Sadar", "Sonadanga", "Daulatpur", "Khalishpur", "Khan Jahan Ali", "Dumuria", "Batiaghata", "Dacope", "Paikgachha", "Koyra", "Rupsha", "Terokhada", "Dighalia", "Phultala")),
                BangladeshDistrict("jessore", "Jashore", "যশোর", 23.1664, 89.2081, listOf("Jashore Sadar", "Jhikargachha", "Sharsha", "Manirampur", "Keshabpur", "Abhaynagar", "Bagherpara", "Chaugachha")),
                BangladeshDistrict("kushtia", "Kushtia", "কুষ্টিয়া", 23.9013, 89.1205, listOf("Kushtia Sadar", "Kumarkhali", "Khoksa", "Mirpur", "Bheramara", "Daulatpur")),
                BangladeshDistrict("satkhira", "Satkhira", "সাতক্ষীরা", 22.7185, 89.0705, listOf("Satkhira Sadar", "Assasuni", "Debhata", "Kalaroa", "Kaliganj", "Shyamnagar", "Tala")),
                BangladeshDistrict("bagerhat", "Bagerhat", "বাগেরহাট", 22.6516, 89.7859, listOf("Bagerhat Sadar", "Mongla", "Morrelganj", "Sarankhola", "Rampal", "Kachua", "Fakirhat", "Mollahat", "Chitalmari")),
                BangladeshDistrict("jhenaidah", "Jhenaidah", "ঝিনাইদহ", 23.5450, 89.1726, listOf("Jhenaidah Sadar", "Kaliganj", "Kotchandpur", "Maheshpur", "Shailkupa", "Harinakunda")),
                BangladeshDistrict("chuadanga", "Chuadanga", "চুয়াডাঙ্গা", 23.6402, 88.8418, listOf("Chuadanga Sadar", "Alamdanga", "Damurhuda", "Jibannagar")),
                BangladeshDistrict("magura", "Magura", "মাগুরা", 23.4873, 89.4199, listOf("Magura Sadar", "Sreepur", "Mohammadpur", "Shalikha")),
                BangladeshDistrict("meherpur", "Meherpur", "মেহেরপুর", 23.7722, 88.6318, listOf("Meherpur Sadar", "Gangni", "Mujibnagar")),
                BangladeshDistrict("narail", "Narail", "নড়াইল", 23.1725, 89.5127, listOf("Narail Sadar", "Lohagara", "Kalia"))
            )
        ),
        BangladeshDivision(
            id = "barisal",
            nameEn = "Barishal",
            nameBn = "বরিশাল",
            districts = listOf(
                BangladeshDistrict("barisal_sadar", "Barishal", "বরিশাল", 22.7010, 90.3535, listOf("Barishal Sadar", "Bakerganj", "Babuganj", "Wazirpur", "Banaripara", "Gournadi", "Agailjhara", "Mehendiganj", "Muladi", "Hizla")),
                BangladeshDistrict("patuakhali", "Patuakhali", "পটুয়াখালী", 22.3596, 90.3299, listOf("Patuakhali Sadar", "Galachipa", "Bauphal", "Dashmina", "Kalapara", "Mirzaganj", "Dumki", "Rangabali")),
                BangladeshDistrict("bhola", "Bhola", "ভোলা", 22.6859, 90.6481, listOf("Bhola Sadar", "Borhanuddin", "Char Fasson", "Daulatkhan", "Lalmohan", "Manpura", "Tazumuddin")),
                BangladeshDistrict("pirojpur", "Pirojpur", "পিরোজপুর", 22.5841, 89.9720, listOf("Pirojpur Sadar", "Bhandaria", "Mathbaria", "Kawkhali", "Nazirpur", "Nesarabad", "Zianagar")),
                BangladeshDistrict("barguna", "Barguna", "বরগুনা", 22.1570, 90.1256, listOf("Barguna Sadar", "Amtali", "Patharghata", "Betagi", "Bamna", "Taltali")),
                BangladeshDistrict("jhalokati", "Jhalokathi", "ঝালকাঠি", 22.6406, 90.1987, listOf("Jhalokathi Sadar", "Kathalia", "Nalchity", "Rajapur"))
            )
        ),
        BangladeshDivision(
            id = "rangpur",
            nameEn = "Rangpur",
            nameBn = "রংপুর",
            districts = listOf(
                BangladeshDistrict("rangpur_sadar", "Rangpur", "রংপুর", 25.7439, 89.2752, listOf("Rangpur Sadar", "Badarganj", "Gangachhara", "Kaunia", "Mithapukur", "Pirgachha", "Pirganj", "Taraganj")),
                BangladeshDistrict("dinajpur", "Dinajpur", "দিনাজপুর", 25.6217, 88.6355, listOf("Dinajpur Sadar", "Birganj", "Biral", "Bochaganj", "Chirirbandar", "Fulbari", "Ghoraghat", "Hakimpur", "Kaharole", "Khansama", "Nawabganj", "Parbatipur")),
                BangladeshDistrict("gaibandha", "Gaibandha", "গাইবান্ধা", 25.3288, 89.5430, listOf("Gaibandha Sadar", "Gobindaganj", "Palashbari", "Sadullapur", "Saghata", "Sundarganj", "Fulchhari")),
                BangladeshDistrict("kurigram", "Kurigram", "কুড়িগ্রাম", 25.8054, 89.6362, listOf("Kurigram Sadar", "Nageshwari", "Bhurungamari", "Phulbari", "Rajarhat", "Ulipur", "Chilmari", "Roumari", "Char Rajibpur")),
                BangladeshDistrict("nilphamari", "Nilphamari", "নীলফামারী", 25.9318, 88.8560, listOf("Nilphamari Sadar", "Saidpur", "Domar", "Dimla", "Jaldhaka", "Kishoreganj")),
                BangladeshDistrict("lalmonirhat", "Lalmonirhat", "লালমনিরহাট", 25.9923, 89.2847, listOf("Lalmonirhat Sadar", "Aditmari", "Kaliganj", "Hatibandha", "Patgram")),
                BangladeshDistrict("thakurgaon", "Thakurgaon", "ঠাকুরগাঁও", 26.0337, 88.4617, listOf("Thakurgaon Sadar", "Pirganj", "Ranisankail", "Haripur", "Baliadangi")),
                BangladeshDistrict("panchagarh", "Panchagarh", "পঞ্চগড়", 26.3411, 88.5542, listOf("Panchagarh Sadar", "Debiganj", "Boda", "Atwari", "Tetulia"))
            )
        ),
        BangladeshDivision(
            id = "mymensingh",
            nameEn = "Mymensingh",
            nameBn = "ময়মনসিংহ",
            districts = listOf(
                BangladeshDistrict("mymensingh_sadar", "Mymensingh", "ময়মনসিংহ", 24.7471, 90.4203, listOf("Mymensingh Sadar", "Muktagachha", "Bhaluka", "Trishal", "Gafargaon", "Phulpur", "Haluaghat", "Dhobaura", "Iswarganj", "Nandail", "Gouripur", "Tara Khanda")),
                BangladeshDistrict("jamalpur", "Jamalpur", "জামালপুর", 24.9375, 89.9378, listOf("Jamalpur Sadar", "Melandaha", "Islampur", "Dewanganj", "Sarishabari", "Madarganj", "Bakshiganj")),
                BangladeshDistrict("netrokona", "Netrokona", "নেত্রকোণা", 24.8709, 90.7279, listOf("Netrokona Sadar", "Kendua", "Mohanganj", "Barhatta", "Purbadhala", "Atpara", "Madan", "Khaliajuri", "Durgapur", "Kalmakanda")),
                BangladeshDistrict("sherpur", "Sherpur", "শেরপুর", 25.0205, 90.0153, listOf("Sherpur Sadar", "Nakla", "Nalitabari", "Sreebardi", "Jhenaigati"))
            )
        )
    )

    val globalCities: List<GlobalCity> = listOf(
        GlobalCity("Makkah", "মক্কা মুকাররমা", "مكة المكرمة", "Saudi Arabia", "সৌদি আরব", 21.4225, 39.8262, "Asia/Riyadh"),
        GlobalCity("Madinah", "মদিনা মুনাওয়ারা", "المدينة المنورة", "Saudi Arabia", "সৌদি আরব", 24.5247, 39.5692, "Asia/Riyadh"),
        GlobalCity("Jerusalem (Al-Quds)", "জেরুজালেম (আল-কুদস)", "القدس الشريف", "Palestine", "ফিলিস্তিন", 31.7683, 35.2137, "Asia/Jerusalem"),
        GlobalCity("Cairo", "কায়রো", "القاهرة", "Egypt", "মিশর", 30.0444, 31.2357, "Africa/Cairo"),
        GlobalCity("Istanbul", "ইস্তাম্বুল", "إسطنبول", "Turkey", "তুরস্ক", 41.0082, 28.9784, "Europe/Istanbul"),
        GlobalCity("Dubai", "দুবাই", "دبي", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.2048, 55.2708, "Asia/Dubai"),
        GlobalCity("London", "লন্ডন", "لندن", "United Kingdom", "যুক্তরাজ্য", 51.5074, -0.1278, "Europe/London"),
        GlobalCity("New York", "নিউ ইয়র্ক", "نيويورك", "United States", "যুক্তরাষ্ট্র", 40.7128, -74.0060, "America/New_York"),
        GlobalCity("Kuala Lumpur", "কুয়ালালামপুর", "كوالالمبور", "Malaysia", "মালয়েশিয়া", 3.1390, 101.6869, "Asia/Kuala_Lumpur"),
        GlobalCity("Jakarta", "জাকার্তা", "جاكرتا", "Indonesia", "ইন্দোনেশিয়া", -6.2088, 106.8456, "Asia/Jakarta"),
        GlobalCity("Toronto", "টরন্টো", "تورونتو", "Canada", "কানাডা", 43.6532, -79.3832, "America/Toronto"),
        GlobalCity("Berlin", "বার্লিন", "برلين", "Germany", "জার্মানি", 52.5200, 13.4050, "Europe/Berlin"),
        GlobalCity("Sydney", "সিডনি", "سيدني", "Australia", "অস্ট্রেলিয়া", -33.8688, 151.2093, "Australia/Sydney"),
        GlobalCity("Karachi", "করাচি", "كراتشي", "Pakistan", "পাকিস্তান", 24.8607, 67.0011, "Asia/Karachi"),
        GlobalCity("Delhi", "দিল্লি", "دلهي", "India", "ভারত", 28.6139, 77.2090, "Asia/Kolkata")
    )
}
