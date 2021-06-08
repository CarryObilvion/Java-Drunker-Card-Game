package src.source;

// масти
public enum CardSuits {
    Diamonds("Бубны"),
    Hearts("Черви"),
    Spades("Пики"),
    Clubs("Трефы");

    // описание
    private final String description;

    CardSuits(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
