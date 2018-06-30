package com.amarjot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

public class Main {

    private static String globalHashFileName = "globalHashFile";

    public static void main(String[] args) throws IOException {

        String save = "save";
        String load = "load";

        // Save
        if (args[0].equals(save))
        {
            save(args[1]);
        }
        // Load
        else if (args[0].equals(load))
        {
            load(args[1]);
        }
    }

    private static boolean save (String filename) throws IOException {

        // Read the file into a byte array
        byte [] data = readBytesFromFile(filename);

        int length = data.length;
        int chunk = length / 2;

        byte [] dataSplit;

        int loopLength = length / chunk;

        int from = 0;
        int to = chunk;

        for (int x = 0; x < loopLength; x++)
        {
            String chunkName = x + "";
            dataSplit = Arrays.copyOfRange(data, from, to);

            // Calculate Hash
            String hash = calculateHash(dataSplit);

            // IF HASH exist
            if (hashExists (dataSplit, hash))
            {
                // INCREMEANT HASH occurence counter IN Global hashfile
            }
            else
            {
                // OTHERWISE
                // Write this new chunk to a file
                write(chunkName, dataSplit);

                // ADD HASH TO Global hashfile
            }

            // ADD HASH TO skeleton file
            String skeletonfile = filename + "dup";
            createEmptyFile(skeletonfile);
            addToFile(skeletonfile, hash);


            from = to;
            to = from + chunk;
        }

        if ((length % 2) == 1)
        {
            System.out.println(length);
            System.out.println("Adding last bit");
            dataSplit = Arrays.copyOfRange(data, (length - 1), length - 1);
            write("10", dataSplit);
        }

        return false;
    }


    private static boolean load (String filename)
    {

        return false;
    }

    private static void write (String filename, byte [] data) throws IOException {
        FileOutputStream stream = new FileOutputStream(filename);
        try
        {
            stream.write(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            stream.close();
        }
    }

    private static void createEmptyFile (String filename) throws IOException {

        Path path = Paths.get(filename);

        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }

    private static void appendToGlobalHash (String filename, byte [] data) throws IOException {

    }

    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(filePath);
            fileInputStream.read(bytesArray);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;
    }


    private static String calculateHash (byte [] data)
    {
        return "hash";
    }

    private static boolean hashExists (byte[] filename, String hash)
    {
        return false;
    }

    // When file uploaded, makes entry in dataMap.
    private static void addToFile(String filename, String data)
    {
        String entry = data + '\n';

        try {
            Files.write(Paths.get(filename), entry.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
