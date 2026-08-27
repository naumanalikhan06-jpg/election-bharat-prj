package com.example

import com.example.data.security.CryptoVoteEngine
import com.example.model.Candidate
import com.example.model.ElectorProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun cryptoVoteEngine_encryptsAndVerifiesIntegrity() {
        val elector = ElectorProfile(
            epicNumber = "BEN9876543",
            fullName = "Test Elector",
            state = "NCT of Delhi",
            parliamentaryConstituency = "New Delhi (PC-04)"
        )
        val candidate = Candidate(
            id = "cand-001",
            name = "Dr. Rajesh Sharma",
            partyName = "Progressive Democratic Alliance",
            partyAbbr = "PDA",
            partySymbolEmoji = "🌱",
            constituencyName = "New Delhi (PC-04)",
            state = "NCT of Delhi",
            age = 48,
            education = "Ph.D. in Economics",
            profession = "Economist & Policy Researcher",
            totalAssetsInCrores = 4.25,
            totalLiabilitiesInCrores = 0.35,
            criminalCasesCount = 0,
            criminalCaseDetails = "None",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/pda-001.pdf",
            previousElectionPerformance = "Won 2021 by 14,200 votes",
            keyManifestoPledges = listOf("Green Infrastructure", "Skill Development")
        )

        // Generate encrypted payload
        val payload = CryptoVoteEngine.encryptBallotSelection(
            elector = elector,
            candidate = candidate,
            tokenNumber = "TK-9921",
            vvpatRef = "VV-8831",
            boothId = "PB-DEL-102"
        )

        assertNotNull(payload.ciphertextBase64)
        assertNotNull(payload.ivBase64)
        assertNotNull(payload.integrityHmac)
        assertNotNull(payload.digitalSealSha256)
        assertNotNull(payload.anonymizedElectorHash)

        // Verify ballot choice is encrypted and not plaintext
        assertFalse(payload.ciphertextBase64.contains("Dr. Rajesh Sharma"))

        // Verify HMAC and digital seal integrity check
        val auditProof = CryptoVoteEngine.verifyBallotIntegrity(payload)
        assertTrue(auditProof.isTamperProof)
        assertTrue(auditProof.isHmacValid)
        assertTrue(auditProof.isSealValid)
        assertEquals(payload.integrityHmac, auditProof.computedHmac)

        // Verify authorized recount decryption
        val decrypted = CryptoVoteEngine.decryptBallotForAudit(payload)
        assertNotNull(decrypted)
        assertTrue(decrypted!!.contains("Dr. Rajesh Sharma"))
        assertTrue(decrypted.contains("cand-001"))
    }

    @Test
    fun cryptoVoteEngine_detectsTamperedPayload() {
        val elector = ElectorProfile(epicNumber = "BEN1122334")
        val candidate = Candidate(
            id = "cand-002",
            name = "Smt. Sunita Verma",
            partyName = "National People's Party",
            partyAbbr = "NPP",
            partySymbolEmoji = "🌾",
            constituencyName = "New Delhi (PC-04)",
            state = "NCT of Delhi",
            age = 52,
            education = "M.A. Public Administration",
            profession = "Social Activist",
            totalAssetsInCrores = 2.10,
            totalLiabilitiesInCrores = 0.15,
            criminalCasesCount = 0,
            criminalCaseDetails = "None",
            affidavitDocumentUrl = "https://affidavit.eci.gov.in/npp-002.pdf",
            previousElectionPerformance = "First Contest",
            keyManifestoPledges = listOf("Universal Healthcare", "Clean Water")
        )

        val payload = CryptoVoteEngine.encryptBallotSelection(
            elector = elector,
            candidate = candidate,
            tokenNumber = "TK-9922",
            vvpatRef = "VV-8832",
            boothId = "PB-DEL-102"
        )

        // Tamper with ciphertext
        val tamperedPayload = payload.copy(
            ciphertextBase64 = payload.ciphertextBase64.reversed()
        )

        // Tampered integrity check must fail
        val auditProof = CryptoVoteEngine.verifyBallotIntegrity(tamperedPayload)
        assertFalse(auditProof.isTamperProof)
    }

    @Test
    fun cryptoVoteEngine_anonymizedHashIsDeterministicPerElector() {
        val hash1 = CryptoVoteEngine.generateAnonymizedElectorHash("EPIC123456", "Varanasi")
        val hash1Repeat = CryptoVoteEngine.generateAnonymizedElectorHash("EPIC123456", "Varanasi")
        val hash2 = CryptoVoteEngine.generateAnonymizedElectorHash("EPIC999999", "Varanasi")

        assertEquals(hash1, hash1Repeat)
        assertNotEquals(hash1, hash2)
        assertFalse(hash1.contains("EPIC123456"))
    }
}


