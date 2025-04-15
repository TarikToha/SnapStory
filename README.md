# 📷 Snaps to Visual Narratives

![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)

An Android app that transforms a captured photo into a short story and illustrates the sentences
using the Gemini multimodal API.


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
3. Tap **Next** to visualize each sentence as an image
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

- [Google Gemini SDK](https://ai.google.dev)
- [Android Jetpack](https://developer.android.com/jetpack)
- [Volley](https://developer.android.com/training/volley)

---