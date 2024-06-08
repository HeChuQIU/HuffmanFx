package net.hechu.huffmanfx;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Controller {
    static final String OUTPUT_TIP = "请打开txt文件或在左侧文本框输入文本";

    @FXML
    private Button openFileButton;

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    private final PublishSubject<String> inputChangedSubject = PublishSubject.create();

    @FXML
    public void initialize() {
        inputText.textProperty().addListener((observable, oldValue, newValue) -> {
            inputChangedSubject.onNext(newValue);
        });

        Disposable disposable = inputChangedSubject.debounce(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(text -> {
                    if (text.isEmpty()) return OUTPUT_TIP;
                    else return getOutput(text);
                })
                .subscribe(this::setOutputText);

        outputText.setText(OUTPUT_TIP);
    }

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(openFileButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                List<String> lines = Files.readAllLines(selectedFile.toPath());

                inputText.clear();

                for (String line : lines) {
                    inputText.appendText(line + "\n");
                }
            } catch (IOException e) {
                inputText.setText("打开文件时发生错误: " + e.getMessage());
            }
        }
    }

    private String getOutput(String text) {
        Map<Character, String> huffmanCode = HuffmanCoding.encode(text);
        Map<Character, Double> characterFrequency = huffmanCode.keySet().stream().collect(
                Collectors.toMap(c -> c, c -> (double) Collections.frequency(text.chars().mapToObj(i -> (char) i).collect(Collectors.toList()), c) / text.length()));

        StringBuilder sb = new StringBuilder();
        sb.append("字符\t频率\tHuffman编码\n");
        for (Map.Entry<Character, Double> entry : characterFrequency.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).toList()) {
            Character ch = entry.getKey();
            String escapedCh = String.valueOf(ch);
            if (ch <= 127)
                escapedCh = StringEscapeUtils.escapeJava(String.valueOf(ch));
            sb
                    .append(escapedCh).append("\t")
                    .append(String.format("%.4f", entry.getValue())).append("\t")
                    .append(huffmanCode.get(ch)).append("\n");
        }

        return sb.toString();
    }

    private void setOutputText(String text) {
        outputText.setText(text);
    }
}