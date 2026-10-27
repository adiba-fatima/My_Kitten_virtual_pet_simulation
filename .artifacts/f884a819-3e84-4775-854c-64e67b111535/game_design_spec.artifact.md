# Virtual Pet Cat Simulation: Game Design Math & Architecture Specification

This document outlines the core mathematical models, decay/recovery formulas, behavioral thresholds, and edge-case mitigations for the virtual pet simulator.

---

## 1. Time System & Pacing Scaling

### Core Principle
The simulation operates on continuous real-world timestamps (`System.currentTimeMillis()`), ensuring that stat decay is calculated purely based on elapsed wall-clock time rather than variable tick loops. This prevents cheating or discrepancies when switching time modes mid-session or restarting the app.

### Time Scaling Factor ($S$)
- **Normal Mode:** $1\text{ real-world hour} = 1\text{ in-game hour}$ ($S_{\text{normal}} = 1.0$).
- **Fast Mode:** $1\text{ real-world hour} = 5\text{ in-game hours}$ ($S_{\text{fast}} = 5.0$).

### In-Game Hours Elapsed Calculation
$$\Delta h_{\text{in-game}} = \left(\frac{t_{\text{current}} - t_{\text{lastSave}}}{3,600,000}\right) \times S$$
*(where time is in milliseconds)*

---

## 2. Hunger Model

### Decay & Recovery Formulas
- **Decay Rate:** Decays from $100 \to 0$ over **8 in-game hours** when unfed.
  - Base decay rate: $r_H = \frac{100}{8} = 12.5\text{ units / in-game hour}$.
  - Formula per update: $\text{Hunger}_{\text{new}} = \max\left(0, \text{Hunger}_{\text{old}} - (r_H \times \Delta h_{\text{in-game}})\right)$.
- **Feeding Mechanics & Lockout Threshold:**
  - **Threshold:** $\text{Hunger} \le 75\%$.
  - **Exploit Fix:** If $\text{Hunger} > 75\%$, the cat refuses food (*"I'm too full! 🐾"*), preventing coin/food wasting and infinite stat farming.
  - **Recovery:** Each food item restores $+25$ hunger, capped at a maximum of 2 items per feeding session.

---

## 3. Rest Model & Sleep

### Decay & Recovery Formulas
- **Decay Rate:** Decays from $100 \to 0$ over **12 in-game hours**.
  - Base decay rate: $r_R = \frac{100}{12} \approx 8.33\text{ units / in-game hour}$.
- **Sleep Recovery Rate:**
  - Fully restores Rest from $0 \to 100$ in **4 in-game hours** of sleep.
  - Recovery rate: $v_R = \frac{100}{4} = 25\text{ units / in-game hour}$.
  - Real-world duration to fully rest:
    - Normal mode ($S=1$): 4 real-world hours.
    - Fast mode ($S=5$): $4 / 5 = 0.8$ real-world hours (48 minutes).

### Interruptibility
- Sleep is **fully interruptible** by user actions (feeding, playing, petting) or automatically when Rest reaches 100%.

---

## 4. Play & Mood Model

### Decay & Recovery Formulas
- **Play Stat (Boredom/Stimulation):** Decays from $100 \to 0$ over **6 in-game hours** of zero interaction.
  - Decay rate: $r_P = \frac{100}{6} \approx 16.67\text{ units / in-game hour}$.
- **Play Action & Cooldown:**
  - Restores Play by $+30$ units.
  - **Cooldown:** 15 in-game minutes between play sessions to prevent reward farming.

---

## 5. Derived Energy Formula

### Formula
Rather than storing Energy as an independent decay stat, Energy is dynamically derived from Hunger and Rest, reflecting biological reality (a hungry or exhausted cat has no energy):
$$\text{Energy} = \text{clamp}\left(0.40 \times \text{Hunger} + 0.60 \times \text{Rest}, 0, 100\right)$$

### Direct Nudges
- **Feeding:** $+5$ temporary energy boost (digestion warmth).
- **Playing:** $-10$ energy cost due to physical exertion.

---

## 6. Happiness Condition & Role of Play

### Happiness Accumulation Rule
- Happiness (and subsequently aging) increases by $+1$ unit per hour **only when** Hunger $= 100$, Rest $= 100$, and Energy $= 100$ **simultaneously**.
- **Role of Play:** Play stat must be $> 50\%$. If the cat is bored ($\le 50$), happiness accumulation is paused even if physical stats are maxed.

---

## 7. Aging Pacing

### Rule
- Age increments by 1 day for every 24 accumulated hours of optimal happiness/care.
- **Pacing in Fast Mode ($S=5$):** To prevent hyper-aging, aging progress scales against active engagement time rather than raw wall-clock acceleration, ensuring 1 cat year takes consistent meaningful interaction time.

---

## 8. Behavioral Realism & Thresholds

| Stat Level | Behavioral State | Visual / Animation | Notification Urgency |
| :--- | :--- | :--- | :--- |
| **$> 50\%$** | Content / Playful | Normal idle / alert | None |
| **$30\% - 50\%$** | Mildly tired / hungry | Yawning / looking around | Gentle prompt |
| **$15\% - 30\%$** | Warning / Uncomfortable | Pacing / Meowing / Drooping ears | Pulsing icon & badge |
| **$< 15\%$** | Critical Distress | Crying / Hyperventilating / Sleeping curled up | High-priority system alert |

---

## 9. Edge Cases & Exploits (Mitigations)

1. **Simultaneous 0 (Hunger & Rest):**
   - *Issue:* Cat could crash or enter invalid state.
   - *Fix:* Implement a Health Buffer (HP pool) that drains when both Hunger and Rest hit 0, triggering an emergency rescue state instead of abrupt failure.
2. **Time Mode Switching Exploits:**
   - *Issue:* Toggling Normal/Fast modes could exploit decay calculation jumps.
   - *Fix:* Store absolute Unix timestamps (`System.currentTimeMillis()`) and compute absolute in-game hours elapsed with the active mode multiplier at the moment of calculation.
