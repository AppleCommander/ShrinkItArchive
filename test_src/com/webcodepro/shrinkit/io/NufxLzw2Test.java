package com.webcodepro.shrinkit.io;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.webcodepro.shrinkit.HeaderBlock;
import com.webcodepro.shrinkit.NuFileArchive;
import com.webcodepro.shrinkit.ThreadKind;
import com.webcodepro.shrinkit.ThreadRecord;

public class NufxLzw2Test extends TestCase {
	public void testPascalFile() throws IOException {
		NuFileArchive archive = new NuFileArchive(getClass().getResourceAsStream("APPLE.II-LZW2.SHK"));
		List<HeaderBlock> blocks = archive.getHeaderBlocks();
		HeaderBlock block = blocks.get(0);
		if (block.getFilename() != null) System.out.printf("\n\n%s\n\n", block.getFilename());
		List<ThreadRecord> records = block.getThreadRecords();
		for (ThreadRecord record : records) {
			if (record.getThreadKind() == ThreadKind.FILENAME) {
				System.out.printf("\n\n%s\n\n", record.getText());
			}
			long bytes = record.getThreadEof();
			if (record.getThreadKind() == ThreadKind.DATA_FORK) {
				NufxLzw2InputStream is = new NufxLzw2InputStream(new LittleEndianByteInputStream(record.getRawInputStream()));
				while ( bytes-- > 0 ) {
					System.out.print((char)is.read());
				}
			}
		}
	}
}
