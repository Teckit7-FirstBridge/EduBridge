package com.ll.edubridge.domain.youtube;

import com.ll.edubridge.global.exceptions.GlobalException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class PythonScriptService {

    private static void runCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command execution failed with exit code: " + exitCode);
        }
    }

    public static void installPython() throws IOException, InterruptedException {
        // Check if Python is already installed
        if (isPythonInstalled()) {
            System.out.println("Python is already installed.");
            return;
        }

        // Install Python using apt (for Ubuntu/Debian based systems)
        String[] command = {
                "sudo", "apt-get", "update"
        };
        runCommand(command);

        command = new String[] {
                "sudo", "apt-get", "install", "-y", "python3"
        };
        runCommand(command);

        if (isPythonInstalled()) {
            System.out.println("Python installation successful.");
        } else {
            System.out.println("Python installation failed.");
        }
    }


    private static boolean isPythonInstalled() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "--version");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public String runPythonScript(String videoId) throws IOException {
        // Python 실행 파일 경로 설정
        String pythonExecutable = "/usr/bin/python3"; // Unix/Linux/macOS에서는 python3을 사용합니다.

        // Python 스크립트 경로 설정
        String scriptPath = "src/main/resources/youtube_transcript2.py";


        String[] command = {
                pythonExecutable, // Python 실행 파일 경로 (시스템 환경 변수에 Python이 설정되어 있는 경우)
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