package dev.bti.chrona.androidsdk.common;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pair<K, V> implements Parcelable {
    public static final Creator<Pair<?, ?>> CREATOR = new Creator<>() {
        @Override
        public Pair<?, ?> createFromParcel(Parcel in) {
            return new Pair<>(in);
        }

        @Override
        public Pair<?, ?>[] newArray(int size) {
            return new Pair[size];
        }
    };
    K one;
    V two;

    @SuppressWarnings("unchecked")
    protected Pair(Parcel in) {
        this.one = (K) in.readValue(getClass().getClassLoader());
        this.two = (V) in.readValue(getClass().getClassLoader());
    }

    public static <K, V> Pair<K, V> of(K one, V two) {
        return new Pair<>(one, two);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(one);
        dest.writeValue(two);
    }
}
