package org.janelia.saalfeldlab.n5.metadata.ome.v03;

import org.janelia.saalfeldlab.n5.metadata.N5Metadata;

public class OmeNgffMetadata implements N5Metadata
{

	public final String path;

	public final OmeNgffMultiScaleMetadata[] multiscales;

	public OmeNgffMetadata( final String path,
			final OmeNgffMultiScaleMetadata[] multiscales )
	{
		this.path = path;
		this.multiscales = multiscales;
	}

	@Override
	public String getPath()
	{
		return path;
	}

}
