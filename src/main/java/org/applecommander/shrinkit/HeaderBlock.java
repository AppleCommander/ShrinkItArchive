/*
 * ShrinkItArchive
 * Copyright (C) 2026  Rob Greene
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 */
package org.applecommander.shrinkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.applecommander.shrinkit.io.LittleEndianByteInputStream;

/**
 * The Header Block contains information and content
 * about a single entry (be it a file or disk image).
 * <p>
 * Note that we need to support multiple versions of the NuFX
 * archive format.  Some details may be invalid, depending on
 * version, and those are documented in the getter methods.
 * 
 * @author robgreene@users.sourceforge.net
 * @see <a href="http://www.nulib.com/library/FTN.e08002.htm">Apple II File Type Note $E0/$8002</a>
 */
public class HeaderBlock {
	private int headerCrc;
	private int attribCount;
	private int versionNumber;
	private long totalThreads;
	private int fileSysId;
	private int fileSysInfo;
	private long access;
	private long fileType;
	private long extraType;
	private int storageType;
	private Date createWhen;
	private Date modWhen;
	private Date archiveWhen;
	private int optionSize;
	private byte[] optionListBytes;
	private byte[] attribBytes;
	private String filename;
	private String rawFilename;
	private long headerSize = 0;
	private List<ThreadRecord> threads = new ArrayList<>();
	
	/**
	 * Create the Header Block.  This is done dynamically since
	 * the Header Block size varies significantly.
	 */
	public HeaderBlock(LittleEndianByteInputStream bs) throws IOException {
		int type = bs.seekFileType(4);
		if (type == 0) {
			throw new IOException("Unable to decode this archive.");
		}
		headerCrc = bs.readWord();
		attribCount = bs.readWord();
		versionNumber = bs.readWord();
		totalThreads = bs.readLong();
		fileSysId = bs.readWord();
		fileSysInfo = bs.readWord();
		access = bs.readLong();
		fileType = bs.readLong();
		extraType = bs.readLong();
		storageType = bs.readWord();
		createWhen = bs.readDate();
		modWhen = bs.readDate();
		archiveWhen = bs.readDate();
		// Read the mysterious option_list
		if (versionNumber >= 1) {
			optionSize = bs.readWord();
			if (optionSize > 0) {
				optionListBytes = bs.readBytes(optionSize-2);
			}
		}
		// Compute attribute bytes that exist and read (if needed)
		int sizeofAttrib = attribCount - 58;
		if (versionNumber >= 1) {
			if (optionSize == 0) sizeofAttrib -= 2;
			else sizeofAttrib -= optionSize;
		}
		if (sizeofAttrib > 0) {
			attribBytes = bs.readBytes(sizeofAttrib);
		}
		// Read the (defunct) filename
		int length = bs.readWord();
		if (length > 0) {
			byte[] data = bs.readBytes(length);
			rawFilename = Utility.makeString(data, length);
		}
		if (rawFilename == null) {
			rawFilename = "Unknown";
		}
	}
	/**
	 * Read in all data threads.  All ThreadRecords are read and then
	 * each thread's data is read (per NuFX spec).
	 */
	public void readThreads(LittleEndianByteInputStream bs) throws IOException {
		for (long l=0; l<totalThreads; l++) threads.add(new ThreadRecord(this, bs));
		for (ThreadRecord r : threads) {
			r.readThreadData(bs);
			headerSize += r.getThreadEof();
		}
	}

	/**
	 * Locate the filename and return it.  It may have been given in the old
	 * location, in which case, it is in the String filename. Otherwise, it will
	 * be in the filename thread.  If it is in the thread, we shove it in the 
	 * filename variable just so we don't need to search for it later.  This 
	 * should not be a problem, because if we write the file, we'll write the
	 * more current version anyway.
	 */
	public String getFilename() {
		if (filename == null) {
			Optional<ThreadRecord> opt = findThreadRecord(ThreadKind.FILENAME);
            opt.ifPresent(threadRecord -> filename = threadRecord.getText());
			if (filename == null) filename = rawFilename;
			if (filename.contains(":")) {
				filename = filename.replace(":","/");
			}
		}
		return filename;
	}
	
	/**
	 * Final element in the path, in those cases where a filename actually holds a path name
	 */
	public String getFinalFilename() {
		String filename = getFilename();
		String[] path;
		path = filename.split("/");
		filename = path[path.length - 1];
		return filename;
	}
	
	/**
	 * Get the data fork.
	 * Note that this first searches the data fork and then searches for a disk image; 
	 * this may not be correct behavior.
	 */
	public Optional<ThreadRecord> getDataForkThreadRecord() {
		return findThreadRecord(ThreadKind.DATA_FORK)
					.or(() -> findThreadRecord(ThreadKind.DISK_IMAGE))
					.or(Optional::empty);
	}

