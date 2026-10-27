# 🐾 My Kitten: Virtual Pet Simulation App
## Project Presentation & Architecture Deck

---

## Slide 1: Title & Introduction
### **My Kitten: Your Immersive Virtual Pet Companion**
- **App Concept:** A feature-rich, real-time virtual pet simulation for Android inspired by classic tamagotchi & pet simulators, built with native Android (Kotlin & XML Views).
- **Core Experience:** Adopt a kitten, feed, play, put to sleep, explore multiple rooms, decorate, earn coins, and watch your companion grow!
- **Key Highlights:**
  - Continuous real-world time-based simulation engine.
  - Dynamic homeostatic energy drift & behavioral realism.
  - Multi-room navigation & interactive "Find the Cat" minigame.
  - Persistent storage & intelligent push notifications.

---

## Slide 2: Project Scope & Work Done
### **What We Have Built (Feature Summary)**
| Feature Module | Description & Implementation Status |
| :--- | :--- |
| **Splash & Onboarding** | Splash screen with animated branding, name entry, and adoption flow (`AdoptActivity`). |
| **Adoption & Breeds** | Choose from Ginger Tabby, Tuxedo Cat, and Calico Cat, each with detailed lore & care guides. |
| **Simulation Engine** | Core mathematics for Hunger, Rest, Play, Energy decay, and optimal care age progression (`PetSimulationEngine`). |
| **Multi-Room Navigation** | Explore Living Room 🛋️, Kitchen 🍳, Bedroom 🛏️, and Playroom 🧸 with customizable room props. |
| **Shop & Inventory** | Economy system (`ShopActivity`) to buy food (Fish, Kibble, Milk, Treats) and room decorations (Box, Plant, Curtain, Cushion). |
| **Background & Notifications** | `NotificationHelper` sending intelligent push alerts (Hunger/Rest <= 25%) with cooldown timers. |
| **Game Pace Toggle** | Normal mode (1x) vs. Fast mode (5x time scaling) for rapid testing and gameplay flexibility. |

---

## Slide 3: How We Did It – Architecture & Tech Stack
### **Technical Architecture**
- **Language & Framework:** 100% Kotlin, Android SDK, AppCompat, Material Design components.
- **UI Architecture:** Multi-Activity MVVM/Controller pattern (`MainActivity`, `AdoptActivity`, `ShopActivity`, `LoginActivity`, `SplashActivity`).
- **Layouts & Styling:** Custom XML layouts using `ConstraintLayout`, card backgrounds, progress bars, and custom animations (`cat_wiggle.xml`).
- **Data Persistence:** Lightweight local persistence using Android `SharedPreferences` to save pet stats, timestamps, inventory counts, coins, and adoption choices.
- **Asynchronous Tick Loop:** Android `Handler` and `Runnable` ticking every second to recalculate simulation stats in real-time.

---

## Slide 4: Core Engineering – The Mathematical Simulation Engine
### **Precision Time & Homeostatic Drift (`PetSimulationEngine.kt`)**
- **Timestamp Delta Calculation:**
  $$\Delta h_{\text{in-game}} = \left(\frac{t_{\text{current}} - t_{\text{lastSave}}}{3600000}\right) \times \text{Scale}$$
  *(Scale = 1.0 for Normal, 5.0 for Fast Mode)*
- **Decay Models:**
  - Hunger: Decays $100 \to 0$ over 8 in-game hours ($12.5\text{ units/hr}$).
  - Rest: Decays over 12 in-game hours or recovers during sleep ($+25\text{ units/hr}$).
  - Play: Decays over 6 in-game hours with a 15-minute real-world cooldown.
- **Closed-Form Energy Drift:**
  $$\text{Target}_{\text{Energy}} = 0.40 \times \text{Hunger} + 0.60 \times \text{Rest}$$
  $$\text{Energy}_{\text{new}} = \text{Target} + (\text{Energy}_{\text{old}} - \text{Target}) \times e^{-\lambda \times \Delta h}$$
- **Time-Independent Age Progression:** Optimal care tracking ($Hunger=100, Rest=100, Energy=100, Play>50\%$) accumulates real hours toward leveling up age every 24 optimal hours.

---

## Slide 5: Technical & Design Challenges Overcome
### **1. Time Drift & Offline Progression**
- **Challenge:** Calculating stats correctly when the app is closed, paused, or restarted after hours/days.
- **Solution:** Switched from ticking timers to absolute wall-clock timestamp comparisons (`System.currentTimeMillis()`), ensuring exact deterministic decay upon app resume.
- **2. Stat Coupling & Stability**
- **Challenge:** Energy fluctuating erratically when multiple stats changed simultaneously.
- **Solution:** Implemented exponential decay homeostatic drift chasing a weighted hunger/rest target, preventing abrupt UI jumps.
- **3. Notification Spam Prevention**
- **Challenge:** Avoiding annoying repetitive notification pings when stats hovered below threshold.
- **Solution:** Implemented a strict 30-minute cooldown timestamp check in `NotificationHelper`.

---

## Slide 6: Future Scope & Roadmap
### **Where We Can Take "My Kitten" Next**
- ☁️ **Cloud Sync & User Profiles:** Integrate Firebase Auth & Firestore to back up pet stats across devices.
- 🎮 **Expanded Mini-Games:** Add interactive catch-the-mouse, yarn ball batting physics, and grooming minigames.
- 📱 **Adaptive UI & Compose Migration:** Modernize views to Jetpack Compose with adaptive layouts for tablets, foldables, and Wear OS companion tiles.
- 🗣️ **AI & Voice Integration:** Leverage Android AppFunctions and Assistant actions to let users feed or check on their cat via voice commands.

---

## Slide 7: Conclusion & Q&A
### **Summary**
- **"My Kitten"** successfully blends engaging virtual pet mechanics with robust architectural design, state persistence, and mathematical realism.
- **Thank you!** Open for questions and feedback.
