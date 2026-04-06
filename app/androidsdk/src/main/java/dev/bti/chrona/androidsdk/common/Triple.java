package dev.bti.chrona.androidsdk.common;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Triple<K, T, X> implements Parcelable {
    public static final Creator<Triple<?, ?, ?>> CREATOR = new Creator<>() {
        @Override
        public Triple<?, ?, ?> createFromParcel(Parcel in) {
            return new Triple<>(in);
        }

        @Override
        public Triple<?, ?, ?>[] newArray(int size) {
            return new Triple[size];
        }
    };
    K one;
    T two;
    X three;

    @SuppressWarnings("unchecked")
    protected Triple(Parcel in) {
        this.one = (K) in.readValue(getClass().getClassLoader());
        this.two = (T) in.readValue(getClass().getClassLoader());
        this.three = (X) in.readValue(getClass().getClassLoader());
    }

    public static <K, T, X> Triple<K, T, X> of(K one, T two, X three) {
        return new Triple<>(one, two, three);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(one);
        dest.writeValue(two);
        dest.writeValue(three);
    }

}
