package filesystem;

public interface Visitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);

    default void reset() {
    }

    default void setLocation(String path, int depth) {
    }
}
