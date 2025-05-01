# Snaps to Visual Narratives

![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)

Snaps to Visual Narratives is an Android app that turns photos into illustrated micro-stories. It
uses Google’s Gemini API to generate short narratives from captured images and visualizes each
sentence with AI-generated artwork—ideal for storytelling, creativity, and education. This app was
featured at the UNC Computer Science 60th Anniversary Open House on April 12, 2025.

---

## Setup

- Android Studio
- Android 8.0+ (API level 26+)
- Google Gemini API key

Add your Gemini API key to `local.properties`:

```properties
GEMINI_KEY=your_actual_gemini_api_key_here
```

---

## Usage

1. Tap **Take a Picture** to capture a photo
2. Gemini generates a short story based on the image
3. Tap **Generate Scene** to create visuals for each sentence
4. Swipe through the AI-generated illustrations

---

## Development

Open the project in Android Studio and run it on a device or emulator with camera support.

Ensure your `local.properties` file contains a valid Gemini API key before building.

---

## License

MIT © 2025

---

## Credits

- [Google Gemini API](https://ai.google.dev/)
- [Android](https://developer.android.com/)
- [Volley](https://developer.android.com/training/volley)