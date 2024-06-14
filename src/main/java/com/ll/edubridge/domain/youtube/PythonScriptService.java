package com.ll.edubridge.domain.youtube;

import com.ll.edubridge.global.exceptions.GlobalException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {

    public String runPythonScript(String videoId) throws IOException {
        // Python 실행 파일 경로 설정
        String pythonExecutable = "/usr/bin/python3";

        // Python 스크립트 경로 설정
        String scriptPath = "/app/youtube_transcript2.py";


        String[] command = {
                pythonExecutable, // Python 실행 파일 경로
                scriptPath,
                videoId
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output= new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Python script execution failed with exit code: " + exitCode);
                throw new GlobalException("Python script execution failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}