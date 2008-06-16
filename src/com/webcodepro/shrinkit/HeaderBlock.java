package com.webcodepro.shrinkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Header Block contains information and content
 * about a single entry (be it a file or disk image).
 * <p>
 * Note that we need to support multiple versions of the NuFX
 * archive format.  Some details may be invalid, depending on
 * version, and those are documented in the getter methods.
 * 
 * @author robgreene@users.sourceforge.net
 * @see http://www.nulib.com/library/FTN.e08002.htm
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
	private List<ThreadRecord> threads;
	
	/**
	 * Create the Header Block.  This is done dynamically since
	 * the Header Block size varies significantly.
	 */
	public HeaderBlock(ByteSource bs) throws IOException {
		bs.checkNuFxId();
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
			filename = new String(bs.readBytes(length));
		}
	}
	/**
	 * Read in all data threads.  All ThreadRecords are read and then
	 * each thread's data is read (per NuFX spec).
	 */
	public void readThreads(ByteSource bs) throws IOException {
		threads = new ArrayList<ThreadRecord>();
		for (long l=0; l<totalThreads; l++) threads.add(new ThreadRecord(bs));
		for (ThreadRecord r : threads) r.readThreadData(bs);
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public List<ThreadRecord> getThreadRecords() {
		return threads;
	}
	public void setThreadRecords(List<ThreadRecord> threads) {
		this.threads = threads;
	}
}
