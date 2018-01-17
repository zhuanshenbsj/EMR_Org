package com.cloud.emr.util;

import java.io.IOException;
import java.io.InputStream;

public class NullInputStream extends InputStream{
	public int read() throws IOException {
		return -1;
	}
}
