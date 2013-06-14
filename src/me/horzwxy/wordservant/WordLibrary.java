package me.horzwxy.wordservant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class WordLibrary {
	
	private HashSet< String > wordLib;
	
	public WordLibrary( InitializationType type, String filename ) {
		// get words from a TXT file
		if( type.equals( InitializationType.PLAIN_TEXT ) ) {
			File dbFile = new File( filename );
			wordLib = new HashSet< String >();
			
			// open file
			try {
				FileReader fr = new FileReader( dbFile );
				BufferedReader br = new BufferedReader( fr );
				
				// load the words
				while( br.ready() ) {
					String word = br.readLine();
					wordLib.add( word.toLowerCase() );
				}
				
				br.close();
				fr.close();
			} catch (FileNotFoundException e) {
				System.out.println( "Word lisr file not found, with name " + filename );
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean contains( String word ) {
		return wordLib.contains( word );
	}
	
	public enum InitializationType {
		PLAIN_TEXT,
		XML;
	}
}