package me.horzwxy.wordservant.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import me.horzwxy.wordservant.Recognizer;
import me.horzwxy.wordservant.WordLibrary;

public class Test {
	
	public static void main( String[] args ) {
		articleTest1();
		//buildLib();
	}
	
	private static void articleTest1() {
		WordLibrary lib = new WordLibrary( "wordList.xml" );
		Recognizer reg = new Recognizer();
		reg.setWordLibrary( lib );
		
		File inFile = new File( "in.txt" );
		File outFile = new File( "out.html" );
		
		try
		{
			FileReader fr = new FileReader( inFile );
			BufferedReader br = new BufferedReader( fr );
			FileWriter fw = new FileWriter( outFile );
			PrintWriter pw = new PrintWriter( fw );
			
			pw.write( Values.HTML_HEAD );
			
			while( br.ready() )
			{
				pw.write( Values.LABEL_P_START );
				
				String line = br.readLine();
				String[] rawWords = line.split( " " );
				for( String rawWord : rawWords )
				{
					String purifiedWord = wordPurifier( rawWord );
					reg.setCurrentWord( purifiedWord );
					if( reg.getResult() )
					{
						System.out.println( purifiedWord );
						/*
						 * Should mark the PURIFIED word red. Limited by tech, mark the RAW one.
						 */
						pw.write( " " );
						pw.write( Values.LABEL_A_START1 );
						pw.write( purifiedWord );
						pw.write( Values.LABEL_A_START2 );
						pw.write( rawWord );
						pw.write( Values.LABEL_A_END );
					}
					else
					{
						pw.write( " " + rawWord );
					}
				}
				
				pw.write( Values.LABEL_P_END );
			}
			
			pw.write( Values.HTML_TAIL );
			
			pw.close();
			fw.close();
			br.close();
			fr.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * find a word from a raw one;
	 * not consider the style of two words combination
	 * @param rawWord
	 * @return
	 */
	private static String wordPurifier( String rawWord )
	{
		String result = rawWord;
		
		int i = 0;
		// cut the head
		for( i = 0; i < rawWord.length(); i++ )
		{
			if( !Character.isLetter( rawWord.charAt( i ) ) )
			{
				continue;
			}
			else
			{
				result = rawWord.substring( i );
				break;
			}
		}
		
		if( i == rawWord.length() )	// reach end of word, meaning no letter in it
		{
			result = "";
			return result;
		}
		
		// cut the tail
		for( i = result.length() - 1; i > -1; i-- )
		{
			if( !Character.isLetter( result.charAt( i ) ) )
			{
				continue;
			}
			else
			{
				result = result.substring( 0, i + 1 );
				break;
			}
		}
		
		// omit the points
		while( result.indexOf( '.' ) != -1 )
		{
			result = result.substring( 0, result.indexOf( '.' ) ) + result.substring( result.indexOf( '.' ) + 1 );
		}
		
		return result;
	}

	/**
	 * build the xml lib
	 */
	private static void buildLib() {
		me.horzwxy.wordservant.tools.XMLWordLibBuilder.buildXMLLib();
	}
}
