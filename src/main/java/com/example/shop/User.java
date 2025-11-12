package com.example.shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    private String login;
    private String password;

    public User(String login, String password){
        setLogin(login);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean Compare(){
        String filePath = "users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String fileLogin = parts[0].trim();
                    String filePassword = parts[1].trim();

                    if (fileLogin.equals(login) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return false;
    }

    public Boolean IsRegistered() {
        String filePath = "users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String fileLogin = parts[0].trim();

                    if (fileLogin.equals(login)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return false;
    }

    public void Add() {
        String filePath = "users.txt";

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write( login + ";" + password + "\n" );
        } catch (IOException e) {
            System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
        }
    }
}
