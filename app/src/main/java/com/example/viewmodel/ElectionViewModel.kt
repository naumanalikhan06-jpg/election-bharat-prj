package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ElectionRepository
import com.example.data.gemini.GeminiAiService
import com.example.model.BoothChecklistItem
import com.example.model.Candidate
import com.example.model.ElectorProfile
import com.example.model.FactCheckItem
import com.example.model.Language
import com.example.model.MCCViolationReport
import com.example.model.OfficialAnnouncement
import com.example.model.ParliamentaryConstituency
import com.example.model.PollingBooth
import com.example.model.SMSAlertNotification
import com.example.model.UserRole
import com.example.model.VoterReceipt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user", "nirvachan_ai"
    val text: String,
    val sourceCitation: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ElectionUiState(
    val currentLanguage: Language = Language.ENGLISH,
    val currentRole: UserRole = UserRole.CITIZEN,
    val isHighContrast: Boolean = false,
    val isLargeText: Boolean = false,
    val elector: ElectorProfile = ElectorProfile(),
    val constituencies: List<ParliamentaryConstituency> = emptyList(),
    val candidates: List<Candidate> = emptyList(),
    val pollingBooths: List<PollingBooth> = emptyList(),
    val factChecks: List<FactCheckItem> = emptyList(),
    val announcements: List<OfficialAnnouncement> = emptyList(),
    val mccReports: List<MCCViolationReport> = emptyList(),
    val voterReceipts: List<VoterReceipt> = emptyList(),
    val boothChecklist: List<BoothChecklistItem> = emptyList(),
    val activeSmsAlert: SMSAlertNotification? = null,
    val latestReceipt: VoterReceipt? = null,
    
    // EVM / Voting Simulator State
    val isVotingInProgress: Boolean = false,
    val votingStep: Int = 1, // 1: Verify OTP, 2: 3D EVM Ballot Unit, 3: VVPAT 7-Sec Slip Feed, 4: Receipt & SMS Success
    val selectedCandidateForVote: Candidate? = null,
    val otpInput: String = "",
    val isOtpVerified: Boolean = false,
    val vvpatSlipVisible: Boolean = false,

    // AI Chat State
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = "nirvachan_ai",
            text = "Namaste! I am Nirvachan AI, India's sovereign multilingual election assistant. How may I assist you with voter registration, polling stations, VVPAT verification, or Model Code of Conduct procedures?",
            sourceCitation = "Official Election Commission of India Rules & Manuals"
        )
    ),
    val isAiThinking: Boolean = false,

    // Search and Filters
    val searchQuery: String = "",
    val selectedStateFilter: String = "All States",
    val selectedConstituencyForDetail: ParliamentaryConstituency? = null,
    val comparisonCandidate1: Candidate? = null,
    val comparisonCandidate2: Candidate? = null,

    // MCC Report Form State
    val mccCategory: String = "Cash / Liquor Inducement",
    val mccDescription: String = "",
    val mccLocationDesc: String = "",
    val mccIsAnonymous: Boolean = false,
    val mccSubmittedToken: String? = null
)

class ElectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ElectionRepository(application)
    private val aiService = GeminiAiService()

    private val _uiState = MutableStateFlow(
        ElectionUiState(
            constituencies = repository.constituencies,
            candidates = repository.candidates,
            pollingBooths = repository.pollingBooths,
            factChecks = repository.factChecks,
            announcements = repository.announcements,
            comparisonCandidate1 = repository.candidates.firstOrNull(),
            comparisonCandidate2 = repository.candidates.getOrNull(1)
        )
    )
    val uiState: StateFlow<ElectionUiState> = _uiState.asStateFlow()

    init {
        // Collect live data from Room database
        viewModelScope.launch {
            repository.getAllMccReports().collectLatest { reports ->
                _uiState.update { it.copy(mccReports = reports) }
            }
        }
        viewModelScope.launch {
            repository.getAllReceipts().collectLatest { receipts ->
                _uiState.update { it.copy(voterReceipts = receipts, latestReceipt = receipts.firstOrNull()) }
            }
        }
        viewModelScope.launch {
            repository.getAllChecklistItems().collectLatest { items ->
                _uiState.update { it.copy(boothChecklist = items) }
            }
        }
    }

    fun setLanguage(language: Language) {
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(currentRole = role) }
    }

    fun toggleHighContrast() {
        _uiState.update { it.copy(isHighContrast = !it.isHighContrast) }
    }

    fun toggleLargeText() {
        _uiState.update { it.copy(isLargeText = !it.isLargeText) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setStateFilter(state: String) {
        _uiState.update { it.copy(selectedStateFilter = state) }
    }

    fun selectConstituencyForDetail(pc: ParliamentaryConstituency?) {
        _uiState.update { it.copy(selectedConstituencyForDetail = pc) }
    }

    fun setComparisonCandidates(c1: Candidate?, c2: Candidate?) {
        _uiState.update { it.copy(comparisonCandidate1 = c1, comparisonCandidate2 = c2) }
    }

    // --- Voting and VVPAT + SMS Operations ---
    fun startVotingFlow() {
        _uiState.update {
            it.copy(
                isVotingInProgress = true,
                votingStep = 1,
                selectedCandidateForVote = null,
                isOtpVerified = false,
                otpInput = "",
                vvpatSlipVisible = false
            )
        }
    }

    fun setOtpInput(otp: String) {
        _uiState.update { it.copy(otpInput = otp) }
    }

    fun verifyOtpAndProceedToEvm() {
        if (_uiState.value.otpInput.length >= 4 || _uiState.value.otpInput.isEmpty()) {
            _uiState.update { it.copy(isOtpVerified = true, votingStep = 2) }
        }
    }

    fun castVoteOnEvm(candidate: Candidate) {
        _uiState.update {
            it.copy(
                selectedCandidateForVote = candidate,
                votingStep = 3,
                vvpatSlipVisible = true
            )
        }

        // Trigger 7-second VVPAT inspection window before cutting and dropping into the ballot box
        viewModelScope.launch {
            kotlinx.coroutines.delay(4500L) // 4.5 seconds animation window
            
            // Record vote cryptographically and dispatch SMS to registered mobile
            val (receipt, smsAlert) = repository.recordVoteAndGenerateReceipt(
                elector = _uiState.value.elector,
                selectedCandidate = candidate
            )

            _uiState.update {
                it.copy(
                    vvpatSlipVisible = false,
                    votingStep = 4,
                    elector = it.elector.copy(hasVoted = true),
                    activeSmsAlert = smsAlert,
                    latestReceipt = receipt
                )
            }
        }
    }

    fun dismissSmsBanner() {
        _uiState.update { it.copy(activeSmsAlert = null) }
    }

    fun resetVotingFlow() {
        _uiState.update { it.copy(isVotingInProgress = false, votingStep = 1) }
    }

    // --- Nirvachan AI Chat ---
    fun sendAiMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = userText)
        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isAiThinking = true
            )
        }

        viewModelScope.launch {
            val (reply, citation) = aiService.askNirvachan(userText, _uiState.value.currentLanguage)
            val aiMsg = ChatMessage(
                sender = "nirvachan_ai",
                text = reply,
                sourceCitation = citation
            )
            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + aiMsg,
                    isAiThinking = false
                )
            }
        }
    }

    // --- MCC Violation Reporting ---
    fun updateMccForm(
        category: String? = null,
        desc: String? = null,
        location: String? = null,
        isAnon: Boolean? = null
    ) {
        _uiState.update { current ->
            current.copy(
                mccCategory = category ?: current.mccCategory,
                mccDescription = desc ?: current.mccDescription,
                mccLocationDesc = location ?: current.mccLocationDesc,
                mccIsAnonymous = isAnon ?: current.mccIsAnonymous
            )
        }
    }

    fun submitMccViolationReport() {
        val state = _uiState.value
        val token = "MCC-2026-UP-" + (10000..99999).random()
        val newReport = MCCViolationReport(
            trackingToken = token,
            category = state.mccCategory,
            description = state.mccDescription.ifBlank { "Unlawful electoral activity observed in public domain." },
            locationDescription = state.mccLocationDesc.ifBlank { "Varanasi North Sector 4" },
            latitude = 25.3200,
            longitude = 82.9800,
            isAnonymous = state.mccIsAnonymous,
            status = "Submitted (Assigned to FST Unit #3)",
            assignedFlyingSquadUnit = "Flying Squad Team #3",
            officialActionRemark = "Complaint received via Bharat Election Nexus Citizen App. GPS geofence recorded."
        )

        viewModelScope.launch {
            repository.submitMccReport(newReport)
            _uiState.update {
                it.copy(
                    mccSubmittedToken = token,
                    mccDescription = "",
                    mccLocationDesc = ""
                )
            }
        }
    }

    fun clearMccSubmittedToken() {
        _uiState.update { it.copy(mccSubmittedToken = null) }
    }

    // --- Official Booth Checklist Operations ---
    fun toggleChecklistItem(item: BoothChecklistItem) {
        val updated = item.copy(
            isCompleted = !item.isCompleted,
            completedTimestamp = if (!item.isCompleted) System.currentTimeMillis() else null
        )
        viewModelScope.launch {
            repository.updateChecklistItem(updated)
        }
    }
}
