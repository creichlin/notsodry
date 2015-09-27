package ch.kerbtier.notsodry;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Copy {
  
  private Path directory;
  private Map<String, String> patterns;
  
  public static void main(String... arguments) throws IOException {
    Path directory = Paths.get(arguments[0]);
    
    Map<String, String> patterns = Arrays.stream(arguments).skip(1).collect(Collectors.toMap((String p) -> {
      return p.split(":")[0];
    }, (String p) -> {
      return p.split(":")[1];
    }));

    new Copy(directory, patterns).copy();
  }

  
  public Copy(Path directory, Map<String, String> patterns) {
    this.directory = directory;
    this.patterns = patterns;
  }


  
  public void copy() throws IOException {
    Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(Path relativeFile, BasicFileAttributes attrs) throws IOException {
        final Path file = relativeFile.toAbsolutePath().normalize();
        
        boolean copyFile = patterns.keySet().stream().anyMatch(p -> {
          return file.toString().indexOf(p) != -1;
        });
        
        if(copyFile) {
          copyFile(file);
        }
        
        return FileVisitResult.CONTINUE;
      }
      
    });
    
  }

  protected void copyFile(Path file) throws IOException {
    String target = patterns.keySet().stream().reduce(file.toString(), (String id, String pattern) -> {
      return id.replace(pattern, patterns.get(pattern));
    });
    System.out.println(file + " -> " + target);
    
    
    List<String> replaced = Files.readAllLines(file, Charset.forName("utf-8")).stream().map((String line) -> {
      return patterns.keySet().stream().reduce(line, (String id, String pattern) -> {
        return id.replace(pattern, patterns.get(pattern));
      });
    }).collect(Collectors.toList());;
    
    Path targetPath = Paths.get(target);
    
    Files.createDirectories(targetPath.getParent());
    
    Files.write(targetPath, replaced);
    
  }

}
