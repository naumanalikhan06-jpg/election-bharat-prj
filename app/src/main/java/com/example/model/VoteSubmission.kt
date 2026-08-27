package com.example.model

import java.util.UUID

/**
 * Sovereign Cryptographic Vote Payload representation.
 * Designed for air-gapped security, zero-knowledge anonymity, and immutability.
 */
data class EncryptedVotePayload(
    val ballotId: String = UUID.randomUUID().toString(),
    val ciphertextBase64: String,
    val ivBase64: String,
    val keyAlgorithm: String = "AES/GCM/NoPadding",
    val integrityHmac: String,
    val digitalSealSha256: String,
    val anonymizedElectorHash: String,
    val constituencyCode: String,
    val boothId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val receiptToken: String,
    val vvpatReference: String,
    val status: String = "SEALED_IMMUTABLE",
    val blockSequenceNumber: Long = 104928L
)

sealed class VoteSubmissionResult {
    data class Success(
        val receipt: VoterReceipt,
        val encryptedPayload: EncryptedVotePayload,
        val txHash: String,
        val isFirestoreCloudSynced: Boolean,
        val message: String = "Vote submitted successfully with cryptographic seal and Firestore atomic transaction."
    ) : VoteSubmissionResult()

    data class DoubleVoteBlocked(
        val reason: String = "Duplicate vote submission detected for this elector. Transaction aborted by Firestore atomic validator.",
        val previousTimestamp: Long? = null
    ) : VoteSubmissionResult()

    data class IntegrityCheckFailed(
        val reason: String = "Ballot integrity check failed. Cryptographic HMAC mismatch detected before submission."
    ) : VoteSubmissionResult()

    data class Failure(
        val errorMessage: String
    ) : VoteSubmissionResult()
}

data class AuditVerificationProof(
    val ballotId: String,
    val electorHash: String,
    val computedHmac: String,
    val storedHmac: String,
    val isHmacValid: Boolean,
    val isSealValid: Boolean,
    val isTamperProof: Boolean,
    val verificationTimestamp: Long = System.currentTimeMillis()
)
