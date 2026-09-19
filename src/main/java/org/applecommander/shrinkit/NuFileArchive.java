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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.applecommander.shrinkit.io.LittleEndianByteInputStream;

/**
 * Basic reading of a NuFX archive.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NuFileArchive {
	public static final String VERSION;
	
	static {
		VERSION = NuFileArchive.class.getPackage().getImplementationVersion();
	}	
	
	private final MasterHeaderBlock master;
	private final List<HeaderBlock> headers;
	private long totalSize = 0;

	/**
	 * Need to enumerate some basic subtypes of archives.
	 */
	public static final int NUFILE_ARCHIVE = 1;
	public static final int NUFX_ARCHIVE = 2;
	public static final int BXY_ARCHIVE = 3;

	/**
	 * Read in the NuFile/NuFX/Shrinkit archive.
	 */
	public NuFileArchive(InputStream inputStream) throws IOException {
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(inputStream);
		master = new MasterHeaderBlock(bs);
		headers = new ArrayList<>();
		for (int i=0; i<master.getTotalRecords(); i++) {
			HeaderBlock header = new HeaderBlock(bs);
			header.readThreads(bs);
			headers.add(header);
			totalSize += header.getHeaderSize();
		}
	}

	/**
	 * @return long size in bytes of the archive
	 */
	public long getArchiveSize() {
		return totalSize;
	}

	public MasterHeaderBlock getMasterHeaderBlock() {
		return master;
	}
	public List<HeaderBlock> getHeaderBlocks() {
		return headers;
	}}
