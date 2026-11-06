package filesystem;
public interface Element {
    String getName();
    void accept(Visitor visitor);
    void traverse(FileSystemWalker walker, Visitor visitor,
                  String path, int depth);
}

