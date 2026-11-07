package filesystem;

public abstract class Node implements Element {
    private final String name;
    private DirectoryNode parent;

    protected Node(String name) {
        if (name == null || name.isBlank()
                || name.contains("/") || name.contains("\\")
                || name.equals(".") || name.equals("..")
                || name.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Invalid node name");
        }

        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    public final DirectoryNode getParent() {
        return parent;
    }

    final void attachTo(DirectoryNode directory) {
        if (directory == null || parent != null) {
            throw new IllegalArgumentException("Invalid parent");
        }
        parent = directory;
    }
}
