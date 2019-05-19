package com.estsoft.r_subway_android.Parser;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-22.
 */
public class TtfXmlParser {

    private static final String TAG = "TtfXmlParser";
    private static final String DEBUG = "DEBUG";

    private static final String namespace = null;
    private InputStream inputStream = null;

    private int svgWidth = 0;
    private int svgHeight = 0;

    private int circleFactorCount = 5;

    private List<GTag> gList = new ArrayList<>();


    public List<CircleTag> getCircleList(int laneNumber) {
        List<CircleTag> lane = new ArrayList<>();
        for (CircleTag circle : gList.get(laneNumber).getCircleList()) {
            lane.add(circle);
        }
        return lane;
    }

    public List<CircleTag> getCircleList() {

        List<CircleTag> circleList = new ArrayList<>();
        for (GTag g : gList) {
            for (CircleTag circle : g.getCircleList()) {
                circleList.add(circle);
            }
        }
        return circleList;

    }

    public int getSvgWidth() {
        return svgWidth;
    }

    public int getSvgHeight() {
        return svgHeight;
    }

    public TtfXmlParser(InputStream in) throws XmlPullParserException, IOException {
        this.inputStream = in;
        this.parse();
    }

    private void parse() throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            readSVG(parser);

        } finally {
            inputStream.close();
            //inputSteam = null; ??
        }
    }

    private void readSVG(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, "svg");
        if (parser.getName().equals("svg")) {
            String data = parser.getAttributeValue(namespace, "width");
            svgWidth = Integer.parseInt(data.replace("px", ""));
            data = parser.getAttributeValue(namespace, "height");
            svgHeight = Integer.parseInt(data.replace("px", ""));
//            svgWidth = Integer.parseInt(parser.getAttributeValue(namespace, "width"));
//            svgHeight = Integer.parseInt(parser.getAttributeValue(namespace, "height"));
        }
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("g")) {
                readGTag(parser);
            } else if (false) {

            } else {
                skip(parser);
            }

        }

    }

    private void readGTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, "g");
        String fill = parser.getAttributeValue(namespace, "fill");
        int laneNumber = Integer.parseInt(parser.getAttributeValue(namespace, "number"));
        GTag gTag = new GTag(fill, laneNumber);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("circle")) {
                String[] circleFactorArr = readCircle(parser);
                gTag.addCircle(circleFactorArr);
            } else {
                skip(parser);
            }
        }
        gList.add(gTag);
    }

    private String[] readCircle(XmlPullParser parser) throws XmlPullParserException, IOException {
        String[] circleFactors = new String[circleFactorCount];
        //circleFactors 에 추가될 수 있다.
        parser.require(XmlPullParser.START_TAG, namespace, "circle");
        String tag = parser.getName();
        if (tag.equals("circle")) {
            circleFactors[0] = parser.getAttributeValue(namespace, "cx");
            circleFactors[1] = parser.getAttributeValue(namespace, "cy");
            circleFactors[2] = parser.getAttributeValue(namespace, "r");
            circleFactors[3] = parser.getAttributeValue(namespace, "id");
            circleFactors[4] = parser.getAttributeValue(namespace, "name");
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, namespace, "circle");
        return circleFactors;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        String tagName = parser.getName();
//        Log.d("SKIP_TAG", tagName);
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
