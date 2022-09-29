package org.janelia.saalfeldlab.n5.metadata.canonical;

import org.janelia.saalfeldlab.n5.metadata.N5MetadataGroup;

import java.util.Arrays;

/**
 * @author Caleb Hulbert
 * @author John Bogovic
 */
public class MultiChannelMetadataCanonical implements N5MetadataGroup<CanonicalMetadata> {

	private String path;

	private CanonicalMetadata[] datasets;

	public MultiChannelMetadataCanonical( String path, CanonicalMetadata[] datasets )
	{
		this.path = path;
		this.datasets = datasets;
	}

	@Override public String[] getPaths() {

		// children store relative paths to parent
		return Arrays.stream(datasets).map(x -> path + "/" + x.getPath()).toArray(String[]::new);
	}

	@Override public CanonicalMetadata[] getChildrenMetadata() {

		return datasets;
	}

	@Override public String getPath() {

		return path;
	}

}
