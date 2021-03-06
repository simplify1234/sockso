
package com.pugh.sockso.music.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.metadata.Metadata;
import org.kc7bfi.jflac.metadata.VorbisComment;

import org.apache.log4j.Logger;

/**
 *  class to read tag information from flac files
 *
 */

public class FlacTag extends AudioTag {
    
    private static final Logger log = Logger.getLogger( FlacTag.class );
    
    /**
     *  reads the audio comments from a Flac file
     * 
     *  @param file
     * 
     */
    
    public void parse( final File file ) throws IOException {
        
        final FileInputStream in = new FileInputStream( file );
        final FLACDecoder dec = new FLACDecoder( in );

        Metadata[] metadata = dec.readMetadata( dec.readStreamInfo() );
        
        // look for the vorbis comment
        for ( final Metadata item : metadata ) {            
            if ( item.getClass().equals(VorbisComment.class) ) {

                final VorbisComment comment = (VorbisComment) item;

                artistTitle = getComment( comment, "ARTIST" );
                albumTitle = getComment( comment, "ALBUM" );
                trackTitle = getComment( comment, "TITLE" );
                albumYear = getComment( comment, "DATE" );
                setTrackNumber( getComment(comment,"TRACKNUMBER") );

            }
        }
        
    }

    /**
     *  tries to extract a named comment from a VorbisComment.  if the comment
     *  doesn't exist then the empty string is returned
     * 
     *  @param comment
     *  @param name
     * 
     *  @return
     */
    
    protected String getComment( final VorbisComment comment, final String name ) {

        try {

            final String[] comments = comment.getCommentByName( name );

            return ( comments.length > 0 )
                ? comments[ 0 ] : "";

        }

        catch ( final Exception e ){
            return name;
        }

    }

}
