package com.ll.edubridge.domain.youtube;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {

    public void runPythonScript() throws IOException {



        // Python 실행 파일 경로 설정
        String pythonExecutable = "/usr/bin/python3"; // Unix/Linux/macOS에서는 python3을 사용합니다.

        // Python 스크립트 경로 설정
        String scriptPath = "src/main/resources/youtube_transcript2.py";


        String[] command = {
                pythonExecutable, // Python 실행 파일 경로 (시스템 환경 변수에 Python이 설정되어 있는 경우)
                scriptPath
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Python script execution failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}