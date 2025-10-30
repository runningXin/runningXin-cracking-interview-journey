# 🏃‍♀️ runningXin — Cracking Interview Journey

Welcome to my **interview preparation repository**, where I record my journey through **Algorithms**, **Object-Oriented Design (OOD)**, **System Design (SD)**, and **real-world backend practice**.

This is more than just LeetCode — it's a comprehensive and evolving archive of my problem-solving, design thinking, and backend engineering growth.

---

## 📂 Folder Structure

| Folder              | Description |
|---------------------|-------------|
| **algos/**          | Algorithm practice — each problem in its own folder (e.g. `0132-palindrome-partitioning-ii/`, `0872-leaf-similar-trees/`), with notes and clean Java code. |
| **OOD/**            | Object-Oriented Design problems and patterns (e.g. Parking Lot, Elevator, File System). |
| **SD/**             | System Design discussions — architecture, trade-offs, bottlenecks, diagrams. |
| **backend-practice/** | Real-world backend exercises — including RESTful validation, Spring Boot, Kafka producers, async messaging, etc. Built to simulate engineering test scenarios. |

---

## 🧠 Example — Algos

Example: `algos/0872-leaf-similar-trees/notes.md`

```markdown
# 0872. Leaf-Similar Trees
**Difficulty:** Easy  
**Language:** Java  
**Tags:** Tree, DFS

## Approach
- Use DFS to collect all leaf values from both trees
- Compare the leaf value sequences

## Code
```java
public boolean leafSimilar(TreeNode root1, TreeNode root2) {
    List<Integer> leaves1 = new ArrayList<>();
    List<Integer> leaves2 = new ArrayList<>();
    dfs(root1, leaves1);
    dfs(root2, leaves2);
    return leaves1.equals(leaves2);
}