/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.scijava.plugin.Plugin;

/**
 * {@link DataHandle} for a {@link FileLocation}.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = DataHandle.class)
public class FileHandle extends AbstractDataHandle<FileLocation> {

	// -- Fields --

	/** The {@link RandomAccessFile} backing this file handle. */
	private RandomAccessFile raf;

	/** The mode of the {@link RandomAccessFile}. */
	private String mode = "rw";

	// -- FileHandle methods --

	/** Gets the random access file object backing this FileHandle. */
	public RandomAccessFile getRandomAccessFile() throws IOException {
		return raf();
	}

	public String getMode() {
		return mode;
	}

	public void setMode(final String mode) {
		if (raf != null) {
			throw new IllegalStateException("File already initialized");
		}
		this.mode = mode;
	}

	// -- DataHandle methods --

	@Override
	public long offset() throws IOException {
		return raf().getFilePointer();
	}

	@Override
	public long length() throws IOException {
		return raf().length();
	}

	@Override
	public int read() throws IOException {
		return raf().read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return raf().read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
		throws IOException
	{
		return raf().read(b, off, len);
	}

	@Override
	public void seek(final long pos) throws IOException {
		raf().seek(pos);
	}

	// -- DataInput methods --

	@Override
	public boolean readBoolean() throws IOException {
		return raf().readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return raf().readByte();
	}

	@Override
	public char readChar() throws IOException {
		return raf().readChar();
	}

	@Override
	public double readDouble() throws IOException {
		return raf().readDouble();
	}

	@Override
	public float readFloat() throws IOException {
		return raf().readFloat();
	}

	@Override
	public void readFully(final byte[] b) throws IOException {
		raf().readFully(b);
	}

	@Override
	public void readFully(final byte[] b, final int off, final int len)
		throws IOException
	{
		raf().readFully(b, off, len);
	}

	@Override
	public int readInt() throws IOException {
		return raf().readInt();
	}

	@Override
	public String readLine() throws IOException {
		return raf().readLine();
	}

	@Override
	public long readLong() throws IOException {
		return raf().readLong();
	}

	@Override
	public short readShort() throws IOException {
		return raf().readShort();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return raf().readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return raf().readUnsignedShort();
	}

	@Override
	public String readUTF() throws IOException {
		return raf().readUTF();
	}

	@Override
	public int skipBytes(final int n) throws IOException {
		return raf().skipBytes(n);
	}

	// -- DataOutput methods --

	@Override
	public void write(final byte[] b) throws IOException {
		raf().write(b);
	}

	@Override
	public void write(final byte[] b, final int off, final int len)
		throws IOException
	{
		raf().write(b, off, len);
	}

	@Override
	public void write(final int b) throws IOException {
		raf().write(b);
	}

	@Override
	public void writeBoolean(final boolean v) throws IOException {
		raf().writeBoolean(v);
	}

	@Override
	public void writeByte(final int v) throws IOException {
		raf().writeByte(v);
	}

	@Override
	public void writeBytes(final String s) throws IOException {
		raf().writeBytes(s);
	}

	@Override
	public void writeChar(final int v) throws IOException {
		raf().writeChar(v);
	}

	@Override
	public void writeChars(final String s) throws IOException {
		raf().writeChars(s);
	}

	@Override
	public void writeDouble(final double v) throws IOException {
		raf().writeDouble(v);
	}

	@Override
	public void writeFloat(final float v) throws IOException {
		raf().writeFloat(v);
	}

	@Override
	public void writeInt(final int v) throws IOException {
		raf().writeInt(v);
	}

	@Override
	public void writeLong(final long v) throws IOException {
		raf().writeLong(v);
	}

	@Override
	public void writeShort(final int v) throws IOException {
		raf().writeShort(v);
	}

	@Override
	public void writeUTF(final String str) throws IOException {
		raf().writeUTF(str);
	}

	// -- Closeable methods --

	@Override
	public void close() throws IOException {
		raf().close();
	}

	// -- Typed methods --

	@Override
	public Class<FileLocation> getType() {
		return FileLocation.class;
	}

	// -- Helper methods --

	private RandomAccessFile raf() throws IOException {
		if (raf == null) initRAF();
		return raf;
	}

	private synchronized void initRAF() throws IOException {
		raf = new RandomAccessFile(get().getFile(), getMode());
	}

}
