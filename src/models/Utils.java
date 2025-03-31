package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Utils {
    public static Map<String, String> convertArgsToMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        if (args.length > 0) {
            if (args.length != 3) {
                throw new IllegalArgumentException("Wrong number of arguments - " +
                        " args[0] must be board position1 file name, args[1] should be board position2 file name and args[2] should be depth of tree");
            }

            map.put("inputDir", args[0]);
            map.put("outputDir", args[1]);
            map.put("depth", args[2]);
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter input (e.g., MiniMax board1.txt board2.txt 2):");
            String input = scanner.nextLine();
            String[] tokens = input.split(" ");

            if (tokens.length == 3) {
                map.put("inputDir", tokens[0]);
                map.put("outputDir", tokens[1]);
                map.put("depth", tokens[2]);
            } else {
                throw new IllegalArgumentException("Illegal Inputs given");
            }

            scanner.close();
        }
        return map;
    }

    public static void output(Result result, String outputBoardName, ProblemType problemType) throws IOException {
        System.out.println("Output board position: " + result.getBestBoard().toString());
        System.out.println("Positions evaluated by static estimation: " + result.getPositionsEvaluated());
        System.out.println(problemType.getName() + " estimate: " + result.getEstimate());
        System.out.println();
        Files.write(Paths.get(outputBoardName), result.getBestBoard().toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String getInputStringFromFile(String fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
        String inputOneString = new String(bytes, StandardCharsets.UTF_8);
        validateInputString(inputOneString);
        return inputOneString;
    }

    private static void validateInputString(String inputOneString) {
        if (inputOneString.length() != 16) {
            throw new IllegalArgumentException("Wrong number of arguments - input strings must be of size 16");
        }
        if (countPiece(inputOneString, Piece.WHITE_KING) > 1) {
            throw new IllegalArgumentException("Input string cannot contain more than one white king");
        }
        if (countPiece(inputOneString, Piece.BLACK_KING) > 1) {
            throw new IllegalArgumentException("Input string cannot contain more than one black king");
        }
        if (countPiece(inputOneString, Piece.WHITE_PAWN) > 3) {
            throw new IllegalArgumentException("Input string cannot contain more than three white pawn");
        }
        if (countPiece(inputOneString, Piece.BLACK_PAWN) > 3) {
            throw new IllegalArgumentException("Input string cannot contain more than three black pawn");
        }
    }

    private static int countPiece(String inputOneString, Piece piece) {
        int count = 0;
        for (char c : inputOneString.toCharArray()) {
            if (c == piece.getPiece())
                count++;
        }
        return count;
    }
}