package com.webcodepro.shrinkit;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Not really a JUnit test, but this does verify that NuFX archives can be read.
 * We read a couple of samples and dump out the details.
 * <p>
 * Note that to successfully run this, the classpath must have the samples folder
 * added to it.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NuFileArchiveTest extends TestCase {
	public void testReadJoystickShk() throws IOException {
		display("/Joystick.SHK");
	}
	public void testReadSccShk() throws IOException {
		display("/Scc.shk");
	}
	
	private void display(String archiveName) throws IOException {
		System.out.printf("Details for %s\n\n", archiveName);
		InputStream is = getClass().getResourceAsStream(archiveName);
		if (is == null) {
			System.out.printf("*** ERROR:  Unable to locate '%s'", archiveName);
			fail("Unable to locate archive file");
		}
		NuFileArchive a = new NuFileArchive(is);
		MasterHeaderBlock m = a.getMasterHeaderBlock();
		System.out.printf("Master Header Block\n==================\n"
				+ "master_crc=$%x\ntotal_records=%d\narchive_create_when=%tc\narchive_mod_when=%tc\n"
				+ "master_version=%d\nmaster_eof=$%x\nisValidCrc = %b\n\n",
			m.getMasterCrc(), m.getTotalRecords(), m.getArchiveCreateWhen(), m.getArchiveModWhen(), 
			m.getMasterVersion(), m.getMasterEof(), m.isValidCrc());
		for (HeaderBlock b : a.getHeaderBlocks()) {
			System.out.printf("\tHeader Block\n\t============\n");
			System.out.printf("\theader_crc=$%x\n\tattrib_count=%d\n\tversion_number=%d\n\ttotal_threads=%d\n\t"
					+ "file_sys_id=$%x\n\tfile_sys_info=$%x\n\taccess=$%x\n\tfile_type=$%x\n\textra_type=$%x\n\t"
					+ "storage_type=$%x\n\tcreate_when=%tc\n\tmod_when=%tc\n\tarchive_when=%tc\n\toption_size=%d\n\t"
					+ "filename=%s\n\n",
				b.getHeaderCrc(), b.getAttribCount(), b.getVersionNumber(), b.getTotalThreads(), b.getFileSysId(),
				b.getFileSysInfo(), b.getAccess(), b.getFileType(), b.getExtraType(), b.getStorageType(),
				b.getCreateWhen(), b.getModWhen(), b.getArchiveWhen(), b.getOptionSize(), b.getFilename());
			System.out.printf("\t\tThreads\n\t\t=======\n");
			for (ThreadRecord r : b.getThreadRecords()) {
				System.out.printf("\t\tthread_class=%s\n\t\tthread_format=%s\n\t\tthread_kind=%s\n\t\t"
						+ "thread_crc=$%x\n\t\tthread_eof=$%x\n\t\tcompThreadEof=$%x\n",
					r.getThreadClass(), r.getThreadFormat(), r.getThreadKind(), r.getThreadCrc(),
					r.getThreadEof(), r.getCompThreadEof());
				if (r.getThreadKind() == ThreadKind.FILENAME) {
					System.out.printf("\t\tFILENAME=%s\n", r.getText());
				}
				System.out.printf("\n");
			}
		}
	}
}
