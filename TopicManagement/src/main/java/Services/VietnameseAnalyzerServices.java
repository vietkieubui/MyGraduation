/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.vi.VietnameseAnalyzer;

/**
 *
 * @author BVKieu
 */
public final class VietnameseAnalyzerServices {
    public static String[] textToStrings(String text) throws IOException {
        VietnameseAnalyzer va  = new VietnameseAnalyzer();
        TokenStream ts = va.tokenStream("VN_Analyzer_arraylist", text);
        CharTermAttribute termAttribute = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        ArrayList<String> arrString = new ArrayList<String>();
        while (ts.incrementToken()) {
            arrString.add(termAttribute.toString());
        }
        ts.end();
        ts.close();
        String[] strings = arrString.toArray(new String[arrString.size()]);
        return strings;
    }
}
