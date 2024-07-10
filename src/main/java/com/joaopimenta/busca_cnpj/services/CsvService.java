//package com.joaopimenta.busca_cnpj.services;
//
//import com.fasterxml.jackson.databind.MappingIterator;
//import com.fasterxml.jackson.dataformat.csv.CsvMapper;
//import com.fasterxml.jackson.dataformat.csv.CsvMappingException;
//import com.fasterxml.jackson.dataformat.csv.CsvSchema;
//import com.joaopimenta.busca_cnpj.exception.MapeamentEmpresasException;
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//@Service
//public class CsvService {
//
//    public List<String[]> readCsvData() throws IOException, CsvException {
//        List<String[]> csvData;
//        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
//            csvData = reader.readAll();
//        }
//        return csvData;
//    }
//
//    public List<String> getColumnHeaders() throws IOException, CsvException {
//        List<String[]> csvData = readCsvData();
//        if (!csvData.isEmpty()) {
//            // Assuming the first row contains headers
//            String[] headers = csvData.get(0);
//            List<String> headersList = new ArrayList<>();
//            for (String header : headers) {
//                headersList.add(header);
//            }
//            return headersList;
//        }
//        return null;
//    }
//
//    public List<String> getColumnData(String columnName) throws IOException, CsvException {
//        List<String[]> csvData = readCsvData();
//        List<String> columnData = new ArrayList<>();
//
//        // Find column index by header name
//        List<String> headers = getColumnHeaders();
//        int columnIndex = headers.indexOf(columnName);
//
//        if (columnIndex != -1) {
//            // Extract data from the specified column
//            for (int i = 1; i < csvData.size(); i++) {
//                String[] rowData = csvData.get(i);
//                if (rowData.length > columnIndex) {
//                    columnData.add(rowData[columnIndex]);
//                }
//            }
//        }
//
//        return columnData;
//    }
//
//    public List<byte[]> extractCsvFiles(byte[] zipBytes) {
//        List<byte[]> csvFiles = new ArrayList<>();
//
//        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".csv")) {
//                    byte[] csvBytes = readEntry(zipInputStream);
//                    csvFiles.add(csvBytes);
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to extract CSV files from zip", e);
//        }
//
//        if (csvFiles.isEmpty()) {
//            throw new IllegalStateException("No CSV files found in the ZIP archive");
//        }
//
//        return csvFiles;
//    }
//
//
//
//}
