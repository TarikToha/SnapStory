# 📷 Gemini Image Story Generator

![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)

An Android app that captures an image using the device camera and generates a 100-word story using
the Gemini multimodal API.

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

1. Tap the **Take a Picture** button to open the camera
2. Capture the image and wait for the Gemini model to respond
3. The generated story is displayed in a scrollable view

---

## Run Locally

Open the project in Android Studio and run on a physical device or emulator with camera support.

---

## License

MIT © 2024

---

## Credits

- [Google Gemini SDK](https://ai.google.dev)
- [Android Jetpack](https://developer.android.com/jetpack)
- [CameraX / Bitmap / Executors]

