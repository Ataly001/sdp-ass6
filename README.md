# Assignment 06 — Visitor Pattern

## 1. Project Description

This project implements the **Visitor Design Pattern** using a simple in-memory file system. It supports files, nested directories, and independent operations without changing the file system elements.

## 2. Features

The project includes three visitors:

- **TotalSizeVisitor** — calculates the total size of all files.
- **SearchVisitor** — searches files by filename condition and minimum size (`size >= minimumSize`).
- **StatisticsVisitor** — counts files, directories, and maximum tree depth.

The file system prevents negative file sizes, duplicate sibling names, multiple parents, and directory cycles.

## 3. Project Structure

```text
src/
├── main/java/filesystem/
│   ├── Element.java
│   ├── Node.java
│   ├── FileNode.java
│   ├── DirectoryNode.java
│   ├── Visitor.java
│   ├── FileSystemWalker.java
│   ├── TotalSizeVisitor.java
│   ├── SearchVisitor.java
│   ├── StatisticsVisitor.java
│   └── Main.java
└── test/java/filesystem/
    └── FileSystemTest.java
```

## 4. How to Run

**Requirements:** Java 17 and Maven 3.

Run tests from the project root:

```bash
mvn clean test
```

Run the application:

```bash
mvn compile exec:java
```

## 5. Example Output

```text
Total size: 1800 bytes
Found files: [docs/notes.txt]
Files: 4
Directories: 3
Maximum depth: 2
```

## 6. Design Explanation

**Double Dispatch:** Each element implements `accept(Visitor)` and calls `visitor.visit(this)`. This selects the appropriate visit method for files or directories.

**Traversal:** `FileSystemWalker` performs depth-first traversal and visits each reachable element once. Visitors only process elements.

**Search:** Results are sorted lexicographically, use `/` as the separator, and contain paths relative to the selected root.

**Depth:** Root depth is 0, and each nested level increases depth by 1.

**Reusability:** Visitors can be reused sequentially because the walker calls `reset()` before each execution.

**Extensibility:** `StatisticsVisitor` was added without changing existing element classes. Adding a new element type, however, requires updating the Visitor interface and its implementations.

## 7. Testing

The project includes **10 JUnit 5 tests** covering all required checkpoints (T1–T8):

- Double dispatch and total size calculation.
- Search filters, paths, and sorting.
- Statistics and empty directories.
- Repeated visitor execution.
- Invalid tree operations and encapsulation.
- Inclusive minimum-size threshold.

**Maven test results:**

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 8. Clean Code Practices

- **Meaningful naming:** Methods such as `addChild()` and `getTotalSize()` clearly describe their purpose.
- **Single Responsibility:** Each visitor performs one independent operation.
- **Small methods:** `accept()`, `visit()`, and `reset()` are short and focused.
- **Encapsulation:** Directory children are protected from external modification.
- **No type-checking chains:** Double dispatch avoids `instanceof` and casts.

## 9. References

-  Lecture 9: Visitor Pattern.
- Freeman, E., & Robson, E. *Head First Design Patterns*, 2nd Edition.
- [Refactoring.Guru — Visitor Pattern](https://refactoring.guru/design-patterns/visitor)
- [JUnit 5 User Guide](https://docs.junit.org/5.11.4/user-guide/)