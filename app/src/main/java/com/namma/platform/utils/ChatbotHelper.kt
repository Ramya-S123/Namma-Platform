package com.namma.platform.utils

object ChatbotHelper {

    fun getResponse(query: String, useKannada: Boolean): String {
        val q = query.lowercase().trim()

        return when {
            q.contains("platform") || q.contains("ಪ್ಲಾಟ್") ->
                if (useKannada) "ಪ್ಲಾಟ್‌ಫಾರ್ಮ್ ಸಂಖ್ಯೆಯನ್ನು ನಿಮ್ಮ ಟ್ರೈನ್ ಕಾರ್ಡ್ ಮೇಲೆ ನೋಡಿ." 
                else "Check the platform number on your train card."

            q.contains("general") || q.contains("ಸಾಮಾನ್ಯ") ->
                if (useKannada) "ಸಾಮಾನ್ಯ ಬೋಗಿ ಹಸಿರು ಬಣ್ಣದಲ್ಲಿ ಹೈಲೈಟ್ ಆಗಿದೆ. ಎಂಜಿನ್ ನಂತರ ನಿಲ್ಲಿ."
                else "General coach is highlighted in green. Stand after the engine."

            q.contains("ladies") || q.contains("ಮಹಿಳೆ") || q.contains("ಲೇಡೀ") ->
                if (useKannada) "ಮಹಿಳಾ ಬೋಗಿ ಗುಲಾಬಿ ಬಣ್ಣದಲ್ಲಿ ಹೈಲೈಟ್ ಆಗಿದೆ."
                else "Ladies coach is highlighted in pink."

            q.contains("delay") || q.contains("ತಡ") ->
                if (useKannada) "ತಡವಾದ ಟ್ರೈನ್‌ಗಳು ಕಿತ್ತಳೆ ಬಣ್ಣದಲ್ಲಿ ಕಾಣಿಸುತ್ತವೆ."
                else "Delayed trains appear in orange color."

            q.contains("cancel") || q.contains("ರದ್ದು") ->
                if (useKannada) "ರದ್ದಾದ ಟ್ರೈನ್‌ಗಳು ಕೆಂಪು ಬಣ್ಣದಲ್ಲಿ ಕಾಣಿಸುತ್ತವೆ."
                else "Cancelled trains appear in red color."

            q.contains("help") || q.contains("ಸಹಾಯ") ->
                if (useKannada) "ನಾನು ಪ್ಲಾಟ್‌ಫಾರ್ಮ್, ಬೋಗಿ ಸ್ಥಾನ, ತಡೆ ಮತ್ತು ರದ್ದು ಬಗ್ಗೆ ಸಹಾಯ ಮಾಡುತ್ತೇನೆ."
                else "I can help with platform, coach position, delays and cancellations."

            q.contains("station") || q.contains("ನಿಲ್ದಾಣ") ->
                if (useKannada) "ಮೇಲಿನ ಹೋಮ್ ಪೇಜ್‌ನಲ್ಲಿ ನಿಲ್ದಾಣವನ್ನು ಹುಡುಕಿ ಆಯ್ಕೆ ಮಾಡಿ."
                else "Search and select a station on the Home page."

            else ->
                if (useKannada) "ಕ್ಷಮಿಸಿ, ನಾನು ಅರ್ಥಮಾಡಿಕೊಂಡಿಲ್ಲ. 'ಪ್ಲಾಟ್‌ಫಾರ್ಮ್', 'ಸಾಮಾನ್ಯ ಬೋಗಿ' ಅಥವಾ 'ಸಹಾಯ' ಎಂದು ಕೇಳಿ."
                else "Sorry, I didn't understand. Try asking about 'platform', 'general coach', or 'help'."
        }
    }
}
