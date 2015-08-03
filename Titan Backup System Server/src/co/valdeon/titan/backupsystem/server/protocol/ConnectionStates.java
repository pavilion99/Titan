package co.valdeon.titan.backupsystem.server.protocol;

public enum ConnectionStates {

    WAITING(0),
    USERNAME_REQUESTED(1),
    USERNAME_REREQUESTED(0),
    PASSWORD_REQUESTED(2),
    PASSWORD_REREQUESTED(1),
    RETRY(2),
    DIR(3),
    WAIT(4),
    DONE(5);

    int value;

    ConnectionStates(int a) {
        this.value = a;
    }

}
