package com.example.data

import com.example.model.Language

data class ElectionRagFact(
    val topic: String,
    val keywords: List<String>,
    val answerEn: String,
    val answerHi: String,
    val sourceCitation: String,
    val confidenceScore: Double = 0.99
)

object NirvachanRAGKnowledgeBase {

    val verifiedFacts: List<ElectionRagFact> = listOf(
        ElectionRagFact(
            topic = "Voter Registration (Form 6)",
            keywords = listOf("register", "voter id", "apply", "form 6", "new voter", "first time", "enroll", "पंजीकरण", "फॉर्म 6"),
            answerEn = "To register as a new elector in India: 1. You must be an Indian citizen and at least 18 years old on qualifying dates (Jan 1, Apr 1, Jul 1, Oct 1). 2. Submit Form 6 online via the portal or Voters Services Portal / BLO. 3. Documents required: Proof of Age (Birth Certificate, Aadhaar, Passport) and Proof of Residence (Electricity bill, Ration card, Passport, Aadhaar).",
            answerHi = "भारत में नए मतदाता के रूप में पंजीकरण करने के लिए: 1. आपकी आयु कम से कम 18 वर्ष होनी चाहिए। 2. पोर्टल या बीएलओ के माध्यम से फॉर्म 6 जमा करें। 3. आवश्यक दस्तावेज: आयु प्रमाण (आधार/जन्म प्रमाण पत्र) और निवास प्रमाण।",
            sourceCitation = "Registration of Electors Rules 1960, Rule 13 (Form 6 Guidelines)"
        ),
        ElectionRagFact(
            topic = "Documents Required for Polling Day",
            keywords = listOf("document", "id proof", "id card", "epic", "aadhaar", "voting documents", "दस्तावेज", "पहचान पत्र"),
            answerEn = "Even if you do not possess an EPIC (Voter ID) card on polling day, you can vote provided your name is in the Electoral Roll! You must show one of 12 approved photo IDs: 1. Aadhaar Card, 2. Driving License, 3. Passport, 4. PAN Card, 5. MNREGA Job Card, 6. Passbook with photograph (Bank/Post Office), 7. Pension document with photo, 8. Smart Card issued by RGI under NPR, 9. Health Insurance Smart Card, 10. Official identity cards issued to MPs/MLAs/MLCs.",
            answerHi = "यदि मतदान के दिन आपके पास वोटर आईडी (EPIC) नहीं है, तब भी यदि आपका नाम मतदाता सूची में है तो आप वोट डाल सकते हैं! 12 मान्य पहचान पत्रों में से एक दिखाएं: 1. आधार कार्ड, 2. ड्राइविंग लाइसेंस, 3. पासपोर्ट, 4. पैन कार्ड, 5. फोटोयुक्त बैंक पासबुक आदि।",
            sourceCitation = "ECI Order on Alternative Photo Identity Documents, Schedule II"
        ),
        ElectionRagFact(
            topic = "VVPAT Slip Verification",
            keywords = listOf("vvpat", "paper trail", "slip", "verify vote", "evm", "voter verifiable", "वीवीपीएटी", "पर्ची"),
            answerEn = "VVPAT (Voter Verifiable Paper Audit Trail) allows you to visually verify that your vote was cast correctly. When you press the blue button on the Balloting Unit: 1. A red lamp glows against your chosen candidate. 2. The VVPAT window illuminates for 7 seconds displaying a printed paper slip showing the candidate's Serial No., Name, and Symbol. 3. The slip is automatically cut and dropped into the sealed drop box with an audible beep.",
            answerHi = "VVPAT (वोटर वेरिफिएबल पेपर ऑडिट ट्रेल) आपको यह देखने की सुविधा देता है कि आपका वोट सही पड़ा है। बटन दबाने पर 7 सेकंड के लिए स्क्रीन पर उम्मीदवार का नाम, चुनाव चिह्न और क्रमांक वाली पर्ची दिखाई देती है और बीप के बाद बॉक्स में गिर जाती है।",
            sourceCitation = "Conduct of Elections Rules 1961, Rule 49M & 49MA"
        ),
        ElectionRagFact(
            topic = "Model Code of Conduct (MCC) & Violations",
            keywords = listOf("mcc", "violation", "bribe", "money", "liquor", "loudspeaker", "complain", "report", "शिकायत", "आचार संहिता"),
            answerEn = "The Model Code of Conduct comes into force immediately upon election announcement. Prohibited activities: 1. Bribery, cash or liquor distribution, 2. Use of loudspeakers between 10:00 PM and 06:00 AM, 3. Using government vehicles or public property for campaigning, 4. Appealing to caste/communal feelings. You can report violations anonymously via this app's MCC Module or the cVIGIL portal with geotagged photo evidence.",
            answerHi = "आदर्श आचार संहिता (MCC) चुनाव घोषणा के साथ ही लागू हो जाती है। रिश्वत, शराब बांटना, रात 10 बजे के बाद लाउडस्पीकर का उपयोग करना और धार्मिक/जातिगत आधार पर वोट मांगना सख्त मना है। आप ऐप के MCC मॉड्यूल से सीधे रिपोर्ट कर सकते हैं।",
            sourceCitation = "ECI Compendium of Instructions on Model Code of Conduct, Vol 1"
        ),
        ElectionRagFact(
            topic = "Accessibility & PwD Services",
            keywords = listOf("pwd", "disability", "senior", "wheelchair", "blind", "braille", "home voting", "दिव्यांग", "व्हीलचेयर"),
            answerEn = "All polling stations in India feature Assured Minimum Facilities (AMF): 1. Permanent wheelchair ramps with standard slope, 2. Braille signage on EVM Ballot Units and dummy Braille ballot sheets, 3. Priority queues and ground-floor voting rooms for senior citizens and expectant mothers, 4. Optional Home Voting facility via postal ballot for senior citizens aged 85+ and PwD voters with 40%+ benchmark disability.",
            answerHi = "सभी मतदान केंद्रों पर सुनिश्चित न्यूनतम सुविधाएं उपलब्ध हैं: व्हीलचेयर रैंप, ईवीएम पर ब्रेल लिपि, वरिष्ठ नागरिकों और दिव्यांगजनों के लिए प्राथमिकता कतार, तथा 85+ वरिष्ठ नागरिकों व दिव्यांगों के लिए घर से मतदान (Home Voting) की सुविधा।",
            sourceCitation = "ECI Strategic Plan on Accessible Elections & AMF Guidelines"
        ),
        ElectionRagFact(
            topic = "Political Neutrality & Party Recommendation Policy",
            keywords = listOf("who to vote", "best candidate", "recommend", "which party", "who will win", "किसे वोट दें", "सर्वश्रेष्ठ उम्मीदवार"),
            answerEn = "As Nirvachan AI, I am strictly politically neutral and governed by the Election Commission of India transparency principles. I DO NOT recommend, endorse, rank, or evaluate political parties or candidates. Please use our 'Candidate Transparency Center' to compare all candidates based on factual affidavits, educational qualifications, asset declarations, and official manifestos.",
            answerHi = "निर्वाचन एआई के रूप में, मैं पूरी तरह से राजनीतिक रूप से निष्पक्ष हूँ। मैं किसी भी पार्टी या उम्मीदवार का समर्थन या सिफारिश नहीं कर सकता। कृपया सभी उम्मीदवारों के शपथ पत्र, शिक्षा और संपत्ति विवरण देखने के लिए उम्मीदवार तुलना केंद्र का उपयोग करें।",
            sourceCitation = "Bharat Election Nexus Neutrality Protocol & Constitution of India Art. 324"
        )
    )

