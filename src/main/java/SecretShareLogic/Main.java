package SecretShareLogic;

public class Main {
    static ShamirsSecretSharing shamirsSecretSharing = new ShamirsSecretSharing();
    static FeldmanShamirsSecretSharing feldmanShamirsSecretSharing = new FeldmanShamirsSecretSharing();
    static VerifiableSecretSharing verifiableSecretSharing = new VerifiableSecretSharing();

    public static void main(String[] args) {
        //shamirsSecretSharing.start();
        verifiableSecretSharing.startMethod();
    }
}
