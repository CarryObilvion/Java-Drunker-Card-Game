package src.source;

// номиналы карт
public enum CardDenominations {
    Two("Двойка", 0),
    Three("Тройка", 1),
    Four("Четверка", 2),
    Five("Пятерка", 3),
    Six("Шестерка", 4),
    Seven("Семерка", 5),
    Eight("Восмерка", 6),
    Nine("Девятка", 7),
    Ten("Десятка", 8),
    Jack("Валет", 9),
    Queen("Дама", 10),
    King("Король", 11),
    Ace("Туз", 12);


    // описание
    private final String description;
    private final int priority;

    // описание и приоритет
    CardDenominations(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
