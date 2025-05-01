package beadando.isports_app.utils.callbacks;

import androidx.annotation.Nullable;

public interface FirebaseAuthCallback<T> {
    void onSuccess(@Nullable T result);
    void onFailure(Exception e);
}
