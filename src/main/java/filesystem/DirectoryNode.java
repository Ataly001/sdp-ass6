package filesystem;
import java.util.*;

public final class DirectoryNode extends Node {
    private final List<Node> children = new ArrayList<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void addChild(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }
        if (child.getParent() != null) {
            throw new IllegalArgumentException(
                    "Child already has a parent");
        }

        for (DirectoryNode current = this;
             current != null;
             current = current.getParent()) {

            if (current == child) {
                throw new IllegalArgumentException("Cycle detected");
            }
        }

        for (Node existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException(
                        "Duplicate sibling name");
            }
        }

        child.attachTo(this);
        children.add(child);
    }

    public List<Node> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void traverse(FileSystemWalker walker, Visitor visitor,
                         String path, int depth) {
        walker.visitNode(this, visitor, path, depth);
        walker.walkChildren(this, visitor, path, depth);
    }
}
