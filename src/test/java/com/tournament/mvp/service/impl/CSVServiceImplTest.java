package com.tournament.mvp.service.impl;

import com.opencsv.exceptions.CsvValidationException;
import com.tournament.mvp.exception.CsvException;
import com.tournament.mvp.exception.CustomFileNotFoundException;
import com.tournament.mvp.exception.SamePlayerException;
import com.tournament.mvp.model.Match;
import com.tournament.mvp.service.CSVService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVServiceImplTest {

    private static CSVService csvService;

    private static String FILE_PATH = "../mvp/src/test/resources/";
    // Basketball games
    private static final String FILE1 = "file1.csv";
    private static final String FILE2 = "file2.csv";
    private static final String FILE3 = "file3.csv";
    private static final String FILE4 = "file4.csv";
    private static final String FILE5 = "file5.csv";
    private static final String FILE6 = "file6.csv";
    private static final String FILE7 = "file7.csv";
    private static final String FILE8 = "file8.csv";
    private static final String FILE999 = "file999.csv";
    private static final String FILE_NOT_VALID = "file12.csv";

    private static List<String> basketball;
    private static List<String> handball;
    private static List<String> noUniqueNickname;
    private static List<String> fileNotExists;
    private static List<String> notValid;

    @BeforeAll
    public static void init() {
        csvService = new CSVServiceImpl();
        basketball = List.of(FILE_PATH + FILE1, FILE_PATH + FILE2, FILE_PATH + FILE3);
        handball = List.of(FILE_PATH + FILE4, FILE_PATH + FILE5, FILE_PATH + FILE6);
        noUniqueNickname = List.of(FILE_PATH + FILE7, FILE_PATH + FILE8);
        fileNotExists = List.of(FILE_PATH + FILE999);
        notValid = List.of(FILE_PATH + FILE_NOT_VALID);
    }

    @Test
    void readAllHandballCSVsTest() {
        List<Match> matches = csvService.readCSVs(handball);
        assertEquals(3, matches.size());
    }

    @Test
    void readAllBasketballCSVsTest() {
        List<Match> matches = csvService.readCSVs(basketball);
        assertEquals(3, matches.size());
    }

    @Test
    void uniqueNicknameTest() {
        assertThrows(SamePlayerException.class, () -> {
            csvService.readCSVs(noUniqueNickname);
        }, "The game duplicates the player");
    }

    @Test
    void fileNotExistsTest() {
        CustomFileNotFoundException customFileNotFoundException = assertThrows(CustomFileNotFoundException.class, () -> {
            csvService.readCSVs(fileNotExists);
        }, "Invalid file paths: " + FILE_PATH + FILE999);
        assertEquals("Invalid file paths: " + FILE_PATH + FILE999, customFileNotFoundException.getMessage());
    }

    @Test
    void csvValidationException() {
        CsvException invalidCsv = assertThrows(CsvException.class, () -> {
            csvService.readCSVs(notValid);
        }, "Invalid CSV");
        assertEquals("Invalid CSV", invalidCsv.getMessage());
    }

}