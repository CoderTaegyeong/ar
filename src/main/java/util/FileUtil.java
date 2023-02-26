package util;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.panel.CustomPanel;

public class FileUtil {
    public static void copy(String src, String newFile) {
        try {
            File srcFile = new File(src);
            File destFile = new File(newFile);
            
            InputStream inputStream = new FileInputStream(srcFile);
            
            OutputStream outputStream = new FileOutputStream(destFile);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            
            inputStream.close();
            outputStream.close();
            
            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  //-----------------------------------create Simple JFileChooser--------------------------------//
  	public static JFileChooser createFileChooser(File file, String... exts) {
  		JFileChooser fileChooser = new JFileChooser(file);
  		if(exts != null && exts.length > 0) {
  			FileNameExtensionFilter filter = new FileNameExtensionFilter("Files", exts);
  			fileChooser.setFileFilter(filter);
  		}
  		return fileChooser;
  	}
  	
  	public static JFileChooser createFileChooser(String... exts) {
  		return createFileChooser(new File(""), exts);
  	}
  	
  	public static File[] getFiles(CustomPanel customPanel, File current, String... exts) {
  		return getFiles(customPanel.getPanel(), current, exts);
  	}
  	
  	public static File[] getFiles(Component parent, File current, String... exts) {
          JFileChooser fileChooser = createFileChooser(exts);
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(true);
          if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
              return fileChooser.getSelectedFiles();
          } else {
              return new File[0];
          }
      }
  	
  	public static File getFile(CustomPanel customPanel, File current, String... exts) {
  		return getFile(customPanel.getPanel(), current, exts);
  	}

  	
  	public static File getFile(Component parent, File current, String... exts) {
          JFileChooser fileChooser = createFileChooser(current, exts);
          if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
              return fileChooser.getSelectedFile();
          } else {
              return null;
          }
      }
  	
  	public static File[] getFiles(String... exts) {
          JFileChooser fileChooser = createFileChooser(exts);
          if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              return fileChooser.getSelectedFiles();
          } else {
              return null;
          }
  	}
  	
  	public static File getFile(String... exts) {
          JFileChooser fileChooser = createFileChooser(exts);
          if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              return fileChooser.getSelectedFile();
          } else {
              return null;
          }
  	}
}