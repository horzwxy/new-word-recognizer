package me.horzwxy.wordservant.tools;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A tool class to transform the plain-text style word list into XML style.
 * Used only once.
 * Cannot be instantialized
 * @author horz
 *
 */
public class XMLWordLibBuilder {

	private XMLWordLibBuilder() {}
	
	public static void buildXMLLib() {
		File file = new File( "wordList.txt" );
		try {
			FileReader fr = new FileReader( file );
			BufferedReader br = new BufferedReader( fr );
			
			// set up xml doc factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.newDocument();
			
			Element root = xmlDoc.createElement( "words" );
			xmlDoc.appendChild( root );
			
			// set up xml builder
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty( "indent", "yes" );
			DOMSource source = new DOMSource();
			source.setNode( xmlDoc );
			
			while( br.ready() ) {
				String wordString = br.readLine();
				
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( wordString );
				word.appendChild( english );
				Element type = xmlDoc.createElement( "type" );
				type.setTextContent( "normal" );
				word.appendChild( type );
				Element sentences = xmlDoc.createElement( "sentences" );
				word.appendChild( sentences );
				Element in_time = xmlDoc.createElement( "in_time" );
				in_time.setTextContent( "2013-01-01" );
				word.appendChild( in_time );
				Element last_time = xmlDoc.createElement( "last_time" );
				last_time.setTextContent( "2013-01-01" );
				word.appendChild( last_time );
				Element total_times = xmlDoc.createElement( "total_times" );
				total_times.setTextContent( "0" );
				word.appendChild( total_times );
				Element peak_times = xmlDoc.createElement( "peak_times" );
				peak_times.setTextContent( "0" );
				word.appendChild( peak_times );
				Element article_frequency = xmlDoc.createElement( "article_frequency" );
				article_frequency.setTextContent( "0" );
				word.appendChild( article_frequency );
				
				root.appendChild( word );
			}
			
			StreamResult result = new StreamResult();
			result.setOutputStream( new FileOutputStream( "wordList.xml" ) );
			t.transform( source, result );
			
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
