/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.searchbar;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class EmployeeXMLParser {
    private static final String ns = null;


    public List<Employee> parse(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();

            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Employee> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Employee> entries = new ArrayList<Employee>();
        parser.require(XmlPullParser.START_TAG, ns, "CED_PERSON_DATA");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            
            //Starts by looking for the entry tag, which is PERSON for each employee
            //name refers to the tag name
            if (name.equals("PERSON")) {
            	//Now need to parse out the information for the current employee
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective methods for processing. Otherwise, skips the tag.
    private Employee readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "PERSON");
        String first_name=null;
        String last_name=null;
        String dept_name=null;
        String email=null;
        String extension=null;
        String mobile_num=null;
        String office_num=null;
        String username=null;
        String employee_num=null;
        String job_title=null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            //name refers to the current tag name
            String name = parser.getName();
            if (name.equals("EMPLOYEE_NUM")) {
                employee_num = readEmployeeNum(parser);
            } else if (name.equals("LAST_NAME")) {
                last_name = readLastName(parser);
            } else if (name.equals("FIRST_NAME")) {
                first_name = readFirstName(parser);
            } else if (name.equals("DEPT_NAME")) {
                dept_name = readDeptName(parser);
            } else if (name.equals("EMAIL")) {
                email = readEmail(parser);
            } else if (name.equals("EXTENSION")) {
                extension = readExtension(parser);
            } else if (name.equals("PHONE_NUM")) {
                mobile_num = readMobileNum(parser);
            } else if (name.equals("USERNAME")) {
                username = readUsername(parser);
            } else if (name.equals("JOB_TITLE")) {
                job_title = readJobTitle(parser);
            } else if (name.equals("OFFICE_NUM")) {
                office_num = readOfficeNum(parser);
            } else {
                skip(parser);
            }
        }
        return new Employee(first_name, last_name, dept_name, email,
		         extension, mobile_num, office_num, username,
		         employee_num, job_title);
    }

    private String readEmployeeNum(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "EMPLOYEE_NUM");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "EMPLOYEE_NUM");
        return title;
    }

    private String readLastName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "LAST_NAME");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "LAST_NAME");
        return summary;
    }

    private String readFirstName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "FIRST_NAME");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "FIRST_NAME");
        return summary;
    }
    
    private String readDeptName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "DEPT_NAME");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "DEPT_NAME");
        return summary;
    }
    
    private String readEmail(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "EMAIL");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "EMAIL");
        return summary;
    }
    
    private String readExtension(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "EXTENSION");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "EXTENSION");
        return summary;
    }
    
    private String readMobileNum(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "PHONE_NUM");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "PHONE_NUM");
        return summary;
    }
    
    private String readUsername(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "USERNAME");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "USERNAME");
        return summary;
    }
    
    private String readJobTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "JOB_TITLE");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "JOB_TITLE");
        return summary;
    }
    
    private String readOfficeNum(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "OFFICE_NUM");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "OFFICE_NUM");
        return summary.substring(3);
        
    }
    
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
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
