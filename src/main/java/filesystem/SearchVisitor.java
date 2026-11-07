package filesystem;
import java.util.*;
import java.util.function.Predicate;

public final class SearchVisitor implements Visitor {
    private final Predicate<String> nameFilter;
    private final long minimumSize;
    private final List<String> matches = new ArrayList<>();
    private String currentPath = "";

    public SearchVisitor(Predicate<String> nameFilter, long minimumSize) {
        if (nameFilter == null || minimumSize < 0) {
            throw new IllegalArgumentException(
                    "Invalid search settings");
        }

        this.nameFilter = nameFilter;
        this.minimumSize = minimumSize;
    }

    @Override
    public void setLocation(String path, int depth) {
        currentPath = path;
    }

    @Override
    public void visit(FileNode file) {
        if (nameFilter.test(file.getName())
                && file.getSize() >= minimumSize) {

            matches.add(currentPath.isEmpty()
                    ? file.getName()
                    : currentPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
    }

    @Override
    public void reset() {
        matches.clear();
        currentPath = "";
    }

    public List<String> getMatches() {
        List<String> sorted = new ArrayList<>(matches);
        Collections.sort(sorted);
        return List.copyOf(sorted);
    }
}
