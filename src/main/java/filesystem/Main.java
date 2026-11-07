package filesystem;
public final class Main {

    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("project");

        root.addChild(new FileNode("Main.java", 500));
        root.addChild(new FileNode("README.txt", 100));

        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("notes.txt", 200));
        root.addChild(docs);

        DirectoryNode images = new DirectoryNode("images");
        images.addChild(new FileNode("logo.png", 1000));
        root.addChild(images);

        FileSystemWalker walker = new FileSystemWalker();

        TotalSizeVisitor size = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor(
                name -> name.endsWith(".txt"), 150);
        StatisticsVisitor stats = new StatisticsVisitor();

        walker.walk(root, size);
        walker.walk(root, search);
        walker.walk(root, stats);

        System.out.println("Total size: "
                + size.getTotalSize() + " bytes");
        System.out.println("Found files: " + search.getMatches());
        System.out.println("Files: " + stats.getFileCount());
        System.out.println("Directories: " + stats.getDirectoryCount());
        System.out.println("Maximum depth: " + stats.getMaximumDepth());
    }
}
