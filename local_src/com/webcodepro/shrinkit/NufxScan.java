package com.webcodepro.shrinkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Scan through the directories in NufxScan.txt, looking for 
 * *.SHK and *.SDK files.  When one is found, do a file listing
 * (including compression types) and dump to screen.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NufxScan {
	public static void main(String[] args) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(NufxScan.class.getResourceAsStream("NufxScan.txt")));
		String line = r.readLine();
		while (line != null) {
			scanDirectory(line);
			line = r.readLine();
		}
	}
	
	private static void scanDirectory(String dirName) throws IOException {
		File dir = new File(dirName);
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("'" + dirName + "' is not a directory");
		}
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".shk") || name.toLowerCase().endsWith(".sdk");
			}
		});
		for (File file : files) {
			display(file);
		}
	}
	
	private static void display(File archive) throws IOException {
		System.out.printf("Details for %s\n\n", archive.getAbsoluteFile());
		InputStream is = new FileInputStream(archive);
		if (is == null) {
			throw new IOException("Unable to locate '" + archive.getAbsoluteFile() + "'");
		}
		NuFileArchive a = new NuFileArchive(is);
		System.out.println("Ver# Threads  FSId FSIn Access   FileType ExtraTyp Stor Thread Formats..... Filename");
		System.out.println("==== ======== ==== ==== ======== ======== ======== ==== =================== ==============================");
		for (HeaderBlock b : a.getHeaderBlocks()) {
			System.out.printf("%04x %08x %04x %04x %08x %08x %08x %04x ",
					b.getVersionNumber(), b.getTotalThreads(), b.getFileSysId(), b.getFileSysInfo(), b.getAccess(),
					b.getFileType(), b.getExtraType(), b.getStorageType());
			int threadsPrinted = 0;
			String filename = b.getFilename();
			for (ThreadRecord r : b.getThreadRecords()) {
				threadsPrinted++;
				System.out.printf("%04x ", r.getThreadFormat().getThreadFormat());
				if (r.getThreadKind() == ThreadKind.FILENAME) {
					filename = r.getText();
				}
			}
			while (threadsPrinted < 4) {
				System.out.printf("     ");
				threadsPrinted++;
			}
			if (filename == null || filename.length() == 0) {
				filename = "<Unknown>";
			}
			System.out.println(filename);
		}
		System.out.println();
	}
}
