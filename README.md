# 📷 Snaps to Visual Narratives

![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)

This Android app transforms a captured photo into a short story and visualizes each sentence as an
AI-generated scene using the Gemini multimodal API. It was showcased during the UNC Computer Science
60th Anniversary Open House, held at Sitterson Hall on April 12, 2025.

---

## Requirements

- Android Studio
- Android 8.0+ (API level 26+)
- Google Gemini API key

Add the key to a `.env` file in the root directory:

```env
GEMINI_KEY=your_gemini_api_key_here
```

---

## Capture & Generate

1. Tap **Take a Picture** to open the camera
2. Gemini generates a short story from the photo
3. Tap **Generate Scene** to visualize each sentence as an image
4. Cycle through the AI-rendered illustrations of the story

---

## Run Locally

Open the project in Android Studio and run on a device or emulator with camera support.

Ensure `.env` is properly configured before building.

---

## License

MIT © 2024

---

## Credits

- [Google Gemini API](https://ai.google.dev/)
- [Android](https://developer.android.com/)
- [Volley](https://developer.android.com/training/volley)