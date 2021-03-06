package fr.xebia.essentials.ui.list;

import fr.xebia.essentials.core.Context;
import fr.xebia.essentials.model.Card;
import fr.xebia.essentials.model.JsonData;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CardsListController implements Initializable {

    @FXML private ListView<Card> listView;
    private ObservableList<Card> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setListView();
    }

    private void setListView() {
        List<Card> cards = Context.INSTANCE.getCards();
        observableList.setAll(cards.stream().filter(card -> !card.isDeprecated()).collect(Collectors.toList()));
        listView.setItems(observableList);
        listView.setCellFactory(param -> new CardsListItem());

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), listView);
            scaleTransition.setToX(2f);
            scaleTransition.setToY(2f);
            scaleTransition.setOnFinished(e -> {
                Context.INSTANCE.setSelectedCard(newValue);
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/card_detail.fxml"));
                    Context.INSTANCE.getPrimaryStage().setScene(new Scene(root));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            scaleTransition.play();
        });
    }
}
