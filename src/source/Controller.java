package src.source;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class Controller {
    @FXML
    private Button firstPlayerPickupButton;
    @FXML
    private Button secondPlayerPickupButton;
    @FXML
    private ListView<String> playedCardList;
    @FXML
    private Button firstPlayerMoveButton;
    @FXML
    private Button secondPlayerMoveButton;
    @FXML
    private TextField firstPlayerName;
    @FXML
    private TextField secondPlayerName;
    @FXML
    private CheckBox isBigDeckCheckbox;
    @FXML
    private Label firstPlayerCardCount;
    @FXML
    private Label secondPlayerCardCount;

    private boolean isFirstPlayerTurn;

    private final Stack<Card> playedCards = new Stack<>();
    // самая простая реализация очереди в яве
    private final LinkedList<Card> firstPlayerDeck = new LinkedList<>();
    private final LinkedList<Card> secondPlayerDeck = new LinkedList<>();

    @FXML
    public void initialize() {
    }

    // заполнение колоды и её перемешивание
    @FXML
    private void dealCards() {
        firstPlayerDeck.clear();
        secondPlayerDeck.clear();
        LinkedList<Card> deck = generateFullDeck(isBigDeckCheckbox.isSelected());

        // рандомно перемешивает коллекцию
        Collections.shuffle(deck);

        // раздаем перемешанные карты игрокам
        while (!deck.isEmpty()) {
            // заполняем имена
            Card first = deck.pop();
            Card second = deck.pop();
            first.setOwner(firstPlayerName.getText());
            second.setOwner(secondPlayerName.getText());

            firstPlayerDeck.add(first);
            secondPlayerDeck.add(second);
        }

        updateCardCount();

        // шанс, кому выпадает первый ход
        int turn = (int) Math.round(Math.random());
        if (turn == 0) {
            isFirstPlayerTurn = true;
            firstPlayerMoveButton.setVisible(true);
            secondPlayerMoveButton.setVisible(false);
        } else {
            isFirstPlayerTurn = false;
            secondPlayerMoveButton.setVisible(true);
            firstPlayerMoveButton.setVisible(false);
        }

        firstPlayerName.setDisable(true);
        secondPlayerName.setDisable(true);
    }

    @FXML
    private void firstPlayerPickup() {
        while (!playedCards.isEmpty()) {
            firstPlayerDeck.addLast(playedCards.pop());
        }

        updateCardCount();

        showTurnButtons();

        firstPlayerPickupButton.setVisible(false);
        secondPlayerPickupButton.setVisible(false);
    }

    @FXML
    private void secondPlayerPickup() {
        while (!playedCards.isEmpty()) {
            secondPlayerDeck.addLast(playedCards.pop());
        }

        updateCardCount();

        showTurnButtons();

        firstPlayerPickupButton.setVisible(false);
        secondPlayerPickupButton.setVisible(false);
    }

    private void hideTurnButtons() {
        firstPlayerMoveButton.setVisible(false);
        secondPlayerMoveButton.setVisible(false);
    }

    private void showTurnButtons() {
        firstPlayerMoveButton.setVisible(isFirstPlayerTurn);
        secondPlayerMoveButton.setVisible(!isFirstPlayerTurn);
    }

    // переключаем ходы
    private void toggleTurn() {
        isFirstPlayerTurn = !isFirstPlayerTurn;
    }

    @FXML
    private void firstPlayerMove() {
        if (firstPlayerDeck.isEmpty()) {
            return;
        }
        Card nextCard = firstPlayerDeck.pop();
        playedCards.push(nextCard);

        toggleTurn();
        showTurnButtons();

        boolean result = closeTurn();

        updateCardCount();
    }

    @FXML
    private void secondPlayerMove() {
        if (secondPlayerDeck.isEmpty()) {
            return;
        }
        Card nextCard = secondPlayerDeck.pop();
        playedCards.push(nextCard);

        toggleTurn();
        showTurnButtons();

        boolean result = closeTurn();

        updateCardCount();
    }

    // завершить ход
    private boolean closeTurn() {
        // пытаемся закрыть ход только если количество карт в стопке четное
        if (playedCards.size() % 2 == 0) {
            // берем две карты
            Card first = playedCards.pop();
            Card second = playedCards.peek();

            // возвращаем карту назад
            playedCards.push(first);

            // проверяем их приоритет
            if (first.getDenomination().getPriority() > second.getDenomination().getPriority()) {

                // если первая карта - туз, а вторая - двойка или шестерка
                if (first.getDenomination().getPriority() == 12 &&
                        second.getDenomination().getPriority() == 4 ||
                        second.getDenomination().getPriority() == 0) {
                    // если вторая карта - принадлежит первому игроку
                    // то первый выиграл иначе выиграл второй
                    turnWin(second.getOwner().equals(firstPlayerName.getText()));
                    return true;
                }

                // остальные случаи, когда приоритет первой карты выше второй карты
                // если карта первого - выиграл первый
                // иначе - второй
                turnWin(first.getOwner().equals(firstPlayerName.getText()));

                hideTurnButtons();
                return true;
            } else if (first.getDenomination().getPriority() < second.getDenomination().getPriority()) {
                // если приоритет первой карты ниже, чем у второй

                // и если первая карта - двойка или шестерка, а вторая - туз, то выиграла первая
                if (first.getDenomination().getPriority() == 0 ||
                        first.getDenomination().getPriority() == 4 &&
                                second.getDenomination().getPriority() == 12) {
                    // если первая карта принадлежит первому игроку - то он выиграл
                    turnWin(first.getOwner().equals(firstPlayerName.getText()));
                    return true;
                }

                turnWin(second.getOwner().equals(firstPlayerName.getText()));

                hideTurnButtons();
                return true;
            } else if (first.getDenomination().getPriority() == second.getDenomination().getPriority()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private void turnWin(boolean isFirstPlayerWin) {
        firstPlayerPickupButton.setVisible(isFirstPlayerWin);
        secondPlayerPickupButton.setVisible(!isFirstPlayerWin);
    }

    private LinkedList<Card> generateFullDeck(boolean isBigDeck) {
        LinkedList<Card> result = new LinkedList<>();

        // по всем мастям (каждой масти по 4 карты)
        for (CardSuits suit : CardSuits.values()) {
            // номиналы
            for (CardDenominations denomination : CardDenominations.values()) {
                // колода может быть 36 или 52 карты (джокеры не используются)
                // если не isBigDeck , то колода 36 карт...
                if (!isBigDeck) {
                    // ...соответственно пропускаем 2,3,4,5 значения карт
                    if (denomination == CardDenominations.Two ||
                            denomination == CardDenominations.Three ||
                            denomination == CardDenominations.Four ||
                            denomination == CardDenominations.Five) {
                        continue;
                    }
                }
                // добавляем в колоду
                result.add(new Card(suit, denomination));
            }
        }

        return result;
    }

    private void updateCardCount() {

        if (firstPlayerDeck.size() == 0) {
            hideTurnButtons();
            playedCardList.getItems().clear();
            playedCardList.getItems().add(firstPlayerName.getText() + " выиграл!");
            playedCardList.getItems().add("Нажмите \"Раздать\", чтобы начать заново!");
            return;
        }

        if (secondPlayerDeck.size() == 0) {
            hideTurnButtons();
            playedCardList.getItems().clear();
            playedCardList.getItems().add(secondPlayerName.getText() + " выиграл!");
            playedCardList.getItems().add("Нажмите \"Раздать\", чтобы начать заново!");
            return;
        }

        // обновляем количество карт игроков
        firstPlayerCardCount.setText(String.valueOf(firstPlayerDeck.size()));
        secondPlayerCardCount.setText(String.valueOf(secondPlayerDeck.size()));

        // очищаем лист на экране и заполняем "битой"
        playedCardList.getItems().clear();
        playedCards.forEach(card -> playedCardList.getItems().add(card.toString()));
    }
}
