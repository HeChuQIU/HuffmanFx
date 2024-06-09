module net.hechu.huffmanfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires io.reactivex.rxjava3;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;

    opens net.hechu.huffmanfx to javafx.fxml;
    exports net.hechu.huffmanfx;
}