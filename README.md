# 🏃‍♀️ runningXin — Cracking Interview Journey

Welcome to my **interview preparation repository**, where I record my learning journey through **Algorithms**, **Object-Oriented Design (OOD)**, and **System Design (SD)**.

This repo documents both my technical growth and my reflections along the way.

---

## 📘 Folder Structure

| Folder | Description |
|--------|--------------|
| **algos/** | Algorithm practice — each problem has its own folder (e.g. `0001-two-sum/`, `0146-lru-cache/`), containing notes and/or runnable code. |
| **OOD/** | Object-Oriented Design problems and patterns (e.g. Parking Lot, Elevator, Coffee Machine). |
| **SD/** | System Design discussions — architecture, scalability, trade-offs, and diagrams. |

---

## 🧩 Example Folder — Algos

Example: `algos/0001-two-sum/notes.md`

```markdown
# 0001. Two Sum
**Difficulty:** Easy  
**Language:** Java  
**Tags:** Array, HashMap

## Approach
Use a HashMap to store complement and index.

## Code
```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[]{};
}
```
```

---

## 🎯 Goals
- Archive every solved problem with clean code and concise notes  
- Strengthen problem-solving and design skills  
- Build a long-term reference base for interviews

---

## 🌱 About Me
I’m **Xin Liu**, a backend software engineer passionate about clean design, distributed systems, and continuous learning.  
This repo is part of my personal journey toward mastery — one problem at a time. 💪
