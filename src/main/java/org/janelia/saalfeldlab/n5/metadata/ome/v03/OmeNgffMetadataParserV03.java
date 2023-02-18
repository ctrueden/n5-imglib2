package org.janelia.saalfeldlab.n5.metadata.ome.v03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5TreeNode;
import org.janelia.saalfeldlab.n5.N5URL;
import org.janelia.saalfeldlab.n5.metadata.MetadataUtils;
import org.janelia.saalfeldlab.n5.metadata.N5DatasetMetadata;
import org.janelia.saalfeldlab.n5.metadata.N5MetadataParser;
import org.janelia.saalfeldlab.n5.metadata.N5SingleScaleMetadata;

public class OmeNgffMetadataParserV03 implements N5MetadataParser< OmeNgffMetadataV03 >
{
	@Override
	public Optional< OmeNgffMetadataV03 > parseMetadata( N5Reader n5, N5TreeNode node )
	{
		OmeNgffMultiScaleMetadataV03[] multiscales;
		try
		{
			multiscales = n5.getAttribute( node.getPath(), "multiscales", OmeNgffMultiScaleMetadataV03[].class );
		} catch ( IOException e )
		{
			return Optional.empty();
		}

		if ( multiscales == null || multiscales.length == 0 )
		{
			return Optional.empty();
		}

		int nd = -1;
		final Map< String, N5TreeNode > scaleLevelNodes = new HashMap<>();
		for ( final N5TreeNode childNode : node.childrenList() )
		{
			if ( childNode.isDataset() && childNode.getMetadata() != null )
			{
				scaleLevelNodes.put( childNode.getPath(), childNode );
				if( nd < 0 )
					nd = ((N5DatasetMetadata)childNode.getMetadata()).getAttributes().getNumDimensions();
			}
		}

		if( nd < 0 )
			return Optional.empty();

		/*
		 * Need to replace all children with new children with the 
		 * metadata from this  
		 */
		for ( OmeNgffMultiScaleMetadataV03 ms : multiscales )
		{
			ms.path = node.getPath();
			String[] paths = ms.getPaths();
			DatasetAttributes[] attrs = new DatasetAttributes[ ms.getPaths().length ];
			N5DatasetMetadata[] dsetMeta = new N5DatasetMetadata[ paths.length ];
			for( int i = 0; i < paths.length; i++ )
			{
				dsetMeta[ i ] = ((N5DatasetMetadata)scaleLevelNodes.get( MetadataUtils.canonicalPath( node, paths[ i ] ) ).getMetadata());
				attrs[ i ] = dsetMeta[ i ].getAttributes();
			}

			N5SingleScaleMetadata[] msChildrenMeta = ms.buildChildren( nd, attrs );
//			N5SingleScaleMetadata[] mergedChildrenMeta = MetadataUtils.updateChildrenDatasetAttributes( msChildrenMeta, dsetMeta );
			MetadataUtils.updateChildrenMetadata( node, msChildrenMeta );
			ms.childrenMetadata = msChildrenMeta;
			

//			for( int i = 0; i < attrs.length; i++ )
//			int i = 0;
//			for ( String childPath : ms.getPaths() )
//			{
//				final String childPathCan = canonicalPath( node, childPath );
//				final N5SingleScaleMetadata meta = (N5SingleScaleMetadata) scaleLevelNodes.get( childPathCan ).getMetadata();
//				childrenMeta[ i ] = meta;
//				attrs[ i++ ] = meta.getAttributes();
//
//				// build node
//				final N5TreeNode childNode = new N5TreeNode( childPathCan );
//				childNode.setMetadata( children[ i ] );
//				node.add( childNode );;
//			}

//			for ( int i = 0; i < children.length; i++ )
//			{
//				final String childPath = children[i].getPath();
//				final String childPathCan = canonicalPath( node, childPath );
//				if( scaleLevelNodes.containsKey( childPathCan ))
//				{
//					
//				}
//			}

//			N5SingleScaleMetadata[] cs = ( N5SingleScaleMetadata[] ) Arrays.stream( childrenMeta ).filter( x -> {
//				final String childPath = x.getPath();
//				final String childPathCan = canonicalPath( node, childPath );
//				return scaleLevelNodes.containsKey( childPathCan );
//			}).toArray();
//
//			final String childPathCan = "";
//			scaleLevelNodes.get( childPathCan ).set
			

//			ms.childrenMetadata = childrenMeta;
//			ms.childrenMetadata = cs;
			ms.childrenAttributes = attrs;
		}
		return Optional.of( new OmeNgffMetadataV03( node.getPath(), multiscales ) );
	}



}
