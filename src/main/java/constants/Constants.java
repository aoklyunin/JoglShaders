package constants;

/**
 * Класс констант
 */
public class Constants {
    /**
     * константа пути
     */
    public static final String RESOURCE_PATH = "src/main/resources/";

    /**
     * кол-во доступных логических ядер процессора
     */
    public static final int AVAILABLE_PROCESSOR_CNT;

    static {
        AVAILABLE_PROCESSOR_CNT = Runtime.getRuntime().availableProcessors();
    }

    /**
     * Конструктор для запрета наследования
     */
    private Constants() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