	/**
	 * Get the resource fork.
	 */
	public Optional<ThreadRecord> getResourceForkThreadRecord() {
		return findThreadRecord(ThreadKind.RESOURCE_FORK);
	}

	/**
	 * Locate a ThreadRecord by its ThreadKind.
	 */
	public Optional<ThreadRecord> findThreadRecord(ThreadKind tk) {
		for (ThreadRecord r : threads) {
			if (r.getThreadKind() == tk) return Optional.of(r);
		}
		return Optional.empty();
	}
	
	// HELPER METHODS
	
	/**
	 * Helper method to determine the file system separator.
	 * Due to some oddities, breaking apart by byte value...
	 */
	public String getFileSystemSeparator() {
        return switch (getFileSysInfo() & 0xff) {
            case 0xaf, 0x2f -> "/";
			// Note that $3F is per the documentation(!)
            case 0x3a, 0xba, 0x3f -> ":";
            case 0x5c, 0xdc -> "\\";
            default -> "";
        };
	}
	
	public long getUncompressedSize() {
		long size = 0;
		for (ThreadRecord r : threads) {
			if (r.getThreadClass() == ThreadClass.DATA) {
				size+= r.getThreadEof();
			}
		}
		return size;
	}
	public long getCompressedSize() {
		long size = 0;
		for (ThreadRecord r : threads) {
			if (r.getThreadClass() == ThreadClass.DATA) {
				size+= r.getCompThreadEof();
			}
		}
		return size;
	}

	public String getFileSysIdText() {
		return switch (getFileSysInfo()) {
			case 1 -> "ProDOS/SOS";
			case 2 -> "DOS 3.3";
			case 3 -> "DOS 3.2";
			case 4 -> "Apple II Pascal";
			case 5 -> "Macintosh HFS";
			case 6 -> "Macintosh MFS";
			case 7 -> "Lisa File System";
			case 8 -> "Apple CP/M";
			case 10 -> "MS-DOS";
			case 11 -> "High Sierra";
			case 12 -> "ISO 9660";
			case 13 -> "AppleShare";
			default -> "Reserved";
		};
	}

	// GENERATED CODE
	
	public int getHeaderCrc() {
		return headerCrc;
	}
	public void setHeaderCrc(int headerCrc) {
		this.headerCrc = headerCrc;
	}
	public int getAttribCount() {
		return attribCount;
	}
	public void setAttribCount(int attribCount) {
		this.attribCount = attribCount;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public long getTotalThreads() {
		return totalThreads;
	}
	public void setTotalThreads(long totalThreads) {
		this.totalThreads = totalThreads;
	}
	public int getFileSysId() {
		return fileSysId;
	}
	public void setFileSysId(int fileSysId) {
		this.fileSysId = fileSysId;
	}
	public int getFileSysInfo() {
		return fileSysInfo;
	}
	public void setFileSysInfo(int fileSysInfo) {
		this.fileSysInfo = fileSysInfo;
	}
	public long getAccess() {
		return access;
	}
	public void setAccess(long access) {
		this.access = access;
	}
	public long getFileType() {
		return fileType;
	}
	public void setFileType(long fileType) {
		this.fileType = fileType;
	}
	public long getExtraType() {
		return extraType;
	}
	public void setExtraType(long extraType) {
		this.extraType = extraType;
	}
	public int getStorageType() {
		return storageType;
	}
	public void setStorageType(int storageType) {
		this.storageType = storageType;
	}
	public Date getCreateWhen() {
		return createWhen;
	}
	public void setCreateWhen(Date createWhen) {
		this.createWhen = createWhen;
	}
	public Date getModWhen() {
		return modWhen;
	}
	public void setModWhen(Date modWhen) {
		this.modWhen = modWhen;
	}
	public Date getArchiveWhen() {
		return archiveWhen;
	}
	public void setArchiveWhen(Date archiveWhen) {
		this.archiveWhen = archiveWhen;
	}
	public int getOptionSize() {
		return optionSize;
	}
	public void setOptionSize(int optionSize) {
		this.optionSize = optionSize;
	}
	public byte[] getOptionListBytes() {
		return optionListBytes;
	}
	public void setOptionListBytes(byte[] optionListBytes) {
		this.optionListBytes = optionListBytes;
	}
	public byte[] getAttribBytes() {
		return attribBytes;
	}
	public void setAttribBytes(byte[] attribBytes) {
		this.attribBytes = attribBytes;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getRawFilename() {
		return rawFilename;
	}
	public List<ThreadRecord> getThreadRecords() {
		return threads;
	}
	public void setThreadRecords(List<ThreadRecord> threads) {
		this.threads = threads;
	}
	public long getHeaderSize() {
		return headerSize;
	}
}
