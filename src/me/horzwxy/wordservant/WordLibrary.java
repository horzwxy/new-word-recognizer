package me.horzwxy.wordservant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.horzwxy.wordservant.Word.WordType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Word list that contains all words I know.
 * @author horz
 *
 */
public class WordLibrary {
	
	private HashSet< Word > wordLib;
	
	public WordLibrary( String filename ) {
		// get words from an XML file
		wordLib = new HashSet< Word >();
		
		// open file
		try {
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( new File( filename ) );
			
			// get the word node list
			NodeList list = xmlDoc.getElementsByTagName( "word" );
			for( int i = 0; i < list.getLength(); i++ ) {
				Node e = list.item( i );
				NodeList childList = e.getChildNodes();
				
				Word w = new Word( null );
				
				// initialize the words
				for( int j = 0; j < childList.getLength(); j++ ) {
					String nodeName = childList.item( j ).getNodeName();
					if( nodeName.equals( "english" ) ) {
						w.setEnglishContent( childList.item( j ).getTextContent() );	// stay in original form, not into lower case form
					}
					else if( nodeName.equals( "type" ) ) {
						String typeString = childList.item( j ).getTextContent();
						if( typeString.equals( "spot name" ) ) {
							w.setType( WordType.SPOT_NAME );
						}
						else if( typeString.equals( "person name" ) ) {
							w.setType( WordType.PERSON_NAME );
						}
						else if( typeString.equals( "normal" ) ) {
							w.setType( WordType.NORMAL );
						}
					}
					else if( nodeName.equals( "sentences" ) ) {
						NodeList sNodeList = childList.item( j ).getChildNodes();	// children of <sentences>
						for( int k = 0; k < sNodeList.getLength(); k++ ) {
							String s = sNodeList.item( k ).getTextContent();
							w.addSentence( s );
						}
					}
					else if( nodeName.equals( "in_time" ) ) {
						w.setIn_time( childList.item( j ).getTextContent() );
					}
					else if( nodeName.equals( "last_time" ) ) {
						w.setLast_time( childList.item( j ).getTextContent() );
					}
					else if( nodeName.equals( "total_times" ) ) {
						w.setTotal_times( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "peak_times" ) ) {
						w.setPeak_times( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "article_frequency" ) ) {
						w.setArticle_frequency( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "#text" ) ) {
						continue;
					}
					else {
						throw new RuntimeException( "XML parsing error: unknown element " + nodeName );
					}
				}
				
				wordLib.add( w );
			}
		} catch (FileNotFoundException e) {
			System.out.println( "Word list file not found, with name " + filename );
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WordLibrary( HashSet< Word > wordLib ) {
		this.wordLib = wordLib;
	}
	
	/**
	 * Check whether a word is on the list.
	 * @param word
	 * @return
	 */
	public boolean contains( String word ) {
		return wordLib.contains( new Word( word ) );
	}
	
	/**
	 * Save the word instances into a XML file.
	 */
	public void saveIntoXMLFile( String filename ) {
		try {
			// set up xml doc factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.newDocument();

			// set up xml builder
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty( "indent", "yes" );
			DOMSource source = new DOMSource();
			source.setNode( xmlDoc );
			
			// Transform the word list into a ordered set. Then the output list is ordered.
			TreeSet< Word > ts = new TreeSet< Word >( wordLib );
			
			for( Word w : ts ) {
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( w.getEnglishContent() );
				word.appendChild( english );
				Element type = xmlDoc.createElement( "type" );
				if( w.getType() == Word.WordType.NORMAL ) {
					type.setTextContent( "normal" );
				}
				else if( w.getType() == Word.WordType.PERSON_NAME ) {
					type.setTextContent( "person name" );
				}
				else if( w.getType() == Word.WordType.SPOT_NAME ) {
					type.setTextContent( "spot name" );
				}
				else {
					System.out.println( "Unknown word type:" + w.getType().toString() );
					System.exit( 0 );
				}
				word.appendChild( type );
				Element sentences = xmlDoc.createElement( "sentences" );
				for( int i = 0; i < w.getSentences().size(); i++ ) {
					Element s = xmlDoc.createElement( "sentence" );
					s.setTextContent( w.getSentences().get( i ) );
					sentences.appendChild( s );
				}
				word.appendChild( sentences );
				Element in_time = xmlDoc.createElement( "in_time" );
				in_time.setTextContent( w.getIn_time() );
				word.appendChild( in_time );
				Element last_time = xmlDoc.createElement( "last_time" );
				last_time.setTextContent( w.getLast_time() );
				word.appendChild( last_time );
				Element total_times = xmlDoc.createElement( "total_times" );
				total_times.setTextContent( w.getTotal_times() + "" );
				word.appendChild( total_times );
				Element peak_times = xmlDoc.createElement( "peak_times" );
				peak_times.setTextContent( w.getPeak_times() + "" );
				word.appendChild( peak_times );
				Element article_frequency = xmlDoc.createElement( "article_frequency" );
				article_frequency.setTextContent( w.getArticle_frequency() + "" );
				word.appendChild( article_frequency );
				
				xmlDoc.appendChild( word );
			}
			
			StreamResult result = new StreamResult();
			result.setOutputStream( new FileOutputStream( filename ) );
			t.transform( source, result );
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}