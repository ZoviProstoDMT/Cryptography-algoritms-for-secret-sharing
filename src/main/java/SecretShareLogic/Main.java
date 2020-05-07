package SecretShareLogic;

public class Main {
    static ShamirsSecretSharing shamirsSecretSharing = new ShamirsSecretSharing();
    static FeldmanShamirsSecretSharing feldmanShamirsSecretSharing = new FeldmanShamirsSecretSharing();

    public static void main(String[] args) {
        shamirsSecretSharing.start();
        feldmanShamirsSecretSharing.start();

    }
}
