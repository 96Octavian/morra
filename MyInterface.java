import java.rmi.RemoteException;
public interface MyInterface<T> {
    void accept(T t) throws RemoteException;
}