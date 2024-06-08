module net.hechu.huffmanfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens net.hechu.huffmanfx to javafx.fxml;
    exports net.hechu.huffmanfx;
}