package com.nesh.tags;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;

import java.io.File;

/**
 *
 */
public class Main {
    public static void main(String[] args) {
//        File mp3File = new File("D:\\nesh\\anshlavs_end_titles.mp3");

//        FrameBodyTXXX txxxBody = new FrameBodyTXXX();
//        txxxBody.setDescription(description);
//        txxxBody.setText(text);
//
//        // Get the tag from the audio file
//        // If there is no ID3Tag create an ID3v2.3 tag
//        Tag tag = audioFile.getTagOrCreateAndSetDefault();
//        // If there is only a ID3v1 tag, copy data into new ID3v2.3 tag
//        if(!(tag instanceof ID3v23Tag || tag instanceof ID3v24Tag)){
//            Tag newTagV23 = null;
//            if(tag instanceof ID3v1Tag){
//                newTagV23 = new ID3v23Tag((ID3v1Tag)audioFile.getTag()); // Copy old tag data
//            }
//            if(tag instanceof ID3v22Tag){
//                newTagV23 = new ID3v23Tag((ID3v11Tag)audioFile.getTag()); // Copy old tag data
//            }
//            audioFile.setTag(newTagV23);
//        }
//
//        AbstractID3v2Frame frame = null;
//        if(tag instanceof ID3v23Tag){
//            frame = new ID3v23Frame("TXXX");
//        }
//        else if(tag instanceof ID3v24Tag){
//            frame = new ID3v24Frame("TXXX");
//        }
//
//        frame.setBody(txxxBody);
//
//        try {
//            tag.addField(frame);
//        } catch (FieldDataInvalidException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        try {
//            audioFile.commit();
//        } catch (CannotWriteException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
    }
}
