package de.codeshield.cloudscan.serverlessGoatJava;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/** @author Manuel Benz 1.8.18 */
public class IOUtils {

  /**
   * Export a resource embedded in a Jar file to the local file path. The given resource name has to
   * include the path of the resource. "/resname" will lead to searching the resource in the root
   * folder "resname" will search in the classes package.
   *
   * @param resourceName ie.: "/SmartLibrary.dll"
   * @param outDir Directory to write to. A file with the resourceName will be created. in this
   *     directory
   * @return The path to the exported resource
   * @throws Exception
   */
  public static Path copyResourceToFile(Class askingClass, String resourceName, Path outDir)
      throws IOException {
    Path outFile;

    final Path resourcePath = getResourcePath(askingClass, resourceName);

    outFile = getOutPath(resourceName, outDir);

    // create dir if non existent
    Files.createDirectories(outFile.getParent());

    Files.copy(resourcePath, outFile);

    closeFsIfNecessary(resourcePath);

    return outFile;
  }

  /**
   * Export all resources embedded under the given prefix in a Jar file to the local file path. The
   * given resource name has to include the path of the resource. "/resname" will lead to searching
   * the resource in the root folder, "resname" will search in the given class' package.
   *
   * @param askingClass Class that wants to acquire the resource. The class' package is used to root
   *     the path of the searched resource.
   * @param resourceName ie.: "/folder" to extract all content under "/folder"
   * @param outDir Directory to write to.
   * @throws Exception
   * @return All paths of extracted files
   */
  public static Path copyResourceToFileRecursively(
      Class askingClass, String resourceName, final Path outDir) throws IOException {

    Path resourcePath = getResourcePath(askingClass, resourceName);

    final Path outRoot = getOutPath(resourceName, outDir);

    Files.walkFileTree(
        resourcePath,
        new SimpleFileVisitor<Path>() {
          private Path currentTarget;

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
              throws IOException {
            currentTarget = outRoot.resolve(resourcePath.relativize(dir).toString());
            Files.createDirectories(currentTarget);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.copy(
                file, outRoot.resolve(resourcePath.relativize(file).toString()), REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
          }
        });

    closeFsIfNecessary(resourcePath);

    return outRoot;
  }

  private static void closeFsIfNecessary(Path resourcePath) throws IOException {
    try {
      resourcePath.getFileSystem().close();
    } catch (UnsupportedOperationException e) {
      // we cannot close the default fs...just ignore it
    }
  }

  /**
   * Constructs a java.nio.Path for the searched resource.
   *
   * @param askingClass
   * @param resourceName Resouce name relative to the package of the askingClass or to the resource
   *     root if prepended with "/".
   * @return
   * @throws IOException
   */
  public static Path getResourcePath(Class askingClass, String resourceName) throws IOException {

    URI uri;

    try {
      URL resource = askingClass.getResource(resourceName);

      if (resource == null) {
        throw new IOException(
            String.format("Resource %s not found for class %s", resourceName, askingClass));
      }

      uri = resource.toURI();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }

    if (uri.getScheme().equals("file")) {
      return Paths.get(uri);
    }

    FileSystem zipfs;
    try {
      zipfs = FileSystems.getFileSystem(uri);
    } catch (FileSystemNotFoundException e) {
      // it is not a file. Try to create our own fs (e.g. zipFS)
      Map<String, String> env = new HashMap<>();
      env.put("create", "true");
      zipfs = FileSystems.newFileSystem(uri, env);
    }
    return zipfs.getPath(resourceName);
  }

  private static Path getOutPath(String resourceName, Path outDir) {
    resourceName = toRelativeResourcePath(resourceName);

    Path outFile = outDir.resolve(resourceName);
    return outFile;
  }

  private static String toRelativeResourcePath(String resourceName) {
    if (resourceName.startsWith("/")) {
      resourceName = resourceName.substring(1);
    }
    return resourceName;
  }
}
