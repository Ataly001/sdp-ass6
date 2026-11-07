package filesystem;

import java.util.*;

public final class FileSystemWalker {

    public void walk(Node root, Visitor visitor) {
        if (root == null || visitor == null) {
            throw new IllegalArgumentException(
                    "Root and visitor are required");
        }

        visitor.reset();
        root.traverse(this, visitor, "", 0);
    }

    public void visitNode(Node node, Visitor visitor,
                          String path, int depth) {
        visitor.setLocation(path, depth);
        node.accept(visitor);
    }

    public void walkChildren(DirectoryNode directory, Visitor visitor,
                             String path, int depth) {

        directory.getChildren().stream()
                .sorted(Comparator.comparing(Node::getName))
                .forEach(child -> {
                    String childPath = path.isEmpty()
                            ? child.getName()
                            : path + "/" + child.getName();

                    child.traverse(this, visitor, childPath, depth + 1);
                });
    }
}