    fun queryKnowledgeBase(query: String, language: Language = Language.ENGLISH): Pair<String, String> {
        val lower = query.lowercase().trim()
        
        // Neutrality guardrail check
        if (lower.contains("who to vote") || lower.contains("best candidate") || lower.contains("which party is good") ||
            lower.contains("recommend party") || lower.contains("who will win") || lower.contains("bjp or congress") ||
            lower.contains("किसे वोट दें") || lower.contains("कौन जीतेगा")
        ) {
            val fact = verifiedFacts.last()
            return Pair(
                if (language == Language.HINDI) fact.answerHi else fact.answerEn,
                fact.sourceCitation
            )
        }

        // Match against known keywords
        for (fact in verifiedFacts) {
            if (fact.keywords.any { lower.contains(it) }) {
                return Pair(
                    if (language == Language.HINDI) fact.answerHi else fact.answerEn,
                    fact.sourceCitation
                )
            }
        }

        // General procedural fallback
        val defaultTextEn = "For verified procedural guidance on this election topic, please consult the official handbook or dial the 24x7 ECI National Toll-Free Helpline at 1950. You may also browse the Candidate Transparency or Voter Services tabs."
        val defaultTextHi = "इस चुनाव विषय पर सत्यापित आधिकारिक मार्गदर्शन के लिए, कृपया 24x7 राष्ट्रीय टोल-फ्री हेल्पलाइन 1950 पर संपर्क करें या ऐप के मतदाता सेवा अनुभाग को देखें।"
        return Pair(
            if (language == Language.HINDI) defaultTextHi else defaultTextEn,
            "ECI National Voter Services Portal (voters.eci.gov.in) & Toll-Free 1950"
        )
    }
}
