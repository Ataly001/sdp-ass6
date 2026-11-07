package filesystem;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    @Test
    void testDoubleDispatch() {
        int[] calls = {0, 0};

        Visitor visitor = new Visitor() {
            public void visit(FileNode file) {
                calls[0]++;
            }

            public void visit(DirectoryNode directory) {
                calls[1]++;
            }
        };

        new FileNode("a.txt", 100).accept(visitor);
        new DirectoryNode("docs").accept(visitor);

        assertEquals(1, calls[0]);
        assertEquals(1, calls[1]);
    }

    @Test
    void testTotalSize() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");

        root.addChild(new FileNode("a.txt", 100));
        docs.addChild(new FileNode("b.txt", 200));
        root.addChild(docs);

        TotalSizeVisitor visitor = new TotalSizeVisitor();
        new FileSystemWalker().walk(root, visitor);

        assertEquals(300, visitor.getTotalSize());
    }

    @Test
    void testSearchFilters() {
        DirectoryNode root = new DirectoryNode("root");

        root.addChild(new FileNode("a.txt", 100));
        root.addChild(new FileNode("b.txt", 200));
        root.addChild(new FileNode("c.java", 300));

        SearchVisitor visitor = new SearchVisitor(
                name -> name.endsWith(".txt"), 200);

        new FileSystemWalker().walk(root, visitor);

        assertEquals(List.of("b.txt"), visitor.getMatches());
    }

    @Test
    void testSearchPaths() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");

        root.addChild(new FileNode("z.txt", 100));
        root.addChild(new FileNode("a.txt", 100));
        docs.addChild(new FileNode("b.txt", 100));
        root.addChild(docs);

        SearchVisitor visitor = new SearchVisitor(
                name -> name.endsWith(".txt"), 0);

        new FileSystemWalker().walk(root, visitor);

        assertEquals(
                List.of("a.txt", "docs/b.txt", "z.txt"),
                visitor.getMatches()
        );
    }

    @Test
    void testStatistics() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");

        root.addChild(new FileNode("a.txt", 100));
        docs.addChild(new FileNode("b.txt", 200));
        root.addChild(docs);

        StatisticsVisitor visitor = new StatisticsVisitor();
        new FileSystemWalker().walk(root, visitor);

        assertEquals(2, visitor.getFileCount());
        assertEquals(2, visitor.getDirectoryCount());
        assertEquals(2, visitor.getMaximumDepth());
    }

    @Test
    void testEmptyAndNoMatches() {
        DirectoryNode root = new DirectoryNode("empty");

        TotalSizeVisitor size = new TotalSizeVisitor();
        StatisticsVisitor stats = new StatisticsVisitor();
        SearchVisitor search = new SearchVisitor(
                name -> name.endsWith(".txt"), 0);

        FileSystemWalker walker = new FileSystemWalker();
        walker.walk(root, size);
        walker.walk(root, stats);
        walker.walk(root, search);

        assertEquals(0, size.getTotalSize());
        assertEquals(0, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(0, stats.getMaximumDepth());
        assertTrue(search.getMatches().isEmpty());
    }

    @Test
    void testRepeatedExecution() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 100));

        TotalSizeVisitor size = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor(
                name -> name.endsWith(".txt"), 0);

        FileSystemWalker walker = new FileSystemWalker();

        walker.walk(root, size);
        walker.walk(root, search);

        walker.walk(root, size);
        walker.walk(root, search);

        assertEquals(100, size.getTotalSize());
        assertEquals(List.of("a.txt"), search.getMatches());
    }

    @Test
    void testInvalidTree() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode child = new DirectoryNode("child");
        root.addChild(child);

        assertThrows(IllegalArgumentException.class,
                () -> new FileNode("bad.txt", -1));

        assertThrows(IllegalArgumentException.class,
                () -> root.addChild(new FileNode("child", 10)));

        assertThrows(IllegalArgumentException.class,
                () -> child.addChild(root));

        DirectoryNode another = new DirectoryNode("another");

        assertThrows(IllegalArgumentException.class,
                () -> another.addChild(child));

        assertEquals(1, root.getChildren().size());
    }

    @Test
    void testMinimumSizeInclusive() {
        DirectoryNode root = new DirectoryNode("root");

        root.addChild(new FileNode("small.txt", 199));
        root.addChild(new FileNode("exact.txt", 200));

        SearchVisitor visitor = new SearchVisitor(
                name -> name.endsWith(".txt"), 200);

        new FileSystemWalker().walk(root, visitor);

        assertEquals(List.of("exact.txt"), visitor.getMatches());
    }

    @Test
    void testReadOnlyChildren() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 100));

        assertThrows(UnsupportedOperationException.class,
                () -> root.getChildren().clear());

        assertEquals(1, root.getChildren().size());
    }
}
