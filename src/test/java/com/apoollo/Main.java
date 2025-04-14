/**
 * 
 */
package com.apoollo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuyulong
 * @since 2025-04-10
 */
public class Main {

	public static void main(String[] args) {

		String originPath = "C:\\Users\\DELL\\Desktop\\test\\大模型知识库-v1";
		String targetPath = "C:\\Users\\DELL\\Desktop\\test\\target3";

		String[] supportExtentions = new String[] { "txt", "md", "mdx", "pdf", "html", "xlsx", "xls", "docx", "csv",
				"markdown", "htm" };
		Collection<File> files = FileUtils.listFiles(new File(originPath), supportExtentions, true);
		write(files, targetPath, null);

		String[] notSupportExtentions = new String[] { "java", "gradle", "properties", "cs", "xaml", "resx", "settings",
				"config", "sln", "ini", "cmake", "h", "cc", "hpp", "c", "cpp", "js", "kt", "py", "rc" };
		Collection<File> notSupportFiles = FileUtils.listFiles(new File(originPath), notSupportExtentions, true);
		write(notSupportFiles, targetPath, "txt");

	}

	private static void write(Collection<File> files, String targetPath, String suffix) {
		files.stream().sorted().forEach(file -> {
			try {

				File targetFile = new File(targetPath, file.getName());

				File cacled = getTargetFile(targetFile, suffix);
				System.out.println(cacled);
				FileUtils.copyFile(file, cacled);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static File getTargetFile(File file, String suffix) {
		String name = StringUtils.substringBeforeLast(file.getName(), ".");
		if (null == suffix) {
			suffix = StringUtils.substringAfterLast(file.getName(), ".");
		} else {
			file = new File(file.getParentFile(), StringUtils.join(name, ".", suffix));
		}
		if (file.exists()) {
			String fileName = null;
			String namePrefix = StringUtils.substringBeforeLast(name, "-");
			Integer nameEndNumber = 1;
			if (-1 != name.lastIndexOf("-")) {
				String digital = StringUtils.substringAfterLast(name, "-");
				if (digital.matches("^[0-9]+$")) {
					nameEndNumber = Integer.valueOf(digital) + 1;
				}
			}
			fileName = StringUtils.join(namePrefix, "-", nameEndNumber, ".", suffix);
			return getTargetFile(new File(file.getParentFile(), fileName), suffix);
		}

		return file;
	}

}
