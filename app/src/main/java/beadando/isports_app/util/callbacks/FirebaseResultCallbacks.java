package beadando.isports_app.util.callbacks;

import androidx.annotation.Nullable;

public interface FirebaseResultCallbacks<T, E> {
    void onSuccess(T result, @Nullable E extra);
    void onFailure(Exception e);
}
