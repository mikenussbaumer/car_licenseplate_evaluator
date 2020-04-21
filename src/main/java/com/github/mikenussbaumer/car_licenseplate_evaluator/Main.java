package com.github.mikenussbaumer.car_licenseplate_evaluator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mike Nußbaumer (dev@mike-nussbaumer.com)
 * @date 20.04.20
 * @time 18:39
 * @project car_licenseplate_evaluator
 * <p>
 * Example: KV-231W
 * <p>
 * Definition of the license plate:
 * <p>
 * - before and after the license plate is a space
 * - min 1 char but max 2 chars before the dash no numbers
 * - 1 dash
 * - alphanumeric only uppercase not more than 6 chars
 */
public class Main {

    public static void main ( String[] args ) {
        // print usage if the user does not properly use the software
        if ( args.length != 1 ) {
            System.out.println( "Welcome to the LicenseTemplateReader of Mike Nussbaumer." );
            System.out.println( "Please give me as first argument the filename which should be processed." );
            System.out.println( "USAGE: java -jar car_licenseplate_evaluator.jar <fileName>" );

            System.exit( 0 );
        }

        // get filename from jar arguments
        String fileName = args[ 0 ];
        String contents = null;

        try {
            // read file data as string
            contents = new String( Files.readAllBytes( Paths.get( fileName ) ) );
            List < String > licensePlates = getAllLicensePlatesFromText( contents );

            // print out license plates
            for ( int i = 0; i < licensePlates.size( ); i++ )
                System.out.println( i + 1 + ": " + licensePlates.get( i ) );

            System.out.println( "------------------------------------------" );

            TreeMap < String, Integer > licensePlatesMappedToLocation = getLicensePlatesMappedToLocation( licensePlates );

            // print out the TreeMap which is naturally sorted
            for ( Map.Entry < String, Integer > entry : licensePlatesMappedToLocation.entrySet( ) )
                System.out.println( entry.getKey( ) + ": " + entry.getValue( ) );

        } catch ( IOException e ) {
            System.out.println( "File " + fileName + " not found or the file is not readable." );
        }

    }

    /**
     * Extracts the license plates from text
     *
     * @param text - input text to extract license plates
     * @return sorted list of license plates without duplicates
     */
    private static List < String > getAllLicensePlatesFromText ( String text ) {

        //regular expression to filter out license plates
        Pattern pattern = Pattern.compile( "\\s[A-Z]{1,2}-[A-Z0-9]{1,6}\\s" );
        Matcher matcher = pattern.matcher( text );

        Set < String > licensePlates = new HashSet <>( );

        // find all license plates from the file and add it to a set and moreover to remove duplicates
        while ( matcher.find( ) ) {
            licensePlates.add( matcher.group( ).trim( ) );
        }

        // create list to sort the license plates
        List < String > licensePlateList = new ArrayList <>( licensePlates );
        Collections.sort( licensePlateList );

        return licensePlateList;
    }

    /**
     * Maps a list of license plates to their specific location
     *
     * @param licensePlates - list with license plates
     * @return treeMap with all license plates
     */
    private static TreeMap < String, Integer > getLicensePlatesMappedToLocation ( List < String > licensePlates ) {
        HashMap < String, Integer > locationLicensePlates = new HashMap <>( );

        for ( String licensePlate : licensePlates ) {
            String location = licensePlate.split( "-" )[ 0 ];

            if ( locationLicensePlates.containsKey( location ) ) {
                locationLicensePlates.put( location, locationLicensePlates.get( location ) + 1 );
            } else {
                locationLicensePlates.put( location, 1 );
            }
        }

        // sort
        return new TreeMap <>( locationLicensePlates );
    }
}
