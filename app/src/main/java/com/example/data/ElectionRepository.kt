package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.model.BoothChecklistItem
import com.example.model.Candidate
import com.example.model.ElectorProfile
import com.example.model.FactCheckItem
import com.example.model.MCCViolationReport
import com.example.model.OfficialAnnouncement
import com.example.model.ParliamentaryConstituency
import com.example.model.PollingBooth
import com.example.model.SMSAlertNotification
import com.example.model.VoterReceipt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ElectionRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "bharat_election_nexus.db"
    ).build()

    private val mccDao = db.mccViolationDao()
    private val receiptDao = db.voterReceiptDao()
    private val checklistDao = db.boothChecklistDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDefaultDataIfEmpty()
        }
    }

    private suspend fun seedDefaultDataIfEmpty() {
        val initialReports = listOf(
            MCCViolationReport(
                trackingToken = "MCC-2026-UP-84920",
                category = "Unauthorized Loudspeakers After 10 PM",
                description = "High decibel sound speakers used at night without SDM sound permit near Assi Ghat.",
                locationDescription = "Assi Ghat Road, Varanasi",
                latitude = 25.2820,
                longitude = 82.9995,
                timestamp = System.currentTimeMillis() - 86400000L,
                status = "Action Taken",
                assignedFlyingSquadUnit = "FST Unit #2 (Bhelupur)",
                officialActionRemark = "Flying squad arrived at 22:18 hrs. Sound equipment seized under Police Act Sec 34. Notice served."
            ),
            MCCViolationReport(
                trackingToken = "MCC-2026-DL-19283",
                category = "Defacement of Public Property",
                description = "Political banners pasted over public bus transit shelter without municipal authorization.",
                locationDescription = "Ring Road near Lajpat Nagar, New Delhi",
                latitude = 28.5672,
                longitude = 77.2433,
                timestamp = System.currentTimeMillis() - 43200000L,
                status = "Verified & Notice Issued",
                assignedFlyingSquadUnit = "Static Surveillance Team #5",
                officialActionRemark = "Civic body notified. Defacing material removed within 4 hours. Cost debited to candidate's expenditure register."
            )
        )
        for (report in initialReports) {
            if (mccDao.getReportByToken(report.trackingToken) == null) {
                mccDao.insertReport(report)
            }
        }

        val initialChecklist = listOf(
            BoothChecklistItem("CHK-01", "Mock Poll Conducted & 50 Sample Votes Verified with VVPAT", "EVM/VVPAT", true, System.currentTimeMillis() - 7200000L),
            BoothChecklistItem("CHK-02", "Control Unit Clear Button Pressed & Zero Total Shown to Polling Agents", "EVM/VVPAT", true, System.currentTimeMillis() - 7000000L),
            BoothChecklistItem("CHK-03", "Green Paper Seal & Special Tag Affixed in Presence of Party Agents", "Security", true, System.currentTimeMillis() - 6800000L),
            BoothChecklistItem("CHK-04", "Wheelchair Ramp Slope & Handrail Inspected for PwD Accessibility", "Accessibility", true, System.currentTimeMillis() - 6500000L),
            BoothChecklistItem("CHK-05", "Braille Ballot Sheet Placed on EVM Unit for Visually Impaired Electors", "Accessibility", true, System.currentTimeMillis() - 6000000L),
            BoothChecklistItem("CHK-06", "Drinking Water, Medical Kit & Shaded Elector Waiting Area Operational", "Readiness", true, System.currentTimeMillis() - 5500000L),
            BoothChecklistItem("CHK-07", "Indelible Ink Marker Verified & First 100 Electors Finger-Marked", "Readiness", true, System.currentTimeMillis() - 3600000L),
            BoothChecklistItem("CHK-08", "Hourly Elector Turnout (Form 17A) Synchronized with District EOC", "Readiness", false, null)
        )
        checklistDao.insertAll(initialChecklist)
    }

    // Live Flow getters
    fun getAllMccReports(): Flow<List<MCCViolationReport>> = mccDao.getAllReports()
    fun getAllReceipts(): Flow<List<VoterReceipt>> = receiptDao.getAllReceipts()
    fun getAllChecklistItems(): Flow<List<BoothChecklistItem>> = checklistDao.getAllItems()

    suspend fun submitMccReport(report: MCCViolationReport) {
        mccDao.insertReport(report)
    }

    suspend fun updateChecklistItem(item: BoothChecklistItem) {
        checklistDao.updateItem(item)
    }

    suspend fun recordVoteAndGenerateReceipt(
        elector: ElectorProfile,
        selectedCandidate: Candidate
    ): Pair<VoterReceipt, SMSAlertNotification> {
        val timestamp = System.currentTimeMillis()
        val token = "BEN-VOTE-" + (100000..999999).random()
        val vvpatRef = "VVP-" + (10000..99999).random()
        
        // Cryptographic SHA-256 hash generation for Zero-Knowledge proof
        val rawData = "${elector.epicNumber}|${elector.parliamentaryConstituency}|$timestamp|$vvpatRef|SECRET_SALT_2026"
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(rawData.toByteArray(Charsets.UTF_8))
        val sha256Hex = hashBytes.joinToString("") { "%02x".format(it) }

        val maskedEpic = elector.epicNumber.take(4) + "-XXXX-" + elector.epicNumber.takeLast(3)
        val maskedMobile = elector.mobileNumber.take(6) + "XXXX" + elector.mobileNumber.takeLast(2)

        val receipt = VoterReceipt(
            receiptId = UUID.randomUUID().toString(),
            tokenNumber = token,
            voterEpicMasked = maskedEpic,
            constituencyName = elector.parliamentaryConstituency,
            pollingStationName = elector.pollingStationName,
            timestamp = timestamp,
            digitalSignatureSha256 = sha256Hex,
            vvpatSlipReference = vvpatRef,
            smsDispatchStatus = "DELIVERED_TO_REGISTERED_MOBILE",
            mobileNumberMasked = maskedMobile
        )

        receiptDao.insertReceipt(receipt)

        val dateStr = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH).format(Date(timestamp))
        val smsMessage = "Govt of India / ECI Alert: Dear Elector, your vote has been securely cast and verified via VVPAT slip #$vvpatRef at ${elector.assemblyConstituency} on $dateStr. Digital Receipt Token: $token. SHA-256 Proof: ${sha256Hex.take(12)}... Official Election Operations."

        val smsAlert = SMSAlertNotification(
            recipientMobile = elector.mobileNumber,
            messageText = smsMessage,
            timestamp = timestamp,
            isDelivered = true
        )

        return Pair(receipt, smsAlert)
    }

    // Static nationwide dataset
    val constituencies: List<ParliamentaryConstituency> = listOf(
        ParliamentaryConstituency("PC-77", "77", "Varanasi", "Uttar Pradesh", 18.5, 1840, 7, "01 June 2024", 68.4, 7, 25.3176, 82.9739),
        ParliamentaryConstituency("PC-04", "04", "Wayanad", "Kerala", 14.6, 1420, 2, "26 April 2024", 73.8, 6, 11.6854, 76.1320),
        ParliamentaryConstituency("PC-06", "06", "Gandhinagar", "Gujarat", 19.8, 1920, 3, "07 May 2024", 60.1, 8, 23.2156, 72.6369),
        ParliamentaryConstituency("PC-01", "01", "New Delhi", "Delhi NCT", 15.2, 1380, 6, "25 May 2024", 58.9, 9, 28.6139, 77.2090),
        ParliamentaryConstituency("PC-26", "26", "Bangalore South", "Karnataka", 22.4, 2150, 2, "26 April 2024", 54.3, 7, 12.9249, 77.5833),
        ParliamentaryConstituency("PC-09", "09", "Hyderabad", "Telangana", 19.4, 1890, 4, "13 May 2024", 48.5, 8, 17.3850, 78.4867),
        ParliamentaryConstituency("PC-23", "23", "Kolkata South", "West Bengal", 17.9, 1710, 7, "01 June 2024", 69.2, 6, 22.5280, 88.3650),
        ParliamentaryConstituency("PC-05", "05", "Chennai Central", "Tamil Nadu", 13.5, 1340, 1, "19 April 2024", 53.9, 7, 13.0827, 80.2707),
        ParliamentaryConstituency("PC-30", "30", "Patna Sahib", "Bihar", 21.6, 2040, 7, "01 June 2024", 46.8, 9, 25.5941, 85.1376),
        ParliamentaryConstituency("PC-31", "31", "Mumbai South", "Maharashtra", 15.1, 1490, 5, "20 May 2024", 51.2, 8, 18.9401, 72.8347)
    )

    val candidates: List<Candidate> = listOf(
        Candidate(
            id = "CND-01",
            name = "Narendra D. Modi",
            partyName = "Bharatiya Janata Party",
            partyAbbr = "BJP",
            partySymbolEmoji = "🪷",
            constituencyName = "Varanasi (PC-77)",
            state = "Uttar Pradesh",
            age = 73,
            education = "Post Graduate (MA Political Science)",
            profession = "Public Service / Parliamentarian",
            totalAssetsInCrores = 3.02,
            totalLiabilitiesInCrores = 0.0,
            criminalCasesCount = 0,
            criminalCaseDetails = "No criminal cases registered or pending",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/varanasi/cnd01.pdf",
            previousElectionPerformance = "Won 2019 by 4,79,505 vote margin",
            keyManifestoPledges = listOf("Expansion of National Infrastructure & Logistics", "Digital Public Goods & AI Mission", "Renewable Solar Rooftop Initiative", "Universal Healthcare Coverage Expansion")
        ),
        Candidate(
            id = "CND-02",
            name = "Ajay Rai",
            partyName = "Indian National Congress",
            partyAbbr = "INC",
            partySymbolEmoji = "✋",
            constituencyName = "Varanasi (PC-77)",
            state = "Uttar Pradesh",
            age = 54,
            education = "Graduate (BA)",
            profession = "Social Worker & Agriculture",
            totalAssetsInCrores = 5.25,
            totalLiabilitiesInCrores = 0.65,
            criminalCasesCount = 2,
            criminalCaseDetails = "2 political protest cases under IPC Sec 147/188 (Sub-judice)",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/varanasi/cnd02.pdf",
            previousElectionPerformance = "Secured 1,52,548 votes in 2019",
            keyManifestoPledges = listOf("Youth Employment Guarantee Scheme", "Farmer MSP Legal Guarantee Support", "Local Handloom & Silk Weavers Welfare Package", "Universal Pension Support for Senior Citizens")
        ),
        Candidate(
            id = "CND-03",
            name = "Athar Jamal Lari",
            partyName = "Bahujan Samaj Party",
            partyAbbr = "BSP",
            partySymbolEmoji = "🐘",
            constituencyName = "Varanasi (PC-77)",
            state = "Uttar Pradesh",
            age = 62,
            education = "Graduate (B.Com)",
            profession = "Business & Social Work",
            totalAssetsInCrores = 1.95,
            totalLiabilitiesInCrores = 0.12,
            criminalCasesCount = 0,
            criminalCaseDetails = "No criminal cases pending",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/varanasi/cnd03.pdf",
            previousElectionPerformance = "Contested 2004 parliamentary elections",
            keyManifestoPledges = listOf("Social Justice & Equal Educational Opportunity", "Urban Drainage & Sanitation Upgrades", "Support for Small Artisans & Handicrafts", "Transparent Local Governance")
        ),
        Candidate(
            id = "CND-04",
            name = "Gagan Prakash Yadav",
            partyName = "Yug Thulasi Party",
            partyAbbr = "YTP",
            partySymbolEmoji = "🌱",
            constituencyName = "Varanasi (PC-77)",
            state = "Uttar Pradesh",
            age = 41,
            education = "Post Graduate (M.Sc Agriculture)",
            profession = "Organic Farming & Environmental Research",
            totalAssetsInCrores = 0.85,
            totalLiabilitiesInCrores = 0.05,
            criminalCasesCount = 0,
            criminalCaseDetails = "No criminal cases pending",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/varanasi/cnd04.pdf",
            previousElectionPerformance = "First-time Parliamentary Contestant",
            keyManifestoPledges = listOf("Ganga Ecological Rejuvenation Program", "Zero Budget Natural Farming Subsidies", "Solar Powered Cold Storage Units", "Clean Air & Noise Pollution Controls")
        ),
        Candidate(
            id = "CND-05",
            name = "Dr. Shashi Tharoor",
            partyName = "Indian National Congress",
            partyAbbr = "INC",
            partySymbolEmoji = "✋",
            constituencyName = "Thiruvananthapuram (PC-20)",
            state = "Kerala",
            age = 68,
            education = "Doctor of Philosophy (PhD) - Fletcher School",
            profession = "Author, Diplomat & Parliamentarian",
            totalAssetsInCrores = 56.4,
            totalLiabilitiesInCrores = 0.0,
            criminalCasesCount = 0,
            criminalCaseDetails = "Discharged in all historical matters. Nil pending.",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/tvm/cnd05.pdf",
            previousElectionPerformance = "Won 2019 by 99,989 votes",
            keyManifestoPledges = listOf("High-Tech Knowledge Economy Corridor", "Coastal Community Protection & Fishermen Subsidies", "Deepwater Port Infrastructure Upgrades", "Green Mobility & Clean City Initiatives")
        ),
        Candidate(
            id = "CND-06",
            name = "Tejasvi Surya",
            partyName = "Bharatiya Janata Party",
            partyAbbr = "BJP",
            partySymbolEmoji = "🪷",
            constituencyName = "Bangalore South (PC-26)",
            state = "Karnataka",
            age = 33,
            education = "Bachelor of Laws (LL.B)",
            profession = "Advocate & Youth Parliamentarian",
            totalAssetsInCrores = 4.10,
            totalLiabilitiesInCrores = 0.28,
            criminalCasesCount = 1,
            criminalCaseDetails = "1 matter relating to public gathering (Sub-judice)",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/bengaluru/cnd06.pdf",
            previousElectionPerformance = "Won 2019 by 3,31,192 votes",
            keyManifestoPledges = listOf("Bengaluru Suburban Rail Acceleration", "Startup & Deep-Tech Innovation Grants", "Lake Rejuvenation & Green Canopy Restoration", "Cybersecurity & Citizen Helpline Infrastructure")
        )
    )

    val pollingBooths: List<PollingBooth> = listOf(
        PollingBooth(
            id = "PB-142",
            name = "Govt. Inter College, Orderly Bazar",
            roomNumber = "Room No. 3 (Ground Floor)",
            address = "Orderly Bazar, Varanasi, Uttar Pradesh 221002",
            constituencyName = "Varanasi (PC-77)",
            latitude = 25.3340,
            longitude = 82.9860,
            isWheelchairAccessible = true,
            hasBrailleSignage = true,
            hasDrinkingWater = true,
            hasShadedWaitingArea = true,
            currentQueueWaitTimeMinutes = 12,
            queueStatus = "Moderate Flow",
            totalVotersAssigned = 1180,
            votesCastSoFar = 742,
            bloName = "Smt. Sunita Verma",
            bloContact = "+91 94150-84721"
        ),
        PollingBooth(
            id = "PB-143",
            name = "Central Hindu Boys School",
            roomNumber = "Auditorium Hall Wing A",
            address = "Kamachha, Varanasi, Uttar Pradesh 221010",
            constituencyName = "Varanasi (PC-77)",
            latitude = 25.3050,
            longitude = 82.9930,
            isWheelchairAccessible = true,
            hasBrailleSignage = true,
            hasDrinkingWater = true,
            hasShadedWaitingArea = true,
            currentQueueWaitTimeMinutes = 5,
            queueStatus = "Fast / Low Queue",
            totalVotersAssigned = 960,
            votesCastSoFar = 680,
            bloName = "Shri Rajesh Kumar Gupta",
            bloContact = "+91 94500-19284"
        ),
        PollingBooth(
            id = "PB-144",
            name = "St. John's Higher Secondary School",
            roomNumber = "Science Block Room 102",
            address = "DLW Colony, Varanasi, Uttar Pradesh 221004",
            constituencyName = "Varanasi (PC-77)",
            latitude = 25.2780,
            longitude = 82.9640,
            isWheelchairAccessible = true,
            hasBrailleSignage = true,
            hasDrinkingWater = true,
            hasShadedWaitingArea = true,
            currentQueueWaitTimeMinutes = 28,
            queueStatus = "High Rush",
            totalVotersAssigned = 1350,
            votesCastSoFar = 910,
            bloName = "Dr. Anita Srivastava",
            bloContact = "+91 94152-33019"
        )
    )

    val factChecks: List<FactCheckItem> = listOf(
        FactCheckItem(
            id = "FC-01",
            claim = "Viral WhatsApp message claims that voting without an Aadhaar link will cancel your voter registration immediately.",
            verdict = "FALSE_HOAX",
            verdictExplanation = "The Election Commission of India has explicitly clarified that submission of Aadhaar under Form 6B is strictly voluntary. No voter's name will be deleted from the electoral roll on the ground that Aadhaar number has not been provided.",
            officialSource = "ECI Official Notification Press Note No. ECI/PN/52/2023",
            timestamp = "Today, 09:30 AM",
            category = "Electoral Roll & Identity"
        ),
        FactCheckItem(
            id = "FC-02",
            claim = "Video circulating on social media claims that EVM counts can be changed via WiFi/Bluetooth signals.",
            verdict = "FALSE_HOAX",
            verdictExplanation = "EVMs (Electronic Voting Machines) used in India are standalone, air-gapped computers with no wireless, Bluetooth, internet, or remote connectivity hardware whatsoever. Software is one-time masked microchip programmed at foundry level.",
            officialSource = "ECI Status Paper on Electronic Voting Machine (Edition 4, Chapter 3)",
            timestamp = "Yesterday, 04:15 PM",
            category = "EVM & Voting Security"
        ),
        FactCheckItem(
            id = "FC-03",
            claim = "First-time voters can cast vote by showing high-school mark sheet as proof of identity at the polling booth.",
            verdict = "MISLEADING",
            verdictExplanation = "High-school mark sheet alone is NOT in the list of 12 approved photo identity documents for polling day. Approved photo IDs include EPIC, Aadhaar card, PAN card, Driving License, Passport, Bank Passbook with photo, and MNREGA job card.",
            officialSource = "ECI Order on Alternative Photo Identity Documents (Schedule II)",
            timestamp = "2 days ago",
            category = "Polling Day Identification"
        )
    )

    val announcements: List<OfficialAnnouncement> = listOf(
        OfficialAnnouncement(
            id = "ANN-01",
            title = "Model Code of Conduct (MCC) Enforcement in Effect",
            summary = "Strict vigilance activated on unauthorized cash transit, liquor distribution, and defacement across all phase constituencies.",
            date = "27 Aug 2026",
            category = "Advisory",
            isHighPriority = true
        ),
        OfficialAnnouncement(
            id = "ANN-02",
            title = "Home Voting Facility Operational for Citizens Aged 85+ and 40%+ PwD",
            summary = "Polling officials with videography team to visit registered eligible electors at their residence with postal ballot box kit.",
            date = "26 Aug 2026",
            category = "Notification",
            isHighPriority = false
        ),
        OfficialAnnouncement(
            id = "ANN-03",
            title = "Special Helpline '1950' and 24x7 Voter Grievance Portal Active",
            summary = "Electors can dial toll-free 1950 for immediate BLO contact, roll status search, and polling booth direction assistance.",
            date = "25 Aug 2026",
            category = "Press Release",
            isHighPriority = false
        )
    )
}
