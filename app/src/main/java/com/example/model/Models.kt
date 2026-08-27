package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    TELUGU("te", "Telugu", "తెలుగు"),
    MALAYALAM("ml", "Malayalam", "മലയാളം"),
    MARATHI("mr", "Marathi", "मराठी"),
    BENGALI("bn", "Bengali", "বাংলা"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી"),
    PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ")
}

enum class UserRole(val title: String, val level: String, val badge: String) {
    CITIZEN("Elector / Citizen", "Public Portal", "🗳️ Citizen"),
    FIRST_TIME_VOTER("First-Time Voter", "Empowerment Portal", "🎓 New Voter"),
    PWD_SENIOR("Accessible Voter (PwD/Senior)", "Inclusive Portal", "♿ Accessible"),
    BLO("Booth Level Officer (BLO)", "Field Operations", "📋 Official (BLO)"),
    DEO("District Election Officer (DEO)", "District Command", "🏛️ Official (DEO)"),
    OBSERVER("General / Police Observer", "Oversight", "👁️ Observer"),
    ADMIN("National Election Admin", "Super Admin", "🛡️ Super Admin")
}

data class ElectorProfile(
    val epicNumber: String = "BEN-2026-948102",
    val fullName: String = "Aaditya Vikram Sharma",
    val fatherOrSpouseName: String = "Rajesh Sharma",
    val age: Int = 29,
    val gender: String = "Male",
    val mobileNumber: String = "+91 98765-43210",
    val state: String = "Uttar Pradesh",
    val district: String = "Varanasi",
    val parliamentaryConstituency: String = "Varanasi (PC-77)",
    val assemblyConstituency: String = "Varanasi Cantt (AC-390)",
    val partNumber: String = "142",
    val serialNumber: String = "89",
    val pollingStationName: String = "Govt. Inter College, Orderly Bazar, Room No. 3",
    val pollingStationAddress: String = "Orderly Bazar, Varanasi, Uttar Pradesh 221002",
    val pollingDate: String = "April 26, 2026",
    val pollingHours: String = "07:00 AM – 06:00 PM",
    val isVerified: Boolean = true,
    val isPwdAssistanceRequired: Boolean = false,
    val hasVoted: Boolean = false
)

data class Candidate(
    val id: String,
    val name: String,
    val partyName: String,
    val partyAbbr: String,
    val partySymbolEmoji: String,
    val constituencyName: String,
    val state: String,
    val age: Int,
    val education: String,
    val profession: String,
    val totalAssetsInCrores: Double,
    val totalLiabilitiesInCrores: Double,
    val criminalCasesCount: Int,
    val criminalCaseDetails: String,
    val affidavitDocumentUrl: String,
    val previousElectionPerformance: String,
    val keyManifestoPledges: List<String>,
    val verifiedDate: String = "2026-03-15",
    val officialSourceAttribution: String = "Form 26 Affidavit submitted to Returning Officer"
)

data class PollingBooth(
    val id: String,
    val name: String,
    val roomNumber: String,
    val address: String,
    val constituencyName: String,
    val latitude: Double,
    val longitude: Double,
    val isWheelchairAccessible: Boolean = true,
    val hasBrailleSignage: Boolean = true,
    val hasDrinkingWater: Boolean = true,
    val hasShadedWaitingArea: Boolean = true,
    val currentQueueWaitTimeMinutes: Int = 15,
    val queueStatus: String = "Moderate Flow", // Low, Moderate, High
    val totalVotersAssigned: Int = 1180,
    val votesCastSoFar: Int = 742,
    val bloName: String = "Smt. Sunita Verma",
    val bloContact: String = "+91 94150-XXXXX"
)

data class ParliamentaryConstituency(
    val id: String,
    val code: String,
    val name: String,
    val state: String,
    val totalElectorsInLakhs: Double,
    val totalPollingStations: Int,
    val phase: Int,
    val pollingDate: String,
    val turnoutPercentage: Double,
    val candidatesCount: Int,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "mcc_violation_reports")
data class MCCViolationReport(
    @PrimaryKey
    val reportId: String = UUID.randomUUID().toString(),
    val trackingToken: String,
    val category: String,
    val description: String,
    val locationDescription: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Under Investigation", // Submitted, Assigned, Verified, Action Taken, Closed
    val isAnonymous: Boolean = false,
    val complainantMobileMasked: String = "+91 98***-**210",
    val assignedFlyingSquadUnit: String = "FST Unit #4 (Sector 2)",
    val officialActionRemark: String = "Flying Squad dispatched to location within 12 mins. Evidence logged."
)

@Entity(tableName = "voter_receipts")
data class VoterReceipt(
    @PrimaryKey
    val receiptId: String = UUID.randomUUID().toString(),
    val tokenNumber: String,
    val voterEpicMasked: String,
    val constituencyName: String,
    val pollingStationName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val digitalSignatureSha256: String,
    val vvpatSlipReference: String,
    val smsDispatchStatus: String = "DISPATCHED_TO_REGISTERED_MOBILE",
    val mobileNumberMasked: String
)

@Entity(tableName = "booth_checklist_items")
data class BoothChecklistItem(
    @PrimaryKey
    val itemId: String,
    val title: String,
    val category: String, // Readiness, EVM/VVPAT, Accessibility, Security
    val isCompleted: Boolean = false,
    val completedTimestamp: Long? = null,
    val verifiedBy: String = "BLO"
)

data class FactCheckItem(
    val id: String,
    val claim: String,
    val verdict: String, // VERIFIED_TRUE, FALSE_HOAX, MISLEADING, UNDER_REVIEW
    val verdictExplanation: String,
    val officialSource: String,
    val timestamp: String,
    val category: String
)

data class OfficialAnnouncement(
    val id: String,
    val title: String,
    val summary: String,
    val date: String,
    val category: String, // Notification, Press Release, Guideline, Advisory
    val authority: String = "Election Commission Authority",
    val isHighPriority: Boolean = false
)

data class SMSAlertNotification(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "GOV-ECI-NEXUS",
    val recipientMobile: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isDelivered: Boolean = true
)
