import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ManageAccounts;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("main started");
        ManageAccounts manager = new ManageAccounts();
        manager.start();
    }
}
