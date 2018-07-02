package com.amarjot;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

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

    private static boolean save (String filename) throws Exception {

        // Read the file into a byte array
        byte [] data = readBytesFromFile(filename);

        // Stores hashs used to make up original file
        String skeletonfile = filename + "dup";
        createEmptyFile(skeletonfile);

        int length = data.length;
        int chunk = length / 2;

        byte [] dataSplit;

        int loopLength = length / chunk;

        int from = 0;
        int to = chunk;

        for (int x = 0; x < loopLength; x++)
        {
            String chunkName = filename + x;
            dataSplit = Arrays.copyOfRange(data, from, to);

            // Calculate Hash
            String hash = calculateHash(dataSplit);

            // IF HASH exist
            if (hashExists ("globalHashs", hash))
            {
                System.out.println("Chunk already exists.");
                // INCREMEANT HASH occurence counter IN Global hashfile
            }
            else
            {
                // OTHERWISE Write this new chunk to a file
                write(chunkName, dataSplit);

                // ADD HASH TO Global hashfile
                addToFile("globalHashs", hash);
                addToFile(skeletonfile, hash);
            }

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

    private static boolean createEmptyFile (String filename) throws IOException {

        Path path = Paths.get(filename);

        try {
            Files.createFile(path);
            return false;
        } catch (FileAlreadyExistsException e) {
            //System.err.println("already exists: " + e.getMessage());
            return true;
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


    private static String calculateHash (byte [] data) throws Exception
    {
        Checksum x = new Checksum();
        return x.getChecksum(data, "SHA1");
    }

    private static boolean hashExists (String filename, String hash)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(hash))
                {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
