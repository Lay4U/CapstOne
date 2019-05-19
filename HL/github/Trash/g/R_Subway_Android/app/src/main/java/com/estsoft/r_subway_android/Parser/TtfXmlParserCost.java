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
public class TtfXmlParserCost {

    private static final String TAG = "TtfXmlParserCost";
    private static final String DEBUG = "DEBUG";

    private static final String namespace = null;
    private InputStream inputStream = null;

    private List<GroupTag> groupTags = null;

    private List<StationTag> stationTags = null;


    public TtfXmlParserCost(InputStream in) throws XmlPullParserException, IOException {
        this.inputStream = in;
        groupTags = new ArrayList<>();
        this.parse();
    }

    public List<StationTag> getStationTags(){
        if ( stationTags == null ) {
            stationTags = new ArrayList<>();
            for (GroupTag group : groupTags) {
                stationTags.addAll(group.getStationTags());
            }
        }

        return stationTags;
    }

    public List<StationTag> getStationTags( int laneNumber ) {
        for ( GroupTag group : groupTags ) {
            if ( group.getLaneNumber() == laneNumber ) {
                return group.getStationTags();
            }
        }
        return null;
    }

    private void parse() throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            readTtf(parser);
//            readSVG(parser);

        } finally {
            inputStream.close();
            //inputSteam = null; ??
        }
    }

    private void readTtf(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require( XmlPullParser.START_TAG, namespace, "ttf" );
        if (parser.getName().equals("ttf")) {

        }
        while (parser.next() != XmlPullParser.END_TAG ) {
            if (parser.getEventType() != XmlPullParser.START_TAG ) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("group")) {
                readGroupTag(parser);
            } else if (false) {

            } else {
                skip(parser);
            }
        }

    }

    private void readGroupTag ( XmlPullParser parser )  throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, namespace, "group");
        String laneName = parser.getAttributeValue(namespace, "name");
        String number = parser.getAttributeValue(namespace, "number");
        GroupTag groupTag = new GroupTag( Integer.parseInt(number), laneName );
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("station")) {
                Object[] arr = readStationTag( parser ) ;
                groupTag.addStationTag((String)arr[0], (String)arr[1], (List)arr[2], (List)arr[3], (List)arr[4] );
            } else {
                skip(parser);
            }
        }
        groupTags.add(groupTag);
    }

    private Object[] readStationTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        Object[] objs = new Object[ 5 ];
        parser.require(XmlPullParser.START_TAG, namespace, "station");
        String tag = parser.getName();
        if (tag.equals("station")) {
            objs[0] = parser.getAttributeValue(namespace, "name"); //지하철 이름
            objs[1] = parser.getAttributeValue(namespace, "id"); // 지하철 아이디

            List<String> nextTmp  = new ArrayList<>();
            for ( int i =0; i < 2; i ++ ) {
                String read = parser.getAttributeValue(namespace, "next_cost0" + (i + 1));
                if (!(read.equals(""))) {
                    nextTmp.add( read );
                }
            }
            objs[2] = nextTmp;

            List<String> prevTmp  = new ArrayList<>();
            for ( int i =0; i < 2; i ++ ) {
                String read = parser.getAttributeValue(namespace, "prev_cost0" + (i + 1));
                if (!(read.equals(""))) {
                    prevTmp.add( read );
                }
            }
            objs[3] = prevTmp;

            List<String> exTmp  = new ArrayList<>();
            String read = parser.getAttributeValue(namespace, "ex_cost");
            if (!(read.equals(""))) {
                exTmp.add( read );
            }
            objs[4] = exTmp;

            parser.nextTag();

        }
        parser.require(XmlPullParser.END_TAG, namespace, "station");
        return objs;

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
