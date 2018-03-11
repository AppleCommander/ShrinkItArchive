package com.webcodepro.shrinkit;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Scan through the directories in NufxScan.txt, looking for 
 * *.SHK and *.SDK files.  When one is found, do a file listing
 * (including compression types) and dump to screen.
 * <p>
 * Adding some minor hard-coded searching capabilities.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NufxScan {
	private static File archiveWithSmallestCompressedFile;
	private static String smallestCompressedFilename;
	private static long sizeOfSmallestCompressedFile;
	
	public static void main(String[] args) throws IOException {
		for (String dir : args) {
			scanDirectory(dir);
		}
	}
	
	private static void scanDirectory(String dirName) throws IOException {
		File dir = new File(dirName);
		scanDirectory(dir);
	}
	
	private static void scanDirectory(File directory) throws IOException {
		System.out.printf("Scanning '%s'...\n", directory.toString());
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("'" + directory.toString() + "' is not a directory");
		}
		File[] files = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				boolean isSHK = file.getName().toLowerCase().endsWith(".shk");
				boolean isSDK = file.getName().toLowerCase().endsWith(".sdk");
				boolean isDirectory = file.isDirectory();
				boolean keep = isSHK || isSDK || isDirectory;
				return keep;
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				scanDirectory(file);
			} else {
				displayArchive(file);
			}
		}
		if (sizeOfSmallestCompressedFile != 0) {
			System.out.printf("\n\nSmallest compressed file:\n");
			System.out.printf("Archive = %s\n", archiveWithSmallestCompressedFile.getAbsoluteFile());
			System.out.printf("Filename = %s\n", smallestCompressedFilename);
			System.out.printf("Size = %08x (%d)\n", sizeOfSmallestCompressedFile, sizeOfSmallestCompressedFile);
		}
	}
	
	private static void displayArchive(File archive) throws IOException {
		System.out.printf("Details for %s\n\n", archive.getAbsoluteFile());
		try (InputStream is = new FileInputStream(archive)) {
			NuFileArchive a = new NuFileArchive(is);
			System.out.println("Ver# Threads  FSId FSIn Access   FileType ExtraTyp Stor Thread Formats..... OrigSize CompSize Filename");
			System.out.println("==== ======== ==== ==== ======== ======== ======== ==== =================== ======== ======== ==============================");
			for (HeaderBlock b : a.getHeaderBlocks()) {
				System.out.printf("%04x %08x %04x %04x %08x %08x %08x %04x ",
						b.getVersionNumber(), b.getTotalThreads(), b.getFileSysId(), b.getFileSysInfo(), b.getAccess(),
						b.getFileType(), b.getExtraType(), b.getStorageType());
				int threadsPrinted = 0;
				String filename = b.getFilename();
				long origSize = 0;
				long compSize = 0;
				boolean compressed = false;
				for (ThreadRecord r : b.getThreadRecords()) {
					threadsPrinted++;
					System.out.printf("%04x ", r.getThreadFormat().getThreadFormat());
					compressed |= (r.getThreadFormat() != ThreadFormat.UNCOMPRESSED);
					if (r.getThreadKind() == ThreadKind.FILENAME) {
						filename = r.getText();
					}
					if (r.getThreadClass() == ThreadClass.DATA) {
						origSize+= r.getThreadEof();
						compSize+= r.getCompThreadEof();
					}
				}
				while (threadsPrinted < 4) {
					System.out.printf("     ");
					threadsPrinted++;
				}
				System.out.printf("%08x %08x ", origSize, compSize);
				if (filename == null || filename.length() == 0) {
					filename = "<Unknown>";
				}
				System.out.println(filename);
				if (compressed && (sizeOfSmallestCompressedFile == 0 || compSize < sizeOfSmallestCompressedFile)) {
					sizeOfSmallestCompressedFile = compSize;
					archiveWithSmallestCompressedFile = archive;
					smallestCompressedFilename = filename;
				}
			}
			System.out.println();
		}
	}
}
