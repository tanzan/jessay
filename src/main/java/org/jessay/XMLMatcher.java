package org.jessay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author serg
 */
public class XMLMatcher extends BaseMatcher {

    private static final int BANNER_SIZE=64;
    private XML xml;

    public XMLMatcher(XML xml) {
        if (xml == null) {
            throw new IllegalArgumentException();
        }
        this.xml = xml;
    }

    private String fillString(char c, int n){
        char[] buff = new char[n];
        Arrays.fill(buff, c);
        return new String(buff);
    }

    private void banner(String message){
        int left, right;
        left = right = (BANNER_SIZE - message.length())/2;
        if (left * 2  + message.length() < BANNER_SIZE){
            right +=2;
        }
        System.out.println(fillString('-',left) + message + fillString('-',right));
    }



    @Override
    public boolean matches(Object item) {
        if (item == null) {
            throw new NullPointerException();
        }

        XML xmlItem = null;

        if (item instanceof XML){
            xmlItem = (XML) item;
        }
        if (item instanceof String){
            xmlItem = xml((String)item);
        }
        if (item instanceof InputStream){
            xmlItem = xml((InputStream)item);
        }

        if (xmlItem == null){
            throw new IllegalArgumentException("XMLMatcher accepts only XMLMatcher.XML, String and InputStream");
        }

        boolean result = xml.equals(xmlItem);

        if (!result) {
            banner("Expected XML");
            xml.print(System.out, Charset.defaultCharset());          
            banner("Actual XML");            
            xmlItem.print(System.out, Charset.defaultCharset());
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(xml.toString());
    }

    public static Matcher<XMLMatcher.XML> matchXML(XMLMatcher.XML xml) {
        return new XMLMatcher(xml);
    }

    public static Matcher<String> matchXMLString(XMLMatcher.XML xml) {
        return new XMLMatcher(xml);
    }

    public static Matcher<InputStream> matchXMLSream(XMLMatcher.XML xml) {
        return new XMLMatcher(xml);
    }

    public static XML xml(String value) {
        return new XML(value);
    }

    public static XML xml(InputStream is){
        StringBuffer xmlBuff = new StringBuffer();
        byte[] buff = new byte[1024];
        int size;
        for(;;){
            try {
                size = is.read(buff);
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
            if (size == -1){
                break;
            }
            try {
                xmlBuff.append(new String(buff,0,size,"UTF-8"));
            } catch(UnsupportedEncodingException ex){
                throw new IllegalStateException(ex);
            }
        }
        return new XML(xmlBuff.toString());
    }

    private static String normalize(String item){
        return item.replaceAll(">\\s+<", "><").trim();
    }
    public static class XML {

        private String xml;
        private DocumentBuilder builder;
        private Document doc;

        XML(String xml) {
            if (xml == null) {
                throw new IllegalArgumentException();
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                new IllegalStateException(ex);
            }
            this.xml = xml;
            doc = createDocument();
        }

        Document createDocument() {
            try {                
                InputSource source = new InputSource(new ByteArrayInputStream(normalize(xml).getBytes("UTF-8")));
                source.setEncoding("UTF-8");                
                return builder.parse(source);
            } catch (SAXException ex) {
                throw new IllegalArgumentException(ex);
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        Document getDocument(){
            return doc;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof XML)) {
                return false;
            }
            XML other = (XML) obj;
            Document thisDoc = (Document) getDocument().cloneNode(true);
            removeComments(thisDoc);
            Document otherDoc = (Document) other.getDocument().cloneNode(true);
            removeComments(otherDoc);            
            return thisDoc.isEqualNode(otherDoc);
        }

        private void removeComments(Node node){
            NodeList nodes = node.getChildNodes();
            for(int i = 0; i < nodes.getLength(); ++i ){
                Node child = nodes.item(i);
                removeComments(child);
                if (child instanceof Comment){
                    node.removeChild(child);
                }
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + (this.xml != null ? this.xml.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return xml;
        }

        void print(OutputStream os, Charset charset) {

            try {

                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl =(DOMImplementationLS) registry.getDOMImplementation("LS");
                LSSerializer writer = impl.createLSSerializer();
                //writer.getDomConfig().setParameter("format-pretty-print", true);
                LSOutput output = impl.createLSOutput();
                output.setByteStream(os);
                output.setEncoding(charset.name());
                writer.write(getDocument(), output);


            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            } catch (InstantiationException ex) {
                throw new IllegalStateException(ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            } catch (ClassCastException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}
