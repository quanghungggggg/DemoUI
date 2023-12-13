package com.example.demoui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

public class Main extends Application {
    private TextField inputField;
    private Label resultLabel;
    private Label SendToServerBT;
    private Label FromServerBT;
    private Label comparisonLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Socket Client");

        // Tạo các thành phần giao diện
        Label inputLabel = new Label("Nhập từ client:");
        inputField = new TextField();
        Button sendButton = new Button("Gửi");
        resultLabel = new Label();
        SendToServerBT = new Label();
        FromServerBT = new Label();
        comparisonLabel = new Label();

        // Xử lý sự kiện khi nhấn nút "Gửi"
        sendButton.setOnAction(event -> {
            try {
                sendToServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Định dạng layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(inputLabel, inputField, sendButton, resultLabel, SendToServerBT,FromServerBT,comparisonLabel);

        // Tạo scene và hiển thị cửa sổ
        Scene scene = new Scene(layout, 500, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendToServer() throws IOException {
        // Tạo InputStream từ bàn phím
        UUID uuid = UUID.randomUUID();
        String stringToServer = inputField.getText();

        // Tạo socket cho client kết nối đến server qua ID address và port number
        Socket clientSocket = new Socket("127.0.0.1", 6543);

        // Tạo OutputStream nối với socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        // Tạo inputStream nối với socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));

        // Gửi chuỗi ký tự tới server thông qua outputStream đã nối với Socket (ở trên)
        String dataToSend = uuid.toString() + " " + stringToServer;
        outToServer.writeBytes(dataToSend + "\n");

        // Đọc tin từ Server thông qua InputSteam đã nối với socket
        String stringFromServer = inFromServer.readLine();

        // In kết quả ra màn hình
//        System.out.println("Send to Server: " + dataToSend);
        SendToServerBT.setText("Send to Server: " + dataToSend);

        String stringToCut = " (Kết nối thành công!)";
        int index = stringFromServer.indexOf(stringToCut);
        String modifiedString = "";
        if (index != -1) {
            modifiedString = stringFromServer.substring(0, index);
        }
        FromServerBT.setText("From Server    : " + modifiedString);

        if (modifiedString.equals(dataToSend)) {
            comparisonLabel.setText("Hai chuỗi giống nhau!");
        } else {

            comparisonLabel.setText("Hai chuỗi khác nhau!");
        }

        // Hiển thị kết quả lên giao diện
        resultLabel.setText("Chuỗi trả về: " + stringFromServer);

        // Đóng liên kết
        clientSocket.close();
    }
}