package org.janelia.saalfeldlab.n5.translation;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.container.ContainerMetadataNode;
import org.janelia.saalfeldlab.n5.container.ContainerMetadataWriter;


public class TranslatedTreeMetadataWriter extends TreeTranslation{

	final N5Writer n5;
	private ContainerMetadataWriter writer;

	public TranslatedTreeMetadataWriter(
			final N5Writer n5,
			final String dataset,
			final String translation) {
		super(	ContainerMetadataNode.build(n5, dataset, JqUtils.buildGson(n5) ), 
				JqUtils.buildGson(n5), 
				translation );

		this.n5 = n5;
		this.writer = new ContainerMetadataWriter(n5, rootTranslated);
	}
	
	public TranslatedTreeMetadataWriter(
			final N5Writer n5,
			final String translation) {

		this( n5, "", translation );
	}

	public void writeAllTranslatedAttributes() throws IOException {
		writer.setMetadataTree(rootTranslated);
		writer.writeAllAttributes();
	}

	/**
	 * Writes all attributes stored in the node corresponding to the given pathName.
	 * 
	 * @param pathName the path
	 * @throws IOException the exception
	 */
	public void writeAllTranslatedAttributes(
			final String pathName ) throws IOException {

		writer.setMetadataTree(rootTranslated);
		writer.writeAllAttributes( pathName );
	}

	public void writeTranslatedAttribute(
			final String pathName,
			final String key) throws IOException {

		writer.setMetadataTree(rootTranslated);
		writer.writeAttribute( pathName, key );
	}

}
